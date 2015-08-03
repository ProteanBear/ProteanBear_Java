package pb.system.limit.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import pb.data.Connector;
import pb.system.limit.entity.SystemApplication;
import pb.system.limit.manager.SystemApplicationFacade;
import pb.system.limit.manager.SystemApplicationPlatformFacade;
import pb.system.limit.manager.SystemApplicationPlatformFacadeLocal;
import pb.system.limit.module.SystemApplicationOutput;
import pb.system.limit.servlet.AbstractServlet;
import pb.system.limit.servlet.SystemApplicationServlet;

/**
 * 数据应用层类——记录企业运维的APP应用的相关信息。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-15
 */
public class SystemApplicationAction extends AbstractAction<SystemApplication> implements DataAction
{
    /**
     * 域(受保护)<br>
     * 名称:    typePluginManager<br>
     * 描述:    企业类型对应插件的数据管理器对象<br>
     */
    protected SystemApplicationPlatformFacadeLocal platManager;

    /**
     * 构造函数
     *
     * @param connector - 数据库连接器对象
     */
    public SystemApplicationAction(Connector connector)
    {
        super(connector,SystemApplication.class);
    }

    /**
     * 方法（受保护）
     * 名称:initManager
     * 描述:初始化数据管理器
     */
    @Override
    protected void initManager()
    {
        this.manager=new SystemApplicationFacade(this.connector);
        this.platManager=new SystemApplicationPlatformFacade(this.connector);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    afterCreate<br>
     * 描述:    处理创建数据成功后的处理工作（多用于多对多数据的创建）<br>
     *
     * @param request - HTTP请求对象
     * @param appId   - 创建后的应用标识
     * @throws ServletException - 抛出处理错误
     */
    @Override
    protected void afterCreate(HttpServletRequest request,String appId)
            throws ServletException
    {
        try
        {
            //获取指定的平台标识数组
            String[] appPlat=request.getParameterValues
                    (SystemApplicationServlet.PARAM_APPPLAT);

            //类型判断
            if(appPlat!=null)
            {
                //遍历删除主键集
                int successNum=0, errorNum=0;
                String[] platArr;
                boolean success;
                for(String plat : appPlat)
                {
                    if(this.paramNullCheck(plat)) continue;
                    //存在逗号分隔，则再次分解遍历
                    platArr=plat.split(",");
                    for(String aPlat : platArr)
                    {
                        success=this.platManager.create(appId,aPlat);
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
                            "成功创建对应平台数据"+successNum+"个,"
                                    +"失败"+(errorNum)+"个"
                    );
                }
            }
        }
        catch(ServletException ex)
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
        //获取管理角色标识
        String appId=request.getParameter(AbstractServlet.PARAM_PRIMARYKEY);

        //删除企业类型对应的插件
        boolean success=this.platManager.removeByAppId(appId);

        if(!success)
        {
            throw new ServletException(this.platManager.getError());
        }

        //重新创建企业类型对应的插件
        this.afterCreate(request,appId);
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
    (HttpServletRequest request,List<SystemApplication> list)
    {
        List<Object> result=null;
        if(list!=null)
        {
            result=new ArrayList<>();
            for(SystemApplication app : list)
            {
                SystemApplicationOutput appOut=new SystemApplicationOutput(app);
                appOut.setAppPlat(this.platManager.findByAppId(app.getAppId()));
                result.add(appOut);
            }
        }
        return result;
    }
}