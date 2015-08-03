package pb.net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * UDP通信客户端类。<br/>
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/07/15
 */
public class UDPClient
{
    /**
     * 域(私有)<br/>
     * 名称:    udpSocket<br/>
     * 描述:    UDP数据连接处理对象<br/>
     */
    private DatagramSocket udpSocket;

    /**
     * 域(私有)<br/>
     * 名称:    Ip<br/>
     * 描述:    记录Socket服务端IP地址<br/>
     */
    private String Ip;

    /**
     * 域(私有)<br/>
     * 名称:    port<br/>
     * 描述:    记录Socket服务监听端口<br/>
     */
    private int port;

    /**
     * 域(私有)<br/>
     * 名称:    error<br/>
     * 描述:    记录错误信息<br/>
     */
    private String error;

    /**
     * 构造函数<br/>
     * 描述：   初始化域
     */
    public UDPClient()
    {
        this.udpSocket=null;
        this.Ip=null;
        this.port=-1;
        this.error=null;
    }

    /**
     * 构造函数<br/>
     * 描述：   初始化域
     *
     * @param Ip   - Socket服务端IP地址
     * @param port - Socket服务监听端口
     */
    public UDPClient(String Ip,int port)
    {
        this();
        this.Ip=Ip;
        this.port=port;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    connect<br/>
     * 描述:    连接服务器，如果IP和Port为空则返回false<br/>
     *
     * @return boolean - 连接成功，返回true；否则返回false
     */
    public boolean connect()
    {
        boolean success=false;

        try
        {
            //判断是否设置了服务器地址和端口
            if(this.Ip==null || this.port==-1)
            {
                this.setError("未设置服务器地址或端口！");
                return success;
            }

            //如果udpSocket对象为空，创建对象
            if(this.udpSocket==null)
            {
                this.udpSocket=new DatagramSocket();
            }
            this.udpSocket.connect(new InetSocketAddress(this.Ip,this.port));
            success=this.udpSocket.isConnected();
        }
        catch(SocketException ex)
        {
            this.setError(ex.toString());
        }

        return success;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    close<br/>
     * 描述:    关闭Socket连接<br/>
     */
    public void close()
    {
        try
        {
            //关闭UDP连接
            if(this.udpSocket!=null) this.udpSocket.close();
        }
        catch(Exception ex)
        {
            this.setError(ex.toString());
        }
        finally
        {
            this.udpSocket=null;
        }
    }

    /**
     * 方法（公共）<br/>
     * 名称:    send<br/>
     * 描述:    发送信息到服务端<br/>
     *
     * @param text - 信息内容
     * @return boolean - 发送成功，返回true;返回false
     */
    public boolean send(String text)
    {
        boolean success=false;

        try
        {
            //判断是否已连接服务端，未连接则连接
            if(this.udpSocket==null) this.connect();
            if(!this.udpSocket.isConnected()) return false;

            byte[] bytes=text.getBytes();
            DatagramPacket dp=new DatagramPacket(bytes,bytes.length);
            this.udpSocket.send(dp);

            success=true;
        }
        catch(IOException ex)
        {
            this.setError(ex.toString());
        }

        return success;
    }

    /**
     * 访问器<br/>
     * 目标：   Ip<br/>
     *
     * @return String - Socket服务端IP地址
     */
    public String getIp()
    {
        return Ip;
    }

    /**
     * 更改器<br/>
     * 目标：   Ip
     *
     * @param Ip - Socket服务端IP地址
     * @return UDPClient - 返回对象本省
     */
    public final UDPClient setIp(String Ip)
    {
        this.Ip=Ip;
        return this;
    }

    /**
     * 访问器<br/>
     * 目标：   port<br/>
     *
     * @return int -  Socket服务监听端口
     */
    public int getPort()
    {
        return port;
    }

    /**
     * 更改器<br/>
     * 目标：   port<br/>
     *
     * @param port - Socket服务监听端口
     * @return UDPClient - 返回对象本省
     */
    public final UDPClient setPort(int port)
    {
        this.port=port;
        return this;
    }

    /**
     * 访问器（公共）<br/>
     * 目标：   error<br/>
     *
     * @return String - 数据库连接字符串
     */
    public String getError()
    {
        return this.error;
    }

    /**
     * 更改器(受保护的)<br/>
     * 目标：   error
     *
     * @param error - 错误信息
     */
    protected void setError(String error)
    {
        this.error=error;
    }
}
