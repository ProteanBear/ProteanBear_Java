package pb.system.cms.manager;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.system.cms.entity.BusiFeedback;

/**
 * 数据表映射类数据管理类——记录应用的意见反馈信息
 *
 * @author ProteanBear
 * @version 1.00 2014-11-20
 */
public class BusiFeedbackFacade
        extends AbstractEntityManager<BusiFeedback>
        implements BusiFeedbackFacadeLocal
{
    /**
     * 构造函数
     *
     * @param connector - 指定数据连接器
     */
    public BusiFeedbackFacade(Connector connector)
    {
        super(connector,BusiFeedback.class);
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
        return "custId";
    }
}