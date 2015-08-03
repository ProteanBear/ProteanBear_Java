package pb.system.cms.servlet;

import pb.system.cms.action.BusiCmsArticleAction;
import pb.system.limit.servlet.AbstractServlet;

/**
 * 数据访问HTTP接口——记录文章内容相关信息
 *
 * @author ProteanBear
 * @version 1.00 2014-08-21
 */
public class BusiCmsArticleServlet extends AbstractServlet
{
    /*-----------------------------开始：静态内容------------------------------*/

    /**
     * 静态常量(公共)<br/>
     * 名称:    PARAM_RESOURCE<br/>
     * 描述:    记录请求参数名称——文章对应的资源标识<br/>
     */
    public static final String PARAM_RESOURCE="articleResources[]";

    /**
     * 静态常量(公共)<br/>
     * 名称:    PARAM_GROUPS<br/>
     * 描述:    记录请求参数名称——文章对应的资源标识<br/>
     */
    public static final String PARAM_GROUPS="articleGroups[]";

    /**
     * 静态常量(公共)<br/>
     * 名称:    PARAM_SECTION<br/>
     * 描述:    记录请求参数名称——栏目标识<br/>
     */
    public static final String PARAM_SECTION="sectionCode";

    /**
     * 静态常量(公共)<br/>
     * 名称:    PARAM_APP<br/>
     * 描述:    记录请求参数名称——所属应用<br/>
     */
    public static final String PARAM_APP="appCode";

    /**
     * 静态常量(公共)<br/>
     * 名称:    PARAM_AUTO_IMAGE<br/>
     * 描述:    记录请求参数名称——所属应用<br/>
     */
    public static final String PARAM_AUTO_IMAGE="autoImg";
    
    /*-----------------------------结束：静态内容------------------------------*/

    /**
     * 方法（受保护）
     * 名称:initDataAction
     * 描述:初始化数据应用层接口
     */
    @Override
    protected void initDataAction()
    {
        this.dataAction=new BusiCmsArticleAction(this.connector);
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
        return "BUSI_CMS_ARTICLE";
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    stringForReturn<br/>
     * 描述:    用于自定义增删改操作是返回的内容<br/>
     *
     * @param mode - 处理模式
     * @return boolean - 如果参数值为null或""，返回true
     */
    @Override
    protected String stringForReturn(String mode)
    {
        return (AbstractServlet.VALUE_MODE_CREATE.equals(mode)?this.dataAction.lastKeyGenerator():null);
    }
}