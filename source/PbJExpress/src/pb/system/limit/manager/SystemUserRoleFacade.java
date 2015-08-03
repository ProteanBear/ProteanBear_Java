package pb.system.limit.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.data.SqlAttribute;
import pb.system.limit.entity.SystemUserRole;

/**
 * 数据表映射类数据管理类——记录企业类型数据表和功能插件数据表的多对多数据关系。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-15
 */
public class SystemUserRoleFacade
        extends AbstractEntityManager<SystemUserRole>
        implements SystemUserRoleFacadeLocal
{
    /**
     * 构造函数<br>
     *
     * @param connector - 指定数据连接器
     */
    public SystemUserRoleFacade(Connector connector)
    {
        super(connector,SystemUserRole.class);
    }

    /**
     * 方法（公共）<br>
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
        return null;//"USER_ROLE_SEQUENCE.nextval";
    }

    /**
     * 方法（公共）<br>
     * 名称:    create<br>
     * 描述:    创建指定管理人员和管理角色的多对多数据<br>
     *
     * @param userId - 管理人员标识
     * @param roleId - 管理角色标识
     */
    @Override
    public boolean create(int userId,String roleId)
    {
        //构建实体对象
        SystemUserRole userRole=new SystemUserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);

        //构造查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("user_id=?",userId);
        condition.put("role_id=?",roleId);

        //如果数据已存在，返回true
        if(this.exist(condition))
        {
            return true;
        }
        //如果数据不存在，添加数据
        else
        {
            return super.create(userRole);
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    removeByUserId<br>
     * 描述:    清除指定的管理人员对应的多对多数据<br>
     *
     * @param userId - 管理人员标识
     * @return boolean - 如果清除成功，返回true
     */
    @Override
    public boolean removeByUserId(int userId)
    {
        boolean success=false;
        try
        {
            //构造删除语句
            SqlAttribute sqlAttr=new SqlAttribute()
                    .setTable("SYSTEM_USER_ROLE")
                    .addCondition("user_id=?");

            //删除数据
            success=this.dataManager.setAttribute(sqlAttr).delete(userId);
        }
        catch(Exception ex)
        {
            success=this.logAndReturnFalse(ex.toString());
        }
        return success;
    }

    /**
     * 方法（公共）<br>
     * 名称:    findByUserId<br>
     * 描述:    获取指定的管理人员对应的多对多数据<br>
     *
     * @param userId - 管理角色标识
     * @return List - 返回结果
     */
    @Override
    public List<SystemUserRole> findByUserId(int userId)
    {
        //生成查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("userId=?",userId);

        return this.find(condition);
    }

}