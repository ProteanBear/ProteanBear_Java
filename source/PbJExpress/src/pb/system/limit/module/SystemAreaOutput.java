package pb.system.limit.module;

import java.util.List;

import pb.system.limit.entity.SystemArea;
import pb.system.limit.entity.SystemAreaTypeMap;

/**
 * 数据表输出类——行政区域信息表。<br>
 * 记录区域类型的相关信息。<br>
 * 继承自数据表映射类，添加区域类型列表属性。<br>
 *
 * @author proteanBear(马强)
 * @version 1.00 2012/02/29
 */
public class SystemAreaOutput extends SystemArea
{
    /**
     * 域(受保护)<br>
     * 名称:    upId<br>
     * 描述:    记录区域的父区域标识<br>
     */
    protected String upId;

    /**
     * 域(受保护)<br>
     * 名称:    areaTypeMaps<br>
     * 描述:    记录区域对应的企业类型列表,插件之间以“,”分隔<br>
     */
    protected String[] areaTypeMaps;

    /**
     * 域(受保护)<br>
     * 名称:    displayAreaName<br>
     * 描述:    记录区域显示名称(带层次标识)<br>
     */
    protected String displayAreaName;

    /**
     * 构造函数<br>
     *
     * @param area - 行政区域
     */
    public SystemAreaOutput(SystemArea area)
    {
        this.areaId=area.getAreaId();
        this.areaName=area.getAreaName();
        this.areaClass=area.getAreaClass();
        this.displayAreaName=area.getDisplayAreaName();
        this.areaIcon=area.getAreaIcon();
    }

    /**
     * 访问器<br>
     * 目标：   areaTypeMaps<br>
     *
     * @return String - 区域对应的企业类型列表
     */
    public String[] getAreaTypeMaps()
    {
        return areaTypeMaps;
    }

    /**
     * 更改器<br>
     * 目标：   areaTypeMaps<br>
     *
     * @param areaTypeMaps - 区域对应的企业类型列表
     */
    public void setAreaTypeMaps(List<SystemAreaTypeMap> areaTypeMaps)
    {
        if(areaTypeMaps!=null)
        {
            this.areaTypeMaps=new String[areaTypeMaps.size()];
            for(int i=0;i<areaTypeMaps.size();i++)
            {
                this.areaTypeMaps[i]=areaTypeMaps.get(i).getTypeId();
            }
        }
    }

    /**
     * 访问器<br>
     * 目标：   upId<br>
     *
     * @return String - 区域的父区域标识
     */
    public String getUpId()
    {
        return upId;
    }

    /**
     * 更改器<br>
     * 目标：   upId<br>
     *
     * @param upId - 区域的父区域标识
     */
    public void setUpId(String upId)
    {
        this.upId=upId;
    }

    /**
     * 访问器<br>
     * 目标：   displayAreaName<br>
     *
     * @return String - 区域显示名称(带层次标识)
     */
    @Override
    public String getDisplayAreaName()
    {
        return displayAreaName;
    }

    /**
     * 更改器<br>
     * 目标：   displayAreaName<br>
     *
     * @param diplayAreaName - 区域显示名称(带层次标识)
     */
    public void setDisplayAreaName(String diplayAreaName)
    {
        this.displayAreaName=diplayAreaName;
    }
}
