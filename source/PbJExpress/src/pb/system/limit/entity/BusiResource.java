package pb.system.limit.entity;

/**
 * 数据表映射类——记录媒体资源的相关信息
 *
 * @author ProteanBear
 * @version 1.00 2014-10-13
 */
public class BusiResource
{
    /**
     * 域(受保护)
     * 名称: resourceId
     * 描述: 主键，使用时间戳的16位MD5编码
     */
    protected String resourceId;

    /**
     * 访问器
     * 目标: resourceId
     *
     * @return String - 主键，使用时间戳的16位MD5编码
     */
    public String getResourceId()
    {
        return resourceId;
    }

    /**
     * 更改器
     * 目标: resourceId
     *
     * @param resourceId - 主键，使用时间戳的16位MD5编码
     */
    public void setResourceId(String resourceId)
    {
        this.resourceId=resourceId;
    }

    /**
     * 域(受保护)
     * 名称: resourceTitle
     * 描述: 记录资源的标题
     */
    protected String resourceTitle;

    /**
     * 访问器
     * 目标: resourceTitle
     *
     * @return String - 记录资源的标题
     */
    public String getResourceTitle()
    {
        return resourceTitle;
    }

    /**
     * 更改器
     * 目标: resourceTitle
     *
     * @param resourceTitle - 记录资源的标题
     */
    public void setResourceTitle(String resourceTitle)
    {
        this.resourceTitle=resourceTitle;
    }

    /**
     * 域(受保护)
     * 名称: resourceThumb
     * 描述: 记录资源缩略图的链接
     */
    protected String resourceThumb;

    /**
     * 访问器
     * 目标: resourceThumb
     *
     * @return String - 记录资源缩略图的链接
     */
    public String getResourceThumb()
    {
        return resourceThumb;
    }

    /**
     * 更改器
     * 目标: resourceThumb
     *
     * @param resourceThumb - 记录资源缩略图的链接
     */
    public void setResourceThumb(String resourceThumb)
    {
        this.resourceThumb=resourceThumb;
    }

    /**
     * 域(受保护)
     * 名称: resourceIsOut
     * 描述: 记录资源是否为外部资源，0-否、1-是
     */
    protected int resourceIsOut;

    /**
     * 访问器
     * 目标: resourceIsOut
     *
     * @return int - 记录资源是否为外部资源，0-否、1-是
     */
    public int getResourceIsOut()
    {
        return resourceIsOut;
    }

    /**
     * 访问器
     * 目标: resourceIsOut
     *
     * @return int - 记录资源是否为外部资源，0-否、1-是
     */
    public boolean resourceIsOut()
    {
        return resourceIsOut==1;
    }

    /**
     * 更改器
     * 目标: resourceIsOut
     *
     * @param resourceIsOut - 记录资源是否为外部资源，0-否、1-是
     */
    public void setResourceIsOut(int resourceIsOut)
    {
        this.resourceIsOut=resourceIsOut;
    }

    /**
     * 更改器
     * 目标: resourceIsOut
     *
     * @param resourceIsOut - 记录资源是否为外部资源，0-否、1-是
     */
    public void setResourceIsOut(String resourceIsOut)
    {
        this.resourceIsOut=new Integer(resourceIsOut);
    }

    /**
     * 域(受保护)
     * 名称: resourceLink
     * 描述: 记录资源链接的路径
     */
    protected String resourceLink;

    /**
     * 访问器
     * 目标: resourceLink
     *
     * @return String - 记录资源链接的路径
     */
    public String getResourceLink()
    {
        return resourceLink;
    }

    /**
     * 更改器
     * 目标: resourceLink
     *
     * @param resourceLink - 记录资源链接的路径
     */
    public void setResourceLink(String resourceLink)
    {
        this.resourceLink=resourceLink;
    }

    /**
     * 域(受保护)
     * 名称: resourceMiddle
     * 描述: 记录资源相关中等尺寸的图片链接
     */
    protected String resourceMiddle;

    /**
     * 访问器
     * 目标: resourceMiddle
     *
     * @return String - 记录资源相关中等尺寸的图片链接
     */
    public String getResourceMiddle()
    {
        return resourceMiddle;
    }

    /**
     * 更改器
     * 目标: resourceMiddle
     *
     * @param resourceMiddle - 记录资源相关中等尺寸的图片链接
     */
    public void setResourceMiddle(String resourceMiddle)
    {
        this.resourceMiddle=resourceMiddle;
    }

    /**
     * 域(受保护)
     * 名称: resourceFile
     * 描述: 记录资源相关的文件名称
     */
    protected String resourceFile;

    /**
     * 访问器
     * 目标: resourceFile
     *
     * @return String - 记录资源相关的文件名称
     */
    public String getResourceFile()
    {
        return resourceFile;
    }

    /**
     * 更改器
     * 目标: resourceFile
     *
     * @param resourceFile - 记录资源相关的文件名称
     */
    public void setResourceFile(String resourceFile)
    {
        this.resourceFile=resourceFile;
    }

    /**
     * 域(受保护)
     * 名称: resourceSuffix
     * 描述: 记录文件的后缀名称
     */
    protected String resourceSuffix;

    /**
     * 访问器
     * 目标: resourceSuffix
     *
     * @return String - 记录文件的后缀名称
     */
    public String getResourceSuffix()
    {
        return resourceSuffix;
    }

    /**
     * 更改器
     * 目标: resourceSuffix
     *
     * @param resourceSuffix - 记录文件的后缀名称
     */
    public void setResourceSuffix(String resourceSuffix)
    {
        this.resourceSuffix=resourceSuffix;
    }

    /**
     * 域(受保护)
     * 名称: resourcePath
     * 描述: 记录资源储存的硬盘路径
     */
    protected String resourcePath;

    /**
     * 访问器
     * 目标: resourcePath
     *
     * @return String - 记录资源储存的硬盘路径
     */
    public String getResourcePath()
    {
        return resourcePath;
    }

    /**
     * 更改器
     * 目标: resourcePath
     *
     * @param resourcePath - 记录资源储存的硬盘路径
     */
    public void setResourcePath(String resourcePath)
    {
        this.resourcePath=resourcePath;
    }

    /**
     * 域(受保护)
     * 名称: resourceSort
     * 描述: 用于资源内容显示的排序
     */
    protected String resourceSort;

    /**
     * 访问器
     * 目标: resourceSort
     *
     * @return String - 用于资源内容显示的排序
     */
    public String getResourceSort()
    {
        return resourceSort;
    }

    /**
     * 更改器
     * 目标: resourceSort
     *
     * @param resourceSort - 用于资源内容显示的排序
     */
    public void setResourceSort(String resourceSort)
    {
        this.resourceSort=resourceSort;
    }

    /**
     * 域(受保护)
     * 名称: resourceType
     * 描述: 记录资源的显示类型， 0-图片资源、1-语音资源、2-视频资源
     */
    protected int resourceType;

    /**
     * 访问器
     * 目标: resourceType
     *
     * @return int - 记录资源的显示类型， 0-图片资源、1-语音资源、2-视频资源
     */
    public int getResourceType()
    {
        return resourceType;
    }

    /**
     * 更改器
     * 目标: resourceType
     *
     * @param resourceType - 记录资源的显示类型， 0-图片资源、1-语音资源、2-视频资源
     */
    public void setResourceType(int resourceType)
    {
        this.resourceType=resourceType;
    }

    /**
     * 更改器
     * 目标: resourceType
     *
     * @param resourceType - 记录资源的显示类型， 0-图片资源、1-语音资源、2-视频资源
     */
    public void setResourceType(String resourceType)
    {
        this.resourceType=new Integer(resourceType);
    }

    /**
     * 域(受保护)
     * 名称: resourceCreate
     * 描述: 记录资源上传创建的时间
     */
    protected String resourceCreate;

    /**
     * 访问器
     * 目标: resourceCreate
     *
     * @return String - 记录资源上传创建的时间
     */
    public String getResourceCreate()
    {
        return resourceCreate;
    }

    /**
     * 更改器
     * 目标: resourceCreate
     *
     * @param resourceCreate - 记录资源上传创建的时间
     */
    public void setResourceCreate(String resourceCreate)
    {
        this.resourceCreate=resourceCreate;
    }

    /**
     * 域(受保护)
     * 名称: resourceDate
     * 描述: 记录资源上传的日期
     */
    protected String resourceDate;

    /**
     * 访问器
     * 目标: resourceDate
     *
     * @return String - 记录资源上传的日期
     */
    public String getResourceDate()
    {
        return resourceDate;
    }

    /**
     * 更改器
     * 目标: resourceDate
     *
     * @param resourceDate - 记录资源上传的日期
     */
    public void setResourceDate(String resourceDate)
    {
        this.resourceDate=resourceDate;
    }

    /**
     * 域(受保护)
     * 名称: resourceSize
     * 描述: 记录上传资源的文件大小
     */
    protected String resourceSize;

    /**
     * 访问器
     * 目标: resourceSize
     *
     * @return String - 记录上传资源的文件大小
     */
    public String getResourceSize()
    {
        return resourceSize;
    }

    /**
     * 更改器
     * 目标: resourceSize
     *
     * @param resourceSize - 记录上传资源的文件大小
     */
    public void setResourceSize(String resourceSize)
    {
        this.resourceSize=resourceSize;
    }

    /**
     * 域(受保护)
     * 名称: resourceCount
     * 描述: 记录资源被使用的计数，默认为1；为0时可能会被系统清除
     */
    protected int resourceCount;

    /**
     * 访问器
     * 目标: resourceCount
     *
     * @return int - 记录资源被使用的计数，默认为1；为0时可能会被系统清除
     */
    public int getResourceCount()
    {
        return resourceCount;
    }

    /**
     * 更改器
     * 目标: resourceCount
     *
     * @param resourceCount - 记录资源被使用的计数，默认为1；为0时可能会被系统清除
     */
    public void setResourceCount(int resourceCount)
    {
        this.resourceCount=resourceCount;
    }

    /**
     * 更改器
     * 目标: resourceCount
     *
     * @param resourceCount - 记录资源被使用的计数，默认为1；为0时可能会被系统清除
     */
    public void setResourceCount(String resourceCount)
    {
        this.resourceCount=new Integer(resourceCount);
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