package pb.system.limit.module;

import java.util.List;

import pb.system.limit.entity.SystemApplication;
import pb.system.limit.entity.SystemApplicationPlatform;
import pb.system.limit.entity.SystemArea;
import pb.system.limit.entity.SystemAreaTypeMap;
import pb.system.limit.entity.SystemRole;
import pb.system.limit.entity.SystemUser;
import pb.system.limit.entity.SystemVersion;
import pb.system.limit.manager.SystemVersionFacadeLocal;

/**
 * 行政区域树结构节点类。<br>
 * 继承行政区域类(SystemArea)的基本属性，添加适用于zTree的相关属性
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/12/14
 */
public class SystemAreaTreeNode extends SystemArea
{
    /**
     * 域(私有)<br>
     * 名称:    id<br>
     * 描述:    记录当前节点的标识<br>
     */
    private String id;

    /**
     * 域(私有)<br>
     * 名称:    pId<br>
     * 描述:    记录当前行政区域的父亲节点标识<br>
     */
    private String pId;

    /**
     * 域(私有)<br>
     * 名称:    name<br>
     * 描述:    记录当前行政区域的节点显示名称<br>
     */
    private String name;

    /**
     * 域(私有)<br>
     * 名称:    type<br>
     * 描述:    记录当前行政区域的节点显示类型<br>
     */
    private int type;

    /**
     * 域(私有)<br>
     * 名称:    isParent<br>
     * 描述:    记录当前行政区域的节点是否为父节点<br>
     */
    private boolean isParent;

    /**
     * 域(私有)<br>
     * 名称:    open<br>
     * 描述:    记录当前行政区域的节点是否展开<br>
     */
    private boolean open;

    /**
     * 域(私有)<br>
     * 名称:    iconOpen<br>
     * 描述:    节点打开时的图标链接地址<br>
     */
    private String iconOpen;

    /**
     * 域(私有)<br>
     * 名称:    iconClose<br>
     * 描述:    节点收缩时的图标链接地址<br>
     */
    private String iconClose;

    /**
     * 域(私有)<br>
     * 名称:    icon<br>
     * 描述:    节点图标链接地址<br>
     */
    private String icon;

    /**
     * 域(受保护)<br>
     * 名称:    areaTypeMaps<br>
     * 描述:    记录区域对应的企业类型列表,插件之间以“,”分隔<br>
     */
    protected String[] areaTypeMaps;

    /**
     * 构造函数<br>
     */
    public SystemAreaTreeNode()
    {
    }

    /**
     * 构造函数<br>
     *
     * @param app - 企业应用
     */
    public SystemAreaTreeNode(SystemApplication app)
    {
        this.id=app.getAppId();
        this.name=app.getAppName();
        this.areaName=app.getAppCode();
        this.type=2;
        this.icon=app.getAppThumbnail();
        this.areaAddress=app.getAppIcon();
        this.dataRemark=app.getDataRemark();
        this.areaContact=app.getAppAlias();
        this.areaTel=app.getAppWeb();
    }

    /**
     * 构造函数<br>
     *
     * @param user - 系统用户
     */
    public SystemAreaTreeNode(SystemUser user)
    {
        this.id=user.getCustId()+"";
        this.name=user.getUserNick();
        this.areaName=user.getUserName();
        this.type=3;
        this.icon=user.getUserIcon();
        this.areaContact=user.getUserQq();
        this.areaTel=user.getUserTel();
    }

    /**
     * 构造函数<br>
     *
     * @param area - 行政区域对象
     * @param open - 是否打开
     */
    public SystemAreaTreeNode(SystemArea area,boolean open)
    {
        this.setAreaId(area.getAreaId());
        this.setAreaName(area.getAreaName());
        this.setAreaIconOpen(area.getAreaIconOpen());
        this.setAreaIconClose(area.getAreaIconClose());
        this.setAreaClass(area.getAreaClass());
        this.setAreaAddress(area.getAreaAddress());
        this.setAreaContact(area.getAreaContact());
        this.setAreaTel(area.getAreaTel());
        this.setDataDelete(area.getDataDelete());
        this.setDataRemark(area.getDataRemark());
        this.setAreaIcon(area.getAreaIcon());
        this.id=area.getAreaId();
        this.name=area.getAreaName();
        this.pId=(this.id.length()>4)?(this.id.substring(0,this.id.length()-4)):"0";
        this.type=area.getAreaClass();
        this.isParent=true;
        this.open=open;
        this.iconOpen=area.getAreaIconOpen();
        this.iconClose=area.getAreaIconClose();
    }

    /**
     * 更改器<br>
     * 目标：   areaTypeMaps<br>
     *
     * @param userRole - 区域对应的用户角色列表
     */
    public void setAreaTypeMapsForRole(List<SystemRole> userRole)
    {
        if(userRole!=null)
        {
            this.areaTypeMaps=new String[userRole.size()];
            for(int i=0;i<userRole.size();i++)
            {
                this.areaTypeMaps[i]=userRole.get(i).getRoleId();
            }
        }
    }

    /**
     * 更改器<br>
     * 目标：   areaTypeMaps<br>
     *
     * @param appPlat        - 应用对应的平台列表
     * @param versionManager
     */
    public void setAreaTypeMapsForPlat(List<SystemApplicationPlatform> appPlat,
                                       SystemVersionFacadeLocal versionManager)
    {
        if(appPlat!=null)
        {
            this.areaTypeMaps=new String[appPlat.size()*3];
            for(int i=0;i<appPlat.size();i++)
            {
                this.areaTypeMaps[i*3+0]=appPlat.get(i).getPlatId()+"";

                SystemVersion version=versionManager.findLastestByPlatId(appPlat.get(i).getCustId());
                this.areaTypeMaps[i*3+1]=(version!=null)?(version.getAppPlatId()+""):"";
                this.areaTypeMaps[i*3+2]=(version!=null)?(version.getVersionName()+""):"";
            }
        }
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

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id=id;
    }

    public String getPId()
    {
        return pId;
    }

    public void setPId(String pId)
    {
        this.pId=pId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name=name;
    }

    public boolean getIsParent()
    {
        return isParent;
    }

    public void setIsParent(boolean isParent)
    {
        this.isParent=isParent;
    }

    public boolean isOpen()
    {
        return open;
    }

    public boolean getOpen()
    {
        return open;
    }

    public void setOpen(boolean open)
    {
        this.open=open;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type=type;
    }

    public String getIconOpen()
    {
        return iconOpen;
    }

    public void setIconOpen(String iconOpen)
    {
        this.iconOpen=iconOpen;
    }

    public String getIconClose()
    {
        return iconClose;
    }

    public void setIconClose(String iconClose)
    {
        this.iconClose=iconClose;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon=icon;
    }
}
