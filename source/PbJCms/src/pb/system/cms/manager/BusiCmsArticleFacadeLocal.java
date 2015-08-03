package pb.system.cms.manager;

import java.util.List;

import pb.data.AbstractFacadeLocal;
import pb.system.cms.entity.BusiCmsArticle;

/**
 * 数据表映射类数据管理接口——记录文章内容相关信息
 *
 * @author ProteanBear
 * @version 1.00 2014-08-21
 */
public interface BusiCmsArticleFacadeLocal extends AbstractFacadeLocal<BusiCmsArticle>
{
    /**
     * 描述:    获取指定的专题文章ID对应的关联文章列表<br/>
     *
     * @param articleId - 专题文章标识
     * @return List - 企业类型列表
     */
    List<BusiCmsArticle> findTopicByArticleId(String articleId);
}