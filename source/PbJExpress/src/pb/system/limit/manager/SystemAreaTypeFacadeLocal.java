package pb.system.limit.manager;

import java.util.List;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.SystemAreaType;

/**
 * 数据表映射类数据管理接口—— 企业类型信息
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/11/23
 */
public interface SystemAreaTypeFacadeLocal extends AbstractFacadeLocal<SystemAreaType>
{
    /**
     * 描述:    获取指定的行政区域对应的企业类型列表<br>
     *
     * @param areaId - 行政区域标识
     * @return List - 企业类型列表
     */
    List<SystemAreaType> findByAreaid(String areaId);

    /**
     * 描述:    获取指定的行政区域对应的最大企业类型级别<br>
     *
     * @param areaId - 行政区域标识
     * @return int - 企业类型级别。0-平台级、1-企业级
     */
    int findMaxTypeLevel(String areaId);
}
