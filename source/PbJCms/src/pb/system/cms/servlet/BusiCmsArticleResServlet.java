package pb.system.cms.servlet;

import pb.system.cms.action.BusiCmsArticleResAction;
import pb.system.limit.servlet.AbstractServlet;

/**
 * 数据访问HTTP接口——记录文章与资源管理关系
 *
 * @author ProteanBear
 * @version 1.00 2014-09-28
 */
public class BusiCmsArticleResServlet extends AbstractServlet
{
    /**
     * 方法（受保护）
     * 名称:initDataAction
     * 描述:初始化数据应用层接口
     */
    @Override
    protected void initDataAction()
    {
        this.dataAction=new BusiCmsArticleResAction(this.connector);
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
        return "BUSI_CMS_ARTICLE_RES";
    }
}