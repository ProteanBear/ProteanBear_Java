package pb.system.limit.servlet;

import pb.system.limit.action.BusiMemberAction;
import pb.system.limit.entity.BusiMember;
import pb.system.limit.module.LoginUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

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

    /**
     * 域(受保护)<br>
     * 名称:    dataAction<br>
     * 描述:    使用DataAction接口对数据进行相关处理<br>
     */
    protected BusiMemberAction memberAction;

    /**
     * 方法（受保护）<br>
     * 名称:    processRequest<br>
     * 描述:    处理来自HTTP访问的相关请求，调用DataAction接口进行数据的增删改查处理<br>
     *
     * @param request  - HTTP请求对象
     * @param response - HTTP输出对象
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException
    {
        PrintWriter out=response.getWriter();
        LoginUser user=null;
        boolean canCreate=false;
        boolean canRemove=false;
        boolean canEdit=false;
        boolean canFind=false;
        String mode=null;
        BusiMember member=null;

        //检查用户是否已经登录
        //boolean isLogin=this.userAction.isLogin(request);
        //如果用户未登录，执行登录处理
        //if(!isLogin)       isLogin=this.userAction.login(request);
        //如果用户已经登录，获取登录用户属性
        user=this.userAction.getLoginUserInSession(request);

        //支持外部查询获取,增加会员登录处理
        if(this.dataAction.supportNoLoginFind() && user==null)
        {
            //检查用户是否已经登录
            boolean isLogin=this.memberAction.isLogin(request);
            //如果用户未登录，执行登录处理
            if(!isLogin)       isLogin=this.memberAction.login(request);
            //如果用户已经登录，获取登录用户属性
            if(isLogin)
            {
                canCreate=false;
                canRemove=false;
                canEdit=true;
                canFind=true;
            }
        }
        else
        {
            if(user==null)
            {
                throw new ServletException("未登录无法使用此接口！");
            }
            //获取用户对如下模式的操作权限
            canCreate=user.isHaveLimit(this.getLimitPluginId(),LIMITTYPE_CREATE);
            canRemove=user.isHaveLimit(this.getLimitPluginId(),LIMITTYPE_REMOVE);
            canEdit=user.isHaveLimit(this.getLimitPluginId(),LIMITTYPE_EDIT);
            canFind=user.isHaveLimit(this.getLimitPluginId(),LIMITTYPE_FIND);
        }

        //获取当前的操作模式(默认为搜索模式)
        mode=request.getParameter(PARAM_MODE);
        mode=(mode==null)?VALUE_MODE_LIST:mode;
        mode=mode.trim().toLowerCase();

        //创建模式
        if(VALUE_MODE_CREATE.equals(mode))
        {
            if(canCreate)
            {
                final Object newId=this.dataAction.create(request);
                String str=this.stringForReturn(mode);
                this.generateResult(out,str!=null?str:"成功插入新的数据信息",new HashMap<String,Object>(){{
                    put("newId",newId);
                }});
                this.recordUserLog(user,mode);
            }
            else
            {
                this.errorHandle(out,"用户没有创建此数据的权限");
            }
        }
        //删除模式
        else if(VALUE_MODE_REMOVE.equals(mode))
        {
            if(canRemove)
            {
                this.dataAction.remove(request);
                String str=this.stringForReturn(mode);
                this.generateResult(out,str!=null?str:"成功删除指定的数据及其相关信息");
                this.recordUserLog(user,mode);
            }
            else
            {
                this.errorHandle(out,"用户没有删除此数据的权限");
            }
        }
        //编辑模式
        else if(VALUE_MODE_EDIT.equals(mode))
        {
            if(canEdit)
            {
                this.dataAction.edit(request);
                String str=this.stringForReturn(mode);
                this.generateResult(out,str!=null?str:"成功修改指定的数据信息");
                this.recordUserLog(user,mode);
            }
            else
            {
                this.errorHandle(out,"用户没有修改此数据的权限");
            }
        }
        //查询模式
        else if(VALUE_MODE_LIST.equals(mode))
        {
            if(canFind)
            {
                out.print(
                        (this.isExcel)?this.dataAction.findToExcel(request)
                                :((this.isXml)?this.dataAction.findToXml(request)
                                :this.dataAction.findToJson(request))
                );
                this.recordUserLog(user,mode);
            }
            else
            {
                this.errorHandle(out,"用户没有查询此数据的权限");
            }
        }
        //错误模式
        else
        {
            throw new ServletException("错误的操作模式，请检查请求参数 - "+PARAM_MODE+"！");
        }
    }

    /**方法（受保护）
     * 名称:initDataAction
     * 描述:初始化数据应用层接口
     */
    @Override protected void initDataAction()
    {
        this.dataAction=new BusiMemberAction(this.connector);
        this.memberAction=(BusiMemberAction)this.dataAction;
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