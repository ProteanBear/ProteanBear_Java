package pb.text;

/**
 * 整型生成器。<br>
 * 使用静态方法，用于生成各种格式的整型转换。<br>
 * 1.00 —— 增加方法，将给定的两个整形数字拼在一起（储存在一个字符数组中），并转换为16进制整形数字
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/08/02
 */
public class IntProducer
{
    /**
     * 静态方法（公共）<br>
     * 名称:    spliceAllHexIntegers<br>
     * 描述:    将给定的整形数字拼在一起（储存在一个字符数组中），并转换为16进制整形数字<br>
     *
     * @param numbers - 可变参数，整形数字
     * @return int - 16进制整形数字
     */
    public static int spliceAllHexIntegers(int... numbers)
    {
        char[] data=new char[numbers.length];
        for(int i=0;i<numbers.length;i++) data[i]=(char)numbers[i];

        return Integer.parseInt
                (TextProducer.generateByCharArray(data,numbers.length),16);
    }
}
