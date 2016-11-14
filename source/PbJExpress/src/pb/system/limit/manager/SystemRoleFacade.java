package pb.system.limit.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.data.SqlAttribute;
import pb.system.limit.entity.SystemRole;
import pb.text.DateProcessor;

/**
 * 数据表映射类数据管理类——记录系统用户角色的相关数据信息
 *
 * @author ProteanBear
 * @version 1.00 2014-07-15
 */
public class SystemRoleFacade
        extends AbstractEntityManager<SystemRole>
        implements SystemRoleFacadeLocal
{
    /**
     * 域(私有)<br>
     * 名称:    dp<br>
     * 描述:    日期处理器对象<br>
     */
    private DateProcessor dp;

    /**
     * 构造函数
     *
     * @param connector - 指定数据连接器
     */
    public SystemRoleFacade(Connector connector)
    {
        super(connector,SystemRole.class);
        this.dp=new DateProcessor("yyyyMMddHHmmss");
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
        return "roleId";
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
        return this.dp.getCurrent();
    }

    /**
     * 方法（受保护）<br>
     * 名称:    afterRemove<br>
     * 描述:    设置删除后的相关处理<br>
     *
     * @param obj - 主键或删除对象
     */
    @Override
    protected void afterRemove(Object obj)
    {
        //指定主键或对象为空
        if(obj==null) this.logAndReturnFalse("指定主键或对象为空");

        //获取管理角色的主键
        String roleId=null;
        if(obj.getClass().isAssignableFrom(SystemRole.class))
        {
            roleId=((SystemRole)obj).getRoleId();
        }
        else
        {
            roleId=obj+"";
        }

        //创建多对多企业类型插件管理器对象
        SystemRolePluginFacadeLocal manager=new SystemRolePluginFacade(this.connector);

        //清除相关企业类型多对多数据
        manager.removeByRoleId(roleId);
    }

    /**
     * 方法（公共）<br>
     * 名称:    findByUserId<br>
     * 描述:    获取指定的管理人员对应的用户角色列表<br>
     *
     * @param userId - 管理人员标识
     * @return List - 用户角色列表
     */
    @Override
    public List<SystemRole> findByUserId(int userId)
    {
        List<SystemRole> result=null;
        try
        {
            //设置查询语句
            SqlAttribute sqlAttri=new SqlAttribute()
                    .setColumnNames("sr.*")
                    .setTable("system_user_role sur,system_role sr")
                    .addCondition("sur.role_id=sr.role_id")
                    .addCondition("sur.user_id=?");

            //读取数据库;
            result=this.generateObjectList(
                    this.dataManager
                            .setAttribute(sqlAttri)
                            .setPageSize(0)
                            .setOrderBy(this.getFindOrderBy())
                            .select((Object)(userId))
            );
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |InstantiationException
                |NoSuchMethodException
                |InvocationTargetException
                |SQLException ex)
        {
            result=(List<SystemRole>)this.logAndReturnNull(ex.toString());
        }
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    findMaxRoleType<br>
     * 描述:    获取指定的管理人员对应的最大权限范围<br>
     *
     * @param userId - 管理人员标识
     * @return int - 权限范围。0-全局，1-本级和下级，2-本级
     */
    @Override
    public int findMaxRoleType(int userId)
    {
        int result=-1;
        try
        {
            //设置查询语句
            SqlAttribute sqlAttri=new SqlAttribute()
                    .setColumnNames("max(sr.role_type)")
                    .setTable("system_user_role sur,system_role sr")
                    .addCondition("sur.role_id=sr.role_id")
                    .addCondition("sur.user_id=?");

            //读取数据库;
            ResultSet sqlResult=this.dataManager
                    .setAttribute(sqlAttri)
                    .setPageSize(0)
                    .setOrderBy("")
                    .select((Object)(userId));

            if(sqlResult!=null)
            {
                Object resultObj=null;
                if(sqlResult.next()) resultObj=sqlResult.getObject(1);
                if(resultObj!=null)
                {
                    if(resultObj.getClass().isAssignableFrom(BigDecimal.class))
                    {
                        result=((BigDecimal)resultObj).intValue();
                    }
                    else if(resultObj.getClass().isAssignableFrom(Long.class))
                    {
                        result=((Long)resultObj).intValue();
                    }
                    else
                    {
                        result=(int)resultObj;
                    }
                }
            }
        }
        catch(SQLException ex)
        {
            result=this.logAndReturnInt(ex.toString());
        }
        return result;
    }
}