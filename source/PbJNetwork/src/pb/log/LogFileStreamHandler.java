package pb.log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;
import java.util.logging.LogManager;

/**
 * 自定义日志文件处理器。<br>
 * 日期及文件大小存储，并自动清除过期日志文件
 * @author      proteanBear(马强)
 * @version     1.00 2011/12/31
 */
public class LogFileStreamHandler extends StreamHandler
{
    /**静态常量(私有)<br/>
     * 名称:    undifine<br/>
     * 描述:    <br/>
     */private static final String undifine = "xlcallcenter";
    
    /**域(私有)<br/>
     * 名称:    append<br/>
     * 描述:    是否将日志写入已存在的日志文件中<br/>
     */private boolean append;
     
    /**域(私有)<br/>
     * 名称:    dateinterval<br/>
     * 描述:    保存几天之内的日志文件<br/>
     *          时间间隔小于等于0时表明不删除历史记录<br/>
     */private int dateinterval;
     
    /**域(私有)<br/>
     * 名称:    files<br/>
     * 描述:    保存存在的日志文件 <br/>
     */private Map<String, TreeSet<LogFile>> files;
     
    /**域(私有)<br/>
     * 名称:    limit<br/>
     * 描述:    每个日志希望写入的最大字节数，如果日志达到最大字节数则当天日期的一个新的编号的日志文件将被创建，最新的日志记录在最大编号的文件中。<br/>
     *          文件大小为小于等于0时表明不限制日志文件大小。<br/>
     */private int limit;
     
    /**域(私有)<br/>
     * 名称:    output<br/>
     * 描述:    输出流<br/>
     */private LogFileStream output;
     
    /**域(私有)<br/>
     * 名称:    pattern<br/>
     * 描述:    文件路径， 可以是个目录或希望的日志名称，如果是个目录则日志为""<br/>
     */private String pattern;
     
    /**域(私有)<br/>
     * 名称:    splitDateIndexChar<br/>
     * 描述:    记录日期的分隔符<br/>
     */private char splitDateIndexChar='#';
     
    /**构造函数<br/>
     * @param
     */public LogFileStreamHandler() throws Exception 
     {  
         configure();  
         openWriteFiles();  
     }
     
    /**构造函数<br/>
     * @param fileUrl - 文件路径， 可以是个目录或希望的日志名称，如果是个目录则日志为"callcenter_zd" <br/>
     *                   指定日志名称时不需要包括日期，程序会自动生成日志文件的生成日期及相应的编号 <br/>
     * @param limit - 每个日志希望写入的最大字节数，如果日志达到最大字节数则当天日期的一个新的编 <br/>
     *                 号的日志文件将被创建，最新的日志记录在最大编号的文件中 <br/>
     * @param dateinterval - 保存几天之内的日志文件 <br/>
     * @param append - 是否将日志写入已存在的日志文件中 <br/>
     * @throws java.lang.Exception 
     */public LogFileStreamHandler
             (String fileUrl, int limit, int dateinterval,  
              boolean append) throws Exception 
     {
         super();
         this.pattern = fileUrl;
         this.limit = limit;
         this.dateinterval = dateinterval;
         this.append = append;
         openWriteFiles();
     }
     
    /**方法（私有）<br/>
     * 名称:    deleteExpiredLog<br/>
     * 描述:    检查当前日志时间,删除过期日志 <br/>
     */private void deleteExpiredLog() 
     {  
         try
         {
             // 今天作为基准  
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
             String today = sdf.format(new Date().getTime());
             // 删除过期日志  
             for (String keyDate : files.keySet())
             {
                 if ((sdf.parse(today).getTime() - sdf.parse(keyDate).getTime())
                     / (86400 * 1000) > dateinterval)
                 {
                     TreeSet<LogFile> traceDateFiles = files.get(keyDate);
                     for (File deletingFile : traceDateFiles)
                     {
                         // if(deletingFile.exists()) {  
                         deletingFile.delete();
                         // }  
                     }
                     files.remove(today);
                 }
             }
         }
         catch (Exception ex)
         {
             System.out.println(ex.toString());
         }
     }
     
    /**方法（私有）<br/>
     * 名称:    configure<br/>
     * 描述:    来自FileHandler，用于初始化属性设置<br/>
     */private void configure() 
     {  
         LogManager manager = pb.log.LogManager.getLogManager();
         String cname = getClass().getName();

         // 获得pattern  
         pattern = manager.getProperty(cname + ".pattern");
         if (pattern == null)
         {
             pattern = "./log/pbsyslog ";
         }

         // 获得limit  
         String limitVal = manager.getProperty(cname + ".limit");
         if (limitVal == null)
         {
             limit = 1048576 * 5;
         }
         else
         {
             try
             {
                 limit = Integer.parseInt(limitVal.trim());
                 // if (limit < 0) {  
                 // limit = 1048576 * 5;  
                 // }  
             }
             catch (Exception ex)
             {
                 limit = 1048576 * 5;
             }
         }

         // 获得formatter  
         String formatVal = manager.getProperty(cname + ".formatter");
         if (formatVal == null)
         {
             setFormatter(new LogFormatter());
         }
         else
         {
             try
             {
                 Class clz = ClassLoader.getSystemClassLoader().loadClass(
                         formatVal);
                 setFormatter((Formatter) clz.newInstance());
             }
             catch (Exception ex)
             {
                 // We got one of a variety of exceptions in creating the  
                 // class or creating an instance.  
                 // Drop through.  
             }
         }

         // 获得append  
         String appendVal = manager.getProperty(cname + ".append");
         if (appendVal == null)
         {
             append = false;
         }
         else
         {
             if (appendVal.equalsIgnoreCase("true") || appendVal.equals("1"))
             {
                 append = true;
             }
             else if (appendVal.equalsIgnoreCase("false")
                      || appendVal.equals("0"))
             {
                 append = false;
             }
         }

         // 获得logLevel
         String levelVal = manager.getProperty(cname + ".logLevel");
         if (levelVal == null)
         {
             setLevel(Level.ALL);
         }
         else
         {
             try
             {
                 setLevel(Level.parse(levelVal.trim()));
             }
             catch (Exception ex)
             {
                 setLevel(Level.ALL);
             }
         }

         // 获得dateinterval  
         String dateintervalVal = manager.getProperty(cname + ".dateinterval");
         if (dateintervalVal == null)
         {
             dateinterval = 15;
         }
         else
         {
             try
             {
                 dateinterval = Integer.parseInt(dateintervalVal.trim());
                 if (dateinterval <= 0)
                 {
                     dateinterval = 15;
                 }
             }
             catch (Exception ex)
             {
                 dateinterval = 15;
             }
         }

         // 获得filter  
         String filterVal = manager.getProperty(cname + ".filter");
         if (filterVal == null)
         {
             setFilter(null);
         }
         else
         {
             try
             {
                 Class clz = ClassLoader.getSystemClassLoader().loadClass(
                         filterVal);
                 setFilter((Filter) clz.newInstance());
             }
             catch (Exception ex)
             {
                 // We got one of a variety of exceptions in creating the  
                 // class or creating an instance.  
                 // Drop through.  
             }
         }

         // 获得encoding  
         String encodingVal = manager.getProperty(cname + ".encoding");
         if (encodingVal == null)
         {
             try
             {
                 setEncoding(null);
             }
             catch (Exception ex2)
             {
                 // doing a setEncoding with null should always work.  
                 // assert false;  
             }
         }
         else
         {
             try
             {
                 setEncoding(encodingVal);
             }
             catch (Exception ex)
             {
                 try
                 {
                     setEncoding(null);
                 }
                 catch (Exception ex2)
                 {
                     // doing a setEncoding with null should always work.  
                     // assert false;  
                 }
             }
         } 
     }
     
    /**方法（私有）<br/>
     * 名称:    openLastFile<br/>
     * 描述:    将离现在最近的文件作为写入文件的文件 例如 xunleidemo_2008-02-19#30.log<br/>
     *          xunleidemo表示自定义的日志文件名，2008-02-19表示日志文件的生成日期，30 表示此日期的第30个日志文件<br/>
     * @param append - 是否将日志写入已存在的日志文件中
     */private void openLastFile(boolean append) 
     {  
         try
         {
             super.close();
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
             String today = sdf.format(new Date().getTime());

             // 如果没有包含当天的日期，则添加当天日期的日志文件  
             boolean isFirstLogToday = false;
             if (!files.containsKey(today))
             {
                 String logIndex = today + splitDateIndexChar + 1;
                 TreeSet<LogFile> todayFiles = new TreeSet<LogFile>();
                 todayFiles.add(getNewFile(logIndex));
                 files.put(today, todayFiles);
                 isFirstLogToday = true;
             }

             // 获得今天最大的日志文件编号  
             LogFile todayLastFile = files.get(today).last();
             int maxLogCount = todayLastFile.getSid();
             String logIndex = today + splitDateIndexChar
                               + (maxLogCount + (isFirstLogToday ? 0 : (append ? 0 : 1)));
             LogFile wantWriteFile = getNewFile(logIndex);
             files.get(today).add(wantWriteFile);
             openFile(wantWriteFile, append);
         }
         catch (Exception ex)
         {
             System.out.println(ex.toString());
         } 
     }
     
    /**方法（私有）<br/>
     * 名称:    getNewFile<br/>
     * 描述:    根据logIndex建立File<br/>
     * @param logIndex - 包含今天日期及编号,如2008-09-11#1
     * @return LogFile - 创建的文件对象
     */private LogFile getNewFile(String logIndex) 
     {  
         File file = new File(pattern);
         LogFile wantWriteFile = null;
         StringBuilder filePath = new StringBuilder(pattern);
         if (file.isDirectory())
         {
             filePath.append(File.separator);
             filePath.append(undifine);
         }
         filePath.append('_');
         filePath.append(logIndex);
         filePath.append(".log");
         wantWriteFile = new LogFile(filePath.toString());
         return wantWriteFile;
    }
     
    /**方法（私有）<br/>
     * 名称:    getRecodedLog<br/>
     * 描述:    读取已经记录的日志的时间信息<br/>
     * @return Map<String, TreeSet<LogFile>> - 日志记录
     */private Map<String, TreeSet<LogFile>> getRecodedLog() 
     {  
         Map<String, TreeSet<LogFile>> filesMap = new HashMap<String, TreeSet<LogFile>>();
         try
         {
             // 建立相关目录  
             File file = new File(pattern);
             File fileDir = null;
             if (pattern.endsWith("/") || pattern.endsWith("\\"))
             {
                 // 是个目录  
                 fileDir = file;
                 if (!file.exists())
                 {
                     file.mkdirs();
                 }
             }
             else
             {
                 // 带了前缀  
                 File parentFile = new File(file.getParent());
                 fileDir = parentFile;
                 // 父目录不存在则新建目录  
                 if (!parentFile.exists())
                 {
                     parentFile.mkdirs();
                 }
             }

             // 加入到filesMap中  
             for (File contentFile : fileDir.listFiles())
             {
                 if (contentFile.isFile())
                 {
                     LogFile newXLLogFile = new LogFile(contentFile.getAbsolutePath());
                     TreeSet<LogFile> fileListToDate = filesMap.get(newXLLogFile.getDateString());
                     if (fileListToDate == null)
                     {
                         fileListToDate = new TreeSet<LogFile>();
                     }
                     fileListToDate.add(newXLLogFile);
                     filesMap.put(newXLLogFile.getDateString(), fileListToDate);
                 }
             }

             files = filesMap;
             return filesMap;
         }
         catch (Exception ex)
         {
             System.out.println(ex.toString());
         }
         return null;
    }
     
    /**方法（私有）<br/>
     * 名称:    openFile<br/>
     * 描述:    打开需要写入的文件<br/>
     * @param file - 需要打开的文件
     * @param append - 是否将内容添加到文件末尾
     * @return
     */private void openFile(File file, boolean append) throws Exception 
     {  
         int len = 0;
         if (append)
         {
             len = (int) file.length();
         }
         FileOutputStream fout = new FileOutputStream(file.toString(), append);
         BufferedOutputStream bout = new BufferedOutputStream(fout);
         output = new LogFileStream(bout, len);
         setOutputStream(output);
     }  
     
    /**方法（私有）<br/>
     * 名称:    openWriteFiles<br/>
     * 描述:    获得将要写入的文件<br/>
     */private synchronized void openWriteFiles() throws Exception 
     {  
         if (!getLevel().equals(Level.OFF))
         {
             getRecodedLog();
             deleteExpiredLog();
             openLastFile(append);
         }
     } 
     
    /**方法（公共、重载）<br/>
     * 名称:    publish<br/>
     * 描述:    发布日志信息<br/>
     * @param record - 日志信息
     */@Override public synchronized void publish(LogRecord record) 
     {  
         super.publish(record);
         super.flush();
         if (getLevel().equals(Level.OFF))          return;
         if (limit > 0 && output.written >= limit)  openLastFile(false);
     }
    
    /*-----------------------------开始：重载输出流类---------------------------*/
    
    /**
     * 日志输出流类。<br/>
     * 抄自FileHandler的实现，用于跟踪写入文件的字节数 这样以便提高效率。<br/>
     * @author      proteanBear(马强)
     * @version     1.00 2011/12/31
     */private class LogFileStream extends OutputStream
     {
         /**域()<br/>
          * 名称:    out<br/>
          * 描述:    记录输出流对象<br/>
          */private OutputStream out;
          
         /**域()<br/>
          * 名称:    written<br/>
          * 描述:    记录当前写入字节数<br/>
          */private int written;

         /**构造函数<br/>      
          * @param out - 输出流
          * @param written - 写入的字节数
          */LogFileStream(OutputStream out, int written)
         {
             this.out = out;
             this.written = written;
         }

        /**方法（公共、重载）<br/>
         * 名称:    close<br/>
         * 描述:    关闭输出流<br/>
         * @throws IOException
         */@Override public void close() throws IOException{out.close();}

        /**方法（公共、重载）<br/>
         * 名称:    flush<br/>
         * 描述:    刷新输出流<br/>
         * @throws IOException
         */@Override public void flush() throws IOException{out.flush();}

        /**方法（公共、重载）<br/>
         * 名称:    write<br/>
         * 描述:    将字节数组写入输出流，并记录写入的字节数<br/>
         * @throws IOException
         */@Override public void write(byte buff[]) throws IOException
         {
             out.write(buff);
             written += buff.length;
         }

        /**方法（公共、重载）<br/>
         * 名称:    write<br/>
         * 描述:    将字节数组写入输出流，并记录写入的字节数<br/>
         * @throws IOException
         */@Override public void write(byte buff[], int off, int len) throws IOException
         {
             out.write(buff, off, len);
             written += len;
         }

        /**方法（公共、重载）<br/>
         * 名称:    write<br/>
         * 描述:    将数字写入输出流，并记录写入的字节数<br/>
         * @throws IOException
         */@Override public void write(int b) throws IOException
         {
             out.write(b);
             written++;
         }
    }
    
    /*-----------------------------结束：重载输出流类---------------------------*/
     
    /*-----------------------------开始：日志文件类-----------------------------*/
     
    /**
     * 日志文件类。<br/>
     * 继承File类，增加时间标识文件名处理。<br/>
     * @author      proteanBear(马强)
     * @version     1.00 2011/12/31
     */private class LogFile extends File
     {
         private static final long serialVersionUID = 952141123094287978L;
        
        /**域(私有)<br/>
         * 名称:    date<br/>
         * 描述:    日期对象<br/>
         */private Date date;
        
        /**域(私有)<br/>
         * 名称:    dateString<br/>
         * 描述:    日期文本<br/>
         */private String dateString;
        
        /**域(私有)<br/>
         * 名称:    sid<br/>
         * 描述:    记录当前文件的索引标识<br/>
         */private int sid;
         
        /**域(私有)<br/>
         * 名称:    sdf<br/>
         * 描述:    记录使用的日期格式<br/>
         */private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        /**访问器<br/>
         * 目标：   sid<br/>
         * @return int - 
         */public int getSid(){return this.sid;}

        /**更改器<br/>
         * 目标：   sid<br/>
         * @param sid - 
         */public void setSid(int sid){this.sid = sid;}

        /**访问器<br/>
         * 目标：   date<br/>
         * @return int - 日期对象
         */public Date getDate(){return this.date;}

        /**更改器<br/>
         * 目标：   date<br/>
         * @param date - 日期对象
         */public void setDate(Date date){this.date = date;}

        /**访问器<br/>
         * 目标：   dateString<br/>
         * @return int - 日期文本
         */public String getDateString(){return this.dateString;}

        /**更改器<br/>
         * 目标：   dateString<br/>
         * @param dateString - 日期文本
         */public void setDateString(String dateString){this.dateString = dateString;}
         
        /**构造函数<br/>
         * @param pathname - 路径名称
         */public LogFile(String pathname)
         {
             super(pathname);
             try
             {
                 int dot = pathname.lastIndexOf('.');
                 int split = pathname.lastIndexOf(splitDateIndexChar);
                 int underline = pathname.lastIndexOf('_');
                 dateString = pathname.substring(underline + 1, split);
                 String numStr = pathname.substring(split + 1, dot);
                 date = sdf.parse(dateString);
                 sid = Integer.valueOf(numStr);
             }
             catch (Exception e)
             {
                 System.out.println(e.toString());
             }
         }

        /**方法（公共、重载）<br/>
         * 名称:    compareTo<br/>
         * 描述:    比较两个文件，并返回差值<br/>
         * @param another - 指定的另一个文件
         * @return int - 文件索引或时间差值
         */@Override public int compareTo(File another)
         {
             LogFile ano = (LogFile) another;
             int dateComResult = date.compareTo(ano.getDate());
             if (dateComResult == 0)
             {
                 return sid - ano.getSid();
             }
             return dateComResult;
         }
     }
     
    /*-----------------------------开始：日志文件类-----------------------------*/
}
