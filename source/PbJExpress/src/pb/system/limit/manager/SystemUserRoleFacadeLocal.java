package pb.system.limit.manager;

import java.util.List;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.SystemUserRole;

/**
 * 数据表映射类数据管理接口——记录企业类型数据表和功能插件数据表的多对多数据关系。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-15
 */
public interface SystemUserRoleFacadeLocal extends AbstractFacadeLocal<SystemUserRole>
{
    /**
     * 描述:    清除指定的管理人员对应的多对多数据<br>
     *
     * @param userId - 管理人员标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean removeByUserId(int userId);

    /**
     * 描述:    获取指定的管理人员对应的多对多数据<br>
     *
     * @param userId - 管理角色标识
     * @return List - 返回结果
     */
    List<SystemUserRole> findByUserId(int userId);

    /**
     * 描述:    创建指定管理人员和管理角色的多对多数据<br>
     *
     * @param userId - 管理人员标识
     * @param roleId - 管理角色标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean create(int userId,String roleId);
}