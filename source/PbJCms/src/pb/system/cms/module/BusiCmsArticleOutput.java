package pb.system.cms.module;

import java.util.List;

import pb.system.cms.entity.BusiCmsArticle;
import pb.system.limit.entity.BusiResource;

/**
 * 文章输出属性
 *
 * @author proteanBear(马强)
 * @version 1.00 2014/09/28
 */
public class BusiCmsArticleOutput extends BusiCmsArticle
{
    /**
     * 域(受保护)<br/>
     * 名称:    articleResource<br/>
     * 描述:    记录文章对应的资源列表<br/>
     */
    protected List<BusiResource> articleResource;

    /**
     * 域(受保护)<br/>
     * 名称:    subArticles<br/>
     * 描述:    记录文章对应的关联文章列表<br/>
     */
    protected List<BusiCmsArticle> subArticles;

    /**
     * 构造函数<br/>
     *
     * @param article - 文章对象
     */
    public BusiCmsArticleOutput(BusiCmsArticle article)
    {
        this.setArticleAuthor(article.getArticleAuthor());
        this.setArticleCanComment(article.getArticleCanComment());
        this.setArticleContent(article.getArticleContent());
        this.setArticleCountComment(article.getArticleCountComment());
        this.setArticleCountFavorite(article.getArticleCountFavorite());
        this.setArticleCountPush(article.getArticleCountPush());
        this.setArticleCountRead(article.getArticleCountRead());
        this.setArticleCountShare(article.getArticleCountShare());
        this.setArticleCreateTime(article.getArticleCreateTime());
        this.setArticleHtml(article.getArticleHtml());
        this.setArticleId(article.getArticleId());
        this.setArticleImageFocus(article.getArticleImageFocus());
        this.setArticleImageTitle(article.getArticleImageTitle());
        this.setArticleIsFocus(article.getArticleIsFocus());
        this.setArticleIsTop(article.getArticleIsTop());
        this.setArticleKeywords(article.getArticleKeywords());
        this.setArticleReleaseTime(article.getArticleReleaseTime());
        this.setArticleSort(article.getArticleSort());
        this.setArticleSource(article.getArticleSource());
        this.setArticleSourceType(article.getArticleSourceType());
        this.setArticleStatus(article.getArticleStatus());
        this.setArticleSummary(article.getArticleSummary());
        this.setArticleTitle(article.getArticleTitle());
        this.setArticleTitleSub(article.getArticleTitleSub());
        this.setArticleType(article.getArticleType());
        this.setArticleUpdateTime(article.getArticleUpdateTime());
        this.setDataDelete(article.getDataDelete());
        this.setDataRemark(article.getDataRemark());
        this.setSectionCode(article.getSectionCode());
    }

    /**
     * 访问器<br/>
     * 目标：   articleResource<br/>
     *
     * @return List<> - 文章对应的资源列表
     */
    public List<BusiResource> getArticleResource()
    {
        return articleResource;
    }

    /**
     * 更改器<br/>
     * 目标：   articleResource<br/>
     *
     * @param articleResource - 文章对应的资源列表
     */
    public void setArticleResource(List<BusiResource> articleResource)
    {
        this.articleResource=articleResource;
    }

    /**
     * 访问器<br/>
     * 目标：   subArticles<br/>
     *
     * @return List<> - 文章对应的关联文章列表
     */
    public List<BusiCmsArticle> getSubArticles()
    {
        return subArticles;
    }

    /**
     * 更改器<br/>
     * 目标：   subArticles<br/>
     *
     * @param subArticles - 文章对应的关联列表
     */
    public void setSubArticles(List<BusiCmsArticle> subArticles)
    {
        this.subArticles=subArticles;
    }
}
