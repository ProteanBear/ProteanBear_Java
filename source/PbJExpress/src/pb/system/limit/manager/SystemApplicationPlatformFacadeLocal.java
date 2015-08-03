package pb.system.limit.manager;

import java.util.List;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.SystemApplicationPlatform;

/**
 * 数据表映射类数据管理接口——记录企业应用对应应用平台数据的多对多对应表
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public interface SystemApplicationPlatformFacadeLocal
        extends AbstractFacadeLocal<SystemApplicationPlatform>
{
    /**
     * 描述:    清除指定的应用对应的多对多企业类型数据<br>
     *
     * @param appId - 行政区域标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean removeByAppId(String appId);

    /**
     * 描述:    获取指定的应用对应的多对多数据<br>
     *
     * @param appId - 行政区域标识
     * @return List - 返回结果
     */
    List<SystemApplicationPlatform> findByAppId(String appId);

    /**
     * 描述:    获取指定的应用对应的多对多数据<br>
     *
     * @param appId  - 应用标识
     * @param plat   - 平台所属
     * @param source - 源码类型
     * @return List - 返回结果
     */
    int findAppPlatIdBy(String appId,String plat,int source);

    /**
     * 描述:    创建指定应用和平台的多对多数据<br>
     *
     * @param appId  - 应用标识
     * @param platId - 企业类型标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean create(String appId,String platId);
}