package pb.system.limit.action;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import pb.data.Connector;
import pb.system.limit.entity.SystemVersion;
import pb.system.limit.manager.SystemApplicationPlatformFacade;
import pb.system.limit.manager.SystemApplicationPlatformFacadeLocal;
import pb.system.limit.manager.SystemVersionFacade;
import pb.system.limit.servlet.AbstractServlet;
import pb.system.limit.servlet.SystemVersionServlet;

/**
 * 数据应用层类——记录系统应用的相关平台的版本信息。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-15
 */
public class SystemVersionAction extends AbstractAction<SystemVersion> implements DataAction
{
    /**
     * 域(受保护)<br>
     * 名称:    platManager<br>
     * 描述:    应用平台的数据管理器对象<br>
     */
    protected SystemApplicationPlatformFacadeLocal platManager;

    /**
     * 构造函数
     *
     * @param connector - 数据库连接器对象
     */
    public SystemVersionAction(Connector connector)
    {
        super(connector,SystemVersion.class);
    }

    /**
     * 方法（受保护）
     * 名称:initManager
     * 描述:初始化数据管理器
     */
    @Override
    protected void initManager()
    {
        this.manager=new SystemVersionFacade(this.connector);
        this.platManager=new SystemApplicationPlatformFacade(this.connector);
    }

    /**
     * 方法（公共）<br>
     * 名称:    lastVersionForAppPlat<br>
     * 描述:    获取指定应用平台的最新版本<br>
     *
     * @param request - HTTP请求对象
     * @return SystemVersion - 返回最新的版本信息内容
     * @throws ServletException - 抛出处理错误
     */
    public SystemVersion lastVersionForAppPlat(HttpServletRequest request)
            throws ServletException
    {
        try
        {
            //判断数据管理器是否初始化
            if(this.managerNullCheck())
            {
                throw new ServletException("数据库管理器未初始化！");
            }

            //获取指定的参数-所属应用
            String appCert=request.getParameter(AbstractServlet.PARAM_FROMAPP);
            if(this.paramNullCheck(appCert))
            {
                throw new ServletException("未指定所属的应用标识！");
            }

            //获取指定的参数-所属平台
            String appPlat=request.getParameter(SystemVersionServlet.PARAM_PLAT);
            if(this.paramNullCheck(appPlat))
            {
                throw new ServletException("未指定所属的应用平台！");
            }
            if(!SystemVersionServlet.VALUE_PLAT_IOS.equals(appPlat)
                    && !SystemVersionServlet.VALUE_PLAT_ANDROID.equals(appPlat))
            {
                throw new ServletException("指定所属的应用平台不支持！");
            }

            //获取所属平台的源码类型
            String appSource=request.getParameter(SystemVersionServlet.PARAM_SOURCE);
            if(this.paramNullCheck(appSource))
            {
                throw new ServletException("未指定所属的应用平台源码类型！");
            }

            //查询所属平台的最新版本信息
            int appPlatId=this.platManager.findAppPlatIdBy(appCert,appPlat,Integer.parseInt(appSource));
            SystemVersion result=null;
            if(appPlatId!=-1)
            {
                result=((SystemVersionFacade)this.manager).findLastestByPlatId(appPlatId);
            }

            return result;
        }
        catch(ServletException ex)
        {
            throw new ServletException(ex.toString());
        }
    }
}
