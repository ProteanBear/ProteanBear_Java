package pb.data;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 本Java类用于封装sql语句的执行，而用户只需提供连接信息、使用的数据表（可多表）等信息，其中：<br>
 * select - 可指定输出的字段、筛选条件、排序、分组<br>
 * insert - 可指定设置的字段名、字段值和筛选条件<br>
 * update - 可指定设置的字段名、字段值和筛选条件<br>
 * delete - 可指定筛选条件<br>
 * 2.11 - 修正替换参数处理时count未加参数的Bug<br>
 * 2.1  - 去除执行查询命令后返回SqlResult对象，改为返回ResultSet对象;<br>
 * 去除预存语句的Map<br>
 * 2.02 - 增加支持所有SqlAttribute中的set方法<br>
 * 2.01 - 在getSqlAttribute时改为获取对象的拷贝，修正在程序中修改SqlAttribute对象时预定义的内容改变的问题
 *
 * @author proteanBear(马强)
 * @version 2.11 2011/11/01
 */
public class DataManager
{
    /**---------------------------开始：静态内容------------------------------**/

    /**
     * 静态常量<br>
     * 名称:    CONNECT_TYPE_JDBC<br>
     * 描述:    连接类型-jdbc<br>
     */
    public static final int CONNECT_TYPE_JDBC=0;

    /**
     * 静态常量<br>
     * 名称:    CONNECT_TYPE_RESOURCE<br>
     * 描述:    连接类型-资源调用<br>
     */
    public static final int CONNECT_TYPE_RESOURCE=1;

    /**---------------------------结束：静态内容------------------------------**/

    /**
     * 域(私有)<br>
     * 名称:    attribute<br>
     * 描述:    SqlAttribute对象<br>
     */
    private SqlAttribute attribute;

    /**
     * 域(私有)<br>
     * 名称:    pageSize<br>
     * 描述:    记录每一页显示的数据量,用于分页<br>
     */
    private int pageSize;

    /**
     * 域(私有)<br>
     * 名称:    total<br>
     * 描述:    记录总数据量<br>
     */
    private int total;

    /**
     * 域(私有)<br>
     * 名称:    totalPage<br>
     * 描述:    记录总页数<br>
     */
    private int totalPage;

    /**
     * 域(私有)<br>
     * 名称:    connecter<br>
     * 描述:    数据库连接器<br>
     */
    private Connector connecter;

    /**
     * 域(私有)<br>
     * 名称:    query<br>
     * 描述:    语句执行器<br>
     */
    private QueryManager query;

    /**
     * 域(私有)<br>
     * 名称:    encoding<br>
     * 描述:    字符编码<br>
     */
    private String encoding;

    /**
     * 构造函数（无参数）<br>
     * 描述：   初始化私有域
     */
    private DataManager()
    {
        this.attribute=new SqlAttribute();
        this.pageSize=30;
        this.total=0;
        this.totalPage=0;
        this.encoding="utf-8";
    }

    /**
     * 构造函数（一个参数）<br>
     *
     * @param url - 数据库连接url或者连接资源名称
     */
    public DataManager(String url)
    {
        this(url,CONNECT_TYPE_JDBC);
    }

    /**
     * 构造函数（两个参数）<br>
     *
     * @param url      - 数据库连接url或者连接资源名称
     * @param connType - 数据库连接类型
     */
    public DataManager(String url,int connType)
    {
        this();
        if(CONNECT_TYPE_RESOURCE==connType)
        {
            this.connecter=new SourceConnector(url);
        }
        else
        {
            this.connecter=new JdbcConnector(url);
        }
        this.query=new QueryManager(this.connecter);
    }

    /**
     * 构造函数（三个参数）<br>
     *
     * @param url       - 数据库连接url或者连接资源名称
     * @param loginname - 登录名称
     * @param password  - 登录密码
     */
    public DataManager(String url,String loginname,String password)
    {
        this(url,loginname,password,CONNECT_TYPE_JDBC);
    }

    /**
     * 构造函数（四个参数）<br>
     *
     * @param url       - 数据库连接url或者连接资源名称
     * @param loginname - 登录名称
     * @param password  - 登录密码
     * @param connType  - 连接类型
     */
    public DataManager(String url,String loginname,String password,int connType)
    {
        this();
        if(CONNECT_TYPE_RESOURCE==connType)
        {
            this.connecter=new SourceConnector(url,loginname,password);
        }
        else
        {
            this.connecter=new JdbcConnector(url,loginname,password);
        }
        this.query=new QueryManager(this.connecter);
    }

    /**
     * 构造函数（一个参数）<br>
     *
     * @param connector - 指定数据连接器
     */
    public DataManager(Connector connector)
    {
        this();
        this.connecter=connector;
        this.query=new QueryManager(this.connecter);
    }

    /**
     * 方法（受保护、重载）<br>
     * 名称:    销毁变量时关闭连接<br>
     * 描述:    本方法用于<br>
     *
     * @throws Throwable 异常：
     */
    @Override
    protected void finalize() throws Throwable
    {
        this.close();
        super.finalize();
    }

    /**
     * 方法（公共）<br>
     * 名称:    connect<br>
     * 描述:    连接数据库<br>
     */
    public void connect()
    {
        this.connecter.connect();
        if(this.connecter.isConnected())
        {
            this.executeUpdate(
                    "SET character_set_connection="+this.encoding+","
                            +"character_set_results="+this.encoding+","
                            +"character_set_client=binary"
            );
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    exist<br>
     * 描述:    判断指定的数据是否存在<br>
     *
     * @return boolean - 是否存在
     */
    public boolean exist()
    {
        return (this.count()>0);
    }

    /**
     * 方法（公共）<br>
     * 名称:    exist<br>
     * 描述:    判断指定的数据是否存在<br>
     *
     * @param params - 使用java可变参数，用于替换的sql中的参数
     * @return boolean - 是否存在
     */
    public boolean exist(Object... params)
    {
        return (this.count(params)>0);
    }

    /**
     * 方法（公共）<br>
     * 名称:    count<br>
     * 描述:    获得数据总数<br>
     *
     * @return int - 数据总数
     */
    public int count()
    {
        ResultSet sql=this.query.executeQuery(this.generateSQL("count"));
        if(sql==null) return 0;
        Object count=0;

        try
        {
            if(sql.next()) count=sql.getObject(1);
        }
        catch(SQLException e)
        {
            count=0;
        }

        if(count.getClass().isAssignableFrom(BigDecimal.class))
        {
            return ((BigDecimal)count).intValue();
        }
        if(count.getClass().isAssignableFrom(Long.class))
        {
            return ((Long)count).intValue();
        }
        return (Integer)count;
    }

    /**
     * 方法（公共）<br>
     * 名称:    count<br>
     * 描述:    获得数据总数<br>
     *
     * @param params - 使用java可变参数，用于替换的sql中的参数
     * @return int - 数据总数
     */
    public int count(Object... params)
    {
        ResultSet sql=this.query.executeQuery(this.generateSQL("count"),params);
        if(sql==null) return 0;
        Object count=0;

        try
        {
            if(sql.next()) count=sql.getObject(1);
        }
        catch(SQLException e)
        {
            count=0;
        }

        if(count.getClass().isAssignableFrom(BigDecimal.class))
        {
            return ((BigDecimal)count).intValue();
        }
        if(count.getClass().isAssignableFrom(Long.class))
        {
            return ((Long)count).intValue();
        }
        return (Integer)count;
    }

    /**
     * 方法（公共）<br>
     * 名称:    select<br>
     * 描述:    查询数据<br>
     *
     * @return ResultSet - 结果对象
     */
    public ResultSet select()
    {
        return this.select(1);
    }

    /**
     * 方法（公共）<br>
     * 名称:    select<br>
     * 描述:    查询数据<br>
     *
     * @param params - 使用java可变参数，用于替换的sql中的参数
     * @return ResultSet - 结果对象
     */
    public ResultSet select(Object... params)
    {
        return this.select(1,params);
    }

    /**
     * 方法（公共）<br>
     * 名称:    select<br>
     * 描述:    查询数据<br>
     *
     * @param page - 需要获取的页数
     * @return ResultSet - 结果对象
     */
    public ResultSet select(int page)
    {
        if(!this.connecter.isConnected()) this.connect();
        if(this.pageSize!=0)
        {
            this.total=this.count();
            this.totalPage=this.total/this.pageSize+((this.total%this.pageSize!=0)?1:0);
            page=(page>this.totalPage)?this.totalPage:page;
        }
        int max=page*this.pageSize;
        int start=this.pageSize*(page-1);
        return this.query.setMaxResults(max).setFirstResults(start)
                .executeQuery(this.generateSQL("select"));
    }

    /**
     * 方法（公共）<br>
     * 名称:    select<br>
     * 描述:    查询数据<br>
     *
     * @param page   - 需要获取的页数
     * @param params - 使用java可变参数，用于替换的sql中的参数
     * @return ResultSet - 结果对象
     */
    public ResultSet select(int page,Object... params)
    {
        if(!this.connecter.isConnected()) this.connect();
        if(this.pageSize!=0)
        {
            this.total=this.count(params);
            this.totalPage=this.total/this.pageSize+((this.total%this.pageSize!=0)?1:0);
            //page=(page>this.totalPage)?this.totalPage:page;
        }
        int max=page*this.pageSize;
        int start=this.pageSize*(page-1);
        return this.query.setMaxResults(max).setFirstResults(start)
                .executeQuery(this.generateSQL("select"),params);
    }

    /**
     * 方法（公共）<br>
     * 名称:    insert<br>
     * 描述:    插入数据<br>
     *
     * @return boolean - 是否成功
     */
    public boolean insert()
    {
        return executeUpdate("insert");
    }

    /**
     * 方法（公共）<br>
     * 名称:    insert<br>
     * 描述:    插入数据<br>
     *
     * @param params - 语句中的替换参数
     * @return boolean - 是否成功
     */
    public boolean insert(Object... params)
    {
        return executeUpdate("insert",params);
    }

    /**
     * 方法（公共）<br>
     * 名称:    insertAndReturn<br>
     * 描述:    插入数据并返回自动生成的主键<br>
     *
     * @param params - 语句中的替换参数
     * @return boolean - 是否成功
     */
    public Integer insertAndReturn(Object... params)
    {
        return executeUpdateAndReturn("insert",params);
    }

    /**
     * 方法（公共）<br>
     * 名称:    update<br>
     * 描述:    更新数据<br>
     *
     * @return boolean - 是否成功
     */
    public boolean update()
    {
        return executeUpdate("update");
    }

    /**
     * 方法（公共）<br>
     * 名称:    update<br>
     * 描述:    更新数据<br>
     *
     * @param params - 语句中的替换参数
     * @return boolean - 是否成功
     */
    public boolean update(Object... params)
    {
        return executeUpdate("update",params);
    }

    /**
     * 方法（公共）<br>
     * 名称:    delete<br>
     * 描述:    删除数据<br>
     *
     * @return boolean - 是否成功
     */
    public boolean delete()
    {
        return executeUpdate("delete");
    }

    /**
     * 方法（公共）<br>
     * 名称:    delete<br>
     * 描述:    删除数据<br>
     *
     * @param params - 语句中的替换参数
     * @return boolean - 是否成功
     */
    public boolean delete(Object... params)
    {
        return executeUpdate("delete",params);
    }

    /**
     * 方法（公共）<br>
     * 名称:    transactionBegin<br>
     * 描述:     事务处理开始<br>
     *
     * @return boolean - 是否成功
     */
    public boolean transactionBegin()
    {
        return this.query.transactionBegin();
    }

    /**
     * 方法（公共）<br>
     * 名称:    transactionBegin<br>
     * 描述:     事务处理回滚<br>
     *
     * @return boolean - 是否成功
     */
    public boolean transactionRollBack()
    {
        return this.query.transactionRollBack();
    }

    /**
     * 方法（公共）<br>
     * 名称:    transactionBegin<br>
     * 描述:     事务处理提交<br>
     *
     * @return boolean - 是否成功
     */
    public boolean transactionCommit()
    {
        return this.query.transactionCommit();
    }

    /**
     * 方法（公共）<br>
     * 名称:    delete<br>
     * 名称:    getError<br>
     * 描述:    获取错误信息<br>
     *
     * @return 返回错误信息
     */
    public String getError()
    {
        return this.query.getError();
    }

    /**
     * 方法（公共）<br>
     * 名称:    close<br>
     * 描述:    关闭连接数据库时所有使用的ResultSet、Statement等以及数据库连接<br>
     */
    public void close()
    {
        this.query.closeAll();
    }

    /**
     * 方法（公共）<br>
     * 名称:    getTotalCount<br>
     * 描述:    获得总数<br>
     *
     * @return int - 总数
     */
    public int getTotalCount()
    {
        return this.total;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getPageSize<br>
     * 描述:    获得一页数据数量<br>
     *
     * @return int - 一页数据数量
     */
    public int getPageSize()
    {
        return this.pageSize;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getTotalPage<br>
     * 描述:    获得总页数<br>
     *
     * @return int - 总页数
     */
    public int getTotalPage()
    {
        return this.totalPage;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setPageSize<br>
     * 描述:    设置每一页显示的数据量<br>
     *
     * @param pageSize - 每一页显示的数据量
     * @return DataManager - 对象本身
     */
    public DataManager setPageSize(int pageSize)
    {
        this.pageSize=pageSize;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnNames<br>
     * 描述:    设置字段名称,可变参数<br>
     *
     * @param columnNames - 字段名称
     * @return DataManager - 对象本身
     */
    public DataManager setColumnNames(String... columnNames)
    {
        this.attribute.setColumnNames(columnNames);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnValues<br>
     * 描述:    设置字段值，可变参数<br>
     *
     * @param columnValues - 字段值
     * @return DataManager - 对象本身
     */
    public DataManager setColumnValues(String... columnValues)
    {
        this.attribute.setColumnValues(columnValues);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    addColumnSet<br>
     * 描述:    增加一条字段名值设置，用于update命令中的set name=value<br>
     *
     * @param columnName  - 字段名
     * @param columnValue - 字段值
     * @return DataManager - 对象本身
     */
    public DataManager addColumnSet(String columnName,String columnValue)
    {
        this.attribute.addColumnSet(columnName,columnValue);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnSets<br>
     * 描述:    设置字段名值设置，用于update命令中的set name=value<br>
     *
     * @param columnSets - 设置值
     * @return DataManager - 对象本身
     */
    public DataManager setColumnSets(String... columnSets)
    {
        this.attribute.setColumnSets(columnSets);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    addCondition<br>
     * 描述:    设置筛选条件<br>
     *
     * @param condition - 筛选条件
     * @return DataManager - 对象本身
     */
    public DataManager addCondition(String condition)
    {
        this.attribute.addCondition(condition);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    addGroupBy<br>
     * 描述:    设置group by<br>
     *
     * @param groupBy - 指定分组字段
     * @return DataManager - 对象本身
     */
    public DataManager addGroupBy(String groupBy)
    {
        this.attribute.addGroupBy(groupBy);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    addOrderBy<br>
     * 描述:    设置order by<br>
     *
     * @param orderBy - 指定排序字段，可写入升序或降序
     * @return DataManager - 对象本身
     */
    public DataManager addOrderBy(String orderBy)
    {
        this.attribute.addOrderBy(orderBy);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setTable<br>
     * 描述:    设置使用的数据表名称<br>
     *
     * @param table - 数据表名称
     * @return DataManager - 对象本身
     */
    public DataManager setTable(String table)
    {
        this.attribute.setTable(table);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    addColumnName<br>
     * 描述:    设置字段名称,可变参数<br>
     *
     * @param columnName - 字段名称
     * @return DataManager - 返回对象本身
     */
    public DataManager addColumnName(String columnName)
    {
        this.attribute.addColumnName(columnName);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnNames<br>
     * 描述:    设置字段名称,可变参数<br>
     *
     * @param columnNames - 字段名称
     * @return DataManager - 返回对象本身
     */
    public DataManager setColumnNames(String columnNames)
    {
        this.attribute.setColumnNames(columnNames);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnSets<br>
     * 描述:    设置字段名值设置，用于update命令中的set name=value<br>
     *
     * @param columnSets - 设置值
     * @return DataManager - 返回对象本身
     */
    public DataManager setColumnSets(String columnSets)
    {
        this.attribute.setColumnSets(columnSets);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    addColumnValue<br>
     * 描述:    增加字段值<br>
     *
     * @param columnValue - 字段名称
     * @return DataManager - 返回对象本身
     */
    public DataManager addColumnValue(String columnValue)
    {
        this.attribute.addColumnValue(columnValue);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnValues<br>
     * 描述:    设置字段值<br>
     *
     * @param columnValues - 字段值
     * @return DataManager - 返回对象本身
     */
    public DataManager setColumnValues(String columnValues)
    {
        this.attribute.setColumnValues(columnValues);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setCondition<br>
     * 描述:    设置筛选条件<br>
     *
     * @param condition - 筛选条件
     * @return DataManager - 返回对象本身
     */
    public DataManager setCondition(String condition)
    {
        this.attribute.setCondition(condition);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setGroupBy<br>
     * 描述:    设置group by<br>
     *
     * @param groupBy - 指定分组字段
     * @return DataManager - 返回对象本身
     */
    public DataManager setGroupBy(String groupBy)
    {
        this.attribute.setGroupBy(groupBy);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setOrderBy<br>
     * 描述:    设置order by<br>
     *
     * @param orderBy - 指定排序字段，可写入升序或降序
     * @return DataManager - 返回对象本身
     */
    public final DataManager setOrderBy(String orderBy)
    {
        this.attribute.setOrderBy(orderBy);
        return this;
    }

    /**
     * 方法（私有）<br>
     * 名称:    generateSQL<br>
     * 描述:    替换指定的sql模板中的数据生成使用的sql<br>
     *
     * @param sqlname - sql模板索引名
     * @return String - sql语句
     */
    private String generateSQL(String sqlname)
    {
        return this.attribute.toSql(sqlname);
    }

    /**
     * 方法（私有）<br>
     * 名称:    executeUpdate<br>
     * 描述:    执行语句<br>
     *
     * @param sqlname - sql语句索引值
     * @return boolean
     */
    private boolean executeUpdate(String sqlname)
    {
        if(!this.connecter.isConnected()) this.connect();
        return this.query.executeUpdate(this.generateSQL(sqlname));
    }

    /**
     * 方法（私有）<br>
     * 名称:    executeUpdate<br>
     * 描述:    执行语句<br>
     *
     * @param sqlname - sql语句索引值
     * @param params  - 语句中的替换参数
     * @return boolean
     */
    private boolean executeUpdate(String sqlname,Object... params)
    {
        if(!this.connecter.isConnected()) this.connect();
        return this.query.executeUpdate(this.generateSQL(sqlname),params);
    }

    /**
     * 方法（私有）<br>
     * 名称:    executeUpdateAndReturn<br>
     * 描述:    执行语句<br>
     *
     * @param sqlname - sql语句索引值
     * @param params  - 语句中的替换参数
     * @return boolean
     */
    private Integer executeUpdateAndReturn(String sqlname,Object... params)
    {
        if(!this.connecter.isConnected()) this.connect();
        return this.query.executeUpdateAndReturn(this.generateSQL(sqlname),params);
    }

    /**
     * 方法（私有）<br>
     * 名称:    setAttribute<br>
     * 描述:    设置Sql属性<br>
     *
     * @param attri - SqlAttribute
     * @return DataManager - 对象本身
     */
    public DataManager setAttribute(SqlAttribute attri)
    {
        this.attribute=attri;
        return this;
    }
}