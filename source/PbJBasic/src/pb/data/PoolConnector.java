package pb.data;

/**
 * 本Java类是用于描述数据库连接池专用连接器的接口<br>
 * 2.00 —— 创建连接器忙碌标识
 *
 * @author proteanBear(马强)
 * @version 2.00 2011/06/14
 * @since jdk1.4
 */
public interface PoolConnector extends Connector
{
    /**
     * 访问器（公共）<br>
     * 目标：   url<br>
     *
     * @return String - 数据库连接字符串
     */
    String getUrl();

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   url<br>
     *
     * @param url - 数据库连接字符串
     */
    void setUrl(String url);

    /**
     * 访问器（公共）<br>
     * 目标：   sourceType<br>
     *
     * @return String - 数据资源类型
     */
    String getSourceType();

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   sourceType<br>
     *
     * @param type - 数据资源类型
     */
    void setSourceType(int type);

    /**
     * 访问器（公共）<br>
     * 目标：   isBusy<br>
     *
     * @return boolean - 连接器是否正在使用中
     */
    boolean isBusy();

    /**
     * 更改器（公共）<br>
     * 目标：   setBusy<br>
     *
     * @param busy - 连接器使用标识
     */
    void setBusy(boolean busy);
}
