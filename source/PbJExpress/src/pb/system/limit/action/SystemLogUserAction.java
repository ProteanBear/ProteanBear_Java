package pb.system.limit.action;

import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import pb.data.Connector;
import pb.system.limit.entity.SystemLogUser;
import pb.system.limit.manager.SystemLogUserFacade;
import pb.system.limit.manager.SystemLogUserFacadeLocal;

/**
 * 数据应用层类 - 告警信息表。<br>
 *
 * @author jia_stop
 * @version 1.00 2012/03/14
 */
public class SystemLogUserAction
        extends AbstractAction<SystemLogUser>
        implements DataAction
{
    /**
     * 域(受保护)<br>
     * 名称:    manager<br>
     * 描述:    记录当前使用的数据管理器<br>
     */
    protected SystemLogUserFacadeLocal logManager;

    /**
     * 构造函数<br>
     *
     * @param connector - 数据库连接器对象
     */
    public SystemLogUserAction(Connector connector)
    {
        super(connector,SystemLogUser.class);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    initManager<br>
     * 描述:    初始化数据管理器<br>
     */
    @Override
    protected void initManager()
    {
        this.manager=new SystemLogUserFacade(connector);
        this.logManager=(SystemLogUserFacadeLocal)this.manager;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getAreaLimitField<br>
     * 描述:    指定当前业务处理部门限制使用的行政区域字段名，使用null为不指定<br>
     *
     * @return String - 当前业务处理部门限制使用的行政区域字段名
     */
    @Override
    protected String getAreaLimitField()
    {
        return "areaId";
    }

    /**
     * 方法（公共）
     * 名称：   create
     * 描述:    创建数据<br>
     *
     * @param request - HTTP请求对象
     * @throws ServletException - 抛出处理错误
     */
    @Override
    public void create(HttpServletRequest request)
            throws ServletException
    {
        throw new UnsupportedOperationException("告警记录无法手动添加，平台不支持此方法！");
    }

    /**
     * 方法（公共）
     * 名称：   remove
     * 描述:    删除数据<br>
     *
     * @param request - HTTP请求对象
     * @throws ServletException - 抛出处理错误
     */
    @Override
    public void remove(HttpServletRequest request)
            throws ServletException
    {
        throw new UnsupportedOperationException("告警记录无法手动删除，平台不支持此方法！");
    }

    /**
     * 方法（公共）
     * 名称：   edit
     * 描述:    编辑数据<br>
     *
     * @param request - HTTP请求对象
     * @throws ServletException - 抛出处理错误
     */
    @Override
    public void edit(HttpServletRequest request)
            throws ServletException
    {
        throw new UnsupportedOperationException("告警记录无法手动修改，平台不支持此方法！");
    }

    /**
     * 方法（受保护）<br>
     * 名称:    findTo<br>
     * 描述:    查询数据，并返回List<br>
     *
     * @param request - HTTP请求对象
     * @return List<GpsLocusHistory> - 数据对象列表
     * @throws ServletException - 抛出处理错误
     */
    @Override
    protected List<SystemLogUser> findTo
    (HttpServletRequest request)
            throws ServletException
    {
        try
        {
            //判断数据管理器是否初始化
            if(this.managerNullCheck())
            {
                throw new ServletException("数据库管理器未初始化！");
            }

            //获取指定的搜索条件
            Map<String,Object> condition=this.generateCondition(request);

            String dataTime=(String)condition.get("dateTime");
            if(this.paramNullCheck(dataTime))
            {
                throw new ServletException("缺少必要的查询条件");
            }
            condition.remove("dateTime");

            //查询结果
            List<SystemLogUser> list=this.logManager.find(condition,dataTime);
            if(list==null) throw new ServletException(this.logManager.getError());
            return list;
        }
        catch(ServletException ex)
        {
            throw new ServletException(ex.toString());
        }
    }
}
