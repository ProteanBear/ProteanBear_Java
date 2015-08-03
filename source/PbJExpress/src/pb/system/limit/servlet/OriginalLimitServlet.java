package pb.system.limit.servlet;

import pb.system.limit.action.OriginalLimitAction;

/**
 * 数据访问HTTP接口——基础码表，用于在创建权限时生成基础权限（当INIT属性为true时），以及在创建编辑权限时选择权限类型。 此处为添加权限
 *
 * @author ProteanBear
 * @version 1.00 2014-07-16
 */
public class OriginalLimitServlet extends AbstractServlet
{
    /**
     * 方法（受保护）
     * 名称:initDataAction
     * 描述:初始化数据应用层接口
     */
    @Override
    protected void initDataAction()
    {
        this.dataAction=new OriginalLimitAction(this.connector);
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
        return "ORIGINAL_LIMIT";
    }
}