package pb.system.cms.entity;

/**
 * 数据表映射类——记录文章分栏的相关信息，用于专题文章
 *
 * @author ProteanBear
 * @version 1.00 2014-10-05
 */
public class BusiCmsArticleGroup
{
    /**
     * 域(受保护)
     * 名称: groupId
     * 描述: 主键，使用时间戳的MD516位数编码
     */
    protected String groupId;

    /**
     * 访问器
     * 目标: groupId
     *
     * @return String - 主键，使用时间戳的MD516位数编码
     */
    public String getGroupId()
    {
        return groupId;
    }

    /**
     * 更改器
     * 目标: groupId
     *
     * @param groupId - 主键，使用时间戳的MD516位数编码
     */
    public void setGroupId(String groupId)
    {
        this.groupId=groupId;
    }

    /**
     * 域(受保护)
     * 名称: articleId
     * 描述: 记录分栏所属的文章标识
     */
    protected String articleId;

    /**
     * 访问器
     * 目标: articleId
     *
     * @return String - 记录分栏所属的文章标识
     */
    public String getArticleId()
    {
        return articleId;
    }

    /**
     * 更改器
     * 目标: articleId
     *
     * @param articleId - 记录分栏所属的文章标识
     */
    public void setArticleId(String articleId)
    {
        this.articleId=articleId;
    }

    /**
     * 域(受保护)
     * 名称: groupName
     * 描述: 记录分组的显示名称
     */
    protected String groupName;

    /**
     * 访问器
     * 目标: groupName
     *
     * @return String - 记录分组的显示名称
     */
    public String getGroupName()
    {
        return groupName;
    }

    /**
     * 更改器
     * 目标: groupName
     *
     * @param groupName - 记录分组的显示名称
     */
    public void setGroupName(String groupName)
    {
        this.groupName=groupName;
    }

    /**
     * 域(受保护)
     * 名称: groupType
     * 描述: 记录分组的显示类型，关联文章下有效，0-图文格式、1-图片格式、2-视频格式
     */
    protected int groupType;

    /**
     * 访问器
     * 目标: groupType
     *
     * @return int - 记录分组的显示类型，关联文章下有效，0-图文格式、1-图片格式、2-视频格式
     */
    public int getGroupType()
    {
        return groupType;
    }

    /**
     * 更改器
     * 目标: groupType
     *
     * @param groupType - 记录分组的显示类型，关联文章下有效，0-图文格式、1-图片格式、2-视频格式
     */
    public void setGroupType(int groupType)
    {
        this.groupType=groupType;
    }

    /**
     * 更改器
     * 目标: groupType
     *
     * @param groupType - 记录分组的显示类型，关联文章下有效，0-图文格式、1-图片格式、2-视频格式
     */
    public void setGroupType(String groupType)
    {
        this.groupType=new Integer(groupType);
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