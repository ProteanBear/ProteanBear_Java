package pb.net.socket;

import pb.pool.AbstractCachePool;
import pb.thread.ListenerThread;

/**
 * Socket连接端口监听器缓存池类。
 * 1.00 —— 继承自AbstractCachePool使用Map方式缓存监听器对象
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/06/22
 */
public class SocketServerListenerPool extends AbstractCachePool
{
    /**
     * 域(私有)<br/>
     * 名称:    currentListenPort<br/>
     * 描述:    记录当前监听的端口，用于显示，端口间以逗号分隔<br/>
     */
    private String currentListenPort;

    /**
     * 构造函数<br/>
     * 描述：   初始化域。
     */
    public SocketServerListenerPool()
    {
        super();
        this.currentListenPort="";
        this.initPool();
    }

    /**
     * 方法（公共）<br/>
     * 名称:    addListener<br/>
     * 描述:    添加新的监听器线程到缓冲池中（值名对）<br/>
     *
     * @param port     - 监听器端口
     * @param listener - 监听器对象
     * @return boolean - 添加成功，返回true；否则，返回false。
     */
    public boolean addListener(int port,ListenerThread listener)
    {
        boolean success=this.addToPool(new Integer(port),listener);
        if(success) this.addToListenPortString(port);
        return success;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    deleteListener<br/>
     * 描述:    将指定端口的监听器对象从缓冲池中删除（值名对）<br/>
     *
     * @param port - 监听器端口
     * @return boolean - 删除成功，返回true；否则，返回false。
     */
    public boolean deleteListener(int port)
    {
        boolean success=this.deleteFromPool(new Integer(port));
        if(success) this.generateListenPortString();
        return success;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    updateListener<br/>
     * 描述:    更新缓冲池中指定端口的监听器为新的监听器对象（值名对）<br/>
     *
     * @param port     - 监听器端口
     * @param listener - 新的监听器对象
     * @return boolean - 添加成功，返回true；否则，返回false。
     */
    public boolean updateListener(int port,ListenerThread listener)
    {
        return this.editInPool(new Integer(port),listener);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    getListener<br/>
     * 描述:    从缓存池中获取指定端口的监听器对象（值名对）<br/>
     *
     * @param port - 监听器端口
     * @return ListenerThread - 监听器对象
     */
    public ListenerThread getListener(int port)
    {
        return (ListenerThread)this.getFromPool(new Integer(port));
    }

    /**
     * 方法（公共）<br/>
     * 名称:    containListener<br/>
     * 描述:    判断缓冲池中是否有指定的端口，如果有则返回true；<br/>
     *
     * @param port - 端口
     * @return boolean - 指定的端口是否已经使用
     */
    public boolean containListener(int port)
    {
        return this.containMap(new Integer(port));
    }

    /**
     * 方法（公共）<br/>
     * 名称:    displayListenPort<br/>
     * 描述:    显示当前缓冲池中监听器所有的监听端口，端口之间用逗号分隔<br/>
     *
     * @return String - 监听端口字符串，端口之间用逗号分隔
     */
    public String displayListenPort()
    {
        return this.currentListenPort;
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
            ListenerThread thread=(ListenerThread)this.getFromPool(key);
            if(thread!=null) thread.stopThread();
        }
        this.clearPool();
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
     * 方法（私有）<br/>
     * 名称:    generateListenPortString<br/>
     * 描述:    生成当前监听端口显示字符串<br/>
     */
    private void generateListenPortString()
    {
        this.currentListenPort="";
        for(Object key : this.keySet())
        {
            this.addToListenPortString((Integer)key);
        }
    }

    /**
     * 方法（私有）<br/>
     * 名称:    addToListenPortString<br/>
     * 描述:    添加端口到监听端口显示字符串中<br/>
     *
     * @param port - 监听器端口
     */
    private void addToListenPortString(int port)
    {
        if(this.currentListenPort==null) this.currentListenPort="";
        if(!"".equals(this.currentListenPort)) this.currentListenPort+=",";
        this.currentListenPort+=port;
    }
}
