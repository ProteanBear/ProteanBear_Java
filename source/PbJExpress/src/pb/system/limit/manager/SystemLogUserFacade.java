package pb.system.limit.manager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.data.EntityTransformer;
import pb.data.SqlAttribute;
import pb.system.limit.entity.SystemLogUser;

/**
 * 数据表映射类数据管理类—— 用户操作日志表。<br>
 * 1.01 - 修改增加分区查询方法。<br>
 *
 * @author proteanBear(马强)
 * @version 1.01 2012/03/22
 */
public class SystemLogUserFacade
        extends AbstractEntityManager<SystemLogUser>
        implements SystemLogUserFacadeLocal
{
    /*-----------------------------开始：静态内容------------------------------*/

    /**
     * 静态常量(公共)<br>
     * 名称:    logPartitionName<br>
     * 描述:    记录用户日志分区的名称<br>
     */
    public static final String logPartitionName="GPS_PARTITION_";
    
    /*-----------------------------结束：静态内容------------------------------*/

    /**
     * 构造函数
     *
     * @param connector
     */
    public SystemLogUserFacade(Connector connector)
    {
        super(connector,SystemLogUser.class);
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

    /**
     * 方法（公共）<br>
     * 名称:    find<br>
     * 描述:    查询指定执行时间的相关数据<br>
     *
     * @param condition - 筛选条件
     * @param dataTime  - 执行时间，时间格式：yyyy-MM-dd
     */
    @Override
    public List<SystemLogUser> find
    (Map<String,Object> condition,String dataTime)
    {
        List<SystemLogUser> result=null;
        try
        {
            //计算定位时间对应的分区
            int partition=this.calculatePartition(dataTime);
            if(partition==-1) return result;

            //设置查询条件
            SqlAttribute sqlAttr=EntityTransformer.generateSelectSqlFromEntity(
                    entityClass,
                    condition,
                    "SYSTEM_LOG_USER partition("+logPartitionName+partition+")"
            );

            //查询结果
            result=this.generateObjectList(
                    this.dataManager
                            .setPageSize(0)
                            .setAttribute(sqlAttr)
                            .select(EntityTransformer.generateConditionParamsFromEntity(condition))
            );
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |InstantiationException
                |NoSuchMethodException
                |InvocationTargetException
                |SQLException ex)
        {
            result=(List<SystemLogUser>)this.logAndReturnNull(ex.toString());
        }
        return result;
    }

    /**
     * 方法（私有）<br>
     * 名称:    calculatePartition<br>
     * 描述:    计算定位时间对应的分区编号<br>
     *
     * @param locTime - 定位时间，时间格式：yyyy-MM-dd HH
     * @return int - 分区编号
     */
    private int calculatePartition(String dataTime)
    {
        if(dataTime==null) return this.logAndReturnInt("定位时间条件为空");
        dataTime=dataTime.trim();

        int first=dataTime.indexOf("-");
        int last=dataTime.lastIndexOf("-");
        if(first==-1 || last==-1) return this.logAndReturnInt("定位时间条件解析错误");

        //获取定位时间的月份值
        String monthStr=dataTime.substring(first+1,last);
        int month=Integer.parseInt(monthStr);

        //获取定位时间的日期值
        String dayStr=dataTime.substring(last+1);
        int day=Integer.parseInt(dayStr);

        //计算定位时间对应的分区
        int total=10;
        return (((month-1))*31+day)%total;
    }
}