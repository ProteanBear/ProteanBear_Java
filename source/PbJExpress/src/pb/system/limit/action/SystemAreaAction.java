package pb.system.limit.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import pb.data.Connector;
import pb.data.EntityTransformer;
import pb.system.limit.entity.SystemArea;
import pb.system.limit.entity.SystemAreaType;
import pb.system.limit.manager.*;
import pb.system.limit.module.SystemAreaOutput;
import pb.system.limit.servlet.AbstractServlet;
import pb.system.limit.servlet.SystemAreaServlet;

/**
 * 数据应用层类 - 行政区域表。<br>
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/11/04
 */
public class SystemAreaAction extends AbstractAction<SystemArea>
        implements DataAction
{
    /**
     * 域(受保护)<br>
     * 名称:    manager<br>
     * 描述:    记录当前使用的数据管理器<br>
     */
    protected SystemAreaFacadeLocal areaManager;

    /**
     * 域(受保护)<br>
     * 名称:    areaTypeManager<br>
     * 描述:    行政区域对应企业类型的数据管理器对象<br>
     */
    protected SystemAreaTypeMapFacadeLocal areaTypeManager;

    /**
     * 构造函数<br>
     *
     * @param connector - 数据库连接器对象
     */
    public SystemAreaAction(Connector connector)
    {
        super(connector,SystemArea.class);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    initManager<br>
     * 描述:    初始化数据管理器<br>
     */
    @Override
    protected void initManager()
    {
        this.manager=new SystemAreaFacade(this.connector);
        this.areaManager=(SystemAreaFacadeLocal)this.manager;
        this.areaTypeManager=new SystemAreaTypeMapFacade(this.connector);
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
            String upId=request.getParameter(SystemAreaServlet.PARAM_UPID);
            if(upId==null)
            {
                throw new ServletException("未指定上级行政区域标识！");
            }

            //创建数据实体对象
            SystemArea entity=new SystemArea();

            //根据请求参数设置相应对象
            entity=(SystemArea)EntityTransformer.updateEntityByHttpRequest(
                    entityClass,
                    entity,
                    request,
                    this.areaManager.getPrimaryKeyName(),
                    false
            );

            //插入数据
            boolean success=this.areaManager.create(upId,entity);

            //处理结果
            if(!success)
            {
                throw new ServletException(this.areaManager.getError());
            }
            //创建成功则调用创建后处理工作
            else
            {
                this.afterCreate(request,this.areaManager.getLastGenerator());
            }

            return this.areaManager.getLastGenerator();
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
     * 名称:    afterCreate<br>
     * 描述:    处理创建数据成功后的处理工作（多用于多对多数据的创建）<br>
     *
     * @param request - HTTP请求对象
     * @param areaId  - 生成的行政区域标识
     * @throws ServletException - 抛出处理错误
     */
    @Override
    protected void afterCreate(HttpServletRequest request,String areaId)
            throws ServletException
    {
        try
        {
            //获取指定的企业类型标识数组
            String[] areaTypes=request.getParameterValues
                    (SystemAreaServlet.PARAM_AREATYPE);

            //获取当前的行政区域类型
            String areaClass=request.getParameter("areaClass");

            //遍历删除主键集
            int successNum=0, errorNum=0;
            String typeId, types;
            String[] typeArr;
            boolean success;
            //如果企业类型数组为空并且行政区域类型为部门,则继承上级部门的企业类型
            if((areaTypes==null || areaTypes.length==0) && "1".equals(areaClass))
            {
                String upId=this.areaManager.getUpAreaId(areaId);
                if(upId==null) throw new ServletException(this.areaManager.getError());

                SystemAreaTypeFacadeLocal typeManager=new SystemAreaTypeFacade(this.connector);
                List<SystemAreaType> list=typeManager.findByAreaid(areaId);

                if(list!=null)
                {
                    for(SystemAreaType areaType : list)
                    {
                        typeId=areaType.getTypeId();
                        success=this.areaTypeManager.create(areaId,typeId);
                        if(success)
                        {
                            successNum++;
                        }
                        else
                        {
                            errorNum++;
                        }
                    }
                }
            }
            //否则，根据指定的企业类型处理
            else
            {
                if(areaTypes!=null)
                {
                    for(String areaType : areaTypes)
                    {
                        types=areaType;
                        if(this.paramNullCheck(types)) continue;
                        //存在逗号分隔，则再次分解遍历
                        typeArr=types.split(",");
                        for(String type : typeArr)
                        {
                            typeId=type;
                            success=this.areaTypeManager.create(areaId,typeId);
                            if(success)
                            {
                                successNum++;
                            }
                            else
                            {
                                errorNum++;
                            }
                        }
                    }
                }
            }

            //处理结果
            if(errorNum!=0)
            {
                throw new ServletException(
                        "成功创建对应企业类型数据"+successNum+"个,"
                                +"失败"+(errorNum)+"个"
                );
            }
        }
        catch(ServletException ex)
        {
            throw new ServletException(ex.toString());
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
    @Override
    protected SystemArea beforeEdit(HttpServletRequest request,SystemArea entity)
            throws ServletException
    {
        //企业类型重置企业图片
        if(entity.getAreaClass()==1)
        {
            entity.setAreaIconOpen(entity.getAreaIcon());
            entity.setAreaIconClose(entity.getAreaIcon());
        }

        return entity;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    afterEdit<br>
     * 描述:    处理编辑数据成功后的处理工作（多用于多对多数据的修改）<br>
     *
     * @param request - HTTP请求对象
     * @throws ServletException - 抛出处理错误
     */
    @Override
    protected void afterEdit(HttpServletRequest request)
            throws ServletException
    {
        //获取企业类型标识
        String areaId=request.getParameter(AbstractServlet.PARAM_PRIMARYKEY);

        //获取指定的区域
        SystemArea area=this.manager.find(areaId);
        if(area!=null && area.getAreaClass()==1)
        {
            //删除企业类型对应的插件
            boolean success=this.areaTypeManager.removeByAreaId(areaId);

            if(!success)
            {
                throw new ServletException(this.areaTypeManager.getError());
            }

            //重新创建企业类型对应的插件
            this.afterCreate(request,areaId);
        }
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
    protected List<Object> generateOutput
    (HttpServletRequest request,List<SystemArea> list)
    {
        List<Object> result=null;
        if(list!=null)
        {
            result=new ArrayList<>();
            for(SystemArea sysArea : list)
            {
                SystemAreaOutput area=new SystemAreaOutput(sysArea);
                area.setUpId(this.areaManager.getUpAreaId(sysArea.getAreaId()));
                area.setAreaTypeMaps(this.areaTypeManager.findByAreaId(sysArea.getAreaId()));
                result.add(area);
            }
        }
        return result;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getAreaLimitField<br>
     * 描述:    指定当前业务处理部门限制使用的行政区域字段名，使用null为不指定<br>
     *
     * @return String - 当前业务处理部门限制使用的行政区域字段名
     */
    @Override
    protected String getAreaLimitField()
    {
        return "areaId";
    }
}
