package pb.system.cms.servlet;

import pb.system.cms.action.BusiFeedbackAction;
import pb.system.limit.servlet.AbstractServlet;

/**
 * 数据访问HTTP接口——记录应用的意见反馈信息
 *
 * @author ProteanBear
 * @version 1.00 2014-11-20
 */
public class BusiFeedbackServlet extends AbstractServlet
{
    /*-----------------------------开始：静态内容------------------------------*/
    /**
     * 静态常量(公共)<br/>
     * 名称:    PARAM_UPID<br/>
     * 描述:    记录请求参数名称——所属应用<br/>
     */
    public static final String PARAM_APP="feedbackApp";
    
    /*-----------------------------结束：静态内容------------------------------*/

    /**
     * 方法（受保护）
     * 名称:initDataAction
     * 描述:初始化数据应用层接口
     */
    @Override
    protected void initDataAction()
    {
        this.dataAction=new BusiFeedbackAction(this.connector);
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
        return "BUSI_FEEDBACK";
    }
}