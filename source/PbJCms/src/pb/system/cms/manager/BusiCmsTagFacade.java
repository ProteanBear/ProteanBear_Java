package pb.system.cms.manager;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.system.cms.entity.BusiCmsTag;

/**
 * 数据表映射类数据管理类——
 *@author ProteanBear
 *@version 1.00 2016-04-07
 */
public class BusiCmsTagFacade 
			extends AbstractEntityManager<BusiCmsTag>
			implements BusiCmsTagFacadeLocal
{
    /**构造函数
     * @param connector - 指定数据连接器
     */
    public BusiCmsTagFacade(Connector connector)
    {
        super(connector,BusiCmsTag.class);
    }

    /**方法（受保护）
     * 名称:    getPrimaryKeyName
     * 描述:    获取数据表主键字段名称
     * @return String - 获取主键名称
     */
    @Override public String getPrimaryKeyName()
    {
        return "custId";
    }
}