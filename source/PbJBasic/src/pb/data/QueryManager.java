package pb.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 本Java类是用于封装对数据表进行操作的方法，支持批量操作<br>
 * 2.1  - 去除执行命令后返回SqlResult对象，改为返回ResultSet对象；增加新方法，返回SqlResult类型
 * 2.01 - 新增对Oracle和mysql分页的设置
 *
 * @author proteanBear(马强)
 * @version 2.1 2010/09/26
 */
public class QueryManager
{
    /**
     * 域(私有)<br>
     * 名称:    conn<br>
     * 描述:    数据库连接器<br>
     */
    private Connector connecter;

    /**
     * 域(私有)<br>
     * 名称:    error<br>
     * 描述:    记录错误信息<br>
     */
    private String error;

    /**
     * 域(私有)<br>
     * 名称:    statement<br>
     * 描述:    静态 SQL 语句对象<br>
     */
    private Statement statement;

    /**
     * 域(私有)<br>
     * 名称:    prepstmt<br>
     * 描述:    预编译的 SQL 语句的对象<br>
     */
    private PreparedStatement prepstmt;

    /**
     * 域(私有)<br>
     * 名称:    resultset<br>
     * 描述:    数据结果集对象<br>
     */
    private ResultSet resultset;

    /**
     * 域(私有)<br>
     * 名称:    isBatch<br>
     * 描述:    是否执行批量操作<br>
     */
    private boolean isBatch;

    /**
     * 域(私有)<br>
     * 名称:    maxResults<br>
     * 描述:    返回结果的数量最大值,为0表示全部<br>
     */
    private int maxResults;

    /**
     * 域(私有)<br>
     * 名称:    firstResult<br>
     * 描述:    结果数据开始位置<br>
     */
    private int firstResult;

    /**
     * 域(私有)<br>
     * 名称:    oracleSelectUseRownum<br>
     * 描述:    oracle分页模板<br>
     */
    private String oracleSelectUseRownum;

    /**
     * 域(私有)<br>
     * 名称:    isUseOraclePageMethod<br>
     * 描述:    是否使用了Oracle的分页方法<br>
     */
    private boolean isUseOraclePageMethod;

    /**
     * 构造函数（无参数）<br>
     */
    public QueryManager()
    {
        this.isBatch=false;
        this.maxResults=0;
        this.firstResult=0;
        this.oracleSelectUseRownum=
                "select * from "
                        +"("
                        +"select A.*,ROWNUM RN "
                        +"from ({$ORACLESQL}) A "
                        +"where ROWNUM<={$MAX}"
                        +") "
                        +"where RN>={$FIRST}";
        this.isUseOraclePageMethod=false;
    }

    /**
     * 构造函数（一个参数）<br>
     * 描述：   指定数据库连接器
     *
     * @param connecter - 数据库连接器
     */
    public QueryManager(Connector connecter)
    {
        this();
        this.setConnecter(connecter);
    }

    /**
     * 重载析构方法（受保护的）<br>
     * 描述:    重载析构方法，在对象清理前调用关闭连接函数<br>
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable
    {
        this.closeAll();
        super.finalize();
    }

    /**
     * 方法（私有）<br>
     * 名称:    setPreparedStatementParams<br>
     * 描述:    遍历可变参数params,设置sql中的参数替换<br>
     *
     * @param params - Object类型的可变参数
     * @throws java.sql.SQLException - 数据库未连接或出现设置参数错误时，抛出SQLException异常
     */
    private void setPreparedStatementParams(Object... params)
            throws SQLException
    {
        int i=1;
        for(Object param : params)
        {
            this.prepstmt.setObject(i,param);
            i++;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    executeQuery<br>
     * 描述:    用于SELECT查询语句的结果集返回<br>
     *
     * @param sql - SQL语句
     * @return ResultSet - 结果集对象
     */
    public ResultSet executeQuery(String sql)
    {
        //关闭上一条sql语句对象
        this.close();
        //检查数据库是否连接
        if(!this.connecter.isConnected()) this.connecter.connect();
        if(!this.connecter.isConnected())
        {
            this.setError(this.connecter.getError());
            return null;
        }

        //执行语句并生成结果
        try
        {
            sql=this.setMaxRowInDifferentDatabase(sql);
            //创建可滚动并只读的Statement对象
            this.statement=this.connecter.getConnection().createStatement
                    (ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            if(this.statement==null) return null;
            this.resultset=this.statement.executeQuery(sql);
            this.isUseOraclePageMethod=false;
            if(this.resultset==null) this.setError("结果集为null");
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
            this.resultset=null;
        }

        return this.resultset;
    }

    /**
     * 方法（公共）<br>
     * 名称:    executeQuery<br>
     * 描述:    用于SELECT查询语句的结果集返回,并支持sql中的参数设置<br>
     *
     * @param sql    - SQL语句
     * @param params - 使用java可变参数，用于替换的sql中的参数
     * @return ResultSet - 结果集对象
     */
    public ResultSet executeQuery(String sql,Object... params)
    {
        //关闭上一条sql语句对象
        this.close();
        //检查数据库是否连接
        if(!this.connecter.isConnected()) this.connecter.connect();
        if(!this.connecter.isConnected())
        {
            this.setError(this.connecter.getError());
            return null;
        }

        //执行语句并生成结果
        try
        {
            //创建可滚动并只读的prepareStatement对象
            sql=this.setMaxRowInDifferentDatabase(sql);
            this.prepstmt=this.connecter.getConnection().prepareStatement
                    (sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            if(this.prepstmt==null) return null;
            if(this.maxResults!=0) this.prepstmt.setMaxRows(this.maxResults);
            //遍历可变参数params,设置sql中的参数替换
            this.setPreparedStatementParams(params);
            this.resultset=this.prepstmt.executeQuery();
            this.isUseOraclePageMethod=false;
            if(this.resultset==null) this.setError("结果集为null");
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
            this.resultset=null;
        }

        return this.resultset;
    }

    /**
     * 方法（公共）<br>
     * 名称:    executeQuery<br>
     * 描述:    用于SELECT查询语句的自定义结果集返回<br>
     *
     * @param sql - SQL语句
     * @return SqlResult - 自定义结果集对象
     * @throws java.sql.SQLException 异常：
     */
    public SqlResult executeQueryReturnSqlResult(String sql) throws SQLException
    {
        boolean isOracle=this.isUseOraclePageMethod;
        return SqlResult.createSqlResult(this.executeQuery(sql),isOracle);
    }

    /**
     * 方法（公共）<br>
     * 名称:    executeQuery<br>
     * 描述:    用于SELECT查询语句的自定义结果集返回,并支持sql中的参数设置<br>
     *
     * @param sql    - SQL语句
     * @param params - 使用java可变参数，用于替换的sql中的参数
     * @return SqlResult - 自定义结果集对象
     * @throws java.sql.SQLException 异常：
     */
    public SqlResult executeQueryReturnSqlResult(String sql,Object... params) throws SQLException
    {
        boolean isOracle=this.isUseOraclePageMethod;
        return SqlResult.createSqlResult(this.executeQuery(sql,params),isOracle);
    }

    /**
     * 方法（公共）<br>
     * 名称:    executeUpdate<br>
     * 描述:    用于INSERT、UPDATE、DELETE修改语句<br>
     *
     * @param sql - SQL语句
     * @return boolean - 是否成功
     */
    public boolean executeUpdate(String sql)
    {
        boolean success=false;

        //关闭上一条sql语句对象
        this.close();
        //检查数据库是否连接
        if(!this.connecter.isConnected()) this.connecter.connect();

        //执行语句并生成结果
        try
        {
            //创建可滚动并只读的Statement对象
            this.statement=this.connecter.getConnection().createStatement();
            if(this.statement==null) return success;
            this.statement.executeUpdate(sql);
            success=true;
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
            success=false;
        }

        return success;
    }

    /**
     * 方法（公共）<br>
     * 名称:    executeUpdate<br>
     * 描述:    用于INSERT、UPDATE、DELETE修改语句,并支持sql中的参数设置<br>
     *
     * @param sql    - SQL语句
     * @param params - 使用java可变参数，用于替换的sql中的参数
     * @return boolean - 是否成功
     */
    public boolean executeUpdate(String sql,Object... params)
    {
        boolean success=false;

        //关闭上一条sql语句对象
        this.close();
        //检查数据库是否连接
        if(!this.connecter.isConnected()) this.connecter.connect();

        //执行语句并生成结果
        try
        {
            //创建可滚动并只读的prepareStatement对象
            this.prepstmt=this.connecter.getConnection().prepareStatement(sql);
            if(this.prepstmt==null) return success;
            //遍历可变参数params,设置sql中的参数替换
            this.setPreparedStatementParams(params);
            this.prepstmt.executeUpdate();
            success=true;
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
            success=false;
        }

        return success;
    }

    /**
     * 方法（公共）<br>
     * 名称:    executeUpdateAndReturn<br>
     * 描述:    用于INSERT语句,支持sql中的参数设置并返回新自动生成的ID<br>
     *
     * @param sql    - SQL语句
     * @param params - 使用java可变参数，用于替换的sql中的参数
     * @return boolean - 是否成功
     */
    public Integer executeUpdateAndReturn(String sql,Object... params)
    {
        Integer result=0;

        //关闭上一条sql语句对象
        this.close();
        //检查数据库是否连接
        if(!this.connecter.isConnected()) this.connecter.connect();

        //执行语句并生成结果
        try
        {
            //创建可滚动并只读的prepareStatement对象
            this.prepstmt=this.connecter.getConnection().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            if(this.prepstmt==null) return result;
            //遍历可变参数params,设置sql中的参数替换
            this.connecter.getConnection().setAutoCommit(false);
            this.setPreparedStatementParams(params);
            this.prepstmt.executeUpdate();
            //获取新自动生成的主键
            ResultSet rs = this.prepstmt.getGeneratedKeys();
            if(rs.next())   result=new Integer(rs.getObject(1)+"");
            this.connecter.getConnection().commit();
            this.connecter.getConnection().setAutoCommit(true);
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
            result=0;
        }

        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    addBatch<br>
     * 描述:    用于添加INSERT、UPDATE、DELETE修改语句的批量操作<br>
     *
     * @param sql - SQL语句
     * @return QueryManager - 返回对象本身，便于连续调用
     */
    public QueryManager addBatch(String sql)
    {
        //未增加过批量操作，则关闭以前的操作对象并建立新的Statement对象
        try
        {
            if(!this.isBatch)
            {
                this.close();
                this.statement=this.connecter.getConnection().createStatement();
                if(this.statement!=null) this.isBatch=true;
            }
            //增加新的操作
            this.statement.addBatch(sql);
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
        }
        finally
        {
            return this;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    addBatch<br>
     * 描述:    用于添加INSERT、UPDATE、DELETE修改语句的批量操作,并支持sql中的参数设置<br>
     *
     * @param sql    - SQL语句
     * @param params - 使用java可变参数，用于替换的sql中的参数
     * @return QueryManager - 返回对象本身，便于连续调用
     */
    public QueryManager addBatch(String sql,Object... params)
    {
        //未增加过批量操作，则关闭以前的操作对象并建立新的Statement对象
        try
        {
            if(!this.isBatch)
            {
                this.close();
                this.prepstmt=this.connecter.getConnection().prepareStatement(sql);
                if(this.statement!=null) this.isBatch=true;
            }
            //增加新的操作
            this.setPreparedStatementParams(params);
            this.statement.addBatch(sql);
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
        }
        finally
        {
            return this;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    executeBatch<br>
     * 描述:    本方法用于执行语句的批量操作<br>
     *
     * @return boolean - 是否成功
     */
    public boolean executeBatch()
    {
        boolean success=false;
        int[] result=new int[]{};
        try
        {
            //判断是否添加了批量操作
            if(!this.isBatch)
            {
                this.setError("未添加批量操作");
                this.close();
                return success;
            }
            //执行批量操作并清理
            if(this.statement!=null)
            {
                result=this.statement.executeBatch();
                this.statement.clearBatch();
            }
            if(this.prepstmt!=null)
            {
                result=this.prepstmt.executeBatch();
                this.prepstmt.clearBatch();
            }
            //解析结果
            String tempinfor="";
            for(int i=0;i<result.length;i++)
            {
                tempinfor=(result[i]>0)?"":("语句"+i+"影响的行数为0");
                success=(result[i]>0);
                if(!success)
                {
                    this.setError(tempinfor);
                    break;
                }
            }
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
            success=false;
        }
        finally
        {
            this.close();
            this.isBatch=false;
        }
        return success;
    }

    /**
     * 方法（公共）<br>
     * 名称:    transactionBegin<br>
     * 描述:    事务处理开始<br>
     *
     * @return boolean - 是否成功
     */
    public boolean transactionBegin()
    {
        if(this.connecter.getConnection()==null)
        {
            this.setError("数据库连接对象为null");
            return false;
        }
        if(!this.connecter.isConnected()) this.connecter.connect();
        try
        {
            this.connecter.getConnection().setAutoCommit(false);
            return true;
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
            return false;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    transactionRollBack<br>
     * 描述:    事务处理回滚<br>
     *
     * @return boolean - 是否成功
     */
    public boolean transactionRollBack()
    {
        if(this.connecter.getConnection()==null)
        {
            this.setError("数据库连接对象为null");
            return false;
        }
        if(!this.connecter.isConnected()) this.connecter.connect();
        try
        {
            this.connecter.getConnection().rollback();
            return true;
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
            return false;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    transactionCommit<br>
     * 描述:    事务处理提交<br>
     *
     * @return boolean - 是否成功
     */
    public boolean transactionCommit()
    {
        if(this.connecter.getConnection()==null)
        {
            this.setError("数据库连接对象为null");
            return false;
        }
        if(!this.connecter.isConnected()) this.connecter.connect();
        try
        {
            this.connecter.getConnection().commit();
            return true;
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
            return false;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    setMaxResults<br>
     * 描述:    设置最大获取的结果数<br>
     *
     * @param max - 最大值
     * @return QueryManager - 返回对象本身
     */
    public QueryManager setMaxResults(int max)
    {
        this.maxResults=max;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setMaxResults<br>
     * 描述:    设置结果集开始位置<br>
     *
     * @param start - 起始位置
     * @return QueryManager - 返回对象本身
     */
    public QueryManager setFirstResults(int start)
    {
        this.firstResult=start;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    close<br>
     * 描述:    用于SELECT语句查询、使用读取数据后，关闭结果集<br>
     */
    public void close()
    {
        try
        {
            if(this.resultset!=null) this.resultset.close();
            if(this.statement!=null) this.statement.close();
            if(this.prepstmt!=null) this.prepstmt.close();
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
        }
        finally
        {
            this.resultset=null;
            this.statement=null;
            this.prepstmt=null;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    closeAll<br>
     * 描述:    用于SELECT语句查询、使用读取数据后，关闭结果集并关闭数据库连接<br>
     */
    public void closeAll()
    {
        this.close();
        this.connecter.close();
    }

    /**
     * 访问器(公共)<br>
     * 目标：   connecter<br>
     *
     * @return Connector - 获取当前连接器
     */
    public Connector getConnecter()
    {
        return connecter;
    }

    /**
     * 更改器(公共、不可继承)<br>
     * 目标：   connecter<br>
     *
     * @param connecter - 指定连接器
     */
    public final void setConnecter(Connector connecter)
    {
        this.connecter=connecter;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   error<br>
     *
     * @return String - 数据库连接字符串
     */
    public String getError()
    {
        return this.error;
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
     * 方法（私有）<br>
     * 名称:    setMaxRowInDifferentDatabase<br>
     * 描述:    针对不同的数据库设置分页方式<br>
     *
     * @param sql - SQL语句
     */
    private String setMaxRowInDifferentDatabase(String sql)
            throws SQLException
    {
        if(this.maxResults!=0)
        {
            String name=this.connecter.getDatabaseProductName();
            switch(name)
            {
                case "ORACLE":
                    sql=this.oracleSelectUseRownum.replace("{$ORACLESQL}",sql);
                    sql=sql.replace("{$MAX}",this.maxResults+"");
                    sql=sql.replace("{$FIRST}",(this.firstResult+1)+"");
                    this.isUseOraclePageMethod=true;
                    this.maxResults=0;
                    this.firstResult=0;
                    break;
                case "MYSQL":
                    sql+=" LIMIT "+this.firstResult+","+(this.maxResults-this.firstResult);
                    this.maxResults=0;
                    this.firstResult=0;
                    break;
            }
        }
        return sql;
    }
}
