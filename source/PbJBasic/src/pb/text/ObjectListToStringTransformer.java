package pb.text;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 本Java类是用于描述字符串转换器接口的对象列表XML转换器<br>
 * 1.03 - 增加isFilterNull属性。<br>
 * 1.02 - 去除空列表报错<br>
 * 1.01 - 增加总页数属性<br>
 *
 * @author proteanBear(马强)
 * @version 1.03 2012/03/21
 */
public class ObjectListToStringTransformer
        extends AbstractStringTransformer<List<Object>>
        implements StringTransformer
{
    /**
     * 域(私有)<br>
     * 名称:    classtype<br>
     * 描述:    对象类型<br>
     */
    private Class classtype;

    /**
     * 构造函数（无参数）<br>
     * 描述：   初始化域
     */
    public ObjectListToStringTransformer()
    {
        super();
        this.setData(new ArrayList<>());
    }

    /**
     * 构造函数（无参数）<br>
     * 描述：   初始化域
     *
     * @param isFilterNull - 是否过滤掉空数据
     */
    public ObjectListToStringTransformer(boolean isFilterNull)
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
    public ObjectListToStringTransformer(List<Object> objlist)
    {
        super();
        this.setData(objlist);
        if(objlist==null || objlist.isEmpty()) return;
        this.classtype=objlist.get(0).getClass();
    }

    /**
     * 构造函数（一个参数）<br>
     * 描述：   初始化域
     *
     * @param objlist      - 对象列表
     * @param isFilterNull - 是否过滤掉空数据
     */
    public ObjectListToStringTransformer(List<Object> objlist,boolean isFilterNull)
    {
        this(objlist);
        this.isFilterNull=isFilterNull;
    }

    /**
     * 方法（公共）<br>
     * 名称:    transToString<br>
     * 描述:    转换为使用分隔符分隔的字符串<br>
     *
     * @return String - 使用分隔符分隔的字符串
     */
    @Override
    public String transToString()
    {
        try
        {
            if(this.getData()==null) return showErrorInformation("空对象列表");
            //if(this.getData().isEmpty())    return showErrorInformation("列表中没有数据");

            return createDocument(this.getData().size());
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    transToString<br>
     * 描述:    转换为使用分隔符分隔的字符串<br>
     *
     * @param pageSize  - 一页数据量
     * @param page      - 页码
     * @param total     - 总数
     * @param totalPage - 总页数
     * @return String - 使用分隔符分隔的字符串
     */
    @Override
    public String transToString(int pageSize,int page,int total,int totalPage)
    {
        try
        {
            if(this.getData()==null) return showErrorInformation("空对象列表");
            //if(this.getData().isEmpty())    return showErrorInformation("列表中没有数据");

            return createDocument(pageSize,page,total,totalPage);
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    /**
     * 方法（公共）
     * 名称:    transToString<br>
     * 描述:    转换为字符串<br>
     *
     * @param attributes - 根目录属性
     * @return String - 使用分隔符分隔的字符串
     */
    @Override
    public String transToString(Map<String,String> attributes)
    {
        try
        {
            if(this.getData()==null) return showErrorInformation("空对象列表");
            //if(this.getData().isEmpty())    return showErrorInformation("列表中没有数据");

            return createDocument(attributes);
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    /**
     * 方法（受保护）<br>
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
            for(int i=0;i<this.getData().size();i++)                          //遍历列表
            {
                buffer.append(StringProductor.getRowExcision());
                Object tempobj=this.getData().get(i);
                Field[] fields=this.classtype.getDeclaredFields();             //获取类属性列表
                buffer.append("\"");
                for(int j=1;j<fields.length;j++)                               //遍历类属性列表
                {
                    if(j!=1) buffer.append(StringProductor.getRangeExcision());
                    String name=fields[j].getName();                           //获取类属性名称
                    String getname="get"+name.substring(0,1).toUpperCase()+
                            name.substring(1);
                    Method method=classtype.getMethod(getname);
                    Object value=method.invoke(tempobj);
                    //if(this.isFilterNull&&value==null) continue;

                    //特殊字符转换
                    String strValue=value+"";
                    strValue=strValue.trim();
                    strValue=strValue.replaceAll("[\"]","\\\"");
                    strValue=strValue.replaceAll("[\\r]","\\\\r");
                    strValue=strValue.replaceAll("[\\n]","\\\\n");
                    strValue=strValue.replaceAll("[\\t]","\\\\t");
                    strValue=strValue.replaceAll("[\\\\]","\\\\\\\\");
                    //strValue=strValue.replaceAll("\'","\\\\'");

                    buffer.append(strValue);
                }
                buffer.append("\"");
            }
            return buffer.toString();
        }
        catch(SecurityException|NoSuchMethodException|IllegalAccessException
                |IllegalArgumentException|InvocationTargetException ex)
        {
            return null;
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getClassType<br>
     * 描述:    获得当前的数据对象的类型<br>
     *
     * @return Class - 类
     */
    protected Class getClassType()
    {
        return this.classtype;
    }
}
