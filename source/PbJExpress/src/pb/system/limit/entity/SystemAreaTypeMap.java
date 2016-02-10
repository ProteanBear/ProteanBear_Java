package pb.system.limit.entity;

/**
 * 数据表映射类——记录行政区域数据表和企业类型数据表的多对多数据关系。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class SystemAreaTypeMap
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
     * 名称: areaId
     * 描述: 行政区域标识
     */
    protected String areaId;

    /**
     * 访问器
     * 目标: areaId
     *
     * @return String - 行政区域标识
     */
    public String getAreaId()
    {
        return areaId;
    }

    /**
     * 更改器
     * 目标: areaId
     *
     * @param areaId - 行政区域标识
     */
    public void setAreaId(String areaId)
    {
        this.areaId=areaId;
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