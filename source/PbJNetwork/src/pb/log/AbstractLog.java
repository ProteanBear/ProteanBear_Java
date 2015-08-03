package pb.log;

/**
 * 系统日志类。<br/>
 * 记录并显示系统相关信息到信息或文件中。<br/>
 * 1.1 - 更新日志系统，增加系统日志对象，可储存日志到文件中。<br/>
 *
 * @author proteanBear(马强)
 * @version 1.00 2012/01/31
 */
public abstract class AbstractLog
{
    /**
     * 域(受保护)<br/>
     * 名称:    logger<br/>
     * 描述:    记录日志处理对象<br/>
     */
    protected SystemLog logger;

    /**
     * 构造函数<br/>
     *
     * @param logName - 日志名称
     */
    public AbstractLog(String logName)
    {
        this.logger=new SystemLog(logName);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    error<br/>
     * 描述:    记录并显示线程中的错误信息<br/>
     *
     * @param err - 错误信息
     */
    public void error(String err)
    {
        this.logger.error(err);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    debug<br/>
     * 描述:    记录并显示线程中的调试信息<br/>
     *
     * @param text - 调试信息
     */
    public void debug(String text)
    {
        this.logger.debug(text);
    }

    /**
     * 方法（公共）<br/>
     * 名称:    infor<br/>
     * 描述:    记录并显示线程中的其他信息<br/>
     *
     * @param text - 信息
     */
    public void infor(String text)
    {
        this.logger.infor(text);
    }

    /**
     * 方法（私有）<br/>
     * 名称:    setIsDebug<br/>
     * 描述:    设置是否显示调试信息<br/>
     *
     * @param isDebug - 是否显示调试信息
     */
    public final void setIsDebug(boolean isDebug)
    {
        this.logger.setIsDebug(isDebug);
    }
}
