package pb.system.limit.manager;

import java.util.List;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.BusiResource;

/**
 * 数据表映射类数据管理接口——记录媒体资源的相关信息
 *
 * @author ProteanBear
 * @version 1.00 2014-09-28
 */
public interface BusiResourceFacadeLocal extends AbstractFacadeLocal<BusiResource>
{
    /**
     * 描述:    获取指定的文章对应的资源数据<br>
     *
     * @param articleId - 文章标识
     * @return List - 返回结果
     */
    List<BusiResource> findByArticleId(String articleId);
}