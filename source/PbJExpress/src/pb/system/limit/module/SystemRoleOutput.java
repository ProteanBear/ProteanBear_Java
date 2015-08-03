package pb.system.limit.module;

import java.util.List;

import pb.system.limit.entity.SystemRole;
import pb.system.limit.entity.SystemRolePlugin;

/**
 * 数据表输出类——用户角色信息表。<br>
 * 记录角色权限的相关信息。<br>
 * 继承自数据表映射类，添加权限列表属性。<br>
 *
 * @author proteanBear(马强)
 * @version 1.00 2012/02/29
 */
public class SystemRoleOutput extends SystemRole
{
    /**
     * 域(受保护)<br>
     * 名称:    roleLimits<br>
     * 描述:    记录用户角色对应的权限列表<br>
     */
    protected Integer[] roleLimits;

    /**
     * 构造函数<br>
     *
     * @param role - 用户角色
     */
    public SystemRoleOutput(SystemRole role)
    {
        this.roleId=role.getRoleId();
        this.roleName=role.getRoleName();
        this.roleType=role.getRoleType();
        this.dataDelete=role.getDataDelete();
        this.dataRemark=role.getDataRemark();
        //this.roleDescription=role.getRoleDescription();
    }

    /**
     * 访问器<br>
     * 目标：   roleLimits<br>
     *
     * @return Integer - 用户角色对应的权限列表
     */
    public Integer[] getRoleLimits()
    {
        return roleLimits;
    }

    /**
     * 更改器<br>
     * 目标：   roleLimits<br>
     *
     * @param roleLimits - 用户角色对应的权限列表
     */
    public void setRoleLimits(List<SystemRolePlugin> roleLimits)
    {
        if(roleLimits!=null)
        {
            this.roleLimits=new Integer[roleLimits.size()];
            for(int i=0;i<roleLimits.size();i++)
            {
                this.roleLimits[i]=roleLimits.get(i).getPluginId();
            }
        }
    }
}
