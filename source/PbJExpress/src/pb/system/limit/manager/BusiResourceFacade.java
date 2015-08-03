package pb.system.limit.manager;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import pb.code.MD5Productor;
import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.data.SqlAttribute;
import pb.system.limit.entity.BusiResource;
import pb.text.DateProcessor;

/**
 * 数据表映射类数据管理类——记录媒体资源的相关信息
 *
 * @author ProteanBear
 * @version 1.00 2014-09-28
 */
public class BusiResourceFacade
        extends AbstractEntityManager<BusiResource>
        implements BusiResourceFacadeLocal
{
    /**
     * 域(私有)<br>
     * 名称:    dp<br>
     * 描述:    日期处理器对象<br>
     */
    private DateProcessor dp;

    /**
     * 构造函数
     *
     * @param connector - 指定数据连接器
     */
    public BusiResourceFacade(Connector connector)
    {
        super(connector,BusiResource.class);
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
        return "resourceId";
    }

    /**
     * 方法（受保护、抽象）<br>
     * 名称:    getKeyGenerator<br>
     * 描述:    返回自增主键的生成器，非生成的主键返回""<br>
     * 使用时间戳的16位MD5编码为主键<br>
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
     * 方法（受保护）<br>
     * 名称:    getLastGenerator<br>
     * 描述:    获取最新生成的主键<br>
     *
     * @return String - 最新生成的主键（仅适用于自定义生成主键的获取）
     */
    @Override
    public String getLastGenerator()
    {
        return this.lastGenerator.replaceAll("'","");
    }

    /**
     * 方法（受保护、抽象）<br>
     * 名称:    findByArticleId<br>
     * 描述:    获取指定的文章对应的资源数据<br>
     *
     * @param articleId - 文章标识
     * @return List - 返回结果
     */
    @Override
    public List<BusiResource> findByArticleId(String articleId)
    {
        List<BusiResource> result=null;
        try
        {
            //设置查询语句
            SqlAttribute sqlAttri=new SqlAttribute()
                    .setColumnNames("br.*")
                    .setTable("busi_cms_article_res bcar,busi_resource br")
                    .addCondition("bcar.article_id=?")
                    .addCondition("bcar.resource_id=br.resource_id");

            //读取数据库;
            result=this.generateObjectList(
                    this.dataManager
                            .setAttribute(sqlAttri)
                            .setPageSize(0)
                            .setOrderBy(this.getFindOrderBy())
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
            result=(List<BusiResource>)this.logAndReturnNull(ex.toString());
        }
        return result;
    }
}