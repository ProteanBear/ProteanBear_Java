package pb.system.limit.manager;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.data.EntityTransformer;
import pb.data.SqlAttribute;
import pb.system.limit.entity.SystemPlugin;
import pb.text.StringProductor;

/**
 * 数据表映射类数据管理类—— 系统功能插件<br>
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/11/23
 */
public class SystemPluginFacade extends AbstractEntityManager<SystemPlugin>
        implements SystemPluginFacadeLocal
{
    /*-----------------------------开始：静态内容------------------------------*/

    /**
     * 静态常量(私有)<br>
     * 名称:    AREA_DEPTH<br>
     * 描述:    行政区域的深度（允许的区域层数）<br>
     */
    public static final int AREA_DEPTH=4;

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
    public SystemPluginFacade(Connector connector)
    {
        super(connector,SystemPlugin.class);
        this.orderBy="plugin_code asc";
    }

    /**
     * 方法（受保护）<br>
     * 名称: getPrimaryKeyName<br>
     * 描述: 获取数据表主键字段名称<br>
     *
     * @return String - 获取主键名称
     */
    @Override
    public String getPrimaryKeyName()
    {
        return "custId";
    }

    /**
     * 方法（受保护）<br>
     * 名称: getKeyGenerator<br>
     * 描述: 返回自增主键的生成器，非生成的主键返回""<br>
     *
     * @return String - 主键生成值
     */
    @Override
    protected String getKeyGenerator()
    {
        return null;//"SYSTEM_PLUGIN_SEQUENCE.nextval";
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getCodeGenerator<br>
     * 描述:    返回编码的生成器<br>
     *
     * @param upId - 指定的顶层区域标识
     * @return String - 主键生成值
     */
    protected String getCodeGenerator(final String upId)
    {
        String result="";

        //参数为空
        if(upId==null)
        {
            return (String)this.logAndReturnNull("指定的上级区域标识为null");
        }
        if(!"".equals(upId))
        {
            //判断上级区域的深度是否已经超过了允许值
            if(upId.trim().length()>=AREA_DEPTH*AREA_WIDTH)
            {
                return (String)this.logAndReturnNull("超过区域级数限制（"+AREA_DEPTH+"层）");
            }
            //判断指定的上级区域数据是否存在
            if(!this.exist(
                    new HashMap<String,Object>()
                    {{
                            put("pluginCode=?",upId);
                        }}
            ))
            {
                return (String)this.logAndReturnNull("指定的上级区域不存在");
            }
        }

        //读取指定区域的所有下级区域
        List<SystemPlugin> children=this.childrenNextDepth(upId);

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
            areaId=children.get(i).getPluginCode();
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
     * 方法（受保护）<br>
     * 名称:    getFindOrderBy<br>
     * 描述:    获取查询使用的排序方法<br>
     *
     * @return String - 排序方法
     */
    @Override
    protected String getFindOrderBy()
    {
        return "plugin_code asc";
    }

    /**
     * 方法（公共）<br>
     * 名称:    childrenNextDepth<br>
     * 描述:    获取指定区域的下一级所有数据<br>
     *
     * @param upId - 指定的顶层数据标识
     * @return List - 下一级数据列表
     */
    @Override
    public List<SystemPlugin> childrenNextDepth(String upId)
    {
        //获取系统使用级别宽度
        String like="";
        for(int i=0;i<AREA_WIDTH;i++) like+="_";

        //构造查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("plugin_code like ?",upId+like);

        //查询数据
        return this.find(condition);
    }

    /**
     * 方法（公共）
     * 名称：   create
     * 描述:    重载并标识为过期方法，添加对应的数据请使用特定类型添加方法<br>
     *
     * @param entity - 添加记录的描述对象
     * @return boolean - 添加成功，返回true;
     */
    @Override
    @Deprecated
    public boolean create(SystemPlugin entity)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 方法（公共、重载）
     * 名称：   create
     * 描述:    添加一条新的记录到数据库中<br>
     *
     * @param upId   - 指定的顶层区域标识
     * @param entity - 添加记录的描述对象
     * @return boolean - 添加成功，返回true;
     */
    @Override
    public boolean create(final String upId,SystemPlugin entity)
    {
        boolean success=false;

        //根据创建的类型不同设置不同的编码
        //模块和插件类型
        if(entity.isTypeModule() || entity.isTypePlugin())
        {
            String pluginCode=this.getCodeGenerator(upId);
            if(pluginCode==null) return success;
            entity.setPluginCode(pluginCode);

            //插件类型：增加所属模块名称
            if(entity.isTypePlugin())
            {
                List<SystemPlugin> parentModules=this.find(
                        new HashMap<String,Object>()
                        {{
                                put("pluginCode=?",upId);
                            }}
                );
                if(parentModules!=null && !parentModules.isEmpty())
                {
                    SystemPlugin parentModule=parentModules.get(0);
                    if(parentModule!=null) entity.setPluginParent(parentModule.getPluginName());
                }
            }
        }
        //权限类型
        else if(entity.isTypeLimit())
        {
            entity.setPluginCode(
                    upId+StringProductor
                            .toFullStringByNumberString(entity.getPluginParent(),2)
            );
        }
        //其他类型
        else
        {
            return this.logAndReturnFalse("类型设置错误");
        }

        //添加数据
        success=super.create(entity);

        //记录最新生成编号
        this.lastGenerator=entity.getPluginCode();

        return success;
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
        return "custId|pluginCode";
    }

    /**
     * 方法（受保护）<br>
     * 名称:    afterEdit<br>
     * 描述:    设置编辑后的相关处理<br>
     *
     * @param entity - 编辑后的对象
     */
    @Override
    protected void afterEdit(SystemPlugin entity)
    {
        //修改模块名称后更新插件内的名称记录
        if(entity.isTypeModule())
        {
            //设置更新语句
            SqlAttribute sqlAttri=new SqlAttribute()
                    .setTable("system_plugin")
                    .addColumnSet("plugin_parent","?")
                    .addCondition("plugin_code like ?");

            //执行
            this.dataManager.setAttribute(sqlAttri)
                    .update(entity.getPluginName(),entity.getPluginCode()+"____");
        }
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
                //获取指定主键的实体
                SystemPlugin entity=this.find(id);
                if(entity!=null)
                {
                    String keyName=this.getPrimaryKeyName();
                    this.dataManager.setAttribute(
                            EntityTransformer
                                    .generateSelectSqlFromEntity(entityClass,keyName)
                    );
                    success=this.dataManager.delete(id);

                    if(success)
                    {
                        this.afterRemove(entity);
                    }
                    else
                    {
                        this.setError(this.dataManager.getError());
                    }
                }
                else
                {
                    this.setError("指定的数据不存在！");
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
        SystemPlugin plugin=null;
        if(obj.getClass().isAssignableFrom(SystemPlugin.class))
        {
            plugin=(SystemPlugin)obj;

            //清除相关数据
            if(plugin!=null)
            {
                //清除全部下级相关数据
                this.removeAllSubPlugins(plugin);

                //清除企业类型多对多数据
                SystemAreaTypePluginFacadeLocal typeManager=new SystemAreaTypePluginFacade(this.connector);
                typeManager.removeByPluginId(plugin.getCustId());

                //清除用户角色多对多数据
                SystemRolePluginFacadeLocal roleManager=new SystemRolePluginFacade(this.connector);
                roleManager.removeByPluginId(plugin.getCustId());
            }
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    removeAllSubPlugins<br>
     * 描述:    获取指定的插件标识相关下级的插件权限数据<br>
     *
     * @param entity - 顶级数据对象
     * @return SystemPlugin - 系统功能插件列表
     */
    @Override
    public boolean removeAllSubPlugins(SystemPlugin entity)
    {
        boolean success=false;
        try
        {
            //构造删除语句
            SqlAttribute sqlAttr=new SqlAttribute()
                    .setTable("SYSTEM_PLUGIN")
                    .addCondition("plugin_code like ?");

            //删除数据
            success=this.dataManager.setAttribute(sqlAttr).delete(entity.getPluginCode()+"%");
        }
        catch(Exception ex)
        {
            success=this.logAndReturnFalse(ex.toString());
        }
        return success;
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
    public List<SystemPlugin> childrenAllDownDepth(String upId)
    {
        //构造查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("plugin_code like ?",upId+"%");

        //查询数据
        return this.find(condition);
    }

    /**
     * 方法（公共）
     * 名称：   findAll
     * 描述:    获取数据表中的所有数据<br>
     *
     * @return List - 数据列表
     */
    @Override
    public List<SystemPlugin> findAllPlugin()
    {
        return this.find(
                new HashMap<String,Object>()
                {{
                        put("pluginType=?",1);
                    }}
        );
    }

    /**
     * 方法（公共）<br>
     * 名称: findByAreaId<br>
     * 描述: 获取指定的行政区域对应的系统功能插件列表<br>
     *
     * @param areaId - 行政区域标识
     * @return List - 系统功能插件列表
     */
    @Override
    public List<SystemPlugin> findByAreaId(String areaId)
    {
        List<SystemPlugin> result=null;
        try
        {
            //设置查询语句
            SqlAttribute sqlAttri=new SqlAttribute()
                    .setColumnNames("distinct(satp.cust_id) as type_plugin_id,sp.*")
                    .setTable("system_area_type_map satm,system_area_type_plugin satp,system_plugin sp")
                    .addCondition("substr(?,0,length(satm.area_id))=satm.area_id")
                    .addCondition("satm.type_id=satp.type_id")
                    .addCondition("satp.plugin_cust_id=sp.cust_id")
                    .addCondition("sp.plugin_hidden=0");

            //读取数据库;
            result=this.generateObjectList(
                    this.dataManager
                            .setAttribute(sqlAttri)
                            .setPageSize(0)
                            .setOrderBy("sp.plugin_code asc")
                            .select(areaId)
            );
        }
        catch(IllegalAccessException|IllegalArgumentException|InstantiationException
                |NoSuchMethodException|InvocationTargetException|SQLException ex)
        {
            result=(List<SystemPlugin>)this.logAndReturnNull(ex.toString());
        }
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称: findByTypeId<br>
     * 描述: 获取指定的企业类型对应的系统功能插件列表<br>
     *
     * @param typeId - 企业类型标识
     * @return List - 系统功能插件列表
     */
    @Override
    public List<SystemPlugin> findByTypeId(String typeId)
    {
        List<SystemPlugin> result=null;
        try
        {
            //设置查询语句
            SqlAttribute sqlAttri=new SqlAttribute()
                    .setColumnNames("sp.*")
                    .setTable("system_area_type_plugin satp,system_plugin sp")
                    .addCondition("satp.type_id=?")
                    .addCondition("satp.plugin_cust_id=sp.cust_id");

            //读取数据库;
            result=this.generateObjectList(
                    this.dataManager
                            .setAttribute(sqlAttri)
                            .setPageSize(0)
                            .setOrderBy(this.getFindOrderBy())
                            .select(typeId)
            );
        }
        catch(IllegalAccessException|IllegalArgumentException|InstantiationException
                |NoSuchMethodException|InvocationTargetException|SQLException ex)
        {
            result=(List<SystemPlugin>)this.logAndReturnNull(ex.toString());
        }
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    findByRoleId<br>
     * 描述:    获取指定的管理角色对应的功能权限列表<br>
     *
     * @param roleId - 管理角色标识
     */
    @Override
    public List<SystemPlugin> findByRoleId(String roleId)
    {
        List<SystemPlugin> result=null;
        try
        {
            //设置查询语句
            SqlAttribute sqlAttri=new SqlAttribute()
                    .setColumnNames("sp.*")
                    .setTable("system_role_plugin srp,system_plugin sp")
                    .addCondition("srp.plugin_id=sp.cust_id ")
                    .addCondition("srp.role_id=?");

            //读取数据库;
            result=this.generateObjectList(
                    this.dataManager
                            .setAttribute(sqlAttri)
                            .setPageSize(0)
                            .setOrderBy(this.getFindOrderBy())
                            .select(roleId)
            );
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |InstantiationException
                |NoSuchMethodException
                |InvocationTargetException
                |SQLException ex)
        {
            result=(List<SystemPlugin>)this.logAndReturnNull(ex.toString());
        }
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getPrimaryKeyName<br>
     * 描述:    获取指定的管理人员对应的功能权限列表<br>
     *
     * @param userId - 管理人员标识
     * @return List - 功能权限列表
     */
    @Override
    public List<SystemPlugin> findByUserId(int userId)
    {
        List<SystemPlugin> result=null;
        try
        {
            //设置查询语句
            SqlAttribute sqlAttri=new SqlAttribute()
                    .setColumnNames("distinct(srp.cust_id),sp.*")
                    .setTable("system_user_role sur,system_role_plugin srp,system_plugin sp")
                    .addCondition("sur.role_id=srp.role_id")
                    .addCondition("srp.plugin_id=sp.cust_id")
                    .addCondition("sur.user_id=?");

            //读取数据库;
            result=this.generateObjectList(
                    this.dataManager
                            .setAttribute(sqlAttri)
                            .setPageSize(0)
                            .setOrderBy("srp.cust_id asc")
                            .select(userId)
            );
        }
        catch(IllegalAccessException
                |IllegalArgumentException
                |InstantiationException
                |NoSuchMethodException
                |InvocationTargetException
                |SQLException ex)
        {
            result=(List<SystemPlugin>)this.logAndReturnNull(ex.toString());
        }
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    existPlugin<br>
     * 描述:    判断指定的主键标识对应的数据为已存在的插件类型<br>
     *
     * @param id - 插件标识
     * @return boolean - 是否存在
     */
    @Override
    public boolean existPlugin(final Object id)
    {
        Map<String,Object> condition=new HashMap()
        {{
                put("pluginType=?",1);
                put("custId=?",id);
            }};

        return this.exist(condition);
    }

    /**
     * 方法（公共）<br>
     * 名称:    existLimit<br>
     * 描述:    判断指定的主键标识对应的数据为已存在的权限类型<br>
     *
     * @param id - 插件标识
     * @return boolean - 是否存在
     */
    @Override
    public boolean existLimit(final Object id)
    {
        Map<String,Object> condition=new HashMap()
        {{
                put("pluginType=?",2);
                put("custId=?",id);
            }};

        return this.exist(condition);
    }
}