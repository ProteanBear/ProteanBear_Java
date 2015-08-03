package pb.system.limit.servlet;

import pb.system.limit.action.OriginalPlatformAction;

/**
 * 数据访问HTTP接口——记录系统应用对应的平台信息的基础码表
 *
 * @author ProteanBear
 * @version 1.00 2014-07-16
 */
public class OriginalPlatformServlet extends AbstractServlet
{
    /**
     * 方法（受保护）
     * 名称:initDataAction
     * 描述:初始化数据应用层接口
     */
    @Override
    protected void initDataAction()
    {
        this.dataAction=new OriginalPlatformAction(this.connector);
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
        return "ORIGINAL_PLATFORM";
    }
}