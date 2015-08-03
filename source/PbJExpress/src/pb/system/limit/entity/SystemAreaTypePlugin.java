package pb.system.limit.entity;

/**
 * 数据表映射类——记录企业类型数据表和功能插件数据表的多对多数据关系。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class SystemAreaTypePlugin
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
     * 名称: pluginCustId
     * 描述: 功能插件标识
     */
    protected int pluginCustId;

    /**
     * 访问器
     * 目标: pluginCustId
     *
     * @return int - 功能插件标识
     */
    public int getPluginCustId()
    {
        return pluginCustId;
    }

    /**
     * 更改器
     * 目标: pluginCustId
     *
     * @param pluginCustId - 功能插件标识
     */
    public void setPluginCustId(int pluginCustId)
    {
        this.pluginCustId=pluginCustId;
    }

    /**
     * 更改器
     * 目标: pluginCustId
     *
     * @param pluginCustId - 功能插件标识
     */
    public void setPluginCustId(String pluginCustId)
    {
        this.pluginCustId=new Integer(pluginCustId);
    }

    /**
     * 域(受保护)
     * 名称: typeId
     * 描述: 企业类型标识
     */
    protected String typeId;

    /**
     * 访问器
     * 目标: typeId
     *
     * @return String - 企业类型标识
     */
    public String getTypeId()
    {
        return typeId;
    }

    /**
     * 更改器
     * 目标: typeId
     *
     * @param typeId - 企业类型标识
     */
    public void setTypeId(String typeId)
    {
        this.typeId=typeId;
    }

}