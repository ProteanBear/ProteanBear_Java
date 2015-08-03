package pb.system.limit.manager;

import java.util.List;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.SystemPlugin;

/**
 * 数据表映射类数据管理接口—— 系统功能插件
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/11/23
 */
public interface SystemPluginFacadeLocal extends AbstractFacadeLocal<SystemPlugin>
{
    /**
     * 描述:    重载并标识为过期方法，添加对应的数据请使用特定类型添加方法<br>
     *
     * @param entity - 添加记录的描述对象
     * @return boolean - 添加成功，返回true;
     */
    @Deprecated
    @Override
    boolean create(SystemPlugin entity);

    /**
     * 描述:    添加新的数据到数据库中<br>
     *
     * @param upId   - 指定上一层主键标识
     * @param entity - 添加记录的描述对象
     * @return boolean - 添加成功，返回true;
     */
    boolean create(String upId,SystemPlugin entity);

    /**
     * 描述:    获取指定区域的下一级所有数据<br>
     *
     * @param upId - 指定的顶层数据标识
     * @return List - 下一级数据列表
     */
    List<SystemPlugin> childrenNextDepth(String upId);

    /**
     * 描述:    获取指定的插件标识相关下级的插件权限数据<br>
     *
     * @param entity - 顶级数据对象
     * @return SystemPlugin - 系统功能插件列表
     */
    boolean removeAllSubPlugins(SystemPlugin entity);

    /**
     * 描述:    获取指定区域的所有低层次区域<br>
     *
     * @param upId - 指定的顶层区域标识
     * @return List - 低层次所有区域列表
     */
    List<SystemPlugin> childrenAllDownDepth(String upId);

    /**
     * 描述:    获取数据表中的所有插件类型数据<br>
     *
     * @return List - 数据列表
     */
    List<SystemPlugin> findAllPlugin();

    /**
     * 描述:    获取指定的行政区域对应的系统功能插件列表<br>
     *
     * @param areaId - 行政区域标识
     * @return List - 系统功能插件列表
     */
    List<SystemPlugin> findByAreaId(String areaId);

    /**
     * 描述:    获取指定的企业类型对应的系统功能插件列表<br>
     *
     * @param typeId - 企业类型标识
     * @return List - 系统功能插件列表
     */
    List<SystemPlugin> findByTypeId(String typeId);

    /**
     * 描述:    获取指定的管理角色对应的功能权限列表<br>
     *
     * @param roleId - 管理角色标识
     * @return List - 功能权限列表
     */
    List<SystemPlugin> findByRoleId(String roleId);

    /**
     * 描述:    获取指定的管理人员对应的功能权限列表<br>
     *
     * @param userId - 管理人员标识
     * @return List - 功能权限列表
     */
    List<SystemPlugin> findByUserId(int userId);

    /**
     * 描述:    判断指定的主键标识对应的数据为已存在的插件类型<br>
     *
     * @param id - 插件标识
     * @return List - 功能权限列表
     */
    boolean existPlugin(Object id);

    /**
     * 描述:    判断指定的主键标识对应的数据为已存在的权限类型<br>
     *
     * @param id - 插件标识
     * @return List - 功能权限列表
     */
    boolean existLimit(Object id);
}