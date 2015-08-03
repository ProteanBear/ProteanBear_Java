package pb.thread;

/**
 * 缓存池使用监听线程类<br/>
 * 停止监听，线程即为死亡状态<br/>
 * 1.01 —— 添加isBusy属性，将原来的线程类中控制线程状态的属性移动到实现类中。<br/>
 * 1.00 —— 设置当前线程类为监听线程，继承run方法，在run方法中使用死循环；使用运行标志位来处理线程运行和死亡<br/>
 *
 * @author proteanBear(马强)
 * @version 1.01 2011/08/12
 */
public abstract class AbstractListenerRunnable implements ListenerRunnable
{
    /**
     * 域(私有)<br/>
     * 名称:    error<br/>
     * 描述:    记录进行操作时出现的错误信息<br/>
     */
    private String error;

    /**
     * 域(私有)<br/>
     * 名称:    sleepTime<br/>
     * 描述:    设置监听线程在空闲时的休眠时间<br/>
     */
    private long sleepTime=10;

    /**
     * 域(私有)<br/>
     * 名称:    isListen<br/>
     * 描述:    记录当前线程是否在监听中<br/>
     */
    private boolean isListen=true;

    /**
     * 域(私有)<br/>
     * 名称:    isStop<br/>
     * 描述:    停止线程标识<br/>
     */
    private boolean isStop=false;

    /**
     * 域(私有)<br/>
     * 名称:    isBusy<br/>
     * 描述:    标识线程是否正在使用中<br/>
     */
    private boolean isBusy;

    /**
     * 更改器(公共)<br/>
     * 目标：   error
     *
     * @param error - 错误信息
     */
    public final void setError(String error)
    {
        this.error=error;
    }

    /**
     * 访问器(公共)<br/>
     * 目标：   error<br/>
     *
     * @return String - 错误信息
     */
    public String getError()
    {
        return this.error;
    }

    /**
     * 更改器(公共)<br/>
     * 目标：   sleepTime
     *
     * @param sleepTime - 线程睡眠时间
     */
    public final void setSleepTime(long sleepTime)
    {
        this.sleepTime=sleepTime;
    }

    /**
     * 访问器(公共)<br/>
     * 目标：   sleepTime<br/>
     *
     * @return long - 线程睡眠时间
     */
    public long getSleepTime()
    {
        return this.sleepTime;
    }

    /**
     * 访问器（公共）<br/>
     * 目标：   isBusy<br/>
     *
     * @return boolean - 线程是否正在使用中
     */
    @Override
    public boolean isBusy()
    {
        return this.isBusy;
    }

    /**
     * 更改器（公共、不可继承）<br/>
     * 目标：   isBusy<br/>
     *
     * @param busy - 线程使用标识
     */
    @Override
    public final void setBusy(boolean busy)
    {
        this.isBusy=busy;
        if(this.isBusy())
        {
            this.startListen();
        }
        else
        {
            this.stopListen();
        }
    }

    /**
     * 方法（公共）<br/>
     * 名称:    startListen<br/>
     * 描述:    开始监听线程<br/>
     */
    @Override
    public void startListen()
    {
        this.isListen=true;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    stopListen<br/>
     * 描述:    停止当前监听<br/>
     */
    @Override
    public void stopListen()
    {
        this.isListen=false;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    stopThread<br/>
     * 描述:    中断监听线程<br/>
     */
    @Override
    public void stopThread()
    {
        this.isStop=true;
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    beforeStop<br/>
     * 描述:    停止监听线程前进行的处理,此处为空实现<br/>
     */
    @Override
    public void beforeStop()
    {
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    Execute<br/>
     * 描述:    线程运行监听调用方法。<br/>
     */
    protected abstract void Execute();

    /**
     * 方法（公共、重载）<br/>
     * 名称:    run<br/>
     * 描述:    线程运行执行方法<br/>
     * 使用死循环，执行监听代码
     */
    @Override
    public void run()
    {
        try
        {
            //通过中断标识控制循环
            while(!this.isStop)
            {
                if(this.isListen) this.Execute();
                Thread.sleep(this.getSleepTime());
            }
        }
        catch(Exception ex)
        {
            this.setError(ex.toString());
        }
    }
}