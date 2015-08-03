package pb.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 时间字符串生成器测试程序
 *
 * @author proteanBear(马强)
 * @version 2.10 2011/04/07
 */
public class DateProcessor_Test
{
    public static void main(String[] args) throws IOException
    {
        DateProcessor dateTest=new DateProcessor();
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        for(;;)
        {
            System.out.println("功能选择：1、设置时间格式；2、显示当前时间；3、本周第一天；4、本月第一天；5、偏移时间；6、时间比较；7、退出");
            System.out.print("输入选择的功能：");
            String text=br.readLine();
            int mode=Integer.parseInt(text);
            switch(mode)
            {
                //设置时间格式
                case 1:
                {
                    System.out.print("输入时间格式（当前"+dateTest.getFormat()+"）：");
                    text=br.readLine();
                    dateTest.setFormat(text);
                    break;
                }
                //显示当前时间
                case 2:
                {
                    System.out.println("当前时间："+dateTest.getCurrent());
                    break;
                }
                //显示本周第一天时间
                case 3:
                {
                    System.out.println("本周第一天："+dateTest.getFirstDayOfWeek());
                    break;
                }
                //显示本月第一天时间
                case 4:
                {
                    System.out.println("本月第一天："+dateTest.getFirstDayOfMonth());
                    break;
                }
                //显示偏移时间
                case 5:
                {
                    System.out.print("输入偏移时间（之前时间为负值）：");
                    text=br.readLine();
                    int distance=Integer.parseInt(text);
                    System.out.println("偏移时间："+dateTest.getDate(distance));
                    break;
                }
                //测试时间比较
                case 6:
                {
                    System.out.print("输入比较时间（格式"+dateTest.getFormat()+"）：");
                    text=br.readLine();
                    String result="相同";
                    if(dateTest.isAfter(text)) result="之后";
                    if(dateTest.isBefore(text)) result="之前";
                    System.out.println("比较结果："+dateTest.getCurrent()+result);
                    break;
                }
                //退出
                case 7:
                {
                    break;
                }
                default:
                {
                    System.out.println("选择错误！");
                }
            }
            //退出
            if(mode==7) break;
        }
    }
}