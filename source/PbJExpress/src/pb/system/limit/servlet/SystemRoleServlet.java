package pb.system.limit.servlet;

import pb.system.limit.action.SystemRoleAction;

/**
 * 数据访问HTTP接口 - 用户管理角色
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/11/28
 */
public class SystemRoleServlet extends AbstractServlet
{
    /*-----------------------------开始：静态内容------------------------------*/

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_PLUGIN<br>
     * 描述:    记录请求参数名称——管理角色对应的功能权限标识<br>
     */
    public static final String PARAM_LIMIT="roleLimits[]";
    
    /*-----------------------------结束：静态内容------------------------------*/

    /**
     * 方法（受保护）<br>
     * 名称:    initDataAction<br>
     * 描述:    初始化数据应用层接口<br>
     */
    @Override
    protected void initDataAction()
    {
        this.dataAction=new SystemRoleAction(this.connector);
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
        return "SYSTEM_CONFIG_ROLE";
    }
}