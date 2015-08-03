package pb.net.socket;

import java.io.IOException;

import pb.thread.ListenerRunnable;

/**
 * Socket数据处理器线程描述接口。<br/>
 * 继承自ListenerRunnable接口，增加客户端信息发送方法<br/>
 * 1.10 —— 增加字节发送接口方法。<br/>
 * 1.04 —— 增加判断当前的Socket通讯是否连接的isConnected方法。<br/>
 * 1.03 —— 修改信息发送方法的信息内容参数类型为字符数组。<br/>
 * 1.02 —— 修改sendDataToClient方法拋出IOException异常.<br/>
 * 1.01 —— 增加线程名称属性，可用于缓冲池中的线程索引标识<br/>
 * 1.00 —— 增加信息发送方法。
 *
 * @author proteanBear(马强)
 * @version 1.10 2012/03/30
 */
public interface SocketDataProcessorRunnable extends ListenerRunnable
{
    /**
     * 描述:    发送信息到当前的Socket连接客户端<br/>
     *
     * @param dataText - 信息内容
     * @return boolean -发送是否成功
     */
    boolean sendDataToClient(char[] dataText) throws IOException;

    /**
     * 描述:    发送信息到当前的Socket连接客户端<br/>
     *
     * @param dataText - 信息内容
     * @return boolean -发送是否成功
     */
    boolean sendDataToClient(byte[] dataText) throws IOException;

    /**
     * 访问器<br/>
     * 目标：   threadName<br/>
     *
     * @return String - 当前线程的线程名称
     */
    Object getThreadName();

    /**
     * 更改器<br/>
     * 目标：   threadName<br/>
     *
     * @param threadName - 当前线程的线程名称
     */
    void setThreadName(Object threadName);

    /**
     * 描述:    判断当前的Socket通讯是否连接<br/>
     *
     * @return boolean - 当前的Socket通讯是否畅通
     */
    boolean isConnected();
}
