package pb.system.limit.manager;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.system.limit.entity.OriginalPlatform;

/**
 * 数据表映射类数据管理类——记录系统应用对应的平台信息的基础码表
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class OriginalPlatformFacade
        extends AbstractEntityManager<OriginalPlatform>
        implements OriginalPlatformFacadeLocal
{
    /**
     * 构造函数
     *
     * @param connector - 指定数据连接器
     */
    public OriginalPlatformFacade(Connector connector)
    {
        super(connector,OriginalPlatform.class);
    }

    /**
     * 方法（受保护）
     * 名称:    getPrimaryKeyName
     * 描述:    获取数据表主键字段名称
     *
     * @return String - 获取主键名称
     */
    @Override
    public String getPrimaryKeyName()
    {
        return "custId";
    }
}