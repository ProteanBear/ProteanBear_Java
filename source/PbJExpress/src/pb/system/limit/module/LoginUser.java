package pb.system.limit.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pb.system.limit.entity.SystemApplication;
import pb.system.limit.entity.SystemArea;
import pb.system.limit.entity.SystemPlugin;
import pb.system.limit.entity.SystemUser;
import pb.text.StringProductor;

/**
 * 登录用户数据记录类。<br>
 * 作为数据记录对象，保存在登录用户的session中。<br>
 * 1.05 - 增加getArea(String areaId) 方法。<br>
 * 1.04 - 增加generateAreaId方法。<br>
 * 1.03 - 增加getPluginByPluginId方法。<br>
 * 1.02 - 增加登录用户的报警控制配置属性。<br>
 * 1.01 - 添加用户管理范围属性。<br>
 *
 * @author proteanBear(马强)
 * @version 1.05 2012/03/22
 */
public class LoginUser extends SystemUser
{
    /**
     * 域(私有)<br>
     * 名称:    limits<br>
     * 描述:    记录登录用户的相关功能权限<br>
     */
    private String limits;

    /**
     * 域(私有)<br>
     * 名称:    plugins<br>
     * 描述:    记录登录用户所在的行政区域对应的功能插件<br>
     */
    private List<SystemPlugin> plugins;

    /**
     * 域(私有)<br>
     * 名称:    areaList<br>
     * 描述:    记录登录用户的管理的行政区域列表<br>
     */
    private List<SystemArea> areaList;

    /**
     * 域(私有)<br>
     * 名称:    appList<br>
     * 描述:    记录登录用户的管理的应用列表<br>
     */
    private List<SystemApplication> appList;

    /**
     * 域(私有)<br>
     * 名称:    roleType<br>
     * 描述:    记录用户的最大管理范围<br>
     */
    private int roleType;

    /**
     * 域(私有)<br>
     * 名称:    typeLevel<br>
     * 描述:    记录用户所在的企业类型的类型级别<br>
     */
    private int typeLevel;

    /**
     * 构造函数<br>
     *
     * @param user - SystemUser对象
     */
    public LoginUser(SystemUser user)
    {
        this.setAreaId(user.getAreaId());
        this.setAreaName(user.getAreaName());
        this.setCustId(user.getCustId());
        this.setDataDelete(user.getDataDelete());
        this.setDataRemark(user.getDataRemark());
        this.setUserBelong(user.getUserBelong());
        this.setUserEmail(user.getUserEmail());
        this.setUserIsgroup(user.getUserIsgroup());
        this.setUserName(user.getUserName());
        this.setUserNick(user.getUserNick());
        this.setUserQq(user.getUserQq());
        this.setUserTel(user.getUserTel());
    }

    /**
     * 方法（公共）<br>
     * 名称:    logoutFromSystem<br>
     * 描述:    用户注销。清空所有线程及缓存<br>
     */
    public void logoutFromSystem()
    {
        if(this.areaList!=null) this.areaList.clear();
        this.areaList=null;
        if(this.plugins!=null) this.plugins.clear();
        this.plugins=null;
        this.roleType=-1;
        this.typeLevel=-1;
    }

    /**
     * 方法（公共）<br>
     * 名称:    isHaveLimit<br>
     * 描述:    是否具有指定的权限<br>
     *
     * @param limitPluginId - 权限的功能插件标识
     * @param limitType     - 权限的功能类型
     * @return boolean - 如果登录用户权限中包含此权限编码，则返回true
     */
    public boolean isHaveLimit(String limitPluginId,int limitType)
    {
        if(this.isSuperAdmin()) return true;
        String pluginCode=this.getPluginCodeByPluginId(limitPluginId);
        if(pluginCode==null) return false;
        return (this.limits.contains(
                pluginCode
                        +""+StringProductor.toFullStringByInteger(limitType,2)
        ));
    }

    /**
     * 方法（公共）<br>
     * 名称:    pluginSize<br>
     * 描述:    返回功能插件的长度<br>
     *
     * @return int - 功能插件列表的长度
     */
    public int pluginSize()
    {
        if(this.plugins==null) return -1;
        return this.plugins.size();
    }

    /**
     * 方法（公共）<br>
     * 名称:    applicationSize<br>
     * 描述:    返回管理应用的长度<br>
     *
     * @return int - 功能插件列表的长度
     */
    public int applicationSize()
    {
        if(this.appList==null) return -1;
        return this.appList.size();
    }

    /**
     * 方法（公共）<br>
     * 名称:    getPlugin<br>
     * 描述:    获取指定列表索引值的功能插件<br>
     *
     * @param index - 索引值
     * @return SystemPlugin - 功能插件
     */
    public SystemPlugin getPlugin(int index)
    {
        if(this.plugins==null) return null;
        return this.plugins.get(index);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getApplication<br>
     * 描述:    获取指定列表索引值的应用信息<br>
     *
     * @param index - 索引值
     * @return SystemApplication - 应用信息
     */
    public SystemApplication getApplication(int index)
    {
        if(this.appList==null) return null;
        return this.appList.get(index);
    }

    /**
     * 方法（公共）<br>
     * 名称:    areaListSize<br>
     * 描述:    返回行政区域列表的长度<br>
     *
     * @return int - 行政区域列表的长度
     */
    public int areaListSize()
    {
        if(this.areaList==null) return -1;
        return this.areaList.size();
    }

    /**
     * 方法（公共）<br>
     * 名称:    getAreaList<br>
     * 描述:    获取指定列表索引值的行政区域<br>
     *
     * @param index - 索引值
     * @return SystemArea - 行政区域
     */
    public SystemArea getArea(int index)
    {
        if(this.areaList==null) return null;
        return this.areaList.get(index);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getAreaList<br>
     * 描述:    获取指定行政区域标识的行政区域<br>
     *
     * @param areaId - 索引值
     * @return SystemArea - 行政区域
     */
    public SystemArea getArea(String areaId)
    {
        if(this.areaList==null) return null;
        SystemArea area=null;
        for(int i=0;i<areaList.size();i++)
        {
            area=areaList.get(i);
            if(area==null) continue;
            if(area.getAreaId().equals(areaId)) break;
        }
        return area;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getRoleType<br>
     * 描述:    获取当前的用户角色管理范围。0-全局，1-本级和下级，2-本级<br>
     *
     * @return int - 当前的用户角色管理范围
     */
    public int getRoleType()
    {
        return this.roleType;
    }

    /**
     * 方法（公共）<br>
     * 名称:    generateAreaCondition<br>
     * 描述:    根据当前用户的管理范围生成子集查询条件<br>
     *
     * @param sqlName - 字段名称
     * @param areaId  - 行政区域标识
     * @return Map<String,Object> - 查询条件
     */
    public Map<String,Object> generateAreaCondition(String sqlName,String areaId)
    {
        Map<String,Object> result=new HashMap<>();

        //判断当前管理范围
        switch(this.roleType)
        {
            //全局
            case 0:
            {
                break;
            }
            //本级和下级
            case 1:
            {
                result.put(sqlName+" like ?",areaId+"%");
                break;
            }
            //本级
            case 2:
            {
                result.put(sqlName+"=?",areaId);
                break;
            }
            //其他
            default:
            {
                result.put(sqlName+"=?","");
                break;
            }
        }
        return result;
    }

    /**
     * 方法（私有）<br>
     * 名称:    generateAreaId<br>
     * 描述:    根据当前用户的管理范围对比生成相应的行政区域<br>
     *
     * @param areaId - 行政区域标识
     * @return String - 与用户的行政区域对比后生成的行政区域
     */
    public String generateAreaId(String areaId)
    {
        String result=areaId;

        //判断当前管理范围
        switch(this.roleType)
        {
            //全局
            case 0:
            {
                break;
            }
            //本级和下级
            case 1:
            {
                result=(areaId.startsWith(this.areaId))?areaId:this.areaId;
                break;
            }
            //本级
            case 2:
            {
                result=this.areaId;
                break;
            }
            //其他
            default:
            {
                result=null;
                break;
            }
        }

        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    typeLevel<br>
     * 描述:    记录当前企业类型的级别。0-平台级、1-企业级<br>
     *
     * @return int - 当前企业类型的级别
     */
    public int getTypeLevel()
    {
        return this.typeLevel;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getPluginCustIdByPluginId<br>
     * 描述:    通过插件标识获取插件对应的主键<br>
     *
     * @param pluginId - 插件标识
     * @return int - 主键标识
     */
    public SystemPlugin getPluginByPluginId(String pluginId)
    {
        SystemPlugin result=null;
        if(this.plugins!=null)
        {
            SystemPlugin plugin;
            for(int i=0;i<this.pluginSize();i++)
            {
                plugin=this.getPlugin(i);
                if(plugin!=null)
                {
                    if(pluginId.equals(plugin.getPluginId()))
                    {
                        result=plugin;
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 方法（私有）<br>
     * 名称:    getPluginCodeByPluginId<br>
     * 描述:    通过插件标识获取插件对应的主键<br>
     *
     * @param pluginId - 插件标识
     * @return String - 主键编号
     */
    private String getPluginCodeByPluginId(String pluginId)
    {
        String result=null;
        if(this.plugins!=null)
        {
            SystemPlugin plugin;
            for(int i=0;i<this.pluginSize();i++)
            {
                plugin=this.getPlugin(i);
                if(plugin!=null)
                {
                    if(pluginId.equals(plugin.getPluginId()))
                    {
                        result=plugin.getPluginCode();
                        break;
                    }
                }
            }
        }
        return result;
    }
     
    /*-----------------------------开始：访问器和修改器-------------------------*/

    /**
     * 访问器<br>
     * 目标：   limits<br>
     *
     * @return String - 权限字符串
     */
    public String getLimits()
    {
        return this.limits;
    }

    /**
     * 更改器<br>
     * 目标：   limits<br>
     *
     * @param limits - 将功能权限列表转换为字符串形式。
     */
    public void setLimits(List<SystemPlugin> limits)
    {
        //清空并初始化权限属性
        this.limits="";

        //遍历功能权限列表
        if(limits==null) return;
        for(int i=0;i<limits.size();i++)
        {
            if(i!=0) this.limits+="|";
            this.limits+=limits.get(i).getPluginCode();
        }
    }

    /**
     * 更改器<br>
     * 目标：   plugins<br>
     *
     * @param plugins - 功能插件列表。
     */
    public void setPlugins(List<SystemPlugin> plugins)
    {
        this.plugins=plugins;
    }

    /**
     * 更改器<br>
     * 目标：   areaList<br>
     *
     * @param areaList - 行政区域列表。
     */
    public void setAreaList(List<SystemArea> areaList)
    {
        this.areaList=areaList;
    }

    /**
     * 更改器<br>
     * 目标：   roleType<br>
     *
     * @param roleType - 用户管理范围
     */
    public void setRoleType(int roleType)
    {
        this.roleType=roleType;
    }

    /**
     * 更改器<br>
     * 目标：   typeLevel<br>
     *
     * @param typeLevel - 用户管理范围
     */
    public void setTypeLevel(int typeLevel)
    {
        this.typeLevel=typeLevel;
    }

    /**
     * 访问器<br>
     * 目标：   appList<br>
     *
     * @return List - 应用列表
     */
    public List<SystemApplication> getAppList()
    {
        return appList;
    }

    /**
     * 更改器<br>
     * 目标：   appList<br>
     *
     * @param appList - 管理的应用列表
     */
    public void setAppList(List<SystemApplication> appList)
    {
        this.appList=appList;
    }
     
    /*-----------------------------结束：访问器和修改器-------------------------*/
}