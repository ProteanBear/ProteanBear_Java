package pb.net.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import pb.thread.AbstractListenerRunnable;
import pb.thread.ListenerRunnable;

/**
 * Socket服务端连接监听器类。<br/>
 * 实现ListenerRunnable端口，用于创建监听器线程。<br/>
 * 接收Socket客户端发送的握手请求，收到请求后从指定的Socket服务端的线程池中获取空闲的线程进行处理数据交互。
 * 1.01 —— 去除等待客户连接超时的时间设置
 * 1.00 —— 实现基本的方法
 *
 * @author proteanBear(马强)
 * @version 1.01 2011/07/04
 */
public class SocketServerListener extends AbstractListenerRunnable implements ListenerRunnable
{
    /**
     * 域(私有)<br/>
     * 名称:    listener<br/>
     * 描述:    当前Socket连接服务端服务端口监听器<br/>
     */
    private ServerSocket listener;

    /**
     * 域(私有)<br/>
     * 名称:    server<br/>
     * 描述:    当前监听器所属的Socket服务器<br/>
     */
    private SocketServer server;

    /**域(私有)<br/>
     * 名称:    timeOut<br/>
     * 描述:    连接超时时间<br/>
     *///private int timeOut;

    /**
     * 域(私有)<br/>
     * 名称:    dataManager<br/>
     * 描述:    记录对当前监听器使用的数据管理器<br/>
     */
    private SocketDataManager dataManager;

    /**
     * 构造函数<br/>
     * 描述：   初始化域。
     * 捕捉ServerSocket创建时的IOException异常，如果出现异常停止监听并记录错误信息。
     *
     * @param server      - 当前监听器所属的Socket服务器
     * @param port        - 监听端口
     * @param dataManager - Socket数据管理器接口
     */
    public SocketServerListener(int port,SocketDataManager dataManager,SocketServer server)
    {
        super();
        try
        {
            this.server=server;
            this.dataManager=dataManager;
            //this.timeOut=600000;
            this.listener=new ServerSocket(port);
            //this.listener.setSoTimeout(timeOut);
        }
        catch(IOException ex)
        {
            this.stopThread();
            this.setError(ex.toString());
        }
    }

    /**
     * 构造函数<br/>
     * 描述：   初始化域。
     * 捕捉ServerSocket创建时的IOException异常，如果出现异常停止监听并记录错误信息。
     *
     * @param server      - 当前监听器所属的Socket服务器
     * @param dataManager - Socket数据管理器接口
     * @param port        - 监听端口
     * @param timeOut     - 服务超时时间
     */
    public SocketServerListener(int port,SocketDataManager dataManager,SocketServer server,int timeOut)
    {
        super();
        try
        {
            this.server=server;
            this.dataManager=dataManager;
            //this.timeOut=timeOut;
            this.listener=new ServerSocket(port);
            //this.listener.setSoTimeout(timeOut);
        }
        catch(IOException ex)
        {
            this.stopThread();
            this.setError(ex.toString());
        }
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
        try
        {
            this.listener.close();
        }
        catch(IOException ex)
        {
            this.setError(ex.toString());
        }
        finally
        {
            this.listener=null;
        }
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
            Socket client=this.listener.accept();
            //client.setOOBInline(false);
            if(client!=null)
            {
                this.server.addSocketProcessor(client,this.dataManager);
            }
        }
        catch(IOException ex)
        {
            //this.stopThread();
            //this.setError(ex.toString());
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
        throw new UnsupportedOperationException("未实现此方法！");
    }
}