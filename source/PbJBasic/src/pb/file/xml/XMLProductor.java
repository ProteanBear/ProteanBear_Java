package pb.file.xml;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Xml文本生成器。<br>
 * 包含各种静态方法，将用户提供的信息生成XML格式的文本字符串。<br>
 * 1.01 —— 在输出时增加总页数属性。<br>
 *
 * @author proteanBear(马强)
 * @version 1.01 2011/11/07
 */
public class XMLProductor
{
    /**
     * 静态方法（公共）<br>
     * 名称:    toInforXml<br>
     * 描述:    获得信息的xml格式<br>
     *
     * @param success - 是否成功
     * @param infor   - 显示信息
     * @return String - xml格式的信息
     * @throws java.io.IOException - 文档生成错误
     */
    public static String toInforXml(boolean success,String infor)
            throws IOException
    {
        XMLProcessor proc=new XMLProcessor();
        Document doc=DocumentHelper.createDocument();
        Element root=doc.addElement("RETURN");
        root.addElement("SUCCESS").addText(success+"");
        root.addElement("INFOR").addText(infor);
        return proc.getDocumentString(doc);
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    toInforXml<br>
     * 描述:    获得信息的xml格式<br>
     *
     * @param success - 是否成功
     * @param infor   - 显示信息
     * @param valueMap - 输出值Map
     * @return String - xml格式的信息
     * @throws java.io.IOException - 文档生成错误
     */
    public static String toInforXml(boolean success,String infor,Map<String,Object> valueMap)
            throws IOException
    {
        XMLProcessor proc=new XMLProcessor();
        Document doc=DocumentHelper.createDocument();
        Element root=doc.addElement("RETURN");
        root.addElement("SUCCESS").addText(success+"");
        root.addElement("INFOR").addText(infor);

        //生成结果信息
        for(String key:valueMap.keySet())
        {
            root.addElement(key).addText(valueMap.get(key)+"");
        }

        return proc.getDocumentString(doc);
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    toXmlString<br>
     * 描述:    获得列表的xml字符串（针对对象数组）<br>
     *
     * @param list - 对象数组
     * @return String - xml格式的信息
     */
    public static String toXmlString(List<Object> list)
    {
        return new ObjectListToXmlTransformer(list).transToXmlString();
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    toXmlString<br>
     * 描述:    获得列表的xml字符串（针对对象数组）<br>
     *
     * @param list      - 对象数组
     * @param pageSize  - 一页数据量
     * @param page      - 页码
     * @param total     - 总数
     * @param totalPage - 总页数
     * @return String - xml格式的信息
     */
    public static String toXmlString(List<Object> list,
                                     int pageSize,int page,int total,int totalPage)
    {
        return new ObjectListToXmlTransformer(list).transToXmlString(pageSize,page,total,totalPage);
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    toXmlString<br>
     * 描述:    获得列表的xml字符串（针对对象数组）<br>
     *
     * @param list       - 对象数组
     * @param attributes - 根目录属性
     * @return String - xml格式的信息
     */
    public static String toXmlString(List<Object> list,Map<String,String> attributes)
    {
        return new ObjectListToXmlTransformer(list).transToXmlString(attributes);
    }

    /**
     * 构造函数（无参数）<br>
     */
    private XMLProductor()
    {
    }
}