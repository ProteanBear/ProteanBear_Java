package pb.system.limit.servlet;

import pb.system.limit.action.SystemVersionAction;

/**
 * 数据访问HTTP接口——记录系统应用的相关平台的版本信息。
 *
 * @author ProteanBear
 * @version 1.00 2014-11-20
 */
public class SystemVersionServlet extends AbstractServlet
{
    /*-----------------------------开始：静态内容------------------------------*/
    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_APP<br>
     * 描述:    记录请求参数名称——所属应用<br>
     */
    public static final String PARAM_APP="versionApp";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_PLAT<br>
     * 描述:    记录请求参数名称——所属平台<br>
     */
    public static final String PARAM_PLAT="plat";

    /**
     * 静态常量(公共)<br>
     * 名称:    VALUE_PLAT_IOS<br>
     * 描述:    记录请求参数值——所属平台iOS<br>
     */
    public static final String VALUE_PLAT_IOS="iOS";

    /**
     * 静态常量(公共)<br>
     * 名称:    VALUE_PLAT_ANDROID<br>
     * 描述:    记录请求参数值——所属平台Android<br>
     */
    public static final String VALUE_PLAT_ANDROID="Android";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_SOURCE<br>
     * 描述:    记录请求参数名称——源码类型<br>
     */
    public static final String PARAM_SOURCE="source";
    /*-----------------------------结束：静态内容------------------------------*/

    /**
     * 方法（受保护）
     * 名称:initDataAction
     * 描述:初始化数据应用层接口
     */
    @Override
    protected void initDataAction()
    {
        this.dataAction=new SystemVersionAction(this.connector);
    }

    /**
     * 方法（受保护）
     * 名称:getLimitPluginId
     * 描述:获取当前功能对应的功能插件标识
     *
     * @return int - 创建模式的权限编码
     */
    @Override
    protected String getLimitPluginId()
    {
        return "SYSTEM_VERSION";
    }
}