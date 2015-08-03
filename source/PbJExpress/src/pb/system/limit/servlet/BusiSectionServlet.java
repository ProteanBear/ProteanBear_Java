package pb.system.limit.servlet;

import pb.system.limit.action.BusiSectionAction;

/**
 * 数据访问HTTP接口——记录当前系统业务栏目划分的数据信息。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-31
 */
public class BusiSectionServlet extends AbstractServlet
{
    /*-----------------------------开始：静态内容------------------------------*/

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_UPID<br>
     * 描述:    记录请求参数名称——上层栏目<br>
     */
    public static final String PARAM_UPID="upCode";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_UPID<br>
     * 描述:    记录请求参数名称——所属应用<br>
     */
    public static final String PARAM_APP="sectionApp";
    
    /*-----------------------------结束：静态内容------------------------------*/

    /**
     * 方法（受保护）
     * 名称:initDataAction
     * 描述:初始化数据应用层接口
     */
    @Override
    protected void initDataAction()
    {
        this.dataAction=new BusiSectionAction(this.connector);
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
        return "BUSI_MANAGE_SECTION";
    }
}