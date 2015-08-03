package pb.system.cms.manager;

import java.util.List;

import pb.data.AbstractFacadeLocal;
import pb.system.cms.entity.BusiCmsArticleRes;

/**
 * 数据表映射类数据管理接口——记录文章与资源管理关系
 *
 * @author ProteanBear
 * @version 1.00 2014-09-28
 */
public interface BusiCmsArticleResFacadeLocal extends AbstractFacadeLocal<BusiCmsArticleRes>
{
    /**
     * 描述:    清除指定的文章标识对应的多对多数据<br/>
     *
     * @param articleId - 文章标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean removeByArticleId(String articleId);

    /**
     * 描述:    清除指定的资源标识对应的多对多数据<br/>
     *
     * @param resourceId - 分栏标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean removeByResourceId(String resourceId);

    /**
     * 描述:    获取指定的文章对应的多对多数据<br/>
     *
     * @param articleId - 文章标识
     * @return List - 返回结果
     */
    List<BusiCmsArticleRes> findByArticleId(String articleId);

    /**
     * 描述:    创建指定文章标识和分栏标识的多对多数据<br/>
     *
     * @param articleId  - 文章标识
     * @param resourceId - 资源标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean create(String articleId,String resourceId);
}