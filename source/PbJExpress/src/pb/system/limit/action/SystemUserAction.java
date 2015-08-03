package pb.system.limit.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import pb.data.Connector;
import pb.system.limit.entity.SystemApplication;
import pb.system.limit.entity.SystemArea;
import pb.system.limit.entity.SystemPlugin;
import pb.system.limit.entity.SystemUser;
import pb.system.limit.manager.SystemApplicationFacade;
import pb.system.limit.manager.SystemApplicationFacadeLocal;
import pb.system.limit.manager.SystemAreaFacade;
import pb.system.limit.manager.SystemAreaFacadeLocal;
import pb.system.limit.manager.SystemAreaTypeFacade;
import pb.system.limit.manager.SystemAreaTypeFacadeLocal;
import pb.system.limit.manager.SystemPluginFacade;
import pb.system.limit.manager.SystemPluginFacadeLocal;
import pb.system.limit.manager.SystemRoleFacade;
import pb.system.limit.manager.SystemRoleFacadeLocal;
import pb.system.limit.manager.SystemUserFacade;
import pb.system.limit.manager.SystemUserFacadeLocal;
import pb.system.limit.manager.SystemUserRoleFacade;
import pb.system.limit.manager.SystemUserRoleFacadeLocal;
import pb.system.limit.module.LoginUser;
import pb.system.limit.module.SystemUserOutput;
import pb.system.limit.servlet.AbstractServlet;
import pb.system.limit.servlet.SystemUserServlet;

/**
 * 数据应用层类——记录系统管理用户的相关数据信息
 *
 * @author ProteanBear
 * @version 1.00 2014-07-15
 */
public class SystemUserAction extends AbstractAction<SystemUser> implements DataAction
{
    /**
     * 域(受保护)<br>
     * 名称:    userManager<br>
     * 描述:    管理人员管理器对象<br>
     */
    protected SystemUserFacadeLocal userManager;

    /**
     * 域(受保护)<br>
     * 名称:    typePluginManager<br>
     * 描述:    管理人员对应管理角色的数据管理器对象<br>
     */
    protected SystemUserRoleFacadeLocal userRoleManager;

    /**
     * 构造函数<br>
     *
     * @param connector - 数据库连接器对象
     */
    public SystemUserAction(Connector connector)
    {
        super(connector,SystemUser.class);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    initManager<br>
     * 描述:    初始化数据管理器<br>
     */
    @Override
    protected void initManager()
    {
        this.manager=new SystemUserFacade(this.connector);
        this.userManager=(SystemUserFacadeLocal)this.manager;
        this.userRoleManager=new SystemUserRoleFacade(this.connector);
    }

    /**
     * 方法（公共）<br>
     * 名称:    beforeCreate<br>
     * 描述:    处理创建数据前的处理工作（多用于无主键生成器的数据表）<br>
     *
     * @param request - HTTP请求对象
     * @param entity  - 构建的实体类
     * @return SystemUser - 处理后的实体类
     * @throws ServletException - 抛出处理错误
     */
    @Override
    public SystemUser beforeCreate
    (HttpServletRequest request,SystemUser entity)
            throws ServletException
    {
        //获取新密码设置
        String pass=request.getParameter(SystemUserServlet.PARAM_NEW_PASS);
        String passAgain=request.getParameter(SystemUserServlet.PARAM_PASS_AGAIN);
        //判断是否是指了密码
        if(this.paramNullCheck(pass) && this.paramNullCheck(passAgain))
        {
            throw new ServletException("未设置登录密码");
        }
        else
        {
            if(!pass.equals(passAgain))
            {
                throw new ServletException("两次密码输入不同");
            }
            else
            {
                entity.setUserPassword(pass);
            }
        }

        //获取并设置用户标识主键
        String userId=request.getParameter(AbstractServlet.PARAM_PRIMARYKEY);
        if(!this.paramNullCheck(userId)) entity.setUserPassword(userId);

        return entity;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    afterCreate<br>
     * 描述:    处理创建数据成功后的处理工作（多用于多对多数据的创建）<br>
     *
     * @param request - HTTP请求对象
     * @param userId  - 创建后的管理人员标识
     * @throws ServletException - 抛出处理错误
     */
    @Override
    protected void afterCreate(HttpServletRequest request,String userId)
            throws ServletException
    {
        try
        {
            //获取指定的用户角色标识数组
            String[] userRoles=request.getParameterValues
                    (SystemUserServlet.PARAM_ROLE);
            int id=(this.paramNullCheck(userId))?0:new Integer(userId);

            //类型判断
            if(userRoles!=null)
            {
                //遍历删除主键集
                int successNum=0, errorNum=0;
                String roleId, roles;
                String[] roleArr;
                boolean success;
                for(String userRole : userRoles)
                {
                    roles=userRole;
                    if(this.paramNullCheck(roles)) continue;
                    //存在逗号分隔，则再次分解遍历
                    roleArr=roles.split(",");
                    for(String role : roleArr)
                    {
                        roleId=role;
                        success=this.userRoleManager.create(id,roleId);
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
                            "成功创建对应用户角色数据"+successNum+"个,"
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
     * 名称:    beforeEdit<br>
     * 描述:    处理编辑数据前的处理工作（多用于多对多数据的创建）<br>
     *
     * @param request - HTTP请求对象
     * @param entity  - 构建的实体类
     * @return SystemUser - 处理后的实体类
     * @throws ServletException - 抛出处理错误
     */
    @Override
    protected SystemUser beforeEdit(HttpServletRequest request,SystemUser entity)
            throws ServletException
    {
        //获取新密码设置
        String pass=request.getParameter(SystemUserServlet.PARAM_NEW_PASS);
        String passAgain=request.getParameter(SystemUserServlet.PARAM_PASS_AGAIN);
        //判断是否重置了密码
        if(!this.paramNullCheck(pass) || !this.paramNullCheck(passAgain))
        {
            if(!pass.equals(passAgain))
            {
                throw new ServletException("两次密码输入不同");
            }
            else
            {
                entity.setUserPassword(pass);
            }
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
        //获取管理人员标识
        String userIdStr=request.getParameter(AbstractServlet.PARAM_PRIMARYKEY);
        int userId=(this.paramNullCheck(userIdStr))?0:new Integer(userIdStr);

        //删除用户角色对应的权限
        boolean success=this.userRoleManager.removeByUserId(userId);

        if(!success)
        {
            throw new ServletException(this.userRoleManager.getError());
        }

        //重新创建企业类型对应的插件
        this.afterCreate(request,userIdStr);
    }

    /**
     * 方法（公共）<br>
     * 名称:    login<br>
     * 描述:    处理用户登录相关处理。<br>
     * 创建session记录登录用户的基本信息、相关功能权限以及功能插件等相关数据信息。<br>
     *
     * @param request - HTTP请求对象
     * @return boolean - 登录成功则返回true
     * @throws ServletException - 抛出处理错误
     */
    public boolean login(HttpServletRequest request)
            throws ServletException
    {
        try
        {
            //判断数据管理器是否初始化
            if(this.managerNullCheck())
            {
                throw new ServletException("数据库管理器未初始化！");
            }

            //获取指定的登录用户名和登录密码
            String loginUser=request.getParameter(AbstractServlet.PARAM_LOGIN_USER);
            String loginPass=request.getParameter(AbstractServlet.PARAM_LOGIN_PASS);

            //判断用户登录
            SystemUser user=this.userManager.login(loginUser,loginPass);

            //处理结果
            boolean success=(user!=null);
            if(!success)
            {
                throw new ServletException(this.userManager.getError());
            }
            //创建成功则调用创建后处理工作
            else
            {
                //创建登录用户描述对象
                LoginUser userObj=new LoginUser(user);
                userObj=this.loadLoginUser(userObj);

                //储存用户数据信息到Session中
                request.getSession().setAttribute
                        (AbstractServlet.SESSION_LOGIN_USER,userObj);
            }
            return success;
        }
        catch(ServletException ex)
        {
            throw new ServletException(ex.toString());
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    isLogin<br>
     * 描述:    判断当前用户是否已经登录<br>
     *
     * @param request - HTTP请求对象
     * @return boolean - 如果用户已经登录则返回true
     * @throws ServletException - 抛出处理错误
     */
    public boolean isLogin(HttpServletRequest request)
            throws ServletException
    {
        return (this.getLoginUserInSession(request)!=null);
    }

    /**
     * 方法（公共）<br>
     * 名称:    loadLoginUser<br>
     * 描述:    载入登录用户的相关信息<br>
     *
     * @param user - 登录用户信息对象
     * @return LoginUser - 载入信息后的登录用户对象
     */
    public LoginUser loadLoginUser(LoginUser user)
    {
        //读取用户的角色管理范围
        SystemRoleFacadeLocal roleManager=new SystemRoleFacade(this.connector);
        if(!user.isSuperAdmin())
        {
            user.setRoleType(roleManager.findMaxRoleType(user.getCustId()));
        }
        else
        {
            user.setRoleType(0);
        }

        //读取用户的企业类型级别
        SystemAreaTypeFacadeLocal typeManager=new SystemAreaTypeFacade(this.connector);
        if(!user.isSuperAdmin())
        {
            user.setTypeLevel(typeManager.findMaxTypeLevel(user.getAreaId()));
        }
        else
        {
            user.setTypeLevel(0);
        }

        //读取用户相关的功能权限数据
        List<SystemPlugin> plugins=null;
        SystemPluginFacadeLocal pluginManager=new SystemPluginFacade(this.connector);
        //如果不是超级管理员登录，获取用户相关的功能权限
        if(!user.isSuperAdmin())
        {
            plugins=pluginManager.findByUserId(user.getCustId());
        }
        user.setLimits(plugins);

        //读取用户行政区域对应的功能插件数据

        //如果是超级管理员登录，获取所有的功能插件
        if(user.isSuperAdmin())
        {
            plugins=pluginManager.findAllPlugin();
        }
        //如果为非超级管理员登录，获取用户行政区域对应的功能插件
        else
        {
            plugins=pluginManager.findByAreaId(user.getAreaId());
        }
        user.setPlugins(plugins);

        //读取用户的行政区域数据
        SystemAreaFacadeLocal areaManager=new SystemAreaFacade(this.connector);
        //获取用户对应的行政区域列表
        List<SystemArea> areaList=areaManager.find(user.generateAreaCondition("areaId",user.getAreaId()));
        user.setAreaList(areaList);

        //读取用户管理的应用数据
        SystemApplicationFacadeLocal appManager=new SystemApplicationFacade(this.connector);
        Map<String,Object> condition=user.generateAreaCondition("areaId",user.getAreaId());
        List<SystemApplication> appList=appManager.find(condition);
        user.setAppList(appList);

        return user;
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
    (HttpServletRequest request,List<SystemUser> list)
    {
        List<Object> result=null;
        if(list!=null)
        {
            result=new ArrayList<>();
            for(SystemUser sysUser : list)
            {
                SystemUserOutput role=new SystemUserOutput(sysUser);
                role.setUserRoles(this.userRoleManager.findByUserId(sysUser.getCustId()));
                result.add(role);
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