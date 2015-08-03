package pb.system.limit.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import pb.data.Connector;
import pb.system.limit.entity.SystemAreaType;
import pb.system.limit.manager.SystemAreaTypeFacade;
import pb.system.limit.manager.SystemAreaTypePluginFacade;
import pb.system.limit.manager.SystemAreaTypePluginFacadeLocal;
import pb.system.limit.manager.SystemPluginFacade;
import pb.system.limit.manager.SystemPluginFacadeLocal;
import pb.system.limit.module.LoginUser;
import pb.system.limit.module.SystemAreaTypeOutput;
import pb.system.limit.servlet.AbstractServlet;
import pb.system.limit.servlet.SystemAreaTypeServlet;

/**
 * 数据应用层类 - 企业类型信息表。<br>
 * 增加findPluginsOfAreaType方法，在输出时包含企业类型包含插件。<br>
 *
 * @author proteanBear(马强)
 * @version 1.01 2012/02/28
 */
public class SystemAreaTypeAction extends AbstractAction<SystemAreaType>
        implements DataAction
{
    /**
     * 域(受保护)<br>
     * 名称:    typePluginManager<br>
     * 描述:    企业类型对应插件的数据管理器对象<br>
     */
    protected SystemAreaTypePluginFacadeLocal typePluginManager;

    /**
     * 域(受保护)<br>
     * 名称:    pluginManager<br>
     * 描述:    企业类型对应插件的数据管理器对象<br>
     */
    protected SystemPluginFacadeLocal pluginManager;

    /**
     * 构造函数<br>
     *
     * @param connector - 数据库连接器对象
     */
    public SystemAreaTypeAction(Connector connector)
    {
        super(connector,SystemAreaType.class);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    initManager<br>
     * 描述:    初始化数据管理器<br>
     */
    @Override
    protected void initManager()
    {
        this.manager=new SystemAreaTypeFacade(this.connector);
        this.typePluginManager=new SystemAreaTypePluginFacade(this.connector);
        this.pluginManager=new SystemPluginFacade(this.connector);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    afterCreate<br>
     * 描述:    处理创建数据成功后的处理工作（多用于多对多数据的创建）<br>
     *
     * @param request - HTTP请求对象
     * @param typeId  - 创建后的企业类型标识
     * @throws ServletException - 抛出处理错误
     */
    @Override
    protected void afterCreate(HttpServletRequest request,String typeId)
            throws ServletException
    {
        try
        {
            //获取指定的企业类型标识数组
            String[] typePlugins=request.getParameterValues
                    (SystemAreaTypeServlet.PARAM_PLUGIN);

            //类型判断
            if(typePlugins!=null)
            {
                //遍历删除主键集
                int successNum=0, pluginCustId, errorNum=0;
                String pluginId, plugins;
                String[] pluginArr;
                boolean success;
                for(String typePlugin : typePlugins)
                {
                    plugins=typePlugin;
                    if(this.paramNullCheck(plugins)) continue;
                    //存在逗号分隔，则再次分解遍历
                    pluginArr=plugins.split(",");
                    for(String plugin : pluginArr)
                    {
                        pluginId=plugin;
                        pluginCustId=Integer.parseInt(pluginId);
                        //获取指定的插件，保证插件类型必须为插件，而非模块和权限
                        success=false;
                        if(this.pluginManager.existPlugin(pluginCustId))
                        {
                            success=this.typePluginManager.create(typeId,pluginCustId);
                        }
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

                //处理结果
                if(errorNum!=0)
                {
                    throw new ServletException(
                            "成功创建对应功能插件数据"+successNum+"个,"
                                    +"失败"+(errorNum)+"个"
                    );
                }
            }
        }
        catch(NumberFormatException|ServletException ex)
        {
            throw new ServletException(ex.toString());
        }
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
        String typeId=request.getParameter(AbstractServlet.PARAM_PRIMARYKEY);

        //删除企业类型对应的插件
        boolean success=this.typePluginManager.removeByTypeId(typeId);

        if(!success)
        {
            throw new ServletException(this.typePluginManager.getError());
        }

        //重新创建企业类型对应的插件
        this.afterCreate(request,typeId);
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

        //加入企业类型级别限制，获取到的企业类型级别必须大于当前企业类型级别
        LoginUser user=this.getLoginUserInSession(request);
        condition.put("typeLevel>=?",user.getTypeLevel());

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
    protected List<Object> generateOutput
    (HttpServletRequest request,List<SystemAreaType> list)
    {
        List<Object> result=null;
        if(list!=null)
        {
            result=new ArrayList<>();
            for(SystemAreaType areaType : list)
            {
                SystemAreaTypeOutput type=new SystemAreaTypeOutput(areaType);
                type.setTypePlugins(this.typePluginManager.findByTypeId(areaType.getTypeId()));
                result.add(type);
            }
        }
        return result;
    }
}
