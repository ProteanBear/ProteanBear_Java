<%@ page trimDirectiveWhitespaces="true"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="pb.system.limit.servlet.AbstractServlet"%>
<%@ page import="pb.pool.CachingServiceLocator"%>
<%
    /*根据web.xml的配置转向默认应用*/
    
    //读取配置信息
    String redirectUrl=CachingServiceLocator.getInstance()
            .getString(AbstractServlet.ENV_CONFIG_PAGE_WELCOME);
    
    //跳转到默认应用界面
    String param = request.getQueryString();
    if ((param != null) && (!"".equals(param)))
    {
        redirectUrl = redirectUrl + "?" + param;
    }
    response.sendRedirect(redirectUrl);
    
    //*/
%>