package pb.file.xml;

import java.io.IOException;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import pb.data.SqlResult;

/**
 * SqlResult结构XML转换器类。<br>
 * 实现了XmlTransformer接口，用于将SqlResult转换为XML格式文档。<br>
 * 多用于将SQL查询后生成的SqlResult对象进行XML转换时使用。<br>
 * 1.01 —— 在输出时增加总页数属性。<br>
 * 1.00 —— 实现基本的实现方法。
 *
 * @author proteanBear(马强)
 * @version 1.01 2011/11/07
 */
public class SqlResultToXmlTransformer
        extends AbstractXmlTransformer<SqlResult>
        implements XmlTransformer
{
    /**
     * 构造函数（两个参数）<br>
     * 描述：   初始化域
     *
     * @param sqlResult - 结果集
     * @param rootname
     */
    public SqlResultToXmlTransformer(SqlResult sqlResult,String rootname)
    {
        super();
        this.setData(sqlResult);
        this.setRootname(rootname);
    }

    /**
     * 构造函数（三个参数）<br>
     * 描述：   初始化域
     *
     * @param sqlResult - 结果集
     * @param rootname  - 生成xml的根目录名称
     * @param rowname   - 每列对应名称
     */
    public SqlResultToXmlTransformer(SqlResult sqlResult,String rootname,String rowname)
    {
        super();
        this.setData(sqlResult);
        this.setRootname(rootname);
        this.setRowname(rowname);
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
                    (createDocument(this.getData().getRowCount()));
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
     * @return String - xml字符串
     */
    @Override
    public String transToXmlString(Map<String,String> attributes,boolean isAttri)
    {
        try
        {
            if(this.getData()==null)
            {
                attributes.put("success","false");
                attributes.put("infor","空列表对象");
            }
            else if(this.getData().isEmpty())
            {
                attributes.put("success","false");
                attributes.put("infor","列表中没有数据");
            }

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
        if(this.getData()==null) return doc;
        for(int i=0;i<this.getData().getRowCount();i++)
        {
            Element row=doc.getRootElement().addElement(this.getRowname());
            for(int j=0;j<this.getData().getColumnCount();j++)
            {
                if(!this.getData().isDisplayColumn(j)) continue;
                Object obj=this.getData().getData(i,j);
                String name=this.getData().getColumnName(j);
                if(obj==null) continue;
                String value=(obj+"").trim();
                row.addElement(name.toUpperCase()).addText(value);
            }
        }
        return doc;
    }
}