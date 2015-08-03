package pb.system.limit.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * 数据表应用层方法接口。
 * 声明应用层相关的数据处理方法。
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/11/04
 */
public interface DataAction
{
    /**
     * 描述:    创建数据<br>
     *
     * @param request - HTTP请求对象
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    void create(HttpServletRequest request) throws ServletException;

    /**
     * 描述:    删除数据<br>
     *
     * @param request - HTTP请求对象
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    void remove(HttpServletRequest request) throws ServletException;

    /**
     * 描述:    编辑数据<br>
     *
     * @param request - HTTP请求对象
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    void edit(HttpServletRequest request) throws ServletException;

    /**
     * 描述:    查询数据，返回Xml结果<br>
     *
     * @param request - HTTP请求对象
     * @return String - 获取成功返回XML格式的列表结果
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    String findToXml(HttpServletRequest request) throws ServletException;

    /**
     * 描述:    查询数据，返回Json结果<br>
     *
     * @param request - HTTP请求对象
     * @return String - 获取成功返回JSON格式的列表结果
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    String findToJson(HttpServletRequest request) throws ServletException;

    /**
     * 描述:    查询数据，返回Excel结果<br>
     *
     * @param request - HTTP请求对象
     * @return String - 获取成功返回Excel格式的列表结果
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    String findToExcel(HttpServletRequest request) throws ServletException;

    /**
     * 描述:    获取最后创建的数据主键（适用于生成器方式）<br>
     *
     * @return String - 最后创建的数据主键
     */
    String lastKeyGenerator();

    /**
     * 描述:    是否支持非登录下查询处理<br>
     *
     * @return boolean - 如果参数值为null或""，返回true
     */
    boolean supportNoLoginFind();

    /**
     * 描述:    返回非登录模式下的处理模式<br>
     *
     * @param request
     * @return boolean - 如果参数值为null或""，返回true
     */
    String supportNoLoginMode(HttpServletRequest request);
}