package pb.system.limit.module;

import java.util.List;

import pb.system.limit.entity.SystemUser;
import pb.system.limit.entity.SystemUserRole;

/**
 * 数据表输出类——用户信息表。<br>
 * 记录用户角色的相关信息。<br>
 * 继承自数据表映射类，添加角色列表属性。<br>
 *
 * @author proteanBear(马强)
 * @version 1.00 2012/02/29
 */
public class SystemUserOutput extends SystemUser
{
    /**
     * 域(受保护)<br>
     * 名称:    userRoles<br>
     * 描述:    记录用户对应的角色列表,插件之间以“,”分隔<br>
     */
    protected String[] userRoles;

    /**
     * 构造函数<br>
     *
     * @param user - 管理用户
     */
    public SystemUserOutput(SystemUser user)
    {
        //this.userId=user.getUserId();
        this.userName=user.getUserName();
        this.areaId=user.getAreaId();
        this.areaName=user.getAreaName();
        this.userIcon=user.getUserIcon();
        //this.password="";
    }

    /**
     * 访问器<br>
     * 目标：   userRoles<br>
     *
     * @return String - 用户对应的角色列表
     */
    public String[] getUserRoles()
    {
        return userRoles;
    }

    /**
     * 更改器<br>
     * 目标：   userRoles<br>
     *
     * @param userRoles - 用户对应的角色列表
     */
    public void setUserRoles(List<SystemUserRole> userRoles)
    {
        if(userRoles!=null)
        {
            this.userRoles=new String[userRoles.size()];
            for(int i=0;i<userRoles.size();i++)
            {
                //this.userRoles[i]=userRoles.get(i).getRoleId();
            }
        }
    }
}
