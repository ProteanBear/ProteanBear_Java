package pb.text;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 本Java类是用于实现字符串转换器接口的抽象类
 * 1.03 - 增加isFilterNull属性。<br>
 * 1.02 - 增加父类属性的读取。<br>
 * 1.01 - 增加总页数属性
 *
 * @param <T> 泛型类型，指定当前字符转换对应的对象
 * @author proteanBear(马强)
 * @version 1.03 2012/03/21
 */
public abstract class AbstractStringTransformer<T> implements StringTransformer
{
    /**
     * 域(私有)<br>
     * 名称:    data<br>
     * 描述:    数据<br>
     */
    private T data;

    /**
     * 域(受保护)<br>
     * 名称:    isFilterNull<br>
     * 描述:    记录是否过滤掉空数据<br>
     */
    protected boolean isFilterNull;

    /**
     * 构造函数（无参数）<br>
     * 描述：   初始化私有域
     */
    public AbstractStringTransformer()
    {
        isFilterNull=false;
    }

    /**
     * 构造函数（无参数）<br>
     * 描述：   初始化私有域
     *
     * @param isFilterNull - 是否过滤掉空数据
     */
    public AbstractStringTransformer(boolean isFilterNull)
    {
        this.isFilterNull=isFilterNull;
    }

    /**
     * 方法（抽象）<br>
     * 名称:    transToString<br>
     * 描述:    转换为使用分隔符分隔的字符串<br>
     *
     * @return String - 使用分隔符分隔的字符串
     */
    @Override
    public abstract String transToString();

    /**
     * 方法（抽象）<br>
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
    public abstract String transToString(int pageSize,int page,int total,int totalPage);

    /**
     * 方法（抽象）
     * 名称:    transToString<br>
     * 描述:    转换为字符串<br>
     *
     * @param attributes - 根目录属性
     * @return String - 使用分隔符分隔的字符串
     */
    @Override
    public abstract String transToString(Map<String,String> attributes);

    /**
     * 方法（抽象）<br>
     * 名称:    addListString<br>
     * 描述:    未给定的文档增加来自当前列表数据的子元素<br>
     *
     * @param doc - 使用分隔符分隔的字符串
     * @return String - 使用分隔符分隔的字符串
     */
    protected abstract String transToString(String doc);

    /**
     * 访问器（受保护）<br>
     * 目标：   data<br>
     *
     * @return T - 指定的数据类型
     */
    protected final T getData()
    {
        return this.data;
    }

    /**
     * 更改器（受保护）<br>
     * 目标：   data<br>
     *
     * @param data - 数据
     */
    protected final void setData(T data)
    {
        this.data=data;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    createDocument<br>
     * 描述:    使用分隔符分隔的字符串<br>
     *
     * @param total 总数
     * @return String - 使用分隔符分隔的字符串
     */
    protected String createDocument(int total)
    {
        return this.transToString(StringProductor.createHeader(total));
    }

    /**
     * 方法（受保护）<br>
     * 名称:    createDocument<br>
     * 描述:    生成使用分隔符分隔的字符串<br>
     *
     * @param pageSize  - 一页数据量
     * @param page      - 页码
     * @param total     - 总数
     * @param totalPage - 总页数
     * @return String - 使用分隔符分隔的字符串
     */
    protected String createDocument(int pageSize,int page,int total,int totalPage)
    {
        return this.transToString(StringProductor.createHeader(pageSize,page,total,totalPage));
    }

    /**
     * 方法（受保护）<br>
     * 名称:    createDocument<br>
     * 描述:    生成x使用分隔符分隔的字符串<br>
     *
     * @param attributes - 根目录属性
     * @return String - 使用分隔符分隔的字符串
     */
    protected String createDocument(Map<String,String> attributes)
    {
        return this.transToString(StringProductor.createHeader(attributes));
    }

    /**
     * 方法（受保护）<br>
     * 名称:    replaceQuotesInString<br>
     * 描述:    替换字符串中的引号，防止输出时出错<br>
     *
     * @param string - 用于替换的字符串
     * @return String - 替换后的字符串
     */
    protected String replaceQuotesInString(String string)
    {
        string=string.trim();
        string=string.replaceAll("\"","\\\\\\\"");
        //string=string.replaceAll("\'","\\\\'");
        string=string.replaceAll("[\\r]","\\\\r");
        string=string.replaceAll("[\\n]","\\\\n");
        string=string.replaceAll("[\\t]","\\\\t");
        return string;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    showErrorInformation<br>
     * 描述:    显示错误信息<br>
     *
     * @param infor - 信息
     * @return String - 包装后的信息
     */
    protected String showErrorInformation(String infor)
    {
        return StringProductor.createInformation(false,infor);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getClassAllFields<br>
     * 描述:    获取类所有的属性<br>
     *
     * @param fields 字段列表
     * @param entityClass - 类
     * @return List- 属性列表
     */
    protected List<Field> getClassAllFields(List<Field> fields,Class entityClass)
    {
        Class superClass=entityClass.getSuperclass();
        if(!superClass.isAssignableFrom(Object.class)) fields=getClassAllFields(fields,superClass);

        Field[] result=entityClass.getDeclaredFields();
        fields.addAll(Arrays.asList(result));

        return fields;
    }
     
     

    /*public static void main(String[] args)
    {
        String text="的了恐惧发送的可浪费就快了是空间“的发生的路口警方\"fdsdfsdfdsf\"";
        System.out.print(replaceQuotesInString(text));
    }*/
}
