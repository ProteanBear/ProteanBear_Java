package pb.system.limit.servlet;

import pb.system.limit.action.SystemApplicationAction;

/**
 * 数据访问HTTP接口——记录企业运维的APP应用的相关信息。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-15
 */
public class SystemApplicationServlet extends AbstractServlet
{
    /*-----------------------------开始：静态内容------------------------------*/

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_AREATYPE<br>
     * 描述:    记录请求参数名称——应用对应的平台信息<br>
     */
    public static final String PARAM_APPPLAT="appPlat[]";
    
    /*-----------------------------结束：静态内容------------------------------*/

    /**
     * 方法（受保护）
     * 名称:initDataAction
     * 描述:初始化数据应用层接口
     */
    @Override
    protected void initDataAction()
    {
        this.dataAction=new SystemApplicationAction(this.connector);
    }

    /**
     * 方法（受保护）
     * 名称:getLimitPluginId
     * 描述:获取当前功能对应的功能插件标识
     *
     * @return int - 创建模式的权限编码
     */
    @Override
    protected String getLimitPluginId()
    {
        return "SystemApplication_UPPER";
    }
}