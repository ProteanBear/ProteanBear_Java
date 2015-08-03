package pb.data;

import java.sql.SQLException;

import pb.pool.AbstractListCachePool;

/**
 * 数据库连接池实现类。<br>
 * 1.02 —— 在获取空闲连接（findFree方法）时增加了是否检测连接的判断。<br>
 * 增加当前空闲连接数的记录。<br>
 * 增加当前连接池中总连接数的公共获取方法。<br>
 * 1.01 —— 去除初始池大小、池最大尺寸、池调整大小及等待时间的字段和方法，移动到父类中。<br>
 * 1.00 —— 实现数据库连接的基本方法<br>
 *
 * @author proteanBear(马强)
 * @version 1.01 2011/06/16
 */
public class ConnectionPool extends AbstractListCachePool<PoolConnector>
{
    /**---------------------------开始：静态常量------------------------------**/
    /**
     * 静态常量<br>
     * 名称:    TESTMETHOD_AUTOCOMMIT<br>
     * 描述:    验证方法-auto-commit：使用自动提交模式验证连接是否有效<br>
     */
    public static final int TESTMETHOD_AUTOCOMMIT=0;

    /**
     * 静态常量<br>
     * 名称:    TESTMETHOD_METADATA<br>
     * 描述:    验证方法-meta-data：确保对 Connection.getMetaData() 的调用可以获得对数据库的调用，从而确保连接有效<br>
     */
    public static final int TESTMETHOD_METADATA=1;

    /**
     * 静态常量<br>
     * 名称:    TESTMETHOD_TABLE<br>
     * 描述:    验证方法-sql：使用Sql语句测试<br>
     */
    public static final int TESTMETHOD_SQL=2;

    /**
     * 静态常量<br>
     * 名称:    SOURCETYPE_DATASOURCE<br>
     * 描述:    数据资源类型-DataSource<br>
     */
    public static final int SOURCETYPE_DATASOURCE=0;

    /**
     * 静态常量<br>
     * 名称:    SOURCETYPE_XADATASOURCE<br>
     * 描述:    数据资源类型-XADataSource<br>
     */
    public static final int SOURCETYPE_XADATASOURCE=1;

    /**
     * 静态常量<br>
     * 名称:    SOURCETYPE_POOLDATASOURCE<br>
     * 描述:    数据资源类型-ConnectionPoolDataSource<br>
     */
    public static final int SOURCETYPE_POOLDATASOURCE=2;
    /**---------------------------结束：静态常量------------------------------**/

    /**---------------------------开始：域-----------------------------------**/

    /**
     * 域(私有)<br>
     * 名称:    dataSourceType<br>
     * 描述:    数据资源类型<br>
     */
    private int dataSourceType;

    /**
     * 域(私有)<br>
     * 名称:    url<br>
     * 描述:    数据库连接字符串<br>
     */
    private String url;

    /**
     * 域(私有)<br>
     * 名称:    loginname<br>
     * 描述:    登录名<br>
     */
    private String loginname;

    /**
     * 域(私有)<br>
     * 名称:    password<br>
     * 描述:    登录密码<br>
     */
    private String password;

    /**
     * 域(私有)<br>
     * 名称:    timeOut<br>
     * 描述:    空闲超时,单位为秒<br>
     */
    private int timeOut;

    /**
     * 域(私有)<br>
     * 名称:    isTestConnect<br>
     * 描述:    是否连接验证<br>
     */
    private boolean isTestConnect;

    /**
     * 域(私有)<br>
     * 名称:    testMethod<br>
     * 描述:    验证方法:
     * 0、auto-commit：使用自动提交模式验证连接是否有效<br>
     * 1、meta-data：确保对 Connection.getMetaData() 的调用可以获得对数据库的调用，从而确保连接有效<br>
     * 2、sql:使用Sql测试<br>
     */
    private int testMethod;

    /**
     * 域(私有)<br>
     * 名称:    testTable<br>
     * 描述:    测试Sql-如果选择Sql验证，请指定Sql语句内容<br>
     */
    private String testSql;

    /**
     * 域(私有)<br>
     * 名称:    isAllCloseWhenTestFail<br>
     * 描述:    连接失败时关闭所有连接并重新连接，否则只在使用时重新连接 <br>
     */
    private boolean isAllCloseWhenTestFail;

    /**
     * 域(私有)<br>
     * 名称:    statementTimeOut<br>
     * 描述:    用于终止异常长时间运行查询的连接的超时属性。-1 表示未启用。单位为秒。<br>
     */
    private int statementTimeOut;

    /**
     * 域(私有)<br>
     * 名称:    currentIdleConnectNumber<br>
     * 描述:    记录当前空闲线程的数量<br>
     */
    private int currentIdleConnectNumber;

    /**---------------------------结束：域-----------------------------------**/

    /**---------------------------开始：构造函数-----------------------------**/

    /**
     * 构造函数（无参数）<br>
     */
    public ConnectionPool()
    {
        //默认连接池名称
        this.setPoolName("默认连接池");
        //默认数据资源类型
        this.setSourceType(SOURCETYPE_DATASOURCE);
        //默认初始和最小池大小
        this.setInitialConnectNumber(8);
        //默认池最大尺寸
        this.setMaxConnectNumber(32);
        //默认池调整大小数量
        this.setIncrementalConnectNumber(2);
        //默认空闲超时-300秒
        this.setTimeOut(300);
        //默认等待时间-250毫秒
        this.setWaitTime(250);
        //最长等待时间-60000毫秒
        this.setMaxWaitTime(60000);
        //是否连接验证
        this.setIsTestConnect(false);
        //验证方法
        this.setTestMethod(TESTMETHOD_AUTOCOMMIT);
        //测试表名
        this.setTestSql(null);
        //关闭所有连接
        this.setIsAllCloseWhenTestFail(false);
        //异常长时间运行查询
        this.setStatementTimeOut(-1);
        //初始化缓冲池
        this.initPool();
    }

    /**
     * 构造函数（url）<br>
     * 描述：   指定url，用户名、密码为空
     *
     * @param url - 数据库连接url
     */
    public ConnectionPool(String url)
    {
        this(url,"","");
    }

    /**
     * 构造函数（url,loginname,password）<br>
     * 描述：   指定url、用户名及密码
     *
     * @param url       - 连接url
     * @param loginname - 登录名
     * @param pass      - 密码
     */
    public ConnectionPool(String url,String loginname,String pass)
    {
        this();
        this.setUrl(url);
        this.setLoginname(loginname);
        this.setPassword(pass);
    }

    /**
     * 构造函数（url,loginname,password）<br>
     * 描述：   指定url、用户名及密码
     *
     * @param url       - 连接url
     * @param loginname - 登录名
     * @param pass      - 密码
     * @param type      - 数据资源类型
     */
    public ConnectionPool(String url,String loginname,String pass,int type)
    {
        this();
        this.setUrl(url);
        this.setLoginname(loginname);
        this.setPassword(pass);
        this.setSourceType(type);
    }

    /**
     * 构造函数（databaseClass,ip,port,databaseName,loginname,pass）<br>
     * 描述：   指定数据库类型、连接地址、连接端口、数据库名称、登录名及密码
     *
     * @param databaseClass - 数据库类型
     * @param ip            - 连接地址
     * @param port          - 连接端口
     * @param databaseName  - 数据库名称
     * @param loginname     - 登录名
     * @param pass          - 密码
     */
    public ConnectionPool
    (String databaseClass,String ip,int port,String databaseName,
     String loginname,String pass)
    {
        this();
        this.setUrl(PoolJdbcConnector.generateUrl(databaseClass,ip,port,databaseName));
        this.setLoginname(loginname);
        this.setPassword(pass);
    }

    /**
     * 构造函数（databaseClass,ip,port,databaseName,loginname,pass）<br>
     * 描述：   指定数据库类型、连接地址、连接端口、数据库名称、登录名及密码
     *
     * @param databaseClass - 数据库类型
     * @param ip            - 连接地址
     * @param port          - 连接端口
     * @param databaseName  - 数据库名称
     * @param loginname     - 登录名
     * @param pass          - 密码
     * @param type          - 数据资源类型
     */
    public ConnectionPool
    (String databaseClass,String ip,int port,String databaseName,
     String loginname,String pass,int type)
    {
        this();
        this.setUrl(PoolJdbcConnector.generateUrl(databaseClass,ip,port,databaseName));
        this.setLoginname(loginname);
        this.setPassword(pass);
        this.setSourceType(type);
    }

    /**---------------------------结束：构造函数-----------------------------**/

    /**---------------------------开始：方法---------------------------------**/

    /**
     * 方法（受保护）<br>
     * 名称:    createObject<br>
     * 描述:    创建一个数据库连接，并返回数据库连接对象<br>
     *
     * @return PoolConnector - 数据连接器对象
     */
    @Override
    protected PoolConnector createObject()
    {
        //判断是否设置连接URL
        if(this.getUrl()==null)
        {
            this.setError("未设置数据库连接URL！");
            return null;
        }

        //创建连接器
        PoolJdbcConnector connector=new PoolJdbcConnector(this.url,this.loginname,this.password,this.dataSourceType);
        //连接数据库
        connector.connect();
        //如果连接失败，记录错误信息并返回null
        if(!connector.isConnected())
        {
            this.setError(connector.getError());
            return null;
        }
        else
        {
            //连接成功，添加空闲连接数量
            this.currentIdleConnectNumber++;
            //如果创建连接器成功并且为第一次创建连接器，则获取驱动设置中的最大连接数
            //如果连接池设置的最大连接数大于驱动设置的最大连接数，则设置连接池最大连接数为驱动设置的最大连接数
            if(this.size()==0)
            {
                try
                {
                    int maxDriverConnectNumber=connector.getDatabaseMetaData().getMaxConnections();
                    if(this.size()>maxDriverConnectNumber) this.setMaxConnectNumber(maxDriverConnectNumber);
                }
                catch(SQLException ex)
                {
                    this.setError("获取数据库驱动最大连接数失败！");
                }
            }
        }
        return connector;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    findFreeConnection<br>
     * 描述:    查找连接池中所有的连接，查找一个可用的数据库连接，<br>
     * 如果没有可用的连接，返回 null。<br>
     *
     * @return PoolConnector - 返回一个可用的数据库连接器对象
     */
    @Override
    protected PoolConnector findFree()
    {
        PoolConnector free=null;
        boolean isTestSuccess=true;

        //遍历所有的对象，看是否有可用的连接
        for(int i=0;i<this.size();i++)
        {
            free=(PoolConnector)this.getFromPool(i);
            if(!free.isBusy())
            {
                //如果此连接没有在使用中，则获得它的数据库连接并把它设为忙
                free.setBusy(true);

                //如果设置为需验证连接则做连接测试
                if(this.isIsTestConnect())
                {
                    //测试此连接是否可用
                    if(!this.testConnection(free))
                    {
                        //如果连接池设置为“连接失败时关闭所有连接并重新连接”
                        if(this.isAllCloseWhenTestFail)
                        {
                            isTestSuccess=false;
                            break;
                        }
                        else
                        {
                            //如果此连接无效，重新连接数据库
                            free.connect();
                            //如果连接失败，记录错误信息，设置对象为null,跳出循环
                            if(!free.isConnected())
                            {
                                this.setError("重新连接数据库失败！");
                                free=null;
                                break;
                            }
                        }
                    }
                }

                //当前空闲连接数减1
                this.currentIdleConnectNumber--;

                //找到可用连接，跳出循环
                break;
            }
            free=null;
        }

        //如果连接池设置为“连接失败时关闭所有连接并重新连接”,关闭并重联所有连接
        if(!isTestSuccess) this.refreshConnections();

        return free;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    release<br>
     * 描述:    此函数返回一个数据库连接到连接池中，并把此连接置为空闲。<br>
     * 所有使用连接池获得的数据库连接均应在不使用此连接时返回它。
     *
     * @param connector - 需返回到连接池中的连接器对象
     */
    @Override
    protected void release(PoolConnector connector)
    {
        //遍历查找连接池中相同的连接器,返回索引
        //如果索引为-1（即没有找到指定对象），返回null
        int index=this.indexOfList(connector);
        if(index==-1)
        {
            this.setError("未找到指定的连接器对象！");
        }
        else
        {
            connector.setBusy(false);
            //当前空闲连接数加1
            this.currentIdleConnectNumber++;
            this.editInPool(index,connector);
        }
    }

    /**
     * 方法（私有）<br>
     * 名称:    testConnection<br>
     * 描述:    测试一个连接是否可用，如果不可用，关掉它并返回 false 否则可用返回 true。<br>
     * 通过字段testMethod区分使用什么方法测试连接是否可用
     *
     * @param connector - 数据库连接器对象
     * @return boolean - 返回 true 表示此连接可用， false 表示不可用
     */
    private boolean testConnection(PoolConnector connector)
    {
        boolean success=false;

        if(connector==null)
        {
            this.setError("指定的数据库连接器对象为空！");
            return success;
        }

        //根据连接测试方式testMethod，检测连接是否有效
        switch(this.testMethod)
        {
            case ConnectionPool.TESTMETHOD_AUTOCOMMIT:
            {
                success=connector.testConnectByAutoCommit();
                break;
            }
            case ConnectionPool.TESTMETHOD_METADATA:
            {
                success=connector.testConnectByMetaData();
                break;
            }
            case ConnectionPool.TESTMETHOD_SQL:
            {
                if(this.testSql==null || "".equals(this.testSql.trim()))
                {
                    this.setError("未设置测试用的SQL语句！");
                }
                else
                {
                    success=connector.testConnectBySql(this.testSql);
                }
                break;
            }
        }

        return success;
    }

    /**
     * 方法（公共）<br>
     * 名称:    initConnectionPool<br>
     * 描述:    初始化连接池，创建最小池大小（initialConnectNumber）的连接<br>
     */
    public void initConnectionPool()
    {
        this.initPool();
    }

    /**
     * 方法（公共）<br>
     * 名称:    getConnection<br>
     * 描述:    通过调用 getFreeConnection() 函数返回一个可用的数据库连接<br>
     * 如果当前没有可用的数据库连接，并且更多的数据库连接不能创建（如连接池大小的限制），此函数等待一会再尝试获取。
     *
     * @return PoolConnector - 返回一个可用的数据库连接器对象
     */
    public PoolConnector getConnection()
    {
        return this.get();
    }

    /**
     * 方法（公共）<br>
     * 名称:    returnConnection<br>
     * 描述:    此函数返回一个数据库连接到连接池中，并把此连接置为空闲。<br>
     * 所有使用连接池获得的数据库连接均应在不使用此连接时返回它。
     *
     * @param connector - 需返回到连接池中的连接器对象
     */
    public void returnConnection(PoolConnector connector)
    {
        this.release(connector);
    }

    /**
     * 方法（公共）<br>
     * 名称:    refreshConnections<br>
     * 描述:    刷新连接池中所有的连接器对象。<br>
     */
    public void refreshConnections()
    {
        //如果连接池中没有连接器对象，退出
        if(this.size()==0)
        {
            this.setError("当前连接池中没有数据库连接！");
            return;
        }

        try
        {
            //遍历连接池中所有连接器
            for(int i=0;i<this.size();i++)
            {
                PoolConnector connector=(PoolConnector)this.getFromPool(i);

                //如果连接正在使用中，等待5秒钟，直接刷新连接
                if(connector.isBusy()) wait(5000);
                connector.close();
                connector.connect();
                connector.setBusy(false);

                //连接成功，就更新连接池中的连接器对象；否则跳出循环
                if(connector.isConnected())
                {
                    this.editInPool(i,connector);
                }
                else
                {
                    this.setError("无法连接到数据库！");
                    break;
                }
            }
        }
        catch(InterruptedException ex)
        {
            this.setError(ex.toString());
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    clearConnectionPool<br>
     * 描述:    关闭连接池中所有的连接，并清空连接池。<br>
     */
    public void clearConnectionPool()
    {
        //如果连接池中没有连接器对象，退出
        if(this.size()==0)
        {
            this.setError("当前连接池中没有数据库连接！");
            return;
        }

        try
        {
            //遍历连接池中所有连接器,从尾部开始删除
            for(int i=this.size()-1;i>-1;i--)
            {
                PoolConnector connector=(PoolConnector)this.getFromPool(i);

                //如果连接正在使用中，等待5秒钟，直接刷新连接
                if(connector.isBusy()) wait(5000);

                connector.close();
                this.deleteFromPool(i);

                //清除一个空闲连接数量
                this.currentIdleConnectNumber--;
            }
        }
        catch(InterruptedException ex)
        {
            this.setError(ex.toString());
        }
        finally
        {
            this.clearPool();
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    getCurrentTotalConnectNumber<br>
     * 描述:    获取当前连接池中数据连接总数<br>
     *
     * @return int - 当前连接池中数据连接总数
     */
    public int getCurrentTotalConnectNumber()
    {
        return this.size();
    }

    /**---------------------------结束：方法---------------------------------**/

    /**---------------------------开始：访问器和更改器-----------------------**/

    /**
     * 访问器（公共）<br>
     * 目标：   url<br>
     *
     * @return String - 数据库连接字符串
     */
    public String getUrl()
    {
        return this.url;
    }

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   url<br>
     *
     * @param url - 数据库连接字符串
     */
    public final void setUrl(String url)
    {
        this.url=url;
    }

    /**
     * 更改器(公共)<br>
     * 目标：   loginname
     *
     * @param name - 登录名
     */
    public final void setLoginname(String name)
    {
        this.loginname=name;
    }

    /**
     * 更改器(公共)<br>
     * 目标：   password
     *
     * @param pass - 登录密码
     */
    public final void setPassword(String pass)
    {
        this.password=pass;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   sourceType<br>
     *
     * @return String - 数据资源类型
     */
    public String getSourceType()
    {
        String result="";
        switch(this.dataSourceType)
        {
            case SOURCETYPE_DATASOURCE:
                result="DataSource";
                break;
            case SOURCETYPE_XADATASOURCE:
                result="XADataSource";
                break;
            case SOURCETYPE_POOLDATASOURCE:
                result="ConnectionPoolDataSource";
                break;
            default:
                result="未知";
        }
        return result;
    }

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   sourceType<br>
     *
     * @param type - 数据资源类型
     */
    public final void setSourceType(int type)
    {
        this.dataSourceType=type;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   isAllCloseWhenTestFail<br>
     *
     * @return boolean - 连接失败时关闭所有连接并重新连接，否则只在使用时重新连接
     */
    public boolean getIsAllCloseWhenTestFail()
    {
        return isAllCloseWhenTestFail;
    }

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   isAllCloseWhenTestFail<br>
     *
     * @param isAllCloseWhenTestFail - 连接失败时关闭所有连接并重新连接，否则只在使用时重新连接
     */
    public final void setIsAllCloseWhenTestFail(boolean isAllCloseWhenTestFail)
    {
        this.isAllCloseWhenTestFail=isAllCloseWhenTestFail;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   isTestConnect<br>
     *
     * @return boolean - 是否连接验证
     */
    public boolean isIsTestConnect()
    {
        return isTestConnect;
    }

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   isTestConnect<br>
     *
     * @param isTestConnect - 是否连接验证
     */
    public final void setIsTestConnect(boolean isTestConnect)
    {
        this.isTestConnect=isTestConnect;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   statementTimeOut<br>
     *
     * @return boolean - 用于终止异常长时间运行查询的连接的超时属性。-1 表示未启用。单位为秒。
     */
    public int getStatementTimeOut()
    {
        return statementTimeOut;
    }

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   statementTimeOut<br>
     *
     * @param statementTimeOut - 用于终止异常长时间运行查询的连接的超时属性。-1 表示未启用。单位为秒。
     */
    public final void setStatementTimeOut(int statementTimeOut)
    {
        this.statementTimeOut=statementTimeOut;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   testMethod<br>
     *
     * @return boolean - 验证方法
     */
    public String getTestMethod()
    {
        String result="";
        switch(this.testMethod)
        {
            case ConnectionPool.TESTMETHOD_AUTOCOMMIT:
                result="auto-commit";
                break;
            case ConnectionPool.TESTMETHOD_SQL:
                result="Sql";
                break;
            case ConnectionPool.TESTMETHOD_METADATA:
                result="meta-data";
                break;
            default:
                result="未知";
        }
        return result;
    }

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   testMethod<br>
     *
     * @param testMethod - 验证方法
     */
    public final void setTestMethod(int testMethod)
    {
        this.testMethod=testMethod;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   testSql<br>
     *
     * @return String - 测试Sql-如果选择Sql验证，请指定Sql语句内容
     */
    public String getTestSql()
    {
        return testSql;
    }

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   testSql<br>
     *
     * @param testTable - 测试Sql-如果选择Sql验证，请指定Sql语句内容
     */
    public final void setTestSql(String testTable)
    {
        this.testSql=testTable;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   timeOut<br>
     *
     * @return boolean - 空闲超时,单位为秒
     */
    public int getTimeOut()
    {
        return timeOut;
    }

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   timeOut<br>
     *
     * @param timeOut - 空闲超时,单位为秒
     */
    public final void setTimeOut(int timeOut)
    {
        this.timeOut=timeOut;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   currentIdleThreadNumber<br>
     *
     * @return int - 当前空闲线程的数量
     */
    public int getCurrentIdleThreadNumber()
    {
        return currentIdleConnectNumber;
    }

    /**---------------------------结束：访问器和更改器-------------------------**/
}