package pb.generator.template;

import pb.data.EntityTransformer;
import pb.generator.meta.MetaData;
import pb.text.DateProcessor;

/**
 * 数据表服务接口类模板生成器
 *
 * @author maqiang
 */
public class ServletTemplate implements Template
{
    /*静态属性（受保护）
     *  名称：TEMPLATE_CLASS
     *  描述：类信息模板
     */
    protected static final String TEMPLATE_CLASS=""
            +"/**\n"
            +" * 数据访问HTTP接口——TABLE_COMMENT\n"
            +" *@author ProteanBear\r\n"
            +" *@version 1.00 DATE_CURRENT\n"
            +" */\n"
            +"public class TABLE_NAMEServlet extends AbstractServlet \n"
            +"{\n"
            +"    /**方法（受保护）\n"
            +"     * 名称:initDataAction\n"
            +"     * 描述:初始化数据应用层接口\n"
            +"     */\n"
            +"    @Override protected void initDataAction()\n"
            +"    {\n"
            +"        this.dataAction=new TABLE_NAMEAction(this.connector);\n"
            +"    }\n"
            +"\n"
            +"    /**方法（受保护）\n"
            +"     * 名称:getLimitPluginId\n"
            +"     * 描述:获取当前功能对应的功能插件标识\n"
            +"     * @return int - 创建模式的权限编码\n"
            +"     */\n"
            +"    @Override protected String getLimitPluginId()\n"
            +"    {\n"
            +"        return \"TABLE_NAME_UPPER\";\n"
            +"    }\n"
            +"}";

    protected static final String KEY_TABLECOMMENT="TABLE_COMMENT";
    protected static final String KEY_DATECURRENT="DATE_CURRENT";
    protected static final String KEY_TABLENAME="TABLE_NAME";
    protected static final String KEY_TABLEPRIMARY="TABLE_PRIMARYKEY";
    protected static final String KEY_TABLENAMEUPPER="TABLE_NAME_UPPER";

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
                    TEMPLATE_CLASS
                            .replaceAll(KEY_TABLENAMEUPPER,metaData.getName().toUpperCase())
                            .replaceAll(
                                    KEY_TABLENAME,
                                    EntityTransformer.generateObjNameFromTableName(metaData.getName(),true)
                            )
                            .replaceAll(KEY_TABLECOMMENT,metaData.getComment())
                            .replaceAll(KEY_DATECURRENT,(new DateProcessor("yyyy-MM-dd")).getCurrent())
                            .replaceAll(
                                    KEY_TABLEPRIMARY,
                                    EntityTransformer.generateObjNameFromTableName(metaData.getPrimaryKey(),true)
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
        return EntityTransformer.generateObjNameFromTableName(metaData.getName(),true)+"Servlet.java";
    }
}