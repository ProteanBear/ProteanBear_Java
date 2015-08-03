package pb.system.limit.entity;

/**
 * 数据表映射类——记录当前系统业务栏目划分的数据信息。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-31
 */
public class BusiSection
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
     * 名称: sectionApp
     * 描述: 栏目所属应用的授权编码。
     */
    protected String sectionApp;

    /**
     * 访问器
     * 目标: sectionApp
     *
     * @return String - 栏目所属应用的授权编码。
     */
    public String getSectionApp()
    {
        return sectionApp;
    }

    /**
     * 更改器
     * 目标: sectionApp
     *
     * @param sectionApp - 栏目所属应用的授权编码。
     */
    public void setSectionApp(String sectionApp)
    {
        this.sectionApp=sectionApp;
    }

    /**
     * 域(受保护)
     * 名称: sectionCode
     * 描述: 栏目编码，使用字符形式对区域树形进行逻辑划分，如树顶层为1000，第二层第一条为10000000、第二条为10000001，依次类推；查询时使用正则匹配和编码排序来进行相应控制。
     */
    protected String sectionCode;

    /**
     * 访问器
     * 目标: sectionCode
     *
     * @return String - 栏目编码，使用字符形式对区域树形进行逻辑划分，如树顶层为1000，第二层第一条为10000000、第二条为10000001，依次类推；查询时使用正则匹配和编码排序来进行相应控制。
     */
    public String getSectionCode()
    {
        return sectionCode;
    }

    /**
     * 更改器
     * 目标: sectionCode
     *
     * @param sectionCode - 栏目编码，使用字符形式对区域树形进行逻辑划分，如树顶层为1000，第二层第一条为10000000、第二条为10000001，依次类推；查询时使用正则匹配和编码排序来进行相应控制。
     */
    public void setSectionCode(String sectionCode)
    {
        this.sectionCode=sectionCode;
    }

    /**
     * 域(受保护)
     * 名称: sectionName
     * 描述: 记录栏目的显示名称
     */
    protected String sectionName;

    /**
     * 访问器
     * 目标: sectionName
     *
     * @return String - 记录栏目的显示名称
     */
    public String getSectionName()
    {
        return sectionName;
    }

    /**
     * 更改器
     * 目标: sectionName
     *
     * @param sectionName - 记录栏目的显示名称
     */
    public void setSectionName(String sectionName)
    {
        this.sectionName=sectionName;
    }

    /**
     * 域(受保护)
     * 名称: sectionAlias
     * 描述: 记录栏目的别名
     */
    protected String sectionAlias;

    /**
     * 访问器
     * 目标: sectionAlias
     *
     * @return String - 记录栏目的别名
     */
    public String getSectionAlias()
    {
        return sectionAlias;
    }

    /**
     * 更改器
     * 目标: sectionAlias
     *
     * @param sectionAlias - 记录栏目的别名
     */
    public void setSectionAlias(String sectionAlias)
    {
        this.sectionAlias=sectionAlias;
    }

    /**
     * 域(受保护)
     * 名称: sectionTypeContent
     * 描述: 记录栏目的内容类型，0-新闻类、1-商业类、2-地图
     */
    protected int sectionTypeContent;

    /**
     * 访问器
     * 目标: sectionTypeContent
     *
     * @return int - 记录栏目的内容类型，0-新闻类、1-商业类、2-地图
     */
    public int getSectionTypeContent()
    {
        return sectionTypeContent;
    }

    /**
     * 更改器
     * 目标: sectionTypeContent
     *
     * @param sectionTypeContent - 记录栏目的内容类型，0-新闻类、1-商业类、2-地图
     */
    public void setSectionTypeContent(int sectionTypeContent)
    {
        this.sectionTypeContent=sectionTypeContent;
    }

    /**
     * 更改器
     * 目标: sectionTypeContent
     *
     * @param sectionTypeContent - 记录栏目的内容类型，0-新闻类、1-商业类、2-地图
     */
    public void setSectionTypeContent(String sectionTypeContent)
    {
        this.sectionTypeContent=new Integer(sectionTypeContent);
    }

    /**
     * 域(受保护)
     * 名称: sectionTypeDisplay
     * 描述: 记录栏目的显示类型，0-图文、1-图片单排、2-图片双排
     */
    protected int sectionTypeDisplay;

    /**
     * 访问器
     * 目标: sectionTypeDisplay
     *
     * @return int - 记录栏目的显示类型，0-图文、1-图片单排、2-图片双排
     */
    public int getSectionTypeDisplay()
    {
        return sectionTypeDisplay;
    }

    /**
     * 更改器
     * 目标: sectionTypeDisplay
     *
     * @param sectionTypeDisplay - 记录栏目的显示类型，0-图文、1-图片单排、2-图片双排
     */
    public void setSectionTypeDisplay(int sectionTypeDisplay)
    {
        this.sectionTypeDisplay=sectionTypeDisplay;
    }

    /**
     * 更改器
     * 目标: sectionTypeDisplay
     *
     * @param sectionTypeDisplay - 记录栏目的显示类型，0-图文、1-图片单排、2-图片双排
     */
    public void setSectionTypeDisplay(String sectionTypeDisplay)
    {
        this.sectionTypeDisplay=new Integer(sectionTypeDisplay);
    }

    /**
     * 域(受保护)
     * 名称: sectionEnable
     * 描述: 是否启用
     */
    protected int sectionEnable;

    /**
     * 访问器
     * 目标: sectionEnable
     *
     * @return int - 是否启用
     */
    public int getSectionEnable()
    {
        return sectionEnable;
    }

    /**
     * 更改器
     * 目标: sectionEnable
     *
     * @param sectionEnable - 是否启用
     */
    public void setSectionEnable(int sectionEnable)
    {
        this.sectionEnable=sectionEnable;
    }

    /**
     * 更改器
     * 目标: sectionEnable
     *
     * @param sectionEnable - 是否启用
     */
    public void setSectionEnable(String sectionEnable)
    {
        this.sectionEnable=new Integer(sectionEnable);
    }

    /**
     * 域(受保护)
     * 名称: sectionIcon
     * 描述: 栏目图标
     */
    protected String sectionIcon;

    /**
     * 访问器
     * 目标: sectionIcon
     *
     * @return String - 栏目图标
     */
    public String getSectionIcon()
    {
        return sectionIcon;
    }

    /**
     * 更改器
     * 目标: sectionIcon
     *
     * @param sectionIcon - 栏目图标
     */
    public void setSectionIcon(String sectionIcon)
    {
        this.sectionIcon=sectionIcon;
    }

    /**
     * 域(受保护)
     * 名称: sectionIconOpen
     * 描述: 文件夹展开时图标
     */
    protected String sectionIconOpen;

    /**
     * 访问器
     * 目标: sectionIconOpen
     *
     * @return String - 文件夹展开时图标
     */
    public String getSectionIconOpen()
    {
        return sectionIconOpen;
    }

    /**
     * 更改器
     * 目标: sectionIconOpen
     *
     * @param sectionIconOpen - 文件夹展开时图标
     */
    public void setSectionIconOpen(String sectionIconOpen)
    {
        this.sectionIconOpen=sectionIconOpen;
    }

    /**
     * 域(受保护)
     * 名称: sectionIconClose
     * 描述: 文件夹关闭时图标
     */
    protected String sectionIconClose;

    /**
     * 访问器
     * 目标: sectionIconClose
     *
     * @return String - 文件夹关闭时图标
     */
    public String getSectionIconClose()
    {
        return sectionIconClose;
    }

    /**
     * 更改器
     * 目标: sectionIconClose
     *
     * @param sectionIconClose - 文件夹关闭时图标
     */
    public void setSectionIconClose(String sectionIconClose)
    {
        this.sectionIconClose=sectionIconClose;
    }

    /**
     * 域(受保护)
     * 名称: sectionSort
     * 描述: 栏目排序
     */
    protected int sectionSort;

    /**
     * 访问器
     * 目标: sectionSort
     *
     * @return int - 栏目排序
     */
    public int getSectionSort()
    {
        return sectionSort;
    }

    /**
     * 更改器
     * 目标: sectionSort
     *
     * @param sectionSort - 栏目排序
     */
    public void setSectionSort(int sectionSort)
    {
        this.sectionSort=sectionSort;
    }

    /**
     * 更改器
     * 目标: sectionSort
     *
     * @param sectionSort - 栏目排序
     */
    public void setSectionSort(String sectionSort)
    {
        this.sectionSort=new Integer(sectionSort);
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