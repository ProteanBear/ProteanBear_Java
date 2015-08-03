package pb.system.cms.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import pb.data.Connector;
import pb.system.cms.entity.BusiCmsArticleGroup;
import pb.system.cms.manager.BusiCmsArticleGroupFacade;
import pb.system.limit.action.AbstractAction;
import pb.system.limit.action.DataAction;

/**
 * 数据应用层类——记录文章分栏的相关信息，用于专题文章
 *
 * @author ProteanBear
 * @version 1.00 2014-09-28
 */
public class BusiCmsArticleGroupAction extends AbstractAction<BusiCmsArticleGroup> implements DataAction
{
    /**
     * 构造函数
     *
     * @param connector - 数据库连接器对象
     */
    public BusiCmsArticleGroupAction(Connector connector)
    {
        super(connector,BusiCmsArticleGroup.class);
    }

    /**
     * 方法（受保护）
     * 名称:initManager
     * 描述:初始化数据管理器
     */
    @Override
    protected void initManager()
    {
        this.manager=new BusiCmsArticleGroupFacade(this.connector);
    }

    /**
     * 方法（公共）<br/>
     * 名称: beforeCreate<br/>
     * 描述: 处理创建数据前的处理工作（多用于无主键生成器的数据表）<br/>
     *
     * @param request - HTTP请求对象
     * @param entity  - 构建的实体类
     * @return BusiCmsArticle - 处理后的实体类
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    @Override
    public BusiCmsArticleGroup beforeCreate
    (HttpServletRequest request,BusiCmsArticleGroup entity)
            throws ServletException
    {
        //文章标识
        if(this.paramNullCheck(entity.getArticleId()))
        {
            throw new ServletException("未指定所属的文章标识！");
        }

        //文章名称
        if(this.paramNullCheck(entity.getGroupName()))
        {
            throw new ServletException("未指定添加分组的显示名称！");
        }

        return entity;
    }
}