package pb.net.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.Set;

import pb.net.udp.UdpServerListener;
import pb.thread.ListenerRunnable;
import pb.thread.ListenerThread;

/**
 * Socket服务端类。<br/>
 * 所谓socket通常也称作"套接字"，用于描述IP地址和端口，是一个通信链的句柄。<br/>
 * 应用程序通常通过"套接字"向网络发出请求或者应答网络请求。<br/>
 * 此类实现了Socket服务端的通用方法,支持多个监听器（多个端口）；使用多线程，当有新的Socket连接到服务端时，创建一个线程进行数据通信处理。<br/>
 * 1.10 —— 增加字节发送接口方法。<br/>
 * 1.09 —— 增加处理器线程池遍历集合获取方法。<br/>
 * 1.08 —— 修改广播发送信息的方法，修正手动修改处理器索引值时遍历错误问题。<br/>
 * 1.07 —— 增加对Udp数据信息服务的支持。<br/>
 * 1.06 —— 增加清理空线程的公共方法，便于外部管理调用。<br/>
 * 1.05 —— 增加构造函数用于设置清理空闲线程的间隔时间。<br/>
 * 1.04 —— 增加检查处理器是否已存在的方法。<br/>
 * 1.03 —— 修改删除Socket数据处理器的方法参数为主键。<br/>
 * 1.02 —— 修改信息发送方法的信息内容参数类型为字符数组。<br/>
 * 1.01 —— 增加获取和更改线程名称的方法<br/>
 * 1.00 —— 实现了Socket服务端的通用方法
 *
 * @author proteanBear(马强)
 * @version 1.10 2012/03/30
 */
public class SocketServer
{
    /**
     * 域(私有)<br/>
     * 名称:    error<br/>
     * 描述:    记录进行操作时出现的错误信息<br/>
     */
    private String error;

    /**
     * 域(私有)<br/>
     * 名称:    maxListenerNumber<br/>
     * 描述:    监听器最大数，默认为10个<br/>
     */
    private int maxListenerNumber;

    /**
     * 域(私有)<br/>
     * 名称:    maxProcessorNumber<br/>
     * 描述:    处理器最大数,默认为500个<br/>
     */
    private int maxProcessorNumber;

    /**
     * 域(私有)<br/>
     * 名称:    listeners<br/>
     * 描述:    记录当前Socket服务端使用所有的端口监听器<br/>
     */
    private SocketServerListenerPool listeners;

    /**
     * 域(私有)<br/>
     * 名称:    socketProcessors<br/>
     * 描述:    记录当前Socket服务端使用的所有Socket数据处理器<br/>
     */
    private SocketDataProcessorPool socketProcessors;

    /**
     * 域(私有)<br/>
     * 名称:    timeOut<br/>
     * 描述:    记录Socket连续接收数据时的等待时间,单位为毫秒<br/>
     * 默认为5分钟
     */
    private int timeOut;

    /**
     * 域(私有)<br/>
     * 名称:    udpListeners<br/>
     * 描述:    记录当前UDP服务端使用的所有端口监听器<br/>
     */
    private SocketServerListenerPool udpListeners;

    /**
     * 构造函数<br/>
     * 描述：   初始化域
     */
    public SocketServer()
    {
        this.listeners=new SocketServerListenerPool();
        this.udpListeners=new SocketServerListenerPool();
        this.socketProcessors=new SocketDataProcessorPool();
        this.timeOut=300000;
        this.maxListenerNumber=10;
        this.maxProcessorNumber=500;
    }

    /**
     * 构造函数<br/>
     * 描述：   初始化域
     *
     * @param interval - 设置清理空闲连接的间隔时间
     */
    public SocketServer(long interval)
    {
        this.listeners=new SocketServerListenerPool();
        this.udpListeners=new SocketServerListenerPool();
        this.socketProcessors=new SocketDataProcessorPool(interval);
        this.timeOut=300000;
        this.maxListenerNumber=10;
        this.maxProcessorNumber=500;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    addService<br/>
     * 描述:    增加服务端口，根据传入参数，可监听不同端口。<br/>
     *
     * @param dataManager - Socket数据处理器对象
     * @param port        - 监听端口
     * @return boolean - 添加成功返回true；否则返回false。
     */
    public boolean addService(int port,SocketDataManager dataManager)
    {
        boolean success=false;

        if(this.listeners.size()>=this.maxListenerNumber)
        {
            this.setError("监听器数量已达到设置的最大数！");
            return success;
        }

        ListenerRunnable listener=new SocketServerListener(port,dataManager,this);
        ListenerThread thread=new ListenerThread(listener);
        if(thread==null)
        {
            this.setError("创建线程出现错误！");
            return success;
        }
        success=this.listeners.addListener(port,thread);

        //添加成功，则启动线程
        if(success)
        {
            thread.startListen();
        }
        return success;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    addUdpService<br/>
     * 描述:    增加服务端口，根据传入参数，可监听不同端口。<br/>
     *
     * @param dataManager - Socket数据处理器对象
     * @param port        - 监听端口
     * @return boolean - 添加成功返回true；否则返回false。
     */
    public boolean addUdpService(int port,SocketDataManager dataManager)
    {
        boolean success=false;

        if(this.udpListeners.size()>=this.maxListenerNumber)
        {
            this.setError("监听器数量已达到设置的最大数！");
            return success;
        }

        ListenerRunnable listener=new UdpServerListener(port,dataManager);
        ListenerThread thread=new ListenerThread(listener);
        if(thread==null)
        {
            this.setError("创建线程出现错误！");
            return success;
        }
        success=this.udpListeners.addListener(port,thread);

        //添加成功，则启动线程
        if(success)
        {
            thread.startListen();
        }
        return success;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    delService<br/>
     * 描述:    根据端口移除对应监听服务，并断开相对应联接<br/>
     *
     * @param port - 指定要移除的监听端口
     * @return boolean - 删除成功返回true；否则返回false。
     */
    public boolean delService(int port)
    {
        return this.listeners.deleteListener(port);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    delUdpService<br/>
     * 描述:    根据端口移除对应监听服务，并断开相对应联接<br/>
     *
     * @param port - 指定要移除的监听端口
     * @return boolean - 删除成功返回true；否则返回false。
     */
    public boolean delUdpService(int port)
    {
        return this.udpListeners.deleteListener(port);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    getSocketProcessor<br/>
     * 描述:    获取指定索引标识映射的线程<br/>
     *
     * @param key - 索引标识
     * @return SocketDataProcessorThread - 线程对象
     */
    public SocketDataProcessorThread getSocketProcessor(Object key)
    {
        return this.socketProcessors.getDataProcessor(key);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    addSocketProcessor<br/>
     * 描述:    如果联接对象数量小于maxConnections，增加客户端联接对象<br/>
     *
     * @param client      - Socket连接对象
     * @param dataManager - Socket数据处理器对象
     * @return boolean - 添加成功返回true；否则返回false。
     */
    public boolean addSocketProcessor(Socket client,SocketDataManager dataManager)
    {
        boolean success=false;

        if(this.socketProcessors.size()>=this.maxProcessorNumber)
        {
            this.setError("处理器数量已达到设置的最大数！");
            return success;
        }

        SocketDataProcessorRunnable processor=new SocketDataProcessor(dataManager,client)
                .setTimeOut(this.timeOut);
        SocketDataProcessorThread thread=new SocketDataProcessorThread(processor);
        if(thread==null)
        {
            this.setError("创建线程出现错误！");
            return success;
        }
        Integer index=this.socketProcessors.size();
        thread.setThreadName(index);
        success=this.socketProcessors.addDataProcessor(index,thread);

        //添加成功，则启动线程
        if(success)
        {
            thread.startListen();
        }
        return success;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    delSocketProcessor<br/>
     * 描述:    断开相对应客户端<br/>
     *
     * @param key - 索引标识
     * @return boolean - 删除成功返回true；否则返回false。
     */
    public boolean delSocketProcessor(Object key)
    {
        return this.socketProcessors.deleteDataProcessor(key);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    renameSocketProcessor<br/>
     * 描述:    重命名数据处理器缓冲池中的处理器名称<br/>
     *
     * @param oldName - 原有名称
     * @param newName - 新名称
     * @return boolean - 删除成功返回true；否则返回false。
     */
    public boolean renameSocketProcessor(Object oldName,Object newName)
    {
        return this.socketProcessors.renameKey(oldName,newName);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    containDataProcessor<br/>
     * 描述:    判断缓冲池中是否有指定的处理器对象，如果有则返回true；<br/>
     *
     * @param key - 线程索引
     * @return boolean - 指定的端口是否已经使用
     */
    public boolean containDataProcessor(Object key)
    {
        return this.socketProcessors.containDataProcessor(key);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    updateDataProcessor<br/>
     * 描述:    更新缓冲池中指定序号的处理器为新的监听器对象（值名对）<br/>
     *
     * @param key       - 线程索引
     * @param processor - 新的处理器对象
     * @return boolean - 添加成功，返回true；否则，返回false。
     */
    public boolean updateDataProcessor(Object key,SocketDataProcessorThread processor)
    {
        return this.socketProcessors.updateDataProcessor(key,processor);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    processorPoolKeySet<br/>
     * 描述:    返回处理器对象缓存池的主键遍历集合，便于遍历所有的处理器线程对象<br/>
     *
     * @return Set - 主键遍历集合
     */
    public Set<Object> processorPoolKeySet()
    {
        return this.socketProcessors.poolKeySet();
    }

    /**
     * 方法（公共）<br/>
     * 名称:    displayListenPort<br/>
     * 描述:    显示当前缓冲池中监听器所有的监听端口，端口之间用逗号分隔<br/>
     *
     * @return String - 监听端口字符串，端口之间用逗号分隔
     */
    public String displayListenPort()
    {
        return this.listeners.displayListenPort()+"|"+
                this.udpListeners.displayListenPort();
    }

    /**
     * 方法（公共）<br/>
     * 名称:    clientNumber<br/>
     * 描述:    当前连接到服务端的客户端的数量<br/>
     *
     * @return int - 客户端的数量
     */
    public int clientNumber()
    {
        return this.socketProcessors.size();
    }

    /**
     * 方法（公共）<br/>
     * 名称:    sendTextToAllClients<br/>
     * 描述:    广播信息给所有的客户端信息处理器线程<br/>
     *
     * @param dataText - 信息内容
     */
    public void sendTextToAllClients(char[] dataText)
    {
        for(Object key : this.socketProcessors.poolKeySet())
        {
            try
            {
                SocketDataProcessorThread thread=this.socketProcessors.getDataProcessor(key);
                if(thread==null) continue;
                thread.sendDataToClient(dataText);
            }
            catch(IOException ex)
            {
                this.error+="客户端("+key+")发送失败;";
                continue;
            }
        }
    }

    /**
     * 方法（公共）<br/>
     * 名称:    sendTextToAllClients<br/>
     * 描述:    广播信息给所有的客户端信息处理器线程<br/>
     *
     * @param dataText - 信息内容
     */
    public void sendTextToAllClients(byte[] dataText)
    {
        for(Object key : this.socketProcessors.poolKeySet())
        {
            try
            {
                SocketDataProcessorThread thread=this.socketProcessors.getDataProcessor(key);
                if(thread==null) continue;
                thread.sendDataToClient(dataText);
            }
            catch(IOException ex)
            {
                this.error+="客户端("+key+")发送失败;";
                continue;
            }
        }
    }

    /**
     * 方法（公共）<br/>
     * 名称:    clear<br/>
     * 描述:    停止所有线程，并清除当前所有的监听器和处理器列表<br/>
     */
    public void clear()
    {
        this.socketProcessors.clear();
        this.listeners.clear();
        this.udpListeners.clear();
    }

    /**
     * 方法（公共）<br/>
     * 名称:    clearErrorProcessor<br/>
     * 描述:    清理错误或空置的数据处理器<br/>
     */
    public void clearErrorProcessor()
    {
        this.socketProcessors.clearErrorProcessor();
    }

    /**
     * 更改器(受保护)<br/>
     * 目标：   error
     *
     * @param error - 错误信息
     */
    protected void setError(String error)
    {
        this.error=error;
    }

    /**
     * 访问器(受保护)<br/>
     * 目标：   error<br/>
     *
     * @return String - 错误信息
     */
    protected String getError()
    {
        return this.error;
    }

    /**
     * 访问器<br/>
     * 目标：   maxListenerNumber<br/>
     *
     * @return int - 监听器最大数
     */
    public int getMaxListenerNumber()
    {
        return maxListenerNumber;
    }

    /**
     * 更改器<br/>
     * 目标：   maxListenerNumber
     *
     * @param maxListenerNumber - 监听器最大数
     */
    public void setMaxListenerNumber(int maxListenerNumber)
    {
        this.maxListenerNumber=maxListenerNumber;
    }

    /**
     * 访问器<br/>
     * 目标：   maxProcessorNumber<br/>
     *
     * @return int - 处理器最大数
     */
    public int getMaxProcessorNumber()
    {
        return maxProcessorNumber;
    }

    /**
     * 更改器<br/>
     * 目标：   maxProcessorNumber<br/>
     *
     * @param maxProcessorNumber - 处理器最大数
     */
    public void setMaxProcessorNumber(int maxProcessorNumber)
    {
        this.maxProcessorNumber=maxProcessorNumber;
    }

    /**
     * 更改器<br/>
     * 目标：   timeOut
     *
     * @param timeOut - 等待超时时间
     */
    public final void setTimeOut(int timeOut)
    {
        this.timeOut=timeOut;
    }
}