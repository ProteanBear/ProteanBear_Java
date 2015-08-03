package pb.system.limit.entity;

/**
 * 数据表映射类——企业类型数据表，可以理解为功能插件的集合，系统可以设定不同的企业类型，不同的企业类型可以包含不同的功能模块及其插件，依据此为不同进入平台的企业提供不同的功能插件
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class SystemAreaType
{
    /**
     * 域(受保护)
     * 名称: typeId
     * 描述: 主键，使用时间戳格式yyyyMMddhhmmss
     */
    protected String typeId;

    /**
     * 访问器
     * 目标: typeId
     *
     * @return String - 主键，使用时间戳格式yyyyMMddhhmmss
     */
    public String getTypeId()
    {
        return typeId;
    }

    /**
     * 更改器
     * 目标: typeId
     *
     * @param typeId - 主键，使用时间戳格式yyyyMMddhhmmss
     */
    public void setTypeId(String typeId)
    {
        this.typeId=typeId;
    }

    /**
     * 域(受保护)
     * 名称: typeName
     * 描述: 类型的显示名称
     */
    protected String typeName;

    /**
     * 访问器
     * 目标: typeName
     *
     * @return String - 类型的显示名称
     */
    public String getTypeName()
    {
        return typeName;
    }

    /**
     * 更改器
     * 目标: typeName
     *
     * @param typeName - 类型的显示名称
     */
    public void setTypeName(String typeName)
    {
        this.typeName=typeName;
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

    /**
     * 域(受保护)
     * 名称: typeLevel
     * 描述: 类型级别，0-平台级、1-企业级
     */
    protected int typeLevel;

    /**
     * 访问器
     * 目标: typeLevel
     *
     * @return int - 类型级别，0-平台级、1-企业级
     */
    public int getTypeLevel()
    {
        return typeLevel;
    }

    /**
     * 更改器
     * 目标: typeLevel
     *
     * @param typeLevel - 类型级别，0-平台级、1-企业级
     */
    public void setTypeLevel(int typeLevel)
    {
        this.typeLevel=typeLevel;
    }

    /**
     * 更改器
     * 目标: typeLevel
     *
     * @param typeLevel - 类型级别，0-平台级、1-企业级
     */
    public void setTypeLevel(String typeLevel)
    {
        this.typeLevel=new Integer(typeLevel);
    }

}