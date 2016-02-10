package pb.system.limit.entity;

/**
 * 数据表映射类——记录系统管理用户的相关数据信息
 *
 * @author ProteanBear
 * @version 1.00 2014-07-15
 */
public class SystemUser
{
    /**
     * 域(受保护)
     * 名称: custId
     * 描述: 自增主键
     */
    protected int custId;

    /**
     * 访问器
     * 目标: custId
     *
     * @return int - 自增主键
     */
    public int getCustId()
    {
        return custId;
    }

    /**
     * 更改器
     * 目标: custId
     *
     * @param custId - 自增主键
     */
    public void setCustId(int custId)
    {
        this.custId=custId;
    }

    /**
     * 更改器
     * 目标: custId
     *
     * @param custId - 自增主键
     */
    public void setCustId(Integer custId)
    {
        this.custId=custId.intValue();
    }

    /**
     * 更改器
     * 目标: custId
     *
     * @param custId - 自增主键
     */
    public void setCustId(String custId)
    {
        this.custId=new Integer(custId);
    }

    /**
     * 域(受保护)
     * 名称: areaId
     * 描述: 用户所属的行政区域
     */
    protected String areaId;

    /**
     * 访问器
     * 目标: areaId
     *
     * @return String - 用户所属的行政区域
     */
    public String getAreaId()
    {
        return areaId;
    }

    /**
     * 更改器
     * 目标: areaId
     *
     * @param areaId - 用户所属的行政区域
     */
    public void setAreaId(String areaId)
    {
        this.areaId=areaId;
    }

    /**
     * 域(受保护)
     * 名称: areaName
     * 描述: 记录用户所属企业的名称
     */
    protected String areaName;

    /**
     * 访问器
     * 目标: areaName
     *
     * @return String - 记录用户所属企业的名称
     */
    public String getAreaName()
    {
        return areaName;
    }

    /**
     * 更改器
     * 目标: areaName
     *
     * @param areaName - 记录用户所属企业的名称
     */
    public void setAreaName(String areaName)
    {
        this.areaName=areaName;
    }

    /**
     * 域(受保护)
     * 名称: userName
     * 描述: 记录用户的登录名称
     */
    protected String userName;

    /**
     * 访问器
     * 目标: userName
     *
     * @return String - 记录用户的登录名称
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * 更改器
     * 目标: userName
     *
     * @param userName - 记录用户的登录名称
     */
    public void setUserName(String userName)
    {
        this.userName=userName;
    }

    /**
     * 域(受保护)
     * 名称: userNick
     * 描述: 记录用户的显示昵称
     */
    protected String userNick;

    /**
     * 访问器
     * 目标: userNick
     *
     * @return String - 记录用户的显示昵称
     */
    public String getUserNick()
    {
        return userNick==null?userName:userNick;
    }

    /**
     * 更改器
     * 目标: userNick
     *
     * @param userNick - 记录用户的显示昵称
     */
    public void setUserNick(String userNick)
    {
        this.userNick=userNick;
    }

    /**
     * 域(受保护)
     * 名称: userPassword
     * 描述: 用户登录密码，使用MD5编码
     */
    protected String userPassword;

    /**
     * 访问器
     * 目标: userPassword
     *
     * @return String - 用户登录密码，使用MD5编码
     */
    public String getUserPassword()
    {
        return userPassword;
    }

    /**
     * 更改器
     * 目标: userPassword
     *
     * @param userPassword - 用户登录密码，使用MD5编码
     */
    public void setUserPassword(String userPassword)
    {
        this.userPassword=userPassword;
    }

    /**
     * 域(受保护)
     * 名称: userIcon
     * 描述: 记录用户的头像图片链接
     */
    protected String userIcon;

    /**
     * 访问器
     * 目标: userIcon
     *
     * @return String - 记录用户的头像图片链接
     */
    public String getUserIcon()
    {
        return userIcon;
    }

    /**
     * 更改器
     * 目标: userIcon
     *
     * @param userIcon - 记录用户的头像图片链接
     */
    public void setUserIcon(String userIcon)
    {
        this.userIcon=userIcon;
    }

    /**
     * 域(受保护)
     * 名称: userEmail
     * 描述: 记录用户的电子邮箱
     */
    protected String userEmail;

    /**
     * 访问器
     * 目标: userEmail
     *
     * @return String - 记录用户的电子邮箱
     */
    public String getUserEmail()
    {
        return userEmail;
    }

    /**
     * 更改器
     * 目标: userEmail
     *
     * @param userEmail - 记录用户的电子邮箱
     */
    public void setUserEmail(String userEmail)
    {
        this.userEmail=userEmail;
    }

    /**
     * 域(受保护)
     * 名称: userTel
     * 描述: 记录用户的联系电话
     */
    protected String userTel;

    /**
     * 访问器
     * 目标: userTel
     *
     * @return String - 记录用户的联系电话
     */
    public String getUserTel()
    {
        return userTel;
    }

    /**
     * 更改器
     * 目标: userTel
     *
     * @param userTel - 记录用户的联系电话
     */
    public void setUserTel(String userTel)
    {
        this.userTel=userTel;
    }

    /**
     * 域(受保护)
     * 名称: userQq
     * 描述: 记录用户的QQ
     */
    protected String userQq;

    /**
     * 访问器
     * 目标: userQq
     *
     * @return String - 记录用户的QQ
     */
    public String getUserQq()
    {
        return userQq;
    }

    /**
     * 更改器
     * 目标: userQq
     *
     * @param userQq - 记录用户的QQ
     */
    public void setUserQq(String userQq)
    {
        this.userQq=userQq;
    }

    /**
     * 域(受保护)
     * 名称: userIsgroup
     * 描述: 是否为用户组
     */
    protected int userIsgroup;

    /**
     * 访问器
     * 目标: userIsgroup
     *
     * @return int - 是否为用户组
     */
    public int getUserIsgroup()
    {
        return userIsgroup;
    }

    /**
     * 更改器
     * 目标: userIsgroup
     *
     * @param userIsgroup - 是否为用户组
     */
    public void setUserIsgroup(int userIsgroup)
    {
        this.userIsgroup=userIsgroup;
    }

    /**
     * 更改器
     * 目标: userIsgroup
     *
     * @param userIsgroup - 是否为用户组
     */
    public void setUserIsgroup(String userIsgroup)
    {
        this.userIsgroup=new Integer(userIsgroup);
    }

    /**
     * 域(受保护)
     * 名称: userBelong
     * 描述: 用户所属的用户组标识
     */
    protected int userBelong;

    /**
     * 访问器
     * 目标: userBelong
     *
     * @return int - 用户所属的用户组标识
     */
    public int getUserBelong()
    {
        return userBelong;
    }

    /**
     * 更改器
     * 目标: userBelong
     *
     * @param userBelong - 用户所属的用户组标识
     */
    public void setUserBelong(int userBelong)
    {
        this.userBelong=userBelong;
    }

    /**
     * 更改器
     * 目标: userBelong
     *
     * @param userBelong - 用户所属的用户组标识
     */
    public void setUserBelong(String userBelong)
    {
        this.userBelong=new Integer(userBelong);
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
     * 名称:    isSuperAdmin<br>
     * 描述:    当前用户是否为超级管理员用户<br>
     *
     * @return boolean - 如果为超级管理员用户，返回true
     */
    public boolean isSuperAdmin()
    {
        return (SuperAdminUser.superAdminId.equals(this.userName));
    }
}