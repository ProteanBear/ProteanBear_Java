package pb.system.limit.entity;

/**
 * 数据表映射类——记录系统应用的相关平台的版本信息。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class SystemVersion
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
     * 名称: appPlatId
     * 描述: 记录版本所属的平台标识，对应SYSTEM_ APPLICATION_PLATFORM对应表中的CUST_ID
     */
    protected int appPlatId;

    /**
     * 访问器
     * 目标: appPlatId
     *
     * @return int - 记录版本所属的平台标识，对应SYSTEM_ APPLICATION_PLATFORM对应表中的CUST_ID
     */
    public int getAppPlatId()
    {
        return appPlatId;
    }

    /**
     * 更改器
     * 目标: appPlatId
     *
     * @param appPlatId - 记录版本所属的平台标识，对应SYSTEM_ APPLICATION_PLATFORM对应表中的CUST_ID
     */
    public void setAppPlatId(int appPlatId)
    {
        this.appPlatId=appPlatId;
    }

    /**
     * 更改器
     * 目标: appPlatId
     *
     * @param appPlatId - 记录版本所属的平台标识，对应SYSTEM_ APPLICATION_PLATFORM对应表中的CUST_ID
     */
    public void setAppPlatId(String appPlatId)
    {
        this.appPlatId=new Integer(appPlatId);
    }

    /**
     * 域(受保护)
     * 名称: versionName
     * 描述: 版本的显示名称
     */
    protected String versionName;

    /**
     * 访问器
     * 目标: versionName
     *
     * @return String - 版本的显示名称
     */
    public String getVersionName()
    {
        return versionName;
    }

    /**
     * 更改器
     * 目标: versionName
     *
     * @param versionName - 版本的显示名称
     */
    public void setVersionName(String versionName)
    {
        this.versionName=versionName;
    }

    /**
     * 域(受保护)
     * 名称: versionCode
     * 描述: 版本编号
     */
    protected String versionCode;

    /**
     * 访问器
     * 目标: versionCode
     *
     * @return String - 版本编号
     */
    public String getVersionCode()
    {
        return versionCode;
    }

    /**
     * 更改器
     * 目标: versionCode
     *
     * @param versionCode - 版本编号
     */
    public void setVersionCode(String versionCode)
    {
        this.versionCode=versionCode;
    }

    /**
     * 域(受保护)
     * 名称: versionBuild
     * 描述: 版本打包编号
     */
    protected String versionBuild;

    /**
     * 访问器
     * 目标: versionBuild
     *
     * @return String - 版本打包编号
     */
    public String getVersionBuild()
    {
        return versionBuild;
    }

    /**
     * 更改器
     * 目标: versionBuild
     *
     * @param versionBuild - 版本打包编号
     */
    public void setVersionBuild(String versionBuild)
    {
        this.versionBuild=versionBuild;
    }

    /**
     * 域(受保护)
     * 名称: versionNote
     * 描述: 版本说明
     */
    protected String versionNote;

    /**
     * 访问器
     * 目标: versionNote
     *
     * @return String - 版本说明
     */
    public String getVersionNote()
    {
        return versionNote;
    }

    /**
     * 更改器
     * 目标: versionNote
     *
     * @param versionNote - 版本说明
     */
    public void setVersionNote(String versionNote)
    {
        this.versionNote=versionNote;
    }

    /**
     * 域(受保护)
     * 名称: versionDate
     * 描述: 记录版本的发布日期
     */
    protected String versionDate;

    /**
     * 访问器
     * 目标: versionDate
     *
     * @return String - 记录版本的发布日期
     */
    public String getVersionDate()
    {
        return versionDate;
    }

    /**
     * 更改器
     * 目标: versionDate
     *
     * @param versionDate - 记录版本的发布日期
     */
    public void setVersionDate(String versionDate)
    {
        this.versionDate=versionDate;
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