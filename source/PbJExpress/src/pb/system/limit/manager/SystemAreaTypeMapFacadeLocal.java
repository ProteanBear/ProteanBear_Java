package pb.system.limit.manager;

import java.util.List;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.SystemAreaTypeMap;

/**
 * 数据表映射类数据管理接口—— 行政区域与企业类型多对多对应表
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/11/24
 */
public interface SystemAreaTypeMapFacadeLocal
        extends AbstractFacadeLocal<SystemAreaTypeMap>
{
    /**
     * 描述:    清除指定的行政区域对应的多对多企业类型数据<br>
     *
     * @param areaId - 行政区域标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean removeByAreaId(String areaId);

    /**
     * 描述:    获取指定的行政区域对应的多对多数据<br>
     *
     * @param areaId - 行政区域标识
     * @return List - 返回结果
     */
    List<SystemAreaTypeMap> findByAreaId(String areaId);

    /**
     * 描述:    创建指定行政区域和企业类型的多对多数据<br>
     *
     * @param areaId - 行政区域标识
     * @param typeId - 企业类型标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean create(String areaId,String typeId);
}
