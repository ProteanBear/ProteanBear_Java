package pb.thread;

/**
 * 监听线程线程类<br/>
 * 1.03 —— 去除isBusy属性，修改此属性到runnable接口中。<br/>
 * 1.02 —— 增加线程类属性清理方法。<br/>
 * 1.01 —— 修改当前使用的线程接口对象为可保护的，便于子类调用<br/>
 * 1.00 —— 增加阻塞状态标识，当调用sleep时设置阻塞标识为true；增加是否在阻塞状态的判断方法<br/>
 *
 * @author proteanBear(马强)
 * @version 1.03 2011/08/12
 */
public class ListenerThread extends Thread
{
    /**
     * 域(私有)<br/>
     * 名称:    runnable<br/>
     * 描述:    记录当前使用的线程接口对象<br/>
     */
    protected ListenerRunnable runnable;

    /**
     * 构造函数
     */
    public ListenerThread(ListenerRunnable runnable)
    {
        super(runnable);
        this.runnable=runnable;
        //默认设置isBusy为false
        this.setBusy(false);
    }

    /**
     * 访问器（公共）<br/>
     * 目标：   isBusy<br/>
     *
     * @return boolean - 线程是否正在使用中
     */
    public boolean isBusy()
    {
        return this.runnable.isBusy();
    }

    /**
     * 更改器（公共、不可继承）<br/>
     * 目标：   isBusy<br/>
     *
     * @param busy - 线程使用标识
     */
    public final void setBusy(boolean busy)
    {
        this.runnable.setBusy(busy);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    stopListen<br/>
     * 描述:    停止监听<br/>
     */
    public void stopListen()
    {
        this.runnable.stopListen();
    }

    /**
     * 方法（公共）<br/>
     * 名称:    startListen<br/>
     * 描述:    开始监听，如果线程未激活则激活线程<br/>
     */
    public void startListen()
    {
        this.runnable.startListen();
        if(!this.isAlive()) this.start();
    }

    /**
     * 方法（公共）<br/>
     * 名称:    stopThread<br/>
     * 描述:    中断监听线程<br/>
     */
    public void stopThread()
    {
        this.stopListen();
        this.runnable.beforeStop();
        this.runnable.stopThread();
    }

    /**
     * 方法（公共）<br/>
     * 名称:    clear<br/>
     * 描述:    清除线程属性值<br/>
     */
    public void clear()
    {
        this.runnable=null;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    getRunnableObject<br/>
     * 描述:    获取监听器接口实现类对象<br/>
     *
     * @return ListenerRunnable - 监听器接口实现类对象
     */
    public ListenerRunnable getRunnableObject()
    {
        return this.runnable;
    }
}