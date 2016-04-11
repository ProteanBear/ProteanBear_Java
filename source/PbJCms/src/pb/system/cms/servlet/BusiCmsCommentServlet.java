package pb.system.cms.servlet;

import pb.system.cms.action.BusiCmsCommentAction;
import pb.system.limit.servlet.AbstractServlet;

/**
 * 数据访问HTTP接口——
 *@author ProteanBear
 *@version 1.00 2016-04-11
 */
public class BusiCmsCommentServlet extends AbstractServlet
{
    /**方法（受保护）
     * 名称:initDataAction
     * 描述:初始化数据应用层接口
     */
    @Override protected void initDataAction()
    {
        this.dataAction=new BusiCmsCommentAction(this.connector);
    }

    /**方法（受保护）
     * 名称:getLimitPluginId
     * 描述:获取当前功能对应的功能插件标识
     * @return int - 创建模式的权限编码
     */
    @Override protected String getLimitPluginId()
    {
        return "BUSI_CMS_COMMENT";
    }
}