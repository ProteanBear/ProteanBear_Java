package pb.system.limit.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import pb.data.Connector;
import pb.data.EntityTransformer;
import pb.system.limit.entity.SystemApplication;
import pb.system.limit.entity.SystemArea;
import pb.system.limit.entity.SystemAreaType;
import pb.system.limit.entity.SystemUser;
import pb.system.limit.manager.SystemApplicationFacade;
import pb.system.limit.manager.SystemApplicationFacadeLocal;
import pb.system.limit.manager.SystemApplicationPlatformFacade;
import pb.system.limit.manager.SystemApplicationPlatformFacadeLocal;
import pb.system.limit.manager.SystemAreaFacade;
import pb.system.limit.manager.SystemAreaFacadeLocal;
import pb.system.limit.manager.SystemAreaTypeFacade;
import pb.system.limit.manager.SystemAreaTypeFacadeLocal;
import pb.system.limit.manager.SystemAreaTypeMapFacade;
import pb.system.limit.manager.SystemAreaTypeMapFacadeLocal;
import pb.system.limit.manager.SystemRoleFacade;
import pb.system.limit.manager.SystemRoleFacadeLocal;
import pb.system.limit.manager.SystemUserFacade;
import pb.system.limit.manager.SystemUserFacadeLocal;
import pb.system.limit.manager.SystemVersionFacade;
import pb.system.limit.manager.SystemVersionFacadeLocal;
import pb.system.limit.module.SystemAreaTreeNode;
import pb.system.limit.servlet.AbstractServlet;
import pb.system.limit.servlet.SystemAreaServlet;
import pb.system.limit.servlet.SystemAreaTreeServlet;
import pb.text.JsonProductor;

/**
 * 数据应用层类 - 行政区域树形结构数据（包括下级车辆、员工、驾驶人员等信息）。<br>
 * @author      proteanBear(马强)
 * @version     1.00 2011/11/04
 */
public class SystemAreaTreeAction extends AbstractAction<SystemArea> 
                            implements DataAction
{
    /**域(受保护)<br>
     * 名称:    manager<br>
     * 描述:    记录当前使用的数据管理器<br>
     */
    protected SystemAreaFacadeLocal areaManager;
     
    /**域(受保护)<br>
     * 名称:    areaTypeManager<br>
     * 描述:    行政区域对应企业类型的数据管理器对象<br>
     */
    protected SystemAreaTypeMapFacadeLocal areaTypeManager;
    
    /**域(受保护)<br>
     * 名称:    appManager<br>
     * 描述:    企业应用数据管理器对象<br>
     */
    protected SystemApplicationFacadeLocal appManager;
    
    /**域(受保护)<br>
     * 名称:    userManager<br>
     * 描述:    系统用户数据管理器对象<br>
     */
    protected SystemUserFacadeLocal userManager;
    
    /**域(受保护)<br>
     * 名称:    roleManager<br>
     * 描述:    系统用户角色数据管理器对象<br>
     */
    protected SystemRoleFacadeLocal roleManager;
    
    /**域(受保护)<br>
     * 名称:    platManager<br>
     * 描述:    系统用户角色数据管理器对象<br>
     */
    protected SystemApplicationPlatformFacadeLocal platManager;
    
    /**域(受保护)<br>
     * 名称:    versionManager<br>
     * 描述:    系统应用版本数据管理器对象<br>
     */
    protected SystemVersionFacadeLocal versionManager;
     
    /**构造函数<br>
     * @param connector - 数据库连接器对象
     */
    public SystemAreaTreeAction(Connector connector)
    {
        super(connector,SystemArea.class);
    }
    
    /**方法（受保护）<br>
     * 名称:    initManager<br>
     * 描述:    初始化数据管理器<br>
     */
    @Override protected void initManager()
    {
        this.manager=new SystemAreaFacade(this.connector);
        this.areaManager=(SystemAreaFacadeLocal) this.manager;
        this.areaTypeManager=new SystemAreaTypeMapFacade(this.connector);
        this.appManager=new SystemApplicationFacade(this.connector);
        this.userManager=new SystemUserFacade(this.connector);
        this.roleManager=new SystemRoleFacade(this.connector);
        this.platManager=new SystemApplicationPlatformFacade(this.connector);
        this.versionManager=new SystemVersionFacade(this.connector);
    }
     
    /**方法（公共）
     * 名称：   create
     * 描述:    创建数据<br>
     * @param request - HTTP请求对象
     */
    @Override public Object create(HttpServletRequest request)
             throws ServletException
    {
        try
        {
            //判断数据管理器是否初始化
            if (this.managerNullCheck())
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
            SystemArea entity = new SystemArea();

            //根据请求参数设置相应对象
            entity=(SystemArea) EntityTransformer.updateEntityByHttpRequest(
                           entityClass,
                           entity,
                           request,
                           this.areaManager.getPrimaryKeyName(),
                           false);

            //插入数据
            boolean success=this.areaManager.create(upId, entity);

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
        catch (IllegalAccessException 
                | IllegalArgumentException 
                | InstantiationException 
                | NoSuchMethodException 
                | InvocationTargetException 
                | ServletException ex)
        {
            throw new ServletException(ex.toString());
        }
    }
     
    /**方法（受保护）<br>
     * 名称:    afterCreate<br>
     * 描述:    处理创建数据成功后的处理工作（多用于多对多数据的创建）<br>
     * @param request - HTTP请求对象
     * @param areaId - 生成的行政区域标识
     * @throws ServletException - 抛出处理错误
     */
    @Override protected void afterCreate(HttpServletRequest request,String areaId) 
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
            int successNum=0,errorNum=0;
            String typeId,types;
            String[] typeArr;
            boolean success;
            //如果企业类型数组为空并且行政区域类型为部门,则继承上级部门的企业类型
            if((areaTypes==null||areaTypes.length==0)&&"1".equals(areaClass))
            {
                String upId=this.areaManager.getUpAreaId(areaId);
                if(upId==null) throw new ServletException(this.areaManager.getError());

                SystemAreaTypeFacadeLocal typeManager=new SystemAreaTypeFacade(this.connector);
                List<SystemAreaType> list=typeManager.findByAreaid(areaId);

                if(list!=null)
                {
                    for (SystemAreaType areaType : list)             
                    {
                        typeId = areaType.getTypeId();
                        success=this.areaTypeManager.create(areaId, typeId);
                        if(success)    successNum++;
                        else           errorNum++;
                    }
                }
            }
            //否则，根据指定的企业类型处理
            else
            {
                for (String areaType : areaTypes)             
                {
                    types = areaType;
                    if(this.paramNullCheck(types)) continue;
                    //存在逗号分隔，则再次分解遍历
                    typeArr=types.split(",");
                    for (String type : typeArr)
                    {
                        typeId = type;
                        success=this.areaTypeManager.create(areaId, typeId);
                        if(success)    successNum++;
                        else           errorNum++;
                    }
                }
            }

            //处理结果
            if(errorNum!=0)
            {
                throw new ServletException("成功创建对应企业类型数据"+successNum+"个,"
                                           + "失败"+(errorNum)+"个");
            }
        }
        catch (ServletException ex)
        {
            throw new ServletException(ex.toString());
        }
    }
     
    /**方法（受保护）<br>
     * 名称:    afterEdit<br>
     * 描述:    处理编辑数据成功后的处理工作（多用于多对多数据的修改）<br>
     * @param request - HTTP请求对象
     * @throws ServletException - 抛出处理错误
     */
    @Override protected void afterEdit(HttpServletRequest request) 
             throws ServletException
    {
        //获取企业类型标识
        String areaId = request.getParameter(AbstractServlet.PARAM_PRIMARYKEY);

        //删除企业类型对应的插件
        boolean success=this.areaTypeManager.removeByAreaId(areaId);

        if(!success)
        {
            throw new ServletException(this.areaTypeManager.getError());
        }

        //重新创建企业类型对应的插件
        this.afterCreate(request,areaId);
    }
    
    /**方法（受保护）<br>
     * 名称:    findTo<br>
     * 描述:    查询数据，并返回List<br>
     * @param request - HTTP请求对象
     * @return List - 数据对象列表
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    @Override protected List<SystemArea> findTo(HttpServletRequest request) 
            throws ServletException
    {
        try
        {
            //判断数据管理器是否初始化
            if (this.managerNullCheck())
            {
                throw new ServletException("数据库管理器未初始化！");
            }

            //获取指定的搜索条件集并设置搜索条件
            Map<String,Object> condition=this.generateCondition(request);

            //查询结果
            List<SystemArea> list=this.manager.find(condition);
            if(list==null) throw new ServletException(this.manager.getError());
            return list;
        }
        catch (NumberFormatException 
                | ServletException ex)
        {
            throw new ServletException(ex.toString());
        }
    }
    
    /**方法（受保护）<br>
     * 名称:    findAppTo<br>
     * 描述:    查询数据，并返回List<br>
     * @param request - HTTP请求对象
     * @return List - 数据对象列表
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    protected List<SystemApplication> findAppTo(HttpServletRequest request) 
            throws ServletException
    {
        try
        {
            //判断数据管理器是否初始化
            if (this.managerNullCheck())
            {
                throw new ServletException("数据库管理器未初始化！");
            }
            
            //获取顶级区域标识
            String upId=request.getParameter(SystemAreaTreeServlet.PARAM_UPID);
            if (this.paramNullCheck(upId))
            {
                throw new ServletException("未指定上级区域！");
            }

            //获取指定的搜索条件集并设置搜索条件
            Map<String,Object> condition=new HashMap<>();
            condition.put("areaId like ?", upId+"%");

            //查询结果
            List<SystemApplication> list=this.appManager.find(condition);
            if(list==null) throw new ServletException(this.appManager.getError());
            return list;
        }
        catch (NumberFormatException 
                | ServletException ex)
        {
            throw new ServletException(ex.toString());
        }
    }
    
    /**方法（受保护）<br>
     * 名称:    findUserTo<br>
     * 描述:    查询数据，并返回List<br>
     * @param request - HTTP请求对象
     * @return List - 数据对象列表
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    protected List<SystemUser> findUserTo(HttpServletRequest request) 
            throws ServletException
    {
        try
        {
            //判断数据管理器是否初始化
            if (this.managerNullCheck())
            {
                throw new ServletException("数据库管理器未初始化！");
            }
            
            //获取顶级区域标识
            String upId=request.getParameter(SystemAreaTreeServlet.PARAM_UPID);
            if (this.paramNullCheck(upId))
            {
                throw new ServletException("未指定上级区域！");
            }

            //获取指定的搜索条件集并设置搜索条件
            Map<String,Object> condition=new HashMap<>();
            condition.put("areaId like ?", upId+"%");

            //查询结果
            List<SystemUser> list=this.userManager.find(condition);
            if(list==null) throw new ServletException(this.appManager.getError());
            return list;
        }
        catch (NumberFormatException 
                | ServletException ex)
        {
            throw new ServletException(ex.toString());
        }
    }
    
    /**方法（受保护）<br>
     * 名称:    generateCondition<br>
     * 描述:    通过请求参数生成相应查询条件。<br>
     *          参数格式为：searchValue=con1,value1|con2,value2|.....
     * @param request - HTTP请求对象
     * @throws javax.servlet.ServletException 
     * @return Map<String,Object> - 查询条件集合
     */
    @Override protected Map<String,Object> generateCondition(HttpServletRequest request) 
             throws ServletException
    {
        Map<String,Object> condition=super.generateCondition(request);

        //获取顶级标识
        /*String upId=request.getParameter(SystemAreaTreeServlet.PARAM_UPID);
        if(this.paramNullCheck(upId))
        {
            //获取当前指定的顶级区域
            SystemArea area=this.areaManager.find(upId);
            if(area==null)
            {
                throw new ServletException("指定的上级区域不存在！");
            }
        }*/

        return condition;
    }
     
    /**方法（受保护）<br>
     * 名称:    generateOutput<br>
     * 描述:    用于对查询的数据进行处理，增加新的字段内容（多用于多对多数据的输出）<br>
     * @param request - HTTP请求对象
     * @param list - 数据对象列表
     * @return List - 处理后的数据对象列表
     */
    @Override protected List<Object> generateOutput
            (HttpServletRequest request,List<SystemArea> list)
    {
        List<Object> result=null;
        if(list!=null)
        {
            result=new ArrayList<>();
            for (SystemArea sysArea : list)
            {
                SystemAreaTreeNode area = new SystemAreaTreeNode(sysArea,
                        this.haveSubAreaNode(list,sysArea.getAreaId()));
                area.setAreaTypeMaps(this.areaTypeManager.findByAreaId(sysArea.getAreaId()));
                result.add(area);
            }
        }
        return result;
    }
    
    /**方法（受保护）<br>
     * 名称:    generateAppOutput<br>
     * 描述:    用于对查询的数据进行处理，增加新的字段内容（多用于多对多数据的输出）<br>
     * @param request - HTTP请求对象
     * @param list - 数据对象列表
     * @return List - 处理后的数据对象列表
     */
    protected List<Object> generateAppOutput
            (HttpServletRequest request,List<SystemApplication> list)
    {
        List<Object> result=null;
        if(list!=null)
        {
            result=new ArrayList<>();
            for (SystemApplication app : list)
            {
                SystemAreaTreeNode area = new SystemAreaTreeNode(app);
                area.setAreaTypeMapsForPlat(this.platManager.findByAppId(app.getAppId()),this.versionManager);
                result.add(area);
            }
        }
        return result;
    }
            
    /**方法（受保护）<br>
     * 名称:    generateUserOutput<br>
     * 描述:    用于对查询的数据进行处理，增加新的字段内容（多用于多对多数据的输出）<br>
     * @param request - HTTP请求对象
     * @param list - 数据对象列表
     * @return List - 处理后的数据对象列表
     */
    protected List<Object> generateUserOutput
            (HttpServletRequest request,List<SystemUser> list)
    {
        List<Object> result=null;
        if(list!=null)
        {
            result=new ArrayList<>();
            for (SystemUser user : list)
            {
                SystemAreaTreeNode area = new SystemAreaTreeNode(user);
                area.setAreaTypeMapsForRole(this.roleManager.findByUserId(user.getCustId()));
                result.add(area);
            }
        }
        return result;
    }
     
    /**方法（受保护）<br>
     * 名称:    getAreaLimitField<br>
     * 描述:    指定当前业务处理部门限制使用的行政区域字段名，使用null为不指定<br>
     * @return String - 当前业务处理部门限制使用的行政区域字段名
     */
    @Override protected String getAreaLimitField(){return "areaId";}
    
    /**方法（私有）<br>
     * 名称:    haveSubAreaNode<br>
     * 描述:    遍历列表查看当前节点是否有子节点<br>
     * @return boolean - 是否有子节点
     */
    private boolean haveSubAreaNode(List<SystemArea> list,String areaId)
    {
        boolean result=false;
        for (SystemArea sysArea : list)
        {
            if(sysArea.getAreaId().startsWith(areaId))
            {
                result=true;
                break;
            }
        }
        return result;
    }
    
    /**方法（公共）
     * 名称：   findToJson
     * 描述:    查询数据，返回Json结果<br>
     * @param request - HTTP请求对象
     * @return String - 获取成功返回JSON格式的列表结果
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    @Override public String findToJson(HttpServletRequest request) 
             throws ServletException
    {
        String treeMode=request.getParameter(SystemAreaTreeServlet.PARAM_TREEMODE);

        if(!this.paramNullCheck(treeMode))
        {
            List list=new ArrayList();
            if(null != treeMode)switch (treeMode)
            {
                case "sub":
                    List appList=this.findAppTo(request);
                    appList=this.generateAppOutput(request,appList);
                    List userList=this.findUserTo(request);
                    userList=this.generateUserOutput(request,userList);
                    list.addAll(appList);
                    list.addAll(userList);
                    break;
                case "app":
                    list=this.findAppTo(request);
                    list=this.generateAppOutput(request,list);
                    break;
                case "user":
                    list=this.findUserTo(request);
                    list=this.generateUserOutput(request,list);
                    break;
                default:
                    break;
            }
            return JsonProductor.toJsonByObjectList(list);
        }
        else
        {
            //查询数据
           List list=this.findTo(request);
           list=this.generateOutput(request,list);

           int pageSize=this.manager.getPageSize();
           int page=this.currentPage;
           int total=this.manager.getTotalCount();
           int totalPage=this.manager.getTotalPage();

           return JsonProductor.toJsonByObjectList(list, pageSize, page, total,totalPage);
        }
    }
}