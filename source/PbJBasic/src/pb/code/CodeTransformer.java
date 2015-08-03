package pb.code;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * 编码转换器类。<br>
 * 实现各种类型数据之间的转换方法。<br>
 *
 * @author proteanBear(马强)
 * @version 1.00 2012/03/30
 */
public class CodeTransformer
{
    /**
     * 静态方法（公共）<br>
     * 名称:    fromCharsToBytes<br>
     * 描述:    转换字符数组为字节数组,使用默认的UTF-8编码<br>
     *
     * @param chars - 用于转换的字符数组
     * @return byte[] - 转换后的字节数组
     */
    public static byte[] fromCharsToBytes(char[] chars)
    {
        return fromCharsToBytes(chars,"UTF-8");
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    fromCharsToBytes<br>
     * 描述:    转换字符数组为字节数组<br>
     *
     * @param chars  - 用于转换的字符数组
     * @param encode - 指定使用的编码类型
     * @return byte[] - 转换后的字节数组
     */
    public static byte[] fromCharsToBytes(char[] chars,String encode)
    {
        Charset charSet=Charset.forName(encode);
        CharBuffer charBuf=CharBuffer.allocate(chars.length);
        charBuf.put(chars);
        charBuf.flip();
        ByteBuffer bb=charSet.encode(charBuf);
        return bb.array();
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    fromBytesToChars<br>
     * 描述:    转换字节数组为字符数组,使用默认的UTF-8编码<br>
     *
     * @param bytes - 用于转换的字节数组
     * @return char[] - 转换后的字符数组
     */
    public static char[] fromBytesToChars(byte[] bytes)
    {
        return fromBytesToChars(bytes,"UTF-8");
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    fromBytesToChars<br>
     * 描述:    转换字节数组为字符数组<br>
     *
     * @param bytes  - 用于转换的字节数组
     * @param encode - 指定使用的编码类型
     * @return char[] - 转换后的字符数组
     */
    public static char[] fromBytesToChars(byte[] bytes,String encode)
    {
        Charset charSet=Charset.forName(encode);
        ByteBuffer byteBuf=ByteBuffer.allocate(bytes.length);
        byteBuf.put(bytes);
        byteBuf.flip();
        CharBuffer cb=charSet.decode(byteBuf);
        return cb.array();
    }
}