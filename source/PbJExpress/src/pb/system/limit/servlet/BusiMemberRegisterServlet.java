package pb.system.limit.servlet;

import pb.system.limit.action.BusiMemberAction;
import pb.system.limit.entity.BusiMember;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 数据访问HTTP接口 - 会员注册
 *
 * @author proteanBear(马强)
 * @version 1.04 2016/04/11
 */
public class BusiMemberRegisterServlet extends AbstractServlet
{
    /**
     * 方法（受保护）<br>
     * 名称:    processRequest<br>
     * 描述:    处理来自HTTP访问的相关请求，调用DataAction接口进行数据的增删改查处理<br>
     *
     * @param request  - HTTP请求对象
     * @param response - HTTP输出对象
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void processRequest
    (HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException
    {
        PrintWriter out=response.getWriter();

        //创建新的用户
        BusiMember member=(BusiMember)((BusiMemberAction)this.dataAction).create(request);

        //如果登录成功，则返回当前的用户信息及功能插件
        if(member!=null)
        {
            //储存用户数据信息到Session中
            request.getSession().setAttribute
                    (BusiMemberServlet.SESSION_MEMBER_USER,member);
            this.generateResult(out,request);
        }
        //如果未登录，返回错误信息
        else
        {
            throw new ServletException("用户注册失败!");
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    generateResult<br>
     * 描述:    根据当前要求的输出格式，生成相应的增删改的结果<br>
     *
     * @param out     - 输入对象
     * @param request - HTTP请求对象
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    protected void generateResult(PrintWriter out,HttpServletRequest request)
            throws IOException, ServletException
    {
        BusiMember member=((BusiMemberAction)this.dataAction).getMemberUserInSession(request);
        if(member==null) this.errorHandle(out,"无法获取Session数据");

        if(this.isXml)
        {
            out.print(this.generateXmlResult(member));
        }
        else
        {
            out.print(this.generateJsonResult(member));
        }
    }

    /**
     * 方法（私有）<br>
     * 名称:    generateXmlResult<br>
     * 描述:    生成Xml结果<br>
     *
     * @param member - 登录用户对象
     * @return String - Xml格式的文本
     */
    private String generateXmlResult(BusiMember member)
    {
        StringBuilder result=new StringBuilder();

        //设置根节点信息
        result.append("<RETURN>");

        //设置是否成功显示字段
        result.append("<SUCCESS>TRUE</SUCCESS>");

        //设置用户基本信息
        result.append("<MEMBERNAME>").append(member.getMemberName()).append("</MEMBERNAME>")
                .append("<MEMBERNICK>").append(member.getMemberNick()).append("</MEMBERNICK>")
                .append("<MEMBEREMAIL>").append(member.getMemberEmail()).append("</MEMBEREMAIL>")
                .append("<MEMBERPHONE>").append(member.getMemberPhone()).append("</MEMBERPHONE>")
                .append("<MEMBERHEAD>").append(member.getMemberHead()).append("</MEMBERHEAD>")
                .append("<MEMBERTYPE>").append(member.getMemberType()).append("</MEMBERTYPE>")
                .append("<CREATETIME>").append(member.getCreateTime()).append("</CREATETIME>")
                .append("<UPDATETIME>").append(member.getUpdateTime()).append("</UPDATETIME>")
        ;

        //设置结尾
        result.append("</RETURN>");

        return result.toString();
    }

    /**
     * 方法（私有）<br>
     * 名称:    generateJsonResult<br>
     * 描述:    生成Json结果<br>
     *
     * @param member - 登录用户对象
     * @return String - Json格式的文本
     */
    private String generateJsonResult(BusiMember member)
    {
        StringBuilder result=new StringBuilder();

        //设置根节点信息
        result.append("{");

        //设置是否成功显示字段
        result.append("\"success\":true,");

        //设置用户基本信息
        result.append("\"memberName\":\"").append(member.getMemberName()).append("\",")
                .append("\"memberNick\":\"").append(member.getMemberNick()).append("\",")
                .append("\"memberEmail\":\"").append(member.getMemberEmail()).append("\",")
                .append("\"memberPhone\":\"").append(member.getMemberPhone()).append("\",")
                .append("\"memberHead\":\"").append(member.getMemberHead()).append("\",")
                .append("\"memberType\":").append(member.getMemberType()).append(",")
                .append("\"createTime\":\"").append(member.getCreateTime()).append("\",")
                .append("\"updateTime\":\"").append(member.getUpdateTime()).append("\"")
        ;

        //设置结尾
        result.append("}");

        return result.toString();
    }

    /**
     * 方法（受保护）<br>
     * 名称:    initDataAction<br>
     * 描述:    初始化数据应用层接口<br>
     */
    @Override
    protected void initDataAction()
    {
        this.dataAction=new BusiMemberAction(this.connector);
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
