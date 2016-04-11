package pb.system.cms.action;

import pb.data.Connector;
import pb.json.JSONArray;
import pb.json.JSONObject;
import pb.system.cms.entity.BusiCmsComment;
import pb.system.cms.manager.BusiCmsCommentFacade;
import pb.system.limit.action.AbstractAction;
import pb.system.limit.action.DataAction;
import pb.text.DateProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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
        //设置创建时间
        entity.setCreateTime(this.dp.getCurrent());

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
}