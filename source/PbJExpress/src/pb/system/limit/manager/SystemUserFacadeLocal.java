package pb.system.limit.manager;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.SystemUser;

/**
 * 数据表映射类数据管理接口——记录系统管理用户的相关数据信息
 *
 * @author ProteanBear
 * @version 1.00 2014-07-15
 */
public interface SystemUserFacadeLocal extends AbstractFacadeLocal<SystemUser>
{
    /**
     * 描述:    处理用户登录<br>
     *
     * @param userName   - 管理人员登录名称
     * @param password - 用户登录密码
     * @return SystemUser - 登录成功返回管理人员对象，否则返回Null
     */
    SystemUser login(String userName,String password);

    /**
     * 描述:    清除指定的行政区域对应的管理人员数据<br>
     *
     * @param areaId - 行政区域标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean removeByAreaId(String areaId);
}