package pb.system.cms.manager;

import java.util.List;

import pb.data.AbstractFacadeLocal;
import pb.system.cms.entity.BusiCmsArticleGroupMap;

/**
 * 数据表映射类数据管理接口——记录文章与分栏关联关系
 *
 * @author ProteanBear
 * @version 1.00 2014-09-28
 */
public interface BusiCmsArticleGroupMapFacadeLocal extends AbstractFacadeLocal<BusiCmsArticleGroupMap>
{
    /**
     * 描述:    清除指定的文章标识对应的多对多数据<br/>
     *
     * @param articleId - 文章标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean removeByArticleId(String articleId);

    /**
     * 描述:    清除指定的分栏标识对应的多对多数据<br/>
     *
     * @param groupId - 分栏标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean removeByGroupId(String groupId);

    /**
     * 描述:    获取指定的分栏对应的多对多数据<br/>
     *
     * @param groupId - 分栏标识
     * @return List - 返回结果
     */
    List<BusiCmsArticleGroupMap> findByGroupId(String groupId);

    /**
     * 描述:    创建指定文章标识和分栏标识的多对多数据<br/>
     *
     * @param articleId - 文章标识
     * @param groupId   - 分栏标识
     * @return boolean - 如果清除成功，返回true
     */
    boolean create(String articleId,String groupId);
}