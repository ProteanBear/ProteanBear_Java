package pb.system.limit.entity;

/**
 * 数据表映射类——记录企业类型数据表和功能插件数据表的多对多数据关系。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class SystemRolePlugin
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
    public void setCustId(Integer custId)
    {
        this.custId=custId.intValue();
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
     * 名称: roleId
     * 描述: 用户角色标识
     */
    protected String roleId;

    /**
     * 访问器
     * 目标: roleId
     *
     * @return int - 用户角色标识
     */
    public String getRoleId()
    {
        return roleId;
    }

    /**
     * 更改器
     * 目标: roleId
     *
     * @param roleId - 用户角色标识
     */
    public void setRoleId(String roleId)
    {
        this.roleId=roleId;
    }

    /**
     * 域(受保护)
     * 名称: pluginId
     * 描述: 插件权限标识
     */
    protected int pluginId;

    /**
     * 访问器
     * 目标: pluginId
     *
     * @return int - 插件权限标识
     */
    public int getPluginId()
    {
        return pluginId;
    }

    /**
     * 更改器
     * 目标: pluginId
     *
     * @param pluginId - 插件权限标识
     */
    public void setPluginId(int pluginId)
    {
        this.pluginId=pluginId;
    }

    /**
     * 更改器
     * 目标: pluginId
     *
     * @param pluginId - 插件权限标识
     */
    public void setPluginId(String pluginId)
    {
        this.pluginId=new Integer(pluginId);
    }

}