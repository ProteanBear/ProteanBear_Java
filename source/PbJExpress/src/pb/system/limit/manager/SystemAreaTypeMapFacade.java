package pb.system.limit.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.data.SqlAttribute;
import pb.system.limit.entity.SystemAreaTypeMap;

/**
 * 数据表映射类数据管理类—— 行政区域与企业类型多对多对应表<br>
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/11/23
 */
public class SystemAreaTypeMapFacade
        extends AbstractEntityManager<SystemAreaTypeMap>
        implements SystemAreaTypeMapFacadeLocal
{
    /**
     * 构造函数<br>
     *
     * @param connector - 指定数据连接器
     */
    public SystemAreaTypeMapFacade(Connector connector)
    {
        super(connector,SystemAreaTypeMap.class);
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
        return null;//"TYPE_MAP_SEQUENCE.nextval";
    }

    /**
     * 方法（公共）<br>
     * 名称:    create<br>
     * 描述:    创建指定行政区域和企业类型的多对多数据<br>
     *
     * @param areaId - 行政区域标识
     * @param typeId - 企业类型标识
     */
    @Override
    public boolean create(String areaId,String typeId)
    {
        //构建实体对象
        SystemAreaTypeMap map=new SystemAreaTypeMap();
        map.setTypeId(typeId);
        map.setAreaId(areaId);

        //构造查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("type_id=?",typeId);
        condition.put("area_id=?",areaId);

        //如果数据已存在，返回true
        if(this.exist(condition))
        {
            return true;
        }
        //如果数据不存在，添加数据
        else
        {
            return super.create(map);
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    removeByAreaId<br>
     * 描述:    清除指定的行政区域对应的多对多企业类型数据<br>
     *
     * @param areaId - 行政区域标识
     * @return boolean - 如果清除成功，返回true
     */
    @Override
    public boolean removeByAreaId(String areaId)
    {
        boolean success=false;
        try
        {
            //构造删除语句
            SqlAttribute sqlAttr=new SqlAttribute()
                    .setTable("SYSTEM_AREA_TYPE_MAP")
                    .addCondition("area_id=?");

            //删除数据
            success=this.dataManager.setAttribute(sqlAttr).delete(areaId);
        }
        catch(Exception ex)
        {
            success=this.logAndReturnFalse(ex.toString());
        }
        return success;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    findByAreaId<br>
     * 描述:    获取指定的行政区域对应的多对多数据<br>
     *
     * @param areaId - 行政区域标识
     * @return List - 返回结果
     */
    @Override
    public List<SystemAreaTypeMap> findByAreaId(String areaId)
    {
        //生成查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("areaId=?",areaId);

        return this.find(condition);
    }
}
