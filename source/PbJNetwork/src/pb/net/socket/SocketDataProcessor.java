package pb.net.socket;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import pb.thread.AbstractListenerRunnable;
import pb.thread.ListenerRunnable;

/**
 * Socket数据服务管理线程类。<br/>
 * 处理与Socket客户端的数据交互，接收数据并调用SocketDataManager接口的DataHandle方法进行数据处理。<br/>
 * 1.10 —— 实现字节发送的接口方法。<br/>
 * 1.07 —— 增加判断当前的Socket通讯是否连接的isConnected方法，使用sendUrgentData方法主动探测对方服务端是否断开。<br/>
 * 1.06 —— 修正closeSocket调用socket属性未判断为空报错的问题。<br/>
 * 1.05 —— 增加线程终止时的类属性清除。<br/>
 * 1.04 —— 修改信息发送方法的信息内容参数类型为字符数组。<br/>
 * 1.03 —— 修改sendDataToClient方法拋出IOException异常.<br/>
 * 1.02 —— 增加线程名称属性，可用于缓冲池中的线程索引标识。<br/>
 * 1.01 —— 增加等待超时时间设置。<br/>
 * 1.00 —— 实现数据服务管理线程的基本方法。<br/>
 *
 * @author proteanBear(马强)
 * @version 1.10 2012/03/30
 */
public class SocketDataProcessor extends AbstractListenerRunnable implements SocketDataProcessorRunnable
{
    /**
     * 域(私有)<br/>
     * 名称:    threadName<br/>
     * 描述:    记录当前线程的线程名称，可用于缓冲池中的线程索引标识<br/>
     */
    private Object threadName;

    /**
     * 域(私有)<br/>
     * 名称:    dataManager<br/>
     * 描述:    Socket数据处理接口<br/>
     */
    private SocketDataManager dataManager;

    /**
     * 域(私有)<br/>
     * 名称:    socket<br/>
     * 描述:    Socket连接对象<br/>
     */
    private Socket socket;

    /**
     * 构造函数<br/>
     * 描述：   初始化域，指定处理线程使用的数据管理器。<br/>
     * 未指定Socket连接对象，暂停线程。
     *
     * @param dataManager - Socket数据处理接口
     */
    public SocketDataProcessor(SocketDataManager dataManager)
    {
        super();
        this.dataManager=dataManager;
        this.socket=null;
        this.stopListen();
    }

    /**
     * 构造函数<br/>
     * 描述：   初始化域，指定处理线程使用的数据管理器。
     *
     * @param dataManager - Socket数据处理接口
     * @param socket      -
     */
    public SocketDataProcessor(SocketDataManager dataManager,Socket socket)
    {
        super();
        this.dataManager=dataManager;
        this.socket=socket;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    sendDataToClient<br/>
     * 描述:    发送信息到当前的Socket连接客户端<br/>
     *
     * @param dataText - 信息内容
     * @return boolean -发送是否成功
     */
    @Override
    public boolean sendDataToClient(char[] dataText) throws IOException
    {
        return this.dataManager.sendData(this.socket,dataText);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    sendDataToClient<br/>
     * 描述:    发送信息到当前的Socket连接客户端<br/>
     *
     * @param dataText - 信息内容
     * @return boolean -发送是否成功
     */
    @Override
    public boolean sendDataToClient(byte[] dataText) throws IOException
    {
        return this.dataManager.sendData(this.socket,dataText);
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    beforeStop<br/>
     * 描述:    停止监听线程前进行的处理<br/>
     * 关闭当前的服务端口监听器
     */
    @Override
    public void beforeStop()
    {
        this.closeSocket();
        this.dataManager=null;
        this.socket=null;
        this.threadName=null;
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    Execute<br/>
     * 描述:    线程运行监听调用方法。<br/>
     */
    @Override
    protected void Execute()
    {
        try
        {
            this.dataManager.receiveData(this.socket,this);
        }
        catch(IOException ex)
        {
            this.closeSocket();
            this.dataManager=null;
            this.socket=null;
            this.threadName=null;
            this.stopThread();
        }
    }

    /**
     * 方法（私有）<br/>
     * 名称:    generateObject<br/>
     * 描述:    生成新的线程实现对象<br/>
     * 线程池使用，用于生成新的接口实现对象
     *
     * @return ListenerRunnable - 新的线程实现对象
     */
    @Override
    public ListenerRunnable generateObject()
    {
        return new SocketDataProcessor(this.dataManager);
    }

    /**
     * 更改器<br/>
     * 目标：   timeOut
     *
     * @param timeOut - 等待超时时间
     */
    public final SocketDataProcessor setTimeOut(int timeOut)
    {
        try
        {
            this.socket.setSoTimeout(timeOut);
        }
        catch(SocketException ex)
        {
        }
        return this;
    }

    /**
     * 方法（私有）<br/>
     * 名称:    closeSocket<br/>
     * 描述:    关闭Socket连接<br/>
     */
    private void closeSocket()
    {
        try
        {
            if(this.socket!=null) this.socket.close();
        }
        catch(IOException ex)
        {
            this.setError(ex.toString());
        }
        finally
        {
            this.socket=null;
        }
    }

    /**
     * 访问器<br/>
     * 目标：   threadName<br/>
     *
     * @return Object - 当前线程的线程名称
     */
    @Override
    public Object getThreadName()
    {
        return threadName;
    }

    /**
     * 更改器<br/>
     * 目标：   threadName<br/>
     *
     * @param threadName - 当前线程的线程名称
     */
    @Override
    public void setThreadName(Object threadName)
    {
        this.threadName=threadName;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    isConnected<br/>
     * 描述:    判断当前的Socket通讯是否连接<br/>
     *
     * @return boolean - 当前的Socket通讯是否畅通
     */
    @Override
    public boolean isConnected()
    {
        return this.dataManager.isConnected(this.socket);
    }
}