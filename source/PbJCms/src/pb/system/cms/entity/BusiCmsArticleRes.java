package pb.system.cms.entity;

/**
 * 数据表映射类——记录文章与资源管理关系
 *
 * @author ProteanBear
 * @version 1.00 2014-10-13
 */
public class BusiCmsArticleRes
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
     * 名称: resourceId
     * 描述: 记录资源的标识
     */
    protected String resourceId;

    /**
     * 访问器
     * 目标: resourceId
     *
     * @return String - 记录资源的标识
     */
    public String getResourceId()
    {
        return resourceId;
    }

    /**
     * 更改器
     * 目标: resourceId
     *
     * @param resourceId - 记录资源的标识
     */
    public void setResourceId(String resourceId)
    {
        this.resourceId=resourceId;
    }

}