/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pb.code;

/**
 * @author maqiang
 */
public class CodeTransformerTest
{
    public static void main(String[] args)
    {
        //for (int i = 0; i < 5; i++)        
        {
            String test="我很爱你";
            System.out.println("最初中文："+test);

            char[] testChars=test.toCharArray();
            System.out.println("字符数组："+new String(testChars));

            byte[] testBytes=CodeTransformer.fromCharsToBytes(testChars);
            System.out.println("字节数组："+new String(testBytes));

            testChars=CodeTransformer.fromBytesToChars(testBytes);
            System.out.println("字符数组："+new String(testChars));
        }
    }
}
