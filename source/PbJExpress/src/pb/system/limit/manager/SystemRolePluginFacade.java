package pb.system.limit.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.data.SqlAttribute;
import pb.system.limit.entity.SystemRolePlugin;

/**
 * 数据表映射类数据管理类——记录企业类型数据表和功能插件数据表的多对多数据关系。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-15
 */
public class SystemRolePluginFacade
        extends AbstractEntityManager<SystemRolePlugin>
        implements SystemRolePluginFacadeLocal
{
    /**
     * 构造函数
     *
     * @param connector - 指定数据连接器
     */
    public SystemRolePluginFacade(Connector connector)
    {
        super(connector,SystemRolePlugin.class);
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
     * 方法（受保护）<br>
     * 名称:    getKeyGenerator<br>
     * 描述:    返回自增主键的生成器，非生成的主键返回""<br>
     *
     * @return String - 主键生成值
     */
    @Override
    protected String getKeyGenerator()
    {
        return null;//"ROLE_LIMIT_SEQUENCE.nextval";
    }

    /**
     * 方法（公共）<br>
     * 名称:    create<br>
     * 描述:    创建指定管理角色和功能权限的多对多数据<br>
     *
     * @param roleId        - 管理角色标识
     * @param limitPluginId - 功能权限标识
     */
    @Override
    public boolean create(String roleId,String limitPluginId)
    {
        //构建实体对象
        SystemRolePlugin rolePlugin=new SystemRolePlugin();
        rolePlugin.setRoleId(roleId);
        rolePlugin.setPluginId(limitPluginId);

        //构造查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("roleId=?",roleId);
        condition.put("pluginId=?",limitPluginId);

        //如果数据已存在，返回true
        if(this.exist(condition))
        {
            return true;
        }
        //如果数据不存在，添加数据
        else
        {
            return super.create(rolePlugin);
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    removeByRoleId<br>
     * 描述:    清除指定的管理角色对应的多对多数据<br>
     *
     * @param roleId - 管理角色标识
     * @return boolean - 如果清除成功，返回true
     */
    @Override
    public boolean removeByRoleId(String roleId)
    {
        boolean success=false;
        try
        {
            //构造删除语句
            SqlAttribute sqlAttr=new SqlAttribute()
                    .setTable("SYSTEM_ROLE_PLUGIN")
                    .addCondition("role_id=?");

            //删除数据
            success=this.dataManager.setAttribute(sqlAttr).delete(roleId);
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
     * 描述:    描述:    获取指定的管理角色对应的多对多数据<br>
     *
     * @param roleId - 管理角色标识
     * @return List - 返回结果
     */
    @Override
    public List<SystemRolePlugin> findByRoleId(String roleId)
    {
        //生成查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("roleId=?",roleId);

        return this.find(condition);
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
                    .setTable("SYSTEM_ROLE_PLUGIN")
                    .addCondition("plugin_id=?");

            //删除数据
            success=this.dataManager.setAttribute(sqlAttr).delete(pluginId);
        }
        catch(Exception ex)
        {
            success=this.logAndReturnFalse(ex.toString());
        }
        return success;
    }
}