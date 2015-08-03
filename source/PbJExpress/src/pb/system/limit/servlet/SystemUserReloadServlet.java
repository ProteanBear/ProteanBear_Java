package pb.system.limit.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.system.limit.entity.SystemArea;
import pb.system.limit.entity.SystemPlugin;
import pb.system.limit.module.LoginUser;

/**
 * 数据访问HTTP接口 - 管理人员信息重载
 *
 * @author proteanBear(马强)
 * @version 1.00 2012/03/08
 */
public class SystemUserReloadServlet extends AbstractServlet
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

        //检查用户是否已经登录
        LoginUser user=this.userAction.getLoginUserInSession(request);

        //如果登录成功，则返回当前的用户信息及功能插件
        if(user!=null)
        {
            //清除缓存信息
            user.logoutFromSystem();

            //重载用户信息
            user=this.userAction.loadLoginUser(user);

            //生成结果
            this.generateResult(out,request);
        }
        //如果未登录，返回错误信息
        else
        {
            //重新登录
            boolean isLogin=this.userAction.login(request);
            //如果登录
            if(isLogin)
            {
                user=this.userAction.getLoginUserInSession(request);
            }
            else
            {
                throw new ServletException("用户登录失败！");
            }
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
        LoginUser user=this.userAction.getLoginUserInSession(request);
        if(user==null) this.errorHandle(out,"无法获取Session数据");

        if(this.isXml)
        {
            out.print(this.generateXmlResult(user));
        }
        else
        {
            out.print(this.generateJsonResult(user));
        }
    }

    /**
     * 方法（私有）<br>
     * 名称:    generateXmlResult<br>
     * 描述:    生成Xml结果<br>
     *
     * @param user - 登录用户对象
     * @return String - Xml格式的文本
     */
    private String generateXmlResult(LoginUser user)
    {
        StringBuilder result=new StringBuilder();

        //设置根节点信息
        result.append("<RETURN>");

        //设置是否成功显示字段
        result.append("<SUCCESS>TRUE</SUCCESS>");

        //设置用户基本信息
        result.append("<USERNAME>").append(user.getUserName()).append("</USERNAME>")
                .append("<USERNICK>").append(user.getUserNick()).append("</USERNICK>")
                .append("<AREAID>").append(user.getAreaId()).append("</AREAID>")
                .append("<AREANAME>").append(user.getAreaName()).append("</AREANAME>")
                .append("<ISSUPERADMIN>").append(user.isSuperAdmin()).append("</ISSUPERADMIN>");

        //设置用户行政区域列表
        result.append("<AREALIST>");
        SystemArea area;
        for(int i=0;i<user.areaListSize();i++)
        {
            area=user.getArea(i);
            result.append("<AREAID>").append(area.getAreaId()).append("</AREAID>")
                    .append("<AREANAME>").append(area.getAreaName()).append("</AREANAME>")
                    .append("<AREACLASS>").append(area.getAreaClass()).append("</AREACLASS>");
        }
        result.append("</AREALIST>");

        //设置用户功能插件信息
        result.append("<PLUGINS>");
        SystemPlugin plugin;
        for(int i=0;i<user.pluginSize();i++)
        {
            plugin=user.getPlugin(i);
            result.append("<CUSTID>").append(plugin.getCustId()).append("</CUSTID>")
                    .append("<PLUGINID>").append(plugin.getPluginId()).append("</PLUGINID>")
                    .append("<PLUGINCODE>").append(plugin.getPluginCode()).append("</PLUGINCODE>")
                    .append("<PLUGINNAME>").append(plugin.getPluginName()).append("</PLUGINNAME>")
                    .append("<PLUGINPARENT>").append(plugin.getPluginName()).append("</PLUGINPARENT>")
                    .append("<PLUGINICON>").append(plugin.getPluginIcon()).append("</PLUGINICON>");
        }
        result.append("</PLUGINS>");

        //设置用户的权限信息
        result.append("<LIMITS>")
                .append(user.getLimits())
                .append("</LIMITS>");

        //设置结尾
        result.append("</RETURN>");

        return result.toString();
    }

    /**
     * 方法（私有）<br>
     * 名称:    generateJsonResult<br>
     * 描述:    生成Json结果<br>
     *
     * @param user - 登录用户对象
     * @return String - Json格式的文本
     */
    private String generateJsonResult(LoginUser user)
    {
        StringBuilder result=new StringBuilder();

        //设置根节点信息
        result.append("{");

        //设置是否成功显示字段
        result.append("\"success\":true,");

        //设置用户基本信息
        result.append("\"userId\":").append(user.getCustId()).append(",")
                .append("\"userName\":\"").append(user.getUserName()).append("\",")
                .append("\"userNick\":\"").append(user.getUserNick()).append("\",")
                .append("\"areaId\":\"").append(user.getAreaId()).append("\",")
                .append("\"areaName\":\"").append(user.getAreaName()).append("\",")
                .append("\"isSuperAdmin\":").append(user.isSuperAdmin()).append(",");

        //设置用户行政区域列表
        result.append("\"areaList\":[");
        SystemArea area;
        for(int i=0;i<user.areaListSize();i++)
        {
            area=user.getArea(i);
            if(i!=0) result.append(",");
            result.append("{")
                    .append("\"areaId\":\"").append(area.getAreaId()).append("\",")
                    .append("\"areaName\":\"").append(area.getDisplayAreaName()).append("\",")
                    .append("\"areaClass\":").append(area.getAreaClass()).append("")
                    .append("}");
        }
        result.append("],");

        //设置用户功能插件信息
        result.append("\"plugins\":[");
        SystemPlugin plugin;
        for(int i=0;i<user.pluginSize();i++)
        {
            plugin=user.getPlugin(i);
            if(i!=0) result.append(",");
            result.append("{")
                    .append("\"custId\":").append(plugin.getCustId()).append(",")
                    .append("\"pluginId\":\"").append(plugin.getPluginId()).append("\",")
                    .append("\"pluginName\":\"").append(plugin.getPluginName()).append("\",")
                    .append("\"pluginParent\":\"").append(plugin.getPluginParent()).append("\",")
                    .append("\"pluginCode\":").append(plugin.getPluginCode()).append(",")
                    .append("\"pluginLink\":\"").append(plugin.getPluginLink()).append("\",")
                    .append("\"pluginIcon\":\"").append(plugin.getPluginIcon()).append("\"")
                    .append("}");
        }
        result.append("],");

        //设置用户的权限信息
        result.append("\"limits\":\"").append(user.getLimits()).append("\"");

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
