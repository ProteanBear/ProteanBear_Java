package pb.generator.meta;

import java.util.List;

/**
 * 数据表信息结构类
 *
 * @author maqiang
 */
public class MetaData
{
    /*属性（私有）
     *  名称：name
     *  描述：数据表名称
     */
    private String name;

    /*属性（私有）
     *  名称：tableComment
     *  描述：数据表注释
     */
    private String comment;

    /*属性（私有）
     *  名称：primaryKey
     *  描述：数据表主键
     */
    private String primaryKey;

    /*属性（私有）
     *  名称：tableMetaDatas
     *  描述：数据表结构
     */
    private List<TableMetaData> tableMetaDatas;

    /*构造函数
     *@param name - 数据表名称
     *@param comment - 数据表注释
     *@param primaryKey - 数据表主键
     *@param tableMeraDatas - 数据表结构
     */
    public MetaData(String name,String comment,String primaryKey,
                    List<TableMetaData> tableMetaDatas)
    {
        this.name=name;
        this.comment=comment;
        this.primaryKey=primaryKey;
        this.tableMetaDatas=tableMetaDatas;
    }

    public String getName()
    {
        return name;
    }

    public String getComment()
    {
        return comment;
    }

    public List<TableMetaData> getTableMetaDatas()
    {
        return tableMetaDatas;
    }

    public String getPrimaryKey()
    {
        return primaryKey;
    }
}
