package pb.net.socket;

import java.io.IOException;

import pb.thread.ListenerThread;

/**
 * Socket数据处理器线程类。<br/>
 * 继承自ListenerThread类，增加客户端信息发送方法。<br/>
 * 1.10 —— 增加字节发送接口方法。<br/>
 * 1.04 —— 增加判断当前的Socket通讯是否连接的isConnected方法。<br/>
 * 1.03 —— 修改信息发送方法的信息内容参数类型为字符数组。<br/>
 * 1.02 —— 修改sendDataToClient方法拋出IOException异常.<br/>
 * 1.01 —— 增加获取和更改线程名称的方法
 * 1.00 —— 增加信息发送方法。
 *
 * @author proteanBear(马强)
 * @version 1.10 2012/03/30
 */
public class SocketDataProcessorThread extends ListenerThread
{
    /**
     * 构造函数
     */
    public SocketDataProcessorThread(SocketDataProcessorRunnable runnable)
    {
        super(runnable);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    sendDataToClient<br/>
     * 描述:    发送信息到当前的Socket连接客户端<br/>
     *
     * @param dataText - 信息内容
     * @return boolean -发送是否成功
     */
    public boolean sendDataToClient(char[] dataText) throws IOException
    {
        return ((SocketDataProcessorRunnable)this.runnable).sendDataToClient(dataText);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    sendDataToClient<br/>
     * 描述:    发送信息到当前的Socket连接客户端<br/>
     *
     * @param dataText - 信息内容
     * @return boolean -发送是否成功
     */
    public boolean sendDataToClient(byte[] dataText) throws IOException
    {
        return ((SocketDataProcessorRunnable)this.runnable).sendDataToClient(dataText);
    }

    /**
     * 访问器<br/>
     * 目标：   threadName<br/>
     *
     * @return Object - 当前线程的线程名称
     */
    public Object getThreadName()
    {
        return ((SocketDataProcessorRunnable)this.runnable).getThreadName();
    }

    /**
     * 更改器<br/>
     * 目标：   threadName<br/>
     *
     * @param threadName - 当前线程的线程名称
     */
    public void setThreadName(Object threadName)
    {
        ((SocketDataProcessorRunnable)this.runnable).setThreadName(threadName);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    isConnected<br/>
     * 描述:    判断当前的Socket通讯是否连接<br/>
     *
     * @return boolean - 当前的Socket通讯是否畅通
     */
    public boolean isConnected()
    {
        return ((SocketDataProcessorRunnable)this.runnable).isConnected();
    }
}