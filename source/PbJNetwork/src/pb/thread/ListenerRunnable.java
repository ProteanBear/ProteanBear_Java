package pb.thread;

/**
 * 线程描述接口<br/>
 * 继承自Java系统线程处理接口Runnable，增加监听开始和停止方法，用于在监听线程中使用<br/>
 * 1.03 —— 增加线程状态处理方法声明
 * 1.02 —— 增加beforeStop
 * 1.01 —— 增加startListen、stopListen、stopThread方法
 * 1.00 —— 增加generateObject方法<br/>
 *
 * @author proteanBear(马强)
 * @version 1.03 2011/08/12
 */
public interface ListenerRunnable extends Runnable
{
    /**
     * 描述:    开始监听线程<br/>
     */
    void startListen();

    /**
     * 描述:    停止监听<br/>
     */
    void stopListen();

    /**
     * 描述:    生成新的线程实现对象<br/>
     * 线程池使用，用于生成新的接口实现对象
     *
     * @return ListenerRunnable - 新的线程实现对象
     */
    ListenerRunnable generateObject();

    /**
     * 描述:    停止监听线程前进行的处理<br/>
     */
    void beforeStop();

    /**
     * 描述:    中断监听线程<br/>
     */
    void stopThread();

    /**
     * 描述：   获取当前线程是否空闲
     *
     * @return boolean - 线程是否正在使用中
     */
    boolean isBusy();

    /**
     * 描述：   设置线程状态
     *
     * @param busy - 线程使用标识
     */
    void setBusy(boolean busy);
}
