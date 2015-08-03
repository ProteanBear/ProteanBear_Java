package pb.system.limit.module;

import java.util.List;

import pb.system.limit.entity.SystemAreaType;
import pb.system.limit.entity.SystemAreaTypePlugin;

/**
 * 数据表输出类——企业类型信息表。<br>
 * 记录企业类型的相关信息。<br>
 * 继承自数据表映射类，添加插件列表属性。<br>
 *
 * @author proteanBear(马强)
 * @version 1.00 2012/02/28
 */
public class SystemAreaTypeOutput extends SystemAreaType
{
    /**
     * 域(受保护)<br>
     * 名称:    typePlugins<br>
     * 描述:    记录企业类型对应的插件列表<br>
     */
    protected Integer[] typePlugins;

    /**
     * 构造函数<br>
     *
     * @param areaType - 企业类型
     */
    public SystemAreaTypeOutput(SystemAreaType areaType)
    {
        this.typeId=areaType.getTypeId();
        this.typeName=areaType.getTypeName();
        this.typeLevel=areaType.getTypeLevel();
        this.dataDelete=areaType.getDataDelete();
        this.dataRemark=areaType.getDataRemark();
        //this.typeDescription=areaType.getTypeDescription();
    }

    /**
     * 访问器<br>
     * 目标：   typePlugins<br>
     *
     * @return String - 企业类型对应的插件列表
     */
    public Integer[] getTypePlugins()
    {
        return typePlugins;
    }

    /**
     * 更改器<br>
     * 目标：   typePlugins<br>
     *
     * @param typePlugins - 企业类型对应的插件列表
     */
    public void setTypePlugins(List<SystemAreaTypePlugin> typePlugins)
    {
        if(typePlugins!=null)
        {
            this.typePlugins=new Integer[typePlugins.size()];
            for(int i=0;i<typePlugins.size();i++)
            {
                this.typePlugins[i]=typePlugins.get(i).getPluginCustId();
            }
        }
    }
}
