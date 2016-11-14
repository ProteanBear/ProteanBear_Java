package pb.system.limit.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pb.data.Connector;
import pb.data.SourceConnector;
import pb.file.xml.XMLProductor;
import pb.pool.CachingServiceLocator;
import pb.system.limit.action.DataAction;
import pb.system.limit.action.SystemUserAction;
import pb.system.limit.entity.SystemPlugin;
import pb.system.limit.manager.SystemLogUserCacheFacadeLocal;
import pb.system.limit.module.LoginUser;
import pb.text.DateProcessor;
import pb.text.JsonProductor;

/**
 * 数据访问HTTP接口 - 抽象父类
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/11/04
 */
public abstract class AbstractServlet extends HttpServlet
{
    /*-----------------------------开始：静态内容------------------------------*/

    /**
     * 静态常量(公共)<br>
     * 名称:    ENV_DATASOURCE<br>
     * 描述:    记录环境条目名称——数据源<br>
     */
    public static final String ENV_DATASOURCE="DATA_SOURCE";

    /**
     * 静态常量(公共)<br>
     * 名称:    ENV_DATAUSER<br>
     * 描述:    记录环境条目名称——数据库登录用户名<br>
     * public static final String ENV_DATAUSER="DATA_USER";
     * <p>
     * /**静态常量(公共)<br>
     * 名称:    ENV_DATAPASS<br>
     * 描述:    记录环境条目名称——数据库登录用户密码<br>
     * public static final String ENV_DATAPASS="DATA_PASS";
     * <p>
     * /**静态常量(公共)<br>
     * 名称:    ENV_CONFIG_PAGEMAX<br>
     * 描述:    记录环境条目名称——配置信息，一页数据容量<br>
     */
    public static final String ENV_CONFIG_PAGEMAX="CONFIG_PAGEMAX";

    /**
     * 静态常量(公共)<br>
     * 名称:    ENV_CONFIG_DEBUG<br>
     * 描述:    记录环境条目名称——配置信息，是否显示调试信息<br>
     */
    public static final String ENV_CONFIG_DEBUG="CONFIG_DEBUG";

    /**
     * 静态常量(公共)<br>
     * 名称:    ENV_CONFIG_ISLOG<br>
     * 描述:    记录环境条目名称——配置信息，是否记录用户及终端日志<br>
     */
    public static final String ENV_CONFIG_ISLOG="CONFIG_ISLOG";

    /**
     * 静态常量(公共)<br>
     * 名称:    ENV_CONFIG_PAGE_WELCOME<br>
     * 描述:    记录环境条目名称——欢迎页面，配置初始欢迎页面，访问系统时自动转向设置页面（相对路径）<br>
     */
    public static final String ENV_CONFIG_PAGE_WELCOME="CONFIG_PAGE_WELCOME";

    /**
     * 静态常量(公共)<br>
     * 名称:    ENV_CONFIG_MAXRES<br>
     * 描述:    记录环境条目名称——记录在列表输出时附件最多的条目数<br>
     */
    public static final String ENV_CONFIG_MAXRES="CONFIG_MAX_RES";

    /**
     * 静态常量(公共)<br>
     * 名称:    ENV_CONFIG_AUTO_IMAGE<br>
     * 描述:    记录环境条目名称——是否自动生成缩略图、标题图和焦点图<br>
     */
    public static final String ENV_CONFIG_AUTO_IMAGE="CONFIG_AUTO_IMAGE";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_RETURN<br>
     * 描述:    记录请求参数名称——返回格式<br>
     */
    public static final String PARAM_RETURN="return";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_MODE<br>
     * 描述:    记录请求参数名称——操作模式<br>
     */
    public static final String PARAM_MODE="mode";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_PRIMARYKEY<br>
     * 描述:    记录请求参数名称——主键<br>
     */
    public static final String PARAM_PRIMARYKEY="primaryKey";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_SEARCH_KEY<br>
     * 描述:    记录请求参数名称——搜索条件<br>
     */
    public static final String PARAM_SEARCH_KEY="searchKey";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_SEARCH_VALUE<br>
     * 描述:    记录请求参数名称——搜索内容<br>
     */
    public static final String PARAM_SEARCH_VALUE="searchValue";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_SEARCH_SPLIT<br>
     * 描述:    记录请求参数名称——搜索条件分隔符第一级<br>
     */
    public static final String PARAM_SEARCH_SPLIT="\\|";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_SEARCH_SPLIT<br>
     * 描述:    记录请求参数名称——搜索条件分隔符第二级<br>
     */
    public static final String PARAM_SEARCH_SPLIT_SE=";";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_PAGE<br>
     * 描述:    记录请求参数名称——页码<br>
     */
    public static final String PARAM_PAGE="page";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_MAX<br>
     * 描述:    记录请求参数名称——一页数据数量<br>
     */
    public static final String PARAM_MAX="limit";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_MAX<br>
     * 描述:    记录请求参数名称——一页数据数量<br>
     */
    public static final String PARAM_ORDER="order";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_FROMAPP<br>
     * 描述:    记录请求参数名称——应用标识<br>
     */
    public static final String PARAM_FROMAPP="cert";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_LOGIN_USER<br>
     * 描述:    记录请求参数名称——管理人员登录名<br>
     */
    public static final String PARAM_LOGIN_USER="loginUser";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_LOGIN_PASS<br>
     * 描述:    记录请求参数名称——管理人员登录密码<br>
     */
    public static final String PARAM_LOGIN_PASS="loginPass";

    /**
     * 静态常量(公共)<br>
     * 名称:    SESSION_LOGIN_USER<br>
     * 描述:    记录session属性名称——管理人员登录后记录的缓存数据索引，记录在Session中<br>
     */
    public static final String SESSION_LOGIN_USER="UserInSession";

    /**
     * 静态常量(公共)<br>
     * 名称:    SESSION_LOGIN_VERYCODE<br>
     * 描述:    记录session属性名称——管理人员登录验证码<br>
     */
    public static final String SESSION_LOGIN_VERYCODE="verycode";

    /**
     * 静态常量(公共)<br>
     * 名称:    VALUE_RETURN_XML<br>
     * 描述:    记录请求参数值——返回格式xml<br>
     */
    public static final String VALUE_RETURN_XML="xml";

    /**
     * 静态常量(公共)<br>
     * 名称:    VALUE_RETURN_JSON<br>
     * 描述:    记录请求参数值——返回格式json<br>
     */
    public static final String VALUE_RETURN_JSON="json";

    /**
     * 静态常量(公共)<br>
     * 名称:    VALUE_RETURN_EXCEL<br>
     * 描述:    记录请求参数值——返回格式excel<br>
     */
    public static final String VALUE_RETURN_EXCEL="excel";

    /**
     * 静态常量(公共)<br>
     * 名称:    VALUE_MODE_LIST<br>
     * 描述:    记录请求参数值——操作模式：列表<br>
     */
    public static final String VALUE_MODE_LIST="list";

    /**
     * 静态常量(公共)<br>
     * 名称:    VALUE_MODE_CREATE<br>
     * 描述:    记录请求参数值——操作模式：创建<br>
     */
    public static final String VALUE_MODE_CREATE="create";

    /**
     * 静态常量(公共)<br>
     * 名称:    VALUE_MODE_EDIT<br>
     * 描述:    记录请求参数值——操作模式：编辑<br>
     */
    public static final String VALUE_MODE_EDIT="edit";

    /**
     * 静态常量(公共)<br>
     * 名称:    VALUE_MODE_REMOVE<br>
     * 描述:    记录请求参数值——操作模式：删除<br>
     */
    public static final String VALUE_MODE_REMOVE="remove";

    /**
     * 静态常量(公共)<br>
     * 名称:    PAGE_CODE<br>
     * 描述:    记录页面设置——编码<br>
     */
    public static final String PAGE_CODE="UTF-8";

    /**
     * 静态常量(公共)<br>
     * 名称:    LIMITTYPE_CREATE<br>
     * 描述:    记录权限类型标号——创建<br>
     */
    public static final int LIMITTYPE_CREATE=1;

    /**
     * 静态常量(公共)<br>
     * 名称:    LIMITTYPE_REMOVE<br>
     * 描述:    记录权限类型标号——删除<br>
     */
    public static final int LIMITTYPE_REMOVE=2;

    /**
     * 静态常量(公共)<br>
     * 名称:    LIMITTYPE_EDIT<br>
     * 描述:    记录权限类型标号——创建<br>
     */
    public static final int LIMITTYPE_EDIT=3;

    /**
     * 静态常量(公共)<br>
     * 名称:    LIMITTYPE_FIND<br>
     * 描述:    记录权限类型标号——查询<br>
     */
    public static final int LIMITTYPE_FIND=0;

    /**
     * 静态方法（公共）<br>
     * 名称:    getAttributeInSession<br>
     * 描述:    从Session中读取指定属性名的值<br>
     *
     * @param request - HTTP请求对象
     * @param name    - 属性名
     * @return Object - 属性值
     * @throws ServletException - 抛出处理错误
     */
    public static Object getAttributeInSession
    (HttpServletRequest request,String name)
            throws ServletException
    {
        HttpSession session=null;
        Object result=null;

        try
        {
            session=request.getSession(false);
            if(session!=null)
            {
                result=session.getAttribute(name);
            }
        }
        catch(Exception e)
        {
            throw new ServletException(e.toString());
        }
        return result;
    }
     
    /*-----------------------------结束：静态内容------------------------------*/

    /**
     * 域(受保护)<br>
     * 名称:    connector<br>
     * 描述:    记录当前使用的数据库连接器<br>
     */
    protected Connector connector;

    /**
     * 域(受保护)<br>
     * 名称:    dataAction<br>
     * 描述:    使用DataAction接口对数据进行相关处理<br>
     */
    protected DataAction dataAction;

    /**
     * 域(受保护)<br>
     * 名称:    userAction<br>
     * 描述:    用户相关数据处理，处理用户登录<br>
     */
    protected SystemUserAction userAction;

    /**
     * 域(受保护)<br>
     * 名称:    isXml<br>
     * 描述:    记录当前输出的文本格式<br>
     * 如果为Xml，则为true<br>
     */
    protected boolean isXml;

    /**
     * 域(受保护)<br>
     * 名称:    isExcel<br>
     * 描述:    记录当前输出的文本格式<br>
     * 如果为isExcel，则为true<br>
     */
    protected boolean isExcel;

    /**
     * 域(受保护)<br>
     * 名称:    isLog<br>
     * 描述:    记录是否记录用户及终端日志<br>
     */
    protected boolean isLog;

    /**
     * 域(受保护)<br>
     * 名称:    userLogManager<br>
     * 描述:    用户日志管理器<br>
     */
    protected SystemLogUserCacheFacadeLocal userLogManager;

    /**
     * 域(受保护)<br>
     * 名称:    dp<br>
     * 描述:    时间字符管理器对象<br>
     */
    protected DateProcessor dp;

    /**
     * 方法（受保护）<br>
     * 名称:    processRequest<br>
     * 描述:    处理数据库连接、关闭等操作，并调用processRequest方法处理相关的HTTP请求<br>
     *
     * @param request  - HTTP请求对象
     * @param response - HTTP输出对象
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void process(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException
    {
        //设置页面输入输出编码
        request.setCharacterEncoding(PAGE_CODE);
        response.setCharacterEncoding(PAGE_CODE);

        PrintWriter out=response.getWriter();
        try
        {
            //处理请求参数，设置输出格式（默认为Json格式）
            String re=request.getParameter(PARAM_RETURN);
            re=(re==null)?VALUE_RETURN_JSON:re;
            this.isXml=(VALUE_RETURN_XML.equals(re.trim().toLowerCase()));
            this.isExcel=(VALUE_RETURN_EXCEL.equals(re.trim().toLowerCase()));
            if(this.isExcel)
            {
                response.setContentType("application/vnd.ms-excel;charset="+PAGE_CODE);
                response.setHeader("Content-disposition","attachment; filename="+this.getExcelFileName()+".xls");
            }
            else
            {
                response.setContentType("text/"+((isXml)?"xml":"json")+";charset="+PAGE_CODE);
            }

            //数据库连接器为空
            if(this.connector==null)
            {
                this.errorHandle(out,"数据库连接器未初始化或初始化失败！");
                return;
            }

            //获取数据库连接
            this.connector.connect();
            //连接成功
            if(this.connector.isConnected())
            {
                processRequest(request,response);
            }
            //连接失败
            else
            {
                this.errorHandle(out,"数据库连接失败！");
            }
        }
        catch(Exception ex)
        {
            this.errorHandle(out,ex.getMessage().replace("javax.servlet.ServletException:",""));
        }
        finally
        {
            if(this.connector!=null) this.connector.close();
            out.close();
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    processRequest<br>
     * 描述:    处理来自HTTP访问的相关请求，调用DataAction接口进行数据的增删改查处理<br>
     *
     * @param request  - HTTP请求对象
     * @param response - HTTP输出对象
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest
    (HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException
    {
        PrintWriter out=response.getWriter();
        LoginUser user=null;
        boolean canCreate=false;
        boolean canRemove=false;
        boolean canEdit=false;
        boolean canFind=false;
        String mode=null;

        //检查用户是否已经登录
        //boolean isLogin=this.userAction.isLogin(request);
        //如果用户未登录，执行登录处理
        //if(!isLogin)       isLogin=this.userAction.login(request);
        //如果用户已经登录，获取登录用户属性
        user=this.userAction.getLoginUserInSession(request);

        //支持外部查询获取
        if(this.dataAction.supportNoLoginFind() && user==null)
        {
            String fromApp=request.getParameter(PARAM_FROMAPP);
            if(this.paramNullCheck(fromApp))
            {
                throw new ServletException("未指定来源应用的授权码！");
            }
            mode=this.dataAction.supportNoLoginMode(request);
            canCreate=VALUE_MODE_CREATE.equals(mode);
            canRemove=VALUE_MODE_REMOVE.equals(mode);
            canEdit=VALUE_MODE_EDIT.equals(mode);
            canFind=VALUE_MODE_LIST.equals(mode);
        }
        else
        {
            if(user==null)
            {
                throw new ServletException("未登录无法使用此接口！");
            }
            //获取用户对如下模式的操作权限
            canCreate=user.isHaveLimit(this.getLimitPluginId(),LIMITTYPE_CREATE);
            canRemove=user.isHaveLimit(this.getLimitPluginId(),LIMITTYPE_REMOVE);
            canEdit=user.isHaveLimit(this.getLimitPluginId(),LIMITTYPE_EDIT);
            canFind=user.isHaveLimit(this.getLimitPluginId(),LIMITTYPE_FIND);

            //获取当前的操作模式(默认为搜索模式)
            mode=request.getParameter(PARAM_MODE);
            mode=(mode==null)?VALUE_MODE_LIST:mode;
            mode=mode.trim().toLowerCase();
        }

        //创建模式
        if(VALUE_MODE_CREATE.equals(mode))
        {
            if(canCreate)
            {
                final Object newId=this.dataAction.create(request);
                String str=this.stringForReturn(mode);
                this.generateResult(out,str!=null?str:"成功插入新的数据信息",new HashMap<String,Object>(){{
                    put("newId",newId);
                }});
                this.recordUserLog(user,mode);
            }
            else
            {
                this.errorHandle(out,"用户没有创建此数据的权限");
            }
        }
        //删除模式
        else if(VALUE_MODE_REMOVE.equals(mode))
        {
            if(canRemove)
            {
                this.dataAction.remove(request);
                String str=this.stringForReturn(mode);
                this.generateResult(out,str!=null?str:"成功删除指定的数据及其相关信息");
                this.recordUserLog(user,mode);
            }
            else
            {
                this.errorHandle(out,"用户没有删除此数据的权限");
            }
        }
        //编辑模式
        else if(VALUE_MODE_EDIT.equals(mode))
        {
            if(canEdit)
            {
                this.dataAction.edit(request);
                String str=this.stringForReturn(mode);
                this.generateResult(out,str!=null?str:"成功修改指定的数据信息");
                this.recordUserLog(user,mode);
            }
            else
            {
                this.errorHandle(out,"用户没有修改此数据的权限");
            }
        }
        //查询模式
        else if(VALUE_MODE_LIST.equals(mode))
        {
            if(canFind)
            {
                out.print(
                        (this.isExcel)?this.dataAction.findToExcel(request)
                                :((this.isXml)?this.dataAction.findToXml(request)
                                :this.dataAction.findToJson(request))
                );
                this.recordUserLog(user,mode);
            }
            else
            {
                this.errorHandle(out,"用户没有查询此数据的权限");
            }
        }
        //错误模式
        else
        {
            throw new ServletException("错误的操作模式，请检查请求参数 - "+PARAM_MODE+"！");
        }
    }

    /**
     * 方法（私有）<br>
     * 名称:    recordUserLog<br>
     * 描述:    记录用户操作日志信息<br>
     *
     * @param user - 登录用户对象
     * @param mode - 当前的操作模式
     */
    protected void recordUserLog(LoginUser user,String mode)
    {
        //如果需记录用户操作日志信息
        if(this.userLogManager!=null&&user!=null)
        {
            //判断操作模式
            int operateType=(VALUE_MODE_CREATE.equals(mode))?1:
                    ((VALUE_MODE_REMOVE.equals(mode))?2:
                            ((VALUE_MODE_EDIT.equals(mode))?3:
                                    (VALUE_MODE_LIST.equals(mode))?0:-1));
            String operateTypeDes=(VALUE_MODE_CREATE.equals(mode))?"添加":
                    ((VALUE_MODE_REMOVE.equals(mode))?"删除":
                            ((VALUE_MODE_EDIT.equals(mode))?"编辑":
                                    (VALUE_MODE_LIST.equals(mode))?"查询":""));

            //获取当前的操作插件
            SystemPlugin plugin=user.getPluginByPluginId(this.getLimitPluginId());

            if(operateType!=-1 && plugin!=null)
            {
                 /*SystemLogUserCache log=new SystemLogUserCache();
                 log.setUserId(user.getUserId());
                 log.setUserName(user.getUserName());
                 log.setAreaId(user.getAreaId());
                 log.setAreaName(user.getAreaName());
                 log.setPluginCustId(plugin.getCustId());
                 log.setPluginId(plugin.getPluginId());
                 log.setPluginName(plugin.getPluginName());
                 log.setPluginModule(plugin.getPluginModule());
                 log.setOperateType(operateType);
                 log.setOperateTime(this.dp.getCurrent());
                 log.setLogDescription("用户（"+user.getUserName()+"）"
                                       + "在"+log.getOperateTime()+"时"
                                       + "执行"+log.getPluginName()+"功能"
                                       + "的"+operateTypeDes+"操作");
                 
                 this.userLogManager.create(log);*/
            }
        }
    }

    /**
     * 方法（受保护、抽象）<br>
     * 名称:    getLimitPluginId<br>
     * 描述:    获取当前功能对应的功能插件标识<br>
     *
     * @return int - 创建模式的权限编码
     */
    protected abstract String getLimitPluginId();

    /**
     * 方法（受保护）<br>
     * 名称:    getExcelFileName<br>
     * 描述:    获取Excel输出的文件名称<br>
     *
     * @return String - 文件名称
     */
    protected String getExcelFileName()
    {
        return "Excel导出";
    }

    /**
     * 方法（受保护）<br>
     * 名称:    initConnector<br>
     * 描述:    初始化数据库连接器<br>
     *
     * @throws javax.naming.NamingException
     */
    protected void initConnector() throws NamingException
    {
        //获取数据资源相关设置
        String dataSource=CachingServiceLocator.getInstance().getString(ENV_DATASOURCE);
        //String username=CachingServiceLocator.getInstance().getString(ENV_DATAUSER);
        //String password=CachingServiceLocator.getInstance().getString(ENV_DATAPASS);

        //初始化数据库连接器
        this.connector=new SourceConnector(dataSource/*,username,password*/);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    initDataAction<br>
     * 描述:    初始化数据应用层接口<br>
     */
    protected abstract void initDataAction();

    /**
     * 方法（受保护）<br>
     * 名称:    init<br>
     * 描述:    Servlet初始化时调用此方法，初始化连接器以及数据层接口的实现方法<br>
     *
     * @throws javax.servlet.ServletException
     */
    @Override
    public void init() throws ServletException
    {
        try
        {
            super.init();

            //初始化连接器
            this.initConnector();

            //获取是否记录用户及终端日志
            //this.isLog=CachingServiceLocator.getInstance().getBoolean(ENV_CONFIG_ISLOG);

            //初始化用户数据处理器
            this.userAction=new SystemUserAction(this.connector);

            //初始化日期处理对象
            this.dp=new DateProcessor("yyyy-MM-dd HH:mm:ss");
             
             /*if(this.isLog)
             {
                 //初始化用户日志数据管理器
                 this.userLogManager=new SystemLogUserCacheFacade(this.connector);
             }*/

            //初始化数据应用层接口
            this.initDataAction();
        }
        catch(NamingException ex)
        {
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    errorHandle<br>
     * 描述:    封装产生错误时的处理方法<br>
     *
     * @param out
     * @param error - 错误信息
     * @throws java.io.IOException
     */
    protected void errorHandle(PrintWriter out,String error) throws IOException
    {
        System.out.println(error);
        out.print(
                (this.isXml)?XMLProductor.toInforXml(false,error):
                        JsonProductor.createInformation(false,error)
        );
    }

    /**
     * 方法（受保护）<br>
     * 名称:    debugHandle<br>
     * 描述:    对调试信息进行处理，记录并输出<br>
     *
     * @param out
     * @param infor - 调试信息
     * @throws javax.naming.NamingException
     */
    protected void debugHandle(PrintWriter out,String infor) throws NamingException
    {
        boolean isDebug=CachingServiceLocator.getInstance()
                .getBoolean(ENV_CONFIG_DEBUG);

        if(isDebug)
        {
            //输出到数据接口页面
            out.println(infor);
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    generateResult<br>
     * 描述:    根据当前要求的输出格式，生成相应的增删改的结果<br>
     *
     * @param out
     * @param infor - 成功的相关信息
     * @param valueMap - 输出的其他值
     * @throws java.io.IOException
     */
    protected void generateResult(PrintWriter out,String infor,Map<String,Object> valueMap) throws IOException
    {
        out.print(
                (this.isXml)?XMLProductor.toInforXml(true,infor,valueMap):
                        JsonProductor.createInformation(true,infor,valueMap)
        );
    }

    /**
     * 方法（受保护）<br>
     * 名称:    generateResult<br>
     * 描述:    根据当前要求的输出格式，生成相应的增删改的结果<br>
     *
     * @param out
     * @param infor - 成功的相关信息
     * @throws java.io.IOException
     */
    protected void generateResult(PrintWriter out,String infor) throws IOException
    {
        out.print(
                (this.isXml)?XMLProductor.toInforXml(true,infor):
                        JsonProductor.createInformation(true,infor)
        );
    }

    /**
     * 方法（受保护）<br>
     * 名称:    paramNullCheck<br>
     * 描述:    检查参数是否为空<br>
     *
     * @param param - 参数值
     * @return boolean - 如果参数值为null或""，返回true
     */
    protected boolean paramNullCheck(String param)
    {
        return (param==null || "".equals(param.trim()) || "null".equals(param.trim()));
    }

    /**
     * 方法（受保护）<br>
     * 名称:    stringForReturn<br>
     * 描述:    用于自定义增删改操作是返回的内容<br>
     *
     * @param mode - 处理模式
     * @return boolean - 如果参数值为null或""，返回true
     */
    protected String stringForReturn(String mode)
    {
        return null;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    processRequest<br>
     * 描述:    处理使用get模式访问的HTTP请求
     *
     * @param request  - HTTP请求对象
     * @param response - HTTP输出对象
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet
    (HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException
    {
        process(request,response);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    processRequest<br>
     * 描述:    处理使用POST模式访问的HTTP请求
     *
     * @param request  - HTTP请求对象
     * @param response - HTTP输出对象
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost
    (HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException
    {
        process(request,response);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getServletInfo<br>
     * 描述:    返回Servlet的一个简短描述<br>
     *
     * @return String - Servlet的简短描述
     */
    @Override
    public String getServletInfo()
    {
        return "";
    }
}
