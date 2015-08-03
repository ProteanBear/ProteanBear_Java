package pb.system.limit.action;

import pb.data.Connector;
import pb.system.limit.entity.OriginalPlatform;
import pb.system.limit.manager.OriginalPlatformFacade;

/**
 * 数据应用层类——记录系统应用对应的平台信息的基础码表
 *
 * @author ProteanBear
 * @version 1.00 2014-07-15
 */
public class OriginalPlatformAction extends AbstractAction<OriginalPlatform> implements DataAction
{
    /**
     * 构造函数
     *
     * @param connector - 数据库连接器对象
     */
    public OriginalPlatformAction(Connector connector)
    {
        super(connector,OriginalPlatform.class);
    }

    /**
     * 方法（受保护）
     * 名称:initManager
     * 描述:初始化数据管理器
     */
    @Override
    protected void initManager()
    {
        this.manager=new OriginalPlatformFacade(this.connector);
    }
}