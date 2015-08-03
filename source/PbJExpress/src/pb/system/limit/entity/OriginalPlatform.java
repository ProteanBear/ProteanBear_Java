package pb.system.limit.entity;

/**
 * 数据表映射类——记录系统应用对应的平台信息的基础码表
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class OriginalPlatform
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
     * 名称: platName
     * 描述: 平台显示名称，如iOS（原生）
     */
    protected String platName;

    /**
     * 访问器
     * 目标: platName
     *
     * @return String - 平台显示名称，如iOS（原生）
     */
    public String getPlatName()
    {
        return platName;
    }

    /**
     * 更改器
     * 目标: platName
     *
     * @param platName - 平台显示名称，如iOS（原生）
     */
    public void setPlatName(String platName)
    {
        this.platName=platName;
    }

    /**
     * 域(受保护)
     * 名称: platBelong
     * 描述: 平台名称，如iOS
     */
    protected String platBelong;

    /**
     * 访问器
     * 目标: platBelong
     *
     * @return String - 平台名称，如iOS
     */
    public String getPlatBelong()
    {
        return platBelong;
    }

    /**
     * 更改器
     * 目标: platBelong
     *
     * @param platBelong - 平台名称，如iOS
     */
    public void setPlatBelong(String platBelong)
    {
        this.platBelong=platBelong;
    }

    /**
     * 域(受保护)
     * 名称: platSource
     * 描述: 平台代码类型，0-原生、1-WEB
     */
    protected int platSource;

    /**
     * 访问器
     * 目标: platSource
     *
     * @return int - 平台代码类型，0-原生、1-WEB
     */
    public int getPlatSource()
    {
        return platSource;
    }

    /**
     * 更改器
     * 目标: platSource
     *
     * @param platSource - 平台代码类型，0-原生、1-WEB
     */
    public void setPlatSource(int platSource)
    {
        this.platSource=platSource;
    }

    /**
     * 更改器
     * 目标: platSource
     *
     * @param platSource - 平台代码类型，0-原生、1-WEB
     */
    public void setPlatSource(String platSource)
    {
        this.platSource=new Integer(platSource);
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