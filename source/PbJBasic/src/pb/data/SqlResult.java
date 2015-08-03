package pb.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 本Java类是用于描述sql查询语句返回结果的类方法
 * 2.02 - 增加对是字符串的数据去除首尾空格的操作
 * 2.01 - 增加数据表中数组的位置对应的列名的映射，修正设置了label后输出xml节点名为中文
 *
 * @author proteanBear(马强)
 * @version 2.02 2010/09/28
 */
public class SqlResult
{
    /**
     * 静态方法（公共）<br>
     * 名称:    createSqlResultByResultSet<br>
     * 描述:    本方法用于根据给定的ResultSet对象生成SqlResult对象<br>
     *
     * @param rs                     - ResultSet
     * @param isUserOraclePageMethod - 是否使用了Oracle的分页方法
     * @return SqlResult - 自定义封装的结果对象
     * @throws java.sql.SQLException
     */
    public static SqlResult createSqlResult
    (ResultSet rs,boolean isUserOraclePageMethod)
            throws SQLException
    {
        //异常情况检查，错误则返回null
        if(rs==null) return null;
         /*try{if(rs.isClosed())  return null;}
         catch(SQLException ex){return null;}*/

        //解析ResultSet，构建SqlResult对象
        SqlResult result=new SqlResult();
        ResultSetMetaData metadata=rs.getMetaData();
        //获取数据列数
        int columnCount=rs.getMetaData().getColumnCount();
        columnCount=(isUserOraclePageMethod)?(columnCount-1):columnCount;
        result.setColumnCount(columnCount);
        //遍历结果集属性对象获取列名
        for(int i=0;i<columnCount;i++)
        {
            //添加列名和位置的映射
            result.setColumnIndex(metadata.getColumnName(i+1),i);
            //添加位置和列名的映射
            result.setColumnName(i,metadata.getColumnName(i+1));
            //添加位置和打印显示名称的映射
            result.setColumnLabel(i,metadata.getColumnLabel(i+1));
        }
        //遍历结果集获得结果数据
        result.setTable(createObjectArrayListByResultSet(rs,isUserOraclePageMethod));
        return result;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    createObjectArrayListByResultSet<br>
     * 描述:    本方法用于根据给定的ResultSet对象生成对象数组列表（List\<Object[]\>）<br>
     *
     * @param rs                     - ResultSet
     * @param isUserOraclePageMethod - 是否使用了Oracle的分页方法
     * @return List<Object[]> - 对象数组列表
     * @throws java.sql.SQLException
     */
    public static List<Object[]>
    createObjectArrayListByResultSet
    (ResultSet rs,boolean isUserOraclePageMethod)
            throws SQLException
    {
        //异常情况检查，错误则返回null
        if(rs==null) return null;
        /* try{if(rs.isClosed())  return null;}
         catch(SQLException ex){return null;}*/

        //遍历ResultSet，对象数组列表
        List<Object[]> result=new ArrayList<>();
        Object[] row;
        //获取数据列数
        int columnCount=rs.getMetaData().getColumnCount();
        columnCount=(isUserOraclePageMethod)?(columnCount-1):columnCount;
        //遍历结果集获得结果数据
        while(rs.next())
        {
            row=new Object[columnCount];
            for(int i=0;i<columnCount;i++) row[i]=rs.getObject(i+1);
            result.add(row);
        }
        return result;
    }

    /**
     * 域(私有)<br>
     * 名称:    table<br>
     * 描述:    记录数据的对象数组列表（List\<Object[]\>）<br>
     */
    private List<Object[]> table;

    /**
     * 域(私有)<br>
     * 名称:    columns<br>
     * 描述:    记录数据表的每列的名称并对应在数组中的位置<br>
     */
    private Map<String,Integer> columns;

    /**
     * 域(私有)<br>
     * 名称:    names<br>
     * 描述:    记录数据表中数组的位置对应的列名<br>
     */
    private Map<Integer,String> names;

    /**
     * 域(私有)<br>
     * 名称:    labels<br>
     * 描述:    记录数据表中数组的位置对应的列打印名<br>
     */
    private Map<Integer,String> labels;

    /**
     * 域(私有)<br>
     * 名称:    labelWidths<br>
     * 描述:    记录数据表中列打印名称的宽度<br>
     */
    private Map<String,Integer> labelWidths;

    /**
     * 域(私有)<br>
     * 名称:    notDisplayColumn<br>
     * 描述:    记录在显示列表时不显示的列<br>
     */
    private Set<Integer> notDisplayColumn;

    /**
     * 域(私有)<br>
     * 名称:    mainKeyIndex<br>
     * 描述:    主键字段的位置<br>
     */
    private int mainKeyIndex;

    /**
     * 域(私有)<br>
     * 名称:    columnNumber<br>
     * 描述:    数据列数<br>
     */
    private int columnCount;

    /**
     * 构造函数（私有、无参数）<br>
     * 描述：   私有构造函数保护对象构造只能通过createSqlResult静态方法生成
     */
    private SqlResult()
    {
        this.table=new ArrayList<>();
        this.columns=Collections.synchronizedMap(new HashMap<String,Integer>());
        this.names=Collections.synchronizedMap(new HashMap<Integer,String>());
        this.labels=Collections.synchronizedMap(new HashMap<Integer,String>());
        this.labelWidths=Collections.synchronizedMap(new HashMap<String,Integer>());
        this.notDisplayColumn=Collections.synchronizedSet(new HashSet<Integer>());
        this.mainKeyIndex=0;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   columnCount<br>
     *
     * @return int - 数据列数
     */
    public int getColumnCount()
    {
        return this.columnCount;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getRowCount<br>
     * 描述:    获取数据的行数<br>
     *
     * @return int - 行数，数据列表为空则返回-1
     */
    public int getRowCount()
    {
        if(this.table==null) return -1;
        return this.table.size();
    }

    /**
     * 方法（公共）<br>
     * 名称:    isEmpty<br>
     * 描述:    结果列表是否是空的<br>
     *
     * @return boolean - 是否为空，当结果list的长度为0
     */
    public boolean isEmpty()
    {
        return (this.getRowCount()==0);
    }

    /**
     * 访问器（公共）<br>
     * 目标：   table<br>
     *
     * @return List<Object[]> - 对象数组列表
     */
    public List<Object[]> getTable()
    {
        return this.table;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getTable<br>
     * 描述:    从指定的start位置开始获取到结尾的数据，列表为null、没有数据或索引位置错误则返回null；<br>
     *
     * @param start - 开始位置
     * @return List<Object[]> - 对象数组列表
     */
    public List<Object[]> getTable(int start)
    {
        return this.getTable(start,this.getRowCount()-start-1);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getTable<br>
     * 描述:    从指定的start位置开始获取到结尾的columnName列的数据，列表为null、没有数据或索引位置错误则返回null；<br>
     *
     * @param start       - 开始位置
     * @param columnNames
     * @return List<Object[]> - 对象数组列表
     */
    public List<Object[]> getTable(int start,String... columnNames)
    {
        return this.getTable(start,this.getRowCount()-start-1,columnNames);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getTable<br>
     * 描述:    从指定的start位置开始获取count行的数据，列表为null、没有数据或索引位置错误则返回null；<br>
     * 如果start加上count的位置大于列表结尾位置，则返回从start到结尾的数据<br>
     *
     * @param start - 开始位置
     * @param count - 获取行的数量
     * @return List<Object[]> - 对象数组列表
     */
    public List<Object[]> getTable(int start,int count)
    {
        //检查列表是否为空或没有数据
        int rowcount=this.getRowCount();
        if(rowcount==-1 || rowcount==0 || !(start<rowcount)) return null;

        //获取指定区间的数据
        int end=start+count;
        end=(end<rowcount)?end:rowcount;
        List<Object[]> result=new ArrayList<>();
        for(int i=start;i<end;i++) result.add(this.table.get(i));
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getTable<br>
     * 描述:    从指定的start位置开始获取count行的数据，列表为null、没有数据或索引位置错误则返回null；<br>
     * 如果start加上count的位置大于列表结尾位置，则返回从start到结尾的数据<br>
     *
     * @param start              - 开始位置
     * @param count              - 获取行的数量
     * @param columnNames@return List<Object[]> - 对象数组列表
     */
    public List<Object[]> getTable(int start,int count,String... columnNames)
    {
        //检查列表是否为空或没有数据
        int rowcount=this.getRowCount();
        if(rowcount==-1 || rowcount==0 || !(start<rowcount)) return null;

        //获取指定区间的数据
        int end=start+count;
        end=(end<rowcount)?end:rowcount;
        List<Object[]> result=new ArrayList<>();
        for(int i=start;i<end;i++) result.add(this.getData(rowcount,columnNames));
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getData<br>
     * 描述:    根据指定的索引值即行标获取一行数据<br>
     *
     * @param index - 索引值即行标
     * @return Object[] - 一行数据为一个对象数组,列表为null或没有数据则返回null
     */
    public Object[] getData(int index)
    {
        if(!(index<this.getRowCount() && this.getRowCount()!=0)) return null;
        return this.table.get(index);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getData<br>
     * 描述:    获取rowIndex指定的一行数据中columnIndex列的数据,列表为null或没有数据则返回null；<br>
     * 如果rowIndex的位置大于列表结尾位置或columnIndex大于列数，则返回null<br>
     *
     * @param rowIndex    - 行标
     * @param columnIndex - 列标
     * @return Object - 对象
     */
    public Object getData(int rowIndex,int columnIndex)
    {
        int rowcount=this.getRowCount();
        if(rowcount==-1 || rowcount==0
                || !(rowIndex<rowcount)
                || columnIndex>this.getColumnCount())
        {
            return null;
        }
        return Trim(this.table.get(rowIndex)[columnIndex]);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getData<br>
     * 描述:    获取rowIndex指定的一行数据中列名为columnName的数据,列表为null或没有数据则返回null；<br>
     * 如果rowIndex的位置大于列表结尾位置或columnName列名不存在，则返回null<br>
     *
     * @param rowIndex   - 行标
     * @param columnName - 列名
     * @return Object - 对象
     */
    public Object getData(int rowIndex,String columnName)
    {
        int rowcount=this.getRowCount();
        if(rowcount==-1 || rowcount==0
                || !(rowIndex<rowcount)
                || this.columns==null
                || !this.columns.containsKey(columnName))
        {
            return null;
        }
        return Trim(this.table.get(rowIndex)[this.columns.get(columnName)]);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getData<br>
     * 描述:    获取rowIndex指定的一行数据中包含多个列名的数据,列表为null或没有数据则返回null；<br>
     * 如果rowIndex的位置大于列表结尾位置或columnName列名不存在，则返回null<br>
     *
     * @param rowIndex    - 行标
     * @param columnNames - 可变参数，多个列名
     * @return Object[] - 对象数组
     */
    public Object[] getData(int rowIndex,String... columnNames)
    {
        Object[] result=new Object[columnNames.length];
        int i=0;
        for(String columnName : columnNames)
        {
            result[i]=this.getData(rowIndex,columnName);
            i++;
        }
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setData<br>
     * 描述:    对指定行和指定位置的数据进行修改<br>
     *
     * @param rowIndex      - 行标
     * @param columnIndex   - 列标
     * @param replaceObject
     */
    public void setData(int rowIndex,int columnIndex,Object replaceObject)
    {
        int rowcount=this.getRowCount();
        if(rowcount==-1 || rowcount==0
                || !(rowIndex<rowcount)
                || columnIndex>this.getColumnCount())
        {
            return;
        }
        Object[] objArray=this.table.get(rowIndex);
        objArray[columnIndex]=replaceObject;
        this.table.set(rowIndex,objArray);
    }

    /**
     * 方法（公共）<br>
     * 名称:    setData<br>
     * 描述:    对指定行和指定位置的数据进行修改<br>
     *
     * @param rowIndex      - 行标
     * @param columnName    - 列名
     * @param replaceObject
     */
    public void setData(int rowIndex,String columnName,Object replaceObject)
    {
        if(!this.columns.containsKey(columnName)) return;
        this.setData(rowIndex,this.columns.get(columnName),replaceObject);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getColumnName<br>
     * 描述:    根据列编号获取列名称<br>
     *
     * @param index - 列编号
     * @return String - 列名称
     */
    public String getColumnName(int index)
    {
        return this.names.get(index);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getColumnNames<br>
     * 描述:    根据列编号获取列名称数组，如果列名称映射不存在则返回null<br>
     *
     * @return String[] - 列名称
     */
    public String[] getColumnNames()
    {
        if(this.names==null) return null;
        return (String[])this.names.values().toArray();
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnName<br>
     * 描述:    设置列编号对应的列名称<br>
     *
     * @param index - 列编号
     * @param name  - 列名称
     */
    public void setColumnName(int index,String name)
    {
        this.names.put(index,name);
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnNames<br>
     * 描述:    批量设置列对应的列名称<br>
     *
     * @param names - 可变参数，对应从0开始的列显示名称
     */
    public void setColumnNames(String... names)
    {
        int i=-1;
        for(String name : names)
        {
            i++;
            if(name==null || "".equals(name.trim())) continue;
            this.setColumnName(i,name);
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    getColumnLabel<br>
     * 描述:    根据列编号获取打印显示名称<br>
     *
     * @param index - 列编号
     * @return String - 打印显示名称
     */
    public String getColumnLabel(int index)
    {
        return this.labels.get(index);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getColumnLabels<br>
     * 描述:    根据列编号获取打印显示名称数组，如果打印名映射不存在则返回null<br>
     *
     * @return String[] - 打印显示名称
     */
    public String[] getColumnLabels()
    {
        if(this.labels==null) return null;
        return (String[])this.labels.values().toArray();
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnLabel<br>
     * 描述:    设置列编号对应的打印显示名称<br>
     *
     * @param index - 列编号
     * @param label - 打印显示名称
     */
    public void setColumnLabel(int index,String label)
    {
        this.labels.put(index,label);
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnLabels<br>
     * 描述:    批量设置列对应的打印显示名称<br>
     *
     * @param labels - 可变参数，对应从0开始的列显示名称
     */
    public void setColumnLabels(String... labels)
    {
        int i=-1;
        for(String label : labels)
        {
            i++;
            if(label==null || "".equals(label.trim())) continue;
            this.setColumnLabel(i,label);
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    getColumnLabelWidth<br>
     * 描述:    根据列编号获取打印显示名称<br>
     *
     * @param index - 列编号
     * @return int - 打印列宽度
     */
    public int getColumnLabelWidth(int index)
    {
        String label=this.getColumnLabel(index);
        return this.getColumnLabelWidth(label);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getColumnLabelWidth<br>
     * 描述:    根据列编号获取打印显示名称<br>
     *
     * @param label - 列编号
     * @return int - 打印列宽度
     */
    public int getColumnLabelWidth(String label)
    {
        if(!this.labelWidths.containsKey(label)) return 0;
        return this.labelWidths.get(label);
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnLabelWidth<br>
     * 描述:    设置列编号对应的打印列宽度<br>
     *
     * @param label - 打印显示名称
     * @param width - 宽度
     */
    public void setColumnLabelWidth(String label,int width)
    {
        this.labelWidths.put(label,width);
    }

    /**
     * 方法（私有）<br>
     * 名称:    getMainKeyIndex<br>
     * 描述:    本方法用于获取主键字段所在的位置<br>
     *
     * @return int - 主键字段所在的位置
     */
    public int getMainKeyIndex()
    {
        return this.mainKeyIndex;
    }

    /**
     * 方法（私有）<br>
     * 名称:    getMainKeyColumnName<br>
     * 描述:    获得主键字段的字段名<br>
     *
     * @return String - 主键字段的字段名
     */
    public String getMainKeyColumnName()
    {
        return this.getColumnName(this.getMainKeyIndex());
    }

    /**
     * 方法（私有）<br>
     * 名称:    getMainKey<br>
     * 描述:    获取指定行的主键<br>
     *
     * @param rowIndex - 行标
     * @return Object - 主键值
     */
    public Object getMainKey(int rowIndex)
    {
        return this.getData(rowIndex,this.getMainKeyIndex());
    }

    /**
     * 方法（私有）<br>
     * 名称:    setMainKey<br>
     * 描述:    设置主键的列位置<br>
     *
     * @param index - 主键的索引位置
     */
    public void setMainKey(int index)
    {
        this.mainKeyIndex=index;
    }

    /**
     * 方法（私有）<br>
     * 名称:    setMainKey<br>
     * 描述:    设置主键的列位置<br>
     *
     * @param columnName - 列名称
     */
    public void setMainKey(String columnName)
    {
        if(!this.columns.containsKey(columnName)) return;
        this.mainKeyIndex=this.columns.get(columnName);
    }

    /**
     * 方法（私有）<br>
     * 名称:    isDisplayColumn<br>
     * 描述:    判断此列是否为非显示屏蔽列<br>
     *
     * @param columnIndex - 列索引
     * @return boolean - 此列是否为非显示屏蔽列
     */
    public boolean isDisplayColumn(int columnIndex)
    {
        return !this.notDisplayColumn.contains(columnIndex);
    }

    /**
     * 方法（私有）<br>
     * 名称:    isDisplayColumn<br>
     * 描述:    判断此列是否为非显示屏蔽列<br>
     *
     * @param columnName - 列名称
     * @return boolean - 此列是否为非显示屏蔽列
     */
    public boolean isDisplayColumn(String columnName)
    {
        if(!this.columns.containsKey(columnName)) return true;
        return this.isDisplayColumn(this.columns.get(columnName));
    }

    /**
     * 方法（私有）<br>
     * 名称:    setDisplayColumn<br>
     * 描述:    判断此列是否为非显示屏蔽列<br>
     *
     * @param columnIndex - 列索引
     */
    public void setNotDisplayColumn(int columnIndex)
    {
        this.notDisplayColumn.add(columnIndex);
    }

    /**
     * 方法（私有）<br>
     * 名称:    setDisplayColumn<br>
     * 描述:    判断此列是否为非显示屏蔽列<br>
     *
     * @param columnIndexs - 列索引(可变参数)
     */
    public void setNotDisplayColumn(int... columnIndexs)
    {
        for(int columnIndex : columnIndexs)
        {
            this.setNotDisplayColumn(columnIndex);
        }
    }

    /**
     * 方法（私有）<br>
     * 名称:    setDisplayColumn<br>
     * 描述:    判断此列是否为非显示屏蔽列<br>
     *
     * @param columnName - 列名称
     */
    public void setDisplayColumn(String columnName)
    {
        if(!this.columns.containsKey(columnName)) return;
        this.setNotDisplayColumn(this.columns.get(columnName));
    }

    /**
     * 方法（私有）<br>
     * 名称:    setDisplayColumn<br>
     * 描述:    判断此列是否为非显示屏蔽列<br>
     *
     * @param columnNames - 列名称（可变参数）
     */
    public void setDisplayColumn(String... columnNames)
    {
        for(String columnName : columnNames)
        {
            this.setDisplayColumn(columnName);
        }
    }

    /**
     * 方法（私有）<br>
     * 名称:    setColumnIndex<br>
     * 描述:    设置列名对应的列编号<br>
     *
     * @param columnName  - 列名
     * @param columnIndex - 列编号
     */
    private void setColumnIndex(String columnName,int columnIndex)
    {
        this.columns.put(columnName,columnIndex);
    }

    /**
     * 更改器（私有）<br>
     * 目标：   columnNumber
     *
     * @param num
     */
    private void setColumnCount(int num)
    {
        this.columnCount=num;
    }

    /**
     * 更改器（私有）<br>
     * 目标：   table
     *
     * @param table - 对象数组列表
     */
    private void setTable(List<Object[]> table)
    {
        this.table=table;
    }

    /**
     * 方法（私有）<br>
     * 名称:    Trim<br>
     * 描述:    对是字符串的对象结果首尾去空格<br>
     *
     * @param obj - Object对象
     * @return Object - 去掉空格的字符串对象
     */
    private Object Trim(Object obj)
    {
        if(obj==null) return obj;
        if(obj.getClass().equals(String.class))
        {
            obj=((String)obj).trim();
        }
        return obj;
    }
}
