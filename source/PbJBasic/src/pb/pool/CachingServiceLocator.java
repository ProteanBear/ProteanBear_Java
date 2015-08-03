package pb.pool;

import java.net.URL;
import javax.ejb.EJBHome;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

/**
 * 缓存服务定位器。<br>
 * 从缓存中读取特定的服务，如果不存在此服务就获取此服务并存放在缓存中。<br>
 * 不可构造单实例类。<br>
 * 1.01 —— 修改为饿汉式单例类方式，避免多线程模式下的错误。<br>
 *
 * @author proteanBear(马强)
 * @version 1.01 2011/08/08
 */
public class CachingServiceLocator extends AbstractCachePool
{
    /*--------------------------开始：静态内容---------------------------------*/

    /**
     * 静态常量(私有)<br>
     * 名称:    ENVCONTEXT<br>
     * 描述:    记录获取环境条目时使用的引用名称<br>
     */
    private static final String ENVCONTEXT="java:comp/env";

    /**
     * 静态域(私有)<br>
     * 名称:    me<br>
     * 描述:    缓存服务定位器唯一实例<br>
     */
    private static CachingServiceLocator me;

    /**
     * 静态方法（公共）<br>
     * 名称:    getInstance<br>
     * 描述:    获取类当前唯一对象实例<br>
     *
     * @return CachingServiceLocator - 唯一对象实例
     * @throws javax.naming.NamingException
     */
    public static CachingServiceLocator getInstance() throws NamingException
    {
        if(me==null)
        {
            me=new CachingServiceLocator();
        }
        return me;
    }

    //静态代码，构建唯一实例
    static
    {
        try
        {
            me=new CachingServiceLocator();
        }
        catch(NamingException ex)
        {
        }
    }

    /*--------------------------结束：静态内容---------------------------------*/

    /**
     * 域(私有)<br>
     * 名称:    initialContext<br>
     * 描述:    记录执行命名操作的初始上下文对象，用于定位服务<br>
     */
    private InitialContext initialContext;

    /**
     * 构造函数（私有）<br>
     * 描述：    初始化域，私有构造方法，使类无法创建实例
     *
     * @throws javax.naming.NamingException - InitialContext构建时可抛出上下文引用异常
     */
    private CachingServiceLocator() throws NamingException
    {
        this.initialContext=new InitialContext();
        this.initPool();
    }

    /**
     * 方法（受保护）<br>
     * 名称:    initPool<br>
     * 描述:    初始化缓存服务定位器<br>
     */
    @Override
    protected final void initPool()
    {
        this.initCacheMap();
    }

    /**
     * 方法（私有）<br>
     * 名称:    lookup<br>
     * 描述:    定位指定JNDI引用名的服务，返回服务对象<br>
     *
     * @param jndiName - JNDI引用名
     * @return Object - 服务对象
     * @throws javax.naming.NamingException - InitialContext构建时可抛出上下文引用异常
     */
    private Object lookup(String jndiName) throws NamingException
    {
        Object cachedObj=this.getFromPool(me);
        if(cachedObj==null)
        {
            cachedObj=this.initialContext.lookup(jndiName);
            this.addToPool(jndiName,cachedObj);
        }
        return cachedObj;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getLocalHome<br>
     * 描述:    定位EJB本地服务接口对象<br>
     *
     * @param jndiHomeName - EJB本地服务接口的JNDI引用名
     * @return Object - EJB本地服务接口对象
     * @throws javax.naming.NamingException
     */
    public Object getLocalHome(String jndiHomeName) throws NamingException
    {
        return lookup(jndiHomeName);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getLocalHome<br>
     * 描述:    定位EJB外部服务接口对象<br>
     *
     * @param jndiHomeName - EJB外部服务接口的JNDI引用名
     * @param className
     * @return EJBHome - EJB外部服务接口对象
     * @throws javax.naming.NamingException
     */
    public EJBHome getRemoteHome(String jndiHomeName,Class className)
            throws NamingException
    {
        Object objref=lookup(jndiHomeName);
        return (EJBHome)PortableRemoteObject.narrow(objref,className);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getConnectionFactory<br>
     * 描述:    定位JMS（消息收发系统）服务对象<br>
     *
     * @param connFactoryName - JNDI引用名
     * @return ConnectionFactory - JMS（消息收发系统）服务对象
     * @throws javax.naming.NamingException
     */
    public ConnectionFactory getConnectionFactory(String connFactoryName)
            throws NamingException
    {
        return (ConnectionFactory)lookup(connFactoryName);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDestination<br>
     * 描述:    This method obtains the topc itself for a caller<br>
     *
     * @param destName - JNDI引用名
     * @return Destination - the Topic Destination to send messages to
     * @throws javax.naming.NamingException
     */
    public Destination getDestination(String destName)
            throws NamingException
    {
        return (Destination)lookup(destName);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDataSource<br>
     * 描述:    This method obtains the datasource<br>
     *
     * @param dataSourceName - JNDI引用名
     * @return DataSource - the DataSource corresponding to the name parameter
     * @throws javax.naming.NamingException
     */
    public DataSource getDataSource(String dataSourceName)
            throws NamingException
    {
        return (DataSource)lookup(dataSourceName);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getSession<br>
     * 描述:    This method obtains the mail session<br>
     *
     * @param sessionName - JNDI引用名
     * @return Session - the Session corresponding to the name parameter
     * @throws javax.naming.NamingException
     */
    public Session getSession(String sessionName) throws NamingException
    {
        return (Session)lookup(sessionName);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getUrl<br>
     * 描述:    This method obtains the URL<br>
     *
     * @param envName - JNDI引用名
     * @return URL - the URL value corresponding to the env entry name.
     * @throws javax.naming.NamingException
     */
    public URL getUrl(String envName)
            throws NamingException
    {
        return (URL)lookup(envName);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getBoolean<br>
     * 描述:    从环境条目中获取指定的变量值<br>
     *
     * @param envName - JNDI引用名
     * @return boolean - the boolean value corresponding to the env entry such as SEND_CONFIRMATION_MAIL property.
     * @throws javax.naming.NamingException
     */
    public boolean getBoolean(String envName)
            throws NamingException
    {
        Context envCtx=(Context)this.lookup(ENVCONTEXT);
        return (Boolean)envCtx.lookup(envName);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getString<br>
     * 描述:    从环境条目中获取指定的变量值<br>
     *
     * @param envName - JNDI引用名
     * @return String - the String value corresponding to the env entry name.
     * @throws javax.naming.NamingException
     */
    public String getString(String envName)
            throws NamingException
    {
        Context envCtx=(Context)this.lookup(ENVCONTEXT);
        return (String)envCtx.lookup(envName);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getInteger<br>
     * 描述:    从环境条目中获取指定的变量值<br>
     *
     * @param envName - JNDI引用名
     * @return int -
     * @throws javax.naming.NamingException
     */
    public int getInteger(String envName)
            throws NamingException
    {
        Context envCtx=(Context)this.lookup(ENVCONTEXT);
        Integer integer=(Integer)envCtx.lookup(envName);
        return integer;
    }
}