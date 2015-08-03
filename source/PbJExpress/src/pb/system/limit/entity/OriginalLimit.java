package pb.system.limit.entity;

/**
 * 数据表映射类——基础码表，用于在创建权限时生成基础权限（当INIT属性为true时），
 * 以及在创建编辑权限时选择权限类型。 此处为添加权限
 *
 * @author ProteanBear
 * @version 1.00 2014-07-14
 */
public class OriginalLimit
{
    /**
     * 域(受保护)
     * 名称: limitId
     * 描述: 默认主键，指定当前权限值
     */
    protected int limitId;

    /**
     * 访问器
     * 目标: limitId
     *
     * @return int - 默认主键，指定当前权限值
     */
    public int getLimitId()
    {
        return limitId;
    }

    /**
     * 更改器
     * 目标: limitId
     *
     * @param limitId - 默认主键，指定当前权限值
     */
    public void setLimitId(int limitId)
    {
        this.limitId=limitId;
    }

    /**
     * 更改器
     * 目标: limitId
     *
     * @param limitId - 默认主键，指定当前权限值
     */
    public void setLimitId(String limitId)
    {
        this.limitId=new Integer(limitId);
    }

    /**
     * 域(受保护)
     * 名称: limitName
     * 描述: 权限的显示名称
     */
    protected String limitName;

    /**
     * 访问器
     * 目标: limitName
     *
     * @return String - 权限的显示名称
     */
    public String getLimitName()
    {
        return limitName;
    }

    /**
     * 更改器
     * 目标: limitName
     *
     * @param limitName - 权限的显示名称
     */
    public void setLimitName(String limitName)
    {
        this.limitName=limitName;
    }

    /**
     * 域(受保护)
     * 名称: limitInit
     * 描述: 设置在初始化插件时是否创建当前权限，0-不创建、1-创建
     */
    protected int limitInit;

    /**
     * 访问器
     * 目标: limitInit
     *
     * @return int - 设置在初始化插件时是否创建当前权限，0-不创建、1-创建
     */
    public int getLimitInit()
    {
        return limitInit;
    }

    /**
     * 更改器
     * 目标: limitInit
     *
     * @param limitInit - 设置在初始化插件时是否创建当前权限，0-不创建、1-创建
     */
    public void setLimitInit(int limitInit)
    {
        this.limitInit=limitInit;
    }

    /**
     * 更改器
     * 目标: limitInit
     *
     * @param limitInit - 设置在初始化插件时是否创建当前权限，0-不创建、1-创建
     */
    public void setLimitInit(String limitInit)
    {
        this.limitInit=new Integer(limitInit);
    }

}