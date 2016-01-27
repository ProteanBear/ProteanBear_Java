package pb.system.limit.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pb.data.AbstractEntityManager;
import pb.data.Connector;
import pb.system.limit.entity.BusiSection;

/**
 * 数据表映射类数据管理类——记录当前系统业务栏目划分的数据信息。
 *
 * @author ProteanBear
 * @version 1.00 2014-07-31
 */
public class BusiSectionFacade
        extends AbstractEntityManager<BusiSection>
        implements BusiSectionFacadeLocal
{
    /*-----------------------------开始：静态内容------------------------------*/

    /**
     * 静态常量(私有)<br>
     * 名称:    AREA_DEPTH<br>
     * 描述:    行政区域的深度（允许的区域层数）<br>
     */
    public static final int AREA_DEPTH=4;

    /**
     * 静态常量(私有)<br>
     * 名称:    AREA_WIDTH<br>
     * 描述:    行政区域的宽度（每一层使用的字符数）<br>
     */
    public static final int AREA_WIDTH=4;
     
    /*-----------------------------结束：静态内容------------------------------*/

    /**
     * 构造函数
     *
     * @param connector - 指定数据连接器
     */
    public BusiSectionFacade(Connector connector)
    {
        super(connector,BusiSection.class);
        this.orderBy="section_app asc,section_code asc";
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
     * 方法（受保护）<br>
     * 名称:    getCodeGenerator<br>
     * 描述:    返回编码的生成器<br>
     *
     * @param upCode  - 指定的顶层区域标识
     * @param appCode - 应用编码
     * @return String - 主键生成值
     */
    protected String getCodeGenerator(String upCode,String appCode)
    {
        String result="";

        //参数不为空
        if(upCode!=null && !"".equals(upCode))
        {
            //判断上级区域的深度是否已经超过了允许值
            if(upCode.trim().length()>=AREA_DEPTH*AREA_WIDTH)
            {
                return (String)this.logAndReturnNull("超过栏目级数限制（"+AREA_DEPTH+"层）");
            }
            //判断指定的上级区域数据是否存在
            Map<String,Object> condition=new HashMap<>();
            condition.put("sectionCode=?",upCode);
            if(!this.exist(condition))
            {
                return (String)this.logAndReturnNull("指定的上级栏目不存在");
            }
        }
        else
        {
            upCode="";
        }

        //读取指定区域的所有下级区域
        List<BusiSection> children=this.childrenNextDepth(upCode,appCode);

        //判断查询结果
        if(children==null)
        {
            return (String)this.logAndReturnNull("下级栏目数据读取错误");
        }

        //遍历查询结果，搜索空位
        int num=0;
        if(children.isEmpty() && "".equals(upCode))
        {
            result="0000";
        }
        else
        {
            String areaId;
            for(int i=0;i<children.size();i++,num++)
            {
                //获取当前区域标识
                areaId=children.get(i).getSectionCode();
                if(areaId==null) continue;
                areaId=areaId.trim();

                //生成新标识
                result=upCode+String.format("%1$04d",num);

                //判断是否是顺序的，如果不是顺序的，表示有空位，生成新标识
                if(!result.equals(areaId)) break;
            }
            result=upCode+String.format("%1$04d",num);
        }

        //判断是否超过宽度限制
        String maxStr="";
        for(int i=0;i<AREA_WIDTH;i++) maxStr+="9";
        int max=Integer.parseInt(maxStr);
        if(num>=max) result=(String)this.logAndReturnNull("级别栏目数已满");

        return result;
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getFindOrderBy<br>
     * 描述:    获取查询使用的排序方法<br>
     *
     * @return String - 排序方法
     */
    @Override
    protected String getFindOrderBy()
    {
        return "section_app asc,section_code asc";
    }

    /**
     * 方法（公共）<br>
     * 名称:    childrenNextDepth<br>
     * 描述:    获取指定栏目的下一级所有数据<br>
     *
     * @param upCode  - 指定的顶层数据标识
     * @param appCode - 应用编码
     * @return List - 下一级数据列表
     */
    @Override
    public List<BusiSection> childrenNextDepth(String upCode,String appCode)
    {
        //获取系统使用级别宽度
        String like="";
        for(int i=0;i<AREA_WIDTH;i++) like+="_";

        //构造查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("section_code like ?",upCode+like);
        condition.put("section_app=?",appCode);

        //查询数据
        return this.find(condition);
    }

    /**
     * 方法（公共）
     * 名称：   create
     * 描述:    重载并标识为过期方法，添加对应的数据请使用特定类型添加方法<br>
     *
     * @param entity - 添加记录的描述对象
     * @return boolean - 添加成功，返回true;
     */
    @Override
    @Deprecated
    public boolean create(BusiSection entity)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 方法（公共、重载）
     * 名称：   create
     * 描述:    添加一条新的记录到数据库中<br>
     *
     * @param upCode - 指定的顶层栏目标识
     * @param entity - 添加记录的描述对象
     * @return boolean - 添加成功，返回true;
     */
    @Override
    public boolean create(String upCode,BusiSection entity)
    {
        boolean success=false;

        //所属应用为空，不能添加
        final String app=entity.getSectionApp();
        if(app==null || "".equals(app.trim()))
        {
            return this.logAndReturnFalse("未指定添加栏目所属的应用");
        }

        //所属应用不存在，不能添加
        SystemApplicationFacadeLocal appManager=new SystemApplicationFacade(this.connector);
        if(!appManager.exist(
                new HashMap<String,Object>()
                {{
                        put("appCode=?",app);
                    }}
        ))
        {
            return this.logAndReturnFalse("指定栏目所属的应用不存在");
        }

        //根据指定的上级栏目创建栏目编码
        entity.setSectionCode(this.getCodeGenerator(upCode,app));

        //添加数据
        success=super.create(entity);

        //记录最新生成编号
        this.lastGenerator=entity.getSectionCode();

        return success;
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
        return "custId|sectionCode";
    }

    /**
     * 方法（受保护）<br>
     * 名称:    beforeRemove<br>
     * 描述:    设置删除前的相关处理<br>
     *
     * @param obj - 主键或删除对象
     * @return boolean - 可以删除，返回true
     */
    @Override
    protected boolean beforeRemove(Object obj)
    {
        //指定主键或对象为空
        if(obj==null)
        {
            return this.logAndReturnFalse("指定主键或对象为空");
        }

        //获取指定的栏目
        BusiSection section=obj.getClass().isAssignableFrom(int.class)?
                this.find(obj):((BusiSection)obj);

        //数据不存在
        if(section==null)
        {
            return this.logAndReturnFalse("指定删除的数据不存在");
        }

        //包含子栏目无法删除
        if(this.existChildren(section.getSectionCode(),section.getSectionApp()))
        {
            return this.logAndReturnFalse("包含子栏目无法删除");
        }

        return true;
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

        //获取栏目的主键
        BusiSection plugin=null;
        if(obj.getClass().isAssignableFrom(BusiSection.class))
        {
            plugin=(BusiSection)obj;

            //清除相关数据
            if(plugin!=null)
            {
                //处理全部下级相关数据
            }
        }
    }

    /**
     * 方法（公共）<br>
     * 名称:    childrenAllDownDepth<br>
     * 描述:    获取指定区域的所有低层次栏目<br>
     *
     * @param upCode  - 指定的顶层区域标识
     * @param appCode - 应用编码
     * @return List - 低层次所有区域列表
     */
    @Override
    public List<BusiSection> childrenAllDownDepth(String upCode,String appCode)
    {
        //构造查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("section_code like ?",upCode+"%");
        condition.put("section_app=?",appCode);

        //查询数据
        return this.find(condition);
    }

    /**
     * 方法（公共）<br>
     * 名称:    existChildren<br>
     * 描述:    查看指定的栏目是否有下级栏目<br>
     *
     * @param upCode  - 指定的顶层栏目标识
     * @param appCode - 应用编码
     * @return boolean - 如果有下级栏目，返回true
     */
    @Override
    public boolean existChildren(String upCode,String appCode)
    {
        //获取系统使用级别宽度
        String like="";
        for(int i=0;i<AREA_WIDTH;i++) like+="_";

        //构造查询条件
        Map<String,Object> condition=new HashMap<>();
        condition.put("section_code like ?",upCode+like);
        condition.put("section_app=?",appCode);

        //查询数据
        return this.exist(condition);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getUpAreaId<br>
     * 描述:    获取指定栏目标识的上级栏目标识<br>
     *
     * @param secCode - 指定的顶层区域标识
     * @return String - 返回上级栏目标识
     */
    @Override
    public String getUpAreaId(String secCode)
    {
        if(secCode==null) return (String)this.logAndReturnNull("指定的栏目标识为空");
        if(secCode.length()==AREA_WIDTH) return (String)this.logAndReturnNull("指定的栏目标识已为顶层区域");
        if(secCode.length()%AREA_WIDTH!=0) return (String)this.logAndReturnNull("错误的栏目标识长度");
        return secCode.substring(0,secCode.length()-AREA_WIDTH);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDepth<br>
     * 描述:    获取指定栏目标识对应的级别深度<br>
     *
     * @param secCode - 栏目标识
     * @return int - 深度级别
     */
    @Override
    public int getDepth(String secCode)
    {
        if(secCode==null) return 0;
        return secCode.trim().length()/AREA_WIDTH;
    }

    /**
     * 方法（公共）<br>
     * 名称:    findBySectionCode<br>
     * 描述:    根据指定编码获取栏目数据<br>
     *
     * @param appCode - 指定的应用标识
     * @param code - 指定的数据编码
     * @return List - 下一级数据列表
     */
    @Override
    public BusiSection findBySectionCode(String appCode,String code)
    {
        Map<String,Object> condition=new HashMap<>();
        condition.put("sectionCode=?",code);
        condition.put("sectionApp=?",appCode);

        List<BusiSection> list=this.find(condition);
        return (list!=null && !list.isEmpty())?list.get(0):null;
    }
}