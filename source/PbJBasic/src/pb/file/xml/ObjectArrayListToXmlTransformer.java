package pb.file.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

/**
 * 对象数组列表结构XML转换器类。<br>
 * 实现了XmlTransformer接口，用于将对象数组列表转换为XML格式文档。<br>
 * 多用于将EJB实体类JPQL查询属性结果进行XML转换时使用。<br>
 * 1.01 —— 在输出时增加总页数属性。<br>
 * 1.00 —— 实现基本的实现方法。
 *
 * @author proteanBear(马强)
 * @version 1.01 2011/11/07
 */
public class ObjectArrayListToXmlTransformer
        extends AbstractXmlTransformer<List<Object[]>>
        implements XmlTransformer
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
    public ObjectArrayListToXmlTransformer()
    {
        super();
        this.setData(new ArrayList<Object[]>());
    }

    /**
     * 构造函数（三个参数）<br>
     * 描述：   初始化域
     *
     * @param objlist  - 对象数组列表
     * @param rootname - 生成xml的根目录名称
     * @param namelist - 每列对应名称
     */
    public ObjectArrayListToXmlTransformer
    (List<Object[]> objlist,String rootname,String[] namelist)
    {
        super();
        this.setData(objlist);
        this.setRootname(rootname);
        this.namelist=namelist;
    }

    /**
     * 构造函数（四个参数）<br>
     * 描述：   初始化域
     *
     * @param objlist  - 对象数组列表
     * @param rootname - 生成xml的根目录名称
     * @param rowname  - 一行数据标签名称
     * @param namelist - 每列对应名称
     */
    public ObjectArrayListToXmlTransformer
    (List<Object[]> objlist,String rootname,String rowname,String[] namelist)
    {
        super();
        this.setData(objlist);
        this.setRootname(rootname);
        this.setRowname(rootname);
        this.namelist=namelist;
    }

    /**
     * 方法（公共）<br>
     * 名称:    transToXmlString<br>
     * 描述:    转换为xml字符串<br>
     *
     * @return String - xml字符串
     */
    @Override
    public String transToXmlString()
    {
        try
        {
            if(this.getData()==null) return this.showErrorInformation("空对象列表");
            if(this.getData().isEmpty()) return this.showErrorInformation("列表中没有数据");

            return this.getXMLProcessor().getDocumentString
                    (createDocument(this.getData().size()));
        }
        catch(IOException ex)
        {
            return null;
        }
    }

    /**
     * 方法（公共）<br>
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
    public String transToXmlString
    (int pageSize,int page,int total,int totalPage)
    {
        try
        {
            if(this.getData()==null) return this.showErrorInformation("空对象列表");
            if(this.getData().isEmpty()) return this.showErrorInformation("列表中没有数据");

            return this.getXMLProcessor().getDocumentString
                    (createDocument(pageSize,page,total,totalPage));
        }
        catch(IOException ex)
        {
            return null;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    transToXmlString<br>
     * 描述:    转换为xml字符串<br>
     *
     * @param attributes - 根目录属性
     * @return String - xml字符串
     */
    @Override
    public String transToXmlString(Map<String,String> attributes)
    {
        try
        {
            if(this.getData()==null) return this.showErrorInformation("空对象列表");
            if(this.getData().isEmpty()) return this.showErrorInformation("列表中没有数据");

            return this.getXMLProcessor().getDocumentString
                    (createDocument(attributes));
        }
        catch(IOException ex)
        {
            return null;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    transToXmlString<br>
     * 描述:    转换为xml字符串<br>
     *
     * @param attributes - 根目录属性
     * @param isAttri    - 是否添加附加属性在根节点的属性中，为否时添加为根节点下的子节点
     * @return String - xml字符串
     */
    @Override
    public String transToXmlString(Map<String,String> attributes,boolean isAttri)
    {
        try
        {
            if(this.getData()==null) return this.showErrorInformation("空对象列表");
            if(this.getData().isEmpty()) return this.showErrorInformation("列表中没有数据");

            return this.getXMLProcessor().getDocumentString
                    (createDocument(attributes,isAttri));
        }
        catch(IOException ex)
        {
            return null;
        }
    }

    /**
     * 方法（私有）<br>
     * 名称:    addChildElement<br>
     * 描述:    未给定的文档增加来自当前列表数据的子元素<br>
     *
     * @param doc - Document xml文档
     * @return Document - 增加子元素后的文档
     */
    @Override
    protected Document addChildElement(Document doc)
    {
        for(int i=0;i<this.getData().size();i++)
        {
            Object[] objs=this.getData().get(i);
            Element row=doc.getRootElement().addElement(this.getRootname());
            for(int j=0;j<objs.length;j++)
            {
                Object obj=objs[j];
                String name=this.namelist[j];
                if(obj==null) continue;
                String value=obj.toString().trim();
                row.addElement(name.toUpperCase()).addText(value);
            }
        }
        return doc;
    }
}
