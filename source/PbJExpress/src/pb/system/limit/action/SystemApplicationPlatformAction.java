package pb.system.limit.action;

import pb.data.Connector;
import pb.system.limit.entity.SystemApplicationPlatform;
import pb.system.limit.manager.SystemApplicationPlatformFacade;

/**
 * 数据应用层类——记录企业应用对应应用平台数据的多对多对应表
 *
 * @author ProteanBear
 * @version 1.00 2014-07-19
 */
public class SystemApplicationPlatformAction
        extends AbstractAction<SystemApplicationPlatform>
        implements DataAction
{
    /**
     * 构造函数
     *
     * @param connector - 数据库连接器对象
     */
    public SystemApplicationPlatformAction(Connector connector)
    {
        super(connector,SystemApplicationPlatform.class);
    }

    /**
     * 方法（受保护）
     * 名称:initManager
     * 描述:初始化数据管理器
     */
    @Override
    protected void initManager()
    {
        this.manager=new SystemApplicationPlatformFacade(this.connector);
    }
}