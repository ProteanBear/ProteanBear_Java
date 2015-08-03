package pb.file.xml;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Element;

/**
 * 对象列表结构XML转换器类。<br>
 * 实现了XmlTransformer接口，用于将对象列表转换为XML格式文档。<br>
 * 多用于将EJB实体类JPQL查询实体对象结果进行XML转换时使用。<br>
 * 1.04 —— 去除空列表报错。<br>
 * 1.03 —— 增加输出时对数组属性的支持。<br>
 * 1.02 —— 增加父类属性的读取。<br>
 * 1.01 —— 在输出时增加总页数属性。<br>
 * 1.00 —— 实现基本的实现方法。
 * @author      proteanBear(马强)
 * @version     1.04 2012/02/28
 */
public class ObjectListToXmlTransformer
        extends AbstractXmlTransformer<List<Object>>
        implements XmlTransformer
{
    /**域(私有)<br>
     * 名称:    classtype<br>
     * 描述:    对象类型<br>
     */private Class classtype;

    /**构造函数（无参数）<br>
     * 描述：   初始化域
     */public ObjectListToXmlTransformer()
     {
         super();
         this.setData(new ArrayList<>());
     }

    /**构造函数（一个参数）<br>
     * 描述：   初始化域
     * @param  objlist - 对象列表
     */public ObjectListToXmlTransformer(List<Object> objlist)
     {
         super();
         this.setData(objlist);
         if(objlist==null||objlist.isEmpty()) return;
         this.classtype=objlist.get(0).getClass();
         this.setRootname(this.classtype.getSimpleName().toUpperCase());
     }

    /**方法（公共）<br>
     * 名称:    transToXmlString<br>
     * 描述:    转换为xml字符串<br>
     * @return String - xml字符串
     */@Override public String transToXmlString()
     {
         try
         {
             if(this.getData()==null)        return this.showErrorInformation("空对象列表");
             //if(this.getData().isEmpty())    return this.showErrorInformation("列表中没有数据");

             return this.getXMLProcessor().getDocumentString
                     (createDocument(this.getData().size()));
         }
         catch(IOException ex)
         {
             return null;
         }
     }

    /**方法（公共）<br>
     * 名称:    transToXmlString<br>
     * 描述:    转换为xml字符串<br>
     * @param  pageSize - 一页数据量
     * @param  page - 页码
     * @param  total - 总数
     * @param  totalPage - 总页数
     * @return String - xml字符串
     */@Override public String transToXmlString
             (int pageSize, int page, int total,int totalPage)
     {
         try
         {
             if(this.getData()==null)        return this.showErrorInformation("空对象列表");
             //if(this.getData().isEmpty())    return this.showErrorInformation("列表中没有数据");

             return this.getXMLProcessor().getDocumentString
                     (createDocument(pageSize, page, total,totalPage));
         }
         catch(IOException ex)
         {
             return null;
         }
     }

    /**方法（公共）<br>
     * 名称:    transToXmlString<br>
     * 描述:    转换为xml字符串<br>
     * @param  attributes - 根目录属性
     * @return String - xml字符串
     */@Override public String transToXmlString(Map<String,String> attributes)
     {
         try
         {
             if(this.getData()==null)        return this.showErrorInformation("空对象列表");
             //if(this.getData().isEmpty())    return this.showErrorInformation("列表中没有数据");

             return this.getXMLProcessor().getDocumentString
                     (createDocument(attributes));
         }
         catch(IOException ex)
         {
             return null;
         }
     }

    /**方法（公共）<br>
     * 名称:    transToXmlString<br>
     * 描述:    转换为xml字符串<br>
     * @param  attributes - 根目录属性
     * @return String - xml字符串
     */@Override public String transToXmlString(Map<String,String> attributes,boolean isAttri)
     {
         try
         {
             if(this.getData()==null)        return this.showErrorInformation("空对象列表");
             //if(this.getData().isEmpty())    return this.showErrorInformation("列表中没有数据");

             return this.getXMLProcessor().getDocumentString
                     (createDocument(attributes,isAttri));
         }
         catch(IOException ex)
         {
             return null;
         }
     }

    /**方法（私有）<br>
     * 名称:    addChildElement<br>
     * 描述:    未给定的文档增加来自当前列表数据的子元素<br>
     * @param  doc - Document xml文档
     * @return Document - 增加子元素后的文档
     */@Override protected Document addChildElement(Document doc)
     {
         try
         {
             for(int i=0;i<this.getData().size();i++)                           //遍历列表
             {
                 Object tempobj=this.getData().get(i);
                 Element row=doc.getRootElement().addElement(this.getRootname());

                 List<Field> fields=new ArrayList<>();
                 fields=this.getClassAllFields(fields, classtype);              //获得列表中对象属性
                 for (Field field : fields) //遍历属性数组
                 {
                     String name = field.getName(); //获得属性名
                     String getname="get"+name.substring(0,1).toUpperCase()+
                             name.substring(1);                                 //获得属性访问器名称
                     Method method=this.classtype.getMethod(getname);           //获得属性访问器方法
                     Object value=method.invoke(tempobj);                       //获得属性值
                     if(value!=null&&!"".equals(value))
                     {
                         //如果字段为数组类型
                         if(value.getClass().isArray())
                         {
                             Object[] values=(Object[]) value;
                             value="[";
                             for (int k = 0; k < values.length; k++)
                             {
                                 Object val=values[k];
                                 if(k!=0) value=value+",";
                                 value=value+(val+"");
                             }
                             value=value+"]";
                             row.addElement(name.toUpperCase()).addText(value+"");
                         }
                         else row.addElement(name.toUpperCase()).addText(value.toString());
                     }
                 }
             }
             return doc;
         }
         catch(IllegalAccessException
                 | IllegalArgumentException
                 | NoSuchMethodException
                 | SecurityException
                 | InvocationTargetException ex)
         {
             return null;
         }
     }
}