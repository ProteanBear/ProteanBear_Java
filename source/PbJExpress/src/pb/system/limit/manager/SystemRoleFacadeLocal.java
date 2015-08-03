package pb.system.limit.manager;

import java.util.List;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.SystemRole;

/**
 * 数据表映射类数据管理接口——记录系统用户角色的相关数据信息
 *
 * @author ProteanBear
 * @version 1.00 2014-07-15
 */
public interface SystemRoleFacadeLocal extends AbstractFacadeLocal<SystemRole>
{
    /**
     * 描述:    获取指定的管理人员对应的用户角色列表<br>
     *
     * @param userId - 管理人员标识
     * @return List - 用户角色列表
     */
    List<SystemRole> findByUserId(int userId);

    /**
     * 描述:    获取指定的管理人员对应的最大权限范围<br>
     *
     * @param userId - 管理人员标识
     * @return int - 权限范围。0-全局，1-本级和下级，2-本级
     */
    int findMaxRoleType(int userId);
}