package pb.text;

import java.util.List;
import java.util.Map;

/**
 * 本Java类是用于将制定的内容进行Json字符串构造的方法
 * 1.02 - 增加toJsonByObjectList带isFilterNull参数的方法。<br>
 * 1.01 - 增加总页数属性
 *
 * @author proteanBear(马强)
 * @version 1.02 2012/03/21
 */
public class JsonProductor
{
    /**
     * 静态域(私有)<br>
     * 名称:    success<br>
     * 描述:    成功标示符<br>
     */
    private static String success="success";

    /**
     * 静态域(私有)<br>
     * 名称:    list<br>
     * 描述:    队列标示符<br>
     */
    private static String list="list";

    /**
     * 静态方法（公共）<br>
     * 名称:    getSuccessTag<br>
     * 描述:    获取当前的success成功标示符<br>
     *
     * @return String - 成功标示符
     */
    public static String getSuccessTag()
    {
        return success;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    setSuccessTag<br>
     * 描述:    设置当前的success成功标示符<br>
     *
     * @param tag - 要设置的成功标示符
     * @return JsonProductor - 返回JsonProductor类
     */
    public static Class setSuccessTag(String tag)
    {
        success=tag;
        return JsonProductor.class;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    getListTag<br>
     * 描述:    获取当前的list列表标示符<br>
     *
     * @return String - 列表标示符
     */
    public static String getListTag()
    {
        return list;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    setListTag<br>
     * 描述:    设置当前的list列表标示符<br>
     *
     * @param tag - 要设置的list列表标示符
     * @return JsonProductor - 返回JsonProductor类
     */
    public static Class setListTag(String tag)
    {
        list=tag;
        return JsonProductor.class;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    createJsonHeader<br>
     * 描述:    创建Json字符串的起始字符<br>
     *
     * @param total - 总数参数
     * @return String - Json字符串的起始字符
     */
    public static String createJsonHeader(int total)
    {
        return "{\""+success+"\":true,\"total\":"+total+",\""+list+"\":[";
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    createJsonHeader<br>
     * 描述:    创建Json字符串的起始字符<br>
     *
     * @param pageSize  - 一页数据量
     * @param page      - 页码
     * @param total     - 总数
     * @param totalPage - 总页数
     * @return String - Json字符串的起始字符
     */
    public static String createJsonHeader(int pageSize,int page,int total,int totalPage)
    {
        StringBuilder buffer=new StringBuilder();
        buffer.append("{\"").append(success).append("\":true,")
                .append("\"pageSize\":").append(pageSize).append(",")
                .append("\"page\":").append(page).append(",")
                .append("\"total\":").append(total).append(",")
                .append("\"totalPage\":").append(totalPage).append(",\"")
                .append(list).append("\":[");
        return buffer.toString();
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    createJsonHeader<br>
     * 描述:    创建Json字符串的起始字符<br>
     *
     * @param attributes - 根目录属性
     * @return String - Json字符串的起始字符
     */
    public static String createJsonHeader(Map<String,String> attributes)
    {
        StringBuilder buffer=new StringBuilder();
        buffer.append("{\"").append(success).append("\":true");
        if(!attributes.isEmpty())
        {
            for(Object key : attributes.keySet())
            {
                String name=(String)key;
                String value=attributes.get(name);
                buffer.append(",\"").append(name).append("\":\"").append(value).append("\"");
            }
        }
        buffer.append(",\"").append(list).append("\":[");
        return buffer.toString();
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    createJsonEnd<br>
     * 描述:    创建Json字符串的结尾字符串<br>
     *
     * @return String - 结尾字符串
     */
    public static String createJsonEnd()
    {
        return "]}";
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    createInformation<br>
     * 描述:    显示错误信息<br>
     *
     * @param bool  - 是否成功
     * @param infor - 信息
     * @return String - 包装后的信息
     */
    public static String createInformation(boolean bool,String infor)
    {
        return "{\""+success+"\":"+bool+",\"infor\":\""+infor.trim().replaceAll("\"","'")+"\"}";
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    createInformation<br>
     * 描述:    显示错误信息<br>
     *
     * @param bool  - 是否成功
     * @param infor - 信息
     * @param valueMap - 输出值Map
     * @return String - 包装后的信息
     */
    public static String createInformation(boolean bool,String infor,Map<String,Object> valueMap)
    {
        String result="{\""+success+"\":"+bool+",\"infor\":\""+infor.trim().replaceAll("\"","'")+"\"";

        for(String key:valueMap.keySet())
        {
            result+=",\""+key+"\":\""+valueMap.get(key)+"\"";
        }

        result+="}";
        return result;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    toJsonByObjectList<br>
     * 描述:    生成对象列表的一般格式字符串<br>
     *
     * @param list - 对象列表
     * @return String - 一般格式字符串
     */
    public static String toJsonByObjectList(List<Object> list)
    {
        return new ObjectListToJsonStringTransformer(list).transToString();
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    toJsonByObjectList<br>
     * 描述:    生成对象列表的一般格式字符串<br>
     *
     * @param list         - 对象列表
     * @param isFilterNull - 是否过滤空字段
     * @return String - 一般格式字符串
     */
    public static String toJsonByObjectList(List<Object> list,boolean isFilterNull)
    {
        return new ObjectListToJsonStringTransformer(list,isFilterNull).transToString();
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    toJsonByObjectList<br>
     * 描述:    生成对象分页列表的一般格式字符串<br>
     *
     * @param list      - the list of object array
     * @param pageSize  - 一页数据量
     * @param page      - 页码
     * @param total     - 总数
     * @param totalPage - 总页数
     * @return String - 一般格式字符串
     */
    public static String toJsonByObjectList(List<Object> list
            ,int pageSize,int page,int total,int totalPage)
    {
        return new ObjectListToJsonStringTransformer(list).transToString(pageSize,page,total,totalPage);
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    toJsonByObjectList<br>
     * 描述:    生成对象分页列表的一般格式字符串<br>
     *
     * @param list       - the list of object array
     * @param attributes - 根目录属性
     * @return String - 一般格式字符串
     */
    public static String toJsonByObjectList
    (List<Object> list,Map<String,String> attributes)
    {
        return new ObjectListToJsonStringTransformer(list).transToString(attributes);
    }
}
