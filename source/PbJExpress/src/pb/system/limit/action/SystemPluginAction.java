package pb.system.limit.action;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import pb.data.Connector;
import pb.data.EntityTransformer;
import pb.system.limit.entity.OriginalLimit;
import pb.system.limit.entity.SystemPlugin;
import pb.system.limit.manager.OriginalLimitFacade;
import pb.system.limit.manager.OriginalLimitFacadeLocal;
import pb.system.limit.manager.SystemPluginFacade;
import pb.system.limit.manager.SystemPluginFacadeLocal;
import pb.system.limit.servlet.SystemPluginServlet;

/**
 * 数据应用层类——系统插件数据表，记录当前系统支持的全部功能模块，每个功能模块包含功能插件集合；
 * 功能插件为具体的功能，如文章管理，可对应到具体的操作页面，每个功能插件包含一个功能
 *@author ProteanBear
 *@version 1.00 2014-07-15
 */
public class SystemPluginAction extends AbstractAction<SystemPlugin> implements DataAction
{
    /**域(受保护)<br>
     * 名称:    limitManager<br>
     * 描述:    基础功能权限管理器对象<br>
     */
    protected OriginalLimitFacadeLocal limitManager;
    
    /**域(受保护)<br>
     * 名称:    pluginManager<br>
     * 描述:    系统插件管理器对象<br>
     */
    protected SystemPluginFacadeLocal pluginManager;
    
    /**构造函数
     * @param connector - 数据库连接器对象
     */
    public SystemPluginAction(Connector connector)
    {
       super(connector,SystemPlugin.class);
    }

    /**方法（受保护）
     * 名称:initManager
     * 描述:初始化数据管理器
     */
    @Override protected void initManager()
    {
        this.manager=new SystemPluginFacade(this.connector);
        this.pluginManager=(SystemPluginFacadeLocal) this.manager;
        this.limitManager=new OriginalLimitFacade(this.connector);
    }
    
    /**方法（公共）
     * 名称：   create
     * 描述:    创建数据<br>
     * @param request - HTTP请求对象
     */@Override public void create(HttpServletRequest request) 
             throws ServletException
     {
         try
         {
             //判断数据管理器是否初始化
             if (this.managerNullCheck())
             {
                 throw new ServletException("数据库管理器未初始化！");
             }
             
             //获取创建类型
             String pluginType=request.getParameter("pluginType");
             if(this.paramNullCheck(pluginType))
             {
                 throw new ServletException("未指定添加类型！");
             }
             
             //获取上层行政区域ID
             String upId=request.getParameter(SystemPluginServlet.PARAM_UPID);
             if(this.paramNullCheck(upId)&&!"0".equals(pluginType))
             {
                 throw new ServletException("未指定上级模块或插件标识！");
             }
             upId=("0".equals(pluginType))?"":upId;

             //创建数据实体对象
             SystemPlugin entity = new SystemPlugin();
             
             //根据请求参数设置相应对象
             entity=(SystemPlugin) EntityTransformer.updateEntityByHttpRequest(
                            entityClass,
                            entity,
                            request,
                            this.pluginManager.getPrimaryKeyName(),
                            false);
             
             //插入数据
             boolean success=this.pluginManager.create(upId, entity);
             
             //处理结果
             if(!success)
             {
                 throw new ServletException(this.pluginManager.getError());
             }
             //创建成功则调用创建后处理工作
             else
             {
                 this.afterCreate(request,this.pluginManager.getLastGenerator());
             }
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
     * 描述:    处理创建数据成功后的处理工作（多用于多对多数据的创建）<
     * @throws javax.servlet.ServletException - 抛出处理错误
     * @param request - HTTP请求对象
     * @param primaryKey - 创建记录后生成的主键（只适用于自定义的主键生成器）
     */
    @Override protected void afterCreate
             (HttpServletRequest request,String primaryKey) 
             throws ServletException
    {
        //获取创建的数据类型
        String pluginType=request.getParameter("pluginType");
        
        //添加插件时自动创建基础权限
        if(!this.paramNullCheck(pluginType)&&"1".equals(pluginType))
        {
            //读取全部功能权限类型
            List<OriginalLimit> limitList=this.limitManager.findAll();
            //创建基本权限
            for (OriginalLimit limit : limitList)
            {
                if(limit.getLimitInit()==0) continue;
                SystemPlugin plugin=new SystemPlugin();
                plugin.setPluginCode(limit.getLimitId()+"");
                plugin.setPluginName(limit.getLimitName());
                plugin.setPluginParent(limit.getLimitId()+"");
                plugin.setPluginType(2);
                this.pluginManager.create(primaryKey,plugin);
            }
        }
    }
}