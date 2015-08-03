package pb.generator.template;

import pb.data.EntityTransformer;
import pb.generator.meta.MetaData;
import pb.text.DateProcessor;

/**
 * 管理类接口模板生成器
 *
 * @author maqiang
 */
public class FacadeLocalTemplate implements Template
{
    /*静态属性（受保护）
     *  名称：TEMPLATE_CLASS
     *  描述：类信息模板
     */
    protected static final String TEMPLATE_CLASS=""
            +"/**\n"
            +" * 数据表映射类数据管理接口——TABLE_COMMENT\n"
            +" *@author ProteanBear\r\n"
            +" *@version 1.00 DATE_CURRENT\n"
            +" */\n"
            +"public interface TABLE_NAMEFacadeLocal extends AbstractFacadeLocal<TABLE_NAME>\n"
            +"{\n"
            +"}";

    protected static final String KEY_TABLECOMMENT="TABLE_COMMENT";
    protected static final String KEY_DATECURRENT ="DATE_CURRENT";
    protected static final String KEY_TABLENAME   ="TABLE_NAME";

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
        return EntityTransformer.generateObjNameFromTableName(metaData.getName(),true)+"FacadeLocal.java";
    }
}
