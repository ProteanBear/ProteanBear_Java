package pb.data;

import java.util.List;
import java.util.Map;

/**
 * 实体类回话bean的数据接口的抽象父类。<br>
 * 声明和实体类增删改查的相关数据处理方法。<br>
 * 1.06  -  增加最新生成主键的获取接口方法。<br>
 * 1.05  -  增加是否使用了生成器的接口方法。<br>
 * 1.04  -  增加获取主键名、总数、总页数等相关接口方法。<br>
 * 1.03  -  增加通过主键删除数据的接口方法<br>
 * 1.02  -  新增查看数据是否存在的相关接口方法。<br>
 * 1.01  -  增加错误获取接口方法。<br>
 *
 * @param <T> 泛型，对应的实体类
 * @author proteanBear(马强)
 * @version 1.06 2011/11/24
 */
public interface AbstractFacadeLocal<T>
{
    /**
     * 描述:    添加一条新的记录到数据库中<br>
     *
     * @param entity - 添加记录的描述对象
     * @return boolean - 添加成功，返回true;
     */
    boolean create(T entity);

    /**
     * 描述:    更新指定记录的相关数据到数据库中<br>
     *
     * @param entity - 数据更新的描述对象
     * @return boolean - 更新成功，返回true;
     */
    boolean edit(T entity);

    /**
     * 描述:    从数据库中删除指定主键的记录<br>
     *
     * @param id - 删除的记录标识
     * @return boolean - 删除成功，返回true;
     */
    boolean removeById(Object id);

    /**
     * 描述:    从数据库中删除指定的记录<br>
     *
     * @param entity - 删除的记录描述对象
     * @return boolean - 删除成功，返回true;
     */
    boolean remove(T entity);

    /**
     * 描述:    通过主键查找指定主键的数据表描述对象<br>
     *
     * @param id - 主键
     * @return T - 描述对象
     */
    T find(Object id);

    /**
     * 描述:    获取数据表中的所有数据<br>
     *
     * @return List - 数据列表
     */
    List<T> findAll();

    /**
     * 描述:    获取指定范围的数据表中的数据<br>
     *
     * @param condition - 筛选条件
     * @return List - 数据列表
     */
    List<T> find(Map<String,Object> condition);

    /**
     * 描述:    获取指定范围的数据表中的数据<br>
     *
     * @param page - 页面标号
     * @param max  - 一页的数量
     * @return List - 数据列表
     */
    List<T> find(int page,int max);

    /**
     * 描述:    获取指定范围的数据表中的数据<br>
     *
     * @param condition - 筛选条件
     * @param page      - 页面标号,从1开始
     * @param max       - 一页的数量
     * @return List - 数据列表
     */
    List<T> find(Map<String,Object> condition,int page,int max);

    /**
     * 描述:    当前数据表中的数据数量<br>
     *
     * @return int - 数据数量
     */
    int count();

    /**
     * 描述:    当前数据表中的数据数量<br>
     *
     * @param condition - 筛选条件
     * @return int - 数据数量
     */
    int count(Map<String,Object> condition);

    /**
     * 描述:    当前数据表中的是否存在指定数据<br>
     *
     * @param id 数据主键
     * @return boolean - 如果存在，返回true
     */
    boolean exist(Object id);

    /**
     * 描述:    当前数据表中的数据数量<br>
     *
     * @param condition - 筛选条件
     * @return boolean - 如果存在，返回true
     */
    boolean exist(Map<String,Object> condition);

    /**
     * 描述：   获取错误信息<br>
     *
     * @return String - 错误信息
     */
    String getError();

    /**
     * 描述:    获取数据表主键字段名称<br>
     *
     * @return String - 获取主键名称
     */
    String getPrimaryKeyName();

    /**
     * 描述:    获得总数<br>
     *
     * @return int - 总数
     */
    int getTotalCount();

    /**
     * 描述:    获得一页数据数量<br>
     *
     * @return int - 一页数据数量
     */
    int getPageSize();

    /**
     * 描述:    获得总页数<br>
     *
     * @return int - 总页数
     */
    int getTotalPage();

    /**
     * 描述:    返回当前管理的数据表的主键是否使用了生成器<br>
     *
     * @return boolean - 当前使用了生成器，返回true
     */
    boolean isUseGenerator();

    /**
     * 描述:    获取最新生成的主键<br>
     *
     * @return String - 最新生成的主键（仅适用于自定义生成主键的获取）
     */
    String getLastGenerator();

    /**
     * 描述:    事务处理开始<br>
     *
     * @return 是否成功
     */
    boolean transactionBegin();

    /**
     * 描述:    事务处理回滚<br>
     *
     * @return boolean - 是否成功
     */
    boolean transactionRollBack();

    /**
     * 描述:    事务处理提交<br>
     *
     * @return boolean - 是否成功
     */
    boolean transactionCommit();

    /**
     * 描述:    获取当前不可修改的属性<br>
     *
     * @return String - 当前不可更改的属性，多个属性之间用|分隔
     */
    String getCannotEditParams();

    /**
     * 描述：   设置当前排序字段
     *
     * @param orderBy - 当前排序设置
     */
    void setOrderBy(String orderBy);
}
