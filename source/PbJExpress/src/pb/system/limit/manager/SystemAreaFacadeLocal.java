package pb.system.limit.manager;

import java.util.List;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.SystemArea;

/**
 * 数据表映射类数据管理接口——行政区域数据表，用来对系统中的企业相关的区域进行划分记录，
 * 通过区域标识实现树形结构，在树中即可使用地区也可适用类型，可自由的划分相关区域，如云平台->地区->企
 * 1.04 - 增加获取指定行政区域标识对应的级别深度的方法。<br>
 * 1.03 - 增加普通列表转换为树形结构的方法。<br>
 * 1.02 - 增加判断是否有下级行政区域的接口方法。
 * 1.01 - 增加重载的行政区域创建接口方法。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public interface SystemAreaFacadeLocal extends AbstractFacadeLocal<SystemArea>
{
    /**
     * 描述:    重载并标识为过期方法，因为添加行政区域必须指定上级区域，请使用create(String upId,SystemArea area)方法<br>
     *
     * @param entity - 添加记录的描述对象
     * @return boolean - 添加成功，返回true;
     */
    @Deprecated
    @Override
    boolean create(SystemArea entity);

    /**
     * 描述:    添加一条新的记录到数据库中<br>
     *
     * @param upId - 指定的顶层区域标识
     * @param area - 添加记录的描述对象
     * @return boolean - 添加成功，返回true;
     */
    boolean create(String upId,SystemArea area);

    /**
     * 描述:    获取指定区域的下一级所有区域<br>
     *
     * @param upId - 指定的顶层区域标识
     * @return List - 下一级区域列表
     */
    List<SystemArea> childrenNextDepth(String upId);

    /**
     * 描述:    获取指定区域的所有低层次区域<br>
     *
     * @param upId - 指定的顶层区域标识
     * @return List - 低层次所有区域列表
     */
    List<SystemArea> childrenAllDownDepth(String upId);

    /**
     * 描述:    查看指定的行政区域是否有下级行政区域<br>
     *
     * @param upId - 指定的顶层区域标识
     * @return boolean - 如果有下级行政区域，返回true
     */
    boolean existChildren(String upId);

    /**
     * 描述:    获取指定行政区域标识的上级行政区域标识<br>
     *
     * @param areaId - 指定的顶层区域标识
     * @return String - 返回上级行政区域标识
     */
    String getUpAreaId(String areaId);

    /**
     * 描述:    获取指定行政区域标识对应的级别深度<br>
     *
     * @param areaId - 行政区域标识
     * @return int - 深度级别
     */
    int getDepth(String areaId);
}