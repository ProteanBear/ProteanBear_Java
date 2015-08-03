package pb.data;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.xa.client.OracleXADataSource;

/**
 * 本Java类是用于描述通过jdbc方式直接连接数据库的方法，实现了Connecter接口
 *
 * @author proteanBear(马强)
 * @version 2.00 2010/09/16
 * @since jdk1.4
 */
public class PoolJdbcConnector extends AbstractConnector implements PoolConnector
{
    /**---------------------------开始：静态内容------------------------------**/

    /**
     * 静态常量<br>
     * 名称:    DATABASE_MYSQL<br>
     * 描述:    mysql数据库标示<br>
     */
    public static final String DATABASE_MYSQL="MYSQL";

    /**
     * 静态常量<br>
     * 名称:    DATABASE_SQLSERVER<br>
     * 描述:    sqlserver数据库标示<br>
     */
    public static final String DATABASE_SQLSERVER="SQLSERVER";

    /**
     * 静态常量<br>
     * 名称:    DATABASE_ACCESS<br>
     * 描述:    access数据库标示<br>
     */
    public static final String DATABASE_ACCESS="ACCESS";

    /**
     * 静态常量<br>
     * 名称:    DATABASE_ODBC<br>
     * 描述:    odbc连接标示<br>
     */
    public static final String DATABASE_ODBC="ODBC";

    /**
     * 静态常量<br>
     * 名称:    DATABASE_ORACLE<br>
     * 描述:    oracle数据库标示<br>
     */
    public static final String DATABASE_ORACLE="ORACLE";

    /**
     * 静态常量<br>
     * 名称:    DATABASE_DB2<br>
     * 描述:    db2数据库标示<br>
     */
    public static final String DATABASE_DB2="DB2";

    /**
     * 静态常量<br>
     * 名称:    DATABASE_SYBASE<br>
     * 描述:    sybase数据库标示<br>
     */
    public static final String DATABASE_SYBASE="SYBASE";

    /**
     * 静态常量<br>
     * 名称:    DATABASE_JAVADB<br>
     * 描述:    sybase数据库标示<br>
     */
    public static final String DATABASE_JAVADB="JAVADB";

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

    /**
     * 静态常量(私有)<br>
     * 名称:    URL_IP<br>
     * 描述:    url中的IP标示<br>
     */
    private static final String URL_IP="{$IP}";

    /**
     * 静态常量(私有)<br>
     * 名称:    URL_PORT<br>
     * 描述:    url中的端口标示<br>
     */
    private static final String URL_PORT="{$PORT}";

    /**
     * 静态常量(私有)<br>
     * 名称:    URL_DATABASENAME<br>
     * 描述:    url中的数据库名称标示<br>
     */
    private static final String URL_DATABASENAME="{$DATABASENAME}";

    /**
     * 静态域（私有）<br>
     * 名称:   MAP_URL <br>
     * 描述:   用于存放驱动信息值名对的Map数据结构<br>
     */
    private static Map<String,String> MAP_URL;

    /**
     * 静态方法（公共）<br>
     * 名称:    getUrl<br>
     * 描述:    根据指定的数据库类型获取相应的url连接<br>
     *
     * @param databaseClass - 数据库类型
     * @return String - url
     */
    public static String getUrl(String databaseClass)
    {
        return MAP_URL.get(databaseClass);
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    generateUrl<br>
     * 描述:    根据给定的参数生成对应的url及需要的驱动的名称<br>
     *
     * @param databaseClass - 数据库类型
     * @param ip            - 连接地址
     * @param port          - 连接端口
     * @param databaseName  - 数据库名称
     * @return String - 生成的url
     */
    public static String generateUrl
    (String databaseClass,String ip,int port,String databaseName)
    {
        String result=getUrl(databaseClass);
        result=result.replace(URL_IP,ip);
        result=result.replace(URL_PORT,port+"");
        result=result.replace(URL_DATABASENAME,databaseName);
        return result;
    }

    /**
     * 静态方法（私有）<br>
     * 名称:    getUrl<br>
     * 描述:    根据指定的数据库类型获取相应的驱动名称<br>
     *
     * @param databaseClass - 数据库类型
     * @param url           - 驱动名称
     * @return 无
     */
    private static void setUrl(String databaseClass,String url)
    {
        MAP_URL.put(databaseClass,url);
    }

    //初始化Map
    static
    {
        MAP_URL=Collections.synchronizedMap(new HashMap<String,String>());
    }

    //设置数据库连接格式值名对
    static
    {
        setUrl(
                DATABASE_MYSQL,
                "jdbc:mysql://"+URL_IP+":"+URL_PORT+"/"+URL_DATABASENAME+"?useUnicode=true&characterEncoding=UTF-8"
        );
        setUrl(DATABASE_SQLSERVER,"jdbc:microsoft:sqlserver://"+URL_IP+":"+URL_PORT+";DatabaseName="+URL_DATABASENAME);
        setUrl(DATABASE_ACCESS,"jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ="+URL_DATABASENAME);
        setUrl(DATABASE_ODBC,"jdbc:odbc:"+URL_DATABASENAME);
        setUrl(DATABASE_ORACLE,"jdbc:oracle:thin:@"+URL_IP+":"+URL_PORT+":"+URL_DATABASENAME);
        setUrl(DATABASE_DB2,"jdbc:db2://"+URL_IP+":"+URL_PORT+"/"+URL_DATABASENAME);
        setUrl(DATABASE_SYBASE,"jdbc:sybase:Tds:"+URL_IP+":"+URL_PORT+"/"+URL_DATABASENAME);
        setUrl(DATABASE_JAVADB,"jdbc:derby://"+URL_IP+":"+URL_PORT+"/"+URL_DATABASENAME);
    }

    /**---------------------------结束：静态内容------------------------------**/

    /**
     * 域(私有)<br>
     * 名称:    url<br>
     * 描述:    数据库连接字符串<br>
     */
    private String url;

    /**
     * 域(私有)<br>
     * 名称:    sourceType<br>
     * 描述:    数据资源类型<br>
     */
    private int sourceType;

    /**
     * 域(私有)<br>
     * 名称:    isBusy<br>
     * 描述:    标识连接器是否正在使用中<br>
     */
    private boolean isBusy;

    /**
     * 构造函数（无参数）<br>
     */
    public PoolJdbcConnector()
    {
        super();
        this.setSourceType(SOURCETYPE_DATASOURCE);
    }

    /**
     * 构造函数（url）<br>
     * 描述：   指定url，用户名、密码为空
     *
     * @param url - 数据库连接url
     */
    public PoolJdbcConnector(String url)
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
    public PoolJdbcConnector(String url,String loginname,String pass)
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
    public PoolJdbcConnector(String url,String loginname,String pass,int type)
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
    public PoolJdbcConnector
    (String databaseClass,String ip,int port,String databaseName,
     String loginname,String pass)
    {
        this();
        this.setUrl(generateUrl(databaseClass,ip,port,databaseName));
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
    public PoolJdbcConnector
    (String databaseClass,String ip,int port,String databaseName,
     String loginname,String pass,int type)
    {
        this();
        this.setUrl(generateUrl(databaseClass,ip,port,databaseName));
        this.setLoginname(loginname);
        this.setPassword(pass);
        this.setSourceType(type);
    }

    /**
     * 方法（公共）<br>
     * 名称:    connect<br>
     * 描述:    根据指定的Url连接到数据库并获取连接<br>
     */
    @Override
    public void connect()
    {
        try
        {
            if(this.url==null || "".equals(this.url.trim()))
            {
                this.setError("url为空");
                return;
            }
            if(this.isConnected())
            {
                return;
            }

            //判断数据库类型，构造数据源
            String databaseClass=this.generateDatabaseClass(this.url);
            //Mysql数据库
            if(DATABASE_MYSQL.equals(databaseClass))
            {
                switch(this.sourceType)
                {
                    //DataSource类型资源
                    case 0:
                    {
                        MysqlDataSource dataSource=new MysqlDataSource();
                        dataSource.setURL(this.url);
                        this.setConnection(dataSource.getConnection(this.getLoginname(),this.getPassword()));
                        this.setIsConnect(true);
                        break;
                    }
                    //XADataSource类型资源
                    case 1:
                    {
                        MysqlXADataSource dataSource=new MysqlXADataSource();
                        dataSource.setURL(this.url);
                        this.setConnection(dataSource.getConnection(this.getLoginname(),this.getPassword()));
                        this.setIsConnect(true);
                        break;
                    }
                    //ConnectionPoolDataSource类型资源
                    case 2:
                    {
                        MysqlConnectionPoolDataSource dataSource=new MysqlConnectionPoolDataSource();
                        dataSource.setURL(this.url);
                        this.setConnection(dataSource.getConnection(this.getLoginname(),this.getPassword()));
                        this.setIsConnect(true);
                        break;
                    }
                    default:
                    {
                        this.setError("未知的数据资源类型");
                        this.setIsConnect(false);
                    }
                }
            }
            //SqlServer数据库
            if(DATABASE_SQLSERVER.equals(databaseClass))
            {
                this.setError("目前不支持此数据库类型");
                this.setIsConnect(false);
            }
            //Access数据库
            if(DATABASE_ACCESS.equals(databaseClass))
            {
                this.setError("目前不支持此数据库类型");
                this.setIsConnect(false);
            }
            //Odbc连接
            if(DATABASE_ODBC.equals(databaseClass))
            {
                this.setError("目前不支持此数据库类型");
                this.setIsConnect(false);
            }
            //Oracle数据库
            if(DATABASE_ORACLE.equals(databaseClass))
            {
                switch(this.sourceType)
                {
                    //DataSource类型资源
                    case 0:
                    {
                        OracleDataSource dataSource=new OracleDataSource();
                        dataSource.setURL(this.url);
                        this.setConnection(dataSource.getConnection(this.getLoginname(),this.getPassword()));
                        this.setIsConnect(true);
                        break;
                    }
                    //XADataSource类型资源
                    case 1:
                    {
                        OracleXADataSource dataSource=new OracleXADataSource();
                        dataSource.setURL(this.url);
                        this.setConnection(dataSource.getConnection(this.getLoginname(),this.getPassword()));
                        this.setIsConnect(true);
                        break;
                    }
                    //ConnectionPoolDataSource类型资源
                    case 2:
                    {
                        OracleConnectionPoolDataSource dataSource=new OracleConnectionPoolDataSource();
                        dataSource.setURL(this.url);
                        this.setConnection(dataSource.getConnection(this.getLoginname(),this.getPassword()));
                        this.setIsConnect(true);
                        break;
                    }
                    default:
                    {
                        this.setError("未知的数据资源类型");
                        this.setIsConnect(false);
                    }
                }
            }
            //DB2数据库
            if(DATABASE_DB2.equals(databaseClass))
            {
                this.setError("目前不支持此数据库类型");
                this.setIsConnect(false);
            }
            //Sybase数据库
            if(DATABASE_SYBASE.equals(databaseClass))
            {
                this.setError("目前不支持此数据库类型");
                this.setIsConnect(false);
            }
            //JavaDB数据库
            if(DATABASE_JAVADB.equals(databaseClass))
            {
                this.setError("目前不支持此数据库类型");
                this.setIsConnect(false);
            }
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
            this.setIsConnect(false);
        }
    }

    /**
     * 访问器（公共）<br>
     * 目标：   url<br>
     *
     * @return String - 数据库连接字符串
     */
    @Override
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
    @Override
    public final void setUrl(String url)
    {
        if(url==null || "".equals(url.trim()))
        {
            this.setError("url为空");
            return;
        }
        if(url.toUpperCase().indexOf(DATABASE_MYSQL)!=-1
                && url.toLowerCase().indexOf("characterencoding")==-1)
        {
            url+="useUnicode=true&characterEncoding=UTF-8";
        }
        this.url=url;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   sourceType<br>
     *
     * @return String - 数据资源类型
     */
    @Override
    public String getSourceType()
    {
        String result="";
        switch(this.sourceType)
        {
            case 0:
                result="DataSource";
                break;
            case 1:
                result="XADataSource";
                break;
            case 2:
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
    @Override
    public final void setSourceType(int type)
    {
        this.sourceType=type;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   isBusy<br>
     *
     * @return boolean - 连接器是否正在使用中
     */
    @Override
    public boolean isBusy()
    {
        return this.isBusy;
    }

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   isBusy<br>
     *
     * @param busy - 连接器使用标识
     */
    @Override
    public final void setBusy(boolean busy)
    {
        this.isBusy=busy;
    }

    /**
     * 方法（私有）<br>
     * 名称:    generateDatabaseClass<br>
     * 描述:    根据给定的url返回数据库的类型<br>
     *
     * @param url - 连接url
     * @return String - 驱动名称
     */
    private String generateDatabaseClass(String url)
    {
        if(url==null || "".equals(url.trim()))
        {
            this.setError("url为空");
            return null;
        }
        url=url.toUpperCase();
        if(url.indexOf(DATABASE_MYSQL)!=-1)
        {
            return DATABASE_MYSQL;
        }
        else if(url.indexOf(DATABASE_SQLSERVER)!=-1)
        {
            return DATABASE_SQLSERVER;
        }
        else if(url.indexOf(DATABASE_ACCESS)!=-1)
        {
            return DATABASE_ACCESS;
        }
        else if(url.indexOf(DATABASE_ODBC)!=-1)
        {
            return DATABASE_ODBC;
        }
        else if(url.indexOf(DATABASE_ORACLE)!=-1)
        {
            return DATABASE_ORACLE;
        }
        else if(url.indexOf(DATABASE_DB2)!=-1)
        {
            return DATABASE_DB2;
        }
        else if(url.indexOf(DATABASE_SYBASE)!=-1)
        {
            return DATABASE_SYBASE;
        }
        else if(url.indexOf(DATABASE_JAVADB)!=-1)
        {
            return DATABASE_JAVADB;
        }
        else
        {
            this.setError("不支持url对应的数据库类型");
            return null;
        }
    }
}