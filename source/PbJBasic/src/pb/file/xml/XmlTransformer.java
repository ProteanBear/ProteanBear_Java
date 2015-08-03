package pb.file.xml;

import java.util.Map;

/**
 * XML信息转换器接口。<br>
 * 声明了XML转换的一些相关方法。<br>
 * 1.01 —— 在输出时增加总页数属性。<br>
 * 1.00 —— 声明了基本的转换方法。
 *
 * @author proteanBear(马强)
 * @version 1.01 2011/11/07
 */
public interface XmlTransformer
{
    /**
     * 描述:    转换为xml字符串<br>
     *
     * @return String - xml字符串
     */
    String transToXmlString();

    /**
     * 描述:    转换为xml字符串<br>
     *
     * @param pageSize  - 一页数据量
     * @param page      - 页码
     * @param total     - 总数
     * @param totalPage - 总页数
     * @return String - xml字符串
     */
    String transToXmlString(int pageSize,int page,int total,int totalPage);

    /**
     * 描述:    转换为xml字符串<br>
     *
     * @param attributes - 根目录属性
     * @return String - xml字符串
     */
    String transToXmlString(Map<String,String> attributes);

    /**
     * 描述:    转换为xml字符串<br>
     *
     * @param attributes - 根目录属性
     * @param isAttri
     * @return String - xml字符串
     */
    String transToXmlString(Map<String,String> attributes,boolean isAttri);

    /**
     * 访问器（公共）<br>
     * 目标：   rootname<br>
     *
     * @return String - 根名称
     */
    String getRootname();

    /**
     * 更改器（公共）<br>
     * 目标：   rootname
     *
     * @param rootname - 根名称
     */
    void setRootname(String rootname);

    /**
     * 访问器（公共）<br>
     * 目标：   rowname<br>
     *
     * @return String - 一行数据标签
     */
    String getRowname();

    /**
     * 更改器（公共）<br>
     * 目标：   rowname
     *
     * @param rowname - 一行数据标签
     */
    void setRowname(String rowname);
}
