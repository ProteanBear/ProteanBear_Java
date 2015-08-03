package pb.system.cms.action;

import pb.data.Connector;
import pb.system.cms.entity.BusiCmsArticleRes;
import pb.system.cms.manager.BusiCmsArticleResFacade;
import pb.system.limit.action.AbstractAction;
import pb.system.limit.action.DataAction;

/**
 * 数据应用层类——记录文章与资源管理关系
 *
 * @author ProteanBear
 * @version 1.00 2014-09-28
 */
public class BusiCmsArticleResAction extends AbstractAction<BusiCmsArticleRes> implements DataAction
{
    /**
     * 构造函数
     *
     * @param connector - 数据库连接器对象
     */
    public BusiCmsArticleResAction(Connector connector)
    {
        super(connector,BusiCmsArticleRes.class);
    }

    /**
     * 方法（受保护）
     * 名称:initManager
     * 描述:初始化数据管理器
     */
    @Override
    protected void initManager()
    {
        this.manager=new BusiCmsArticleResFacade(this.connector);
    }
}