package pb.generator.meta;

import pb.data.EntityTransformer;

/**
 * 数据表信息结构类
 *
 * @author maqiang
 */
public class TableMetaData
{
    /*属性（私有）
     *  名称：name
     *  描述：表字段名称
     */

    private String name;

    /*属性（私有）
     *  名称：type
     *  描述：表字段类型
     */
    private String type;

    /*属性（私有）
     *  名称：comment
     *  描述：表字段注释
     */
    private String comment;

    /*构造函数
     *@param name - 表字段名称
     *@param type - 表字段类型
     *@param comment - 表字段注释
     */
    public TableMetaData(String name,String type,String comment)
    {
        this.name=name;
        this.type=type.toLowerCase();
        this.comment=comment;
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public String getComment()
    {
        return comment;
    }
}
