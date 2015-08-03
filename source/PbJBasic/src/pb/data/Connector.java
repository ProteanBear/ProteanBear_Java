package pb.data;

import java.sql.DatabaseMetaData;

/**
 * 本Java类是用于描述数据库连接器的接口<br>
 * 2.01 —— 增加三种模式的连接有效探测方法（auto-commit、meta-data、sql）；增加数据库信息描述对象（DatabaseMetaData）的获取方法
 *
 * @author proteanBear(马强)
 * @version 2.01 2011/06/14
 * @since jdk1.4
 */
public interface Connector
{
    /**
     * 描述:     连接数据库
     */
    void connect();

    /**
     * 描述:     是否已连接数据库<br>
     *
     * @return boolean - 是否连接
     */
    boolean isConnected();

    /**
     * 描述:     关闭数据库连接<br>
     */
    void close();

    /**
     * 描述:     获取错误信息<br>
     *
     * @return String - 错误信息
     */
    String getError();

    /**
     * 描述:    获取数据库连接对象<br>
     *
     * @return java.sql.Connection - 数据库连接对象
     */
    public java.sql.Connection getConnection();

    /**
     * 描述:     获取当前连接的数据库产品名称<br>
     *
     * @return String - 数据库产品名称
     */
    String getDatabaseProductName();

    /**
     * 描述:    获取数据库信息描述对象<br>
     *
     * @return DatabaseMetaData - 数据库信息描述对象
     */
    DatabaseMetaData getDatabaseMetaData();

    /**
     * 描述:    使用自动提交模式验证连接是否有效，只对部分数据库有效<br>
     *
     * @return boolean - 连接是否有效
     */
    boolean testConnectByAutoCommit();

    /**
     * 描述:    确保对 Connection.getMetaData() 的调用验证连接是否有效<br>
     *
     * @return boolean - 连接是否有效
     */
    boolean testConnectByMetaData();

    /**
     * 描述:    使用数据表测试验证连接是否有效<br>
     *
     * @param sql - 测试SQL语句
     * @return boolean - 连接是否有效
     */
    boolean testConnectBySql(String sql);
}
