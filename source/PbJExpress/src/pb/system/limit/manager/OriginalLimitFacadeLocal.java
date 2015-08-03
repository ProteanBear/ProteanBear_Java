package pb.system.limit.manager;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.OriginalLimit;

/**
 * 数据表映射类数据管理接口——基础码表，用于在创建权限时生成基础权限（当INIT属性为true时），
 * 以及在创建编辑权限时选择权限类型。 此处为添加权限
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public interface OriginalLimitFacadeLocal extends AbstractFacadeLocal<OriginalLimit>
{
}