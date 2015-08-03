package pb.system.limit.entity;

/**
 * 数据表映射类——用户操作日志单日缓存表。<br>
 * 缓存每天当日的用户操作日志信息，第二日凌晨清空。<br>
 *
 * @author jia_stop
 * @version 1.00 2012/03/14
 */
public class SystemLogUserCache
{
    /**
     * 域(受保护)<br>
     * 名称: custId<br>
     * 描述: 自增标识。使用序列：LOG_USER_SEQUENCE。<br>
     */
    protected int custId;

    /**
     * 域(受保护)<br>
     * 名称: userId<br>
     * 描述: 记录产生操作日志的管理人员的唯一标识。<br>
     */
    protected String userId;

    /**
     * 域(受保护)<br>
     * 名称: userName<br>
     * 描述: 使用时间戳作为主键，时间格式为yyyyMMddHHmmss<br>
     */
    protected String userName;

    /**
     * 域(受保护)<br>
     * 名称: areaId<br>
     * 描述: 记录产生操作日志的管理人员所属的行政区域的标识。<br>
     */
    protected String areaId;

    /**
     * 域(受保护)<br>
     * 名称: areaName<br>
     * 描述: 记录产生操作日志的管理人员所属的行政区域的显示名称。<br>
     */
    protected String areaName;

    /**
     * 域(受保护)<br>
     * 名称: pluginCustId<br>
     * 描述: 记录产生操作日志的功能插件主键信息<br>
     */
    protected int pluginCustId;

    /**
     * 域(受保护)<br>
     * 名称: pluginId<br>
     * 描述: 记录产生操作日志的功能插件的英文标识。<br>
     */
    protected String pluginId;

    /**
     * 域(受保护)<br>
     * 名称: pluginName<br>
     * 描述: 记录产生操作日志的功能插件的中文名称，功能的显示名称。<br>
     */
    protected String pluginName;

    /**
     * 域(受保护)<br>
     * 名称: pluginModule<br>
     * 描述: 记录产生操作日志的功能插件所属的功能模块。<br>
     * 0-系统配置、1-系统日志、2-系统管理、3-终端配置；<br>
     * 10-通用业务、11-危险品业务、12-旅游品业务、13-出租车业务、<br>
     * 19-终端业务；20-地图信息查询、21-地图业务设置、22-GPS相关地图业务；<br>
     * 30-车辆警报监控、31-车辆动态监控、32-车辆视频监控、33-车辆查询；40-平台监控车辆相关、<br>
     * 42-平台监控客户端相关、43-平台监控服务器相关；50-业务统计车辆相关、51-业务统计日志相关、<br>
     * 52-业务统计企业相关；90-未正式发布功能<br>
     */
    protected int pluginModule;

    /**
     * 域(受保护)<br>
     * 名称: operateType<br>
     * 描述: 记录操作的功能类型-添加、2-删除、3-编辑、0-查询<br>
     */
    protected int operateType;

    /**
     * 域(受保护)<br>
     * 名称: operateTime<br>
     * 描述: 记录操作的执行时间，时间格式：yyyy-MM-dd HH:mm:ss<br>
     */
    protected String operateTime;

    /**
     * 域(受保护)<br>
     * 名称: logDescription<br>
     * 描述: 记录日志对当前操作记录的描述<br>
     */
    protected String logDescription;

    /**
     * 访问器<br>
     * 目标： areaId<br>
     *
     * @return String - 部门标识
     */
    public String getAreaId()
    {
        return areaId;
    }

    /**
     * 更改器<br>
     * 目标： areaId<br>
     *
     * @param areaId - 部门标识
     */
    public void setAreaId(String areaId)
    {
        this.areaId=areaId;
    }

    /**
     * 访问器<br>
     * 目标： areaName<br>
     *
     * @return String - 部门名称
     */
    public String getAreaName()
    {
        return areaName;
    }

    /**
     * 更改器<br>
     * 目标： areaName<br>
     *
     * @param areaName - 部门名称
     */
    public void setAreaName(String areaName)
    {
        this.areaName=areaName;
    }

    /**
     * 访问器<br>
     * 目标： custId<br>
     *
     * @return String - 自增标识
     */
    public Integer getCustId()
    {
        return custId;
    }

    /**
     * 更改器<br>
     * 目标： custId<br>
     *
     * @param custId - 自增标识
     */
    public void setCustId(int custId)
    {
        this.custId=custId;
    }

    /**
     * 访问器<br>
     * 目标： logDescription<br>
     *
     * @return String - 操作描述
     */
    public String getLogDescription()
    {
        return logDescription;
    }

    /**
     * 更改器<br>
     * 目标： logDescription<br>
     *
     * @param logDescription - 操作描述
     */
    public void setLogDescription(String logDescription)
    {
        this.logDescription=logDescription;
    }

    /**
     * 访问器<br>
     * 目标： operateTime<br>
     *
     * @return String - 记录时间
     */
    public String getOperateTime()
    {
        return operateTime;
    }

    /**
     * 更改器<br>
     * 目标： operateTime<br>
     *
     * @param operateTime - 记录时间
     */
    public void setOperateTime(String operateTime)
    {
        this.operateTime=operateTime;
    }

    /**
     * 访问器<br>
     * 目标： operateType<br>
     *
     * @return String - 功能类型
     */
    public int getOperateType()
    {
        return operateType;
    }

    /**
     * 更改器<br>
     * 目标： operateType<br>
     *
     * @param operateType - 功能类型
     */
    public void setOperateType(int operateType)
    {
        this.operateType=operateType;
    }

    /**
     * 更改器<br>
     * 目标： operateType<br>
     *
     * @param operateType - 功能类型
     */
    public void setOperateType(String operateType)
    {
        this.operateType=(operateType==null || "".equals(operateType.trim()))?
                null:Integer.parseInt(operateType);
    }

    /**
     * 访问器<br>
     * 目标： pluginCustId<br>
     *
     * @return String - 插件主键
     */
    public int getPluginCustId()
    {
        return pluginCustId;
    }

    /**
     * 更改器<br>
     * 目标： pluginCustId<br>
     *
     * @param pluginCustId - 插件主键
     */
    public void setPluginCustId(int pluginCustId)
    {
        this.pluginCustId=pluginCustId;
    }

    /**
     * 更改器<br>
     * 目标： pluginCustId<br>
     *
     * @param pluginCustId - 插件主键
     */
    public void setPluginCustId(String pluginCustId)
    {
        this.pluginCustId=(pluginCustId==null || "".equals(pluginCustId.trim()))?
                null:Integer.parseInt(pluginCustId);
    }

    /**
     * 访问器<br>
     * 目标： pluginId<br>
     *
     * @return String - 插件标识
     */
    public String getPluginId()
    {
        return pluginId;
    }

    /**
     * 更改器<br>
     * 目标： pluginId<br>
     *
     * @param pluginId - 插件标识
     */
    public void setPluginId(String pluginId)
    {
        this.pluginId=pluginId;
    }

    /**
     * 访问器<br>
     * 目标： pluginModule<br>
     *
     * @return Integer - 功能模块
     */
    public Integer getPluginModule()
    {
        return pluginModule;
    }

    /**
     * 更改器<br>
     * 目标： pluginModule<br>
     *
     * @param pluginModule - 功能模块
     */
    public void setPluginModule(String pluginModule)
    {
        this.pluginModule=(pluginModule==null || "".equals(pluginModule.trim()))?
                null:Integer.parseInt(pluginModule);
    }

    /**
     * 更改器<br>
     * 目标： pluginModule<br>
     *
     * @param pluginModule - 功能模块
     */
    public void setPluginModule(int pluginModule)
    {
        this.pluginModule=pluginModule;
    }

    /**
     * 访问器<br>
     * 目标： pluginName<br>
     *
     * @return String - 显示名称
     */
    public String getPluginName()
    {
        return pluginName;
    }

    /**
     * 更改器<br>
     * 目标： pluginName<br>
     *
     * @param pluginName - 显示名称
     */
    public void setPluginName(String pluginName)
    {
        this.pluginName=pluginName;
    }

    /**
     * 访问器<br>
     * 目标： userId<br>
     *
     * @return String - 用户标识
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * 更改器<br>
     * 目标： userId<br>
     *
     * @param userId - 用户标识
     */
    public void setUserId(String userId)
    {
        this.userId=userId;
    }

    /**
     * 访问器<br>
     * 目标： userName<br>
     *
     * @return String - 显示名称
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * 更改器<br>
     * 目标： userName<br>
     *
     * @param userName - 显示名称
     */
    public void setUserName(String userName)
    {
        this.userName=userName;
    }
}
