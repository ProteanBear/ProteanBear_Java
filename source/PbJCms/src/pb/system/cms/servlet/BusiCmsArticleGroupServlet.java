package pb.system.cms.servlet;

import pb.system.cms.action.BusiCmsArticleGroupAction;
import pb.system.limit.servlet.AbstractServlet;

/**
 * 数据访问HTTP接口——记录文章分栏的相关信息，用于专题文章
 *
 * @author ProteanBear
 * @version 1.00 2014-09-28
 */
public class BusiCmsArticleGroupServlet extends AbstractServlet
{
    /**
     * 方法（受保护）
     * 名称:initDataAction
     * 描述:初始化数据应用层接口
     */
    @Override
    protected void initDataAction()
    {
        this.dataAction=new BusiCmsArticleGroupAction(this.connector);
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
        return "BUSI_CMS_ARTICLE_GROUP";
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    stringForReturn<br/>
     * 描述:    用于自定义增删改操作是返回的内容<br/>
     *
     * @param mode - 处理模式
     * @return boolean - 如果参数值为null或""，返回true
     */
    @Override
    protected String stringForReturn(String mode)
    {
        return (AbstractServlet.VALUE_MODE_CREATE.equals(mode))?this.dataAction.lastKeyGenerator():null;
    }
}