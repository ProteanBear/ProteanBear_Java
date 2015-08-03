package pb.system.limit.manager;

import java.util.List;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.SystemAreaTypePlugin;

/**
 * 数据表映射类数据管理接口—— 企业类型插件多对多对应表
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/11/24
 */
public interface SystemAreaTypePluginFacadeLocal
        extends AbstractFacadeLocal<SystemAreaTypePlugin>
{
    /**
     * 描述:    清除指定的企业类型对应的多对多数据<br>
     *
     * @param typeId - 企业类型标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean removeByTypeId(String typeId);

    /**
     * 描述:    清除指定的插件标识对应的多对多数据<br>
     *
     * @param pluginId - 插件标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean removeByPluginId(int pluginId);

    /**
     * 描述:    获取指定的企业类型对应的多对多数据<br>
     *
     * @param typeId - 企业类型标识
     * @return List - 返回结果
     */
    List<SystemAreaTypePlugin> findByTypeId(String typeId);

    /**
     * 描述:    创建指定企业类型和插件标识的多对多数据<br>
     *
     * @param typeId   - 企业类型标识
     * @param pluginId - 插件标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean create(String typeId,int pluginId);
}
