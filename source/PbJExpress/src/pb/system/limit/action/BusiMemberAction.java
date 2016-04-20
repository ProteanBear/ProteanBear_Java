package pb.system.limit.action;

import pb.data.Connector;
import pb.data.EntityTransformer;
import pb.system.limit.entity.BusiMember;
import pb.system.limit.manager.BusiMemberFacade;
import pb.system.limit.manager.BusiMemberFacadeLocal;
import pb.system.limit.servlet.AbstractServlet;
import pb.system.limit.servlet.BusiMemberServlet;
import pb.text.DateProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

/**
 * 数据应用层类——记录系统平台的会员信息
 *@author ProteanBear
 *@version 1.00 2016-04-11
 */
public class BusiMemberAction extends AbstractAction<BusiMember> implements DataAction
{
    /**构造函数
     * @param connector - 数据库连接器对象
     */
    public BusiMemberAction(Connector connector)
    {
        super(connector,BusiMember.class);
    }

    /**方法（受保护）
     * 名称:initManager
     * 描述:初始化数据管理器
     */
    @Override protected void initManager()
    {
        this.manager=new BusiMemberFacade(this.connector);
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
    public BusiMember beforeCreate(HttpServletRequest request,BusiMember entity)
            throws ServletException
    {
        //获取新密码设置
        String pass=request.getParameter(BusiMemberServlet.PARAM_NEW_PASS);
        String passAgain=request.getParameter(BusiMemberServlet.PARAM_PASS_AGAIN);
        //判断是否设置了密码
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
                entity.setMemberPassword(pass);
            }
        }

        //设置属性
        entity.setCreateTime((new DateProcessor("yyyy-MM-dd HH:mm:ss")).getCurrent());
        entity.setUpdateTime(entity.getCreateTime());

        return entity;
    }

    /**
     * 方法（公共）
     * 名称：   create
     * 描述:    创建数据<br>
     *
     * @param request - HTTP请求对象
     * @return 返回新建数据的主键值
     * @throws javax.servlet.ServletException - 抛出处理错误
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

            //创建数据实体对象
            BusiMember entity=this.entityClass.newInstance();

            //根据请求参数设置相应对象
            entity=(BusiMember)EntityTransformer.updateEntityByHttpRequest(
                    entityClass,
                    entity,
                    request,
                    this.manager.getPrimaryKeyName(),
                    !this.manager.isUseGenerator()
            );

            //是否使用事务机制
            boolean useTransaction=this.isUseTransaction();
            if(useTransaction)
            {
                this.manager.transactionBegin();
            }

            //调用创建前的处理
            entity=this.beforeCreate(request,entity);

            //插入数据
            boolean success=this.manager.create(entity);

            //处理结果
            if(!success)
            {
                throw new ServletException(this.manager.getError());
            }
            //创建成功则调用创建后处理工作
            else
            {
                this.afterCreate(request,this.manager.getLastGenerator());
                if(useTransaction)
                {
                    this.manager.transactionCommit();
                }
            }

            entity.setCustId(this.manager.getLastGenerator());
            return entity;
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |InstantiationException
                |NoSuchMethodException
                |InvocationTargetException
                |ServletException ex)
        {
            if(this.isUseTransaction())
            {
                this.manager.transactionRollBack();
            }
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
    protected BusiMember beforeEdit(HttpServletRequest request,BusiMember entity)
            throws ServletException
    {
        //获取新密码设置
        String pass=request.getParameter(BusiMemberServlet.PARAM_NEW_PASS);
        String passAgain=request.getParameter(BusiMemberServlet.PARAM_PASS_AGAIN);
        //判断是否重置了密码
        if(!this.paramNullCheck(pass) || !this.paramNullCheck(passAgain))
        {
            if(!pass.equals(passAgain))
            {
                throw new ServletException("两次密码输入不同");
            }
            else
            {
                entity.setMemberPassword(pass);
            }
        }

        //设置属性
        entity.setUpdateTime((new DateProcessor("yyyy-MM-dd HH:mm:ss")).getCurrent());
        return entity;
    }

    /**
     * 方法（公共）
     * 名称：   edit
     * 描述:    编辑数据<br>
     *
     * @param request - HTTP请求对象
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    @Override
    public void edit(HttpServletRequest request)
            throws ServletException
    {
        try
        {
            //判断数据管理器是否初始化
            if(this.managerNullCheck())
            {
                throw new ServletException("数据库管理器未初始化！");
            }

            //获取指定的主键集
            String primaryKey=request.getParameter(AbstractServlet.PARAM_PRIMARYKEY);
            BusiMember member=this.getMemberUserInSession(request);
            primaryKey=member.getCustId()+"";

            //判断主键集是否为空
            if(this.paramNullCheck(primaryKey))
            {
                throw new ServletException("未指定编辑的数据主键！");
            }

            //创建数据实体对象
            BusiMember entity=this.manager.find(primaryKey);
            if(entity==null)
            {
                throw new ServletException("未找到指定的数据，主键错误！");
            }

            //根据请求参数设置相应对象
            entity=(BusiMember)EntityTransformer.updateEntityByHttpRequest(
                    entityClass,
                    entity,
                    request,
                    this.manager.getPrimaryKeyName(),
                    false,this.manager.getCannotEditParams()
            );

            //是否使用事务机制
            boolean useTransaction=this.isUseTransaction();
            if(useTransaction)
            {
                this.manager.transactionBegin();
            }

            //调用创建前的处理
            entity=this.beforeEdit(request,entity);

            //插入数据
            boolean success=this.manager.edit(entity);

            //处理结果
            if(!success)
            {
                if(useTransaction)
                {
                    this.manager.transactionRollBack();
                }
                throw new ServletException(this.manager.getError());
            }
            //调用编辑后处理方法
            else
            {
                this.afterEdit(request);
                if(useTransaction)
                {
                    this.manager.transactionCommit();
                }
            }
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |InstantiationException
                |NoSuchMethodException
                |InvocationTargetException
                |ServletException ex)
        {
            if(this.isUseTransaction())
            {
                this.manager.transactionRollBack();
            }
            throw new ServletException(ex.toString());
        }
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
            String loginUser=request.getParameter(BusiMemberServlet.PARAM_MEMBER);
            String loginPass=request.getParameter(BusiMemberServlet.PARAM_MEMBER_PASS);
            String loginTypeStr=request.getParameter(BusiMemberServlet.PARAM_MEMBER_TYPE);
            if(this.paramNullCheck(loginTypeStr))
            {
                throw new ServletException("缺少必须的参数！");
            }
            int loginType=Integer.parseInt(loginTypeStr);

            //判断用户登录
            BusiMember member=((BusiMemberFacadeLocal)this.manager).login(loginUser,loginPass,loginType);

            //处理结果
            boolean success=(member!=null);
            if(!success)
            {
                throw new ServletException(this.manager.getError());
            }
            //创建成功则调用创建后处理工作
            else
            {
                //储存用户数据信息到Session中
                request.getSession().setAttribute
                        (BusiMemberServlet.SESSION_MEMBER_USER,member);
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
        return (this.getMemberUserInSession(request)!=null);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getLoginUserInSession<br>
     * 描述:    从Session中获取指定的登录用户对象<br>
     *
     * @param request - HTTP请求对象
     * @return LoginUser - 登录用户相关属性
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    public BusiMember getMemberUserInSession(HttpServletRequest request)
            throws ServletException
    {
        return (BusiMember)AbstractServlet.getAttributeInSession
                (request,BusiMemberServlet.SESSION_MEMBER_USER);
    }
}