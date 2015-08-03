package pb.pool;

/**
 * 通用缓存池类<br>
 * 1.00 —— 实现列表缓存池抽象父类的基本处理方法<br>
 *
 * @param <T>
 * @author proteanBear(马强)
 * @version 1.00 2011/06/16
 */
public abstract class AbstractListCachePool<T> extends AbstractCachePool
{
    /**
     * 域(私有)<br>
     * 名称:    initialConnectNumber<br>
     * 描述:    初始和最小池大小<br>
     */
    private int initialConnectNumber;

    /**
     * 域(私有)<br>
     * 名称:    maxConnectNumber<br>
     * 描述:    池最大尺寸<br>
     */
    private int maxConnectNumber;

    /**
     * 域(私有)<br>
     * 名称:    incrementalConnectNumber<br>
     * 描述:    池调整大小数量<br>
     */
    private int incrementalConnectNumber;

    /**
     * 域(私有)<br>
     * 名称:    waitTime<br>
     * 描述:    记录当没有空闲连接时的线程等待时间,单位为毫秒<br>
     */
    private int waitTime;

    /**
     * 域(私有)<br>
     * 名称:    maxWaitTime<br>
     * 描述:    最长等待时间,单位为毫秒<br>
     */
    private int maxWaitTime;

    /**
     * 方法（受保护）<br>
     * 名称:    initPool<br>
     * 描述:    初始化缓存池<br>
     */
    @Override
    protected void initPool()
    {
        //初始化缓存数据列表
        this.initCacheList();

        //确保缓存池池没有创建
        if(this.size()>0) return;

        //创建初始池大小的数据对象
        this.createObjects(this.getInitialConnectNumber());
    }

    /**
     * 方法（受保护、抽象）<br>
     * 名称:    createObject<br>
     * 描述:    创建一个数据对象，并返回此数据对象<br>
     *
     * @return T - 数据对象
     */
    protected abstract T createObject();

    /**
     * 方法（受保护、抽象）<br>
     * 名称:    createObjects<br>
     * 描述:    创建指定数目的数据对象，并存入缓存池列表中<br>
     *
     * @param connectNumber - 要创建的数据库连接的数目
     * @return boolean - 创建成功，返回true；否则，返回false。
     */
    protected boolean createObjects(int connectNumber)
    {
        //记录创建是否成功
        boolean success=true;
        //记录创建成功的数目
        int connNumber=0;
        //记录错误信息
        String errorInfor="";

        //循环创建指定数目的数据库连接
        for(int i=0;i<connectNumber;i++)
        {
            //是否缓存池中的线程的数量己经达到最大？最大值由类成员 maxConnectNumber
            //指出，如果 maxConnectNumber 为 0 或负数，表示线程数量没有限制。
            //如果线程数己经达到最大，即退出。
            if(this.getMaxConnectNumber()>0 && this.size()>=this.getMaxConnectNumber())
            {
                success=false;
                break;
            }

            //创建一个线程处理对象
            T object=this.createObject();
            //如果线程对象不为空（即创建成功），添加线程处理对象到缓冲池中并增加正确连接的次数
            if(object!=null)
            {
                this.addToPool(object);
                connNumber++;
            }
            //如果连接出现错误，记录错误信息和连接序号
            else
            {
                errorInfor+="创建第["+i+"]个时出现错误："+this.getError()+"；\n";
                success=false;
            }
        }

        //如果创建失败，记录错误信息。
        if(!success)
        {
            errorInfor+="成功创建["+connNumber+"]个。\n"+errorInfor;
            this.setError(errorInfor);
        }

        return success;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getFree<br>
     * 描述:    本函数从连接池向量 connections 中返回一个可用的的数据对象，<br>
     * 如果当前没有可用的数据对象，本函数则根据 incrementalConnections 设置的值创建几个数据对象，并放入连接池中。<br>
     * 如果创建后，所有的连接仍都在使用中，则返回 null   <br>
     *
     * @return T - 返回一个可用的数据对象
     */
    protected T getFree()
    {
        T free=null;

        //从连接池中获得一个可用的数据库连接
        free=this.findFree();
        //如果目前连接池中没有可用的连接.创建一些连接
        if(free==null)
        {
            if(this.createObjects(this.getIncrementalConnectNumber()))
            {
                //重新从池中查找是否有可用连接
                //如果创建连接后仍获得不到可用的连接，则返回 null
                free=this.findFree();
            }
        }
        return free;
    }

    /**
     * 方法（受保护、抽象）<br>
     * 名称:    findFree<br>
     * 描述:    查找一个空闲的可用数据对象<br>
     *
     * @return T - 返回一个可用的数据对象
     */
    protected abstract T findFree();

    /**
     * 方法（受保护）<br>
     * 名称:    get<br>
     * 描述:    通过调用 getFree() 函数返回一个可用的数据对象<br>
     * 如果当前没有可用的数据对象，并且更多的数据对象不能创建（如缓存池大小的限制），此函数等待一会再尝试获取。
     *
     * @return T - 返回一个可用的数据库连接器对象
     */
    protected T get()
    {
        //确保连接池已经初始化
        this.initPool();

        //获取空闲的数据库连接器
        T free=this.getFree();

        //如果目前没有可以使用的连接，即所有的连接都在使用中
        //等待一段时间，再次获取
        //如果总等待时间大于最大等待时间，则返回null
        try
        {
            int totalWaitTime=0;
            while(free==null && totalWaitTime<this.getMaxWaitTime())
            {
                //等待一段时间
                this.wait(this.getWaitTime());
                totalWaitTime+=this.getWaitTime();

                //重新再试，直到获得可用的连接
                free=this.getFree();
            }
        }
        catch(InterruptedException ex)
        {
            this.setError(ex.toString());
        }

        return free;
    }

    /**
     * 方法（受保护、抽象）<br>
     * 名称:    release<br>
     * 描述:    此函数返回一个数据对象到缓存池中。<br>
     * 所有使用缓存池获得的数据对象均应在不使用此连接时返回它。
     *
     * @param object - 需返回到缓存池中的数据对象
     */
    protected abstract void release(T object);

    /**
     * 方法（公共）<br>
     * 名称:    wait<br>
     * 描述:    使程序等待给定的毫秒数<br>
     *
     * @param mSeconds - 给定的毫秒数
     * @throws InterruptedException 异常：
     */
    protected void wait(int mSeconds) throws InterruptedException
    {
        Thread.sleep(mSeconds);
    }

    /**
     * 访问器（公共）<br>
     * 目标：   incrementalConnectNumber<br>
     *
     * @return String - 池调整大小数量
     */
    public int getIncrementalConnectNumber()
    {
        return incrementalConnectNumber;
    }

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   incrementalConnectNumber<br>
     *
     * @param incrementalConnectNumber - 池调整大小数量
     */
    public final void setIncrementalConnectNumber
    (int incrementalConnectNumber)
    {
        this.incrementalConnectNumber=incrementalConnectNumber;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   initialConnectNumber<br>
     *
     * @return String - 初始和最小池大小
     */
    public int getInitialConnectNumber()
    {
        return initialConnectNumber;
    }

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   initialConnectNumber<br>
     *
     * @param initialConnectNumber - 初始和最小池大小
     */
    public final void setInitialConnectNumber(int initialConnectNumber)
    {
        this.initialConnectNumber=initialConnectNumber;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   maxConnectNumber<br>
     *
     * @return boolean - 池最大尺寸
     */
    public int getMaxConnectNumber()
    {
        return maxConnectNumber;
    }

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   maxConnectNumber<br>
     *
     * @param maxConnectNumber - 池最大尺寸
     */
    public final void setMaxConnectNumber(int maxConnectNumber)
    {
        this.maxConnectNumber=maxConnectNumber;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   waitTime<br>
     *
     * @return boolean - 等待时间,单位为毫秒
     */
    public int getWaitTime()
    {
        return waitTime;
    }

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   waitTime<br>
     *
     * @param waitTime - 等待时间,单位为毫秒
     */
    public final void setWaitTime(int waitTime)
    {
        this.waitTime=waitTime;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   maxWaitTime<br>
     *
     * @return boolean - 最长等待时间,单位为毫秒
     */
    public int getMaxWaitTime()
    {
        return maxWaitTime;
    }

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   maxWaitTime<br>
     *
     * @param maxWaitTime - 最长等待时间,单位为毫秒
     */
    public final void setMaxWaitTime(int maxWaitTime)
    {
        this.maxWaitTime=maxWaitTime;
    }
}
