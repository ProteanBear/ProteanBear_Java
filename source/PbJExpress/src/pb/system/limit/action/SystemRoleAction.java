package pb.system.limit.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import pb.data.Connector;
import pb.system.limit.entity.SystemRole;
import pb.system.limit.manager.SystemRoleFacade;
import pb.system.limit.manager.SystemRolePluginFacade;
import pb.system.limit.manager.SystemRolePluginFacadeLocal;
import pb.system.limit.module.LoginUser;
import pb.system.limit.module.SystemRoleOutput;
import pb.system.limit.servlet.AbstractServlet;
import pb.system.limit.servlet.SystemRoleServlet;

/**
 * 数据应用层类——记录系统用户角色的相关数据信息
 *
 * @author ProteanBear
 * @version 1.00 2014-07-15
 */
public class SystemRoleAction extends AbstractAction<SystemRole> implements DataAction
{
    /**
     * 域(受保护)<br>
     * 名称:    typePluginManager<br>
     * 描述:    企业类型对应插件的数据管理器对象<br>
     */
    protected SystemRolePluginFacadeLocal rolePluginManager;

    /**
     * 构造函数<br>
     *
     * @param connector - 数据库连接器对象
     */
    public SystemRoleAction(Connector connector)
    {
        super(connector,SystemRole.class);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    initManager<br>
     * 描述:    初始化数据管理器<br>
     */
    @Override
    protected void initManager()
    {
        this.manager=new SystemRoleFacade(this.connector);
        this.rolePluginManager=new SystemRolePluginFacade(this.connector);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    afterCreate<br>
     * 描述:    处理创建数据成功后的处理工作（多用于多对多数据的创建）<br>
     *
     * @param request - HTTP请求对象
     * @param roleId  - 创建后的管理角色标识
     * @throws ServletException - 抛出处理错误
     */
    @Override
    protected void afterCreate(HttpServletRequest request,String roleId)
            throws ServletException
    {
        try
        {
            //获取指定的企业类型标识数组
            String[] roleLimits=request.getParameterValues
                    (SystemRoleServlet.PARAM_LIMIT);

            //类型判断
            if(roleLimits!=null)
            {
                //遍历删除主键集
                int successNum=0, errorNum=0;
                String limitId, limits;
                String[] limitArr;
                boolean success;
                for(String roleLimit : roleLimits)
                {
                    limits=roleLimit;
                    if(this.paramNullCheck(limits)) continue;
                    //存在逗号分隔，则再次分解遍历
                    limitArr=limits.split(",");
                    for(String limit : limitArr)
                    {
                        limitId=limit;
                        success=this.rolePluginManager.create(roleId,limitId);
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
                            "成功创建对应功能权限数据"+successNum+"个,"
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
        String roleId=request.getParameter(AbstractServlet.PARAM_PRIMARYKEY);

        //删除企业类型对应的插件
        boolean success=this.rolePluginManager.removeByRoleId(roleId);

        if(!success)
        {
            throw new ServletException(this.rolePluginManager.getError());
        }

        //重新创建企业类型对应的插件
        this.afterCreate(request,roleId);
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

        //加入管理范围限制，获取到的用户角色级别必须小于当前用户角色级别
        LoginUser user=this.getLoginUserInSession(request);
        condition.put("roleType>=?",user.getRoleType());

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
    (HttpServletRequest request,List<SystemRole> list)
    {
        List<Object> result=null;
        if(list!=null)
        {
            result=new ArrayList<>();
            for(SystemRole sysRole : list)
            {
                SystemRoleOutput role=new SystemRoleOutput(sysRole);
                role.setRoleLimits(this.rolePluginManager.findByRoleId(sysRole.getRoleId()));
                result.add(role);
            }
        }
        return result;
    }

}