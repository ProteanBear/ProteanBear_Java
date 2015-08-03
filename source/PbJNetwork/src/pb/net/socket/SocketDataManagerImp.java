package pb.net.socket;

import java.io.*;
import java.net.Socket;

/**
 * Socket数据管理器接口实现类。<br/>
 * 实现SocketDataManager接口。<br/>
 * 1.11 —— 恢复字符编码设置方法，实现字节和字符发送双模式。<br/>
 * 1.10 —— 去除字符编码设置方法，并实现字节发送的接口方法。<br/>
 * 1.05 —— 为dataHandle方法添加socket参数。<br/>
 * 1.04 —— 增加对收到UDP数据信息处理的相关方法。<br/>
 * 1.03 —— 修改信息发送方法的信息内容参数类型为字符数组。<br/>
 * 1.02 —— 修改sendData方法拋出IOException异常<br/>
 * 1.01 —— 更改dataHandle方法，增加threadName线程标识<br/>
 * 1.00 —— 完成接口方法的通用实现。<br/>
 *
 * @author proteanBear(马强)
 * @version 1.11 2013/03/30
 */
public abstract class SocketDataManagerImp implements SocketDataManager
{
    /**
     * 方法（受保护）<br/>
     * 名称:    dataHandle<br/>
     * 描述:    对获取到的流数据对象进行处理<br/>
     *
     * @param socket - Socket连接对象
     * @param in     - 输入流
     * @param out    - 输出流
     */
    protected abstract void dataHandle
    (Socket socket,InputStream in,OutputStream out,Object threadName)
            throws IOException;

    /**
     * 方法（公共）<br/>
     * 名称:   receiveData<br/>
     * 描述:   对指定的Socket传输数据进行处理<br/>
     *
     * @param socket - Socket连接对象
     */
    @Override
    public void receiveData(Socket socket,SocketDataProcessor processor)
            throws IOException
    {
        try
        {
            InputStream in=socket.getInputStream();
            if(in!=null && in.available()>0)
            {
                OutputStream out=socket.getOutputStream();
                this.dataHandle(socket,in,out,processor.getThreadName());
                //如果socket为短连接，处理数据后会关闭，判断socket是否关闭，如关闭则停止线程。
                 /*if (!this.isConnected(socket))
                 {
                     processor.stopThread();
                 }*/
            }
        }
        catch(Exception ex)
        {
            //System.out.print(ex.getMessage());
        }
    }

    /**
     * 方法（公共）<br/>
     * 名称:   sendData<br/>
     * 描述:   对指定的Socket客户端发送信息<br/>
     *
     * @param socket   - Socket连接对象
     * @param dataText - 发送的信息内容
     */
    @Override
    public boolean sendData(Socket socket,char[] dataText) throws IOException
    {
        boolean success=false;
        if(socket==null) return success;
        if(socket.isClosed()) return success;

        OutputStreamWriter out=new OutputStreamWriter(socket.getOutputStream(),this.getTextEncode());
        out.write(dataText,0,dataText.length);
        out.flush();
        success=true;
        return success;
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    getTextEncode<br/>
     * 描述:    获取当前发送使用的字符编码(默认为GBK）<br/>
     *
     * @return String - 字符编码
     */
    protected String getTextEncode()
    {
        return "GBK";
    }

    /**
     * 方法（公共）<br/>
     * 名称:   sendData<br/>
     * 描述:   对指定的Socket客户端发送信息<br/>
     *
     * @param socket   - Socket连接对象
     * @param dataText - 发送的信息内容
     */
    @Override
    public boolean sendData(Socket socket,byte[] dataText) throws IOException
    {
        boolean success=false;
        if(socket==null) return success;
        if(socket.isClosed()) return success;

        DataOutputStream out=new DataOutputStream(socket.getOutputStream());
        out.write(dataText,0,dataText.length);
        out.flush();
        success=true;
        return success;
    }

    /**
     * 方法（公共）<br/>
     * 名称:   receiveUdpData<br/>
     * 描述:   处理收到的UDP数据信息<br/>
     *
     * @param buffer - 收到的数据信息
     * @param length - 数据信息长度
     */
    @Override
    public void receiveUdpData(byte[] buffer,int length) throws IOException
    {
    }

    /**
     * 方法（公共）<br/>
     * 名称:    isConnected<br/>
     * 描述:    判断当前的Socket通讯是否连接<br/>
     *
     * @param socket - Socket连接对象
     * @return boolean - 当前的Socket通讯是否畅通
     */
    @Override
    public boolean isConnected(Socket socket)
    {
        boolean result=true;
        try
        {
            socket.sendUrgentData(0xFF);
        }
        catch(Exception ex)
        {
            result=false;
        }
        return result;
    }

}