package pb.system.cms.entity;

/**
 * 数据表映射类——
 *@author ProteanBear
 *@version 1.00 2016-04-11
 */
public class BusiCmsComment
{
    /**
     * 生成当前评论的父类内容(即当前的父类内容+当前回复内容)
     * @return
     */
    public String generateUpContent()
    {
        StringBuilder result=new StringBuilder();

        result.append(this.commentUpcontent!=null&&!"".equals(this.commentUpcontent.trim())?(this.commentUpcontent+","):"[");
        result.append("{")
                .append("custId:").append(this.getCustId()).append(",")
                .append("commentParent:").append(this.getCommentParent()).append(",")
                .append("commentContent:\"").append(this.getCommentContent()).append("\",")
                .append("publishMember:").append(this.getPublishMember()).append(",")
                .append("publishName:\"").append(this.getPublishName()).append("\",")
                .append("publishHead:\"").append(this.getPublishHead()).append("\",")
                .append("createTime:\"").append(this.getCreateTime()).append("\"")
                .append("}");

        return result.toString();
    }

    /**域(受保护)
     * 名称: custId
     * 描述: 自增主键
     */
    protected int custId;

    /**访问器
     * 目标: custId
     * @return int - 自增主键
     */
    public int getCustId(){return custId;}

    /**更改器
     * 目标: custId
     * @param custId - 自增主键
     */
    public void setCustId(int custId)
    {
        this.custId=custId;
    }

    /**更改器
     * 目标: custId
     * @param custId - 自增主键
     */
    public void setCustId(Integer custId)
    {
        this.custId=custId.intValue();
    }

    /**更改器
     * 目标: custId
     * @param custId - 自增主键
     */
    public void setCustId(String custId)
    {
        this.custId=new Integer(custId);
    }

    /**域(受保护)
     * 名称: commentArticle
     * 描述: 记录评论回复的文章标识
     */
    protected String commentArticle;

    /**访问器
     * 目标: commentArticle
     * @return String - 记录评论回复的文章标识
     */
    public String getCommentArticle(){return commentArticle;}

    /**更改器
     * 目标: commentArticle
     * @param commentArticle - 记录评论回复的文章标识
     */
    public void setCommentArticle(String commentArticle)
    {
        this.commentArticle=commentArticle;
    }

    /**域(受保护)
     * 名称: commentParent
     * 描述: 指定回复的上级标识，非回复的此字段为0
     */
    protected int commentParent;

    /**访问器
     * 目标: commentParent
     * @return int - 指定回复的上级标识，非回复的此字段为0
     */
    public int getCommentParent(){return commentParent;}

    /**更改器
     * 目标: commentParent
     * @param commentParent - 指定回复的上级标识，非回复的此字段为0
     */
    public void setCommentParent(int commentParent)
    {
        this.commentParent=commentParent;
    }

    /**更改器
     * 目标: commentParent
     * @param commentParent - 指定回复的上级标识，非回复的此字段为0
     */
    public void setCommentParent(String commentParent)
    {
        this.commentParent=new Integer(commentParent);
    }

    /**域(受保护)
     * 名称: commentUpcontent
     * 描述: 指定上级回复的层级内容，非回复类此字段为空
     */
    protected String commentUpcontent;

    /**访问器
     * 目标: commentUpcontent
     * @return String - 指定上级回复的层级内容，非回复类此字段为空
     */
    public String getCommentUpcontent(){return commentUpcontent;}

    /**更改器
     * 目标: commentUpcontent
     * @param commentUpcontent - 指定上级回复的层级内容，非回复类此字段为空
     */
    public void setCommentUpcontent(String commentUpcontent)
    {
        this.commentUpcontent=commentUpcontent;
    }

    /**域(受保护)
     * 名称: commentContent
     * 描述: 回复内容
     */
    protected String commentContent;

    /**访问器
     * 目标: commentContent
     * @return String - 回复内容
     */
    public String getCommentContent(){return commentContent;}

    /**更改器
     * 目标: commentContent
     * @param commentContent - 回复内容
     */
    public void setCommentContent(String commentContent)
    {
        this.commentContent=commentContent;
    }

    /**域(受保护)
     * 名称: publishMember
     * 描述: 发布者的会员标识
     */
    protected int publishMember;

    /**访问器
     * 目标: publishMember
     * @return int - 发布者的会员标识
     */
    public int getPublishMember(){return publishMember;}

    /**更改器
     * 目标: publishMember
     * @param publishMember - 发布者的会员标识
     */
    public void setPublishMember(int publishMember)
    {
        this.publishMember=publishMember;
    }

    /**更改器
     * 目标: publishMember
     * @param publishMember - 发布者的会员标识
     */
    public void setPublishMember(String publishMember)
    {
        this.publishMember=new Integer(publishMember);
    }

    /**域(受保护)
     * 名称: publishName
     * 描述: 发布者的显示名称
     */
    protected String publishName;

    /**访问器
     * 目标: publishName
     * @return String - 发布者的显示名称
     */
    public String getPublishName(){return publishName;}

    /**更改器
     * 目标: publishName
     * @param publishName - 发布者的显示名称
     */
    public void setPublishName(String publishName)
    {
        this.publishName=publishName;
    }

    /**域(受保护)
     * 名称: publishHead
     * 描述: 发布者的显示头像
     */
    protected String publishHead;

    /**访问器
     * 目标: publishHead
     * @return String - 发布者的显示头像
     */
    public String getPublishHead(){return publishHead;}

    /**更改器
     * 目标: publishHead
     * @param publishHead - 发布者的显示头像
     */
    public void setPublishHead(String publishHead)
    {
        this.publishHead=publishHead;
    }

    /**域(受保护)
     * 名称: createTime
     * 描述: 数据创建时间
     */
    protected String createTime;

    /**访问器
     * 目标: createTime
     * @return String - 数据创建时间
     */
    public String getCreateTime(){return createTime;}

    /**更改器
     * 目标: createTime
     * @param createTime - 数据创建时间
     */
    public void setCreateTime(String createTime)
    {
        this.createTime=createTime;
    }

    /**域(受保护)
     * 名称: dataRemark
     * 描述: 数据备注信息
     */
    protected String dataRemark;

    /**访问器
     * 目标: dataRemark
     * @return String - 数据备注信息
     */
    public String getDataRemark(){return dataRemark;}

    /**更改器
     * 目标: dataRemark
     * @param dataRemark - 数据备注信息
     */
    public void setDataRemark(String dataRemark)
    {
        this.dataRemark=dataRemark;
    }

    /**域(受保护)
     * 名称: dataDelete
     * 描述: DATA_DELETE
     */
    protected int dataDelete;

    /**访问器
     * 目标: dataDelete
     * @return int - DATA_DELETE
     */
    public int getDataDelete(){return dataDelete;}

    /**更改器
     * 目标: dataDelete
     * @param dataDelete - DATA_DELETE
     */
    public void setDataDelete(int dataDelete)
    {
        this.dataDelete=dataDelete;
    }

    /**更改器
     * 目标: dataDelete
     * @param dataDelete - DATA_DELETE
     */
    public void setDataDelete(String dataDelete)
    {
        this.dataDelete=new Integer(dataDelete);
    }

}