package pb.system.limit.servlet;

import pb.system.limit.action.BusiMemberAction;

/**
 * 数据访问HTTP接口——记录系统平台的会员信息
 *@author ProteanBear
 *@version 1.00 2016-04-11
 */
public class BusiMemberServlet extends AbstractServlet 
{
    /*-----------------------------开始：静态内容------------------------------*/

    /**
     * 静态常量(公共)<br>
     * 名称:    SESSION_MEMBER_USER<br>
     * 描述:    记录session属性名称——会员登录后记录的缓存数据索引，记录在Session中<br>
     */
    public static final String SESSION_MEMBER_USER="MemberInSession";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_MEMBER<br>
     * 描述:    记录请求参数名称——会员的登录标识<br>
     */
    public static final String PARAM_MEMBER="member";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_MEMBER_PASS<br>
     * 描述:    记录请求参数名称——会员的登录密码<br>
     */
    public static final String PARAM_MEMBER_PASS="memberPass";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_MEMBER_TYPE<br>
     * 描述:    记录请求参数名称——会员的登录类型<br>
     */
    public static final String PARAM_MEMBER_TYPE="memberType";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_NEW_PASS<br>
     * 描述:    记录请求参数名称——会员的新密码<br>
     */
    public static final String PARAM_NEW_PASS="newPass";

    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_PASS_AGAIN<br>
     * 描述:    记录请求参数名称——会员的新密码,再次输入<br>
     */
    public static final String PARAM_PASS_AGAIN="passAgain";

    /*-----------------------------结束：静态内容------------------------------*/

    /**方法（受保护）
     * 名称:initDataAction
     * 描述:初始化数据应用层接口
     */
    @Override protected void initDataAction()
    {
        this.dataAction=new BusiMemberAction(this.connector);
    }

    /**方法（受保护）
     * 名称:getLimitPluginId
     * 描述:获取当前功能对应的功能插件标识
     * @return int - 创建模式的权限编码
     */
    @Override protected String getLimitPluginId()
    {
        return "BUSI_MEMBER";
    }
}