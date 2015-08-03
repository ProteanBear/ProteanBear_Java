package pb.system.limit.action;

import pb.data.Connector;
import pb.system.limit.entity.OriginalLimit;
import pb.system.limit.manager.OriginalLimitFacade;

/**
 * 数据应用层类——基础码表，用于在创建权限时生成基础权限（当INIT属性为true时），以及在创建编辑权限时选择权限类型。 此处为添加权限
 *
 * @author ProteanBear
 * @version 1.00 2014-07-15
 */
public class OriginalLimitAction extends AbstractAction<OriginalLimit> implements DataAction
{
    /**
     * 构造函数
     *
     * @param connector - 数据库连接器对象
     */
    public OriginalLimitAction(Connector connector)
    {
        super(connector,OriginalLimit.class);
    }

    /**
     * 方法（受保护）
     * 名称:initManager
     * 描述:初始化数据管理器
     */
    @Override
    protected void initManager()
    {
        this.manager=new OriginalLimitFacade(this.connector);
    }
}