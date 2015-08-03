package pb.net.socket;

import java.util.Set;

import pb.pool.AbstractCachePool;
import pb.thread.AbstractListenerRunnable;
import pb.thread.ListenerRunnable;
import pb.thread.ListenerThread;

/**
 * Socket数据服务管理线程池类。<br/>
 * 1.07 —— 增加缓存池键值遍历方法poolKeySet。<br/>
 * 1.06 —— 增加清理空线程的公共方法，便于外部管理调用；并修改清理空线程的相关方法。<br/>
 * 1.05 —— 更新监控线程中死亡线程的清理方法。<br/>
 * 1.04 —— 添加获取线程对象的getDataProcessor方法<br/>
 * 1.03 —— 增加获取和更改线程名称的方法<br/>
 * 1.02 —— 修改为适用Map方式缓存Socket数据处理器对象，便于随处适用索引值获取线程，并不受关闭的线程影响<br/>
 * 1.01 —— 添加renameKey方法，用于更新数据的键名<br/>
 * 1.00 —— 继承自AbstractCachePool使用List方式缓存Socket数据处理器对象<br/>
 *
 * @author proteanBear(马强)
 * @version 1.07 2012/02/10
 */
public class SocketDataProcessorPool extends AbstractCachePool
{
    /**
     * 域(私有)<br/>
     * 名称:    poolControlThread<br/>
     * 描述:    缓冲池调度线程，在设置的间隔时间后对缓冲池中的数据进行清理，发现有已断开的Socket连接便从缓冲池中清除此线程<br/>
     */
    private ListenerThread poolControlThread;

    /**
     * 构造函数<br/>
     * 描述：   初始化域。
     */
    public SocketDataProcessorPool()
    {
        super();
        this.initPool();
    }

    /**
     * 构造函数<br/>
     * 描述：   初始化域,设置清理空连接的间隔时间
     *
     * @param interval - 间隔时间,单位为毫秒
     */
    public SocketDataProcessorPool(long interval)
    {
        super();
        this.initPool();
        this.poolControlThread=new ListenerThread(new PoolControl(interval));
        this.poolControlThread.startListen();
    }

    /**
     * 方法（公共）<br/>
     * 名称:    addDataProcessor<br/>
     * 描述:    添加新的Socket数据处理线程到缓冲池中（值名对）<br/>
     *
     * @param key       - 线程索引
     * @param processor - 处理器对象
     * @return boolean - 添加成功，返回true；否则，返回false。
     */
    public boolean addDataProcessor(Object key,SocketDataProcessorThread processor)
    {
        return this.addToPool(key,processor);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    deleteDataProcessor<br/>
     * 描述:    将指定的处理器对象从缓冲池中删除（值名对）<br/>
     *
     * @param key - 线程索引
     * @return boolean - 删除成功，返回true；否则，返回false。
     */
    public boolean deleteDataProcessor(Object key)
    {
        return this.deleteFromPool(key);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    updateDataProcessor<br/>
     * 描述:    更新缓冲池中指定序号的处理器为新的监听器对象（值名对）<br/>
     *
     * @param key       - 线程索引
     * @param processor - 新的处理器对象
     * @return boolean - 添加成功，返回true；否则，返回false。
     */
    public boolean updateDataProcessor(Object key,SocketDataProcessorThread processor)
    {
        return this.editInPool(key,processor);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    getDataProcessor<br/>
     * 描述:    从缓存池中获取指定序号的处理器对象（值名对）<br/>
     *
     * @param key - 线程索引
     * @return SocketDataProcessorThread - 处理器对象
     */
    public SocketDataProcessorThread getDataProcessor(Object key)
    {
        return (SocketDataProcessorThread)this.getFromPool(key);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    containMap<br/>
     * 描述:    判断缓冲池中是否有指定的处理器对象，如果有则返回true；<br/>
     *
     * @param key - 线程索引
     * @return boolean - 指定的端口是否已经使用
     */
    public boolean containDataProcessor(Object key)
    {
        return this.containMap(key);
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    renameKey<br/>
     * 描述:    更新缓冲池中指定键名为新的键名<br/>
     *
     * @param oldKey - 被替换的键名
     * @param newKey - 替换的键名
     * @return boolean - 替换成功，返回true；否则，返回false。
     */
    public boolean renameKey(Object oldKey,Object newKey)
    {
        boolean success=this.editKeyInPool(oldKey,newKey);
        if(success)
        {
            SocketDataProcessorThread thread=this.getDataProcessor(newKey);
            thread.setThreadName(newKey);
        }
        return success;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    poolKeySet<br/>
     * 描述:    缓存池的键值序列，用于遍历缓存池<br/>
     *
     * @return Set - 遍历集合对象
     */
    public Set<Object> poolKeySet()
    {
        return this.keySet();
    }

    /**
     * 方法（公共）<br/>
     * 名称:    clear<br/>
     * 描述:    清空缓冲池<br/>
     */
    public void clear()
    {
        //遍历列表并停止所有处理器线程
        for(Object key : this.keySet())
        {
            SocketDataProcessorThread thread=(SocketDataProcessorThread)this.getFromPool(key);
            if(thread!=null) thread.stopThread();
        }
        this.clearPool();
    }

    /**
     * 方法（公共）<br/>
     * 名称:    clearErrorProcessor<br/>
     * 描述:    清理错误或空置的数据处理器<br/>
     */
    public void clearErrorProcessor()
    {
        for(Object key : keySet())
        {
            SocketDataProcessorThread processor=getDataProcessor(key);
            if(processor!=null)
            {
                if(processor.isAlive() && processor.isConnected())
                {
                    continue;
                }
                else
                {
                    processor.stopThread();
                    processor.clear();
                    processor=null;
                }
            }
            deleteDataProcessor(key);
        }
    }

    /**
     * 方法（公共）<br/>
     * 名称:    size<br/>
     * 描述:    缓冲池当前大小。<br/>
     *
     * @return int - 缓冲池大小
     */
    @Override
    public int size()
    {
        return super.size();
    }

    /**
     * 方法（受保护、抽象）<br/>
     * 名称:    initPool<br/>
     * 描述:    初始化缓冲池<br/>
     */
    @Override
    protected final void initPool()
    {
        this.initCacheMap();
    }

    /**
     * 数据处理器缓存池总控制线程类。<br/>
     * 在设置的间隔时间后对缓冲池中的数据进行清理，发现有已断开的Socket连接便从缓冲池中清除此线程。<br/>
     * 1.00 —— 实现基本的监控清理方法
     *
     * @author proteanBear(马强)
     * @version 1.00 2011/07/06
     */
    private class PoolControl extends AbstractListenerRunnable implements ListenerRunnable
    {
        /**
         * 构造函数<br/>
         * 描述：   初始化域，设置清理空连接的间隔时间
         *
         * @param interval - 间隔时间,单位为毫秒
         */
        public PoolControl(long interval)
        {
            this.setSleepTime(interval);
        }

        /**
         * 方法（受保护）<br/>
         * 名称:    Execute<br/>
         * 描述:    线程运行监听调用方法。<br/>
         */
        @Override
        protected void Execute()
        {
            clearErrorProcessor();
        }

        /**
         * 描述:    生成新的线程实现对象<br/>
         * 线程池使用，用于生成新的接口实现对象，此处未实现此方法
         *
         * @return ListenerRunnable - 新的线程实现对象
         */
        @Override
        public ListenerRunnable generateObject()
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}