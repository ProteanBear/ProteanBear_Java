package pb.system.cms.action;

import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import pb.data.Connector;
import pb.helper.GeneralHelper;
import pb.system.cms.entity.BusiFeedback;
import pb.system.cms.manager.BusiFeedbackFacade;
import pb.system.cms.servlet.BusiFeedbackServlet;
import pb.system.limit.action.AbstractAction;
import pb.system.limit.action.DataAction;
import pb.system.limit.servlet.AbstractServlet;

/**
 * 数据应用层类——记录应用的意见反馈信息
 *
 * @author ProteanBear
 * @version 1.00 2014-11-20
 */
public class BusiFeedbackAction extends AbstractAction<BusiFeedback> implements DataAction
{

    /**
     * 构造函数
     *
     * @param connector - 数据库连接器对象
     */
    public BusiFeedbackAction(Connector connector)
    {
        super(connector,BusiFeedback.class);
    }

    /**
     * 方法（受保护） 名称:initManager 描述:初始化数据管理器
     */
    @Override
    protected void initManager()
    {
        this.manager=new BusiFeedbackFacade(this.connector);
    }

    /**
     * 方法（公共）<br/>
     * 名称: beforeCreate<br/>
     * 描述: 处理创建数据前的处理工作（多用于无主键生成器的数据表）<br/>
     *
     * @param request - HTTP请求对象
     * @param entity  - 构建的实体类
     * @return T - 处理后的实体类
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    @Override
    public BusiFeedback beforeCreate(HttpServletRequest request,BusiFeedback entity)
            throws ServletException
    {
        String app=request.getParameter(AbstractServlet.PARAM_FROMAPP);

        //后台访问
        if(!this.isCurrentUseNoLoginMode(request))
        {
            throw new ServletException("不支持后台创建反馈的功能！");
        }
        //外部接口
        else
        {
            if(this.paramNullCheck(app))
            {
                throw new ServletException("未指定所属的应用");
            }
            if(this.paramNullCheck(entity.getFeedbackEmail()))
            {
                throw new ServletException("未填写反馈人员的邮箱地址");
            }
            if(!GeneralHelper.isStrEmailAddress(entity.getFeedbackEmail()))
            {
                throw new ServletException("填写的邮箱格式不正确");
            }
            if(this.paramNullCheck(entity.getFeedbackContent()))
            {
                throw new ServletException("未填写反馈的内容");
            }
        }

        entity.setFeedbackApp(app);
        return entity;
    }

    /**
     * 方法（受保护）<br/>
     * 名称: beforeEdit<br/>
     * 描述: 处理编辑数据前的处理工作（多用于多对多数据的创建）<br/>
     *
     * @param request - HTTP请求对象
     * @param entity  - 构建的实体类
     * @return T - 处理后的实体类
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    @Override
    protected BusiFeedback beforeEdit
    (HttpServletRequest request,BusiFeedback entity)
            throws ServletException
    {
        throw new ServletException("不支持反馈的修改功能");
        //return entity;
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    generateCondition<br/>
     * 描述:    通过请求参数生成相应查询条件。<br/>
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

        //后台访问
        if(!this.isCurrentUseNoLoginMode(request))
        {
            //增加所属应用判断
            String app=request.getParameter(BusiFeedbackServlet.PARAM_APP);
            if(this.paramNullCheck(app))
            {
                throw new ServletException("未指定栏目所属的应用！");
            }
            condition.put("sectionApp=?",app);
        }
        //外部接口
        else
        {
            throw new ServletException("未登录无法查询反馈信息！");
        }

        return condition;
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
     * 方法（受保护）<br/>
     * 名称:    supportNoLoginMode<br/>
     * 描述:    支持非登录下处理模式<br/>
     *
     * @return boolean - 如果参数值为null或""，返回true
     */
    @Override
    public String supportNoLoginMode(HttpServletRequest request)
    {
        return AbstractServlet.VALUE_MODE_CREATE;
    }
}
