package pb.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

/**
 * MD5编码生成器-测试程序
 *
 * @author proteanBear(马强)
 * @version 2.10 2011/04/07
 */
public class MD5Productor_Test
{
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException
    {
        for(;;)
        {
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            System.out.print("输入加密的字符:");
            String text=br.readLine();
            System.out.println("MD5加密后的字符（16位）："+MD5Productor.encodeToString16(text));
            System.out.println("MD5加密后的字符（32位）："+MD5Productor.encodeToString32(text));
        }
    }
}