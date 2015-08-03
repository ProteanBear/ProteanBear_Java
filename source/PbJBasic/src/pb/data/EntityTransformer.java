package pb.data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * 实体类与SQL之间的转换方法类。<br>
 * 1.07 - 增加条件Map为null的判断。<br>
 * 1.06 - 增加对查询时自定义数据表名称的支持。<br>
 * 1.05 - 修改getClassAllFields为公共方法。<br>
 * 1.04 - 修改类属性获取方法，增加父类属性获取方法。<br>
 * 1.03 - 修改generateParamListFromEntity方法，加入字段为null的判断。<br>
 * 1.02 - 为updateEntityByHttpRequest方法增加设置是否可以更改主键的参数。<br>
 * 1.01 - 在添加Sql生成时增加主键生成器非空字符串（""）判断，用于非自动生成主键的数据添加<br>
 * 1.00 - 测试增删改查操作通过。<br>
 *
 * @author proteanBear(马强)
 * @version 1.07 2012/04/09
 */
public class EntityTransformer
{
    /**
     * 静态方法（公共）<br>
     * 名称:    generateInsertSqlFromEntity<br>
     * 描述:    通过实体类生成插入Sql对象<br>
     *
     * @param entityClass    - 管理器对应的实体类
     * @param entity         - 实体类对象
     * @param primaryKeyName - 主键字段名称
     * @param generator      - 主键生成器
     * @return SqlAttribute - Sql处理对象
     * @throws NoSuchMethodException                       异常：
     * @throws IllegalAccessException                      异常：
     * @throws java.lang.reflect.InvocationTargetException 异常：
     */
    public static SqlAttribute generateInsertSqlFromEntity
    (Class entityClass,Object entity,String primaryKeyName,String generator)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        SqlAttribute result=new SqlAttribute();

        //设置表名
        result.setTable(generateTableNameFromEntity(entityClass));

        //遍历实体类获取相应的值
        List<Field> fields=new ArrayList<>();
        fields=getClassAllFields(fields,entityClass);
        String name;
        for(Field field : fields)
        {
            name=field.getName();
            if(primaryKeyName.equals(name) && generator!=null && !"".equals(generator))
            {
                //使用生成器
                if(!AbstractEntityManager.NOT_DISPLAY_KEY.equals(generator))
                {
                    result.addColumnName(transObjNameToDataName(name));
                    result.addColumnValue(generator);
                }
                continue;
            }
            result.addColumnName(transObjNameToDataName(name));
            result.addColumnValue("?");
        }

        return result;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    generateUpdateSqlFromEntity<br>
     * 描述:    通过实体类生成插入Sql对象<br>
     *
     * @param entityClass    - 管理器对应的实体类
     * @param entity         - 实体类对象
     * @param primaryKeyName - 主键字段名称
     * @return SqlAttribute - Sql处理对象
     * @throws NoSuchMethodException                       异常：
     * @throws IllegalAccessException                      异常：
     * @throws java.lang.reflect.InvocationTargetException 异常：
     */
    public static SqlAttribute generateUpdateSqlFromEntity
    (Class entityClass,Object entity,String primaryKeyName)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        SqlAttribute result=new SqlAttribute();

        //设置表名
        result.setTable(generateTableNameFromEntity(entityClass));

        //遍历实体类获取相应的值
        List<Field> fields=new ArrayList<>();
        fields=getClassAllFields(fields,entityClass);
        String name;
        for(Field field : fields)
        {
            name=transObjNameToDataName(field.getName());

            //字符串属性
            result.addColumnSet(name,"?");
        }

        //设置主键条件
        result.addCondition(transObjNameToDataName(primaryKeyName)+"=?");

        return result;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    generateSelectSqlFromEntity<br>
     * 描述:    通过实体类生成删除Sql对象<br>
     *
     * @param entityClass    - 管理器对应的实体类
     * @param primaryKeyName - 主键字段名称
     * @return SqlAttribute - Sql处理对象
     * @throws NoSuchMethodException                       异常：
     * @throws IllegalAccessException                      异常：
     * @throws java.lang.reflect.InvocationTargetException 异常：
     */
    public static SqlAttribute generateSelectSqlFromEntity
    (Class entityClass,String primaryKeyName)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        SqlAttribute result=new SqlAttribute();

        //设置表名
        result.setTable(generateTableNameFromEntity(entityClass));

        //设置主键条件
        result.addCondition(transObjNameToDataName(primaryKeyName)+"=?");

        return result;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    generateSelectSqlFromEntity<br>
     * 描述:    通过实体类生成删除Sql对象<br>
     *
     * @param entityClass    - 管理器对应的实体类
     * @param primaryKeyName - 主键字段名称
     * @param tableName      - 数据表名称
     * @return SqlAttribute - Sql处理对象
     * @throws NoSuchMethodException                       异常：
     * @throws IllegalAccessException                      异常：
     * @throws java.lang.reflect.InvocationTargetException 异常：
     */
    public static SqlAttribute generateSelectSqlFromEntity
    (Class entityClass,String primaryKeyName,String tableName)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        SqlAttribute result=new SqlAttribute();

        //设置表名
        result.setTable(tableName);

        //设置主键条件
        result.addCondition(transObjNameToDataName(primaryKeyName)+"=?");

        return result;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    generateSelectSqlFromEntity<br>
     * 描述:    通过实体类生成删除Sql对象<br>
     *
     * @param entityClass - 管理器对应的实体类
     * @param condition   - 筛选字段
     * @return SqlAttribute - Sql处理对象
     * @throws NoSuchMethodException                       异常：
     * @throws IllegalAccessException                      异常：
     * @throws java.lang.reflect.InvocationTargetException 异常：
     */
    public static SqlAttribute generateSelectSqlFromEntity
    (Class entityClass,Map<String,Object> condition)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        SqlAttribute result=new SqlAttribute();

        //设置表名
        result.setTable(generateTableNameFromEntity(entityClass));

        //设置条件
        if(condition!=null)
        {
            for(String con : condition.keySet())
            {
                result.addCondition(transObjNameToDataName(con));
            }
        }

        return result;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    generateSelectSqlFromEntity<br>
     * 描述:    通过实体类生成删除Sql对象<br>
     *
     * @param entityClass - 管理器对应的实体类
     * @param condition   - 筛选字段
     * @param tableName   - 数据表名称
     * @return SqlAttribute - Sql处理对象
     * @throws NoSuchMethodException                       异常：
     * @throws IllegalAccessException                      异常：
     * @throws java.lang.reflect.InvocationTargetException 异常：
     */
    public static SqlAttribute generateSelectSqlFromEntity
    (Class entityClass,Map<String,Object> condition,String tableName)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        SqlAttribute result=new SqlAttribute();

        //设置表名
        result.setTable(tableName);

        //设置条件
        if(condition!=null)
        {
            for(String con : condition.keySet())
            {
                result.addCondition(transObjNameToDataName(con));
            }
        }

        return result;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    generateTableNameFromEntity<br>
     * 描述:    通过实体类获取数据表名称<br>
     * 转换原则 - 类名TableName->表名table_name
     *
     * @param entityClass - 管理器对应的实体类
     * @return String - 数据表名称
     */
    public static String generateTableNameFromEntity(Class entityClass)
    {
        //获取类名
        String className=entityClass.getSimpleName();
        return transObjNameToDataName(className);
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    generateObjNameFromTableName<br>
     * 描述:    通过数据表获取实体类名称<br>
     * 转换原则 - 表名table_name->类名TableName
     *
     * @param tableName  - 数据表名称
     * @param firstUpper - 首字母是否大写
     * @return String - 类名称
     */
    public static String generateObjNameFromTableName(String tableName,boolean firstUpper)
    {
        String ObjName="";
        tableName=tableName.toLowerCase();

        //遍历类名字母
        char[] names=tableName.toCharArray();
        String last=names[0]+"";
        last=firstUpper?last.toUpperCase():last;
        for(int i=1;i<names.length;i++)
        {
            String name=names[i]+"";
            //如果上一个字母为_
            if("_".equals(last)) name=name.toUpperCase();
            ObjName+=("_".equals(last))?"":last;
            last=name;
        }
        ObjName+=("_".equals(last))?"":last;

        return ObjName;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    transObjNameToDataName<br>
     * 描述:    转换对象名称为数据库中名称<br>
     * 转换原则 - 类名TableName->表名table_name
     *
     * @param objName - 对象名称
     * @return String - 数据表名称
     */
    public static String transObjNameToDataName(String objName)
    {
        String dataName="";

        //遍历类名字母
        char[] names=objName.toCharArray();
        for(int i=0;i<names.length;i++)
        {
            String name=names[i]+"";
            //如果为大写字母,转换为“_”+“小写字母”
            if(Pattern.matches("[A-Z]",name)) name=((i==0)?"":"_")+name.toLowerCase();
            dataName+=name;
        }

        return dataName;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    generateParamListFromEntity<br>
     * 描述:    通过实体类生成Sql执行替换参数<br>
     *
     * @param entityClass    - 管理器对应的实体类
     * @param entity         - 实体类对象
     * @param primaryKeyName - 主键字段名称
     * @param generator      - 主键生成器
     * @return List<String> - 替换参数列表
     */
    private static List<String> generateParamListFromEntity
    (Class entityClass,Object entity,String primaryKeyName,String generator)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        List<Field> fields=new ArrayList<>();
        fields=getClassAllFields(fields,entityClass);
        String name, getMethodName;
        Method method;
        Object obj;
        List<String> result=new ArrayList<>();

        for(Field field : fields)
        {
            //获取字段名
            name=field.getName();
            //判断主键参数显示
            if(generator!=null && !"".equals(generator) && primaryKeyName.equals(name))
            {
                continue;
            }
            //获取属性访问器
            getMethodName=generateGetMethodName(name);
            method=entityClass.getMethod(getMethodName);
            //获取字段值
            obj=method.invoke(entity);
            result.add((obj==null)?"":(obj+""));
        }

        return result;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    generateInsertParamsFromEntity<br>
     * 描述:    通过实体类生成插入Sql执行替换参数<br>
     *
     * @param entityClass    - 管理器对应的实体类
     * @param entity         - 实体类对象
     * @param primaryKeyName - 主键字段名称
     * @param generator      - 主键生成器
     * @return Object[] - 替换参数数组
     * @throws NoSuchMethodException                       异常：
     * @throws IllegalAccessException                      异常：
     * @throws java.lang.reflect.InvocationTargetException 异常：
     */
    public static Object[] generateInsertParamsFromEntity
    (Class entityClass,Object entity,String primaryKeyName,String generator)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        return generateParamListFromEntity(entityClass,entity,primaryKeyName,generator).toArray();
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    generateConditionParamsFromEntity<br>
     * 描述:    通过实体类生成查询Sql执行替换参数<br>
     *
     * @param condition - 筛选字段
     * @return Object[] - 替换参数数组
     */
    public static Object[] generateConditionParamsFromEntity
    (Map<String,Object> condition)
    {
        List<Object> result=new ArrayList<>();
        if(condition!=null)
        {
            for(String key : condition.keySet())
            {
                result.add(condition.get(key));
            }
        }
        return result.toArray();
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    generateParamsFromEntity<br>
     * 描述:    通过实体类生成Sql执行替换参数<br>
     *
     * @param entityClass    - 管理器对应的实体类
     * @param entity         - 实体类对象
     * @param primaryKeyName - 主键字段名称
     * @return Object[] - 替换参数数组
     * @throws NoSuchMethodException                       异常：
     * @throws IllegalAccessException                      异常：
     * @throws java.lang.reflect.InvocationTargetException 异常：
     */
    public static Object[] generateParamsFromEntity
    (Class entityClass,Object entity,String primaryKeyName)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        List<String> result=generateParamListFromEntity(entityClass,entity,primaryKeyName,null);
        result.add(getPrimaryKeyValueFromEntity(entityClass,entity,primaryKeyName));
        return result.toArray();
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    getPrimaryKeyValueFromEntity<br>
     * 描述:    通过实体类生成Sql执行替换参数<br>
     *
     * @param entityClass    - 管理器对应的实体类
     * @param entity         - 实体类对象
     * @param primaryKeyName - 主键字段名称
     * @return String[] - 替换参数数组
     * @throws NoSuchMethodException                       异常：
     * @throws IllegalAccessException                      异常：
     * @throws java.lang.reflect.InvocationTargetException 异常：
     */
    public static String getPrimaryKeyValueFromEntity
    (Class entityClass,Object entity,String primaryKeyName)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        Method getPrimaryKeyValue=entityClass.getMethod(generateGetMethodName(primaryKeyName));
        String keyValue=getPrimaryKeyValue.invoke(entity)+"";
        return keyValue;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    generateGetMethodName<br>
     * 描述:    获取属性访问器方法名称<br>
     *
     * @param name - 主键字段名称
     * @return String - 属性访问器方法名称
     */
    public static String generateGetMethodName(String name)
    {
        if(name==null || "".equals(name.trim())) return null;
        if(name.trim().length()<2) return null;
        return "get"+name.substring(0,1).toUpperCase()+name.substring(1);
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    generateSetMethodName<br>
     * 描述:    获取属性更改器方法名称<br>
     *
     * @param name - 主键字段名称
     * @return String - 属性访问器方法名称
     */
    public static String generateSetMethodName(String name)
    {
        if(name==null || "".equals(name.trim())) return null;
        if(name.trim().length()<2) return null;
        return "set"+name.substring(0,1).toUpperCase()+name.substring(1);
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    updateEntityByHttpRequest<br>
     * 描述:    根据给定的参数将参数指定数据更新到指定的实体对象上<br>
     *
     * @param entityClass      - 管理器对应的实体类
     * @param entity           - 指定的实体类
     * @param request          - 请求参数对象
     * @param primaryKeyName   - 主键字段名称
     * @param isEditPrimaryKey - 是否可以更改主键
     * @return String - 属性访问器方法名称
     * @throws NoSuchMethodException                       异常：
     * @throws InstantiationException                      异常：
     * @throws IllegalAccessException                      异常：
     * @throws java.lang.reflect.InvocationTargetException 异常：
     */
    public static Object updateEntityByHttpRequest
    (Class entityClass,Object entity,
     HttpServletRequest request,String primaryKeyName,boolean isEditPrimaryKey)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        List<Field> fields=new ArrayList<>();
        fields=getClassAllFields(fields,entityClass);
        String name, value, setMethodName;
        Method method;
        Class type;
        Constructor constructor;

        for(Field field : fields)
        {
            //获取字段名
            name=field.getName();
            //获取字段类型
            type=field.getType();
            //从Http请求中获取字段对应参数值
            value=request.getParameter(name);
            //判断主键参数,主键参数不可修改
            if((primaryKeyName.equals(name) && !isEditPrimaryKey)
                    || value==null)
            {
                continue;
            }
            //获取属性访问器
            setMethodName=generateSetMethodName(name);
            try
            {
                method=entityClass.getMethod(setMethodName,type);
                if(method==null) continue;

                constructor=type.getConstructor(String.class);
                if(constructor==null) continue;
                method.invoke(entity,constructor.newInstance(value));
            }
            catch(NoSuchMethodException ex)
            {
                method=entityClass.getMethod(setMethodName,String.class);
                if(method==null) continue;
                method.invoke(entity,value);
            }
        }

        return entity;
    }

    /**
     * 静态方法（公共）<br>
     * 名称:    updateEntityByHttpRequest<br>
     * 描述:    根据给定的参数将参数指定数据更新到指定的实体对象上<br>
     *
     * @param entityClass      - 管理器对应的实体类
     * @param entity           - 指定的实体类
     * @param request          - 请求参数对象
     * @param primaryKeyName   - 主键字段名称
     * @param isEditPrimaryKey - 是否可以更改主键
     * @param cannotEditParams - 不可修改的字段属性
     * @return String - 属性访问器方法名称
     * @throws NoSuchMethodException                       异常：
     * @throws InstantiationException                      异常：
     * @throws IllegalAccessException                      异常：
     * @throws java.lang.reflect.InvocationTargetException 异常：
     */
    public static Object updateEntityByHttpRequest
    (Class entityClass,Object entity,
     HttpServletRequest request,String primaryKeyName,
     boolean isEditPrimaryKey,String cannotEditParams)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        List<Field> fields=new ArrayList<>();
        fields=getClassAllFields(fields,entityClass);
        String name, value, setMethodName;
        Method method;
        Class type;
        Constructor constructor;

        for(Field field : fields)
        {
            //获取字段名
            name=field.getName();
            //获取字段类型
            type=field.getType();
            //从Http请求中获取字段对应参数值
            value=request.getParameter(name);
            //判断主键参数,主键参数不可修改
            if((primaryKeyName.equals(name) && !isEditPrimaryKey)
                    || value==null || cannotEditParams.contains(name))
            {
                continue;
            }
            //获取属性访问器
            setMethodName=generateSetMethodName(name);
            try
            {
                method=entityClass.getMethod(setMethodName,type);
                if(method==null) continue;

                constructor=type.getConstructor(String.class);
                if(constructor==null) continue;
                method.invoke(entity,constructor.newInstance(value));
            }
            catch(NoSuchMethodException ex)
            {
                method=entityClass.getMethod(setMethodName,String.class);
                if(method==null) continue;
                method.invoke(entity,value);
            }
        }

        return entity;
    }

    /**
     * 静态方法（私有）<br>
     * 名称:    updateEntityByOtherEntity<br>
     * 描述:    复制其他对象的属性到当前对象中<br>
     *
     * @param entity     -
     * @param fromEntity - 类
     * @return Object - 复制后的对象
     * @throws IllegalAccessException                      异常：
     * @throws java.lang.reflect.InvocationTargetException 异常：
     * @throws NoSuchMethodException                       异常：
     * @throws InstantiationException                      异常：
     */
    public static Object updateEntityByOtherEntity(Object entity,Object fromEntity)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, InstantiationException
    {
        Class entityClass=entity.getClass();
        Class fromClass=fromEntity.getClass();
        List<Field> fields=new ArrayList<>();
        fields=getClassAllFields(fields,entityClass);
        String name, setMethodName, getMethodName;
        Method method;
        Object value;
        Class type;
        Constructor constructor;

        for(Field field : fields)
        {
            //获取字段名
            name=field.getName();
            //获取字段类型
            type=field.getType();
            //获取属性访问器
            getMethodName=generateGetMethodName(name);
            try
            {
                method=fromClass.getMethod(getMethodName);
                if(method==null) continue;
                value=method.invoke(fromEntity)+"";
            }
            catch(NoSuchMethodException ex)
            {
                continue;
            }
            //获取属性访问器
            setMethodName=generateSetMethodName(name);
            try
            {
                method=entityClass.getMethod(setMethodName,type);
                if(method==null) continue;

                constructor=type.getConstructor(String.class);
                if(constructor==null) continue;
                method.invoke(entity,constructor.newInstance(value));
            }
            catch(NoSuchMethodException ex)
            {
                method=entityClass.getMethod(setMethodName,String.class);
                if(method==null) continue;
                method.invoke(entity,value);
            }
        }

        return entity;
    }

    /**
     * 静态方法（私有）<br>
     * 名称:    getClassAllFields<br>
     * 描述:    获取类所有的属性<br>
     *
     * @param fields
     * @param entityClass - 类
     * @return List - 属性列表
     */
    public static List<Field> getClassAllFields(List<Field> fields,Class entityClass)
    {
        Field[] result=entityClass.getDeclaredFields();
        fields.addAll(Arrays.asList(result));

        Class superClass=entityClass.getSuperclass();
        if(!superClass.isAssignableFrom(Object.class)) fields=getClassAllFields(fields,superClass);

        return fields;
    }
     
    /*public static void main(String[] args) throws NoSuchMethodException    
    {
        System.out.println(EntityTransformer.generateObjNameFromTableName("table_name_test"));
    }*/
}
