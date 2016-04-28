package pb.system.limit.entity;

/**
 * 数据表映射类——记录企业运维的APP应用的相关信息。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class SystemApplication
{
    /**
     * 域(受保护)
     * 名称: custId
     * 描述: 自增主键
     */
    protected String appId;

    /**
     * 访问器
     * 目标: appId
     *
     * @return String - 自增主键
     */
    public String getAppId()
    {
        return appId;
    }

    /**
     * 更改器
     * 目标: appId
     *
     * @param appId - 自增主键
     */
    public void setAppId(String appId)
    {
        this.appId=appId;
    }

    /**
     * 域(受保护)
     * 名称: areaId
     * 描述: 应用所属的区域标识
     */
    protected String areaId;

    /**
     * 访问器
     * 目标: areaId
     *
     * @return String - 应用所属的区域标识
     */
    public String getAreaId()
    {
        return areaId;
    }

    /**
     * 更改器
     * 目标: areaId
     *
     * @param areaId - 应用所属的区域标识
     */
    public void setAreaId(String areaId)
    {
        this.areaId=areaId;
    }

    /**
     * 域(受保护)
     * 名称: appCode
     * 描述: 记录应用对应的授权编码
     */
    protected String appCode;

    /**
     * 访问器
     * 目标: appCode
     *
     * @return String - 记录应用对应的授权编码
     */
    public String getAppCode()
    {
        return appCode;
    }

    /**
     * 更改器
     * 目标: appCode
     *
     * @param appCode - 记录应用对应的授权编码
     */
    public void setAppCode(String appCode)
    {
        this.appCode=appCode;
    }

    /**
     * 域(受保护)
     * 名称: appName
     * 描述: 记录应用的显示名称
     */
    protected String appName;

    /**
     * 访问器
     * 目标: appName
     *
     * @return String - 记录应用的显示名称
     */
    public String getAppName()
    {
        return appName;
    }

    /**
     * 更改器
     * 目标: appName
     *
     * @param appName - 记录应用的显示名称
     */
    public void setAppName(String appName)
    {
        this.appName=appName;
    }

    /**
     * 域(受保护)
     * 名称: appIcon
     * 描述: 记录APP应用的高清图标链接
     */
    protected String appIcon;

    /**
     * 访问器
     * 目标: appIcon
     *
     * @return String - 记录APP应用的高清图标链接
     */
    public String getAppIcon()
    {
        return appIcon;
    }

    /**
     * 更改器
     * 目标: appIcon
     *
     * @param appIcon - 记录APP应用的高清图标链接
     */
    public void setAppIcon(String appIcon)
    {
        this.appIcon=appIcon;
    }

    /**
     * 域(受保护)
     * 名称: appThumbnail
     * 描述: 记录APP应用的缩略图标链接
     */
    protected String appThumbnail;

    /**
     * 访问器
     * 目标: appThumbnail
     *
     * @return String - 记录APP应用的缩略图标链接
     */
    public String getAppThumbnail()
    {
        return appThumbnail;
    }

    /**
     * 更改器
     * 目标: appThumbnail
     *
     * @param appThumbnail - 记录APP应用的缩略图标链接
     */
    public void setAppThumbnail(String appThumbnail)
    {
        this.appThumbnail=appThumbnail;
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
     * 名称: appAlias
     * 描述: 记录应用的别名
     */
    protected String appAlias;

    /**
     * 访问器
     * 目标: appAlias
     * @return int - 记录应用的别名
     */
    public String getAppAlias()
    {
        return appAlias;
    }

    /**
     * 更改器
     * 目标: appAlias
     * @param appAlias - 记录应用的别名
     */
    public void setAppAlias(String appAlias)
    {
        this.appAlias=appAlias;
    }

    /**
     * 域(受保护)
     * 名称: appWeb
     * 描述: 记录应用的web说明地址
     */
    protected String appWeb;

    /**
     * 访问器
     * 目标: appWeb
     * @return int - 记录应用的web说明地址
     */
    public String getAppWeb()
    {
        return appWeb;
    }

    /**
     * 更改器
     * 目标: appWeb
     * @param appWeb - 记录应用的web说明地址
     */
    public void setAppWeb(String appWeb)
    {
        this.appWeb=appWeb;
    }
}