package pb.system.limit.servlet;

import pb.system.limit.action.SystemAreaTreeAction;

/**
 * @author maqiang
 */
public class SystemAreaTreeServlet extends AbstractServlet
{
    /*-----------------------------开始：静态内容------------------------------*/

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_UPID<br>
     * 描述:    记录请求参数名称——上层行政区域<br>
     */
    public static final String PARAM_UPID="upId";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_TREEMODE<br>
     * 描述:    记录请求参数名称——树形模式<br>
     */
    public static final String PARAM_TREEMODE="treeMode";
    
    /*-----------------------------结束：静态内容------------------------------*/

    /**
     * 方法（受保护）<br>
     * 名称:    initDataAction<br>
     * 描述:    初始化数据应用层接口<br>
     */
    @Override
    protected void initDataAction()
    {
        this.dataAction=new SystemAreaTreeAction(this.connector);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getLimitPluginId<br>
     * 描述:    获取当前功能对应的功能插件标识<br>
     *
     * @return int - 创建模式的权限编码
     */
    @Override
    protected String getLimitPluginId()
    {
        return "SYSTEM_MANAGE_AREA";
    }
}