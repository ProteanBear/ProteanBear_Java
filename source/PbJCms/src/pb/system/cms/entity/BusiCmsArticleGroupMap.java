package pb.system.cms.entity;

/**
 * 数据表映射类——记录文章与分栏关联关系
 *
 * @author ProteanBear
 * @version 1.00 2014-10-05
 */
public class BusiCmsArticleGroupMap
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
     * 名称: articleId
     * 描述: 记录文章的标识
     */
    protected String articleId;

    /**
     * 访问器
     * 目标: articleId
     *
     * @return String - 记录文章的标识
     */
    public String getArticleId()
    {
        return articleId;
    }

    /**
     * 更改器
     * 目标: articleId
     *
     * @param articleId - 记录文章的标识
     */
    public void setArticleId(String articleId)
    {
        this.articleId=articleId;
    }

    /**
     * 域(受保护)
     * 名称: groupId
     * 描述: 记录分栏的标识
     */
    protected String groupId;

    /**
     * 访问器
     * 目标: groupId
     *
     * @return String - 记录分栏的标识
     */
    public String getGroupId()
    {
        return groupId;
    }

    /**
     * 更改器
     * 目标: groupId
     *
     * @param groupId - 记录分栏的标识
     */
    public void setGroupId(String groupId)
    {
        this.groupId=groupId;
    }

}