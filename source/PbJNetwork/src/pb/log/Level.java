package pb.log;

/**
 * 子类化java.util.logging.Level类。创建自定义的日志级别。<br/>
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/12/30
 */
public class Level extends java.util.logging.Level
{
    /*-----------------------------开始：自定义日志级别-------------------------*/

    /**
     * 静态常量<br/>
     * 名称:    DEBUG<br/>
     * 描述:    调试信息级别<br/>
     */
    public static final Level DEBUG=new Level("调试",850,"sun.util.logging.resources.logging");
    /*-----------------------------结束：自定义日志级别-------------------------*/

    /**
     * 构造函数<br/>
     */
    protected Level(String name,int value,String resourceBundleName)
    {
        super(name,value,resourceBundleName);
    }
}
