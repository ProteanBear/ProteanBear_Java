package pb.system.limit.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import pb.data.Connector;
import pb.data.EntityTransformer;
import pb.system.limit.entity.BusiSection;
import pb.system.limit.manager.BusiSectionFacade;
import pb.system.limit.manager.BusiSectionFacadeLocal;
import pb.system.limit.module.BusiSectionOutput;
import pb.system.limit.servlet.AbstractServlet;
import pb.system.limit.servlet.BusiSectionServlet;

/**
 * 数据应用层类——记录当前系统业务栏目划分的数据信息。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-31
 */
public class BusiSectionAction extends AbstractAction<BusiSection> implements DataAction
{
    /**
     * 域(受保护)<br>
     * 名称:    sectionManager<br>
     * 描述:    记录当前使用的栏目数据管理器<br>
     */
    protected BusiSectionFacadeLocal sectionManager;

    /**
     * 构造函数
     *
     * @param connector - 数据库连接器对象
     */
    public BusiSectionAction(Connector connector)
    {
        super(connector,BusiSection.class);
    }

    /**
     * 方法（受保护）
     * 名称:initManager
     * 描述:初始化数据管理器
     */
    @Override
    protected void initManager()
    {
        this.manager=new BusiSectionFacade(this.connector);
        this.sectionManager=(BusiSectionFacadeLocal)this.manager;
    }

    /**
     * 方法（公共）
     * 名称：   create
     * 描述:    创建数据<br>
     *
     * @param request - HTTP请求对象
     */
    @Override
    public Object create(HttpServletRequest request)
            throws ServletException
    {
        try
        {
            //判断数据管理器是否初始化
            if(this.managerNullCheck())
            {
                throw new ServletException("数据库管理器未初始化！");
            }

            //获取上层行政区域ID
            String upId=request.getParameter(BusiSectionServlet.PARAM_UPID);

            //创建数据实体对象
            BusiSection entity=new BusiSection();

            //根据请求参数设置相应对象
            entity=(BusiSection)EntityTransformer.updateEntityByHttpRequest(
                    entityClass,
                    entity,
                    request,
                    this.sectionManager.getPrimaryKeyName(),
                    false
            );

            //插入数据
            boolean success=this.sectionManager.create(upId,entity);

            //处理结果
            if(!success)
            {
                throw new ServletException(this.sectionManager.getError());
            }
            //创建成功则调用创建后处理工作
            else
            {
                this.afterCreate(request,this.sectionManager.getLastGenerator());
            }

            return this.sectionManager.getLastGenerator();
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |InstantiationException
                |NoSuchMethodException
                |InvocationTargetException
                |ServletException ex)
        {
            throw new ServletException(ex.toString());
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    generateCondition<br>
     * 描述:    通过请求参数生成相应查询条件。<br>
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
            String app=request.getParameter(BusiSectionServlet.PARAM_APP);
            if(this.paramNullCheck(app))
            {
                throw new ServletException("未指定栏目所属的应用！");
            }
            condition.put("sectionApp=?",app);
        }
        //外部接口
        if(this.isCurrentUseNoLoginMode(request))
        {
            String app=request.getParameter(AbstractServlet.PARAM_FROMAPP);
            if(!this.paramNullCheck(app))
            {
                condition.put("sectionApp=?",app);
            }

            String inUse=request.getParameter(BusiSectionServlet.PARAM_INUSE);
            if(!this.paramNullCheck(inUse)&&"1".equals(inUse))
            {
                condition.put("sectionEnable=?",1);
            }
            else
            {
                String upId=request.getParameter(BusiSectionServlet.PARAM_UPID);
                upId=(this.paramNullCheck(upId))?"":upId;
                condition.put("sectionCode like ?",upId+"____");
            }
        }

        return condition;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    generateOutput<br>
     * 描述:    用于对查询的数据进行处理，增加新的字段内容（多用于多对多数据的输出）<br>
     *
     * @param request - HTTP请求对象
     * @param list    - 数据对象列表
     * @return List - 处理后的数据对象列表
     */
    @Override
    protected List<Object> generateOutput(HttpServletRequest request,List<BusiSection> list)
    {
        List<Object> result=null;
        if(list!=null)
        {
            result=new ArrayList<>();
            for(BusiSection busiSection : list)
            {
                if(!this.isCurrentUseNoLoginMode(request))
                {
                    BusiSectionOutput section=new BusiSectionOutput(
                            busiSection,
                            this.sectionManager.existChildren(busiSection.getSectionCode(),busiSection.getSectionApp())
                    );
                    result.add(section);
                }
                else
                {
                    result.add(busiSection);
                }
            }
        }
        return result;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    supportNoLoginFind<br>
     * 描述:    是否支持非登录下查询处理<br>
     *
     * @return boolean - 如果参数值为null或""，返回true
     */
    @Override
    public boolean supportNoLoginFind()
    {
        return true;
    }
}