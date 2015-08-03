package pb.system.limit.entity;

/**
 * 数据表映射类——用户操作日志表。<br>
 * 记录管理人员在客户端操作的日志信息。<br>
 *
 * @author jia_stop
 * @version 1.00 2012/03/14
 */
public class SystemLogUser extends SystemLogUserCache
{
    /**
     * 域(受保护)<br>
     * 名称:    partitionNum<br>
     * 描述:    分区标号。每一分区存储一天的数据。<br>
     */
    protected Integer partitionNum;

    /**
     * 访问器<br>
     * 目标：   partitionNum<br>
     *
     * @return String - 分区标号
     */
    public Integer getPartitionNum()
    {
        return partitionNum;
    }

    /**
     * 更改器<br>
     * 目标：   partitionNum<br>
     *
     * @param partitionNum - 分区标号
     */
    public void setPartitionNum(Integer partitionNum)
    {
        this.partitionNum=partitionNum;
    }

    /**
     * 更改器<br>
     * 目标：   partitionNum<br>
     *
     * @param partitionNum - 分区标号
     */
    public void setPartitionNum(String partitionNum)
    {
        this.partitionNum=(partitionNum==null || "".equals(partitionNum.trim()))?
                null:Integer.parseInt(partitionNum);
    }
}
