package pb.text;

/**
 * 文本生成器。<br>
 * 使用静态方法，用于生成各种格式的转换文本。<br>
 * 1.00 —— 添加Char[]生成的16进制文本方法
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/08/02
 */
public class TextProducer
{
    /**
     * 静态方法（公共）<br>
     * 名称:    generateByCharArray<br>
     * 描述:    Char[]生成的16进制文本<br>
     *
     * @param chars - 字节数组
     * @param len   - 字符数据长度
     * @return String - 16进制文本
     */
    public static String generateByCharArray(char[] chars,int len)
    {
        StringBuilder sb=new StringBuilder("");
        int num=0;
        for(char b : chars)
        {
            if(num>=len) break;
            int i=b;// & 0xFF;//byte类型【8位】转换为int类型【32位】
            String hex=Integer.toHexString(i);
            if(hex.length()==1) hex='0'+hex;
            sb.append(hex.toUpperCase());
            num++;
        }
        return sb.toString();
    }
}
