package pb.system.cms.manager;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.system.cms.entity.BusiCmsComment;

/**
 * 数据表映射类数据管理类——
 *@author ProteanBear
 *@version 1.00 2016-04-11
 */
public class BusiCmsCommentFacade 
			extends AbstractEntityManager<BusiCmsComment>
			implements BusiCmsCommentFacadeLocal
{
    /**构造函数
     * @param connector - 指定数据连接器
     */
    public BusiCmsCommentFacade(Connector connector)
    {
        super(connector,BusiCmsComment.class);
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

    /**
     * 方法（受保护）<br/>
     * 名称:    getFindOrderBy<br/>
     * 描述:    获取查询使用的排序方法
     *
     * @return String - 排序方法
     */
    @Override
    protected String getFindOrderBy()
    {
        this.orderBy="cust_id desc";
        return this.orderBy;
    }
}