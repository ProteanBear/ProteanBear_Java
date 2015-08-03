package pb.system.limit.manager;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.system.limit.entity.OriginalLimit;

/**
 * 数据表映射类数据管理类——基础码表，用于在创建权限时生成基础权限（当INIT属性为true时），以及在创建编辑权限时选择权限类型。 此处为添加权限
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class OriginalLimitFacade
        extends AbstractEntityManager<OriginalLimit>
        implements OriginalLimitFacadeLocal
{
    /**
     * 构造函数
     *
     * @param connector - 指定数据连接器
     */
    public OriginalLimitFacade(Connector connector)
    {
        super(connector,OriginalLimit.class);
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
        return "limitId";
    }
}