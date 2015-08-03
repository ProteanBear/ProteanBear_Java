package pb.system.limit.manager;

import java.util.List;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.SystemRolePlugin;

/**
 * 数据表映射类数据管理接口——记录企业类型数据表和功能插件数据表的多对多数据关系。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-15
 */
public interface SystemRolePluginFacadeLocal extends AbstractFacadeLocal<SystemRolePlugin>
{
    /**
     * 描述:    清除指定的插件标识对应的多对多数据<br>
     *
     * @param pluginId - 插件标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean removeByPluginId(int pluginId);

    /**
     * 描述:    清除指定的管理角色对应的多对多数据<br>
     *
     * @param roleId - 管理角色标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean removeByRoleId(String roleId);

    /**
     * 描述:    获取指定的管理角色对应的多对多数据<br>
     *
     * @param roleId - 管理角色标识
     * @return List - 返回结果
     */
    List<SystemRolePlugin> findByRoleId(String roleId);

    /**
     * 描述:    创建指定管理角色和功能权限的多对多数据<br>
     *
     * @param roleId        - 管理角色标识
     * @param limitPluginId - 功能权限标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean create(String roleId,String limitPluginId);
}