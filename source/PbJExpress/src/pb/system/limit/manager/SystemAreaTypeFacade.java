package pb.system.limit.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.data.SqlAttribute;
import pb.system.limit.entity.SystemAreaType;
import pb.text.DateProcessor;

/**
 * 数据表映射类数据管理类—— 企业类型信息<br>
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/11/23
 */
public class SystemAreaTypeFacade extends AbstractEntityManager<SystemAreaType>
        implements SystemAreaTypeFacadeLocal
{
    /**
     * 域(私有)<br>
     * 名称:    dp<br>
     * 描述:    日期处理器对象<br>
     */
    private DateProcessor dp;

    /**
     * 构造函数<br>
     *
     * @param connector - 指定数据连接器
     */
    public SystemAreaTypeFacade(Connector connector)
    {
        super(connector,SystemAreaType.class);
        this.dp=new DateProcessor("yyyyMMddHHmmss");
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
        return "typeId";
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

        //获取行政区域的主键
        String typeId=null;
        if(obj.getClass().isAssignableFrom(SystemAreaType.class))
        {
            typeId=((SystemAreaType)obj).getTypeId();
        }
        else
        {
            typeId=obj+"";
        }

        //创建多对多企业类型插件管理器对象
        SystemAreaTypePluginFacadeLocal manager=
                new SystemAreaTypePluginFacade(this.connector);

        //清除相关企业类型多对多数据
        manager.removeByTypeId(typeId);
    }

    /**
     * 方法（公共）<br>
     * 名称:    findByAreaid<br>
     * 描述:    获取指定的行政区域对应的企业类型列表<br>
     *
     * @param areaId - 行政区域标识
     * @return List - 系统功能插件列表
     */
    @Override
    public List<SystemAreaType> findByAreaid(String areaId)
    {
        List<SystemAreaType> result=null;
        try
        {
            //设置查询语句
            SqlAttribute sqlAttri=new SqlAttribute()
                    .setColumnNames("sat.*")
                    .setTable("system_area_type_map satm,system_area_type sat")
                    .addCondition("satm.area_id=?")
                    .addCondition("satm.type_id=sat.type_id");

            //读取数据库;
            result=this.generateObjectList(
                    this.dataManager
                            .setAttribute(sqlAttri)
                            .setPageSize(0)
                            .setOrderBy(this.getFindOrderBy())
                            .select(areaId)
            );
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |InstantiationException
                |NoSuchMethodException
                |InvocationTargetException
                |SQLException ex)
        {
            result=(List<SystemAreaType>)this.logAndReturnNull(ex.toString());
        }
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    findMaxTypeLevel<br>
     * 描述:    获取指定的行政区域对应的最大企业类型级别<br>
     *
     * @param areaId - 行政区域标识
     * @return int - 企业类型级别。0-平台级、1-企业级
     */
    @Override
    public int findMaxTypeLevel(String areaId)
    {
        int result=1;
        try
        {
            //设置查询语句
            SqlAttribute sqlAttri=new SqlAttribute()
                    .setColumnNames("min(sat.type_level)")
                    .setTable("system_area_type_map satm,system_area_type sat")
                    .addCondition("satm.type_id=sat.type_id")
                    .addCondition("satm.area_id=?");

            //读取数据库;
            ResultSet sqlResult=this.dataManager
                    .setAttribute(sqlAttri)
                    .setPageSize(0)
                    .setOrderBy("")
                    .select(areaId);

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
