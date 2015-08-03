package pb.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.logging.LogRecord;

/**
 * 日志输出格式化类。<br/>
 * @author      proteanBear(马强)
 * @version     1.00 2011/12/31
 */
public class LogFormatter extends java.util.logging.Formatter
{
    /**域(私有)<br/>
     * 名称:    lineSeparator<br/>
     * 描述:    <br/>
     */private static String lineSeparator = System.getProperty("line.separator");
     
    /**域(私有)<br/>
     * 名称:    startTime<br/>
     * 描述:    <br/>
     *///private static long startTime = System.currentTimeMillis();
     
    /**域(私有)<br/>
     * 名称:    threeDigFmt<br/>
     * 描述:    <br/>
     */private static DecimalFormat threeDigFmt = new DecimalFormat("000");
     
    /**域(私有)<br/>
     * 名称:    twoDigFmt<br/>
     * 描述:    <br/>
     */private static DecimalFormat twoDigFmt = new DecimalFormat("00");

    /**方法（公共）<br/>
     * 名称:    format<br/>
     * 描述:    Format the given LogRecord<br/>
     * @param record - the log record to be formatted
     * @return String - a formatted log record
     */public synchronized String format(LogRecord record)
     {
         StringBuilder sb = new StringBuilder();
         // dat.setTime(record.getMillis());  
         // args[0] = dat;  
         // StringBuffer text = new StringBuffer();  
         // if (formatter == null) {  
         // formatter = new MessageFormat(format);  
         // }  
         // formatter.format(args, text, null);  
         // sb.append(text);  
         // current time  
         // Calendar cal = Calendar.getInstance();  
         // int millis = cal.get(Calendar.MILLISECOND);  
         // sb.append('.').append(threeDigFmt.format(millis)).append(' ');  

         // current time  
         Calendar cal = Calendar.getInstance();
         int hour = cal.get(Calendar.HOUR_OF_DAY);
         int minutes = cal.get(Calendar.MINUTE);
         int seconds = cal.get(Calendar.SECOND);
         int millis = cal.get(Calendar.MILLISECOND);
         sb.append(twoDigFmt.format(hour)).append(':');
         sb.append(twoDigFmt.format(minutes)).append(':');
         sb.append(twoDigFmt.format(seconds)).append('.');
         sb.append(threeDigFmt.format(millis)).append(' ');

         // log level  
         sb.append(record.getLevel().getLocalizedName());
         sb.append(": ");

         // caller method  
         int lineNumber = inferCaller(record);
         String loggerName = record.getLoggerName();

         if (loggerName == null)
         {
             loggerName = record.getSourceClassName();
         }

         if (loggerName.startsWith("com.xunlei.callcenter."))
         {
             sb.append(loggerName.substring("com.xunlei.callcenter.".length()));
         }
         else
         {
             sb.append(record.getLoggerName());
         }

         if (record.getSourceMethodName() != null)
         {
             sb.append('.');
             sb.append(record.getSourceMethodName());

             // include the line number if we have it.  
             if (lineNumber != -1)
             {
                 sb.append("().").append(Integer.toString(lineNumber));
             }
             else
             {
                 sb.append("()");
             }
         }
         sb.append(' ');
         sb.append(record.getMessage());
         sb.append(lineSeparator);
         if (record.getThrown() != null)
         {
             try
             {
                 StringWriter sw = new StringWriter();
                 PrintWriter pw = new PrintWriter(sw);
                 record.getThrown().printStackTrace(pw);
                 pw.close();
                 sb.append(sw.toString());
             }
             catch (Exception ex)
             {
             }
         }
         return sb.toString();
     }

    /** 
     * Try to extract the name of the class and method that called the current 
     * log statement. 
     *  
     * @param record 
     *            the logrecord where class and method name should be stored. 
     *  
     * @return the line number that the call was made from in the caller. 
     */
    /**方法（私有）<br/>
     * 名称:    inferCaller<br/>
     * 描述:    Try to extract the name of the class and method that called the current log statement. <br/>
     * @param record - the logrecord where class and method name should be stored.
     * @return int - the line number that the call was made from in the caller.
     */private int inferCaller(LogRecord record)
     {
         // Get the stack trace.  
         StackTraceElement stack[] = (new Throwable()).getStackTrace();

         // the line number that the caller made the call from  
         int lineNumber = -1;

         // First, search back to a method in the XLCallCenter Logger class.  
         int ix = 0;
         while (ix < stack.length)
         {
             StackTraceElement frame = stack[ix];
             String cname = frame.getClassName();
             if (cname.equals("com.xunlei.demo.util.Logger"))
             {
                 break;
             }
             ix++;
         }
         // Now search for the first frame before the XLCallCenter Logger class.  
         while (ix < stack.length)
         {
             StackTraceElement frame = stack[ix];
             lineNumber = stack[ix].getLineNumber();
             String cname = frame.getClassName();
             if (!cname.equals("com.xunlei.demo.util.Logger"))
             {
                 // We've found the relevant frame.  
                 record.setSourceClassName(cname);
                 record.setSourceMethodName(frame.getMethodName());
                 break;
             }
             ix++;
         }

         return lineNumber;
     }
}
