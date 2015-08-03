package pb.generator.template;

import pb.data.EntityTransformer;
import pb.generator.meta.MetaData;
import pb.generator.meta.TableMetaData;
import pb.text.DateProcessor;

/**
 * 实体类模板生成器
 *
 * @author maqiang
 */
public class EntityTemplate implements Template
{
    /*静态属性（受保护）
     *  名称：TEMPLATE_CLASS
     *  描述：类信息模板
     */
    protected static final String TEMPLATE_CLASS=""
            +"/**\n"
            +" * 数据表映射类——TABLE_COMMENT\n"
            +" *@author ProteanBear\r\n"
            +" *@version 1.00 DATE_CURRENT\n"
            +" */\n"
            +"public class TABLE_NAME\n"
            +"{\n"
            +"CLASS_CONTENT"
            +"}";

    /*静态属性（受保护）
     *  名称：TEMPLATE_ATTRIBUTE
     *  描述：属性信息模板
     */
    protected static final String TEMPLATE_ATTRIBUTE=""
            +"    /**域(受保护)\n"
            +"     * 名称: ATTRIBUTE_NAME\n"
            +"     * 描述: ATTRIBUTE_COMMENT\n"
            +"     */\n"
            +"    protected ATTRIBUTE_TYPE ATTRIBUTE_NAME;\n"
            +"\n";

    /*静态属性（受保护）
     *  名称：TEMPLATE_ATTR_GET
     *  描述：访问器信息模板
     */
    protected static final String TEMPLATE_ATTR_GET=""
            +"    /**访问器\n"
            +"     * 目标: ATTRIBUTE_NAME\n"
            +"     * @return ATTRIBUTE_TYPE - ATTRIBUTE_COMMENT\n"
            +"     */\n"
            +"    public ATTRIBUTE_TYPE ATTRIBUTE_GETMETHOD(){return ATTRIBUTE_NAME;}\n"
            +"\n";

    /*静态属性（受保护）
     *  名称：TEMPLATE_ATTR_SET
     *  描述：更改器信息模板
     */
    protected static final String TEMPLATE_ATTR_SET=""
            +"    /**更改器\n"
            +"     * 目标: ATTRIBUTE_NAME\n"
            +"     * @param ATTRIBUTE_NAME - ATTRIBUTE_COMMENT\n"
            +"     */\n"
            +"    public void ATTRIBUTE_SETMETHOD(ATTRIBUTE_TYPE ATTRIBUTE_NAME)\n"
            +"    {\n"
            +"        this.ATTRIBUTE_NAME=ATTRIBUTE_NAME;\n"
            +"    }\n"
            +"\n";

    /*静态属性（受保护）
     *  名称：TEMPLATE_ATTR_SETSTRING
     *  描述：更改器信息模板
     */
    protected static final String TEMPLATE_ATTR_SETSTRING=""
            +"    /**更改器\n"
            +"     * 目标: ATTRIBUTE_NAME\n"
            +"     * @param ATTRIBUTE_NAME - ATTRIBUTE_COMMENT\n"
            +"     */\n"
            +"    public void ATTRIBUTE_SETMETHOD(String ATTRIBUTE_NAME)\n"
            +"    {\n"
            +"        this.ATTRIBUTE_NAME=new Integer(ATTRIBUTE_NAME);\n"
            +"    }\n"
            +"\n";

    protected static final String KEY_TABLECOMMENT ="TABLE_COMMENT";
    protected static final String KEY_DATECURRENT  ="DATE_CURRENT";
    protected static final String KEY_TABLENAME    ="TABLE_NAME";
    protected static final String KEY_CLASSCONTENT ="CLASS_CONTENT";
    protected static final String KEY_ATTRNAME     ="ATTRIBUTE_NAME";
    protected static final String KEY_ATTRCOMMENT  ="ATTRIBUTE_COMMENT";
    protected static final String KEY_ATTRTYPE     ="ATTRIBUTE_TYPE";
    protected static final String KEY_ATTRGETMOTHOD="ATTRIBUTE_GETMETHOD";
    protected static final String KEY_ATTRSETMOTHOD="ATTRIBUTE_SETMETHOD";

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
            StringBuilder content=new StringBuilder();
            for(TableMetaData tableMetaData : metaData.getTableMetaDatas())
            {
                content.append(this.replaceAllDataParam(TEMPLATE_ATTRIBUTE,metaData,tableMetaData))
                        .append(this.replaceAllDataParam(TEMPLATE_ATTR_GET,metaData,tableMetaData))
                        .append(this.replaceAllDataParam(TEMPLATE_ATTR_SET,metaData,tableMetaData));
                if("int".equals(tableMetaData.getType()))
                {
                    content.append(this.replaceAllDataParam(TEMPLATE_ATTR_SETSTRING,metaData,tableMetaData));
                }
            }

            result.append(
                    this.replaceAllDataParam(TEMPLATE_CLASS,metaData,null)
                            .replaceAll(KEY_CLASSCONTENT,content.toString())
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
        return EntityTransformer.generateObjNameFromTableName(metaData.getName(),true)+".java";
    }

    /*方法（私有）
     *  名称：replaceAllDataParam
     *  描述：替换全部数据相关变量
     *@param content - 替换内容
     *@param metaData - 
     *@param tableMetaData - 
     *@return String - 替换后内容
     */
    private String replaceAllDataParam
    (String content,MetaData metaData,TableMetaData tableMetaData)
    {
        content=content.replaceAll(KEY_DATECURRENT,(new DateProcessor("yyyy-MM-dd")).getCurrent());
        if(metaData!=null)
        {
            content=content.replaceAll(KEY_TABLECOMMENT,metaData.getComment())
                    .replaceAll(KEY_TABLENAME,EntityTransformer.generateObjNameFromTableName(metaData.getName(),true));
        }
        if(tableMetaData!=null)
        {
            String type=("varchar".equals(tableMetaData.getType())
                    || "char".equals(tableMetaData.getType())
                    || "text".equals(tableMetaData.getType()))
                    ?"String":tableMetaData.getType();
            String objName=EntityTransformer.generateObjNameFromTableName(tableMetaData.getName(),false);
            content=content.replaceAll(KEY_ATTRNAME,objName)
                    .replaceAll(KEY_ATTRCOMMENT,tableMetaData.getComment())
                    .replaceAll(KEY_ATTRTYPE,type)
                    .replaceAll(KEY_ATTRGETMOTHOD,EntityTransformer.generateGetMethodName(objName))
                    .replaceAll(KEY_ATTRSETMOTHOD,EntityTransformer.generateSetMethodName(objName))
            ;
        }
        return content;
    }
}
