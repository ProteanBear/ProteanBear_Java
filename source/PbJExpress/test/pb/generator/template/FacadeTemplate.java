package pb.generator.template;

import pb.data.EntityTransformer;
import pb.generator.meta.MetaData;
import pb.text.DateProcessor;

/**
 * 管理类实现类模板生成器
 *
 * @author maqiang
 */
public class FacadeTemplate implements Template
{
    /*静态属性（受保护）
     *  名称：TEMPLATE_CLASS
     *  描述：类信息模板
     */
    protected static final String TEMPLATE_CLASS=""
            +"/**\n"
            +" * 数据表映射类数据管理类——TABLE_COMMENT\n"
            +" *@author ProteanBear\n"
            +" *@version 1.00 DATE_CURRENT\n"
            +" */\n"
            +"public class TABLE_NAMEFacade \n"
            +"\t\t\textends AbstractEntityManager<TABLE_NAME> \n"
            +"\t\t\timplements TABLE_NAMEFacadeLocal\n"
            +"{\n"
            +"    /**构造函数\n"
            +"     * @param connector - 指定数据连接器\n"
            +"     */\n"
            +"    public TABLE_NAMEFacade(Connector connector)\n"
            +"    {\n"
            +"        super(connector,TABLE_NAME.class);\n"
            +"    }\n"
            +"\n"
            +"    /**方法（受保护）\n"
            +"     * 名称:    getPrimaryKeyName\n"
            +"     * 描述:    获取数据表主键字段名称\n"
            +"     * @return String - 获取主键名称\n"
            +"     */\n"
            +"    @Override public String getPrimaryKeyName()\n"
            +"    {\n"
            +"        return \"TABLE_PRIMARYKEY\";\n"
            +"    }\n"
            +"}";

    protected static final String KEY_TABLECOMMENT="TABLE_COMMENT";
    protected static final String KEY_DATECURRENT ="DATE_CURRENT";
    protected static final String KEY_TABLENAME   ="TABLE_NAME";
    protected static final String KEY_TABLEPRIMARY="TABLE_PRIMARYKEY";

    /*方法（公共）
     *  名称：contentForMetaData
     *  描述：根据给定的内容生成替换成为相应的内容
     *@param metaData - 数据库信息结构对象
     *@param tableName - 指定数据表的名称
     *@return String - 替换后的内容
     */
    @Override
    public String contentForMetaData(MetaData metaData)
    {
        StringBuilder result=new StringBuilder();

        if(metaData!=null)
        {
            result.append(
                    TEMPLATE_CLASS.replaceAll(
                            KEY_TABLENAME,
                            EntityTransformer.generateObjNameFromTableName(metaData.getName(),true)
                    )
                            .replaceAll(KEY_TABLECOMMENT,metaData.getComment())
                            .replaceAll(KEY_DATECURRENT,(new DateProcessor("yyyy-MM-dd")).getCurrent())
                            .replaceAll(
                                    KEY_TABLEPRIMARY,
                                    EntityTransformer.generateObjNameFromTableName(metaData.getPrimaryKey(),false)
                            )
            );
        }

        return result.toString();
    }

    /*方法（公共）
     *  名称：generateFileName
     *  描述：根据给定的内容生成文件的名称
     *@param metaData - 数据库信息结构对象
     *@return String - 文件名
     */
    @Override
    public String generateFileName(MetaData metaData)
    {
        return EntityTransformer.generateObjNameFromTableName(metaData.getName(),true)+"Facade.java";
    }
}
