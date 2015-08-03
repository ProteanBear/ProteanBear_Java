package pb.text;

import java.util.Map;

/**
 * 本Java接口是用于描述字符串信息转换器
 * 1.01 - 增加总页数属性
 *
 * @author proteanBear(马强)
 * @version 1.01 2011/11/07
 */
public interface StringTransformer
{
    /**
     * 描述:    转换为使用分隔符分隔的字符串<br>
     *
     * @return String - xml字符串
     */
    String transToString();

    /**
     * 描述:    转换为使用分隔符分隔的字符串<br>
     *
     * @param pageSize  - 一页数据量
     * @param page      - 页码
     * @param total     - 总数
     * @param totalPage - 总页数
     * @return String - xml字符串
     */
    String transToString(int pageSize,int page,int total,int totalPage);

    /**
     * 描述:    转换为使用分隔符分隔的字符串<br>
     *
     * @param attributes - 根目录属性
     * @return String - xml字符串
     */
    String transToString(Map<String,String> attributes);
}
