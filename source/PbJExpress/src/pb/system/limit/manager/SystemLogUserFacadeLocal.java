package pb.system.limit.manager;

import java.util.List;
import java.util.Map;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.SystemLogUser;

/**
 * 数据表映射类数据管理接口—— 用户操作日志表。<br>
 * 1.01 - 修改增加分区查询方法。<br>
 *
 * @author proteanBear(马强)
 * @version 1.01 2012/03/22
 */
public interface SystemLogUserFacadeLocal
        extends AbstractFacadeLocal<SystemLogUser>
{
    /**
     * 描述:    查询指定执行时间的相关数据<br>
     *
     * @param condition - 筛选条件
     * @param dataTime  - 执行时间，时间格式：yyyy-MM-dd
     * @return List<SystemLogUser> - 数据列表
     */
    List<SystemLogUser> find(Map<String,Object> condition,String dataTime);
}
