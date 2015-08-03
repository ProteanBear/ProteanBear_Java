package pb.system.limit.servlet;

import pb.system.limit.action.SystemUserAction;

/**
 * 数据访问HTTP接口 - 管理人员
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/11/28
 */
public class SystemUserServlet extends AbstractServlet
{
    /*-----------------------------开始：静态内容------------------------------*/

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_PLUGIN<br>
     * 描述:    记录请求参数名称——管理人员对应的管理角色标识<br>
     */
    public static final String PARAM_ROLE="userRoles[]";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_NEW_PASS<br>
     * 描述:    记录请求参数名称——管理人员的新密码<br>
     */
    public static final String PARAM_NEW_PASS="newPass";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_PASS_AGAIN<br>
     * 描述:    记录请求参数名称——管理人员的新密码,再次输入<br>
     */
    public static final String PARAM_PASS_AGAIN="passAgain";
    
    /*-----------------------------结束：静态内容------------------------------*/

    /**
     * 方法（受保护）<br>
     * 名称:    initDataAction<br>
     * 描述:    初始化数据应用层接口<br>
     */
    @Override
    protected void initDataAction()
    {
        this.dataAction=new SystemUserAction(this.connector);
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
        return "SYSTEM_MANAGE_USER";
    }
}
