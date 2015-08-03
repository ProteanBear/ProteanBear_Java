package pb.system.limit.manager;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.SystemVersion;

/**
 * 数据表映射类数据管理接口——记录系统应用的相关平台的版本信息。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public interface SystemVersionFacadeLocal extends AbstractFacadeLocal<SystemVersion>
{
    /**
     * 描述:    获取指定的平台应用对应的最新版本数据<br>
     *
     * @param appPlatId - 获取指定平台应用对应的表示
     * @return SystemVersion - 返回最新版本信息
     */
    SystemVersion findLastestByPlatId(int appPlatId);
}