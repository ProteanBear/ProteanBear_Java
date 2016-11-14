package pb.system.limit.entity;

/**
 * 数据表映射类——系统插件数据表，记录当前系统支持的全部功能模块，每个功能模块包含功能插件集合；功能插件为具体的功能，如文章管理，可对应到具体的操作页面，每个功能插件包含一个功能
 *
 * @author ProteanBear
 * @version 1.00 2014-07-19
 */
public class SystemPlugin
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
     * 名称: pluginId
     * 描述: 插件英文标识
     */
    protected String pluginId;

    /**
     * 访问器
     * 目标: pluginId
     *
     * @return String - 插件英文标识
     */
    public String getPluginId()
    {
        return pluginId;
    }

    /**
     * 更改器
     * 目标: pluginId
     *
     * @param pluginId - 插件英文标识
     */
    public void setPluginId(String pluginId)
    {
        this.pluginId=pluginId;
    }

    /**
     * 域(受保护)
     * 名称: pluginCode
     * 描述: 权限对应的编码，使用数字构成树形结构。如顶级为1000，下级的第一个为10000000，第二个为10000001以此类推，限制到3层；其中最后权限层为两位数字。
     */
    protected String pluginCode;

    /**
     * 访问器
     * 目标: pluginCode
     *
     * @return String - 权限对应的编码，使用数字构成树形结构。如顶级为1000，下级的第一个为10000000，第二个为10000001以此类推，限制到3层；其中最后权限层为两位数字。
     */
    public String getPluginCode()
    {
        return pluginCode;
    }

    /**
     * 更改器
     * 目标: pluginCode
     *
     * @param pluginCode - 权限对应的编码，使用数字构成树形结构。如顶级为1000，下级的第一个为10000000，第二个为10000001以此类推，限制到3层；其中最后权限层为两位数字。
     */
    public void setPluginCode(String pluginCode)
    {
        this.pluginCode=pluginCode;
    }

    /**
     * 域(受保护)
     * 名称: pluginName
     * 描述: 插件的显示名称
     */
    protected String pluginName;

    /**
     * 访问器
     * 目标: pluginName
     *
     * @return String - 插件的显示名称
     */
    public String getPluginName()
    {
        return pluginName;
    }

    /**
     * 更改器
     * 目标: pluginName
     *
     * @param pluginName - 插件的显示名称
     */
    public void setPluginName(String pluginName)
    {
        this.pluginName=pluginName;
    }

    /**
     * 域(受保护)
     * 名称: pluginParent
     * 描述: 插件上级模块的名称
     */
    protected String pluginParent;

    /**
     * 访问器
     * 目标: pluginParent
     *
     * @return String - 插件上级模块的名称
     */
    public String getPluginParent()
    {
        return pluginParent==null?"":pluginParent;
    }

    /**
     * 更改器
     * 目标: pluginParent
     *
     * @param pluginParent - 插件上级模块的名称
     */
    public void setPluginParent(String pluginParent)
    {
        this.pluginParent=pluginParent;
    }

    /**
     * 域(受保护)
     * 名称: pluginEnable
     * 描述: 插件是否启用，0-禁用、1-启用
     */
    protected int pluginEnable;

    /**
     * 访问器
     * 目标: pluginEnable
     *
     * @return int - 插件是否启用，0-禁用、1-启用
     */
    public int getPluginEnable()
    {
        return pluginEnable;
    }

    /**
     * 更改器
     * 目标: pluginEnable
     *
     * @param pluginEnable - 插件是否启用，0-禁用、1-启用
     */
    public void setPluginEnable(int pluginEnable)
    {
        this.pluginEnable=pluginEnable;
    }

    /**
     * 更改器
     * 目标: pluginEnable
     *
     * @param pluginEnable - 插件是否启用，0-禁用、1-启用
     */
    public void setPluginEnable(String pluginEnable)
    {
        this.pluginEnable=new Integer(pluginEnable);
    }

    /**
     * 域(受保护)
     * 名称: pluginType
     * 描述: 插件数据类型，0-模块、1-插件、2-权限
     */
    protected int pluginType;

    /*方法（公共）
     *  名称：isTypeModule
     *  描述：是否为模块类型
     *@return boolean - 是否为模块类型
     */
    public boolean isTypeModule()
    {
        return pluginType==0;
    }

    /*方法（公共）
     *  名称：isTypePlugin
     *  描述：是否为插件类型
     *@return boolean - 是否为插件类型
     */
    public boolean isTypePlugin()
    {
        return pluginType==1;
    }

    /*方法（公共）
     *  名称：isTypeLimit
     *  描述：是否为权限类型
     *@return boolean - 是否为权限类型
     */
    public boolean isTypeLimit()
    {
        return pluginType==2;
    }

    /**
     * 访问器
     * 目标: pluginType
     *
     * @return int - 插件数据类型，0-模块、1-插件、2-权限
     */
    public int getPluginType()
    {
        return pluginType;
    }

    /**
     * 更改器
     * 目标: pluginType
     *
     * @param pluginType - 插件数据类型，0-模块、1-插件、2-权限
     */
    public void setPluginType(int pluginType)
    {
        this.pluginType=pluginType;
    }

    /**
     * 更改器
     * 目标: pluginType
     *
     * @param pluginType - 插件数据类型，0-模块、1-插件、2-权限
     */
    public void setPluginType(String pluginType)
    {
        this.pluginType=new Integer(pluginType);
    }

    /**
     * 域(受保护)
     * 名称: pluginIcon
     * 描述: 插件对应的图标，仅类型为1-插件时有效
     */
    protected String pluginIcon;

    /**
     * 访问器
     * 目标: pluginIcon
     *
     * @return String - 插件对应的图标，仅类型为1-插件时有效
     */
    public String getPluginIcon()
    {
        return pluginIcon==null?"":pluginIcon;
    }

    /**
     * 更改器
     * 目标: pluginIcon
     *
     * @param pluginIcon - 插件对应的图标，仅类型为1-插件时有效
     */
    public void setPluginIcon(String pluginIcon)
    {
        this.pluginIcon=pluginIcon;
    }

    /**
     * 域(受保护)
     * 名称: pluginLink
     * 描述: 插件对应的链接地址，仅类型为1-插件时有效
     */
    protected String pluginLink;

    /**
     * 访问器
     * 目标: pluginLink
     *
     * @return String - 插件对应的链接地址，仅类型为1-插件时有效
     */
    public String getPluginLink()
    {
        return pluginLink==null?"":pluginLink;
    }

    /**
     * 更改器
     * 目标: pluginLink
     *
     * @param pluginLink - 插件对应的链接地址，仅类型为1-插件时有效
     */
    public void setPluginLink(String pluginLink)
    {
        this.pluginLink=pluginLink;
    }

    /**
     * 域(受保护)
     * 名称: pluginDisplay
     * 描述: 是否显示插件，0-隐藏、1-显示
     */
    protected int pluginDisplay;

    /**
     * 访问器
     * 目标: pluginDisplay
     *
     * @return int - 是否显示插件，0-隐藏、1-显示
     */
    public int getPluginDisplay()
    {
        return pluginDisplay;
    }

    /**
     * 更改器
     * 目标: pluginDisplay
     *
     * @param pluginDisplay - 是否显示插件，0-隐藏、1-显示
     */
    public void setPluginDisplay(int pluginDisplay)
    {
        this.pluginDisplay=pluginDisplay;
    }

    /**
     * 更改器
     * 目标: pluginDisplay
     *
     * @param pluginDisplay - 是否显示插件，0-隐藏、1-显示
     */
    public void setPluginDisplay(String pluginDisplay)
    {
        this.pluginDisplay=new Integer(pluginDisplay);
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