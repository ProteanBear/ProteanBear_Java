package pb.system.limit.manager;

import java.util.List;

import pb.data.AbstractFacadeLocal;
import pb.system.limit.entity.BusiSection;

/**
 * 数据表映射类数据管理接口——记录当前系统业务栏目划分的数据信息。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-31
 */
public interface BusiSectionFacadeLocal extends AbstractFacadeLocal<BusiSection>
{
    /**
     * 描述:    重载并标识为过期方法，因为添加行政区域必须指定上级区域，请使用create(String upId,SystemArea area)方法<br>
     *
     * @param entity - 添加记录的描述对象
     * @return boolean - 添加成功，返回true;
     */
    @Deprecated
    @Override
    boolean create(BusiSection entity);

    /**
     * 描述:    添加一条新的记录到数据库中<br>
     *
     * @param upId    - 指定的顶层区域标识
     * @param section - 添加记录的描述对象
     * @return boolean - 添加成功，返回true;
     */
    boolean create(String upId,BusiSection section);

    /**
     * 描述:    获取指定区域的下一级所有数据<br>
     *
     * @param upCode  - 指定的顶层数据标识
     * @param appCode - 应用编码
     * @return List - 下一级数据列表
     */
    List<BusiSection> childrenNextDepth(String upCode,String appCode);

    /**
     * 描述:    获取指定区域的所有低层次区域<br>
     *
     * @param upCode  - 指定的顶层区域标识
     * @param appCode - 应用编码
     * @return List - 低层次所有区域列表
     */
    List<BusiSection> childrenAllDownDepth(String upCode,String appCode);

    /**
     * 描述:    查看指定的栏目是否有下级栏目<br>
     *
     * @param upCode  - 指定的顶层区域标识
     * @param appCode - 应用编码
     * @return boolean - 如果有下级栏目，返回true
     */
    boolean existChildren(String upCode,String appCode);

    /**
     * 描述:    获取指定栏目标识的上级栏目标识<br>
     *
     * @param secCode - 指定的顶层区域标识
     * @return String - 返回上级栏目标识
     */
    String getUpAreaId(String secCode);

    /**
     * 描述:    获取指定栏目标识对应的级别深度<br>
     *
     * @param secCode - 栏目标识
     * @return int - 深度级别
     */
    int getDepth(String secCode);

    /**
     * 描述:    根据指定编码获取栏目数据<br>
     *
     * @param appCode - 指定的应用标识
     * @param code - 指定的数据编码
     * @return BusiSection - 应用数据
     */
    BusiSection findBySectionCode(String appCode,String code);
}