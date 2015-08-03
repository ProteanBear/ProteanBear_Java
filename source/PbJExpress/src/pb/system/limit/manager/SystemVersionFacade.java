package pb.system.limit.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.system.limit.entity.SystemVersion;

/**
 * 数据表映射类数据管理类——记录系统应用的相关平台的版本信息。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class SystemVersionFacade
        extends AbstractEntityManager<SystemVersion>
        implements SystemVersionFacadeLocal
{
    /**
     * 构造函数
     *
     * @param connector - 指定数据连接器
     */
    public SystemVersionFacade(Connector connector)
    {
        super(connector,SystemVersion.class);
        this.orderBy="cust_id desc";
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

    /**
     * 方法（受保护）
     * 名称:    findLastestByPlatId
     * 描述:    获取指定的平台应用对应的最新版本数据<br>
     *
     * @param appPlatId - 获取指定平台应用对应的表示
     * @return SystemVersion - 返回最新版本信息
     */
    @Override
    public SystemVersion findLastestByPlatId(int appPlatId)
    {
        Map<String,Object> condition=new HashMap<>();
        condition.put("appPlatId=?",appPlatId);

        List<SystemVersion> sqlResult=this.find(condition);
        return sqlResult.isEmpty()?null:sqlResult.get(0);
    }
}