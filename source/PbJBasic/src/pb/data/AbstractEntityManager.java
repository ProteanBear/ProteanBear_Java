package pb.data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自定义实体类管理器抽象父类。<br>
 * 用于对自定义的数据表类进行相关操作，完成对数据表的增删改查操作。<br>
 * 1.15 - 修正生成列表时未设置父类属性的问题。<br>
 * 1.14 - 新增最新生成主键的记录,便于获取自定义主键的生成记录。<br>
 * 1.13 - 增加删除后处理方法。<br>
 * 1.12 - 增加transDataToObject方法处理数据库数字类型和Java数字类型之间的转换。<br>
 * 1.11 - 修改getKeyGenerator为非抽象方法,默认返回为null。<br>
 * 1.10 - 去除了create方法中的generator是否为null的判断。<br>
 * 1.09 - 实现了是否使用了生成器的方法。<br>
 * 1.08 - 增加编辑成功后处理方法。<br>
 * 1.07 - 增加数据库连接器对象属性。<br>
 * 1.06 - 实现获取主键名、总数、总页数等接口方法。<br>
 * 1.05 - 增加删除前处理方法。<br>
 * 1.04 - 实现通过指定主键删除数据的方法。<br>
 * 1.03 - 修正主键生成器调用两次，及null主键依然添加数据的问题。增加非自动生成主键数据表的支持。<br>
 * 1.02 - 实现查看数据是否存在的方法。<br>
 * 1.01 - 增加错误记录和获取方法。<br>
 * 1.00 - 测试增删改查操作通过。<br>
 *
 * @param <T> 泛型，对应实体类
 * @author proteanBear(马强)
 * @version 1.15 2012/03/05
 */
public abstract class AbstractEntityManager<T> implements AbstractFacadeLocal<T>
{
    /**
     * 静态常量(私有)<br>
     * 名称:    NOT_USE_GENERATOR<br>
     * 描述:    未使用生成器标识<br>
     */
    public static final String NOT_DISPLAY_KEY="-1";

    /**
     * 域(受保护)<br>
     * 名称:    dataManager<br>
     * 描述:    数据管理器对象<br>
     */
    protected DataManager dataManager;

    /**
     * 域(受保护)<br>
     * 名称:    connector<br>
     * 描述:    记录数据管理器的连接器对象<br>
     */
    protected Connector connector;

    /**
     * 域(受保护)<br>
     * 名称:    entityClass<br>
     * 描述:    当前实体类<br>
     */
    protected Class<T> entityClass;

    /**
     * 域(受保护)<br>
     * 名称:    orderBy<br>
     * 描述:    记录指定的筛选条件<br>
     */
    protected String orderBy;

    /**
     * 域(私有)<br>
     * 名称:    error<br>
     * 描述:    记录错误信息<br>
     */
    private String error;

    /**
     * 域(受保护)<br>
     * 名称:    lastGenerator<br>
     * 描述:    记录最新生成的主键（仅适用于自定义生成主键的获取）<br>
     */
    protected String lastGenerator;

    /**
     * 构造函数<br>
     *
     * @param connector   - 指定数据连接器
     * @param entityClass - 管理器对应的实体类
     */
    public AbstractEntityManager(Connector connector,Class<T> entityClass)
    {
        this.connector=connector;
        this.dataManager=new DataManager(connector);
        this.entityClass=entityClass;
        this.orderBy=null;
    }

    /**
     * 方法（受保护、抽象）<br>
     * 名称:    getKeyGenerator<br>
     * 描述:    返回自增主键的生成器，非生成的主键返回""<br>
     *
     * @return String - 主键生成值
     */
    protected String getKeyGenerator()
    {
        return null;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getFindOrderBy<br>
     * 描述:    获取查询使用的排序方法<br>
     *
     * @return String - 排序方法
     */
    protected String getFindOrderBy()
    {
        if(this.orderBy==null)
        {
            this.orderBy=EntityTransformer.transObjNameToDataName(this.getPrimaryKeyName())+" asc";
        }
        return this.orderBy;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getLastGenerator<br>
     * 描述:    获取最新生成的主键<br>
     *
     * @return String - 最新生成的主键（仅适用于自定义生成主键的获取）
     */
    @Override
    public String getLastGenerator()
    {
        return this.lastGenerator;
    }

    /**
     * 方法（受保护、抽象）<br>
     * 名称:    getPrimaryKeyName<br>
     * 描述:    获取数据表主键字段名称<br>
     *
     * @return String - 获取主键名称
     */
    @Override
    public abstract String getPrimaryKeyName();

    /**
     * 方法（受保护、抽象）<br>
     * 名称:    getPrimaryKeyValue<br>
     * 描述:    获取指定实体对象主键字段值<br>
     *
     * @param entity 实体类
     * @return Object - 获取主键值
     * @throws NoSuchMethodException 异常：没有指定的方法
     * @throws IllegalAccessException 异常：
     * @throws java.lang.reflect.InvocationTargetException 异常：
     */
    protected Object getPrimaryKeyValue(T entity)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        String primaryKeyName=this.getPrimaryKeyName();
        Object result=null;
        if(primaryKeyName!=null)
        {
            String getMethodName=EntityTransformer.generateGetMethodName(primaryKeyName);
            Method method=this.entityClass.getMethod(getMethodName);
            result=method.invoke(entity);
        }
        return result;
    }

    /**
     * 方法（受保护、抽象）<br>
     * 名称:    setPrimaryKeyValue<br>
     * 描述:    设置指定实体对象主键字段值<br>
     *
     * @param entity 实体类
     * @return Object - 获取主键值
     * @throws NoSuchMethodException 异常：没有指定的方法
     * @throws IllegalAccessException 异常：
     * @throws java.lang.reflect.InvocationTargetException 异常：
     */
    protected Object setPrimaryKeyValue(T entity,Object value)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        String primaryKeyName=this.getPrimaryKeyName();
        Object result=null;
        if(primaryKeyName!=null)
        {
            String setMethodName=EntityTransformer.generateSetMethodName(primaryKeyName);
            Method method=this.entityClass.getMethod(setMethodName);
            result=method.invoke(entity,value);
        }
        return result;
    }

    /**
     * 方法（公共）
     * 名称：   create
     * 描述:    添加一条新的记录到数据库中<br>
     *
     * @param entity - 添加记录的描述对象
     * @return boolean - 添加成功，返回true;
     */
    @Override
    public boolean create(T entity)
    {
        boolean success=false;
        try
        {
            String generator=this.getKeyGenerator();
            Object newId=null;
            this.dataManager.setAttribute(
                    EntityTransformer
                            .generateInsertSqlFromEntity(
                                    entityClass,entity,
                                    this.getPrimaryKeyName(),generator
                            )
            );

            //自增主键类型
            if(generator==null)
            {
                success=this.dataManager.insert(newId,
                        EntityTransformer
                                .generateInsertParamsFromEntity(
                                        entityClass,entity,
                                        this.getPrimaryKeyName(),generator
                                )
                );
            }
            //自己生成主键
            else
            {
                success=this.dataManager.insert(
                        EntityTransformer
                                .generateInsertParamsFromEntity(
                                        entityClass,entity,
                                        this.getPrimaryKeyName(),generator
                                )
                );
            }

            if(success)
            {
                if(generator==null&&newId!=null)
                {
                    this.setPrimaryKeyValue(entity,newId);
                    this.lastGenerator=(newId==null)?null:(newId+"");
                }
                else
                {
                    this.lastGenerator=generator;
                }
            }
            else
            {
                this.setError(this.dataManager.getError());
            }
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |NoSuchMethodException
                |InvocationTargetException ex)
        {
            success=this.logAndReturnFalse(ex.toString());
        }
        return success;
    }

    /**
     * 方法（公共）
     * 名称：   edit
     * 描述:    更新指定记录的相关数据到数据库中<br>
     *
     * @param entity - 数据更新的描述对象
     * @return boolean - 更新成功，返回true;
     */
    @Override
    public boolean edit(T entity)
    {
        boolean success=false;
        try
        {
            String keyName=this.getPrimaryKeyName();
            this.dataManager.setAttribute(
                    EntityTransformer
                            .generateUpdateSqlFromEntity(entityClass,entity,keyName)
            );
            success=this.dataManager.update(
                    EntityTransformer
                            .generateParamsFromEntity(entityClass,entity,keyName)
            );

            if(success)
            {
                afterEdit(entity);
            }
            else
            {
                this.setError(this.dataManager.getError());
            }
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |NoSuchMethodException
                |InvocationTargetException ex)
        {
            success=this.logAndReturnFalse(ex.toString());
        }
        return success;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    afterEdit<br>
     * 描述:    设置编辑后的相关处理<br>
     *
     * @param entity - 编辑后的对象
     */
    protected void afterEdit(T entity)
    {
    }

    /**
     * 方法（受保护）<br>
     * 名称:    beforeRemove<br>
     * 描述:    设置删除前的相关处理<br>
     *
     * @param obj - 主键或删除对象
     * @return boolean - 可以删除，返回true
     */
    protected boolean beforeRemove(Object obj)
    {
        return true;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    afterRemove<br>
     * 描述:    设置删除后的相关处理<br>
     *
     * @param obj - 主键或删除对象
     */
    protected void afterRemove(Object obj)
    {
    }

    /**
     * 方法（公共）
     * 名称：   removeById
     * 描述:    从数据库中删除指定主键的记录<br>
     *
     * @param id - 删除的记录描述标示
     * @return boolean - 删除成功，返回true;
     */
    @Override
    public boolean removeById(Object id)
    {
        boolean success=false;
        try
        {
            if(this.beforeRemove(id))
            {
                String keyName=this.getPrimaryKeyName();
                this.dataManager.setAttribute(
                        EntityTransformer
                                .generateSelectSqlFromEntity(entityClass,keyName)
                );
                success=this.dataManager.delete(id);

                if(success)
                {
                    this.afterRemove(id);
                }
                else
                {
                    this.setError(this.dataManager.getError());
                }
            }
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |NoSuchMethodException
                |InvocationTargetException ex)
        {
            success=this.logAndReturnFalse(ex.toString());
        }
        return success;
    }

    /**
     * 方法（公共）
     * 名称：   remove
     * 描述:    从数据库中删除指定的记录<br>
     *
     * @param entity - 删除的记录描述对象
     * @return boolean - 删除成功，返回true;
     */
    @Override
    public boolean remove(T entity)
    {
        boolean success=false;
        try
        {
            if(this.beforeRemove(entity))
            {
                String keyName=this.getPrimaryKeyName();
                this.dataManager.setAttribute(
                        EntityTransformer
                                .generateSelectSqlFromEntity(entityClass,keyName)
                );
                success=this.dataManager.delete(
                        EntityTransformer
                                .getPrimaryKeyValueFromEntity(entityClass,entity,keyName)
                );

                if(success)
                {
                    this.afterRemove(entity);
                }
                else
                {
                    this.setError(this.dataManager.getError());
                }
            }
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |NoSuchMethodException
                |InvocationTargetException ex)
        {
            success=this.logAndReturnFalse(ex.toString());
        }
        return success;
    }

    /**
     * 方法（公共）
     * 名称：   find
     * 描述:    通过主键查找指定主键的数据表描述对象<br>
     *
     * @param id - 主键
     * @return T - 描述对象
     */
    @Override
    public T find(Object id)
    {
        T result=null;
        try
        {
            //构造结果对象
            result=entityClass.newInstance();

            //读取数据库
            String keyName=this.getPrimaryKeyName();
            this.dataManager.setAttribute(
                    EntityTransformer
                            .generateSelectSqlFromEntity(entityClass,keyName)
            );
            List<T> sqlResult=this.generateObjectList
                    (this.dataManager.setPageSize(0).select(id));

            //处理查询结果
            if(sqlResult!=null && !sqlResult.isEmpty())
            {
                result=sqlResult.get(0);
            }
            else
            {
                this.setError(this.dataManager.getError());
                result=(T)this.logAndReturnNull(this.getError());
            }
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |InstantiationException
                |NoSuchMethodException
                |InvocationTargetException
                |SQLException ex)
        {
            result=(T)this.logAndReturnNull(ex.toString());
        }
        return result;
    }

    /**
     * 方法（公共）
     * 名称：   findAll
     * 描述:    获取数据表中的所有数据<br>
     *
     * @return List - 数据列表
     */
    @Override
    public List<T> findAll()
    {
        List<T> result=null;
        try
        {
            //读取数据库
            SqlAttribute sqlAttri=new SqlAttribute();
            sqlAttri.setTable(
                    EntityTransformer
                            .generateTableNameFromEntity(entityClass)
            );
            this.dataManager.setAttribute(sqlAttri);
            result=this.generateObjectList(
                    this.dataManager
                            .setPageSize(0)
                            .setOrderBy(this.getFindOrderBy())
                            .select()
            );
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |InstantiationException
                |NoSuchMethodException
                |InvocationTargetException
                |SQLException ex)
        {
            result=(List<T>)this.logAndReturnNull(ex.toString());
        }
        return result;
    }

    /**
     * 方法（公共）
     * 名称：   find
     * 描述:    获取指定范围的数据表中的数据<br>
     *
     * @param condition - 筛选条件
     * @return List - 数据列表
     */
    @Override
    public List<T> find(Map<String,Object> condition)
    {
        List<T> result=null;
        try
        {
            //读取数据库
            this.dataManager.setAttribute(
                    EntityTransformer
                            .generateSelectSqlFromEntity(entityClass,condition)
            );
            result=this.generateObjectList(
                    this.dataManager
                            .setPageSize(0)
                            .setOrderBy(this.getFindOrderBy())
                            .select(
                                    EntityTransformer
                                            .generateConditionParamsFromEntity(condition)
                            )
            );
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |InstantiationException
                |NoSuchMethodException
                |InvocationTargetException
                |SQLException ex)
        {
            result=(List<T>)this.logAndReturnNull(ex.toString());
        }
        return result;
    }

    /**
     * 方法（公共）
     * 名称：   find
     * 描述:    获取指定范围的数据表中的数据<br>
     *
     * @param page - 页面标号
     * @param max  - 一页的数量
     * @return List - 数据列表
     */
    @Override
    public List<T> find(int page,int max)
    {
        List<T> result=null;
        try
        {
            //读取数据库
            SqlAttribute sqlAttri=new SqlAttribute();
            sqlAttri.setTable(
                    EntityTransformer
                            .generateTableNameFromEntity(entityClass)
            );
            this.dataManager.setAttribute(sqlAttri);
            result=this.generateObjectList(
                    this.dataManager
                            .setPageSize(max)
                            .setOrderBy(this.getFindOrderBy())
                            .select(page)
            );
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |InstantiationException
                |NoSuchMethodException
                |InvocationTargetException
                |SQLException ex)
        {
            result=(List<T>)this.logAndReturnNull(ex.toString());
        }
        return result;
    }

    /**
     * 方法（公共）
     * 名称：   find
     * 描述:    获取指定范围的数据表中的数据<br>
     *
     * @param condition - 筛选条件
     * @param page      - 页面标号
     * @param max       - 一页的数量
     * @return List - 数据列表
     */
    @Override
    public List<T> find(Map<String,Object> condition,int page,int max)
    {
        List<T> result=null;
        try
        {
            //读取数据库
            this.dataManager.setAttribute(
                    EntityTransformer
                            .generateSelectSqlFromEntity(entityClass,condition)
            );
            result=this.generateObjectList(
                    this.dataManager
                            .setPageSize(max)
                            .setOrderBy(this.getFindOrderBy())
                            .select(page,EntityTransformer.generateConditionParamsFromEntity(condition))
            );
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |InstantiationException
                |NoSuchMethodException
                |InvocationTargetException
                |SQLException ex)
        {
            result=(List<T>)this.logAndReturnNull(ex.toString());
        }
        return result;
    }

    /**
     * 方法（公共）
     * 名称：   count
     * 描述:    当前数据表中的数据总数量,出现错误返回-1<br>
     *
     * @return int - 数据数量
     */
    @Override
    public int count()
    {
        int result=-1;
        try
        {
            //读取数据库
            SqlAttribute sqlAttri=new SqlAttribute();
            sqlAttri.setTable(
                    EntityTransformer
                            .generateTableNameFromEntity(entityClass)
            );
            this.dataManager.setAttribute(sqlAttri);
            result=this.dataManager.count();
        }
        catch(Exception ex)
        {
            result=this.logAndReturnInt(ex.toString());
        }
        return result;
    }

    /**
     * 方法（公共）
     * 名称：   count
     * 描述:    当前数据表中的数据数量,出现错误返回-1<br>
     *
     * @param condition - 筛选条件
     * @return int - 数据数量
     */
    @Override
    public int count(Map<String,Object> condition)
    {
        int result=-1;
        try
        {
            //读取数据库
            this.dataManager.setAttribute(
                    EntityTransformer
                            .generateSelectSqlFromEntity(entityClass,condition)
            );
            result=this.dataManager.count(
                    EntityTransformer
                            .generateConditionParamsFromEntity(condition)
            );
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |NoSuchMethodException
                |InvocationTargetException ex)
        {
            result=this.logAndReturnInt(ex.toString());
        }
        return result;
    }

    /**
     * 方法（公共）
     *
     * @param id 名称：   exist
     *           描述:    当前数据表中的是否存在指定数据<br>
     * @return boolean - 如果存在，返回true
     */
    @Override
    public boolean exist(Object id)
    {
        boolean result=false;
        try
        {
            //读取数据库
            String keyName=this.getPrimaryKeyName();
            this.dataManager.setAttribute(
                    EntityTransformer
                            .generateSelectSqlFromEntity(entityClass,keyName)
            );
            result=this.dataManager.setPageSize(0).exist(id);
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |NoSuchMethodException
                |InvocationTargetException ex)
        {
            result=this.logAndReturnFalse(ex.toString());
        }
        return result;
    }

    /**
     * 方法（公共）
     * 名称：   exist
     * 描述:    当前数据表中的数据数量<br>
     *
     * @param condition - 筛选条件
     * @return boolean - 如果存在，返回true
     */
    @Override
    public boolean exist(Map<String,Object> condition)
    {
        boolean result=false;
        try
        {
            //读取数据库
            this.dataManager.setAttribute(
                    EntityTransformer
                            .generateSelectSqlFromEntity(entityClass,condition)
            );
            result=this.dataManager.exist(
                    EntityTransformer
                            .generateConditionParamsFromEntity(condition)
            );
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |NoSuchMethodException
                |InvocationTargetException ex)
        {
            result=this.logAndReturnFalse(ex.toString());
        }
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getTotalCount<br>
     * 描述:    获得总数<br>
     *
     * @return int - 总数
     */
    @Override
    public int getTotalCount()
    {
        return this.dataManager.getTotalCount();
    }

    /**
     * 方法（公共）<br>
     * 名称:    getPageSize<br>
     * 描述:    获得一页数据数量<br>
     *
     * @return int - 一页数据数量
     */
    @Override
    public int getPageSize()
    {
        return this.dataManager.getPageSize();
    }

    /**
     * 方法（公共）<br>
     * 名称:    getTotalPage<br>
     * 描述:    获得总页数<br>
     *
     * @return int - 总页数
     */
    @Override
    public int getTotalPage()
    {
        return this.dataManager.getTotalPage();
    }

    /**
     * 方法（公共）<br>
     * 名称:    isUseGenerator<br>
     * 描述:    返回当前管理的数据表的主键是否使用了生成器<br>
     *
     * @return boolean - 当前使用了生成器，返回true
     */
    @Override
    public boolean isUseGenerator()
    {
        return (this.getKeyGenerator()!=null && !"".equals(this.getKeyGenerator()));
    }

    /**
     * 方法（受保护）<br>
     * 名称:    generateObjectList<br>
     * 描述:    根据sql查询结果生成对象列表<br>
     *
     * @param sqlResult - 数据查询结果
     * @return List - 对象列表
     * @throws java.sql.SQLException 异常：SQL错误
     * @throws InstantiationException 异常：
     * @throws IllegalAccessException 异常：
     * @throws NoSuchMethodException 异常：指定的方法不存在
     * @throws java.lang.reflect.InvocationTargetException 异常：
     */
    protected List<T> generateObjectList(ResultSet sqlResult)
            throws SQLException, InstantiationException, IllegalAccessException,
            NoSuchMethodException, IllegalArgumentException,
            InvocationTargetException
    {
        List<T> result=null;
        if(sqlResult!=null)
        {
            result=new ArrayList<>();
            while(sqlResult.next())
            {
                //构造结果对象
                T row=entityClass.newInstance();

                //遍历所有set方法
                List<Field> fields=new ArrayList<>();
                fields=EntityTransformer.getClassAllFields(fields,entityClass);
                String name, setMethodName;
                Method method;
                Object value=null;
                Class type;
                for(Field field : fields)
                {
                    //获取字段名
                    name=field.getName();
                    type=field.getType();
                    //获取属性访问器
                    setMethodName=EntityTransformer.generateSetMethodName(name);
                    method=entityClass.getMethod(setMethodName,type);
                    //获取字段值
                    try
                    {
                        Clob clobValue=sqlResult.getClob(EntityTransformer.transObjNameToDataName(name));
                        if(clobValue!=null)
                        {
                            value=clobValue.getSubString((long)1,(int)clobValue.length());
                        }
                        else
                        {
                            value=sqlResult.getObject(EntityTransformer.transObjNameToDataName(name));
                        }
                    }
                    catch(SQLException ex)
                    {
                        value=sqlResult.getObject(EntityTransformer.transObjNameToDataName(name));
                    }
                    finally
                    {

                    }
                    if(value==null) continue;
                    //处理数据库数字类型与Java中数据类型的转换
                    value=this.transDataToObject(type,value);
                    method.invoke(row,value);
                }
                result.add(row);
            }
        }
        return result;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    transDataToObject<br>
     * 描述:    处理数据库数字类型与Java中数据类型的转换<br>
     *
     * @param type  - 当前实体类中使用的Java数据类型
     * @param value - 当前的数据库中的值
     * @return Object - 转换后的对象
     * @throws java.sql.SQLException 异常：SQL错误
     */
    protected Object transDataToObject(Class type,Object value) throws SQLException
    {
        //处理整形数据
        if(type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class))
        {
            if(value.getClass().isAssignableFrom(BigDecimal.class))
            {
                value=((BigDecimal)value).intValue();
            }
            if(value.getClass().isAssignableFrom(Long.class))
            {
                value=((Long)value).intValue();
            }
            if(value.getClass().isAssignableFrom(String.class))
            {
                value=Integer.parseInt((String)value);
            }
        }
        //处理浮点型数据
        if(type.isAssignableFrom(float.class) || type.isAssignableFrom(Float.class))
        {
            if(value.getClass().isAssignableFrom(BigDecimal.class))
            {
                value=((BigDecimal)value).floatValue();
            }
            if(value.getClass().isAssignableFrom(Long.class))
            {
                value=((Long)value).floatValue();
            }
        }
        //处理double型数据
        if(type.isAssignableFrom(double.class) || type.isAssignableFrom(Double.class))
        {
            if(value.getClass().isAssignableFrom(BigDecimal.class))
            {
                value=((BigDecimal)value).doubleValue();
            }
            if(value.getClass().isAssignableFrom(Long.class))
            {
                value=((Long)value).doubleValue();
            }
        }
        //处理CLOB型数据
         /*if("oracle.sql.CLOB".equals(value.getClass().getName()))
         {
             CLOB clob=(CLOB)value;
             if(clob != null)
             {
                value = clob.getSubString((long)1,(int)clob.length());
             }
         }*/
        return value;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    logAndReturnFalse<br>
     * 描述:    记录错误信息<br>
     *
     * @param error - 错误信息
     * @return boolean - 返回false
     */
    protected boolean logAndReturnFalse(String error)
    {
        this.setError(error);
        return false;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    logAndReturnNull<br>
     * 描述:    记录错误信息<br>
     *
     * @param error - 错误信息
     * @return Object - 返回null
     */
    protected Object logAndReturnNull(String error)
    {
        this.setError(error);
        return null;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    logAndReturnNull<br>
     * 描述:    记录错误信息<br>
     *
     * @param error - 错误信息
     * @return int - 返回-1
     */
    protected int logAndReturnInt(String error)
    {
        this.setError(error);
        return -1;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   error<br>
     *
     * @return String - 数据库连接字符串
     */
    @Override
    public String getError()
    {
        return this.error;
    }

    /**
     * 更改器(受保护的)<br>
     * 目标：   error
     *
     * @param error - 错误信息
     */
    protected void setError(String error)
    {
        this.error=error;
    }

    /**
     * 方法（公共）<br>
     * 名称:    transactionBegin<br>
     * 描述:    事务处理开始<br>
     *
     * @return 事物启动是否成功
     */
    @Override
    public boolean transactionBegin()
    {
        if(this.connector.getConnection()==null)
        {
            this.setError("数据库连接对象为null");
            return false;
        }
        if(!this.connector.isConnected()) this.connector.connect();
        try
        {
            this.connector.getConnection().setAutoCommit(false);
            return true;
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
            return false;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    transactionRollBack<br>
     * 描述:    事务处理回滚<br>
     *
     * @return boolean - 是否成功
     */
    @Override
    public boolean transactionRollBack()
    {
        if(this.connector.getConnection()==null)
        {
            this.setError("数据库连接对象为null");
            return false;
        }
        if(!this.connector.isConnected()) this.connector.connect();
        try
        {
            this.connector.getConnection().rollback();
            return true;
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
            return false;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    transactionCommit<br>
     * 描述:    事务处理提交<br>
     *
     * @return boolean - 是否成功
     */
    @Override
    public boolean transactionCommit()
    {
        if(this.connector.getConnection()==null)
        {
            this.setError("数据库连接对象为null");
            return false;
        }
        if(!this.connector.isConnected()) this.connector.connect();
        try
        {
            this.connector.getConnection().commit();
            return true;
        }
        catch(SQLException ex)
        {
            this.setError(ex.getMessage());
            return false;
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    getCannotEditParams<br>
     * 描述:    获取当前不可修改的属性<br>
     *
     * @return String - 当前不可更改的属性，多个属性之间用|分隔
     */
    @Override
    public String getCannotEditParams()
    {
        return "";
    }

    /**
     * 描述：   设置当前排序字段
     *
     * @param orderBy - 当前排序设置
     */
    @Override
    public void setOrderBy(String orderBy)
    {
        this.orderBy=orderBy;
    }
}
