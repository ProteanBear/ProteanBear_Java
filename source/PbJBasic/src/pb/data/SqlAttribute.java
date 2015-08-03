package pb.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 本Java类是用于描述sql语句替换属性的类。<br>
 * 2.04 - 修正set多参数方法未加分隔符的错误。<br>
 * 2.03 - 增加数据数量统计(count)时设置统计字段的处理方法。<br>
 * 2.02 - 新增参数为SqlAttribute对象的构造函数，便于生成拷贝<br>
 * 2.01 - 修正set方法未重置域为空字符而造成sql语句出错的问题<br>
 *
 * @author proteanBear(马强)
 * @version 2.04 2011/12/02
 */
public class SqlAttribute
{
    /**---------------------------开始：静态内容------------------------------**/

    /**
     * 静态域(私有)<br>
     * 名称:    sqlTemplate<br>
     * 描述:    sql模板映射<br>
     */
    private static Map<String,String> sqlTemplate;

    /**
     * 静态方法（私有）<br>
     * 名称:    getSql<br>
     * 描述:    获取sql模板<br>
     *
     * @param sqlname - sql模板索引名
     * @return String - sql模板
     */
    private static String getSqlTemplate(String sqlname)
    {
        return sqlTemplate.get(sqlname);
    }

    /**
     * 静态方法（私有）<br>
     * 名称:    setSql<br>
     * 描述:    设置sql模板<br>
     *
     * @param sqlname - sql模板索引名
     * @param sql     - sql模板
     * @return 无
     */
    private static void setSqlTemplate(String sqlname,String sql)
    {
        sqlTemplate.put(sqlname,sql);
    }

    //初始化Map
    static
    {
        sqlTemplate=Collections.synchronizedMap(new HashMap<String,String>());
    }

    //设置sql模板
    static
    {
        setSqlTemplate("count","select count({$column-count}) from {$table} {$condition} {$groupBy} {$orderBy}");
        setSqlTemplate("select","select {$column-names} from {$table} {$condition} {$groupBy} {$orderBy}");
        setSqlTemplate("insert","insert into {$table} {$column-names} values({$column-values})");
        setSqlTemplate("update","update {$table} set {$column-sets} {$condition}");
        setSqlTemplate("delete","delete from {$table} {$condition}");
    }

    /**---------------------------结束：静态内容------------------------------**/

    /**
     * 域(私有)<br>
     * 名称:    columnNames<br>
     * 描述:    记录指定的字段名称<br>
     */
    private String columnNames;

    /**
     * 域(私有)<br>
     * 名称:    columnValues<br>
     * 描述:    记录指定的字段值<br>
     */
    private String columnValues;

    /**
     * 域(私有)<br>
     * 名称:    columnSets<br>
     * 描述:    记录指定的字段设置<br>
     */
    private String columnSets;

    /**
     * 域(私有)<br>
     * 名称:    isSet<br>
     * 描述:    标示是否设置了set<br>
     */
    private boolean isSet;

    /**
     * 域(私有)<br>
     * 名称:    table<br>
     * 描述:    记录指定的数据表名(可多表)<br>
     */
    private String table;

    /**
     * 域(私有)<br>
     * 名称:    condition<br>
     * 描述:    记录指定的筛选条件<br>
     */
    private String condition;

    /**
     * 域(私有)<br>
     * 名称:    orderBy<br>
     * 描述:    记录指定的筛选条件<br>
     */
    private String orderBy;

    /**
     * 域(私有)<br>
     * 名称:    groupBy<br>
     * 描述:    记录指定的筛选条件<br>
     */
    private String groupBy;

    /**
     * 域(私有)<br>
     * 名称:    sqlContent<br>
     * 描述:    记录自设置的sql语句内容<br>
     */
    private String sqlContent;

    /**
     * 域(私有)<br>
     * 名称:    columnCount<br>
     * 描述:    记录数据统计时使用的字段<br>
     */
    private String columnCount;

    /**
     * 构造函数（无参数）<br>
     */
    public SqlAttribute()
    {
        this.columnNames="";
        this.columnValues="";
        this.columnSets=null;
        this.table=null;
        this.condition="";
        this.orderBy="";
        this.groupBy="";
        this.sqlContent=null;
        this.columnCount="";
    }

    /**
     * 构造函数（一个参数）<br>
     *
     * @param sqlAttri
     */
    public SqlAttribute(SqlAttribute sqlAttri)
    {
        this.columnNames=sqlAttri.columnNames;
        this.columnValues=sqlAttri.columnValues;
        this.columnSets=sqlAttri.columnSets;
        this.table=sqlAttri.table;
        this.condition=sqlAttri.condition;
        this.orderBy=sqlAttri.orderBy;
        this.groupBy=sqlAttri.groupBy;
        this.sqlContent=sqlAttri.sqlContent;
        this.columnCount=sqlAttri.columnCount;
    }

    /**
     * 构造函数（设置基本域）<br>
     *
     * @param columnNames
     * @param table
     * @param condition
     * @param orderBy
     * @param groupBy
     */
    public SqlAttribute(String columnNames,String table,String condition,
                        String orderBy,String groupBy)
    {
        this();
        this.setColumnNames(columnNames);
        this.setTable(table);
        this.setCondition(condition);
        this.setGroupBy(groupBy);
        this.setOrderBy(orderBy);
    }

    /**
     * 构造函数（设置所有域）<br>
     *
     * @param columnNames
     * @param columnValues
     * @param columnSets
     * @param table
     * @param condition
     * @param orderBy
     * @param groupBy
     */
    public SqlAttribute(String columnNames,String columnValues,
                        String columnSets,String table,String condition,
                        String orderBy,String groupBy)
    {
        this(columnNames,table,condition,orderBy,groupBy);
        this.setColumnValues(columnValues);
        this.setColumnSets(columnSets);
    }


    /**
     * 访问器<br>
     * 目标：    columnNames<br>
     *
     * @return
     */
    public String getColumnNames()
    {
        return this.columnNames;
    }

    /**
     * 方法（公共）<br>
     * 名称:    addColumnName<br>
     * 描述:    设置字段名称,可变参数<br>
     *
     * @param columnName - 字段名称
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute addColumnName(String columnName)
    {
        if(columnName==null || "".equals(columnName.trim())) return this;
        if(!"".equals(this.columnNames)) this.columnNames+=",";
        this.columnNames+=columnName;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnNames<br>
     * 描述:    设置字段名称,可变参数<br>
     *
     * @param columnNames - 字段名称
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute setColumnNames(String columnNames)
    {
        this.columnNames=columnNames;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnNames<br>
     * 描述:    设置字段名称,可变参数<br>
     *
     * @param columnNames - 字段名称,多项可变
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute setColumnNames(String... columnNames)
    {
        this.columnNames="";
        int i=0;
        for(String columnName : columnNames)
        {
            if(columnName==null || "".equals(columnName.trim())) continue;
            if(i!=0) this.columnNames+=",";
            this.columnNames+=columnName;
            i++;
        }
        return this;
    }

    /**
     * 访问器<br>
     * 目标：    columnSets<br>
     *
     * @return
     */
    public String getColumnSets()
    {
        return this.columnSets;
    }

    /**
     * 方法（公共）<br>
     * 名称:    addColumnSet<br>
     * 描述:    增加一条字段名值设置，用于update命令中的set name=value<br>
     *
     * @param columnName  - 字段名
     * @param columnValue - 字段值
     * @return SqlAttribute - 返回对象本身
     */
    public SqlAttribute addColumnSet(String columnName,String columnValue)
    {
        if(columnName==null || columnValue==null || "".equals(columnValue.trim())) return this;
        if(!this.isSet)
        {
            this.columnSets=columnName+"="+columnValue;
            this.isSet=true;
        }
        else
        {
            this.columnSets+=","+columnName+"="+columnValue;
        }
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnSets<br>
     * 描述:    设置字段名值设置，用于update命令中的set name=value<br>
     *
     * @param columnSets - 设置值
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute setColumnSets(String columnSets)
    {
        if(columnSets==null) return this;
        if(!this.isSet)
        {
            this.columnSets="";
            this.isSet=true;
        }
        this.columnSets=columnSets;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnSets<br>
     * 描述:    设置字段名值设置，用于update命令中的set name=value<br>
     *
     * @param columnSets - 设置值,多项可变
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute setColumnSets(String... columnSets)
    {
        if(columnSets==null) return this;
        if(!this.isSet) this.isSet=true;
        this.columnSets="";
        int i=0;
        for(String columnSet : columnSets)
        {
            if(columnSet==null || "".equals(columnSet.trim())) continue;
            if(i!=0) this.columnSets+=",";
            this.columnSets+=columnSet;
            i++;
        }
        return this;
    }

    /**
     * 访问器<br>
     * 目标：    columnValues<br>
     *
     * @return String
     */
    public String getColumnValues()
    {
        return this.columnValues;
    }

    /**
     * 方法（公共）<br>
     * 名称:    addColumnValue<br>
     * 描述:    增加字段值<br>
     *
     * @param columnValue - 字段名称
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute addColumnValue(String columnValue)
    {
        if(columnValue==null) return this;
        columnValue=("".equals(columnValue.trim()))?"null":columnValue;
        if(!"".equals(this.columnValues)) this.columnValues+=",";
        this.columnValues+=columnValue;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnValues<br>
     * 描述:    设置字段值<br>
     *
     * @param columnValues - 字段值
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute setColumnValues(String columnValues)
    {
        if(columnValues==null) return this;
        this.columnValues=columnValues;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnValues<br>
     * 描述:    设置字段值,可变参数<br>
     *
     * @param columnValues - 字段值,多项可变
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute setColumnValues(String... columnValues)
    {
        if(columnValues==null) return this;
        this.columnValues="";
        int i=0;
        for(String columnValue : columnValues)
        {
            if(columnValue==null || "".equals(columnValue.trim())) continue;
            if(i!=0) this.columnValues+=",";
            this.columnValues+=columnValue;
            i++;
        }
        return this;
    }

    /**
     * 访问器<br>
     * 目标：    condition<br>
     *
     * @return
     */
    public String getCondition()
    {
        return this.condition;
    }

    /**
     * 方法（公共）<br>
     * 名称:    addCondition<br>
     * 描述:    设置筛选条件<br>
     *
     * @param condition - 筛选条件
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute addCondition(String condition)
    {
        return this.addCondition(condition,true);
    }

    /**
     * 方法（公共）<br>
     * 名称:    addCondition<br>
     * 描述:    设置筛选条件<br>
     *
     * @param condition - 筛选条件
     * @param isAnd     - 为true时条件间为and关系
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute addCondition(String condition,boolean isAnd)
    {
        if(condition==null || "".equals(condition.trim())) return this;

        String temp;
        if("".equals(this.condition.trim()))
        {
            temp="";
            this.condition="where ";
        }
        else
        {
            temp=(isAnd)?" and ":" or ";
        }
        this.condition+=temp+condition;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setCondition<br>
     * 描述:    设置筛选条件<br>
     *
     * @param condition - 筛选条件
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute setCondition(String condition)
    {
        if(condition==null || "".equals(condition.trim())) return this;
        if(!condition.toLowerCase().contains("where"))
        {
            this.condition="where ";
        }
        else
        {
            this.condition="";
        }
        this.condition+=condition;
        return this;
    }

    /**
     * 访问器<br>
     * 目标：    groupBy<br>
     *
     * @return
     */
    public String getGroupBy()
    {
        return this.groupBy;
    }

    /**
     * 方法（公共）<br>
     * 名称:    addGroupBy<br>
     * 描述:    设置group by<br>
     *
     * @param groupBy - 指定分组字段
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute addGroupBy(String groupBy)
    {
        if(groupBy==null || "".equals(groupBy.trim())) return this;

        String temp;
        if("".equals(this.groupBy.trim()))
        {
            temp="";
            this.groupBy="group by ";
        }
        else
        {
            temp=",";
        }
        this.groupBy+=temp+groupBy;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setGroupBy<br>
     * 描述:    设置group by<br>
     *
     * @param groupBy - 指定分组字段
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute setGroupBy(String groupBy)
    {
        if(groupBy==null || "".equals(groupBy.trim())) return this;
        if(!groupBy.toLowerCase().contains("group"))
        {
            this.groupBy="group by ";
        }
        else
        {
            this.groupBy="";
        }
        this.groupBy+=groupBy;
        return this;
    }

    /**
     * 访问器<br>
     * 目标：    orderBy<br>
     *
     * @return
     */
    public String getOrderBy()
    {
        return this.orderBy;
    }

    /**
     * 方法（公共）<br>
     * 名称:    addOrderBy<br>
     * 描述:    设置order by<br>
     *
     * @param orderBy - 指定排序字段，可写入升序或降序
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute addOrderBy(String orderBy)
    {
        if(orderBy==null || "".equals(orderBy.trim())) return this;

        String temp;
        if("".equals(this.groupBy.trim()))
        {
            temp="";
            this.orderBy="order by ";
        }
        else
        {
            temp=",";
        }
        this.orderBy+=temp+orderBy;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setOrderBy<br>
     * 描述:    设置order by<br>
     *
     * @param orderBy - 指定排序字段，可写入升序或降序
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute setOrderBy(String orderBy)
    {
        if(orderBy==null || "".equals(orderBy.trim())) return this;

        if(!orderBy.toLowerCase().contains("order"))
        {
            this.orderBy="order by ";
        }
        else
        {
            this.orderBy="";
        }
        this.orderBy+=orderBy;
        return this;
    }

    /**
     * 访问器<br>
     * 目标：    columnCount<br>
     *
     * @return
     */
    public String getColumnCount()
    {
        return this.columnCount;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setColumnCount<br>
     * 描述:    设置columnCount<br>
     *
     * @param columnCount - 指定数量统计（count）使用字段名称
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute setColumnCount(String columnCount)
    {
        if(columnCount==null || "".equals(columnCount.trim())) return this;
        this.columnCount=columnCount;
        return this;
    }

    /**
     * 访问器<br>
     * 目标：    table<br>
     *
     * @return
     */
    public String getTable()
    {
        return this.table;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setTable<br>
     * 描述:    设置使用的数据表名称<br>
     *
     * @param table - 数据表名称
     * @return SqlAttribute - 返回对象本身
     */
    public final SqlAttribute setTable(String table)
    {
        this.table=table;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    toString<br>
     * 描述:    生成对象所代表的sql语句<br>
     *
     * @param sqlname
     * @return String - sql语句
     */
    public String toSql(String sqlname)
    {
        if(this.sqlContent!=null)
        {
            if("count".equals(sqlname))
            {
                return "select count(*) from ("+this.sqlContent+") sqlContentCount";
            }
            else
            {
                return this.sqlContent;
            }
        }
        return this.generateSQL(sqlname);
    }

    /**
     * 方法（公共）<br>
     * 名称:    setSqlContent<br>
     * 描述:    设置自定义sql内容<br>
     *
     * @param sql - sql内容
     * @return SqlAttribute - 返回对象本身
     */
    public SqlAttribute setSqlContent(String sql)
    {
        if(sql!=null && !"".equals(sql.trim())) this.sqlContent=sql;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    replaceSqlContent<br>
     * 描述:    替换自定义sql中的内容<br>
     *
     * @param target  - 替换目标
     * @param replace - 替换值
     * @return SqlAttribute - 返回对象本身
     */
    public SqlAttribute replaceSqlContent(String target,String replace)
    {
        if(this.sqlContent!=null)
        {
            this.sqlContent=this.sqlContent.replaceAll(target,replace);
        }
        else
        {
            this.condition=this.condition.replaceAll(target,replace);
        }
        return this;
    }

    /**
     * 方法（私有）<br>
     * 名称:    generateSQL<br>
     * 描述:    替换指定的sql模板中的数据生成使用的sql<br>
     *
     * @param sqlname - sql模板索引名
     * @return String - sql语句
     */
    private String generateSQL(String sqlname)
    {
        //判断异常情况
        if(this.getTable()==null) return null;
        if(!sqlTemplate.containsKey(sqlname)) return null;
        if("insert".equals(sqlname) && this.getColumnValues()==null) return null;
        if("update".equals(sqlname) && this.getColumnSets()==null) return null;
        if("select".equals(sqlname) && "".equals(this.getColumnNames().trim()))
        {
            this.setColumnNames("*");
        }
        if("count".equals(sqlname) && "".equals(this.getColumnCount().trim()))
        {
            this.setColumnCount("*");
        }

        //获取sql并替换其中的参数
        String result=getSqlTemplate(sqlname);
        result=result.replace("{$table}",this.getTable());
        if(result.contains("{$column-names}"))
        {
            if("insert".equals(sqlname))
            {
                result=result.replace("{$column-names}",("".equals(this.columnNames))?"":"("+this.columnNames+")");
            }
            else
            {
                result=result.replace("{$column-names}",this.getColumnNames());
            }
        }
        if(result.contains("{$condition}")) result=result.replace("{$condition}",this.getCondition());
        if(result.contains("{$groupBy}")) result=result.replace("{$groupBy}",this.getGroupBy());
        if(result.contains("{$orderBy}")) result=result.replace("{$orderBy}",this.getOrderBy());
        if(result.contains("{$column-values}")) result=result.replace("{$column-values}",this.getColumnValues());
        if(result.contains("{$column-sets}")) result=result.replace("{$column-sets}",this.getColumnSets());
        if(result.contains("{$column-count}")) result=result.replace("{$column-count}",this.getColumnCount());

        return result;
    }
}
