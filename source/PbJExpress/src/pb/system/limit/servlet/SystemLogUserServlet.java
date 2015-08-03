package pb.system.limit.servlet;

import pb.system.limit.action.SystemLogUserAction;

/**
 * 数据访问HTTP接口 - 用户日志
 *
 * @author proteanBear(马强)
 * @version 1.00 2012/03/21
 */
public class SystemLogUserServlet extends AbstractServlet
{

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
        return "SYSTEM_LOG_USER";
    }

    /**
     * 方法（受保护）<br>
     * 名称:    initDataAction<br>
     * 描述:    初始化数据应用层接口<br>
     */
    @Override
    protected void initDataAction()
    {
        this.dataAction=new SystemLogUserAction(connector);
    }

}
