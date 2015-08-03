package pb.text;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

/**
 * 本Java类是用于将制定的内容进行字符串构造的方法
 * 1.01 - 增加总页数属性
 *
 * @author proteanBear(马强)
 * @version 1.01 2011/11/07
 */
public class StringProductor
{
    /**
     * 静态域(私有)<br>
     * 名称:    rowExcision<br>
     * 描述:    列表行分隔符<br>
     */
    private static String rowExcision=",";

    /**
     * 静态域(私有)<br>
     * 名称:    rangeExcision<br>
     * 描述:    列分隔符<br>
     */
    private static String rangeExcision="|";

    /**
     * 域(私有)<br>
     * 名称:    hexString<br>
     * 描述:    16进制字符<br>
     */
    private static String hexString="0123456789ABCDEF";

    /**
     * 静态方法（公共）<br>
     * 名称:    getRowExcision<br>
     * 描述:    获取当前的列表行分隔符<br>
     *
     * @return String - 成功标示符
     */
    public static String getRowExcision()
    {
        return rowExcision;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    setRowExcision<br>
     * 描述:    设置当前的列表行分隔符<br>
     *
     * @param tag - 要设置的列表行分隔符
     * @return StringProductor - 返回StringProductor类
     */
    public static Class setRowExcision(String tag)
    {
        rowExcision=tag;
        return StringProductor.class;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    getRangeExcision<br>
     * 描述:    获取当前的列分隔符<br>
     *
     * @return String - 列分隔符
     */
    public static String getRangeExcision()
    {
        return rangeExcision;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    setRangeExcision<br>
     * 描述:    设置当前的列分隔符<br>
     *
     * @param tag - 要设置的列分隔符
     * @return StringProductor - 返回StringProductor类
     */
    public static Class setRangeExcision(String tag)
    {
        rangeExcision=tag;
        return JsonProductor.class;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    createHeader<br>
     * 描述:    创建字符串的起始字符<br>
     *
     * @param total - 总数参数
     * @return String - Json字符串的起始字符
     */
    public static String createHeader(int total)
    {
        return "\""+total+"\"";
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    createHeader<br>
     * 描述:    创建字符串的起始字符<br>
     *
     * @param pageSize  - 一页数据量
     * @param page      - 页码
     * @param total     - 总数
     * @param totalPage - 总页数
     * @return String - Json字符串的起始字符
     */
    public static String createHeader(int pageSize,int page,int total,int totalPage)
    {
        StringBuilder buffer=new StringBuilder();
        buffer.append("\"").append(pageSize).append(rangeExcision)
                .append(page).append(rangeExcision)
                .append(total).append(rangeExcision)
                .append(totalPage).append("\"");
        return buffer.toString();
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    createHeader<br>
     * 描述:    创建字符串的起始字符<br>
     *
     * @param attributes - 根目录属性
     * @return String - Json字符串的起始字符
     */
    public static String createHeader(Map<String,String> attributes)
    {
        StringBuilder buffer=new StringBuilder();
        buffer.append("\"");
        if(!attributes.isEmpty())
        {
            for(Object key : attributes.keySet())
            {
                String name=(String)key;
                String value=attributes.get(name);
                buffer.append(rangeExcision).append(name).append("=").append(value);
            }
        }
        buffer.append("\"");
        return buffer.toString();
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    createEnd<br>
     * 描述:    创建字符串的结尾字符串<br>
     *
     * @return String - 结尾字符串
     */
    public static String createEnd()
    {
        return "";
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    createInformation<br>
     * 描述:    显示错误信息<br>
     *
     * @param bool  - 是否成功
     * @param infor - 信息
     * @return String - 包装后的信息
     */
    public static String createInformation(boolean bool,String infor)
    {
        return bool+rowExcision+infor;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    toStringByObjectList<br>
     * 描述:    生成对象列表的一般格式字符串<br>
     *
     * @param list - 对象列表
     * @return String - 一般格式字符串
     */
    public static String toStringByObjectList(List<Object> list)
    {
        return new ObjectListToStringTransformer(list).transToString();
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    toStringByObjectList<br>
     * 描述:    生成对象分页列表的一般格式字符串<br>
     *
     * @param list      - the list of object array
     * @param pageSize  - 一页数据量
     * @param page      - 页码
     * @param total     - 总数
     * @param totalPage - 总页数
     * @return String - 一般格式字符串
     */
    public static String toStringByObjectList(List<Object> list
            ,int pageSize,int page,int total,int totalPage)
    {
        return new ObjectListToStringTransformer(list).transToString(pageSize,page,total,totalPage);
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    toStringByObjectList<br>
     * 描述:    生成对象分页列表的一般格式字符串<br>
     *
     * @param list       - the list of object array
     * @param attributes - 根目录属性
     * @return String - 一般格式字符串
     */
    public static String toStringByObjectList
    (List<Object> list,Map<String,String> attributes)
    {
        return new ObjectListToStringTransformer(list).transToString(attributes);
    }

    /**
     * 方法（公共）<br>
     * 名称:    toFullStringByInteger<br>
     * 描述:    获得数字的指定长度的字符串（前面补齐0）<br>
     *
     * @param num    - 指定的数字
     * @param length - 补齐的长度
     * @return String - 补齐长度后的数字字符串
     */
    public static String toFullStringByInteger(int num,int length)
    {
        return toFullStringByNumberString(num+"",length);
    }

    /**
     * 方法（公共）<br>
     * 名称:    toFullStringByInteger<br>
     * 描述:    获得数字的指定长度的字符串（指定补齐位置）<br>
     *
     * @param num    - 指定的数字
     * @param length - 补齐的长度
     * @param inhead - 为true时在前面补0
     * @return String - 补齐长度后的数字字符串
     */
    public static String toFullStringByInteger
    (int num,int length,boolean inhead)
    {
        return toFullStringByNumberString(num+"",length,inhead);
    }

    /**
     * 方法（公共）<br>
     * 名称:    toFullStringByNumberString<br>
     * 描述:    获得数字字符串的指定长度的字符串（前面补齐0）<br>
     *
     * @param numString - 指定的数字字符串
     * @param length    - 补齐的长度
     * @return String - 补齐长度后的数字字符串
     */
    public static String toFullStringByNumberString(String numString,int length)
    {
        return toFullStringByNumberString(numString,length,true);
    }

    /**
     * 方法（公共）<br>
     * 名称:    toFullStringByInteger<br>
     * 描述:    获得数字字符串的指定长度的字符串（指定补齐位置）<br>
     *
     * @param numString - 指定的数字
     * @param length    - 补齐的长度
     * @param inhead    - 为true时在前面补0
     * @return String - 补齐长度后的数字字符串
     */
    public static String toFullStringByNumberString
    (String numString,int length,boolean inhead)
    {
        String result="";
        if(numString.length()>=length) return numString;
        if(!inhead)
        {
            result+=numString;
        }
        for(int i=0;i<length-numString.length();i++) result+="0";
        if(inhead)
        {
            result+=numString;
        }
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    toHexStringByBytes<br>
     * 描述:    获得byte数组的16进制字符串<br>
     *
     * @param bytes - byte数组
     * @return String - 16进制数字字符串
     */
    public static String toHexStringByBytes(byte[] bytes)
    {
        StringBuilder sb=new StringBuilder(bytes.length*2);
        //将字节数组中每个字节拆解成2位16进制整数
        for(int i=0;i<bytes.length;i++)
        {
            sb.append(hexString.charAt((bytes[i]&0xf0) >> 4));
            sb.append(hexString.charAt((bytes[i]&0x0f)));
        }
        return sb.toString();
    }

    /**
     * 方法（公共）<br>
     * 名称:    toHexStringBybytes<br>
     * 描述:    获得浮点数的16进制字符串<br>
     *
     * @param f - 浮点数
     * @return String - 16进制数字字符串
     */
    public static String toHexStringByFloat(float f)
    {
        ByteBuffer byteBuffer=ByteBuffer.allocate(4);
        byte[] bytes=new byte[4];
        FloatBuffer floatBuffer=byteBuffer.asFloatBuffer();
        floatBuffer.put(f);
        byteBuffer.get(bytes);
        return toHexStringByBytes(bytes);
    }

    /**
     * 方法（公共）<br>
     * 名称:    toHexStringByString<br>
     * 描述:    获得字符串的16进制字符串<br>
     *
     * @param str - 字符串
     * @return String - 字符串的16进制字符串
     */
    public static String toHexStringByString(String str)
    {
        byte[] bytes=str.getBytes();
        return toHexStringByBytes(bytes);
    }

    /**
     * 方法（公共）<br>
     * 名称:    toStringByHexString<br>
     * 描述:    通过16进制字符串生成包含16进制表示的内容的字符串<br>
     *
     * @param bytes - 16进制字符串
     * @return String - 包含16进制表示的内容的字符串
     */
    public static String toStringByHexString(String bytes)
    {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2);
        // 将每2位16进制整数组装成一个字节
        for(int i=0;i<bytes.length();i+=2)
        {
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4|hexString.indexOf(bytes.charAt(i+1))));
        }
        return new String(baos.toByteArray());
    }

    /**
     * 方法（公共）<br>
     * 名称： isBlank<br>
     * 描述： 判断字符串是否为空
     *
     * @param str - 字符串
     * @return boolean 为空则返回true不为空返回false
     */
    public static boolean isBlank(String str)
    {
        return str==null || "".equals(str);
    }
}
