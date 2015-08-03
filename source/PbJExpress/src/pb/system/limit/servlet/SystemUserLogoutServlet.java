package pb.system.limit.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.system.limit.entity.SuperAdminUser;
import pb.system.limit.entity.SystemLogUserCache;
import pb.system.limit.module.LoginUser;

/**
 * 数据访问HTTP接口 - 管理人员注销
 *
 * @author proteanBear(马强)
 * @version 1.00 2012/02/27
 */
public class SystemUserLogoutServlet extends AbstractServlet
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
        boolean isLogin=this.userAction.isLogin(request);

        //如果用户已登录，执行注销方法
        if(isLogin)
        {
            //如果用户已经登录，获取登录用户属性
            LoginUser user=this.userAction.getLoginUserInSession(request);
            if(user!=null)
            {
                this.recordUserLog(user,"");
                //清除管理对象
                request.getSession(false).removeAttribute(AbstractServlet.SESSION_LOGIN_USER);
                //关闭相关的用户通讯线程
                user.logoutFromSystem();
            }
        }
        if(this.isXml)
        {
            this.generateXmlResult(out);
        }
        else
        {
            this.generateJsonResult(out);
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
            int operateType=5;

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
                        "用户（"+user.getUserNick()+"）"
                                +"在"+log.getOperateTime()+"时"
                                +"退出"+SuperAdminUser.superAdminAreaName
                );

                this.userLogManager.create(log);
            }
        }
    }

    /**
     * 方法（私有）<br>
     * 名称:    generateXmlResult<br>
     * 描述:    生成Xml结果<br>
     *
     * @param user - 登录用户对象
     */
    private void generateXmlResult(PrintWriter out)
    {
        StringBuilder result=new StringBuilder();

        //设置根节点信息
        result.append("<RETURN>");

        //设置是否成功显示字段
        result.append("<SUCCESS>TRUE</SUCCESS>");

        //设置结尾
        result.append("</RETURN>");

        out.print(result.toString());
    }

    /**
     * 方法（私有）<br>
     * 名称:    generateJsonResult<br>
     * 描述:    生成Json结果<br>
     *
     * @param user - 登录用户对象
     */
    private void generateJsonResult(PrintWriter out)
    {
        StringBuilder result=new StringBuilder();

        //设置根节点信息
        result.append("{");

        //设置是否成功显示字段
        result.append("\"success\":true");

        //设置结尾
        result.append("}");

        out.print(result.toString());
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
