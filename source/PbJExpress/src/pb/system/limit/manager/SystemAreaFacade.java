package pb.system.limit.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.data.EntityTransformer;
import pb.system.limit.entity.SystemArea;

/**
 * 数据表映射类数据管理类——行政区域数据表，用来对系统中的企业相关的区域进行划分记录，
 * 通过区域标识实现树形结构，在树中即可使用地区也可适用类型，可自由的划分相关区域，如云平台->地区->企
 * 系统中使用4位数字表示一级部门，使用“1000”为顶级部门；
 * 而顶级部门的下级第一个部门标识就为“10000000”，第二个部门标识就为“10000001”，
 * “10000001”的下一级的第一个部门就为“100000010000”，以此类推。<br>
 * 1.04 - 增加获取指定行政区域标识对应的级别深度的方法。<br>
 * 1.03 - 增加普通列表转换为树形结构的方法。<br>
 * 1.02 - 增加删除前的是否有管理车辆的判断。<br>
 * 1.01 - 修正主键生成器调用两次，及null主键依然添加数据的问题。<br>
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class SystemAreaFacade
        extends AbstractEntityManager<SystemArea>
        implements SystemAreaFacadeLocal
{
    /*-----------------------------开始：静态内容------------------------------*/

    /**
     * 静态常量(私有)<br>
     * 名称:    AREA_DEPTH<br>
     * 描述:    行政区域的深度（允许的区域层数）<br>
     */
    public static final int AREA_DEPTH=20;

    /**
     * 静态常量(私有)<br>
     * 名称:    AREA_WIDTH<br>
     * 描述:    行政区域的宽度（每一层使用的字符数）<br>
     */
    public static final int AREA_WIDTH=4;
     
    /*-----------------------------结束：静态内容------------------------------*/

    /**
     * 构造函数<br>
     *
     * @param connector - 指定数据连接器
     */
    public SystemAreaFacade(Connector connector)
    {
        super(connector,SystemArea.class);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getPrimaryKeyName<br>
     * 描述:    获取数据表主键字段名称<br>
     *
     * @return String - 获取主键名称
     */
    @Override
    public String getPrimaryKeyName()
    {
        return "areaId";
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getKeyGenerator<br>
     * 描述:    重载并标识为过期方法，因为添加行政区域必须指定上级区域<br>
     *
     * @return String - 主键生成值
     */
    @Override
    @Deprecated
    protected String getKeyGenerator()
    {
        return null;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getKeyGenerator<br>
     * 描述:    返回自增主键的生成器<br>
     *
     * @param upId - 指定的顶层区域标识
     * @return String - 主键生成值
     */
    protected String getKeyGenerator(String upId)
    {
        String result="";

        //参数为空
        if(upId==null)
        {
            return (String)this.logAndReturnNull("指定的上级区域标识为null");
        }
        //判断上级区域的深度是否已经超过了允许值
        if(upId.trim().length()>=AREA_DEPTH*AREA_WIDTH)
        {
            return (String)this.logAndReturnNull("超过区域级数限制（"+AREA_DEPTH+"层）");
        }
        //判断指定的上级区域数据是否存在
        if(!this.exist(upId))
        {
            return (String)this.logAndReturnNull("指定的上级区域不存在");
        }

        //读取指定区域的所有下级区域
        List<SystemArea> children=this.childrenNextDepth(upId);

        //判断查询结果
        if(children==null)
        {
            return (String)this.logAndReturnNull("下级区域数据读取错误");
        }

        //遍历查询结果，搜索空位
        int num=0;
        String areaId;
        for(int i=0;i<children.size();i++,num++)
        {
            //获取当前区域标识
            areaId=children.get(i).getAreaId();
            if(areaId==null) continue;
            areaId=areaId.trim();

            //生成新标识
            result=upId+String.format("%1$04d",num);

            //判断是否是顺序的，如果不是顺序的，表示有空位，生成新标识
            if(!result.equals(areaId)) break;
        }
        result=upId+String.format("%1$04d",num);

        //判断是否超过宽度限制
        String maxStr="";
        for(int i=0;i<AREA_WIDTH;i++) maxStr+="9";
        int max=Integer.parseInt(maxStr);
        if(num>=max) result=(String)this.logAndReturnNull("级别区域数已满");

        return result;
    }

    /**
     * 方法（公共）
     * 名称：   create
     * 描述:    重载并标识为过期方法，因为添加行政区域必须指定上级区域，请使用create(String upId,SystemArea area)方法<br>
     *
     * @param entity - 添加记录的描述对象
     * @return boolean - 添加成功，返回true;
     */
    @Override
    @Deprecated
    public boolean create(SystemArea entity)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 方法（公共、重载）
     * 名称：   create
     * 描述:    添加一条新的记录到数据库中<br>
     *
     * @param upId - 指定的顶层区域标识
     * @param area - 添加记录的描述对象
     * @return boolean - 添加成功，返回true;
     */
    @Override
    public boolean create(String upId,SystemArea area)
    {
        boolean success=false;
        try
        {
            String nextKey=this.getKeyGenerator(upId);
            if(nextKey!=null)
            {
                this.dataManager.setAttribute(
                        EntityTransformer
                                .generateInsertSqlFromEntity(
                                        entityClass,area,
                                        this.getPrimaryKeyName(),nextKey
                                )
                );
                success=this.dataManager.insert(
                        EntityTransformer
                                .generateInsertParamsFromEntity(
                                        entityClass,area,
                                        this.getPrimaryKeyName(),nextKey
                                )
                );

                if(success) this.lastGenerator=nextKey;
            }
        }
        catch(NoSuchMethodException
                |IllegalAccessException
                |IllegalArgumentException
                |InvocationTargetException ex)
        {
            success=false;
            this.setError(ex.toString());
        }
        return success;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    beforeRemove<br>
     * 描述:    设置删除前的相关处理<br>
     *
     * @return boolean - 可以删除，返回true
     */
    @Override
    protected boolean beforeRemove(Object obj)
    {
        //指定主键或对象为空
        if(obj==null)
        {
            return this.logAndReturnFalse("指定主键或对象为空");
        }

        //获取行政区域的主键
        String areaId=null;
        if(obj.getClass().isAssignableFrom(SystemArea.class))
        {
            areaId=((SystemArea)obj).getAreaId();
        }
        else
        {
            areaId=obj+"";
        }

        //行政区域为最顶层区域，不可删除
        if(areaId.length()<=AREA_WIDTH)
        {
            return this.logAndReturnFalse("不可删除最顶层行政区域");
        }

        //行政区域为不存在，不可删除
        if(!this.exist(areaId))
        {
            return this.logAndReturnFalse("指定删除的区域不存在");
        }

        //判断是否有下级行政区域，如果有下级行政区域，则无法删除当前行政区域
        if(this.existChildren(areaId))
        {
            return this.logAndReturnFalse("指定区域包含下级区域，不可删除");
        }

        //判断行政区域包含管理人员，不可删除
        if(this.existSystemUser(areaId))
        {
            return this.logAndReturnFalse("指定区域包含管理人员，不可删除");
        }

        return true;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    afterRemove<br>
     * 描述:    设置删除后的相关处理<br>
     *
     * @param obj - 主键或删除对象
     */
    @Override
    protected void afterRemove(Object obj)
    {
        //指定主键或对象为空
        if(obj==null) this.logAndReturnFalse("指定主键或对象为空");

        //获取行政区域的主键
        String areaId=null;
        if(obj.getClass().isAssignableFrom(SystemArea.class))
        {
            areaId=((SystemArea)obj).getAreaId();
        }
        else
        {
            areaId=obj+"";
        }

        //创建多对多企业类型插件管理器对象
        SystemAreaTypeMapFacadeLocal manager=
                new SystemAreaTypeMapFacade(this.connector);

        //清除相关企业类型多对多数据
        manager.removeByAreaId(areaId);
    }

    /**
     * 方法（公共）<br>
     * 名称:    childrenNextDepth<br>
     * 描述:    获取指定区域的下一级所有区域<br>
     *
     * @param upId - 指定的顶层区域标识
     * @return List - 下一级区域列表
     */
    @Override
    public List<SystemArea> childrenNextDepth(String upId)
    {
        //获取系统使用级别宽度
        String like="";
        for(int i=0;i<AREA_WIDTH;i++) like+="_";

        //构造查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("area_id like ?",upId+like);

        //查询数据
        return this.find(condition);
    }

    /**
     * 方法（公共）<br>
     * 名称:    existChildren<br>
     * 描述:    查看指定的行政区域是否有下级行政区域<br>
     *
     * @param upId - 指定的顶层区域标识
     * @return boolean - 如果有下级行政区域，返回true
     */
    @Override
    public boolean existChildren(String upId)
    {
        //获取系统使用级别宽度
        String like="";
        for(int i=0;i<AREA_WIDTH;i++) like+="_";

        //构造查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("areaId like ?",upId+like);

        //查询数据
        return this.exist(condition);
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
        return true;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getUpAreaId<br>
     * 描述:    获取指定行政区域标识的上级行政区域标识<br>
     *
     * @param areaId - 指定的顶层区域标识
     * @return String - 返回上级行政区域标识
     */
    @Override
    public String getUpAreaId(String areaId)
    {
        if(areaId==null) return (String)this.logAndReturnNull("指定的行政区域标识为空");
        if(areaId.length()==AREA_WIDTH) return (String)this.logAndReturnNull("指定的行政区域标识已为顶层区域");
        if(areaId.length()%AREA_WIDTH!=0) return (String)this.logAndReturnNull("错误的行政区域标识长度");
        return areaId.substring(0,areaId.length()-AREA_WIDTH);
    }

    /**
     * 方法（公共）<br>
     * 名称:    childrenAllDownDepth<br>
     * 描述:    获取指定区域的所有低层次区域<br>
     *
     * @param upId - 指定的顶层区域标识
     * @return List - 低层次所有区域列表
     */
    @Override
    public List<SystemArea> childrenAllDownDepth(String upId)
    {
        //构造查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("area_id like ?",upId+"%");

        //查询数据
        return this.find(condition);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDepth<br>
     * 描述:    获取指定行政区域标识对应的级别深度<br>
     *
     * @param areaId - 行政区域标识
     * @return int - 深度级别
     */
    @Override
    public int getDepth(String areaId)
    {
        if(areaId==null) return 0;
        return areaId.trim().length()/AREA_WIDTH;
    }

    /**
     * 方法（私有）<br>
     * 名称:    existSystemUser<br>
     * 描述:    查看指定的行政区域下是否有管理的人员<br>
     *
     * @param upId - 指定的行政区域标识
     * @return boolean - 如果有管理的人员，返回true
     */
    private boolean existSystemUser(String upId)
    {
        //创建车辆基本信息管理器
        SystemUserFacadeLocal userManager=new SystemUserFacade(this.connector);

        //构造查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("areaId=?",upId);

        //查询数据
        return userManager.exist(condition);
    }
}