package pb.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * 系统日志载入类。<br/>
 * 用于读取配置文件对当前的日志系统进行设置。<br/>
 * @author      proteanBear(马强)
 * @version     1.00 2011/12/30
 */
public class LogManager
{
    // 初始化LogManager
    static
    {
        // 读取配置文件
        InputStream inputStream = LogManager.class.getResourceAsStream("log.properties");
        java.util.logging.LogManager logManager = java.util.logging.LogManager.getLogManager();
        try
        {
            // 重新初始化日志属性并重新读取日志配置。  
            logManager.readConfiguration(inputStream);
        }
        catch (SecurityException e)
        {
        }
        catch (IOException e)
        {
        }
    }
  
    /**静态方法（公共）<br/>
     * 名称:    getLogger<br/>
     * 描述:    获取日志对象<br/>
     * @param name - 日志名称
     * @return Logger - 日志对象
     */public static Logger getLogger(String name)
     {
         Logger logger = Logger.getLogger(name);
         return logger;
     }
     
    /**静态方法（公共）<br/>
     * 名称:    getLogManager<br/>
     * 描述:    获取当前的日志管理器<br/>
     * @return LogManager - 获取当前的日志管理器对象
     */public static java.util.logging.LogManager getLogManager()
     {
         return java.util.logging.LogManager.getLogManager();
     }
}
