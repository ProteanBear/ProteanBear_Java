package pb.log;

import java.util.logging.Logger;

/**
 * 系统日志类。<br/>
 * 记录并显示系统相关信息到信息或文件中。<br/>
 * 1.00 - 封装Java自带的logger类方法，自定义系统日志类。<br/>
 * @author      proteanBear(马强)
 * @version     1.00 2011/12/30
 */
public class SystemLog
{
    /**域(私有)<br/>
     * 名称:    isDebug<br/>
     * 描述:    是否显示调试信息<br/>
     */protected boolean isDebug;
    
    /**域(私有)<br/>
     * 名称:    logger<br/>
     * 描述:    记录当前的日志对象<br/>
     */private Logger logger;
     
    /**构造函数<br/>        
     * @param name - 日志名称
     */public SystemLog(String name)
     {
         this.logger=LogManager.getLogger(name);
         this.isDebug=false;
     }
     
    /**方法（公共）<br/>
     * 名称:    error<br/>
     * 描述:    记录并显示线程中的错误信息<br/>
     * @param err - 错误信息
     */public void error(String err)
     {
         this.logger.warning(err);
     }
     
    /**方法（公共）<br/>
     * 名称:    debug<br/>
     * 描述:    记录并显示线程中的调试信息<br/>
     * @param text - 调试信息
     */public void debug(String text)
     {
         if(isDebug) this.logger.log(Level.DEBUG, text);
     }
     
    /**方法（公共）<br/>
     * 名称:    infor<br/>
     * 描述:    记录并显示线程中的其他信息<br/>
     * @param text - 信息
     */public void infor(String text)
     {
         this.logger.info(text);
     }

    /**访问器<br/>
     * 目标：   isDebug<br/>
     * @return boolean - 是否显示调试信息
     */public boolean isDebug(){return isDebug;}

    /**更改器<br/>
     * 目标：   isDebug<br/>
     * @param isDebug - 是否显示调试信息
     */public void setIsDebug(boolean isDebug)
     {
         this.isDebug = isDebug;
         if(isDebug)    this.logger.setLevel(Level.DEBUG);
     }
}
