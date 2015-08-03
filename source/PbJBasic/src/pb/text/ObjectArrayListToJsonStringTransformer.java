package pb.text;

import java.util.List;
import java.util.Map;

/**
 * 本Java类是用于描述Json字符串转换器接口的对象列表转换器
 * 1.01 - 增加总页数属性
 *
 * @author proteanBear(马强)
 * @version 1.01 2011/11/07
 */
public class ObjectArrayListToJsonStringTransformer
        extends ObjectArrayListToStringTransformer
        implements StringTransformer
{
    /**
     * 域(私有)<br>
     * 名称:    namelist<br>
     * 描述:    每列对应名称<br>
     */
    private String[] namelist;

    /**
     * 构造函数（无参数）<br>
     * 描述：   初始化域
     */
    public ObjectArrayListToJsonStringTransformer()
    {
        super();
    }

    /**
     * 构造函数（一个参数）<br>
     * 描述：   初始化域
     *
     * @param objlist  - 对象列表
     * @param namelist
     */
    public ObjectArrayListToJsonStringTransformer
    (List<Object[]> objlist,String[] namelist)
    {
        super(objlist);
        this.namelist=namelist;
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
            for(int i=0;i<this.getData().size();i++)
            {
                if(i!=0) buffer.append(",");
                buffer.append("{");
                Object[] objs=this.getData().get(i);
                for(int j=0;j<objs.length;j++)
                {
                    if(j!=0) buffer.append(",");
                    Object obj=objs[j];
                    String name=this.namelist[j];
                    String value=(obj==null)?"":obj.toString();
                    value=this.replaceQuotesInString(value);
                    buffer.append("\"").append(name).append("\":\"").append(value).append("\"");
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
