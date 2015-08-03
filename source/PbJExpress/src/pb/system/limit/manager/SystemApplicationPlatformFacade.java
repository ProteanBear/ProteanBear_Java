package pb.system.limit.manager;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.data.SqlAttribute;
import pb.system.limit.entity.SystemApplicationPlatform;

/**
 * 数据表映射类数据管理类——记录企业应用对应应用平台数据的多对多对应表
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class SystemApplicationPlatformFacade
        extends AbstractEntityManager<SystemApplicationPlatform>
        implements SystemApplicationPlatformFacadeLocal
{
    /**
     * 构造函数
     *
     * @param connector - 指定数据连接器
     */
    public SystemApplicationPlatformFacade(Connector connector)
    {
        super(connector,SystemApplicationPlatform.class);
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
     * 方法（公共）<br>
     * 名称:    create<br>
     * 描述:    创建指定应用和平台的多对多数据<br>
     *
     * @param appId  - 应用标识
     * @param platId - 平台标识
     */
    @Override
    public boolean create(String appId,String platId)
    {
        //构建实体对象
        SystemApplicationPlatform plat=new SystemApplicationPlatform();
        plat.setAppId(appId);
        plat.setPlatId(platId);

        //构造查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("app_id=?",appId);
        condition.put("plat_id=?",platId);

        //如果数据已存在，返回true
        if(this.exist(condition))
        {
            return true;
        }
        //如果数据不存在，添加数据
        else
        {
            return super.create(plat);
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    removeByAppId<br>
     * 描述:    清除指定的应用对应的多对多企业类型数据<br>
     *
     * @param appId - 行政区域标识
     * @return boolean - 如果清除成功，返回true
     */
    @Override
    public boolean removeByAppId(String appId)
    {
        boolean success=false;
        try
        {
            //构造删除语句
            SqlAttribute sqlAttr=new SqlAttribute()
                    .setTable("SYSTEM_APPLICATION_PLATFORM")
                    .addCondition("app_id=?");

            //删除数据
            success=this.dataManager.setAttribute(sqlAttr).delete(appId);
        }
        catch(Exception ex)
        {
            success=this.logAndReturnFalse(ex.toString());
        }
        return success;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    findByAppId<br>
     * 描述:    获取指定的应用对应的多对多数据<br>
     *
     * @param appId - 行政区域标识
     * @return List - 返回结果
     */
    @Override
    public List<SystemApplicationPlatform> findByAppId(String appId)
    {
        //生成查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("appId=?",appId);

        return this.find(condition);
    }

    /**
     * 方法（公共）<br>
     * 名称:    findAppPlatIdBy<br>
     * 描述:    获取指定的应用数据<br>
     *
     * @param appId  - 应用标识
     * @param plat   - 平台所属
     * @param source - 源码类型
     * @return List - 返回结果
     */
    @Override
    public int findAppPlatIdBy(String appId,String plat,int source)
    {
        int result=-1;
        try
        {
            //设置查询语句
            SqlAttribute sqlAttri=new SqlAttribute()
                    .setColumnNames("max(sap.cust_id)")
                    .setTable("original_platform op,system_application_platform sap,system_application sa")
                    .addCondition("op.cust_id=sap.plat_id")
                    .addCondition("sap.app_id=sa.app_id")
                    .addCondition("sa.app_code=?")
                    .addCondition("op.plat_belong=?")
                    .addCondition("op.plat_source=?");

            //读取数据库;
            ResultSet sqlResult=this.dataManager
                    .setAttribute(sqlAttri)
                    .setPageSize(0)
                    .setOrderBy("")
                    .select(appId,plat,source);

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
                    if(resultObj.getClass().isAssignableFrom(Long.class))
                    {
                        result=((Long)resultObj).intValue();
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