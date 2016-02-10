package pb.system.limit.entity;

/**
 * 数据表映射类——记录企业应用对应应用平台数据的多对多对应表
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class SystemApplicationPlatform
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
     * 名称: platId
     * 描述: 平台标识
     */
    protected int platId;

    /**
     * 访问器
     * 目标: platId
     *
     * @return int - 平台标识
     */
    public int getPlatId()
    {
        return platId;
    }

    /**
     * 更改器
     * 目标: platId
     *
     * @param platId - 平台标识
     */
    public void setPlatId(int platId)
    {
        this.platId=platId;
    }

    /**
     * 更改器
     * 目标: platId
     *
     * @param platId - 平台标识
     */
    public void setPlatId(String platId)
    {
        this.platId=new Integer(platId);
    }

    /**
     * 域(受保护)
     * 名称: appId
     * 描述: 应用标识
     */
    protected String appId;

    /**
     * 访问器
     * 目标: appId
     *
     * @return int - 应用标识
     */
    public String getAppId()
    {
        return appId;
    }

    /**
     * 更改器
     * 目标: appId
     *
     * @param appId - 应用标识
     */
    public void setAppId(String appId)
    {
        this.appId=appId;
    }

}