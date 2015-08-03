package pb.generator.template;

import pb.generator.meta.MetaData;

/**
 * 数据相关类模板生成接口
 *
 * @author maqiang
 */
public interface Template
{
    /*
     *  描述：根据给定的内容生成替换成为相应的内容
     *@param metaData - 数据库信息结构对象
     *@return String - 替换后的内容
     */
    String contentForMetaData(MetaData metaData);

    /*
     *  描述：根据给定的内容生成文件的名称
     *@param metaData - 数据库信息结构对象
     *@return String - 文件名
     */
    String generateFileName(MetaData metaData);
}
