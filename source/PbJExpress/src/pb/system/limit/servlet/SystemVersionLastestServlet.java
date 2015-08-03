/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pb.system.limit.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.system.limit.action.SystemVersionAction;
import pb.system.limit.entity.SystemVersion;
import pb.system.limit.module.LoginUser;

/**
 * @author maqiang
 */
public class SystemVersionLastestServlet extends AbstractServlet
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
    @Override
    protected void processRequest
    (HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException
    {
        String build=request.getParameter("versionBuild");
        if(this.paramNullCheck(build))
        {
            throw new ServletException("未指定当前的Build版本编号！");
        }

        this.generateResult(
                response.getWriter(),build,
                ((SystemVersionAction)this.dataAction).lastVersionForAppPlat(request)
        );
    }

    /**
     * 方法（私有）<br>
     * 名称:    recordUserLog<br>
     * 描述:    记录用户操作日志信息<br>
     *
     * @param user - 登录用户对象
     * @param mode - 当前的操作模式
     */
    @Override
    protected void recordUserLog(LoginUser user,String mode)
    {

    }

    /**
     * 方法（受保护）<br>
     * 名称:    generateResult<br>
     * 描述:    根据当前要求的输出格式，生成相应的增删改的结果<br>
     *
     * @param out     - 输入对象
     * @param build
     * @param version - HTTP请求对象
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    protected void generateResult(PrintWriter out,String build,SystemVersion version)
            throws IOException, ServletException
    {
        if(this.isXml)
        {
            out.print(this.generateXmlResult(build,version));
        }
        else
        {
            out.print(this.generateJsonResult(build,version));
        }
    }

    /**
     * 方法（私有）<br>
     * 名称:    generateXmlResult<br>
     * 描述:    生成Xml结果<br>
     *
     * @param user - 登录用户对象
     * @return String - Xml格式的文本
     */
    private String generateXmlResult(String build,SystemVersion version)
    {
        StringBuilder result=new StringBuilder();

        //设置根节点信息
        result.append("<RETURN>");

        //设置是否成功显示字段
        result.append("<SUCCESS>TRUE</SUCCESS>");

        //设置结尾
        result.append("</RETURN>");

        return result.toString();
    }

    /**
     * 方法（私有）<br>
     * 名称:    generateJsonResult<br>
     * 描述:    生成Json结果<br>
     *
     * @param user - 登录用户对象
     * @return String - Json格式的文本
     */
    private String generateJsonResult(String build,SystemVersion version)
    {
        StringBuilder result=new StringBuilder();

        //设置根节点信息
        result.append("{");

        //设置是否成功显示字段
        result.append("\"success\":true,");

        //设置用户基本信息
        if(version!=null)
        {
            result.append("\"versionName\":").append(version.getVersionName()).append(",")
                    .append("\"versionCode\":\"").append(version.getVersionCode()).append("\",")
                    .append("\"versionBuild\":\"").append(version.getVersionBuild()).append("\",")
                    .append("\"versionDate\":\"").append(version.getVersionDate()).append("\",")
                    .append("\"versionLink\":\"").append(version.getDataRemark()).append("\",");
        }

        result.append("\"needUpdate\":").append((version!=null && !build.equals(version.getVersionBuild())));

        //设置结尾
        result.append("}");

        return result.toString();
    }
}