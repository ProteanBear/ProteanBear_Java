package pb.system.limit.manager;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.BusiMember;

/**
 * 数据表映射类数据管理接口——记录系统平台的会员信息
 *
 * @author ProteanBear
 * @version 1.00 2016-04-11
 */
public interface BusiMemberFacadeLocal extends AbstractFacadeLocal<BusiMember>
{
    /**
     * 描述:    处理用户登录<br>
     *
     * @param memberLogin - 会员登录使用的关键字,根据登录类型区分
     * @param password    - 用户登录密码
     * @param memberType  - 会员的登录类型、0-邮箱注册、1-手机注册、2-微信、3-QQ、4-微博
     * @return BusiMember - 登录成功返回会员对象，否则返回Null
     */
    BusiMember login(String memberLogin,String password,int memberType);
}