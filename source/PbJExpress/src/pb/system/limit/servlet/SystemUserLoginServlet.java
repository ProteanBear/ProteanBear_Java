package pb.system.limit.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.system.limit.entity.SuperAdminUser;
import pb.system.limit.entity.SystemApplication;
import pb.system.limit.entity.SystemArea;
import pb.system.limit.entity.SystemLogUserCache;
import pb.system.limit.entity.SystemPlugin;
import pb.system.limit.module.LoginUser;

/**
 * 数据访问HTTP接口 - 管理人员登录
 * 1.04 - 增加全局配置读取。<br>
 * 1.03 - 修改行政区域获取的方法。<br>
 * 1.02 - 修改为验证码验证非必须过程。<br>
 * 1.01 - 修改验证处理流程。<br>
 *
 * @author proteanBear(马强)
 * @version 1.04 2012/03/02
 */
public class SystemUserLoginServlet extends AbstractServlet
{
    /*-----------------------------开始：静态内容------------------------------*/

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_LOGIN_VERYCODE<br>
     * 描述:    记录请求参数名称——管理人员登录验证码<br>
     */
    public static final String PARAM_LOGIN_VERYCODE="verycode";
    
    /*-----------------------------结束：静态内容------------------------------*/

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
        boolean isLogin=this.userAction.isLogin(request);

        //如果用户未登录，执行登录处理
        if(!isLogin)
        {
            //获取参数指定的验证码
            String paramVerycode=request.getParameter(PARAM_LOGIN_VERYCODE);

            if(paramVerycode!=null)
            {
                //获取内存记录的验证码
                String sessionVerycode=this.getVeryCodeInSession(request);
                if(sessionVerycode==null)
                {
                    throw new ServletException("验证码过期，请刷新验证码再次登录");
                }
                //对比两个验证码是否相同
                boolean isEqual=(sessionVerycode.toLowerCase().equals(paramVerycode.toLowerCase()));
                if(!isEqual)
                {
                    throw new ServletException("验证码不匹配，请再次输入验证码登录");
                }
            }

            isLogin=this.userAction.login(request);
        }

        //如果登录成功，则返回当前的用户信息及功能插件
        if(isLogin)
        {
            this.generateResult(out,request);
            LoginUser user=this.userAction.getLoginUserInSession(request);
            this.recordUserLog(user,"");
        }
        //如果未登录，返回错误信息
        else
        {
            throw new ServletException("用户未登录或登录失败");
        }
    }

    /**
     * 方法（私有）<br>
     * 名称:    recordUserLog<br>
     * 描述:    记录用户操作日志信息<br>
     *
     * @param user - 登录用户对象
     * @param mode - 当前的操作模式
     */
    @Override
    protected void recordUserLog(LoginUser user,String mode)
    {
        //如果需记录用户操作日志信息
        if(this.userLogManager!=null)
        {
            //判断操作模式
            int operateType=4;

            //获取当前的操作插件
            //SystemPlugin plugin=user.getPluginByPluginId(this.getLimitPluginId());

            //if(operateType!=-1&&plugin!=null)
            {
                SystemLogUserCache log=new SystemLogUserCache();
                log.setUserId(user.getUserName());
                log.setUserName(user.getUserNick());
                log.setAreaId(user.getAreaId());
                log.setAreaName(user.getAreaName());
                //                 log.setPluginCustId(plugin.getCustId());
                //                 log.setPluginId(plugin.getPluginId());
                //                 log.setPluginName(plugin.getPluginName());
                //                 log.setPluginModule(plugin.getPluginModule());
                log.setOperateType(operateType);
                log.setOperateTime(this.dp.getCurrent());
                log.setLogDescription(
                        "用户（"+user.getUserName()+"）"
                                +"在"+log.getOperateTime()+"时"
                                +"登录到"+SuperAdminUser.superAdminAreaName
                );

                this.userLogManager.create(log);
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

        //设置用户管理应用信息
        result.append("<APPS>");
        SystemApplication app;
        for(int i=0;i<user.applicationSize();i++)
        {
            app=user.getApplication(i);
            result.append("<APPCODE>").append(app.getAppCode()).append("</APPCODE>")
                    .append("<APPICON>").append(app.getAppIcon()).append("</APPICON>")
                    .append("<APPID>").append(app.getAppId()).append("</APPID>")
                    .append("<APPNAME>").append(app.getAppName()).append("</APPNAME>")
                    .append("<APPTHUMBNAIL>").append(app.getAppThumbnail()).append("</APPTHUMBNAIL>")
                    .append("<APPDATAREMARK>").append(app.getDataRemark()).append("</APPDATAREMARK>");
        }
        result.append("</APPS>");

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
                    .append("\"pluginCode\":\"").append(plugin.getPluginCode()).append("\",")
                    .append("\"pluginLink\":\"").append(plugin.getPluginLink()).append("\",")
                    .append("\"pluginIcon\":\"").append(plugin.getPluginIcon()).append("\",")
                    .append("\"pluginDisplay\":").append(plugin.getPluginDisplay()).append("")
                    .append("}");
        }
        result.append("],");

        //设置用户的权限信息
        result.append("\"limits\":\"").append(user.getLimits()).append("\",");

        //设置用户功能插件信息
        result.append("\"apps\":[");
        SystemApplication app;
        for(int i=0;i<user.applicationSize();i++)
        {
            app=user.getApplication(i);
            if(i!=0) result.append(",");
            result.append("{")
                    .append("\"appCode\":\"").append(app.getAppCode()).append("\",")
                    .append("\"appIcon\":\"").append(app.getAppIcon()).append("\",")
                    .append("\"appId\":\"").append(app.getAppId()).append("\",")
                    .append("\"appName\":\"").append(app.getAppName()).append("\",")
                    .append("\"appThumbnail\":\"").append(app.getAppThumbnail()).append("\",")
                    .append("\"appDataRemark\":\"").append(app.getDataRemark()).append("\"")
                    .append("}");
        }
        result.append("]");

        //设置结尾
        result.append("}");

        return result.toString();
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getVeryCodeInSession<br>
     * 描述:    获取session中存储的验证码信息<br>
     *
     * @param request - HTTP请求对象
     * @return String - 获取验证码信息
     * @throws ServletException if a servlet-specific error occurs
     */
    protected String getVeryCodeInSession(HttpServletRequest request)
            throws ServletException
    {
        return (String)AbstractServlet.getAttributeInSession
                (request,AbstractServlet.SESSION_LOGIN_VERYCODE);
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
