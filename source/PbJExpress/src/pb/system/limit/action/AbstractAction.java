package pb.system.limit.action;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import pb.data.AbstractFacadeLocal;
import pb.data.Connector;
import pb.data.EntityTransformer;
import pb.file.xml.XMLProductor;
import pb.pool.CachingServiceLocator;
import pb.system.limit.entity.BusiSection;
import pb.system.limit.manager.BusiSectionFacade;
import pb.system.limit.manager.BusiSectionFacadeLocal;
import pb.system.limit.module.LoginUser;
import pb.system.limit.servlet.AbstractServlet;
import pb.text.JsonProductor;

/**
 * 数据应用层 - 抽象父类。<br>
 * 实现DataAction接口。<br>
 * 1.07 - 增加supportNoLoginFind方法，增加外部开放查询设置<br>
 * 1.06 - 增加getAreaLimitField方法，用于重载后对相应的业务进行权限范围限制。<br>
 * 1.05 - 增加generateOutput方法，用于生成相应的输出列表。<br>
 * 1.04 - 增加beforeCreate、beforeEdit方法。<br>
 * 1.03 - 将生成查询条件的方法从findTo中单独提出，便于子类重载时调用。<br>
 * 1.02 - 修正未调用afterEdit的错误。<br>
 * 1.01 - 增加创建后、编辑后以及删除后的数据处理方法，多用于多对多的数据处理。<br>
 *
 * @param <T>
 * @author proteanBear(马强)
 * @version 1.07 2014/10/11
 */
public abstract class AbstractAction<T> implements DataAction
{
    /**
     * 域(受保护)<br>
     * 名称:    manager<br>
     * 描述:    记录当前使用的数据管理器<br>
     */
    protected AbstractFacadeLocal<T> manager;

    /**
     * 域(受保护)<br>
     * 名称:    connector<br>
     * 描述:    记录当前使用的数据库连接器<br>
     */
    protected Connector connector;

    /**
     * 域(受保护)<br>
     * 名称:    entityClass<br>
     * 描述:    当前实体类<br>
     */
    protected Class<T> entityClass;

    /**
     * 域(受保护)<br>
     * 名称:    currentPage<br>
     * 描述:    当前查询页码<br>
     */
    protected int currentPage;

    /**
     * 构造函数<br>
     *
     * @param connector   - 数据库连接器对象
     * @param entityClass
     */
    public AbstractAction(Connector connector,Class<T> entityClass)
    {
        this.connector=connector;
        this.entityClass=entityClass;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    initManager<br>
     * 描述:    初始化数据管理器<br>
     */
    protected abstract void initManager();

    /**
     * 方法（公共）<br>
     * 名称:    getLoginUserInSession<br>
     * 描述:    从Session中获取指定的登录用户对象<br>
     *
     * @param request - HTTP请求对象
     * @return LoginUser - 登录用户相关属性
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    public LoginUser getLoginUserInSession(HttpServletRequest request)
            throws ServletException
    {
        return (LoginUser)AbstractServlet.getAttributeInSession
                (request,AbstractServlet.SESSION_LOGIN_USER);
    }

    /**
     * 方法（公共）<br>
     * 名称:    beforeCreate<br>
     * 描述:    处理创建数据前的处理工作（多用于无主键生成器的数据表）<br>
     *
     * @param request - HTTP请求对象
     * @param entity  - 构建的实体类
     * @return T - 处理后的实体类
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    public T beforeCreate(HttpServletRequest request,T entity)
            throws ServletException
    {
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
            T entity=this.entityClass.newInstance();

            //根据请求参数设置相应对象
            entity=(T)EntityTransformer.updateEntityByHttpRequest(
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

            return this.manager.getLastGenerator();
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
     * 名称:    afterCreate<br>
     * 描述:    处理创建数据成功后的处理工作（多用于多对多数据的创建）<
     *
     * @param request    - HTTP请求对象
     * @param primaryKey - 创建记录后生成的主键（只适用于自定义的主键生成器）
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    protected void afterCreate(HttpServletRequest request,String primaryKey)
            throws ServletException
    {
    }

    /**
     * 方法（公共）
     * 名称：   remove
     * 描述:    删除数据<br>
     *
     * @param request - HTTP请求对象
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    @Override
    public void remove(HttpServletRequest request)
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
            String[] primaryKeys=request.getParameterValues(AbstractServlet.PARAM_PRIMARYKEY);

            //判断主键集是否为空
            if(primaryKeys==null || primaryKeys.length==0)
            {
                throw new ServletException("未指定删除的数据主键！");
            }

            //是否使用事务机制
            boolean useTransaction=this.isUseTransaction();
            if(useTransaction)
            {
                this.manager.transactionBegin();
            }

            //遍历删除主键集
            int num=0;
            String key;
            boolean success;
            for(String primaryKey : primaryKeys)
            {
                key=primaryKey;
                if(this.paramNullCheck(key)) continue;
                success=this.manager.removeById(key);
                if(success) num++;
            }

            //处理结果
            if(num!=primaryKeys.length)
            {
                if(useTransaction)
                {
                    this.manager.transactionRollBack();
                }
                throw new ServletException(
                        "计划删除数据"+primaryKeys.length+"个,"
                                +"失败"+(primaryKeys.length-num)+"个,"
                                +"失败原因："+this.manager.getError()
                );
            }
            else
            {
                if(useTransaction)
                {
                    this.manager.transactionCommit();
                }
            }
        }
        catch(ServletException ex)
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
     * @return T - 处理后的实体类
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    protected T beforeEdit(HttpServletRequest request,T entity)
            throws ServletException
    {
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

            //判断主键集是否为空
            if(this.paramNullCheck(primaryKey))
            {
                throw new ServletException("未指定编辑的数据主键！");
            }

            //创建数据实体对象
            T entity=this.manager.find(primaryKey);
            if(entity==null)
            {
                throw new ServletException("未找到指定的数据，主键错误！");
            }

            //根据请求参数设置相应对象
            entity=(T)EntityTransformer.updateEntityByHttpRequest(
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
     * 方法（公共）
     * 名称：   lastKeyGenerator
     * 描述:    获取最后创建的数据主键（适用于生成器方式）<br>
     *
     * @return String - 最后创建的数据主键
     */
    @Override
    public String lastKeyGenerator()
    {
        return this.manager.getLastGenerator()!=null?this.manager.getLastGenerator().replaceAll("\\\'",""):this.manager
                .getLastGenerator();
    }

    /**
     * 方法（受保护）<br>
     * 名称:    afterEdit<br>
     * 描述:    处理编辑数据成功后的处理工作（多用于多对多数据的修改）<br>
     *
     * @param request - HTTP请求对象
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    protected void afterEdit(HttpServletRequest request)
            throws ServletException
    {
    }

    /**
     * 方法（公共）
     * 名称：   findToXml
     * 描述:    查询数据，返回Xml结果<br>
     *
     * @param request - HTTP请求对象
     * @return String - 获取成功返回XML格式的列表结果
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    @Override
    public String findToXml(HttpServletRequest request)
            throws ServletException
    {
        //查询数据
        List list=this.findTo(request);
        list=this.generateOutput(request,list);

        int pageSize=this.manager.getPageSize();
        int page=this.currentPage;
        int total=this.manager.getTotalCount();
        int totalPage=this.manager.getTotalPage();

        return XMLProductor.toXmlString(list,pageSize,page,total,totalPage);
    }

    /**
     * 方法（公共）
     * 名称：   findToJson
     * 描述:    查询数据，返回Json结果<br>
     *
     * @param request - HTTP请求对象
     * @return String - 获取成功返回JSON格式的列表结果
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    @Override
    public String findToJson(HttpServletRequest request)
            throws ServletException
    {
        //查询数据
        List list=this.findTo(request);
        list=this.generateOutput(request,list);

        int pageSize=this.manager.getPageSize();
        int page=this.currentPage;
        int total=this.manager.getTotalCount();
        int totalPage=this.manager.getTotalPage();

        return JsonProductor.toJsonByObjectList(list,pageSize,page,total,totalPage);
    }

    /**
     * 方法（公共）
     * 名称：   findToExcel
     * 描述:    查询数据，返回Excel结果<br>
     *
     * @param request - HTTP请求对象
     * @return String - 获取成功返回Excel格式的列表结果
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    @Override
    public String findToExcel(HttpServletRequest request)
            throws ServletException
    {
        //查询数据
        List list=this.findTo(request);
        list=this.generateOutput(request,list);

        int pageSize=this.manager.getPageSize();
        int page=this.currentPage;
        int total=this.manager.getTotalCount();
        int totalPage=this.manager.getTotalPage();

        //转换Excel格式

        return "";
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
        return false;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    supportNoLoginMode<br>
     * 描述:    支持非登录下处理模式<br>
     *
     * @return boolean - 如果参数值为null或""，返回true
     */
    @Override
    public String supportNoLoginMode(HttpServletRequest request)
    {
        return AbstractServlet.VALUE_MODE_LIST;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    isCurrentUseNoLoginMode<br>
     * 描述:    当前是否使用非登录外部模式<br>
     *
     * @param request
     * @return boolean - 如果参数值为null或""，返回true
     */
    public boolean isCurrentUseNoLoginMode(HttpServletRequest request)
    {
        try
        {
            return this.supportNoLoginFind() && (this.getLoginUserInSession(request)==null);
        }
        catch(ServletException ex)
        {
            return false;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    getFullLink<br>
     * 描述:    获取当前的服务器指定地址的全链接地址<br>
     *
     * @param request - HTTP请求对象
     * @param link    - 链接地址
     * @return String - 指定地址的全链接地址
     */
    public String getFullLink(HttpServletRequest request,String link)
    {
        if(link==null || "".equals(link.trim())) return link;
        return link.startsWith("http:")?link:(this.getServerRootPath(request)+link);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    findTo<br>
     * 描述:    查询数据，并返回List<br>
     *
     * @param request - HTTP请求对象
     * @return List - 数据对象列表
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    protected List<T> findTo(HttpServletRequest request) throws ServletException
    {
        try
        {
            //判断数据管理器是否初始化
            if(this.managerNullCheck())
            {
                throw new ServletException("数据库管理器未初始化！");
            }

            //获取当前页码
            String maxStr=request.getParameter(AbstractServlet.PARAM_MAX);
            int max=CachingServiceLocator.getInstance()
                    .getInteger(AbstractServlet.ENV_CONFIG_PAGEMAX);
            max=(maxStr==null)?max:Integer.parseInt(maxStr);

            //获取一页数据数量
            String pageStr=request.getParameter(AbstractServlet.PARAM_PAGE);
            int page=(pageStr==null)?1:Integer.parseInt(pageStr);
            this.currentPage=page;

            //获取列表排序字段
            String orderBy=request.getParameter(AbstractServlet.PARAM_ORDER);
            if(this.paramNullCheck(orderBy)) this.manager.setOrderBy(orderBy);

            //获取指定的搜索条件集并设置搜索条件
            Map<String,Object> condition=this.generateCondition(request);

            //查询结果
            List<T> list=this.manager.find(condition,page,max);
            if(list==null) throw new ServletException(this.manager.getError());
            return list;
        }
        catch(NumberFormatException
                |NamingException
                |ServletException ex)
        {
            throw new ServletException(ex.toString());
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getAreaLimitField<br>
     * 描述:    指定当前业务处理部门限制使用的行政区域字段名，使用null为不指定<br>
     *
     * @return String - 当前业务处理部门限制使用的行政区域字段名
     */
    protected String getAreaLimitField()
    {
        return null;
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
    protected Map<String,Object> generateCondition(HttpServletRequest request)
            throws ServletException
    {
        Map<String,Object> condition=new HashMap<>();
        String search=request.getParameter(AbstractServlet.PARAM_SEARCH_VALUE);
        if(!this.paramNullCheck(search))
        {
            String[] searchs=search.split(AbstractServlet.PARAM_SEARCH_SPLIT);
            if(searchs!=null)
            {
                //生成条件集
                String[] values;
                String key, value, searchKey="";
                for(int i=0;i<searchs.length;i++)
                {
                    values=searchs[i].split(AbstractServlet.PARAM_SEARCH_SPLIT_SE);
                    if(values.length<2) continue;

                    //读取数据
                    key=values[0];
                    value=values[1];

                    //如果为搜索条件，记录当前条件
                    if(AbstractServlet.PARAM_SEARCH_KEY.equals(key))
                    {
                        searchKey=value;
                    }
                    else
                    {
                        if(AbstractServlet.PARAM_SEARCH_VALUE.equals(key))
                        {
                            key=searchKey;
                            value="%"+value+"%";
                        }
                        condition.put(key,value);
                    }
                }
            }
        }

        LoginUser user=this.getLoginUserInSession(request);
        //判断是否有行政区域限制
        String sqlName=this.getAreaLimitField();
        if(!this.paramNullCheck(sqlName))
        {
            if(this.supportNoLoginFind() && user==null)
            {
                String fromApp=request.getParameter(AbstractServlet.PARAM_FROMAPP);
                if(this.paramNullCheck(fromApp))
                {
                    throw new ServletException("未指定来源应用的授权码！");
                }
                //获取APP属性
                BusiSectionFacadeLocal secManager=new BusiSectionFacade(this.connector);
                BusiSection section=secManager.findBySectionCode(fromApp,"0000");
                condition.put(sqlName+" like ?",section.getSectionCode());
            }
            else
            {
                condition.putAll(user.generateAreaCondition(sqlName,user.getAreaId()));
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
    protected List<Object> generateOutput
    (HttpServletRequest request,List<T> list)
    {
        return (List<Object>)list;
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
        return (param==null
                || "".equals(param.trim())
                || "null".equals(param.trim())
                || "-1".equals(param.trim()));
    }

    /**
     * 方法（受保护）<br>
     * 名称:    managerNullCheck<br>
     * 描述:    检查当前的数据管理器是否为null<br>
     *
     * @return boolean - 如果数据管理器为null，返回true
     */
    protected boolean managerNullCheck()
    {
        if(this.manager==null) this.initManager();
        return (this.manager==null);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    isUseTransaction<br>
     * 描述:    是否使用事务处理机制<br>
     *
     * @return boolean - 是否使用事务处理机制
     */
    protected boolean isUseTransaction()
    {
        return false;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getServerRootPath<br>
     * 描述:    获取当前的服务器链接地址根目录<br>
     *
     * @param request - HTTP请求对象
     * @return String - 服务器链接地址根目录
     */
    protected String getServerRootPath(HttpServletRequest request)
    {
        String link=request.getRequestURL().toString();
        return link.substring(0,link.lastIndexOf("/")+1);
    }
}