package pb.system.limit.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.data.SqlAttribute;
import pb.system.limit.entity.SystemAreaTypePlugin;

/**
 * 数据表映射类数据管理类—— 企业类型插件多对多对应表<br>
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/11/24
 */
public class SystemAreaTypePluginFacade
        extends AbstractEntityManager<SystemAreaTypePlugin>
        implements SystemAreaTypePluginFacadeLocal
{
    /**
     * 构造函数<br>
     *
     * @param connector - 指定数据连接器
     */
    public SystemAreaTypePluginFacade(Connector connector)
    {
        super(connector,SystemAreaTypePlugin.class);
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
        return null;//"TYPE_PLUGIN_SEQUENCE.nextval";
    }

    /**
     * 方法（公共）<br>
     * 名称:    create<br>
     * 描述:    创建指定企业类型和插件标识的多对多数据<br>
     *
     * @param typeId   - 企业类型标识
     * @param pluginId - 插件标识
     */
    @Override
    public boolean create(String typeId,int pluginId)
    {
        //构建实体对象
        SystemAreaTypePlugin plugin=new SystemAreaTypePlugin();
        plugin.setTypeId(typeId);
        plugin.setPluginCustId(pluginId);

        //构造查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("type_id=?",typeId);
        condition.put("plugin_cust_id=?",pluginId);

        //如果数据已存在，返回true
        if(this.exist(condition))
        {
            return true;
        }
        //如果数据不存在，添加数据
        else
        {
            return super.create(plugin);
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    removeByTypeId<br>
     * 描述:    清除指定的企业类型对应的多对多数据<br>
     *
     * @param typeId - 企业类型标识
     * @return boolean - 如果清除成功，返回true
     */
    @Override
    public boolean removeByTypeId(String typeId)
    {
        boolean success=false;
        try
        {
            //构造删除语句
            SqlAttribute sqlAttr=new SqlAttribute()
                    .setTable("SYSTEM_AREA_TYPE_PLUGIN")
                    .addCondition("type_id=?");

            //删除数据
            success=this.dataManager.setAttribute(sqlAttr).delete(typeId);
        }
        catch(Exception ex)
        {
            success=this.logAndReturnFalse(ex.toString());
        }
        return success;
    }

    /**
     * 方法（公共）<br>
     * 名称:    removeByPluginId<br>
     * 描述:    清除指定的插件标识对应的多对多数据<br>
     *
     * @param pluginId - 插件标识
     * @return boolean - 如果清除成功，返回true
     */
    @Override
    public boolean removeByPluginId(int pluginId)
    {
        boolean success=false;
        try
        {
            //构造删除语句
            SqlAttribute sqlAttr=new SqlAttribute()
                    .setTable("SYSTEM_AREA_TYPE_PLUGIN")
                    .addCondition("plugin_cust_id=?");

            //删除数据
            success=this.dataManager.setAttribute(sqlAttr).delete(pluginId);
        }
        catch(Exception ex)
        {
            success=this.logAndReturnFalse(ex.toString());
        }
        return success;
    }

    /**
     * 方法（公共）<br>
     * 名称:    findByTypeId<br>
     * 描述:    获取指定的企业类型对应的多对多数据<br>
     *
     * @param typeId - 企业类型标识
     * @return List - 返回结果
     */
    @Override
    public List<SystemAreaTypePlugin> findByTypeId(String typeId)
    {
        //生成查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("typeId=?",typeId);

        return this.find(condition);
    }
}
