package pb.system.cms.entity;

/**
 * 数据表映射类——记录文章内容相关信息
 *
 * @author ProteanBear
 * @version 1.00 2014-09-28
 */
public class BusiCmsArticle
{
    /**
     * 域(受保护)
     * 名称: articleId
     * 描述: 文章主键，使用时间戳的MD5编码（十六位编码）
     */
    protected String articleId;

    /**
     * 访问器
     * 目标: articleId
     *
     * @return char - 文章主键，使用时间戳的MD5编码（十六位编码）
     */
    public String getArticleId()
    {
        return articleId;
    }

    /**
     * 更改器
     * 目标: articleId
     *
     * @param articleId - 文章主键，使用时间戳的MD5编码（十六位编码）
     */
    public void setArticleId(String articleId)
    {
        this.articleId=articleId;
    }

    /**
     * 域(受保护)
     * 名称: sectionCode
     * 描述: 文章所属的栏目标识
     */
    protected String sectionCode;

    /**
     * 访问器
     * 目标: sectionCode
     *
     * @return String - 文章所属的栏目标识
     */
    public String getSectionCode()
    {
        return sectionCode;
    }

    /**
     * 更改器
     * 目标: sectionCode
     *
     * @param sectionCode - 文章所属的栏目标识
     */
    public void setSectionCode(String sectionCode)
    {
        this.sectionCode=sectionCode;
    }

    /**
     * 域(受保护)
     * 名称: articleTitle
     * 描述: 记录文章的标题
     */
    protected String articleTitle;

    /**
     * 访问器
     * 目标: articleTitle
     *
     * @return String - 记录文章的标题
     */
    public String getArticleTitle()
    {
        return articleTitle;
    }

    /**
     * 更改器
     * 目标: articleTitle
     *
     * @param articleTitle - 记录文章的标题
     */
    public void setArticleTitle(String articleTitle)
    {
        this.articleTitle=articleTitle;
    }

    /**
     * 域(受保护)
     * 名称: articleTitleSub
     * 描述: 记录文章的副标题内容
     */
    protected String articleTitleSub;

    /**
     * 访问器
     * 目标: articleTitleSub
     *
     * @return String - 记录文章的副标题内容
     */
    public String getArticleTitleSub()
    {
        return articleTitleSub;
    }

    /**
     * 更改器
     * 目标: articleTitleSub
     *
     * @param articleTitleSub - 记录文章的副标题内容
     */
    public void setArticleTitleSub(String articleTitleSub)
    {
        this.articleTitleSub=articleTitleSub;
    }

    /**
     * 域(受保护)
     * 名称: articleType
     * 描述: 记录当前的文章类型，0-文章1-图集、2-视频、3-专题
     */
    protected int articleType;

    /**
     * 访问器
     * 目标: articleType
     *
     * @return int - 记录当前的文章类型，0-文章1-图集、2-视频、3-专题
     */
    public int getArticleType()
    {
        return articleType;
    }

    /**
     * 更改器
     * 目标: articleType
     *
     * @param articleType - 记录当前的文章类型，0-文章1-图集、2-视频、3-专题
     */
    public void setArticleType(int articleType)
    {
        this.articleType=articleType;
    }

    /**
     * 更改器
     * 目标: articleType
     *
     * @param articleType - 记录当前的文章类型，0-文章1-图集、2-视频、3-专题
     */
    public void setArticleType(String articleType)
    {
        this.articleType=new Integer(articleType);
    }

    /**
     * 域(受保护)
     * 名称: articleImageTitle
     * 描述: 记录文章的标题图资源数据的序列号
     */
    protected String articleImageTitle;

    /**
     * 访问器
     * 目标: articleImageTitle
     *
     * @return String - 记录文章的标题图资源数据的序列号
     */
    public String getArticleImageTitle()
    {
        return articleImageTitle;
    }

    /**
     * 更改器
     * 目标: articleImageTitle
     *
     * @param articleImageTitle - 记录文章的标题图资源数据的序列号
     */
    public void setArticleImageTitle(String articleImageTitle)
    {
        this.articleImageTitle=articleImageTitle;
    }

    /**
     * 域(受保护)
     * 名称: articleImageFocus
     * 描述: 记录文章的焦点图资源数据的序列号
     */
    protected String articleImageFocus;

    /**
     * 访问器
     * 目标: articleImageFocus
     *
     * @return String - 记录文章的焦点图资源数据的序列号
     */
    public String getArticleImageFocus()
    {
        return articleImageFocus;
    }

    /**
     * 更改器
     * 目标: articleImageFocus
     *
     * @param articleImageFocus - 记录文章的焦点图资源数据的序列号
     */
    public void setArticleImageFocus(String articleImageFocus)
    {
        this.articleImageFocus=articleImageFocus;
    }

    /**
     * 域(受保护)
     * 名称: articleSummary
     * 描述: 记录文章的简介内容
     */
    protected String articleSummary;

    /**
     * 访问器
     * 目标: articleSummary
     *
     * @return String - 记录文章的简介内容
     */
    public String getArticleSummary()
    {
        return articleSummary;
    }

    /**
     * 更改器
     * 目标: articleSummary
     *
     * @param articleSummary - 记录文章的简介内容
     */
    public void setArticleSummary(String articleSummary)
    {
        this.articleSummary=articleSummary;
    }

    /**
     * 域(受保护)
     * 名称: articleSort
     * 描述: 记录文章的排序序号
     */
    protected String articleSort;

    /**
     * 访问器
     * 目标: articleSort
     *
     * @return String - 记录文章的排序序号
     */
    public String getArticleSort()
    {
        return articleSort;
    }

    /**
     * 更改器
     * 目标: articleSort
     *
     * @param articleSort - 记录文章的排序序号
     */
    public void setArticleSort(String articleSort)
    {
        this.articleSort=articleSort;
    }

    /**
     * 域(受保护)
     * 名称: articleContent
     * 描述: 记录文章的内容，富文本
     */
    protected String articleContent;

    /**
     * 访问器
     * 目标: articleContent
     *
     * @return text - 记录文章的内容，富文本
     */
    public String getArticleContent()
    {
        return articleContent;
    }

    /**
     * 更改器
     * 目标: articleContent
     *
     * @param articleContent - 记录文章的内容，富文本
     */
    public void setArticleContent(String articleContent)
    {
        this.articleContent=articleContent;
    }

    /**
     * 域(受保护)
     * 名称: articleKeywords
     * 描述: 记录文章的搜索关键字
     */
    protected String articleKeywords;

    /**
     * 访问器
     * 目标: articleKeywords
     *
     * @return String - 记录文章的搜索关键字
     */
    public String getArticleKeywords()
    {
        return articleKeywords;
    }

    /**
     * 更改器
     * 目标: articleKeywords
     *
     * @param articleKeywords - 记录文章的搜索关键字
     */
    public void setArticleKeywords(String articleKeywords)
    {
        this.articleKeywords=articleKeywords;
    }

    /**
     * 域(受保护)
     * 名称: articleSource
     * 描述: 记录文章的来源
     */
    protected String articleSource;

    /**
     * 访问器
     * 目标: articleSource
     *
     * @return String - 记录文章的来源
     */
    public String getArticleSource()
    {
        return articleSource;
    }

    /**
     * 更改器
     * 目标: articleSource
     *
     * @param articleSource - 记录文章的来源
     */
    public void setArticleSource(String articleSource)
    {
        this.articleSource=articleSource;
    }

    /**
     * 域(受保护)
     * 名称: articleSourceType
     * 描述: 记录文章的来源类型，0-手工录入、1-爬虫抓取
     */
    protected int articleSourceType;

    /**
     * 访问器
     * 目标: articleSourceType
     *
     * @return int - 记录文章的来源类型，0-手工录入、1-爬虫抓取
     */
    public int getArticleSourceType()
    {
        return articleSourceType;
    }

    /**
     * 更改器
     * 目标: articleSourceType
     *
     * @param articleSourceType - 记录文章的来源类型，0-手工录入、1-爬虫抓取
     */
    public void setArticleSourceType(int articleSourceType)
    {
        this.articleSourceType=articleSourceType;
    }

    /**
     * 更改器
     * 目标: articleSourceType
     *
     * @param articleSourceType - 记录文章的来源类型，0-手工录入、1-爬虫抓取
     */
    public void setArticleSourceType(String articleSourceType)
    {
        this.articleSourceType=new Integer(articleSourceType);
    }

    /**
     * 域(受保护)
     * 名称: articleAuthor
     * 描述: 记录文章的作者名称
     */
    protected String articleAuthor;

    /**
     * 访问器
     * 目标: articleAuthor
     *
     * @return String - 记录文章的作者名称
     */
    public String getArticleAuthor()
    {
        return articleAuthor;
    }

    /**
     * 更改器
     * 目标: articleAuthor
     *
     * @param articleAuthor - 记录文章的作者名称
     */
    public void setArticleAuthor(String articleAuthor)
    {
        this.articleAuthor=articleAuthor;
    }

    /**
     * 域(受保护)
     * 名称: articleHtml
     * 描述: 记录文章的静态页面的链接地址
     */
    protected String articleHtml;

    /**
     * 访问器
     * 目标: articleHtml
     *
     * @return String - 记录文章的静态页面的链接地址
     */
    public String getArticleHtml()
    {
        return articleHtml;
    }

    /**
     * 更改器
     * 目标: articleHtml
     *
     * @param articleHtml - 记录文章的静态页面的链接地址
     */
    public void setArticleHtml(String articleHtml)
    {
        this.articleHtml=articleHtml;
    }

    /**
     * 域(受保护)
     * 名称: articleStatus
     * 描述: 文章状态，0-草稿、1-待审、2-发布
     */
    protected int articleStatus;

    /**
     * 访问器
     * 目标: articleStatus
     *
     * @return int - 文章状态，0-草稿、1-待审、2-发布
     */
    public int getArticleStatus()
    {
        return articleStatus;
    }

    /**
     * 更改器
     * 目标: articleStatus
     *
     * @param articleStatus - 文章状态，0-草稿、1-待审、2-发布
     */
    public void setArticleStatus(int articleStatus)
    {
        this.articleStatus=articleStatus;
    }

    /**
     * 更改器
     * 目标: articleStatus
     *
     * @param articleStatus - 文章状态，0-草稿、1-待审、2-发布
     */
    public void setArticleStatus(String articleStatus)
    {
        this.articleStatus=new Integer(articleStatus);
    }

    /**
     * 域(受保护)
     * 名称: articleCreateTime
     * 描述: 记录文章的创建时间
     */
    protected String articleCreateTime;

    /**
     * 访问器
     * 目标: articleCreateTime
     *
     * @return String - 记录文章的创建时间
     */
    public String getArticleCreateTime()
    {
        return articleCreateTime;
    }

    /**
     * 更改器
     * 目标: articleCreateTime
     *
     * @param articleCreateTime - 记录文章的创建时间
     */
    public void setArticleCreateTime(String articleCreateTime)
    {
        this.articleCreateTime=articleCreateTime;
    }

    /**
     * 域(受保护)
     * 名称: articleUpdateTime
     * 描述: 记录文章的更新时间
     */
    protected String articleUpdateTime;

    /**
     * 访问器
     * 目标: articleUpdateTime
     *
     * @return String - 记录文章的更新时间
     */
    public String getArticleUpdateTime()
    {
        return articleUpdateTime;
    }

    /**
     * 更改器
     * 目标: articleUpdateTime
     *
     * @param articleUpdateTime - 记录文章的更新时间
     */
    public void setArticleUpdateTime(String articleUpdateTime)
    {
        this.articleUpdateTime=articleUpdateTime;
    }

    /**
     * 域(受保护)
     * 名称: articleReleaseTime
     * 描述: 记录文章的发布时间
     */
    protected String articleReleaseTime;

    /**
     * 访问器
     * 目标: articleReleaseTime
     *
     * @return String - 记录文章的发布时间
     */
    public String getArticleReleaseTime()
    {
        return articleReleaseTime;
    }

    /**
     * 更改器
     * 目标: articleReleaseTime
     *
     * @param articleReleaseTime - 记录文章的发布时间
     */
    public void setArticleReleaseTime(String articleReleaseTime)
    {
        this.articleReleaseTime=articleReleaseTime;
    }

    /**
     * 域(受保护)
     * 名称: articleCanComment
     * 描述: 记录文章是否支持评论，0-不支持、1-支持
     */
    protected int articleCanComment;

    /**
     * 访问器
     * 目标: articleCanComment
     *
     * @return int - 记录文章是否支持评论，0-不支持、1-支持
     */
    public int getArticleCanComment()
    {
        return articleCanComment;
    }

    /**
     * 更改器
     * 目标: articleCanComment
     *
     * @param articleCanComment - 记录文章是否支持评论，0-不支持、1-支持
     */
    public void setArticleCanComment(int articleCanComment)
    {
        this.articleCanComment=articleCanComment;
    }

    /**
     * 更改器
     * 目标: articleCanComment
     *
     * @param articleCanComment - 记录文章是否支持评论，0-不支持、1-支持
     */
    public void setArticleCanComment(String articleCanComment)
    {
        this.articleCanComment=new Integer(articleCanComment);
    }

    /**
     * 域(受保护)
     * 名称: articleIsFocus
     * 描述: 记录文章是否为栏目焦点，0-否、1-是
     */
    protected int articleIsFocus;

    /**
     * 访问器
     * 目标: articleIsFocus
     *
     * @return int - 记录文章是否为栏目焦点，0-否、1-是
     */
    public int getArticleIsFocus()
    {
        return articleIsFocus;
    }

    /**
     * 更改器
     * 目标: articleIsFocus
     *
     * @param articleIsFocus - 记录文章是否为栏目焦点，0-否、1-是
     */
    public void setArticleIsFocus(int articleIsFocus)
    {
        this.articleIsFocus=articleIsFocus;
    }

    /**
     * 更改器
     * 目标: articleIsFocus
     *
     * @param articleIsFocus - 记录文章是否为栏目焦点，0-否、1-是
     */
    public void setArticleIsFocus(String articleIsFocus)
    {
        this.articleIsFocus=new Integer(articleIsFocus);
    }

    /**
     * 域(受保护)
     * 名称: articleIsTop
     * 描述: 记录文章是否置顶
     */
    protected int articleIsTop;

    /**
     * 访问器
     * 目标: articleIsTop
     *
     * @return int - 记录文章是否置顶
     */
    public int getArticleIsTop()
    {
        return articleIsTop;
    }

    /**
     * 更改器
     * 目标: articleIsTop
     *
     * @param articleIsTop - 记录文章是否置顶
     */
    public void setArticleIsTop(int articleIsTop)
    {
        this.articleIsTop=articleIsTop;
    }

    /**
     * 更改器
     * 目标: articleIsTop
     *
     * @param articleIsTop - 记录文章是否置顶
     */
    public void setArticleIsTop(String articleIsTop)
    {
        this.articleIsTop=new Integer(articleIsTop);
    }

    /**
     * 域(受保护)
     * 名称: articleCountPush
     * 描述: 记录文章的推送次数
     */
    protected int articleCountPush;

    /**
     * 访问器
     * 目标: articleCountPush
     *
     * @return int - 记录文章的推送次数
     */
    public int getArticleCountPush()
    {
        return articleCountPush;
    }

    /**
     * 更改器
     * 目标: articleCountPush
     *
     * @param articleCountPush - 记录文章的推送次数
     */
    public void setArticleCountPush(int articleCountPush)
    {
        this.articleCountPush=articleCountPush;
    }

    /**
     * 更改器
     * 目标: articleCountPush
     *
     * @param articleCountPush - 记录文章的推送次数
     */
    public void setArticleCountPush(String articleCountPush)
    {
        this.articleCountPush=new Integer(articleCountPush);
    }

    /**
     * 域(受保护)
     * 名称: articleCountShare
     * 描述: 记录文章的分享次数
     */
    protected int articleCountShare;

    /**
     * 访问器
     * 目标: articleCountShare
     *
     * @return int - 记录文章的分享次数
     */
    public int getArticleCountShare()
    {
        return articleCountShare;
    }

    /**
     * 更改器
     * 目标: articleCountShare
     *
     * @param articleCountShare - 记录文章的分享次数
     */
    public void setArticleCountShare(int articleCountShare)
    {
        this.articleCountShare=articleCountShare;
    }

    /**
     * 更改器
     * 目标: articleCountShare
     *
     * @param articleCountShare - 记录文章的分享次数
     */
    public void setArticleCountShare(String articleCountShare)
    {
        this.articleCountShare=new Integer(articleCountShare);
    }

    /**
     * 域(受保护)
     * 名称: articleCountRead
     * 描述: 记录文章的阅读次数
     */
    protected int articleCountRead;

    /**
     * 访问器
     * 目标: articleCountRead
     *
     * @return int - 记录文章的阅读次数
     */
    public int getArticleCountRead()
    {
        return articleCountRead;
    }

    /**
     * 更改器
     * 目标: articleCountRead
     *
     * @param articleCountRead - 记录文章的阅读次数
     */
    public void setArticleCountRead(int articleCountRead)
    {
        this.articleCountRead=articleCountRead;
    }

    /**
     * 更改器
     * 目标: articleCountRead
     *
     * @param articleCountRead - 记录文章的阅读次数
     */
    public void setArticleCountRead(String articleCountRead)
    {
        this.articleCountRead=new Integer(articleCountRead);
    }

    /**
     * 域(受保护)
     * 名称: articleCountFavorite
     * 描述: 记录文章的收藏次数
     */
    protected int articleCountFavorite;

    /**
     * 访问器
     * 目标: articleCountFavorite
     *
     * @return int - 记录文章的收藏次数
     */
    public int getArticleCountFavorite()
    {
        return articleCountFavorite;
    }

    /**
     * 更改器
     * 目标: articleCountFavorite
     *
     * @param articleCountFavorite - 记录文章的收藏次数
     */
    public void setArticleCountFavorite(int articleCountFavorite)
    {
        this.articleCountFavorite=articleCountFavorite;
    }

    /**
     * 更改器
     * 目标: articleCountFavorite
     *
     * @param articleCountFavorite - 记录文章的收藏次数
     */
    public void setArticleCountFavorite(String articleCountFavorite)
    {
        this.articleCountFavorite=new Integer(articleCountFavorite);
    }

    /**
     * 域(受保护)
     * 名称: articleCountComment
     * 描述: 记录文章的评论次数
     */
    protected int articleCountComment;

    /**
     * 访问器
     * 目标: articleCountComment
     *
     * @return int - 记录文章的评论次数
     */
    public int getArticleCountComment()
    {
        return articleCountComment;
    }

    /**
     * 更改器
     * 目标: articleCountComment
     *
     * @param articleCountComment - 记录文章的评论次数
     */
    public void setArticleCountComment(int articleCountComment)
    {
        this.articleCountComment=articleCountComment;
    }

    /**
     * 更改器
     * 目标: articleCountComment
     *
     * @param articleCountComment - 记录文章的评论次数
     */
    public void setArticleCountComment(String articleCountComment)
    {
        this.articleCountComment=new Integer(articleCountComment);
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