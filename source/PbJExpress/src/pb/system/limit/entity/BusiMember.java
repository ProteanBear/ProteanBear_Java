package pb.system.limit.entity;

/**
 * 数据表映射类——记录系统平台的会员信息
 *@author ProteanBear
 *@version 1.00 2016-04-11
 */
public class BusiMember
{
    /**域(受保护)
     * 名称: custId
     * 描述: 自增主键
     */
    protected int custId;

    /**访问器
     * 目标: custId
     * @return int - 自增主键
     */
    public int getCustId(){return custId;}

    /**更改器
     * 目标: custId
     * @param custId - 自增主键
     */
    public void setCustId(int custId)
    {
        this.custId=custId;
    }

    /**更改器
     * 目标: custId
     * @param custId - 自增主键
     */
    public void setCustId(String custId)
    {
        this.custId=new Integer(custId);
    }

    /**域(受保护)
     * 名称: memberName
     * 描述: 记录会员的登录名称
     */
    protected String memberName;

    /**访问器
     * 目标: memberName
     * @return String - 记录会员的登录名称
     */
    public String getMemberName(){return memberName;}

    /**更改器
     * 目标: memberName
     * @param memberName - 记录会员的登录名称
     */
    public void setMemberName(String memberName)
    {
        this.memberName=memberName;
    }

    /**域(受保护)
     * 名称: memberNick
     * 描述: 记录会员的显示昵称
     */
    protected String memberNick;

    /**访问器
     * 目标: memberNick
     * @return String - 记录会员的显示昵称
     */
    public String getMemberNick(){return memberNick;}

    /**更改器
     * 目标: memberNick
     * @param memberNick - 记录会员的显示昵称
     */
    public void setMemberNick(String memberNick)
    {
        this.memberNick=memberNick;
    }

    /**域(受保护)
     * 名称: memberPassword
     * 描述: 记录会员的登录密码，格式为MD5编码
     */
    protected String memberPassword;

    /**访问器
     * 目标: memberPassword
     * @return String - 记录会员的登录密码，格式为MD5编码
     */
    public String getMemberPassword(){return memberPassword;}

    /**更改器
     * 目标: memberPassword
     * @param memberPassword - 记录会员的登录密码，格式为MD5编码
     */
    public void setMemberPassword(String memberPassword)
    {
        this.memberPassword=memberPassword;
    }

    /**域(受保护)
     * 名称: memberEmail
     * 描述: 会员的电子邮箱
     */
    protected String memberEmail;

    /**访问器
     * 目标: memberEmail
     * @return String - 会员的电子邮箱
     */
    public String getMemberEmail(){return memberEmail;}

    /**更改器
     * 目标: memberEmail
     * @param memberEmail - 会员的电子邮箱
     */
    public void setMemberEmail(String memberEmail)
    {
        this.memberEmail=memberEmail;
    }

    /**域(受保护)
     * 名称: memberPhone
     * 描述: 会员的手机号码
     */
    protected String memberPhone;

    /**访问器
     * 目标: memberPhone
     * @return String - 会员的手机号码
     */
    public String getMemberPhone(){return memberPhone;}

    /**更改器
     * 目标: memberPhone
     * @param memberPhone - 会员的手机号码
     */
    public void setMemberPhone(String memberPhone)
    {
        this.memberPhone=memberPhone;
    }

    /**域(受保护)
     * 名称: memberHead
     * 描述: 会员的头像地址
     */
    protected String memberHead;

    /**访问器
     * 目标: memberHead
     * @return String - 会员的头像地址
     */
    public String getMemberHead(){return memberHead;}

    /**更改器
     * 目标: memberHead
     * @param memberHead - 会员的头像地址
     */
    public void setMemberHead(String memberHead)
    {
        this.memberHead=memberHead;
    }

    /**域(受保护)
     * 名称: memberType
     * 描述: 会员的登录类型、0-邮箱注册、1-手机注册、2-微信、3-QQ、4-微博
     */
    protected int memberType;

    /**访问器
     * 目标: memberType
     * @return int - 会员的登录类型、0-邮箱注册、1-手机注册、2-微信、3-QQ、4-微博
     */
    public int getMemberType(){return memberType;}

    /**更改器
     * 目标: memberType
     * @param memberType - 会员的登录类型、0-邮箱注册、1-手机注册、2-微信、3-QQ、4-微博
     */
    public void setMemberType(int memberType)
    {
        this.memberType=memberType;
    }

    /**更改器
     * 目标: memberType
     * @param memberType - 会员的登录类型、0-邮箱注册、1-手机注册、2-微信、3-QQ、4-微博
     */
    public void setMemberType(String memberType)
    {
        this.memberType=new Integer(memberType);
    }

    /**域(受保护)
     * 名称: memberExt
     * 描述: 第三方登录使用登录标识码
     */
    protected String memberExt;

    /**访问器
     * 目标: memberExt
     * @return String - 第三方登录使用登录标识码
     */
    public String getMemberExt(){return memberExt;}

    /**更改器
     * 目标: memberExt
     * @param memberExt - 第三方登录使用登录标识码
     */
    public void setMemberExt(String memberExt)
    {
        this.memberExt=memberExt;
    }

    /**域(受保护)
     * 名称: createTime
     * 描述: 数据创建时间
     */
    protected String createTime;

    /**访问器
     * 目标: createTime
     * @return String - 数据创建时间
     */
    public String getCreateTime(){return createTime;}

    /**更改器
     * 目标: createTime
     * @param createTime - 数据创建时间
     */
    public void setCreateTime(String createTime)
    {
        this.createTime=createTime;
    }

    /**域(受保护)
     * 名称: updateTime
     * 描述: 数据删除标志位
     */
    protected String updateTime;

    /**访问器
     * 目标: updateTime
     * @return String - 数据删除标志位
     */
    public String getUpdateTime(){return updateTime;}

    /**更改器
     * 目标: updateTime
     * @param updateTime - 数据删除标志位
     */
    public void setUpdateTime(String updateTime)
    {
        this.updateTime=updateTime;
    }

    /**域(受保护)
     * 名称: dataRemark
     * 描述: 数据删除标志位
     */
    protected String dataRemark;

    /**访问器
     * 目标: dataRemark
     * @return String - 数据删除标志位
     */
    public String getDataRemark(){return dataRemark;}

    /**更改器
     * 目标: dataRemark
     * @param dataRemark - 数据删除标志位
     */
    public void setDataRemark(String dataRemark)
    {
        this.dataRemark=dataRemark;
    }

    /**域(受保护)
     * 名称: dataDelete
     * 描述: 数据删除标志位
     */
    protected int dataDelete;

    /**访问器
     * 目标: dataDelete
     * @return int - 数据删除标志位
     */
    public int getDataDelete(){return dataDelete;}

    /**更改器
     * 目标: dataDelete
     * @param dataDelete - 数据删除标志位
     */
    public void setDataDelete(int dataDelete)
    {
        this.dataDelete=dataDelete;
    }

    /**更改器
     * 目标: dataDelete
     * @param dataDelete - 数据删除标志位
     */
    public void setDataDelete(String dataDelete)
    {
        this.dataDelete=new Integer(dataDelete);
    }

}