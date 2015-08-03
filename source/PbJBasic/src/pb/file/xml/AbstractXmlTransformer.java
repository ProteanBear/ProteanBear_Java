package pb.file.xml;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Xml转换器接口抽象实现类。<br>
 * 用于实现Xml转换器接口的抽象类。<br>
 * 1.02 —— 增加父类属性的读取。<br>
 * 1.01 —— 在输出时增加总页数属性。<br>
 * 1.00 —— 实现基本的接口方法<br>
 *
 * @param <T>
 * @author proteanBear(马强)
 * @version 1.01 2011/11/22
 */
public abstract class AbstractXmlTransformer<T> implements XmlTransformer
{
    /**
     * 域(私有)<br>
     * 名称:    data<br>
     * 描述:    数据<br>
     */
    private T data;

    /**
     * 域(私有)<br>
     * 名称:    rootname<br>
     * 描述:    生成xml的根目录名称<br>
     */
    private String rootname;

    /**
     * 域(私有)<br>
     * 名称:    rowname<br>
     * 描述:    一行数据标签名称<br>
     */
    private String rowname;

    /**
     * 域(私有)<br>
     * 名称:    proc<br>
     * 描述:    xml解析器<br>
     */
    private XMLProcessor processor;

    /**
     * 构造函数（无参数）<br>
     * 描述：   初始化私有域
     */
    public AbstractXmlTransformer()
    {
        this.rootname="TABLE";
        this.rowname="ROW";
        this.processor=new XMLProcessor();
    }

    /**
     * 方法（抽象）<br>
     * 名称:    transToXmlString<br>
     * 描述:    转换为xml字符串<br>
     *
     * @return String - xml字符串
     */
    @Override
    public abstract String transToXmlString();

    /**
     * 方法（抽象）<br>
     * 名称:    transToXmlString<br>
     * 描述:    转换为xml字符串<br>
     *
     * @param pageSize  - 一页数据量
     * @param page      - 页码
     * @param total     - 总数
     * @param totalPage - 总页数
     * @return String - xml字符串
     */
    @Override
    public abstract String transToXmlString(int pageSize,int page,int total,int totalPage);

    /**
     * 方法（抽象）
     * 名称:    transToXmlString<br>
     * 描述:    转换为xml字符串<br>
     *
     * @param attributes - 根目录属性
     * @return String - xml字符串
     */
    @Override
    public abstract String transToXmlString(Map<String,String> attributes);

    /**
     * 方法（抽象）
     * 名称:    transToXmlString<br>
     * 描述:    转换为xml字符串<br>
     *
     * @param attributes - 根目录属性
     * @param isAttri
     * @return String - xml字符串
     */
    @Override
    public abstract String transToXmlString(Map<String,String> attributes,boolean isAttri);

    /**
     * 方法（抽象）<br>
     * 名称:    addChildElement<br>
     * 描述:    未给定的文档增加来自当前列表数据的子元素<br>
     *
     * @param doc - Document xml文档
     * @return Document - 增加子元素后的文档
     */
    protected abstract Document addChildElement(Document doc);

    /**
     * 访问器（公共）<br>
     * 目标：   rootname<br>
     *
     * @return String - 根名称
     */
    @Override
    public String getRootname()
    {
        return rootname;
    }

    /**
     * 更改器（公共）<br>
     * 目标：   rootname
     *
     * @param rootname - 根名称
     */
    @Override
    public void setRootname(String rootname)
    {
        this.rootname=rootname;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   rowname<br>
     *
     * @return String - 一行数据标签
     */
    @Override
    public String getRowname()
    {
        return rowname;
    }

    /**
     * 更改器（公共）<br>
     * 目标：   rowname
     *
     * @param rowname - 一行数据标签
     */
    @Override
    public void setRowname(String rowname)
    {
        this.rowname=rowname;
    }

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
     * 访问器（受保护）<br>
     * 目标：   processor<br>
     *
     * @return XMLProcessor - xml解析器
     */
    protected final XMLProcessor getXMLProcessor()
    {
        return this.processor;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    createDocument<br>
     * 描述:    生成xml文档<br>
     *
     * @param total
     * @return Document - xml文档
     */
    protected Document createDocument(int total)
    {
        Document doc=DocumentHelper.createDocument();
        Element root=doc.addElement(this.getRootname());
        root.addAttribute("total",total+"");
        doc=this.addChildElement(doc);
        return doc;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    createDocument<br>
     * 描述:    生成xml文档<br>
     *
     * @param pageSize  - 一页数据量
     * @param page      - 页码
     * @param total     - 总数
     * @param totalPage - 总页数
     * @return Document - xml文档
     */
    protected Document createDocument(int pageSize,int page,int total,int totalPage)
    {
        Document doc=DocumentHelper.createDocument();
        Element root=doc.addElement(this.getRootname());
        root.addAttribute("pagesize",pageSize+"")
                .addAttribute("page",page+"")
                .addAttribute("total",total+"")
                .addAttribute("totalpage",totalPage+"");
        doc=this.addChildElement(doc);
        return doc;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    createDocument<br>
     * 描述:    生成xml文档<br>
     *
     * @param attributes - 根目录属性
     * @return Document - xml文档
     */
    protected Document createDocument(Map<String,String> attributes)
    {
        return this.createDocument(attributes,true);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    createDocument<br>
     * 描述:    生成xml文档<br>
     *
     * @param attributes - 根目录属性
     * @param isAttri    - 是否添加附加属性在根节点的属性中，为否时添加为根节点下的子节点
     * @return Document - xml文档
     */
    protected Document createDocument(Map<String,String> attributes,boolean isAttri)
    {
        Document doc=DocumentHelper.createDocument();
        Element root=doc.addElement(this.getRootname());
        for(Object key : attributes.keySet())
        {
            String name=(String)key;
            String value=attributes.get(name);
            value=(value==null)?"":value;
            if(isAttri)
            {
                root.addAttribute(name,value);
            }
            else
            {
                root.addElement(name).addText(value);
            }
        }
        doc=this.addChildElement(doc);
        return doc;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    showErrorInformation<br>
     * 描述:    显示错误信息<br>
     *
     * @param infor - 信息
     * @return String - 包装后的信息
     * @throws java.io.IOException
     */
    protected String showErrorInformation(String infor) throws IOException
    {
        return XMLProductor.toInforXml(false,infor);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getClassAllFields<br>
     * 描述:    获取类所有的属性<br>
     *
     * @param fields
     * @param entityClass - 类
     * @return List - 属性列表
     */
    protected List<Field> getClassAllFields(List<Field> fields,Class entityClass)
    {
        Class superClass=entityClass.getSuperclass();
        if(!superClass.isAssignableFrom(Object.class)) fields=getClassAllFields(fields,superClass);

        Field[] result=entityClass.getDeclaredFields();
        fields.addAll(Arrays.asList(result));

        return fields;
    }
}
