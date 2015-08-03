package pb.text;

import java.util.Map;

import pb.data.SqlResult;

/**
 * 本Java类是用于描述Json字符串转换器接口的对象列表转换器
 * 1.01 - 增加总页数属性
 *
 * @author proteanBear(马强)
 * @version 1.01 2011/11/07
 */
public class SqlResultToJsonStringTransformer
        extends SqlResultToStringTransformer
        implements StringTransformer
{
    /**
     * 构造函数（一个参数）<br>
     * 描述：   初始化域
     *
     * @param sqlResult - 对象列表
     */
    public SqlResultToJsonStringTransformer(SqlResult sqlResult)
    {
        super(sqlResult);
    }

    /**
     * 方法（受保护\重载）<br>
     * 名称:    createDocument<br>
     * 描述:    使用分隔符分隔的字符串<br>
     *
     * @return String - 使用分隔符分隔的字符串
     */
    @Override
    protected String createDocument(int total)
    {
        return this.transToString(JsonProductor.createJsonHeader(total));
    }

    /**
     * 方法（受保护\重载）<br>
     * 名称:    createDocument<br>
     * 描述:    生成使用分隔符分隔的字符串<br>
     *
     * @param pageSize  - 一页数据量
     * @param page      - 页码
     * @param total     - 总数
     * @param totalPage - 总页数
     * @return String - 使用分隔符分隔的字符串
     */
    @Override
    protected String createDocument(int pageSize,int page,int total,int totalPage)
    {
        return this.transToString(JsonProductor.createJsonHeader(pageSize,page,total,totalPage));
    }

    /**
     * 方法（受保护\重载）<br>
     * 名称:    createDocument<br>
     * 描述:    生成x使用分隔符分隔的字符串<br>
     *
     * @param attributes - 根目录属性
     * @return String - 使用分隔符分隔的字符串
     */
    @Override
    protected String createDocument(Map<String,String> attributes)
    {
        return this.transToString(JsonProductor.createJsonHeader(attributes));
    }

    /**
     * 方法（受保护\重载）<br>
     * 名称:    transToString<br>
     * 描述:    未给定的文档增加来自当前列表数据的子元素<br>
     *
     * @param doc - 使用分隔符分隔的字符串
     * @return String - 使用分隔符分隔的字符串
     */
    @Override
    protected String transToString(String doc)
    {
        try
        {
            StringBuilder buffer=new StringBuilder();
            buffer.append(doc);
            for(int i=0;i<this.getData().getRowCount();i++)
            {
                if(i!=0) buffer.append(",");
                buffer.append("{");
                for(int j=0;j<this.getData().getColumnCount();j++)
                {
                    Object obj=this.getData().getData(i,j);
                    if(obj==null) continue;
                    if(j!=0) buffer.append(",");
                    String value=(obj+"").trim();
                    value=this.replaceQuotesInString(value);
                    buffer.append("\"").append(this.getData().getColumnName(j))
                            .append("\":\"").append(value).append("\"");
                }
                buffer.append("}");
            }
            buffer.append(JsonProductor.createJsonEnd());
            return buffer.toString();
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    /**
     * 方法（受保护\重载）<br>
     * 名称:    showErrorInformation<br>
     * 描述:    显示错误信息<br>
     *
     * @param infor - 信息
     * @return String - 包装后的信息
     */
    @Override
    protected String showErrorInformation(String infor)
    {
        return JsonProductor.createInformation(false,infor);
    }
}
