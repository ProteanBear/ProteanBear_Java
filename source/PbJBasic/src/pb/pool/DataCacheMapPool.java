package pb.pool;

/**
 * 通用数据值名对缓冲池类。<br>
 * 继承AbstractCachePool抽象类，实现列表方式储存数据对象的缓冲池，使用ConcurrentHashMap同步及高并发列表类缓存数据<br>
 * 1.00 —— 实现基本缓存池方法
 *
 * @param <K>
 * @param <T>
 * @author proteanBear(马强)
 * @version 1.00 2012/03/20
 * @since 1.6
 */
public class DataCacheMapPool<K,T> extends AbstractCachePool
{
    /**
     * 构造函数（公共）<br>
     * 描述：    初始化域
     */
    public DataCacheMapPool()
    {
        super();
        this.initPool();
    }

    /**
     * 方法（受保护、实现、不可继承）<br>
     * 名称:    initPool<br>
     * 描述:    初始化缓冲池<br>
     */
    @Override
    protected final void initPool()
    {
        this.initCacheMap();
    }

    /**
     * 方法（公共）<br>
     * 名称:    addData<br>
     * 描述:    添加新的数据到缓冲池中（队列）<br>
     *
     * @param key  - 新数据的键名索引
     * @param data - 新数据对象
     * @return boolean - 添加成功，返回true；否则，返回false。
     */
    public boolean addData(K key,T data)
    {
        return this.addToPool(key,data);
    }

    /**
     * 方法（公共）<br>
     * 名称:    editData<br>
     * 描述:    更新缓冲池中指定位置的数据为指定数据内容（队列）<br>
     *
     * @param key  - 数据的键名索引
     * @param data - 更新的数据对象
     * @return boolean - 更新成功，返回true；否则，返回false。
     */
    public boolean editData(K key,T data)
    {
        return this.editInPool(key,data);
    }

    /**
     * 方法（公共）<br>
     * 名称:    deleteData<br>
     * 描述:    删除缓冲池中指定位置的数据为指定数据内容（队列）<br>
     *
     * @param key - 数据的键名索引或队列中的对象
     * @return boolean - 更新成功，返回true；否则，返回false。
     */
    public boolean deleteData(K key)
    {
        return this.deleteFromPool(key);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getData<br>
     * 描述:    从缓冲池中获取指定索引的数据(列表)<br>
     *
     * @param key - 键名索引
     * @return T - 数据对象
     */
    public T getData(K key)
    {
        return (T)this.getFromPool(key);
    }

    /**
     * 方法（公共）<br>
     * 名称:    containData<br>
     * 描述:    判断缓冲池中是否有指定的数据，如果有则返回true；否则，返回false<br>
     *
     * @param data - 数据对象
     * @return boolean - 指定的数据对象是否存在
     */
    public boolean containData(T data)
    {
        return this.containMap(data);
    }

    /**
     * 方法（公共）<br>
     * 名称:    indexOf<br>
     * 描述:    判断缓冲池中是否有指定的数据，如果有则返回位置索引；否则，返回-1<br>
     *
     * @param data - 数据对象
     * @return int - 位置索引
     */
    public K indexOf(T data)
    {
        return (K)this.indexOfMap(data);
    }

    /**
     * 方法（公共）<br>
     * 名称:    clear<br>
     * 描述:    清空缓冲池<br>
     */
    public void clear()
    {
        this.clearPool();
    }

    /**
     * 方法（受保护）<br>
     * 名称:    size<br>
     * 描述:    缓冲池当前大小。<br>
     * 如果列表和值名对对象都不为空，则缓冲池大小为二者之和
     *
     * @return int - 缓冲池大小
     */
    @Override
    public int size()
    {
        return super.size();
    }
}
