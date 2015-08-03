package pb.system.limit.entity;

/**
 * 数据表映射类——行政区域数据表，用来对系统中的企业相关的区域进行划分记录，
 * 通过区域标识实现树形结构，在树中即可使用地区也可适用类型，可自由的划分相关区域，如云平台->地区->企
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class SystemArea
{
    /**
     * 域(受保护)
     * 名称: areaId
     * 描述: 区域编码，使用字符形式对区域树形进行逻辑划分，如树顶层为1000，第二层第一条为10000000、第二条为10000001，依次类推；查询时使用正则匹配和编码排序来进行相应控制。
     */
    protected String areaId;

    /**
     * 访问器
     * 目标: areaId
     *
     * @return String - 区域编码，使用字符形式对区域树形进行逻辑划分，如树顶层为1000，第二层第一条为10000000、第二条为10000001，依次类推；查询时使用正则匹配和编码排序来进行相应控制。
     */
    public String getAreaId()
    {
        return areaId;
    }

    /**
     * 更改器
     * 目标: areaId
     *
     * @param areaId - 区域编码，使用字符形式对区域树形进行逻辑划分，如树顶层为1000，第二层第一条为10000000、第二条为10000001，依次类推；查询时使用正则匹配和编码排序来进行相应控制。
     */
    public void setAreaId(String areaId)
    {
        this.areaId=areaId;
    }

    /**
     * 域(受保护)
     * 名称: areaName
     * 描述: 行政区域显示名称
     */
    protected String areaName;

    /**
     * 访问器
     * 目标: areaName
     *
     * @return String - 行政区域显示名称
     */
    public String getAreaName()
    {
        return areaName;
    }

    /**
     * 更改器
     * 目标: areaName
     *
     * @param areaName - 行政区域显示名称
     */
    public void setAreaName(String areaName)
    {
        this.areaName=areaName;
    }

    /**
     * 域(受保护)
     * 名称: areaClass
     * 描述: 区域类型，0-目录、1-企业、2-部门
     */
    protected int areaClass;

    /**
     * 访问器
     * 目标: areaClass
     *
     * @return int - 区域类型，0-目录、1-企业、2-部门
     */
    public int getAreaClass()
    {
        return areaClass;
    }

    /**
     * 更改器
     * 目标: areaClass
     *
     * @param areaClass - 区域类型，0-目录、1-企业、2-部门
     */
    public void setAreaClass(int areaClass)
    {
        this.areaClass=areaClass;
    }

    /**
     * 更改器
     * 目标: areaClass
     *
     * @param areaClass - 区域类型，0-目录、1-企业、2-部门
     */
    public void setAreaClass(String areaClass)
    {
        this.areaClass=new Integer(areaClass);
    }

    /**
     * 域(受保护)
     * 名称: areaIconClose
     * 描述: 关闭状态行政区域图标链接地址
     */
    protected String areaIconClose;

    /**
     * 访问器
     * 目标: areaIconClose
     *
     * @return String - 关闭状态行政区域图标链接地址
     */
    public String getAreaIconClose()
    {
        return "".equals(areaIconClose)?this.areaIcon:this.areaIconClose;
    }

    /**
     * 更改器
     * 目标: areaIconClose
     *
     * @param areaIconClose - 关闭状态行政区域图标链接地址
     */
    public void setAreaIconClose(String areaIconClose)
    {
        this.areaIconClose=areaIconClose;
    }

    /**
     * 域(受保护)
     * 名称: areaIconOpen
     * 描述: 打开状态行政区域图标链接地址
     */
    protected String areaIconOpen;

    /**
     * 访问器
     * 目标: areaIconOpen
     *
     * @return String - 打开状态行政区域图标链接地址
     */
    public String getAreaIconOpen()
    {
        return "".equals(areaIconOpen)?this.areaIcon:this.areaIconOpen;
    }

    /**
     * 更改器
     * 目标: areaIconOpen
     *
     * @param areaIconOpen - 打开状态行政区域图标链接地址
     */
    public void setAreaIconOpen(String areaIconOpen)
    {
        this.areaIconOpen=areaIconOpen;
    }

    /**
     * 域(受保护)
     * 名称: areaIcon
     * 描述: 打开状态行政区域图标链接地址
     */
    protected String areaIcon;

    /**
     * 访问器
     * 目标: areaIconOpen
     *
     * @return String - 打开状态行政区域图标链接地址
     */
    public String getAreaIcon()
    {
        return areaIcon;
    }

    /**
     * 更改器
     * 目标: areaIcon
     *
     * @param areaIcon - 打开状态行政区域图标链接地址
     */
    public void setAreaIcon(String areaIcon)
    {
        this.areaIcon=areaIcon;
    }

    /**
     * 域(受保护)
     * 名称: areaContact
     * 描述: 企业或部门联系人
     */
    protected String areaContact;

    /**
     * 访问器
     * 目标: areaContact
     *
     * @return String - 企业或部门联系人
     */
    public String getAreaContact()
    {
        return areaContact;
    }

    /**
     * 更改器
     * 目标: areaContact
     *
     * @param areaContact - 企业或部门联系人
     */
    public void setAreaContact(String areaContact)
    {
        this.areaContact=areaContact;
    }

    /**
     * 域(受保护)
     * 名称: areaAddress
     * 描述: 企业或部门联系地址
     */
    protected String areaAddress;

    /**
     * 访问器
     * 目标: areaAddress
     *
     * @return String - 企业或部门联系地址
     */
    public String getAreaAddress()
    {
        return areaAddress;
    }

    /**
     * 更改器
     * 目标: areaAddress
     *
     * @param areaAddress - 企业或部门联系地址
     */
    public void setAreaAddress(String areaAddress)
    {
        this.areaAddress=areaAddress;
    }

    /**
     * 域(受保护)
     * 名称: areaTel
     * 描述: 企业或部门联系电话
     */
    protected String areaTel;

    /**
     * 访问器
     * 目标: areaTel
     *
     * @return String - 企业或部门联系电话
     */
    public String getAreaTel()
    {
        return areaTel;
    }

    /**
     * 更改器
     * 目标: areaTel
     *
     * @param areaTel - 企业或部门联系电话
     */
    public void setAreaTel(String areaTel)
    {
        this.areaTel=areaTel;
    }

    /**
     * 域(受保护)
     * 名称: dataRemark
     * 描述: 数据备注信息
     */
    protected String dataRemark;

    /**
     * 访问器
     * 目标: dataRemark
     *
     * @return String - 数据备注信息
     */
    public String getDataRemark()
    {
        return dataRemark;
    }

    /**
     * 更改器
     * 目标: dataRemark
     *
     * @param dataRemark - 数据备注信息
     */
    public void setDataRemark(String dataRemark)
    {
        this.dataRemark=dataRemark;
    }

    /**
     * 域(受保护)
     * 名称: dataDelete
     * 描述: 数据删除标志位
     */
    protected int dataDelete;

    /**
     * 访问器
     * 目标: dataDelete
     *
     * @return int - 数据删除标志位
     */
    public int getDataDelete()
    {
        return dataDelete;
    }

    /**
     * 更改器
     * 目标: dataDelete
     *
     * @param dataDelete - 数据删除标志位
     */
    public void setDataDelete(int dataDelete)
    {
        this.dataDelete=dataDelete;
    }

    /**
     * 更改器
     * 目标: dataDelete
     *
     * @param dataDelete - 数据删除标志位
     */
    public void setDataDelete(String dataDelete)
    {
        this.dataDelete=new Integer(dataDelete);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDisplayAreaName<br>
     * 描述:    获取行政区域名称的显示名称（带层次结构显示符）<br>
     *
     * @return String - 获取行政区域的显示名称
     */
    public String getDisplayAreaName()
    {
        String prefix="";
        if(this.areaId!=null)
        {
            for(int i=0;i<this.areaId.length()/4;i++)
            {
                prefix+="—";
            }
        }
        return prefix+this.areaName;
    }
}