package pb.net.socket;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import pb.thread.ListenerThread;

/**
 * Socket客户端类。<br/>
 * 所谓socket通常也称作"套接字"，用于描述IP地址和端口，是一个通信链的句柄。<br/>
 * 应用程序通常通过"套接字"向网络发出请求或者应答网络请求。<br/>
 * 此类实现了Socket客户端的通用方法。<br/>
 * 1.11 —— 回复发送时的字符编码设置，修改为字符流和字节流双发送模式。<br/>
 * 1.10 —— 去除发送时的字符编码，更改字符发送为字节发送。<br/>
 * 1.07 —— 增加发送时的字符编码设置。<br/>
 * 1.06 —— 修正关闭后回复监听线程未清空的问题。<br/>
 * 1.05 —— 修改isConnected方法，使用sendUrgentData方法主动探测对方服务端是否断开。<br/>
 * 1.04 —— 增加记录当前是否连接服务器的字段属性以及方法。<br/>
 * 1.03 —— 修改setKeep为公共方法。<br>
 * 1.02 —— 修改信息发送方法的信息内容参数类型为字符数组。<br/>
 * 1.01 —— 增加回复监听线程，用于监听服务端的回复。<br/>
 * 1.00 —— 实现了Socket客户端的通用方法，支持长连接和短连接。<br/>
 *
 * @author proteanBear(马强)
 * @version 1.11 2012/03/30
 */
public class SocketClient
{
    /**
     * 域(私有)<br/>
     * 名称:    socket<br/>
     * 描述:    记录Java的Socket处理对象<br/>
     */
    private Socket socket;

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
     * 名称:    isKeep<br/>
     * 描述:    记录是否通话，字段为true时与服务端的连接为长连接<br/>
     */
    private boolean isKeep;

    /**
     * 域(私有)<br/>
     * 名称:    in<br/>
     * 描述:    信息输入流，用于获取服务端返回的信息流<br/>
     */
    private BufferedReader in;

    /**
     * 域(私有)<br/>
     * 名称:    byteOut<br/>
     * 描述:    信息字节输出流，用于向服务端发送信息<br/>
     */
    private DataOutputStream byteOut;

    /**
     * 域(私有)<br/>
     * 名称:    charOut<br/>
     * 描述:    信息字符输出流，用于向服务端发送信息<br/>
     */
    private OutputStreamWriter charOut;

    /**
     * 域(私有)<br/>
     * 名称:    error<br/>
     * 描述:    记录错误信息<br/>
     */
    private String error;

    /**
     * 域(私有)<br/>
     * 名称:    dataManager<br/>
     * 描述:    记录当前Socket客户端用于监听服务端回复信息的线程使用的数据管理器对象<br/>
     */
    private SocketDataManager dataManager;

    /**
     * 域(私有)<br/>
     * 名称:    receiveThread<br/>
     * 描述:    记录当前Socket客户端用于监听服务端回复信息的线程<br/>
     */
    private ListenerThread receiveThread;

    /**
     * 域(私有)<br/>
     * 名称:    isConnected<br/>
     * 描述:    记录当前是否已经连接服务器成功<br/>
     */
    private boolean isConnected;

    /**
     * 域(私有)<br/>
     * 名称:    textEncode<br/>
     * 描述:    记录字符流发送时的字符编码<br/>
     */
    private String textEncode;

    /**
     * 构造函数<br/>
     * 描述：   初始化域
     */
    public SocketClient()
    {
        //设置Ip
        this.Ip=null;
        //设置服务端口
        this.port=-1;
        //设置是否长连接,默认为短连接
        this.isKeep=false;

        this.dataManager=null;
        this.receiveThread=null;
    }

    /**
     * 构造函数<br/>
     * 描述：   初始化域
     *
     * @param Ip   - Socket服务端IP地址
     * @param port - Socket服务监听端口
     */
    public SocketClient(String Ip,int port)
    {
        this();
        //设置Ip
        this.Ip=Ip;
        //设置服务端口
        this.port=port;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    connect<br/>
     * 描述:    连接服务器，如果IP和Port为空则返回false<br/>
     * 如果已经连接，则关闭连接<br/>
     *
     * @return boolean - 连接成功，返回true；否则返回false
     */
    public boolean connect()
    {
        //关闭原有的Socket连接
        this.close();

        try
        {
            //判断是否设置了服务器地址和端口
            if(this.Ip==null || this.port==-1)
            {
                this.setError("未设置服务器地址或端口！");
                return isConnected;
            }

            //如果Socket对象为空，创建对象
            this.socket=new Socket(this.Ip,this.port);
            //this.socket.setOOBInline(false);
            this.socket.setSoLinger(true,0);

            //如果为长连接方式，启动监听线程
            if(this.isKeep && this.dataManager!=null && this.receiveThread==null)
            {
                SocketDataProcessorRunnable processor=new SocketDataProcessor(this.dataManager,this.socket);
                this.receiveThread=new SocketDataProcessorThread(processor);
                this.receiveThread.startListen();
            }

            isConnected=this.socket.isConnected();
        }
        catch(UnknownHostException ex)
        {
            this.setError("未找到服务器！");
            isConnected=false;
        }
        catch(IOException ex)
        {
            this.setError(ex.toString());
            isConnected=false;
        }

        return isConnected;
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
            //关闭监听线程
            if(this.receiveThread!=null) this.receiveThread.stopThread();
            //关闭字节输出流
            if(this.byteOut!=null) this.byteOut.close();
            //关闭字符输出流
            if(this.charOut!=null) this.charOut.close();
            //关闭输入流
            if(this.in!=null) this.in.close();
            //关闭Socket连接
            if(this.socket!=null) this.socket.close();
        }
        catch(IOException ex)
        {
            this.setError(ex.toString());
        }
        finally
        {
            this.receiveThread=null;
            this.byteOut=null;
            this.charOut=null;
            this.in=null;
            this.socket=null;
            this.isConnected=false;
        }
    }

    /**
     * 方法（公共）<br/>
     * 名称:    sendText<br/>
     * 描述:    发送信息到服务端<br/>
     *
     * @param text - 信息内容
     * @return boolean - 发送成功，返回true;返回false
     */
    public boolean sendText(char[] text)
    {
        boolean success=false;

        try
        {
            //判断是否已连接服务端，未连接则连接
            if(this.socket==null) this.connect();
            if(!this.socket.isConnected()) return false;

            if(this.dataManager!=null)
            {
                success=this.dataManager.sendData(this.socket,text);
            }
            else
            {
                //创建输入输出流
                if(this.in==null)
                {
                    this.in=new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                }
                if(this.charOut==null)
                {
                    this.charOut=new OutputStreamWriter(this.socket.getOutputStream(),this.getTextEncode());
                }

                this.charOut.write(text,0,text.length);
                this.charOut.flush();
            }
        }
        catch(IOException ex)
        {
            this.setError(ex.toString());
        }

        return success;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    sendText<br/>
     * 描述:    发送信息到服务端(字节流方式)<br/>
     *
     * @param text - 信息内容
     * @return boolean - 发送成功，返回true;返回false
     */
    public boolean sendText(byte[] text)
    {
        boolean success=false;

        try
        {
            //判断是否已连接服务端，未连接则连接
            if(this.socket==null) this.connect();
            if(!this.socket.isConnected()) return false;

            //如果指定了数据管理实现类，则调用数据管理实现类的发送方法
            if(this.dataManager!=null)
            {
                success=this.dataManager.sendData(this.socket,text);
            }
            else
            {
                //创建输入输出流
                if(this.in==null)
                {
                    this.in=new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                }
                if(this.byteOut==null)
                {
                    this.byteOut=new DataOutputStream(this.socket.getOutputStream());
                }

                this.byteOut.write(text,0,text.length);
                this.byteOut.flush();
            }

            //如果为短连接则断开连接
            if(!this.isKeep) this.close();
            success=true;
        }
        catch(IOException ex)
        {
            this.setError(ex.toString());
        }

        return success;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    isConnected<br/>
     * 描述:    当前是否已经连接到服务器<br/>
     *
     * @return boolean - 是否已经连接到服务器
     */
    public boolean isConnected()
    {
        return this.isConnected;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    testConnect<br/>
     * 描述:    测试服务端是否断开，未断开则返回true<br/>
     *
     * @param con - 测试内容（一个字节）
     * @return boolean - 服务端是否断开，未断开则返回true
     */
    public boolean testConnect()
    {
        if(this.isConnected)
        {
            try
            {
                this.socket.sendUrgentData(0xFF);
            }
            catch(Exception ex)
            {
                this.isConnected=false;
            }
        }
        return this.isConnected;
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
     * @return SocketClient - 返回对象本省
     */
    public final SocketClient setIp(String Ip)
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
     * @return SocketClient - 返回对象本省
     */
    public final SocketClient setPort(int port)
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

    /**
     * 访问器（公共）<br/>
     * 目标：   isKeep<br/>
     *
     * @return boolean - 是否保持长连接
     */
    public boolean isKeep()
    {
        return this.isKeep;
    }

    /**
     * 更改器(公共)<br/>
     * 目标：   isKeep
     *
     * @param isKeep - 是否保持长连接
     */
    public void setKeep(boolean isKeep)
    {
        this.isKeep=isKeep;
    }

    /**
     * 访问器<br/>
     * 目标：   receiveThread<br/>
     *
     * @return ListenerThread - 回复监听线程
     */
    public ListenerThread getReceiveThread()
    {
        return receiveThread;
    }

    /**
     * 更改器<br/>
     * 目标：   receiveThread<br/>
     *
     * @param dataManager - Socket数据管理器接口
     */
    public void setReceiveThread(SocketDataManager dataManager)
    {
        this.dataManager=dataManager;
    }

    /**
     * 访问器<br/>
     * 目标：   textEncode<br/>
     *
     * @return String - 字符流发送时的字符编码
     */
    public String getTextEncode()
    {
        return textEncode;
    }

    /**
     * 更改器<br/>
     * 目标：   textEncode<br/>
     *
     * @param textEncode - 字符流发送时的字符编码
     */
    public void setTextEncode(String textEncode)
    {
        this.textEncode=textEncode;
    }
}
