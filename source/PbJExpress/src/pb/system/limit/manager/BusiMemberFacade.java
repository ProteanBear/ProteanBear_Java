package pb.system.limit.manager;

import pb.code.MD5Productor;
import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.data.SqlAttribute;
import pb.helper.GeneralHelper;
import pb.system.limit.entity.BusiMember;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据表映射类数据管理类——记录系统平台的会员信息
 *
 * @author ProteanBear
 * @version 1.00 2016-04-11
 */
public class BusiMemberFacade
        extends AbstractEntityManager<BusiMember>
        implements BusiMemberFacadeLocal
{
    /**
     * 构造函数
     *
     * @param connector - 指定数据连接器
     */
    public BusiMemberFacade(Connector connector)
    {
        super(connector,BusiMember.class);
    }

    /**
     * 方法（受保护）
     * 名称:    getPrimaryKeyName
     * 描述:    获取数据表主键字段名称
     *
     * @return String - 获取主键名称
     */
    @Override
    public String getPrimaryKeyName()
    {
        return "custId";
    }

    /**
     * 方法（公共）
     * 名称：   create
     * 描述:    添加一条新的记录到数据库中<br>
     *
     * @param entity - 添加记录的描述对象
     * @return boolean - 添加成功，返回true;
     */
    @Override
    public boolean create(BusiMember entity)
    {
        //判断必须属性-登录名称
        String need=entity.getMemberName();
        if(need==null || "".equals(need.trim()))
        {
            return this.logAndReturnFalse("缺少必要的字段属性-登录用户名");
        }
        //判断必须属性-根据不同类型判断
        int type=entity.getMemberType();
        //邮箱注册
        if(type==0
                && (entity.getMemberEmail()==null || "".equals(entity.getMemberEmail().trim())))
        {
            return this.logAndReturnFalse("缺少必要的字段属性-注册邮箱");
        }
        //手机号注册
        if(type==1
                && (entity.getMemberPhone()==null || "".equals(entity.getMemberPhone().trim())))
        {
            return this.logAndReturnFalse("缺少必要的字段属性-注册手机号");
        }
        //第三方登录
        if(type>1
                && (entity.getMemberExt()==null || "".equals(entity.getMemberExt().trim())))
        {
            return this.logAndReturnFalse("缺少必要的字段属性-第三方标识码");
        }

        //判断必须属性-登录密码
        need=entity.getMemberPassword();
        if(need==null || "".equals(need.trim()))
        {
            return this.logAndReturnFalse("缺少必要的字段属性-登录密码");
        }
        //使用MD5编码对登录密码进行加密
        try
        {
            entity.setMemberPassword(MD5Productor.encodeToString32(need));
        }
        catch(UnsupportedEncodingException|NoSuchAlgorithmException ex)
        {
            return this.logAndReturnFalse(ex.toString());
        }

        //判断重复名称
        Map<String,Object> condition=new HashMap<>();
        condition.put("memberName=?",entity.getMemberName());
        if(this.exist(condition))
        {
            return this.logAndReturnFalse("指定的用户名已经存在，请更换用户名再次注册");
        }

        //判断重复邮箱
        String email=entity.getMemberEmail();
        if(email!=null)
        {
            if(GeneralHelper.isStrEmailAddress(email))
            {
                condition.put("memberEmail=?",email);
                if(this.exist(condition))
                {
                    return this.logAndReturnFalse("指定的邮箱已经存在，请更换邮箱再次注册");
                }
            }
            else
            {
                return this.logAndReturnFalse("邮箱格式错误!");
            }
        }

        //判断重复电话
        String phone=entity.getMemberPhone();
        if(phone!=null)
        {
            if(GeneralHelper.isStrNumeric(phone)
                    &&phone.length()<13)
            {
                condition.clear();
                condition.put("memberPhone=?",phone);
                if(this.exist(condition))
                {
                    return this.logAndReturnFalse("指定的手机号已经存在，请更换手机号再次注册");
                }
            }
            else
            {
                return this.logAndReturnFalse("手机号格式错误!");
            }
        }

        return super.create(entity);
    }

    /**
     * 方法（公共）
     * 名称：   edit
     * 描述:    更新指定记录的相关数据到数据库中<br>
     *
     * @param entity - 数据更新的描述对象
     * @return boolean - 更新成功，返回true;
     */
    @Override
    public boolean edit(BusiMember entity)
    {
        //密码字段处理
        String pass=entity.getMemberPassword();
        if(pass!=null)
        {
            //如果不是32位加密字段，则加密
            if(pass.length()!=32)
            {
                //使用MD5编码对登录密码进行加密
                try
                {
                    entity.setMemberPassword(MD5Productor.encodeToString32(pass));
                }
                catch(UnsupportedEncodingException|NoSuchAlgorithmException ex)
                {
                    return this.logAndReturnFalse(ex.toString());
                }
            }
        }

        return super.edit(entity);
    }

    /**
     * 描述:    处理用户登录<br>
     *
     * @param memberLogin - 会员登录使用的关键字,根据登录类型区分
     * @param password    - 用户登录密码
     * @param memberType  - 会员的登录类型、0-邮箱注册、1-手机注册、2-微信、3-QQ、4-微博
     * @return BusiMember - 登录成功返回会员对象，否则返回Null
     */
    @Override
    public BusiMember login(String memberLogin,String password,int memberType)
    {
        BusiMember result=null;
        boolean success=false;

        //参数判断
        if((memberLogin==null || "".equals(memberLogin)))
        {
            return (BusiMember)this.logAndReturnNull("指定的用户标识为空");
        }

        //设置条件
        SqlAttribute sqlAttribute=new SqlAttribute()
                .setTable("busi_member");
        //第三方登录
        if(memberType>1)
        {
            sqlAttribute.addCondition("member_ext='"+memberLogin+"'")
                    .addCondition("member_type='"+memberType+"'");
        }
        //本地注册登录
        else
        {
            sqlAttribute.addCondition("member_name='"+memberLogin+"'",false)
                    .addCondition("member_email='"+memberLogin+"'",false)
                    .addCondition("member_phone='"+memberLogin+"'",false);
            try
            {
                List<BusiMember> list=this.generateObjectList(this.dataManager.setAttribute(sqlAttribute).select());
                if(list!=null&&list.size()>0)
                {
                    result=list.get(0);
                    //如果结果不为空，对比用户数据的登录密码和指定的登录密码
                    if(result!=null)
                    {
                        success=(password.equals(result.getMemberPassword()));

                        //如果不相等，再对比加密后的密码和用户数据对应的密码
                        if(!success)
                        {
                            try
                            {
                                password=MD5Productor.encodeToString32(password);
                            }
                            catch(UnsupportedEncodingException|NoSuchAlgorithmException ex)
                            {
                                return (BusiMember)this.logAndReturnNull(ex.toString());
                            }

                            success=(password.equals(result.getMemberPassword()));

                            //如果仍然不相等，返回空并记录错误信息
                            if(!success)
                            {
                                return (BusiMember)this.logAndReturnNull("登录名或登录密码错误");
                            }
                        }

                        return result;
                    }
                    else
                    {
                        return (BusiMember)this.logAndReturnNull("登录名或登录密码错误");
                    }
                }
                else
                {
                    return (BusiMember)this.logAndReturnNull("登录名或登录密码错误");
                }
            }
            catch(Exception e)
            {
                return (BusiMember)this.logAndReturnNull("登录名或登录密码错误");
            }
        }

        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getCannotEditParams<br>
     * 描述:    获取当前不可修改的属性<br>
     *
     * @return String - 当前不可更改的属性，多个属性之间用|分隔
     */
    @Override
    public String getCannotEditParams()
    {
        return "memberName|memberEmail|memberPhone|memberExt";
    }
}