package pb.code;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 本Java类是用于生成MD5编码的字符串
 *
 * @author proteanBear(马强)
 * @version 2.10 2011/04/07
 */
public class MD5Productor
{
    /**
     * 静态方法（公共）<br>
     * 名称:    encodeToString32<br>
     * 描述:    获得32位md5码<br>
     *
     * @param str - 源字符串
     * @return String - 编码后字符串
     * @throws java.security.NoSuchAlgorithmException - throw from "MessageDigest.getInstance"
     * @throws java.io.UnsupportedEncodingException   - throw from "md5.update"
     */
    public static String encodeToString32(String str)
            throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        String result;
        char hexDigits[]={'0','1','2','3','4','5','6','7'
                ,'8','9','a','b','c','d','e','f'};
        MessageDigest md5=MessageDigest.getInstance("MD5");
        md5.update(str.getBytes("utf-8"));
        byte tmp[]=md5.digest();
        char string[]=new char[16*2];
        int k=0;
        for(int i=0;i<16;i++)
        {
            byte byte0=tmp[i];
            string[k++]=hexDigits[byte0 >>> 4&0xf];
            string[k++]=hexDigits[byte0&0xf];
        }
        result=new String(string);
        return result;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    encodeToString16<br>
     * 描述:    获得16位md5码<br>
     *
     * @param str - 源字符串
     * @return String - 编码后字符串
     * @throws java.security.NoSuchAlgorithmException - throw from "MessageDigest.getInstance"
     * @throws java.io.UnsupportedEncodingException   - throw from "md5.update"
     */
    public static String encodeToString16(String str)
            throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        return encodeToString32(str).substring(8,24);
    }
}