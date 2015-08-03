package pb.system.limit.action;

import pb.data.Connector;
import pb.system.limit.entity.BusiResource;
import pb.system.limit.manager.BusiResourceFacade;

/**
 * 数据应用层类——记录媒体资源的相关信息
 *
 * @author ProteanBear
 * @version 1.00 2014-09-28
 */
public class BusiResourceAction extends AbstractAction<BusiResource> implements DataAction
{
    /**
     * 构造函数
     *
     * @param connector - 数据库连接器对象
     */
    public BusiResourceAction(Connector connector)
    {
        super(connector,BusiResource.class);
    }

    /**
     * 方法（受保护）
     * 名称:initManager
     * 描述:初始化数据管理器
     */
    @Override
    protected void initManager()
    {
        this.manager=new BusiResourceFacade(this.connector);
    }
}