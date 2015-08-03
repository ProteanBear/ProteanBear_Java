package pb.thread;

import pb.pool.AbstractListCachePool;

/**
 * 线程池类<br/>
 * 1.00 —— 实现线程池的基本方法，需指定Runnable接口实现类，使用clone方法创建多个线程<br/>
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/06/15
 */
public class ThreadPool extends AbstractListCachePool<ListenerThread>
{
    /**
     * 域(私有)<br/>
     * 名称:    runnable<br/>
     * 描述:    记录实现ListenerRunnable接口的线程类对象<br/>
     */
    private ListenerRunnable runnable;

    /**
     * 构造函数（无参数）<br/>
     */
    private ThreadPool()
    {
        //默认连接池名称
        this.setPoolName("默认线程池");
        //默认初始和最小池大小
        this.setInitialConnectNumber(8);
        //默认池最大尺寸
        this.setMaxConnectNumber(32);
        //默认池调整大小数量
        this.setIncrementalConnectNumber(2);
        //默认等待时间-250毫秒
        this.setWaitTime(250);
        //最长等待时间-60000毫秒
        this.setMaxWaitTime(60000);
    }

    /**
     * 构造函数<br/>
     * 描述：   指定实现Runnable接口的线程类对象
     *
     * @param runnable - 线程类对象
     */
    public ThreadPool(ListenerRunnable runnable)
    {
        this();
        this.setRunnable(runnable);
        //this.initPool();
    }

    /**
     * 方法（公共、不可继承）<br/>
     * 名称:    setRunnable<br/>
     * 描述:    设置当前线程池使用的线程类对象<br/>
     *
     * @param runnable - 线程类对象
     * @return ThreadPool - 返回当前对象本身
     */
    public final ThreadPool setRunnable(ListenerRunnable runnable)
    {
        this.runnable=runnable;
        return this;
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    createObject<br/>
     * 描述:    创建一个线程处理对象，并返回此对象<br/>
     * 使用指定线程实现类的对象生成方法，构建线程实现类
     *
     * @return ListenerThread - 线程处理对象
     */
    @Override
    protected ListenerThread createObject()
    {
        ListenerThread thread=new ListenerThread(runnable.generateObject());
        thread.start();
        if(!thread.isAlive()) return null;
        return thread;
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    findFree<br/>
     * 描述:    查找一个空闲的线程处理对象<br/>
     *
     * @return ListenerThread - 返回一个可用的线程处理对象
     */
    @Override
    protected ListenerThread findFree()
    {
        ListenerThread free=null;

        //遍历所有的对象，看是否有可用的连接
        for(int i=0;i<this.size();i++)
        {
            free=(ListenerThread)this.getFromPool(i);
            if(!free.isBusy())
            {
                free.setBusy(true);

                //找到可用连接，跳出循环
                break;
            }
            //重置结果对象为null
            free=null;
        }

        return free;
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    release<br/>
     * 描述:    此函数返回一个数据库连接到连接池中，并把此连接置为空闲。<br/>
     * 所有使用连接池获得的数据库连接均应在不使用此连接时返回它。
     *
     * @param thread - 需返回到连接池中的连接器对象
     */
    @Override
    protected void release(ListenerThread thread)
    {
        //遍历查找连接池中相同的连接器,返回索引
        //如果索引为-1（即没有找到指定对象），返回null
        int index=this.indexOfList(thread);
        if(index==-1)
        {
            this.setError("未找到指定的处理线程对象！");
        }
        else
        {
            thread.setBusy(false);
            this.editInPool(index,thread);
        }
    }

    /**
     * 方法（公共）<br/>
     * 名称:    initThreadPool<br/>
     * 描述:    初始化线程池，创建最小池大小（initialConnectNumber）的连接<br/>
     */
    public void initThreadPool()
    {
        this.initPool();
    }

    /**
     * 方法（公共）<br/>
     * 名称:    getThread<br/>
     * 描述:    通过调用 getFren() 函数返回一个空闲的处理线程<br/>
     * 如果当前没有空闲的处理线程，并且更多的处理线程不能创建（如缓存池大小的限制），此函数等待一会再尝试获取。
     *
     * @return ListenerThread - 返回一个空闲的处理线程对象
     */
    public ListenerThread getThread()
    {
        return this.get();
    }

    /**
     * 方法（公共）<br/>
     * 名称:    returnThread<br/>
     * 描述:    此函数返回一个处理线程到线程池中，并把此线程置为空闲。<br/>
     * 所有使用线程池获得的线程均应在不使用此连接时返回它。
     *
     * @param thread - 需返回到线程池中的线程对象
     */
    public void returnThread(ListenerThread thread)
    {
        //遍历查找连接池中相同的连接器,返回索引
        //如果索引为-1（即没有找到指定对象），返回null
        int index=this.indexOfList(thread);
        if(index==-1)
        {
            this.setError("未找到指定的连接器对象！");
        }
        else
        {
            thread.setBusy(false);
            this.editInPool(index,thread);
        }
    }

    /**
     * 方法（公共）<br/>
     * 名称:    clearThreadPool<br/>
     * 描述:    清除线程池中的所有线程<br/>
     */
    public void clearThreadPool()
    {
        //如果连接池中没有连接器对象，退出
        if(this.size()==0)
        {
            this.setError("当前连接池中没有数据库连接！");
            return;
        }

        try
        {
            //遍历连接池中所有连接器,从尾部开始删除
            for(int i=this.size()-1;i>-1;i--)
            {
                ListenerThread thread=(ListenerThread)this.getFromPool(i);

                //如果连接正在使用中，等待5秒钟，直接中断线程
                if(thread.isBusy()) wait(5000);
                thread.stopThread();
                this.deleteFromPool(i);
            }
        }
        catch(InterruptedException ex)
        {
            this.setError(ex.toString());
        }
        finally
        {
            this.clearPool();
        }
    }
}