package pb.system.limit.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.system.limit.entity.SystemApplication;
import pb.text.DateProcessor;

/**
 * 数据表映射类数据管理类——记录企业运维的APP应用的相关信息。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class SystemApplicationFacade
        extends AbstractEntityManager<SystemApplication>
        implements SystemApplicationFacadeLocal
{
    /**
     * 域(私有)<br>
     * 名称:    dp<br>
     * 描述:    日期处理器对象<br>
     */
    private DateProcessor dp;

    /**
     * 构造函数
     *
     * @param connector - 指定数据连接器
     */
    public SystemApplicationFacade(Connector connector)
    {
        super(connector,SystemApplication.class);
        this.dp=new DateProcessor("yyyyMMddHHmmss");
    }

    /**
     * 返回排序字段
     * @return
     */
    @Override
    protected String getFindOrderBy()
    {
        this.orderBy="order by app_order asc";
        return this.orderBy;
    }

    /**
     * 方法（受保护）
     * 名称:    getPrimaryKeyName
     * 描述:    获取数据表主键字段名称
     *
     * @return String - 获取主键名称
     */
    @Override
    public String getPrimaryKeyName()
    {
        return "appId";
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getKeyGenerator<br>
     * 描述:    返回自增主键的生成器，非生成的主键返回""<br>
     *
     * @return String - 主键生成值
     */
    @Override
    protected String getKeyGenerator()
    {
        return this.dp.getCurrent();
    }

    /**
     * 方法（受保护）<br>
     * 名称:    findByAppCode<br>
     * 描述:    根据指定编码获取应用数据<br>
     *
     * @param code - 指定的数据编码
     * @return SystemApplication - 应用数据
     */
    @Override
    public SystemApplication findByAppCode(String code)
    {
        Map<String,Object> condition=new HashMap<>();
        condition.put("appCode=?",code);

        List<SystemApplication> list=this.find(condition);
        return (list!=null && !list.isEmpty())?list.get(0):null;
    }
}