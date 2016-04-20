package pb.system.cms.action;

import pb.data.Connector;
import pb.json.JSONArray;
import pb.json.JSONObject;
import pb.system.cms.entity.BusiCmsArticle;
import pb.system.cms.entity.BusiCmsComment;
import pb.system.cms.manager.BusiCmsArticleFacade;
import pb.system.cms.manager.BusiCmsArticleFacadeLocal;
import pb.system.cms.manager.BusiCmsCommentFacade;
import pb.system.limit.action.AbstractAction;
import pb.system.limit.action.BusiMemberAction;
import pb.system.limit.action.DataAction;
import pb.system.limit.entity.BusiMember;
import pb.system.limit.servlet.AbstractServlet;
import pb.system.limit.servlet.BusiMemberServlet;
import pb.text.DateProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据应用层类——
 *@author ProteanBear
 *@version 1.00 2016-04-11
 */
public class BusiCmsCommentAction extends AbstractAction<BusiCmsComment> implements DataAction
{
    /**
     * 域(受保护)<br/>
     * 名称: resMapManager<br/>
     * 描述: 文章对应资源的数据管理器对象<br/>
     */
    protected BusiCmsArticleFacadeLocal articleManager;

    /**
     * 域(受保护)<br/>
     * 名称: dp<br/>
     * 描述: 时间日期管理器<br/>
     */
    protected DateProcessor dp;

    /**构造函数
     * @param connector - 数据库连接器对象
     */
    public BusiCmsCommentAction(Connector connector)
    {
        super(connector,BusiCmsComment.class);
    }

    /**方法（受保护）
     * 名称:initManager
     * 描述:初始化数据管理器
     */
    @Override protected void initManager()
    {
        this.manager=new BusiCmsCommentFacade(this.connector);
        this.articleManager=new BusiCmsArticleFacade(this.connector);
        this.dp=new DateProcessor("yyyy-MM-dd HH:mm:ss");
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
    public BusiCmsComment beforeCreate(HttpServletRequest request,BusiCmsComment entity)
            throws ServletException
    {
        //检查文章标识
        String articleId=request.getParameter("commentArticle");
        if(this.paramNullCheck(articleId))
        {
            throw new ServletException("未指定文章标识!");
        }

        //检查文章是否存在
        if(this.articleManager.find(articleId)==null)
        {
            throw new ServletException("指定的文章不存在!");
        }

        //检查评论内容
        String commentContent=request.getParameter("commentContent");
        if(this.paramNullCheck(commentContent))
        {
            throw new ServletException("未指定评论内容!");
        }

        //设置创建时间
        entity.setCreateTime(this.dp.getCurrent());

        //设置发布者
        BusiMember member=this.getMemberUserInSession(request);
        entity.setPublishMember(member.getCustId());
        entity.setPublishName(this.paramNullCheck(member.getMemberNick())?member.getMemberName():member.getMemberNick());

        //设置父类内容
        if(entity.getCommentParent()!=0)
        {
            //读取回复的评论属性
            BusiCmsComment comment=this.manager.find(entity.getCommentParent());
            if(comment==null)
            {
                throw new ServletException("指定回复的评论不存在!");
            }
            entity.setCommentUpcontent(comment.generateUpContent());
        }

        return entity;
    }

    /**
     * 方法（受保护）<br/>
     * 名称: afterCreate<br/>
     * 描述: 处理创建数据成功后的处理工作（多用于多对多数据的创建）<br/>
     *
     * @param request   - HTTP请求对象
     * @param primaryKey - 创建后的评论主键
     * @throws javax.servlet.ServletException
     */
    @Override
    protected void afterCreate(HttpServletRequest request,String primaryKey)
            throws ServletException
    {
        String articleId=request.getParameter("commentArticle");
        if(!this.paramNullCheck(articleId))
        {
            Map<String,Object> condition=new HashMap<>();
            condition.put("commentArticle=?",articleId);
            int count=this.manager.count(condition);

            BusiCmsArticle article=this.articleManager.find(articleId);
            article.setArticleCountComment(count);
            this.articleManager.edit(article);
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    beforeEdit<br>
     * 描述:    处理编辑数据前的处理工作（多用于多对多数据的创建）<br>
     *
     * @param request - HTTP请求对象
     * @param entity  - 构建的实体类
     * @return T - 处理后的实体类
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    protected BusiCmsComment beforeEdit(HttpServletRequest request,BusiCmsComment entity)
            throws ServletException
    {
        throw new ServletException("不支持此操作!");
    }

    /**
     * 方法（受保护）<br/>
     * 名称: generateCondition<br/>
     * 描述: 通过请求参数生成相应查询条件。<br/>
     * 参数格式为：searchValue=con1,value1|con2,value2|.....
     *
     * @param request - HTTP请求对象
     * @return Map<String,Object> - 查询条件集合
     * @throws javax.servlet.ServletException
     */
    @Override
    protected Map<String,Object> generateCondition(HttpServletRequest request)
            throws ServletException
    {
        Map<String,Object> condition=super.generateCondition(request);

        //参数判断
        String commentArticle=request.getParameter("commentArticle");
        if(!this.paramNullCheck(commentArticle))
        {
            condition.put("commentArticle=?",commentArticle);
        }
        else
        {
            throw new ServletException("未指定评论对应的文章标识！");
        }

        return condition;
    }

    /**
     * 方法（公共）
     * 名称： findToJson
     * 描述: 查询数据，返回Json结果<br/>
     *
     * @param request - HTTP请求对象
     * @return String - 获取成功返回JSON格式的列表结果
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    @Override
    public String findToJson(HttpServletRequest request)
            throws ServletException
    {
        //查询数据
        List list=this.findTo(request);
        list=this.generateOutput(request,list);
        boolean isOut=this.isCurrentUseNoLoginMode(request);

        //生成结果
        JSONObject json=new JSONObject();
        json.put("success",true);
        json.put("pageSize",this.manager.getPageSize());
        json.put("page",this.currentPage);
        json.put("total",this.manager.getTotalCount());
        json.put("totalPage",this.manager.getTotalPage());

        //列表内容
        JSONArray array=new JSONArray();
        for(Object obj : list)
        {
            BusiCmsComment comment=(BusiCmsComment)obj;
            JSONObject comJson=new JSONObject();

            //输出通用内容
            comJson.put("custId",comment.getCustId());
            comJson.put("commentArticle",comment.getCommentArticle());
            comJson.put("commentParent",comment.getCommentParent());
            comJson.put("commentContent",comment.getCommentContent());
            comJson.put("publishMember",comment.getPublishMember());
            comJson.put("publishName",comment.getPublishName());
            comJson.put("createTime",comment.getCreateTime());

            //增加上级回复内容
            if(!this.paramNullCheck(comment.getCommentUpcontent()))
            {
                JSONArray parentComments=new JSONArray(comment.getCommentUpcontent()+"]");
                comJson.put("parentComments",parentComments);
            }

            array.put(comJson);
        }

        json.put("list",array);
        return json.toString();
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    supportNoLoginFind<br/>
     * 描述:    是否支持非登录下查询处理<br/>
     *
     * @return boolean - 如果参数值为null或""，返回true
     */
    @Override
    public boolean supportNoLoginFind()
    {
        return true;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getLoginUserInSession<br>
     * 描述:    从Session中获取指定的登录用户对象<br>
     *
     * @param request - HTTP请求对象
     * @return LoginUser - 登录用户相关属性
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    public BusiMember getMemberUserInSession(HttpServletRequest request)
            throws ServletException
    {
        return (BusiMember)AbstractServlet.getAttributeInSession
                (request,BusiMemberServlet.SESSION_MEMBER_USER);
    }
}