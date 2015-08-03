package pb.text;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 本Java类是用于描述Json字符串转换器接口的对象列表转换器<br>
 * 1.06 - 增加isFilterNull属性。<br>
 * 1.05 - 修正非字符类属性输出空字段时的格式错误。<br>
 * 1.04 - 修正属性为null的输出。<br>
 * 1.03 - 修正数组属性输出时，字符型未加引号的问题。<br>
 * 1.02 - 增加输出时对数组属性的支持。<br>
 * 1.01 - 增加总页数属性
 *
 * @author proteanBear(马强)
 * @version 1.04 2012/03/21
 */
public class ObjectListToJsonStringTransformer
        extends ObjectListToStringTransformer
        implements StringTransformer
{
    /**
     * 构造函数（无参数）<br>
     * 描述：   初始化域
     */
    public ObjectListToJsonStringTransformer()
    {
        super();
    }

    /**
     * 构造函数（一个参数）<br>
     * 描述：   初始化域
     *
     * @param isFilterNull - 是否过滤掉空数据
     */
    public ObjectListToJsonStringTransformer(boolean isFilterNull)
    {
        this();
        this.isFilterNull=isFilterNull;
    }

    /**
     * 构造函数（一个参数）<br>
     * 描述：   初始化域
     *
     * @param objlist - 对象列表
     */
    public ObjectListToJsonStringTransformer(List<Object> objlist)
    {
        super(objlist);
    }

    /**
     * 构造函数（二个参数）<br>
     * 描述：   初始化域
     *
     * @param objlist      - 对象列表
     * @param isFilterNull - 是否过滤掉空数据
     */
    public ObjectListToJsonStringTransformer(List<Object> objlist,boolean isFilterNull)
    {
        this(objlist);
        this.isFilterNull=isFilterNull;
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
    protected String createDocument
    (int pageSize,int page,int total,int totalPage)
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
            for(int i=0;i<this.getData().size();i++)                           //遍历列表
            {
                if(i!=0) buffer.append(",");
                buffer.append("{");
                Object tempobj=this.getData().get(i);

                List<Field> fields=new ArrayList<>();
                fields=this.getClassAllFields(fields,this.getClassType());     //获取类属性列表
                int temp=0;
                for(Field field : fields) //遍历类属性列表
                {
                    if(temp!=0) buffer.append(",");
                    String name=field.getName(); //获取类属性名称
                    String getname="get"+name.substring(0,1).toUpperCase()+
                            name.substring(1);
                    Method method=this.getClassType().getMethod(getname);
                    Object value=(method==null)?null:method.invoke(tempobj);
                    if(this.isFilterNull && value==null) continue;
                    Class type=field.getType();
                    String quotation=(type.isAssignableFrom(String.class))?"\"":"";
                    //如果为数组类型
                    if(type.isArray())
                    {
                        if(value!=null)
                        {
                            Object[] values=(Object[])value;
                            value="[";
                            for(int k=0;k<values.length;k++)
                            {
                                Object val=values[k];
                                if(k!=0) value=value+",";
                                String quota=(val.getClass().isAssignableFrom(String.class))?"\"":"";
                                value=value+(quota+val+quota);
                            }
                            value=value+"]";
                        }
                    }
                    else
                    {
                        value=this.replaceQuotesInString(value+"");
                    }
                    buffer.append("\"").append(name).append("\":")
                            .append(quotation)
                            .append(
                                    (value==null || "null".equals(value))?
                                            ((type.isAssignableFrom(String.class))?"":"0"):value
                            )
                            .append(quotation);
                    temp++;
                }
                buffer.append("}");
            }
            buffer.append(JsonProductor.createJsonEnd());
            return buffer.toString();
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |NoSuchMethodException
                |SecurityException
                |InvocationTargetException ex)
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