package pb.text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 本Java类是用于对日期常用操作进行处理的类方法。<br>
 * 1.10 - 增加getDateTime获取时间long值的方法。<br>
 * 1.02 - 增加获取偏移日期的方法（带变化属性参数）。<br>
 *
 * @author proteanBear(马强)
 * @version 1.10 2012/03/31
 */
public class DateProcessor
{
    /**
     * 域(私有)<br>
     * 名称:    cal<br>
     * 描述:    日历对象<br>
     */
    private Calendar cal;

    /**
     * 域(私有)<br>
     * 名称:    format<br>
     * 描述:    格式输出对象<br>
     */
    private SimpleDateFormat format;

    /**
     * 构造函数（无参数）<br>
     * 描述：   初始化私有域
     */
    public DateProcessor()
    {
        this.cal=Calendar.getInstance();
        this.format=new SimpleDateFormat("yyyy-MM-dd");
        this.cal.setFirstDayOfWeek(Calendar.MONDAY);
    }

    /**
     * 构造函数（一个参数）<br>
     * 描述：   初始化私有域
     *
     * @param pattern - 日期格式
     */
    public DateProcessor(String pattern)
    {
        this();
        this.format.applyPattern(pattern);
    }

    /**
     * 构造函数（一个参数）<br>
     * 描述：   初始化私有域
     *
     * @param date - 日期格式
     */
    public DateProcessor(Date date)
    {
        this();
        this.cal.setTime(date);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getFormat<br>
     * 描述:    获取当前的时间格式<br>
     *
     * @return String - 时间格式
     */
    public String getFormat()
    {
        return this.format.toPattern();
    }

    /**
     * 方法（公共）<br>
     * 名称:    setFormat<br>
     * 描述:    设置时间格式<br>
     *
     * @param pattern - 时间格式
     * @return DateProcessor - 对象本身
     */
    public DateProcessor setFormat(String pattern)
    {
        this.format.applyPattern(pattern);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getCurrent<br>
     * 描述:    获得当前时间<br>
     *
     * @return String - 当前时间
     */
    public String getCurrent()
    {
        return this.format.format(new Date());
    }

    /**
     * 方法（公共）<br>
     * 名称:    getFirstDayOfWeek<br>
     * 描述:    获得本周第一天时间<br>
     *
     * @return String - 本周第一天时间
     */
    public String getFirstDayOfWeek()
    {
        return this.getFirstDayOfWeek(this.getCurrent());
    }

    /**
     * 方法（公共）<br>
     * 名称:    getFirstDayOfWeek<br>
     * 描述:    获得指定时间所在周第一天时间<br>
     *
     * @param date - 指定基点
     * @return String - 本周第一天时间
     */
    public String getFirstDayOfWeek(String date)
    {
        String result="";
        try
        {
            Date dat=this.format.parse(date);
            this.cal.setTime(dat);
            this.cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            result=this.format.format(this.cal.getTime());
        }
        catch(ParseException ex)
        {
            result="";
        }
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getFirstDayOfWeek<br>
     * 描述:    获得本周第一天时间<br>
     *
     * @return String - 指定时间所在月第一天时间
     */
    public String getFirstDayOfMonth()
    {
        return this.getFirstDayOfMonth(this.getCurrent());
    }

    /**
     * 方法（公共）<br>
     * 名称:    getFirstDayOfWeek<br>
     * 描述:    获得指定时间所在月第一天时间<br>
     *
     * @param date - 指定时间
     * @return String - 指定时间所在月第一天时间
     */
    public String getFirstDayOfMonth(String date)
    {
        String result="";
        try
        {
            Date dat=this.format.parse(date);
            this.cal.setTime(dat);
            this.cal.set(
                    Calendar.DAY_OF_MONTH,
                    this.cal.getActualMinimum(Calendar.DAY_OF_MONTH)
            );
            result=this.format.format(this.cal.getTime());
        }
        catch(ParseException ex)
        {
            result="";
        }
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDate<br>
     * 描述:    获得当前时间的偏移后的时间<br>
     *
     * @param distance - 偏移量，以前的时间为负值
     * @return long - 时间
     */
    public long getDateTime(int distance)
    {
        return this.getDateTime(this.getCurrent(),distance);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDate<br>
     * 描述:    获得当前时间的偏移后的时间<br>
     *
     * @param field    - 变化的属性
     * @param distance - 偏移量，以前的时间为负值
     * @return String - 时间
     */
    public long getDateTime(int field,int distance)
    {
        return this.getDateTime(field,this.getCurrent(),distance);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDate<br>
     * 描述:    获得指定时间的偏移后的时间<br>
     *
     * @param date     - 日期
     * @param distance - 偏移量，以前的时间为负值
     * @return String - 时间
     */
    public long getDateTime(String date,int distance)
    {
        return this.getDateTime(Calendar.DATE,date,distance);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDate<br>
     * 描述:    获得指定时间的偏移后的时间<br>
     *
     * @param field    - 变化的属性
     * @param date     - 日期
     * @param distance - 偏移量，以前的时间为负值
     * @return String - 时间
     */
    public long getDateTime(int field,String date,int distance)
    {
        long result=-1;
        try
        {
            Date dat=this.format.parse(date);
            this.cal.setTime(dat);
            this.cal.add(field,distance);
            result=this.cal.getTime().getTime();
        }
        catch(ParseException ex)
        {
            result=-1;
        }
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDate<br>
     * 描述:    获得指定时间的偏移后的时间<br>
     *
     * @param date     - 日期
     * @param distance - 偏移量，以前的时间为负值
     * @return String - 时间
     */
    public long getDateTime(Date date,int distance)
    {
        return this.getDateTime(Calendar.DATE,date,distance);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDate<br>
     * 描述:    获得指定时间的偏移后的时间<br>
     *
     * @param field    - 变化的属性
     * @param date     - 日期
     * @param distance - 偏移量，以前的时间为负值
     * @return String - 时间
     */
    public long getDateTime(int field,Date date,int distance)
    {
        long result=-1;
        this.cal.setTime(date);
        this.cal.add(field,distance);
        result=this.cal.getTime().getTime();
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDate<br>
     * 描述:    获得当前时间的偏移后的时间<br>
     *
     * @param distance - 偏移量，以前的时间为负值
     * @return String - 时间
     */
    public String getDate(int distance)
    {
        return this.getDate(this.getCurrent(),distance);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDate<br>
     * 描述:    获得当前时间的偏移后的时间<br>
     *
     * @param field    - 变化的属性
     * @param distance - 偏移量，以前的时间为负值
     * @return String - 时间
     */
    public String getDate(int field,int distance)
    {
        return this.getDate(field,this.getCurrent(),distance);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDate<br>
     * 描述:    获得指定时间的偏移后的时间<br>
     *
     * @param date-    日期
     * @param distance - 偏移量，以前的时间为负值
     * @return String - 时间
     */
    public String getDate(String date,int distance)
    {
        return this.getDate(Calendar.DATE,date,distance);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDate<br>
     * 描述:    获得指定时间的偏移后的时间<br>
     *
     * @param field    - 变化的属性
     * @param date-    日期
     * @param distance - 偏移量，以前的时间为负值
     * @return String - 时间
     */
    public String getDate(int field,String date,int distance)
    {
        String result="";
        try
        {
            Date dat=this.format.parse(date);
            this.cal.setTime(dat);
            this.cal.add(field,distance);
            result=this.format.format(this.cal.getTime());
        }
        catch(ParseException ex)
        {
            result="";
        }
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDate<br>
     * 描述:    获得指定时间的偏移后的时间<br>
     *
     * @param date-    日期
     * @param distance - 偏移量，以前的时间为负值
     * @return String - 时间
     */
    public String getDate(Date date,int distance)
    {
        return this.getDate(Calendar.DATE,date,distance);
    }

    /**
     * 方法（公共）<br>
     * 名称:    getDate<br>
     * 描述:    获得指定时间的偏移后的时间<br>
     *
     * @param field    - 变化的属性
     * @param date-    日期
     * @param distance - 偏移量，以前的时间为负值
     * @return String - 时间
     */
    public String getDate(int field,Date date,int distance)
    {
        String result="";
        this.cal.setTime(date);
        this.cal.add(field,distance);
        result=this.format.format(this.cal.getTime());
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    isBefore<br>
     * 描述:    比较当前时间是否在指定时间以前<br>
     *
     * @param time - 指定时间
     * @return boolean - 是否在指定时间以前
     */
    public boolean isBefore(String time)
    {
        return this.isBefore(time,this.format.format(new Date()));
    }

    /**
     * 方法（公共）<br>
     * 名称:    isBefore<br>
     * 描述:    比较prious指定的时间是否在next指定时间以前<br>
     *
     * @param prious - 较早时间
     * @param next   - 较晚时间
     * @return boolean - 是否在指定时间以前
     */
    public boolean isBefore(String prious,String next)
    {
        Date nexttime=null;
        Date prioustime=null;
        try
        {
            prioustime=this.format.parse(prious);
            nexttime=this.format.parse(next);
        }
        catch(ParseException ex)
        {
            return false;
        }
        return prioustime.before(nexttime);
    }

    /**
     * 方法（公共）<br>
     * 名称:    isAfter<br>
     * 描述:    比较当前时间是否在指定时间之后<br>
     *
     * @param time - 指定时间
     * @return boolean - 是否在指定时间以前
     */
    public boolean isAfter(String time)
    {
        return this.isAfter(this.format.format(new Date()),time);
    }

    /**
     * 方法（公共）<br>
     * 名称:    isAfter<br>
     * 描述:    比较next指定的时间是否在prious指定时间之后<br>
     *
     * @param prious - 较早时间
     * @param next   - 较晚时间
     * @return boolean - 是否在指定时间以前
     */
    public boolean isAfter(String next,String prious)
    {
        Date nexttime=null;
        Date prioustime=null;
        try
        {
            prioustime=this.format.parse(prious);
            nexttime=this.format.parse(next);
        }
        catch(ParseException ex)
        {
            return false;
        }
        return nexttime.after(prioustime);
    }

    /**
     * 方法（公共）<br>
     * 名称:    isEquals<br>
     * 描述:    比较当前时间与指定时间是否相等<br>
     *
     * @param time - 指定时间
     * @return boolean - 当前时间与指定时间是否相等
     */
    public boolean isEquals(String time)
    {
        return this.isAfter(this.format.format(new Date()),time);
    }

    /**
     * 方法（公共）<br>
     * 名称:    isAfter<br>
     * 描述:    比较time1指定的时间是否与time2指定时间相等<br>
     *
     * @param time1 - 时间1
     * @param time2 - 时间2
     * @return boolean - 是否相等
     */
    public boolean isEquals(String time1,String time2)
    {
        Date datetime1=null;
        Date datetime2=null;
        try
        {
            datetime1=this.format.parse(time1);
            datetime2=this.format.parse(time2);
        }
        catch(ParseException ex)
        {
            return false;
        }
        return datetime1.equals(datetime2);
    }
}