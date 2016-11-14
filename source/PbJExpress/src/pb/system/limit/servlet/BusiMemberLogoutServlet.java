package pb.system.limit.servlet;

import pb.system.limit.action.BusiMemberAction;
import pb.system.limit.entity.BusiMember;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 数据访问HTTP接口 - 会员退出登录
 *
 * @author proteanBear(马强)
 * @version 1.00 2012/02/27
 */
public class BusiMemberLogoutServlet extends AbstractServlet
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
    protected void processRequest(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException
    {
        PrintWriter out=response.getWriter();

        //检查用户是否已经登录
        boolean isLogin=this.userAction.isLogin(request);

        //如果用户已登录，执行注销方法
        if(isLogin)
        {
            //如果用户已经登录，获取登录用户属性
            BusiMember member=((BusiMemberAction)this.dataAction).getMemberUserInSession(request);
            if(member!=null)
            {
                //清除管理对象
                request.getSession(false).removeAttribute(BusiMemberServlet.SESSION_MEMBER_USER);
            }
        }
        this.generateResult(out,"成功!");
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
