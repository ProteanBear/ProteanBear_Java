package pb.text;

import java.util.Map;

import pb.data.SqlResult;

/**
 * 本Java类是用于描述字符串转换器接口的自定义sql结果对象XML转换器
 * 1.01 - 增加总页数属性
 *
 * @author proteanBear(马强)
 * @version 1.01 2011/11/07
 */
public class SqlResultToStringTransformer
        extends AbstractStringTransformer<SqlResult>
        implements StringTransformer
{
    /**
     * 构造函数（一个参数）<br>
     * 描述：   初始化域
     *
     * @param sqlResult - 结果集
     */
    public SqlResultToStringTransformer(SqlResult sqlResult)
    {
        super();
        this.setData(sqlResult);
    }

    /**
     * 方法（抽象）<br>
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
            if(this.getData()==null) return this.showErrorInformation("空对象列表");
            if(this.getData().isEmpty()) return this.showErrorInformation("列表中没有数据");

            return this.createDocument(this.getData().getRowCount());
        }
        catch(Exception ex)
        {
            return null;
        }
    }

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
    public String transToString(int pageSize,int page,int total,int totalPage)
    {
        try
        {
            if(this.getData()==null) return this.showErrorInformation("空对象列表");
            if(this.getData().isEmpty()) return this.showErrorInformation("列表中没有数据");

            return this.createDocument(pageSize,page,total,totalPage);
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    /**
     * 方法（抽象）
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
            if(this.getData()==null) return this.showErrorInformation("空对象列表");
            if(this.getData().isEmpty()) return this.showErrorInformation("列表中没有数据");

            return this.createDocument(attributes);
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    /**
     * 方法（抽象）<br>
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
            for(int i=0;i<this.getData().getRowCount();i++)
            {
                buffer.append(StringProductor.getRowExcision());
                buffer.append("\"");
                for(int j=0;j<this.getData().getColumnCount();j++)
                {
                    Object obj=this.getData().getData(i,j);
                    if(obj==null) continue;
                    if(j!=0) buffer.append(StringProductor.getRangeExcision());
                    String value=(obj+"").trim();
                    buffer.append(value);
                }
                buffer.append("\"");
            }
            return buffer.toString();
        }
        catch(Exception ex)
        {
            return null;
        }
    }
}
