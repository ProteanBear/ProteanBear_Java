package pb.system.cms.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.data.SqlAttribute;
import pb.system.cms.entity.BusiCmsArticleGroupMap;

/**
 * 数据表映射类数据管理类——记录文章与分栏关联关系
 *
 * @author ProteanBear
 * @version 1.00 2014-09-28
 */
public class BusiCmsArticleGroupMapFacade
        extends AbstractEntityManager<BusiCmsArticleGroupMap>
        implements BusiCmsArticleGroupMapFacadeLocal
{
    /**
     * 构造函数
     *
     * @param connector - 指定数据连接器
     */
    public BusiCmsArticleGroupMapFacade(Connector connector)
    {
        super(connector,BusiCmsArticleGroupMap.class);
    }

    /**
     * 方法（公共）
     * 名称:    getPrimaryKeyName
     * 描述:    获取数据表主键字段名称
     *
     * @return String - 获取主键名称
     */
    @Override
    public String getPrimaryKeyName()
    {
        return "custId";
    }

    /**
     * 方法（公共）
     * 名称:    removeByArticleId
     * 描述:    清除指定的文章标识对应的多对多数据<br/>
     *
     * @param articleId - 文章标识
     * @return boolean - 如果清除成功，返回true
     */
    @Override
    public boolean removeByArticleId(String articleId)
    {
        boolean success=false;
        try
        {
            //构造删除语句
            SqlAttribute sqlAttr=new SqlAttribute()
                    .setTable("BUSI_CMS_ARTICLE_GROUP_MAP")
                    .addCondition("article_id=?");

            //删除数据
            success=this.dataManager.setAttribute(sqlAttr).delete(articleId);
        }
        catch(Exception ex)
        {
            success=this.logAndReturnFalse(ex.toString());
        }
        return success;
    }

    /**
     * 方法（公共）
     * 名称:    removeByGroupId
     * 描述:    清除指定的分栏标识对应的多对多数据<br/>
     *
     * @param groupId - 分栏标识
     * @return boolean - 如果清除成功，返回true
     */
    @Override
    public boolean removeByGroupId(String groupId)
    {
        boolean success=false;
        try
        {
            //构造删除语句
            SqlAttribute sqlAttr=new SqlAttribute()
                    .setTable("BUSI_CMS_ARTICLE_GROUP_MAP")
                    .addCondition("group_id=?");

            //删除数据
            success=this.dataManager.setAttribute(sqlAttr).delete(groupId);
        }
        catch(Exception ex)
        {
            success=this.logAndReturnFalse(ex.toString());
        }
        return success;
    }

    /**
     * 方法（公共）
     * 名称:    findByGroupId
     * 描述:    获取指定的分栏对应的多对多数据<br/>
     *
     * @param groupId - 分栏标识
     * @return List - 返回结果
     */
    @Override
    public List<BusiCmsArticleGroupMap> findByGroupId(String groupId)
    {
        //生成查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("groupId=?",groupId);

        return this.find(condition);
    }

    /**
     * 方法（公共）
     * 名称:    create
     * 描述:    创建指定文章标识和分栏标识的多对多数据<br/>
     *
     * @param articleId - 文章标识
     * @param groupId   - 分栏标识
     * @return boolean - 如果清除成功，返回true
     */
    @Override
    public boolean create(String articleId,String groupId)
    {
        //构建实体对象
        BusiCmsArticleGroupMap data=new BusiCmsArticleGroupMap();
        data.setArticleId(articleId);
        data.setGroupId(groupId);

        //构造查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("articleId=?",articleId);
        condition.put("groupId=?",groupId);

        //如果数据已存在，返回true
        if(this.exist(condition))
        {
            return true;
        }
        //如果数据不存在，添加数据
        else
        {
            return super.create(data);
        }
    }
}