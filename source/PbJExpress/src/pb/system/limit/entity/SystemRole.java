package pb.system.limit.entity;

/**
 * 数据表映射类——记录系统用户角色的相关数据信息
 *
 * @author ProteanBear
 * @version 1.00 2014-07-16
 */
public class SystemRole
{
    /**
     * 域(受保护)
     * 名称: roleId
     * 描述: 主键，使用时间戳格式yyyyMMddhhmmss
     */
    protected String roleId;

    /**
     * 访问器
     * 目标: roleId
     *
     * @return String - 主键，使用时间戳格式yyyyMMddhhmmss
     */
    public String getRoleId()
    {
        return roleId;
    }

    /**
     * 更改器
     * 目标: roleId
     *
     * @param roleId - 主键，使用时间戳格式yyyyMMddhhmmss
     */
    public void setRoleId(String roleId)
    {
        this.roleId=roleId;
    }

    /**
     * 域(受保护)
     * 名称: roleName
     * 描述: 记录角色的显示名称
     */
    protected String roleName;

    /**
     * 访问器
     * 目标: roleName
     *
     * @return String - 记录角色的显示名称
     */
    public String getRoleName()
    {
        return roleName;
    }

    /**
     * 更改器
     * 目标: roleName
     *
     * @param roleName - 记录角色的显示名称
     */
    public void setRoleName(String roleName)
    {
        this.roleName=roleName;
    }

    /**
     * 域(受保护)
     * 名称: roleType
     * 描述: 记录角色的权限类型，0-平台全局、1-区域全局、2-区域下级、3-本级
     */
    protected int roleType;

    /**
     * 访问器
     * 目标: roleType
     *
     * @return int - 记录角色的权限类型，0-平台全局、1-区域全局、2-区域下级、3-本级
     */
    public int getRoleType()
    {
        return roleType;
    }

    /**
     * 更改器
     * 目标: roleType
     *
     * @param roleType - 记录角色的权限类型，0-平台全局、1-区域全局、2-区域下级、3-本级
     */
    public void setRoleType(int roleType)
    {
        this.roleType=roleType;
    }

    /**
     * 更改器
     * 目标: roleType
     *
     * @param roleType - 记录角色的权限类型，0-平台全局、1-区域全局、2-区域下级、3-本级
     */
    public void setRoleType(String roleType)
    {
        this.roleType=new Integer(roleType);
    }

    /**
     * 域(受保护)
     * 名称: dataRemark
     * 描述: 数据备注信息
     */
    protected String dataRemark;

    /**
     * 访问器
     * 目标: dataRemark
     *
     * @return String - 数据备注信息
     */
    public String getDataRemark()
    {
        return dataRemark;
    }

    /**
     * 更改器
     * 目标: dataRemark
     *
     * @param dataRemark - 数据备注信息
     */
    public void setDataRemark(String dataRemark)
    {
        this.dataRemark=dataRemark;
    }

    /**
     * 域(受保护)
     * 名称: dataDelete
     * 描述: 数据删除标志位
     */
    protected int dataDelete;

    /**
     * 访问器
     * 目标: dataDelete
     *
     * @return int - 数据删除标志位
     */
    public int getDataDelete()
    {
        return dataDelete;
    }

    /**
     * 更改器
     * 目标: dataDelete
     *
     * @param dataDelete - 数据删除标志位
     */
    public void setDataDelete(int dataDelete)
    {
        this.dataDelete=dataDelete;
    }

    /**
     * 更改器
     * 目标: dataDelete
     *
     * @param dataDelete - 数据删除标志位
     */
    public void setDataDelete(String dataDelete)
    {
        this.dataDelete=new Integer(dataDelete);
    }
}