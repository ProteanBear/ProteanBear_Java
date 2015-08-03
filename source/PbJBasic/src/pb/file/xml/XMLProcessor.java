package pb.file.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * Xml文档处理器类。<br>
 * 使用Dom4j对XML文件进行读取和写入。<br>
 * 1.00 —— 实现基本的读取和写入的方法。
 * @author      proteanBear(马强)
 * @version     1.00 2011/07/08
 */
public class XMLProcessor
{
    /**域(私有)<br>
     * 名称:    doc<br>
     * 描述:    xml文档对象<br>
     */private Document doc;

    /**域(私有)<br>
     * 名称:    isLoad<br>
     * 描述:    导入是否成功<br>
     */private boolean isLoad;

    /**构造函数（无参数）<br>
     * 描述：   初始化私有域
     */public XMLProcessor(){this.isLoad=false;}

    /**构造函数（一个参数）<br>
     * 描述：   指定xml文件路径
     * @param  xmlPath - 文件路径
     */public XMLProcessor(String xmlPath)
     {
         this();
         this.load(xmlPath);
     }

    /**构造函数（一个参数）<br>
     * 描述：   指定xml输入流
     * @param  input - 输入流
     */public XMLProcessor(InputStream input)
     {
         this();
         this.load(input);
     }

    /**方法（公共、不可继承）<br>
     * 名称:    load<br>
     * 描述:    导入xml文件生成文档对象<br>
     * @param  xmlPath - 文件路径
     */public final void load(String xmlPath)
     {
         File file=new File(xmlPath);
         if(!file.exists()){this.isLoad=false;return;}
         try
         {
             SAXReader reader=new SAXReader();
             this.doc=reader.read(file);
             this.isLoad=true;
         }
         catch(DocumentException ex)
         {
             this.isLoad=false;
         }
     }

    /**方法（公共、不可继承）<br>
     * 名称:    load<br>
     * 描述:    导入输入流生成文档对象<br>
     * @param  input - 输入流
     */public final void load(InputStream input)
     {
         try
         {
             SAXReader reader=new SAXReader();
             this.doc=reader.read(input);
             this.isLoad=true;
         }
         catch(DocumentException ex)
         {
             this.isLoad=false;
         }
     }

    /**方法（公共）<br>
     * 名称:    save<br>
     * 描述:    储存文档对象为本地文件（当前文档）<br>
     * @param  savePath - 储存路径
     * @return boolean - 是否成功
     */public boolean save(String savePath)
     {
         return this.save(this.doc, savePath);
     }

    /**方法（公共）<br>
     * 名称:    save<br>
     * 描述:    储存文档对象为本地文件（指定文档）<br>
     * @param  doc - 指定文档
     * @param  savePath - 储存路径
     * @return boolean - 是否成功
     */public boolean save(Document doc,String savePath)
     {
         boolean isSuccess=false;
         try
         {
             FileOutputStream output=new FileOutputStream(savePath);
             OutputFormat format=new OutputFormat("",true,"UTF-8");
             XMLWriter writer=new XMLWriter(output,format);
             writer.write(doc);
             writer.close();
             isSuccess=true;
         }
         catch(IOException ex)
         {
             isSuccess=false;
         }
         return isSuccess;
     }

    /**方法（公共）<br>
     * 名称:    save<br>
     * 描述:    储存文档对象为本地文件（指定数据字符串）<br>
     * @param  docString - 指定数据字符串
     * @param  savePath - 储存路径
     * @return boolean - 是否成功
     */public boolean save(String docString,String savePath)
     {
         boolean isSuccess=false;
         try
         {
             Document document = this.getStringDocument(docString);
             isSuccess=this.save(document, savePath);
         }
         catch (DocumentException ex)
         {
             isSuccess=false;
         }
         return isSuccess;
     }

    /**方法（公共）<br>
     * 名称:    getDocumentString<br>
     * 描述:    获得xml文档字符串（指定文档）<br>
     * @param  doc - 指定文档
     * @return String - xml文档字符串
     * @throws java.io.IOException - xml读取错误
     */public String getDocumentString(Document doc) throws IOException
     {
         StringBuilder result=new StringBuilder();
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         OutputFormat format=new OutputFormat(" ",true,"UTF-8");
         XMLWriter writer=new XMLWriter(out,format);
         writer.write(doc);
         result.append(out.toString("UTF-8"));
         return result.toString();
     }

    /**方法（公共）<br>
     * 名称:    getStringDocument<br>
     * 描述:    获得xml文档对象（指定数据字符串）<br>
     * @param  docString - 指定数据字符串
     * @return Document - org.dom4j.Document xml文档
     * @throws DocumentException - org.dom4j.DocumentException 文档生成错误
     */public Document getStringDocument(String docString)
             throws DocumentException
     {
         return DocumentHelper.parseText(docString);
     }

    /**方法（公共）<br>
     * 名称:    getDocument<br>
     * 描述:    获得当前文档<br>
     * @return Document - org.dom4j.Document xml文档
     */public Document getDocument(){return this.doc;}
}