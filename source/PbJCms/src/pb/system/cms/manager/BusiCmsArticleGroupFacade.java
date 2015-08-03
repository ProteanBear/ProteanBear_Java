package pb.system.cms.manager;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import pb.code.MD5Productor;
import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.system.cms.entity.BusiCmsArticleGroup;
import pb.text.DateProcessor;

/**
 * 数据表映射类数据管理类——记录文章分栏的相关信息，用于专题文章
 *
 * @author ProteanBear
 * @version 1.00 2014-09-28
 */
public class BusiCmsArticleGroupFacade
        extends AbstractEntityManager<BusiCmsArticleGroup>
        implements BusiCmsArticleGroupFacadeLocal
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
    public BusiCmsArticleGroupFacade(Connector connector)
    {
        super(connector,BusiCmsArticleGroup.class);
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
        return "groupId";
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
        String groupId=null;
        if(obj.getClass().isAssignableFrom(BusiCmsArticleGroup.class))
        {
            groupId=((BusiCmsArticleGroup)obj).getGroupId();
        }
        else
        {
            groupId=obj+"";
        }

        //创建分栏与文章多对多管理器对象
        BusiCmsArticleGroupMapFacadeLocal gmManager=new BusiCmsArticleGroupMapFacade(this.connector);
        //清除相关分栏与文章多对多数据
        gmManager.removeByGroupId(groupId);
    }
}