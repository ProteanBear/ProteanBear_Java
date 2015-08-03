package pb.net.udp;

import pb.net.socket.SocketDataManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import pb.thread.AbstractListenerRunnable;
import pb.thread.ListenerRunnable;

/**
 * UDP服务端连接监听器类。<br/>
 * 实现ListenerRunnable端口，用于创建监听器线程。<br/>
 * 1.00 —— 实现基本的方法
 *
 * @author proteanBear(马强)
 * @version 1.01 2012/02/06
 */
public class UdpServerListener extends AbstractListenerRunnable implements ListenerRunnable
{
    /**
     * 域(私有)<br/>
     * 名称:    listener<br/>
     * 描述:    发送和接收数据报包的套接字<br/>
     */
    private DatagramSocket listener;

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
    public UdpServerListener(int port,SocketDataManager dataManager)
    {
        super();
        try
        {
            this.dataManager=dataManager;
            this.listener=new DatagramSocket(port);
        }
        catch(SocketException ex)
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
        this.listener.close();
        this.listener=null;
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
            byte[] buf=new byte[8192];
            DatagramPacket packet=new DatagramPacket(buf,buf.length);
            this.listener.receive(packet);
            this.dataManager.receiveUdpData(buf,packet.getLength());
        }
        catch(IOException ex)
        {
            this.setError(ex.getMessage());
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
