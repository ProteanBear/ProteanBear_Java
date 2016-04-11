package pb.system.limit.manager;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import pb.code.MD5Productor;
import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.data.SqlAttribute;
import pb.system.limit.entity.SuperAdminUser;
import pb.system.limit.entity.SystemArea;
import pb.system.limit.entity.SystemUser;

/**
 * 数据表映射类数据管理类——记录系统管理用户的相关数据信息
 *
 * @author ProteanBear
 * @version 1.00 2014-07-15
 */
public class SystemUserFacade
        extends AbstractEntityManager<SystemUser>
        implements SystemUserFacadeLocal
{
    /**
     * 构造函数<br>
     *
     * @param connector - 指定数据连接器
     */
    public SystemUserFacade(Connector connector)
    {
        super(connector,SystemUser.class);
        this.orderBy=" area_id asc";
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getPrimaryKeyName<br>
     * 描述:    获取数据表主键字段名称<br>
     *
     * @return String - 获取主键名称
     */
    @Override
    public String getPrimaryKeyName()
    {
        return "custId";
    }

    /**
     * 方法（受保护）<br>
     * 名称:    afterRemove<br>
     * 描述:    设置删除后的相关处理<br>
     *
     * @param obj - 主键或删除对象
     */
    @Override
    protected void afterRemove(Object obj)
    {
        //指定主键或对象为空
        if(obj==null) this.logAndReturnFalse("指定主键或对象为空");

        //获取管理人员的主键
        int userId=0;
        if(obj.getClass().isAssignableFrom(SystemUser.class))
        {
            userId=((SystemUser)obj).getCustId();
        }
        else
        {
            userId=(int)obj;
        }

        //创建多对多企业类型插件管理器对象
        SystemUserRoleFacadeLocal manager=
                new SystemUserRoleFacade(this.connector);

        //清除相关企业类型多对多数据
        manager.removeByUserId(userId);
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
    public boolean create(SystemUser entity)
    {
        //判断必须属性-用户标识
        String need=entity.getUserName();
        if(need==null || "".equals(need.trim()))
        {
            return this.logAndReturnFalse("缺少必要的字段属性-用户标识");
        }
        //判断超级管理员标识
        Map<String,Object> condition=new HashMap<>();
        condition.put("userName=?",need);
        if(SuperAdminUser.superAdminId.equals(need) || this.exist(condition))
        {
            return this.logAndReturnFalse("指定的用户名已经存在，请更换用户名再次添加");
        }

        //判断必须属性-登录密码
        need=entity.getUserPassword();
        if(need==null || "".equals(need.trim()))
        {
            return this.logAndReturnFalse("缺少必要的字段属性-登录密码");
        }
        //使用MD5编码对登录密码进行加密
        try
        {
            entity.setUserPassword(MD5Productor.encodeToString32(need));
        }
        catch(UnsupportedEncodingException|NoSuchAlgorithmException ex)
        {
            return this.logAndReturnFalse(ex.toString());
        }

        //判断必须属性-行政区域标识
        need=entity.getAreaId();
        if(need==null || "".equals(need.trim()))
        {
            return this.logAndReturnFalse("缺少必要的字段属性-行政区域标识");
        }
        //判断指定的行政区域是否存在
        SystemAreaFacadeLocal areaManager=new SystemAreaFacade(this.connector);
        SystemArea area=areaManager.find(need);
        if(area==null)
        {
            return this.logAndReturnFalse("指定的行政区域不存在");
        }
        else
        {
            //设置行政区域名称
            entity.setAreaName(area.getAreaName());
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
    public boolean edit(SystemUser entity)
    {
        //密码字段处理
        String pass=entity.getUserPassword();
        if(pass!=null)
        {
            //如果不是32位加密字段，则加密
            if(pass.length()!=32)
            {
                //使用MD5编码对登录密码进行加密
                try
                {
                    entity.setUserPassword(MD5Productor.encodeToString32(pass));
                }
                catch(UnsupportedEncodingException|NoSuchAlgorithmException ex)
                {
                    return this.logAndReturnFalse(ex.toString());
                }
            }
        }
        //判断必须属性-行政区域标识
        String need=entity.getAreaId();
        if(need==null || "".equals(need.trim()))
        {
            return this.logAndReturnFalse("缺少必要的字段属性-行政区域标识");
        }
        //判断指定的行政区域是否存在
        SystemAreaFacadeLocal areaManager=new SystemAreaFacade(this.connector);
        SystemArea area=areaManager.find(need);
        if(area==null)
        {
            return this.logAndReturnFalse("指定的行政区域不存在");
        }
        else
        {
            //设置行政区域名称
            entity.setAreaName(area.getAreaName());
        }

        return super.edit(entity);
    }

    /**
     * 方法（公共）<br>
     * 名称:    login<br>
     * 描述:    处理用户登录<br>
     *
     * @param userId   - 管理人员标识
     * @param password - 用户登录密码
     * @return SystemUser - 登录成功返回管理人员对象，否则返回Null
     */
    @Override
    public SystemUser login(String userId,String password)
    {
        SystemUser result=null;
        boolean success=false;

        //参数判断
        if(userId==null || "".equals(userId.trim()))
        {
            return (SystemUser)this.logAndReturnNull("指定的用户标识为空");
        }

        //超级管理员登录处理
        result=this.superAdminLogin(userId,password);
        if(result!=null) return result;

        //读取指定的用户数据
        result=this.find(userId);

        //如果结果不为空，对比用户数据的登录密码和指定的登录密码
        if(result!=null)
        {
            success=(password.equals(result.getUserPassword()));

            //如果不相等，再对比加密后的密码和用户数据对应的密码
            if(!success)
            {
                try
                {
                    password=MD5Productor.encodeToString32(password);
                }
                catch(UnsupportedEncodingException|NoSuchAlgorithmException ex)
                {
                    return (SystemUser)this.logAndReturnNull(ex.toString());
                }

                success=(password.equals(result.getUserPassword()));

                //如果仍然不相等，返回空并记录错误信息
                if(!success)
                {
                    return (SystemUser)this.logAndReturnNull("登录名或登录密码错误");
                }
            }

            return result;
        }
        else
        {
            return (SystemUser)this.logAndReturnNull("登录名或登录密码错误");
        }
    }

    /**
     * 方法（私有）<br>
     * 名称:    superAdminLogin<br>
     * 描述:    处理超级管理员用户登录<br>
     *
     * @param userId   - 管理人员标识
     * @param password - 用户登录密码
     * @return SystemUser - 登录成功返回管理人员对象，否则返回Null
     */
    private SystemUser superAdminLogin(String userId,String password)
    {
        SystemUser result=null;
        boolean success=false;

        if(SuperAdminUser.superAdminId.equals(userId))
        {
            //对比未加密的密码
            success=(SuperAdminUser.superAdminPass.equals(password));

            //对比加密后的密码
            if(!success)
            {
                try
                {
                    password=MD5Productor.encodeToString32(password);
                }
                catch(UnsupportedEncodingException|NoSuchAlgorithmException ex)
                {
                    return (SystemUser)this.logAndReturnNull(ex.toString());
                }

                success=(SuperAdminUser.superAdminPass.equals(password));
            }

            //如果仍然不相等，返回空并记录错误信息
            if(!success)
            {
                return (SystemUser)this.logAndReturnNull("登录密码错误");
            }
            else
            {
                result=new SystemUser();
                result.setUserName(SuperAdminUser.superAdminId);
                result.setUserNick(SuperAdminUser.superAdminName);
                result.setUserPassword(password);
                result.setAreaId(SuperAdminUser.superAdminAreaId);
                result.setAreaName(SuperAdminUser.superAdminAreaName);
            }
        }

        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    removeByAreaId<br>
     * 描述:    清除指定的行政区域对应的多对多企业类型数据<br>
     *
     * @param areaId - 行政区域标识
     * @return boolean - 如果清除成功，返回true
     */
    @Override
    public boolean removeByAreaId(String areaId)
    {
        boolean success=false;
        try
        {
            //构造删除语句
            SqlAttribute sqlAttr=new SqlAttribute()
                    .setTable("SYSTEM_USER")
                    .addCondition("area_id=?");

            //删除数据
            success=this.dataManager.setAttribute(sqlAttr).delete(areaId);
        }
        catch(Exception ex)
        {
            success=this.logAndReturnFalse(ex.toString());
        }
        return success;
    }
}