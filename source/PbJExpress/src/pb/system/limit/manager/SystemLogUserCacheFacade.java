package pb.system.limit.manager;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.system.limit.entity.SystemLogUserCache;

/**
 * 数据表映射类数据管理接口—— 用户操作日志单日缓存表。<br>
 *
 * @author proteanBear(马强)
 * @version 1.00 2012/03/14
 */
public class SystemLogUserCacheFacade
        extends AbstractEntityManager<SystemLogUserCache>
        implements SystemLogUserCacheFacadeLocal
{
    /**
     * 构造函数
     *
     * @param connector
     */
    public SystemLogUserCacheFacade(Connector connector)
    {
        super(connector,SystemLogUserCache.class);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getPrimaryKeyName<br>
     * 描述:    获取数据表主键字段名称<br>
     *
     * @return String - 获取主键名称
     */
    @Override
    public String getPrimaryKeyName()
    {
        return "custId";
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getKeyGenerator<br>
     * 描述:    返回自增主键的生成器，非生成的主键返回""<br>
     *
     * @return String - 主键生成值
     */
    @Override
    protected String getKeyGenerator()
    {
        return "LOG_USER_SEQUENCE.nextval";
    }

    //*/
}