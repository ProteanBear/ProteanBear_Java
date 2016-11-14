package pb.system.cms.action;

import pb.data.Connector;
import pb.system.cms.entity.BusiCmsTag;
import pb.system.cms.manager.BusiCmsTagFacade;
import pb.system.limit.action.AbstractAction;
import pb.system.limit.action.DataAction;

/**
 * 数据应用层类——
 *@author ProteanBear
 *@version 1.00 2016-04-07
 */
public class BusiCmsTagAction extends AbstractAction<BusiCmsTag> implements DataAction
{
    /**构造函数
     * @param connector - 数据库连接器对象
     */
    public BusiCmsTagAction(Connector connector)
    {
        super(connector,BusiCmsTag.class);
    }

    /**方法（受保护）
     * 名称:initManager
     * 描述:初始化数据管理器
     */
    @Override protected void initManager()
    {
        this.manager=new BusiCmsTagFacade(this.connector);
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    supportNoLoginFind<br/>
     * 描述:    是否支持非登录下查询处理<br/>
     *
     * @return boolean - 如果参数值为null或""，返回true
     */
    @Override
    public boolean supportNoLoginFind()
    {
        return true;
    }
}