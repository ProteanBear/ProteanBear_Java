package pb.system.limit.manager;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.SystemApplication;

/**
 * 数据表映射类数据管理接口——记录企业运维的APP应用的相关信息。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public interface SystemApplicationFacadeLocal extends AbstractFacadeLocal<SystemApplication>
{
    /**
     * 描述:    根据指定编码获取应用数据<br>
     *
     * @param code - 指定的数据编码
     * @return SystemApplication - 应用数据
     */
    SystemApplication findByAppCode(String code);
}