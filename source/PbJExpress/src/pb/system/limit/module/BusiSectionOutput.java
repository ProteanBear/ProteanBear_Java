package pb.system.limit.module;

import pb.system.limit.entity.BusiSection;

/**
 * 栏目节点输出类，添加适用于zTree的相关属性
 *
 * @author proteanBear(马强)
 * @version 1.00 2014/08/03
 */
public class BusiSectionOutput extends BusiSection
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
     * 描述:    记录当前栏目的父亲节点标识<br>
     */
    private String pId;

    /**
     * 域(私有)<br>
     * 名称:    name<br>
     * 描述:    记录当前栏目的节点显示名称<br>
     */
    private String name;

    /**
     * 域(私有)<br>
     * 名称:    open<br>
     * 描述:    记录当前栏目的节点是否展开<br>
     */
    private boolean open;

    /**
     * 域(私有)<br>
     * 名称:    isParent<br>
     * 描述:    记录当前栏目的节点是否为父节点<br>
     */
    private boolean isParent;

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
     * 构造函数<br>
     *
     * @param section - 栏目对象
     * @param open    - 是否打开
     */
    public BusiSectionOutput(BusiSection section,boolean open)
    {
        this.setCustId(section.getCustId());
        this.setDataRemark(section.getDataRemark());
        this.setSectionAlias(section.getSectionAlias());
        this.setSectionApp(section.getSectionApp());
        this.setSectionCode(section.getSectionCode());
        this.setSectionEnable(section.getSectionEnable());
        this.setSectionIcon(section.getSectionIcon());
        this.setSectionIconClose(section.getSectionIconClose());
        this.setSectionIconOpen(section.getSectionIconOpen());
        this.setSectionName(section.getSectionName());
        this.id=section.getSectionCode();
        this.name=section.getSectionName();
        this.pId=(this.id.length()>4)?(this.id.substring(0,this.id.length()-4)):"0";
        this.isParent=true;
        this.open=open;
        this.iconOpen=section.getSectionIconOpen();
        this.iconClose=section.getSectionIconClose();
        this.icon=section.getSectionIcon();
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
