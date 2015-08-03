package pb.system.limit.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pb.file.image.VerifyCode;

/**
 * 数据应用层类 - 验证码图片生成。<br>
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/11/28
 */
public class VerifyCodeServlet extends AbstractServlet
{
    /*-----------------------------开始：静态内容------------------------------*/

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_VERYCODE_WIDTH<br>
     * 描述:    记录请求参数名称——验证码图片宽度<br>
     */
    public static final String PARAM_VERYCODE_WIDTH="codeWidth";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_VERYCODE_HEIGHT<br>
     * 描述:    记录请求参数名称——验证码图片高度<br>
     */
    public static final String PARAM_VERYCODE_HEIGHT="codeHeight";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_VERYCODE_COUNT<br>
     * 描述:    记录请求参数名称——验证码数字数量<br>
     */
    public static final String PARAM_VERYCODE_COUNT="codeCount";
    
    /*-----------------------------结束：静态内容------------------------------*/

    /**
     * 方法（受保护）<br>
     * 名称:    processRequest<br>
     * 描述:    处理数据库连接、关闭等操作，并调用processRequest方法处理相关的HTTP请求<br>
     * 生成验证码图片。<br>
     *
     * @param request  - HTTP请求对象
     * @param response - HTTP输出对象
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void process
    (HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException
    {
        //设置页面输入输出编码
        request.setCharacterEncoding(PAGE_CODE);
        response.setCharacterEncoding(PAGE_CODE);

        //设置页面输出参数（去除缓存）
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);

        try
        {
            //设置页面输出格式
            response.setContentType("image/jpeg");

            //读取验证码图片相关的设置参数
            String paramWidth=request.getParameter(PARAM_VERYCODE_WIDTH);
            String paramHeight=request.getParameter(PARAM_VERYCODE_HEIGHT);
            String paramCount=request.getParameter(PARAM_VERYCODE_COUNT);
            int width=(paramWidth==null || "".equals(paramWidth.trim()))?
                    0:Integer.parseInt(paramWidth);
            int height=(paramHeight==null || "".equals(paramHeight.trim()))?
                    0:Integer.parseInt(paramHeight);
            int count=(paramCount==null || "".equals(paramCount.trim()))?
                    4:Integer.parseInt(paramCount);

            //创建验证码生成对象
            VerifyCode verifyCode=new VerifyCode(width,height,count);

            //获取当前用户的session
            HttpSession session=request.getSession();
            if(session!=null)
            {
                //存入生成的验证码
                session.setAttribute(
                        AbstractServlet.SESSION_LOGIN_VERYCODE
                        ,verifyCode.getRandomCode()
                );
                try(ServletOutputStream stream=response.getOutputStream())
                {
                    ImageIO.write(verifyCode.getBuffImg(),"jpeg",stream);
                }
            }
        }
        catch(IOException|NumberFormatException ex)
        {
            try(PrintWriter out=response.getWriter())
            {
                this.errorHandle(out,ex.getMessage());
            }
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    initDataAction<br>
     * 描述:    初始化数据应用层接口<br>
     */
    @Override
    protected void initDataAction()
    {
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getLimitPluginId<br>
     * 描述:    获取当前功能对应的功能插件标识<br>
     *
     * @return int - 创建模式的权限编码
     */
    @Override
    protected String getLimitPluginId()
    {
        return "";
    }
}
