package pb.data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 本Java类是用于描述数据库连接器的抽象类,实现了Connecter接口并提供给具体的实现类继承,<br>
 * 其中声明了连接和关闭数据库，并提供事务处理方法
 * 2.01 ——  增加三种模式的连接有效探测方法（auto-commit、meta-data、sql）；增加数据库信息描述对象（DatabaseMetaData）的获取方法
 *
 * @author proteanBear(马强)
 * @version 2.01 2011/06/15
 * @since jdk1.4
 */
public abstract class AbstractConnector implements Connector
{
    /**
     * 域(私有)<br>
     * 名称:    conn<br>
     * 描述:    数据库连接对象<br>
     */
    private Connection connection=null;

    /**
     * 域(私有)<br>
     * 名称:    isConnect<br>
     * 描述:    是否连接<br>
     */
    private boolean isConnect=false;

    /**
     * 域(私有)<br>
     * 名称:    error<br>
     * 描述:    记录错误信息<br>
     */
    private String error;

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
     * 构造函数（无参数）<br>
     */
    public AbstractConnector()
    {
    }

    /**
     * 构造函数（两个参数）<br>
     * 描述：   指定登录用用户名和密码
     *
     * @param loginname - 登录名
     * @param pass      - 密码
     */
    public AbstractConnector(String loginname,String pass)
    {
        this.loginname=loginname;
        this.password=pass;
    }

    /**
     * 方法（抽象）<br>
     * 名称:    connect<br>
     * 描述:    根据指定的Url连接到数据库并获取连接<br>
     */
    @Override
    abstract public void connect();

    /**
     * 方法（公共）<br>
     * 名称:    isConnect<br>
     * 描述:    是否连接到数据库<br>
     *
     * @return boolean - 是否连接
     */
    @Override
    public boolean isConnected()
    {
        return this.isConnect;
    }

    ;

    /**
     * 方法（公共）<br>
     * 名称:    close<br>
     * 描述:    关闭数据库连接<br>
     */
    @Override
    public void close()
    {
        try
        {
            if(this.connection!=null) this.connection.close();
            this.connection=null;
            this.isConnect=false;
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    getConnection<br>
     * 描述:    获取数据库连接对象<br>
     *
     * @return java.sql.Connection
     */
    @Override
    public Connection getConnection()
    {
        return this.connection;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   error<br>
     *
     * @return String - 数据库连接字符串
     */
    @Override
    public String getError()
    {
        return this.error;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDatabaseProductName<br>
     * 描述:    获取当前连接的数据库产品名称<br>
     *
     * @return String - 数据库产品名称
     */
    @Override
    public String getDatabaseProductName()
    {
        if(!this.isConnect || this.connection==null)
        {
            this.setError("当前并未连接数据库！");
            return null;
        }
        try
        {
            return this.connection.getMetaData().getDatabaseProductName().toUpperCase();
        }
        catch(SQLException ex)
        {
            this.setError(ex.toString());
            return null;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    testConnectByAutoCommit<br>
     * 描述:    使用自动提交模式验证连接是否有效<br>
     * 此方法只在部分数据库可用,如果不可用,抛出异常
     *
     * @return boolean - 连接是否有效
     */
    @Override
    public boolean testConnectByAutoCommit()
    {
        try
        {
            if(!this.isConnect || this.connection==null)
            {
                this.setError("当前并未连接数据库！");
                return false;
            }
            this.connection.setAutoCommit(true);
            return true;
        }
        // 上面抛出异常，此连接己不可用，关闭它，并返回 false;
        catch(SQLException ex)
        {
            this.close();
            return false;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    testConnectByMetaData<br>
     * 描述:    确保对 Connection.getMetaData() 的调用验证连接是否有效<br>
     *
     * @return boolean - 连接是否有效
     */
    @Override
    public boolean testConnectByMetaData()
    {
        try
        {
            if(!this.isConnect || this.connection==null)
            {
                this.setError("当前并未连接数据库！");
                return false;
            }
            this.connection.getMetaData();
            return true;
        }
        catch(SQLException ex)
        {
            this.close();
            return false;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    testConnectBySql<br>
     * 描述:    使用SQL语句测试验证连接是否有效<br>
     *
     * @param sql - 测试SQL语句
     * @return boolean - 连接是否有效
     */
    @Override
    public boolean testConnectBySql(String sql)
    {
        try
        {
            if(!this.isConnect || this.connection==null)
            {
                this.setError("当前并未连接数据库！");
                return false;
            }
            PreparedStatement prepstmt=this.connection.prepareStatement(sql);
            if(prepstmt==null)
            {
                this.setError("生成可编译的语句执行对象失败！");
                return false;
            }
            prepstmt.execute();
            return true;
        }
        catch(SQLException ex)
        {
            this.close();
            return false;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDatabaseMetaData<br>
     * 描述:    获取数据库信息描述对象<br>
     *
     * @return DatabaseMetaData - 数据库信息描述对象
     */
    @Override
    public DatabaseMetaData getDatabaseMetaData()
    {
        if(!this.isConnect || this.connection==null)
        {
            this.setError("当前并未连接数据库！");
            return null;
        }
        try
        {
            return this.connection.getMetaData();
        }
        catch(SQLException ex)
        {
            this.setError(ex.toString());
            return null;
        }
    }

    /**
     * 更改器(受保护的)<br>
     * 目标：   error
     *
     * @param error - 错误信息
     */
    protected void setError(String error)
    {
        this.error=error;
    }

    /**
     * 更改器(受保护的)<br>
     * 目标：   isConnect
     *
     * @param isConn - boolean
     */
    protected void setIsConnect(boolean isConn)
    {
        this.isConnect=isConn;
    }

    /**
     * 更改器(受保护的)<br>
     * 目标：   connection
     *
     * @param conn - 数据库连接
     */
    protected void setConnection(Connection conn)
    {
        this.connection=conn;
    }

    /**
     * 访问器(受保护的)<br>
     * 目标：   loginname<br>
     *
     * @return String - 登录名
     */
    protected String getLoginname()
    {
        return this.loginname;
    }

    /**
     * 更改器(受保护的)<br>
     * 目标：   loginname
     *
     * @param name - 登录名
     */
    protected void setLoginname(String name)
    {
        this.loginname=name;
    }

    /**
     * 访问器(受保护的)<br>
     * 目标：   password<br>
     *
     * @return String - 登录密码
     */
    protected String getPassword()
    {
        return this.password;
    }

    /**
     * 更改器(受保护的)<br>
     * 目标：   password
     *
     * @param pass - 登录密码
     */
    protected void setPassword(String pass)
    {
        this.password=pass;
    }
}
