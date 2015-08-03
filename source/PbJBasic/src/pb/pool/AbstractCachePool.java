package pb.pool;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 缓冲池接口实现抽象基类。<br>
 * 抽象实现缓冲池处理接口，设置各种缓冲池通用属性和通用方法。<br>
 * 支持同时使用列表和值名对两种方式储存缓存数据。<br>
 * 1.05 —— 添加阻塞队列和并发队列方式的存储结构。<br>
 * 1.04 —— 去除清空缓存池时设置列表和值名对为空的操作。<br>
 * 1.03 —— 添加editKeyInPool方法，用于更新数据的键名<br>
 * 1.02 —— 修改列表中数据的删除方法，取消先读出数据的方法，修正为调用remove(index)方法，并捕捉异常。<br>
 * 1.01 —— 增加缓存池名称字段和访问器、设置器。<br>
 * 1.00 —— 创建类，基本实现方法。<br>
 *
 * @author proteanBear(马强)
 * @version 1.05 2011/12/29
 */
public abstract class AbstractCachePool
{
    /**
     * 域(私有)<br>
     * 名称:    poolName<br>
     * 描述:    缓存池名称<br>
     */
    private String poolName;

    /**
     * 域(私有)<br>
     * 名称:    cacheMap<br>
     * 描述:    记录使用值名对方式的缓存信息<br>
     */
    private ConcurrentHashMap<Object,Object> cacheMap;

    /**
     * 域(私有)<br>
     * 名称:    cacheList<br>
     * 描述:    记录使用列表方式的缓存信息<br>
     */
    private CopyOnWriteArrayList<Object> cacheList;

    /**域(私有)<br>
     * 名称:    cacheQueue<br>
     * 描述:    记录使用阻塞队列方式的缓存信息<br>
     *///private LinkedBlockingQueue<Object> cacheBlockQueue;

    /**域(私有)<br>
     * 名称:    cacheQueue<br>
     * 描述:    记录使用并发队列方式的缓存信息<br>
     *///private ConcurrentLinkedQueue<Object> cacheConQueue;

    /**
     * 域(私有)<br>
     * 名称:    error<br>
     * 描述:    记录进行操作时出现的错误信息<br>
     */
    private String error;

    /**
     * 方法（受保护）<br>
     * 名称:    initCacheMap<br>
     * 描述:    初始化值名对缓冲池,使用哈希MAP并设置为同步结构<br>
     */
    protected void initCacheMap()
    {
        if(this.cacheMap==null)
        {
            this.cacheMap=new ConcurrentHashMap<>();
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    initCacheList<br>
     * 描述:    初始化列表缓冲池,使用ArrayList并设置为同步结构<br>
     */
    protected void initCacheList()
    {
        if(this.cacheList==null)
        {
            this.cacheList=new CopyOnWriteArrayList<>();
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    initCacheMap<br>
     * 描述:    初始化值名对缓冲池,使用哈希MAP并设置为同步结构<br>
     */
    /*protected void initCacheBlockQueue()
    {
        if(this.cacheBlockQueue==null)
        {
            this.cacheBlockQueue=new LinkedBlockingQueue<Object>();
        }
    }*/

    /**
     * 方法（受保护）<br>
     * 名称:    initCacheList<br>
     * 描述:    初始化列表缓冲池,使用ArrayList并设置为同步结构<br>
     */
    /*protected void initCacheConQueue()
    {
        if(this.cacheConQueue==null)
        {
            this.cacheConQueue=new ConcurrentLinkedQueue<Object>();
        }
    }*/

    /**
     * 方法（受保护、抽象）<br>
     * 名称:    initPool<br>
     * 描述:    初始化缓冲池<br>
     */
    protected abstract void initPool();

    /**
     * 方法（受保护）<br>
     * 名称:    addToPool<br>
     * 描述:    添加新的数据到缓冲池中（队列）<br>
     *
     * @param data - 新数据对象
     * @return boolean - 添加成功，返回true；否则，返回false。
     */
    protected boolean addToPool(Object data)
    {
        //错误检查
        if(this.cacheList==null/*||this.cacheBlockQueue==null||this.cacheConQueue==null*/)
        {
            this.setError("缓冲池对象为空，未对缓冲池进行初始化！");
            return false;
        }
        if(data==null)
        {
            this.setError("添加的新数据对象为空！");
            return false;
        }

        boolean success=false;
        if(this.cacheList!=null) success=this.cacheList.add(data);
        //if(this.cacheBlockQueue!=null)    success=this.cacheBlockQueue.add(data);
        //if(this.cacheConQueue!=null)      success=this.cacheConQueue.add(data);
        return success;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    addToPool<br>
     * 描述:    添加新的数据到缓冲池中指定位置（队列）<br>
     *
     * @param index - 位置索引
     * @param data  - 新数据对象
     * @return boolean - 添加成功，返回true；否则，返回false。
     */
    protected boolean addToPool(int index,Object data)
    {
        //错误检查
        if(this.cacheList==null)
        {
            this.setError("缓冲池对象为空，未对缓冲池进行初始化！");
            return false;
        }
        if(data==null)
        {
            this.setError("添加的新数据对象为空！");
            return false;
        }

        //添加新数据
        boolean success=false;
        if(index>this.cacheList.size())
        {
            success=this.cacheList.add(data);
        }
        else
        {
            this.cacheList.add(index,data);
            success=true;
        }
        return success;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    addToPool<br>
     * 描述:    添加新的数据到缓冲池中（值名对）<br>
     *
     * @param key     - 新数据的键名索引
     * @param content - 新数据对象
     * @return boolean - 添加成功，返回true；否则，返回false。
     */
    protected boolean addToPool(Object key,Object content)
    {
        try
        {
            //错误检查
            if(this.cacheMap==null)
            {
                this.setError("缓冲池对象为空，未对缓冲池进行初始化！");
                return false;
            }
            if(content==null || key==null)
            {
                this.setError("指定的数据键名或数据内容为空！");
                return false;
            }

            //添加新数据
            this.cacheMap.put(key,content);
            return true;
        }
        catch(Exception ex)
        {
            this.setError(ex.toString());
            return false;
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    deleteFromPool<br>
     * 描述:    将指定位置的数据对象从缓冲池中删除（列表）<br>
     *
     * @param index - 位置索引
     * @return boolean - 删除成功，返回true；否则，返回false。
     */
    protected boolean deleteFromPool(int index)
    {
        try
        {
            //错误检查
            if(this.cacheList==null)
            {
                this.setError("缓冲池对象为空，未对缓冲池进行初始化！");
                return false;
            }
            if(index<0 || index>=this.cacheList.size())
            {
                this.setError("指定索引值超出列表范围！");
                return false;
            }

            //删除数据
            Object result=this.cacheList.remove(index);
            return (result!=null);
        }
        catch(Exception ex)
        {
            this.setError(ex.toString());
            return false;
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    deleteFromPool<br>
     * 描述:    将指定键名的数据对象从缓冲池中删除（值名对）<br>
     *
     * @param key - 数据的键名索引或队列中的对象
     * @return boolean - 删除成功，返回true；否则，返回false。
     */
    protected boolean deleteFromPool(Object key)
    {
        try
        {
            //错误检查
            if(this.cacheMap==null/*||this.cacheBlockQueue==null||this.cacheConQueue==null*/)
            {
                this.setError("缓冲池对象为空，未对缓冲池进行初始化！");
                return false;
            }
            if(key==null)
            {
                this.setError("指定的数据键名为空！");
                return false;
            }

            //删除数据
            boolean success=false;
            if(this.cacheMap!=null)
            {
                Object result=this.cacheMap.remove(key);
                success=(result!=null);
            }
             /*if(this.cacheBlockQueue!=null)
             {
                 success=this.cacheBlockQueue.remove(key);
             }
             if(this.cacheConQueue!=null)
             {
                 success=this.cacheConQueue.remove(key);
             }*/
            return success;
        }
        catch(Exception ex)
        {
            this.setError(ex.toString());
            return false;
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    editInPool<br>
     * 描述:    更新缓冲池中指定位置的数据为指定数据内容（队列）<br>
     *
     * @param index - 位置索引
     * @param data  - 更新的数据对象
     * @return boolean - 更新成功，返回true；否则，返回false。
     */
    protected boolean editInPool(int index,Object data)
    {
        //错误检查
        if(this.cacheList==null)
        {
            this.setError("缓冲池对象为空，未对缓冲池进行初始化！");
            return false;
        }
        if(data==null)
        {
            this.setError("指定更新的新数据对象为空！");
            return false;
        }

        //指定位置数据为空
        if(index>this.cacheList.size()-1)
        {
            this.setError("指定位置索引处的数据不存在，位置索引错误！");
            return false;
        }

        this.cacheList.set(index,data);
        return true;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    editInPool<br>
     * 描述:    更新缓冲池中指定键名的数据为指定数据内容（值名对）<br>
     *
     * @param key     - 数据的键名索引
     * @param content - 更新的数据对象
     * @return boolean - 更新成功，返回true；否则，返回false。
     */
    protected boolean editInPool(Object key,Object content)
    {
        try
        {
            //错误检查
            if(this.cacheMap==null)
            {
                this.setError("缓冲池对象为空，未对缓冲池进行初始化！");
                return false;
            }
            if(content==null || key==null)
            {
                this.setError("指定的数据键名或数据内容为空！");
                return false;
            }

            //更新数据
            Object result=this.cacheMap.put(key,content);
            return (result!=null);
        }
        catch(Exception ex)
        {
            this.setError(ex.toString());
            return false;
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    editKeyInPool<br>
     * 描述:    更新缓冲池中指定键名为新的键名<br>
     *
     * @param oldKey - 被替换的键名
     * @param newKey - 替换的键名
     * @return boolean - 替换成功，返回true；否则，返回false。
     */
    protected boolean editKeyInPool(Object oldKey,Object newKey)
    {
        try
        {
            //错误检查
            if(this.cacheMap==null)
            {
                this.setError("缓冲池对象为空，未对缓冲池进行初始化！");
                return false;
            }
            if(oldKey==null || newKey==null)
            {
                this.setError("指定的数据键名为空！");
                return false;
            }

            //取出旧键名对应的数据内容，并添加到新键名的映射，去除旧键名映射
            Object content=this.getFromPool(oldKey);
            if(content==null) return false;
            if(!this.addToPool(newKey,content)) return false;
            return this.deleteFromPool(oldKey);
        }
        catch(Exception ex)
        {
            this.setError(ex.toString());
            return false;
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getFromPool<br>
     * 描述:    从缓冲池中获取指定索引的数据(列表)<br>
     *
     * @param index - 位置索引
     * @return Object - 数据对象
     */
    protected Object getFromPool(int index)
    {
        //错误检查
        if(this.cacheList==null)
        {
            this.setError("缓冲池对象为空，未对缓冲池进行初始化！");
            return false;
        }
        if(index>this.cacheList.size())
        {
            this.setError("指定索引位置大于缓冲池容量，索引位置错误！");
            return false;
        }

        return this.cacheList.get(index);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    keySet<br>
     * 描述:    获取Map的键名遍历集合对象<br>
     *
     * @return Set - 遍历集合对象
     */
    protected Set<Object> keySet()
    {
        //错误检查
        if(this.cacheMap==null)
        {
            this.setError("缓冲池对象为空，未对缓冲池进行初始化！");
            return null;
        }
        return this.cacheMap.keySet();
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getFromPool<br>
     * 描述:    从终端中获取指定键名的数据（值名对）<br>
     *
     * @param key - 键名索引
     * @return Object - 数据对象
     */
    protected Object getFromPool(Object key)
    {
        //错误检查
        if(this.cacheMap==null)
        {
            this.setError("缓冲池对象为空，未对缓冲池进行初始化！");
            return null;
        }
        if(key==null)
        {
            this.setError("指定的数据键名或数据内容为空！");
            return null;
        }
        return this.cacheMap.get(key);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    containList<br>
     * 描述:    判断缓冲池中是否有指定的数据，如果有则返回true；否则，返回false<br>
     *
     * @param data - 数据对象
     * @return boolean - 指定的数据对象是否存在
     */
    protected boolean containList(Object data)
    {
        //错误检查
        if(this.cacheList==null)
        {
            this.setError("缓冲池对象为空，未对缓冲池进行初始化！");
            return false;
        }
        if(data==null)
        {
            this.setError("指定的数据对象为空！");
            return false;
        }

        return this.cacheList.contains(data);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    containMap<br>
     * 描述:    首先判断缓冲池中是否有指定对象对应的的键名，如果有则返回true；<br>
     * 否则，则再判断缓冲池中是否有指定对象对应的的键值，如果有则返回人true；否则，返回false。
     *
     * @param keyOrValue - 键名（键值）
     * @return boolean - 指定的数据对象是否存在
     */
    protected boolean containMap(Object keyOrValue)
    {
        //错误检查
        if(this.cacheMap==null)
        {
            this.setError("缓冲池对象为空，未对缓冲池进行初始化！");
            return false;
        }
        if(keyOrValue==null)
        {
            this.setError("指定的数据键名或数据内容为空！");
            return false;
        }

        boolean success=this.cacheMap.containsKey(keyOrValue);
        if(!success)
        {
            success=this.cacheMap.containsValue(keyOrValue);
        }
        return success;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    indexOfList<br>
     * 描述:    判断缓冲池中是否有指定的数据，如果有则返回位置索引；否则，返回-1<br>
     *
     * @param data - 数据对象
     * @return int - 位置索引
     */
    protected int indexOfList(Object data)
    {
        //错误检查
        if(this.cacheList==null)
        {
            this.setError("缓冲池对象为空，未对缓冲池进行初始化！");
            return -1;
        }
        if(data==null)
        {
            this.setError("指定的数据对象为空！");
            return -1;
        }

        return this.cacheList.indexOf(data);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    indexOfMap<br>
     * 描述:    首先判断缓冲池中是否有指定对象对应的的键名，如果有则返回键值；<br>
     * 否则，则再判断缓冲池中是否有指定对象对应的的键值，如果有则返回人键名；否则，返回null。
     *
     * @param keyOrValue - 键名（键值）
     * @return Object - 键值（键名）
     */
    protected Object indexOfMap(Object keyOrValue)
    {
        //错误检查
        if(this.cacheMap==null)
        {
            this.setError("缓冲池对象为空，未对缓冲池进行初始化！");
            return false;
        }
        if(keyOrValue==null)
        {
            this.setError("指定的数据键名或数据内容为空！");
            return false;
        }

        Object result=this.cacheMap.get(keyOrValue);
        if(result==null)
        {
            if(this.cacheMap.containsValue(keyOrValue))
            {
                //遍历Map查找指定键值
                for(Object key : this.cacheMap.keySet())
                {
                    Object value=this.cacheMap.get(key);
                    if(keyOrValue.equals(value))
                    {
                        result=key;
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    clearPool<br>
     * 描述:    清空缓冲池<br>
     */
    protected void clearPool()
    {
        if(this.cacheList!=null && !this.cacheList.isEmpty())
        {
            this.cacheList.clear();
        }
        //this.cacheList=null;

        if(this.cacheMap!=null && !this.cacheMap.isEmpty())
        {
            this.cacheMap.clear();
        }
        //this.cacheMap=null;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    size<br>
     * 描述:    缓冲池当前大小。<br>
     * 如果列表和值名对对象都不为空，则缓冲池大小为二者之和
     *
     * @return int - 缓冲池大小
     */
    protected int size()
    {
        int size=0;
        if(this.cacheList!=null)
        {
            size+=this.cacheList.size();
        }
        if(this.cacheMap!=null)
        {
            size+=this.cacheMap.size();
        }
        return size;
    }

    /**
     * 更改器(受保护)<br>
     * 目标：   error
     *
     * @param error - 错误信息
     */
    protected void setError(String error)
    {
        this.error=error;
    }

    /**
     * 访问器(受保护)<br>
     * 目标：   error<br>
     *
     * @return String - 错误信息
     */
    public String getError()
    {
        return this.error;
    }

    /**
     * 访问器（公共）<br>
     * 目标：   poolName<br>
     *
     * @return boolean - 缓存池名称
     */
    public String getPoolName()
    {
        return poolName;
    }

    /**
     * 更改器（公共、不可继承）<br>
     * 目标：   poolName<br>
     *
     * @param poolName - 缓存池名称
     */
    public final void setPoolName(String poolName)
    {
        this.poolName=poolName;
    }
}
