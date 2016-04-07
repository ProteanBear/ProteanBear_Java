package pb.system.cms.entity;

/**
 * 数据表映射类——
 *@author ProteanBear
 *@version 1.00 2016-04-07
 */
public class BusiCmsTag
{
    /**域(受保护)
     * 名称: custId
     * 描述: 自增主键
     */
    protected int custId;

    /**访问器
     * 目标: custId
     * @return int - 自增主键
     */
    public int getCustId(){return custId;}

    /**更改器
     * 目标: custId
     * @param custId - 自增主键
     */
    public void setCustId(int custId)
    {
        this.custId=custId;
    }

    /**更改器
     * 目标: custId
     * @param custId - 自增主键
     */
    public void setCustId(String custId)
    {
        this.custId=new Integer(custId);
    }

    /**域(受保护)
     * 名称: tagName
     * 描述: 记录标签的显示名称
     */
    protected String tagName;

    /**访问器
     * 目标: tagName
     * @return String - 记录标签的显示名称
     */
    public String getTagName(){return tagName;}

    /**更改器
     * 目标: tagName
     * @param tagName - 记录标签的显示名称
     */
    public void setTagName(String tagName)
    {
        this.tagName=tagName;
    }

    /**域(受保护)
     * 名称: dataRemark
     * 描述: 数据备注信息
     */
    protected String dataRemark;

    /**访问器
     * 目标: dataRemark
     * @return String - 数据备注信息
     */
    public String getDataRemark(){return dataRemark;}

    /**更改器
     * 目标: dataRemark
     * @param dataRemark - 数据备注信息
     */
    public void setDataRemark(String dataRemark)
    {
        this.dataRemark=dataRemark;
    }

    /**域(受保护)
     * 名称: dataDelete
     * 描述: 数据删除标志位
     */
    protected int dataDelete;

    /**访问器
     * 目标: dataDelete
     * @return int - 数据删除标志位
     */
    public int getDataDelete(){return dataDelete;}

    /**更改器
     * 目标: dataDelete
     * @param dataDelete - 数据删除标志位
     */
    public void setDataDelete(int dataDelete)
    {
        this.dataDelete=dataDelete;
    }

    /**更改器
     * 目标: dataDelete
     * @param dataDelete - 数据删除标志位
     */
    public void setDataDelete(String dataDelete)
    {
        this.dataDelete=new Integer(dataDelete);
    }

}