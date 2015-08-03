package pb.file.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 本Java类是用于描述校验码生成器的类方法
 *
 * @author proteanBear(马强)
 * @version 2.10 2010/09/30
 */
public class VerifyCode
{
    /**
     * 域(私有)<br>
     * 名称:    width<br>
     * 描述:    图片宽度<br>
     */
    private int width;

    /**
     * 域(私有)<br>
     * 名称:    height<br>
     * 描述:    图片高度<br>
     */
    private int height;

    /**
     * 域(私有)<br>
     * 名称:    codeCount<br>
     * 描述:    号码数量<br>
     */
    private int codeCount;

    /**
     * 域(私有)<br>
     * 名称:    codeX<br>
     * 描述:    号码位置x坐标<br>
     */
    private int codeX;

    /**
     * 域(私有)<br>
     * 名称:    fontHeight<br>
     * 描述:    字体高度<br>
     */
    private int fontHeight;

    /**
     * 域(私有)<br>
     * 名称:    codeY<br>
     * 描述:    号码位置y坐标<br>
     */
    private int codeY;

    /**
     * 域(私有)<br>
     * 名称:    randomCode<br>
     * 描述:    随机号码缓存区<br>
     */
    private StringBuffer randomCode;

    /**
     * 域(私有)<br>
     * 名称:    buffImg<br>
     * 描述:    图像缓存<br>
     */
    private BufferedImage buffImg;

    /**
     * 域(私有)<br>
     * 名称:    codeSequence<br>
     * 描述:    生成验证码范围<br>
     */
    private char[] codeSequence={'A','B','C','D','E','F','G','H','I','J',
                                 'K','L','N','P','R','S','T','U','V','X','Y',
                                 'Z','1','2','3','4','5','6','7','8','9'};

    /**
     * 构造函数（无参数）<br>
     * 描述：   初始化私有域
     */
    public VerifyCode()                                           //构造函数：无参数
    {
        this.width=110;
        this.height=30;
        this.codeCount=4;
        this.setCodeForm();
        this.randomCode=new StringBuffer();
        this.produceImage();
    }

    /**
     * 构造函数（三个参数）<br>
     *
     * @param width  - 图片宽度
     * @param height - 图片高度
     * @param count  - 号码数量
     */
    public VerifyCode(int width,int height,int count)
    {
        width=(width==0)?110:width;
        height=(height==0)?30:height;
        count=(count==0)?4:count;
        this.width=width;
        this.height=height;
        this.codeCount=count;
        this.setCodeForm();
        this.randomCode=new StringBuffer();
        this.produceImage();
    }

    /**
     * 方法（私有）<br>
     * 名称:    setCodeForm<br>
     * 描述:    设置生成号码在图片的位置和高度<br>
     */
    private void setCodeForm()
    {
        this.codeX=(this.width)/(this.codeCount+2);
        this.fontHeight=this.height-2;
        this.codeY=this.height-4;
    }

    /**
     * 方法（私有）<br>
     * 名称:    produceImage<br>
     * 描述:    生成验证码图片<br>
     */
    private void produceImage()
    {
        this.buffImg=new BufferedImage(
                this.width,this.height,
                BufferedImage.TYPE_INT_RGB
        );                                   //定义一个图像buffer
        Graphics2D g=buffImg.createGraphics();

        Random random=new Random();                                          //创建一个随机数生成类

        g.setColor(Color.WHITE);                                               //将图像填充为白色
        g.fillRect(0,0,this.width,this.height);

        Font font=new Font("Fixedsys",Font.PLAIN,fontHeight);              //创建字体，字体的大小应该根据图片的高度来定
        g.setFont(font);                                                       //设置字体

        g.setColor(Color.BLACK);
        g.drawRect(0,0,this.width-1,this.height-1);                     //画边框

        g.setColor(Color.PINK);
        for(int i=0;i<125;i++)                                          //随机产生干扰线，使图象中的认证码不易被其它程序探测到
        {
            int x=random.nextInt(this.width);
            int y=random.nextInt(this.height);
            int xl=random.nextInt(12);
            int yl=random.nextInt(12);
            g.drawLine(x,y,x+xl,y+yl);
        }

        int red=0, green=0, blue=0;
        for(int i=0;i<this.codeCount;i++)                               //随机产生codeCount数字的验证码
        {
            String strRand=String.valueOf
                    (this.codeSequence[random.nextInt(30)]);                   //得到随机产生的验证码数字
            red=random.nextInt(125);                                         //产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同
            green=random.nextInt(125);
            blue=random.nextInt(125);
            g.setColor(new Color(red,green,blue));                           //用随机产生的颜色将验证码绘制到图像中
            g.drawString(strRand,(i+1)*this.codeX+6,this.codeY);
            this.randomCode.append(strRand);                                   //将产生的四个随机数组合在一起
        }
    }

    /**
     * 访问器<br>
     * 目标：       buffImg<br>
     *
     * @return BufferedImage - 验证码图片
     */
    public BufferedImage getBuffImg()
    {
        return this.buffImg;
    }

    /**
     * 访问器<br>
     * 目标：       getRandomCode<br>
     *
     * @return String - 验证码
     */
    public String getRandomCode()
    {
        return this.randomCode.toString();
    }
}