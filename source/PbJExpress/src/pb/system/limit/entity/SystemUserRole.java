package pb.system.limit.entity;

/**
 * 数据表映射类——记录企业类型数据表和功能插件数据表的多对多数据关系。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class SystemUserRole
{
    /**
     * 域(受保护)
     * 名称: custId
     * 描述: 自增主键
     */
    protected int custId;

    /**
     * 访问器
     * 目标: custId
     *
     * @return int - 自增主键
     */
    public int getCustId()
    {
        return custId;
    }

    /**
     * 更改器
     * 目标: custId
     *
     * @param custId - 自增主键
     */
    public void setCustId(int custId)
    {
        this.custId=custId;
    }

    /**
     * 更改器
     * 目标: custId
     *
     * @param custId - 自增主键
     */
    public void setCustId(String custId)
    {
        this.custId=new Integer(custId);
    }

    /**
     * 域(受保护)
     * 名称: userId
     * 描述: 用户标识
     */
    protected int userId;

    /**
     * 访问器
     * 目标: userId
     *
     * @return int - 用户标识
     */
    public int getUserId()
    {
        return userId;
    }

    /**
     * 更改器
     * 目标: userId
     *
     * @param userId - 用户标识
     */
    public void setUserId(int userId)
    {
        this.userId=userId;
    }

    /**
     * 更改器
     * 目标: userId
     *
     * @param userId - 用户标识
     */
    public void setUserId(String userId)
    {
        this.userId=new Integer(userId);
    }

    /**
     * 域(受保护)
     * 名称: roleId
     * 描述: 角色标识
     */
    protected String roleId;

    /**
     * 访问器
     * 目标: roleId
     *
     * @return String - 角色标识
     */
    public String getRoleId()
    {
        return roleId;
    }

    /**
     * 更改器
     * 目标: roleId
     *
     * @param roleId - 角色标识
     */
    public void setRoleId(String roleId)
    {
        this.roleId=roleId;
    }

}