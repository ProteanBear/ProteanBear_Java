package pb.system.cms.manager;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import pb.code.MD5Productor;
import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.data.SqlAttribute;
import pb.system.cms.entity.BusiCmsArticle;
import pb.text.DateProcessor;

/**
 * 数据表映射类数据管理类——记录文章内容相关信息
 *
 * @author ProteanBear
 * @version 1.00 2014-08-21
 */
public class BusiCmsArticleFacade
        extends AbstractEntityManager<BusiCmsArticle>
        implements BusiCmsArticleFacadeLocal
{
    /**
     * 域(私有)<br/>
     * 名称:    dp<br/>
     * 描述:    日期处理器对象<br/>
     */
    private DateProcessor dp;

    /**
     * 构造函数
     *
     * @param connector - 指定数据连接器
     */
    public BusiCmsArticleFacade(Connector connector)
    {
        super(connector,BusiCmsArticle.class);
        this.dp=new DateProcessor("yyyyMMddHHmmssssss");
    }

    /**
     * 方法（受保护）
     * 名称:    getPrimaryKeyName
     * 描述:    获取数据表主键字段名称
     *
     * @return String - 获取主键名称
     */
    @Override
    public String getPrimaryKeyName()
    {
        return "articleId";
    }

    /**
     * 方法（受保护、抽象）<br/>
     * 名称:    getKeyGenerator<br/>
     * 描述:    返回自增主键的生成器，非生成的主键返回""<br/>
     * 使用时间戳的16位MD5编码为主键<br/>
     *
     * @return String - 主键生成值
     */
    @Override
    protected String getKeyGenerator()
    {
        String result=this.dp.getCurrent();
        try
        {
            result="'"+MD5Productor.encodeToString16(result).toUpperCase()+"'";
        }
        catch(NoSuchAlgorithmException|UnsupportedEncodingException ex)
        {
        }
        return result;
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    getFindOrderBy<br/>
     * 描述:    获取查询使用的排序方法
     * 文章排序为焦点（倒序）->置顶（倒序）->排序（正序）->发布时间(倒序)->标题(正序）<br/>
     *
     * @return String - 排序方法
     */
    @Override
    protected String getFindOrderBy()
    {
        this.orderBy="article_is_focus desc"
                +",article_is_top desc"
                +",article_sort asc"
                +",article_release_time desc"
                +",article_title asc";
        return this.orderBy;
    }

    /**
     * 方法（公共）<br/>
     * 名称:    getCannotEditParams<br/>
     * 描述:    获取当前不可修改的属性<br/>
     *
     * @return String - 当前不可更改的属性，多个属性之间用|分隔
     */
    @Override
    public String getCannotEditParams()
    {
        return "articleType|articleHtml|articleStatus"
                +"|articleCreateTime|articleUpdateTime"
                +"|articleCountPush|articleCountPushShare|articleCountRead"
                +"|articleCountFavorite|articleCountComment"
                +"|dataDelete";
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    afterRemove<br/>
     * 描述:    设置删除后的相关处理<br/>
     *
     * @param obj - 主键或删除对象
     */
    @Override
    protected void afterRemove(Object obj)
    {
        //指定主键或对象为空
        if(obj==null) this.logAndReturnFalse("指定主键或对象为空");

        //获取文章的主键
        String articleId=null;
        if(obj.getClass().isAssignableFrom(BusiCmsArticle.class))
        {
            articleId=((BusiCmsArticle)obj).getArticleId();
        }
        else
        {
            articleId=obj+"";
        }

        //创建分栏与文章多对多管理器对象
        BusiCmsArticleResFacadeLocal resManager=new BusiCmsArticleResFacade(this.connector);
        //清除相关分栏与文章多对多数据
        resManager.removeByArticleId(articleId);

        //创建分栏与文章多对多管理器对象
        BusiCmsArticleGroupMapFacadeLocal gmManager=new BusiCmsArticleGroupMapFacade(this.connector);
        //清除相关分栏与文章多对多数据
        gmManager.removeByArticleId(articleId);
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    findTopicByArticleId<br/>
     * 描述:    获取指定的专题文章ID对应的关联文章列表<br/>
     *
     * @param articleId - 专题文章标识
     * @return List - 专题文章关联文章列表
     */
    @Override
    public List<BusiCmsArticle> findTopicByArticleId(String articleId)
    {
        List<BusiCmsArticle> result=null;
        try
        {
            //设置查询语句
            SqlAttribute sqlAttri=new SqlAttribute()
                    .setColumnNames("bcag.group_name,bcag.group_type,bca.*")
                    .setTable("busi_cms_article_group bcag,busi_cms_article_group_map bcagm,busi_cms_article bca")
                    .addCondition("bcag.group_id = bcagm.group_id")
                    .addCondition("bcagm.article_id = bca.article_id")
                    .addCondition("bcag.article_id=?");

            //读取数据库;
            result=this.generateObjectList(
                    this.dataManager
                            .setAttribute(sqlAttri)
                            .setPageSize(0)
                            .setOrderBy("bcag.group_id asc,"+this.getFindOrderBy())
                            .select(articleId)
            );
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |InstantiationException
                |NoSuchMethodException
                |InvocationTargetException
                |SQLException ex)
        {
            result=(List<BusiCmsArticle>)this.logAndReturnNull(ex.toString());
        }
        return result;
    }
}