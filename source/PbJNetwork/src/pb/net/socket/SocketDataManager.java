package pb.net.socket;

import java.io.IOException;
import java.net.Socket;

/**
 * Socket数据管理器接口。<br/>
 * 声明Socket连接数据处理方法，通过实现此接口实现对Socket客户端传输数据的处理方法。
 * 1.10 —— 增加字节发送接口方法。<br/>
 * 1.04 —— 增加isConnected通讯检查方法。<br/>
 * 1.03 —— 增加对收到UDP数据信息处理的相关方法。<br/>
 * 1.02 —— 修改信息发送方法的信息内容参数类型为字符数组。<br/>
 * 1.01 —— 修改sendData方法拋出IOException异常<br/>
 * 1.00 —— 声明DataHandle数据处理方法。
 *
 * @author proteanBear(马强)
 * @version 1.03 2012/02/06
 */
public interface SocketDataManager
{
    /**
     * 描述:   对指定的Socket传输数据进行处理<br/>
     *
     * @param socket - Socket连接对象
     */
    void receiveData(Socket socket,SocketDataProcessor processor) throws IOException;

    /**
     * 描述:   对指定的Socket客户端发送信息<br/>
     *
     * @param socket   - Socket连接对象
     * @param dataText - 发送的信息内容
     */
    boolean sendData(Socket socket,char[] dataText) throws IOException;

    /**
     * 描述:   对指定的Socket客户端发送信息<br/>
     *
     * @param socket   - Socket连接对象
     * @param dataText - 发送的信息内容
     */
    boolean sendData(Socket socket,byte[] dataText) throws IOException;

    /**
     * 描述:    处理收到的UDP数据信息<br/>
     *
     * @param buffer - 收到的数据信息
     * @param length - 数据信息长度
     */
    void receiveUdpData(byte[] buffer,int length) throws IOException;

    /**
     * 描述:    判断当前的Socket通讯是否连接<br/>
     *
     * @param socket - Socket连接对象
     * @return boolean - 当前的Socket通讯是否畅通
     */
    boolean isConnected(Socket socket);
}
