package pb.data;

/**
 * 本Java类是用于描述的类方法
 *
 * @author proteanBear(马强)
 * @version 1.00 2000/00/00
 */
public class SqlLoopExport
{
    public static void main(String[] args)
    {
        int length=91;
        for(int i=28;i<length;i++)
        {
            //输出表空间创建语句
            /*System.out.println("CREATE TABLESPACE glt_"+i+" DATAFILE  'R:\\DATA\\GPS\\glt_"+i+".DBF' \n"
                            + "　　SIZE 1000M AUTOEXTEND \n"
                            + "　　ON NEXT 500M MAXSIZE \n"
                            + "　　UNLIMITED \n"
                            + "　　NOLOGGING \n"
                            + "　　PERMANENT EXTENT MANAGEMENT LOCAL AUTOALLOCATE \n"
                            + "　　BLOCKSIZE 8K \n"
                            + "　　SEGMENT SPACE MANAGEMENT MANUAL \n"
                            + "　　FLASHBACK ON;");
            System.out.println("　partition GLH_P"+i+" values ("+i+") \n"
                            + "　　　tablespace GLT_"+i+" \n"
                            + "　　　pctfree 10 \n"
                            + "　　　pctused 40 \n"
                            + "　　　initrans 1 \n"
                            + "　　　maxtrans 255 \n"
                            + "　　　storage \n"
                            + "　　　( \n"
                            + "　　　　initial 500M \n"
                            + "　　　　minextents 1 \n"
                            + "　　　　maxextents unlimited \n"
                            + "　　　)"+((i==length-1)?"":","));*/
            System.out.println("alter table GPS_LOCATION_HISTORY truncate partition GLH_P"+i+";");
        }
        /*int length2=25;
        String lenStr="";
        for(int j=0;j<length2;j++)
        {
            lenStr=((j<10)?"0":"")+j;
            //System.out.println("\n"
                    //+ "/*---------------------------开始：创建车辆位置实时信息表"+lenStr+"---------------------*//*\n"
                    /*+ "-- 建表并设置表空间\n"
                    + "create table GPS_LOCATION_"+lenStr+"\n"
                    + "(\n"
                    + "　　CUST_ID       NUMBER not null,\n"
                    + "　　CARD_ID       CHAR(15) not null,\n"
                    + "　　PRO_CLASS     CHAR(10),\n"
                    + "　　LAST_LOC_TIME CHAR(19),\n"
                    + "　　LONGITUDE     CHAR(10),\n"
                    + "　　LATITUDE      CHAR(10),\n"
                    + "　　ALTITUDE      NUMBER default 0,\n"
                    + "　　SPEED         NUMBER default 0,\n"
                    + "　　DIRECTION     NUMBER,\n"
                    + "　　VSTATE        CHAR(128),\n"
                    + "　　MILEAGE       NUMBER,\n"
                    + "　　TEMPE         NUMBER,\n"
                    + "　　OIL           NUMBER,\n"
                    + "　　LOC           CHAR(8),\n"
                    + "　　CELL          CHAR(8),\n"
                    + "　　LOC_PROVINCE  CHAR(64),\n"
                    + "　　LOC_CITY      CHAR(64),\n"
                    + "　　LOC_DISTRICT  CHAR(64),\n"
                    + "　　LOC_ADDRESS   CHAR(255)\n"
                    + ")\n"
                    + "　tablespace USERS\n"
                    + "　　pctfree 10\n"
                    + "　　initrans 1\n"
                    + "　　maxtrans 255\n"
                    + "　　storage\n"
                    + "　　(\n"
                    + "　　　initial 64K\n"
                    + "　　　minextents 1\n"
                    + "　　　maxextents unlimited\n"
                    + "　　)\n"
                    + "　NOLOGGING\n"
                    + "　NOCOMPRESS\n"
                    + "　NOCACHE\n"
                    + "　NOPARALLEL\n"
                    + "　MONITORING;\n"
                    + "-- 设置主键\n"
                    + "alter table GPS_LOCATION_"+lenStr+"\n"
                    + "　　add constraint GPS_LOCATION_"+lenStr+"_CUST_ID primary key (CUST_ID)\n"
                    + "　　using index\n"
                    + "　　tablespace USERS\n"
                    + "　　　pctfree 10\n"
                    + "　　　initrans 2\n"
                    + "　　　maxtrans 255\n"
                    + "　　　storage\n"
                    + "　　　(\n"
                    + "　　　　initial 64K\n"
                    + "　　　　minextents 1\n"
                    + "　　　　maxextents unlimited\n"
                    + "　　　);\n"
                    + "-- 设置字段注释\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".CUST_ID\n"
                    + "　is '主键标识';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".CARD_ID\n"
                    + "　is '车辆标识';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".PRO_CLASS\n"
                    + "　is '协议标识';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".LAST_LOC_TIME\n"
                    + "　is '定位时间';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".LONGITUDE\n"
                    + "　is '定位经度';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".LATITUDE\n"
                    + "　is '定位纬度';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".ALTITUDE\n"
                    + "　is '定位海拔';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".SPEED\n"
                    + "　is '定位速度';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".DIRECTION\n"
                    + "　is '定位方向';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".VSTATE\n"
                    + "　is '告警状态';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".MILEAGE\n"
                    + "　is '总里程';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".TEMPE\n"
                    + "　is '温度';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".OIL\n"
                    + "　is '油量';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".LOC\n"
                    + "　is '基站号';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".CELL\n"
                    + "　is '小区号';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".LOC_PROVINCE\n"
                    + "　is '定位所在省';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".LOC_CITY\n"
                    + "　is '定位所在城市';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".LOC_DISTRICT\n"
                    + "　is '定位所在区';\n"
                    + "comment on column GPS_LOCATION_"+lenStr+".LOC_ADDRESS\n"
                    + "　is '定位详细地址';\n"
                    + "-- 设置索引\n"
                    + "create index GPS_LOCATION_"+lenStr+"_CARD_ID on GPS_LOCATION_"+lenStr+" (CARD_ID)\n"
                    + "　tablespace USERS\n"
                    + "　　pctfree 10\n"
                    + "　　initrans 2\n"
                    + "　　maxtrans 255\n"
                    + "　　storage\n"
                    + "　　(\n"
                    + "　　　initial 64K\n"
                    + "　　　minextents 1\n"
                    + "　　　maxextents unlimited\n"
                    + "　　)\n"
                    + "　　LOGGING NOPARALLEL;\n"
                    + "create index GPS_LOCATION_"+lenStr+"_LAST_LOC_TIME on GPS_LOCATION_"+lenStr+" (LAST_LOC_TIME)\n"
                    + "　tablespace USERS\n"
                    + "　　pctfree 10\n"
                    + "　　initrans 2\n"
                    + "　　maxtrans 255\n"
                    + "　　storage\n"
                    + "　　(\n"
                    + "　　　initial 64K\n"
                    + "　　　minextents 1\n"
                    + "　　　maxextents unlimited\n"
                    + "　　)\n"
                    + "　　LOGGING NOPARALLEL;\n"*/
        //+ "/*---------------------------结束：创建车辆位置实时信息表"+lenStr+"----------------------*/");
        //System.out.println(" *        车辆位置实时信息表"+lenStr+"（GPS_LOCATION_"+lenStr+"）");
            /*System.out.println("\n/*---------------------------开始：创建车辆位置实时信息表"+lenStr+"触发器----------------*//*\n"
                    + "-- 创建触发器\n"
                    + "CREATE OR REPLACE TRIGGER SYNCH_LOCATION_DATA_"+lenStr+" \n"
                    + "-- 设置为插入后执行\n"
                    + "AFTER INSERT \n"
                    + "ON GPS_LOCATION_"+lenStr+" \n"
                    + "-- 设置更新前的值为OLDL，更新后的值为NEWL\n"
                    + "REFERENCING NEW AS \"NEWL\" OLD AS \"OLDL\" \n"
                    + "-- 执行脚本"
                    + "FOR EACH ROW \n"
                    + "DECLARE \n"
                    + "tmpVar NUMBER;\n"
                    + "BEGIN\n"
                    + "　tmpVar := 0;\n\n"
                    + "/*将更新后的值插入到历史表，字段为\n"
                    + " * CUST_ID - 主键标识\n"
                    + " * CARD_ID - 车辆标识\n"
                    + " * PRO_CLASS - 协议标识\n"
                    + " * LAST_LOC_TIME - 定位时间\n"
                    + " * LONGITUDE - 定位经度\n"
                    + " * LATITUDE - 定位纬度\n"
                    + " * ALTITUDE - 定位海拔\n"
                    + " * SPEED - 定位速度\n"
                    + " * DIRECTION - 定位方向\n"
                    + " * VSTATE - 告警状态\n"
                    + " * MILEAGE - 总里程\n"
                    + " * TEMPE - 温度\n"
                    + " * OIL - 油量\n"
                    + " * LOC - 基站号\n"
                    + " * CELL - 小区号\n"
                    + " * LOC_PROVINCE - 定位所在省\n"
                    + " * LOC_CITY - 定位所在城市\n"
                    + " * LOC_DISTRICT - 定位所在区\n"
                    + " * LOC_ADDRESS - 定位详细地址\n"
                    + " *//*\n"
                    + "INSERT INTO gps_location_history (\n"
                    + "　CUST_ID, CARD_ID, PRO_CLASS, \n"
                    + "　LAST_LOC_TIME, LONGITUDE, LATITUDE, \n"
                    + "　ALTITUDE, SPEED, DIRECTION, \n"
                    + "　VSTATE, MILEAGE, TEMPE, \n"
                    + "　OIL, LOC, CELL, \n"
                    + "　LOC_PROVINCE, LOC_CITY, LOC_DISTRICT, \n"
                    + "　LOC_ADDRESS) \n"
                    + "VALUES ( :NEWl.CUST_ID, :NEWl.CARD_ID, :NEWl.PRO_CLASS, \n"
                    + "　:NEWl.LAST_LOC_TIME, :NEWl.LONGITUDE, :NEWl.LATITUDE, \n"
                    + "　:NEWl.ALTITUDE, :NEWl.SPEED, :NEWl.DIRECTION, \n"
                    + "　:NEWl.VSTATE, :NEWl.MILEAGE, :NEWl.TEMPE, \n"
                    + "　:NEWl.OIL, :NEWl.LOC, :NEWl.CELL, \n"
                    + "　:NEWl.LOC_PROVINCE, :NEWl.LOC_CITY, :NEWl.LOC_DISTRICT, \n"
                    + "　:NEWl.LOC_ADDRESS ) ;\n\n"
                    + "　EXCEPTION\n"
                    + "　　WHEN OTHERS THEN\n"
                    + "　　　RAISE;\n"
                    + "END synch_location_data;\n"
                    + "/\n"
                    + "SHOW ERRORS;\n"
                    + "/*---------------------------结束：创建车辆位置实时信息表"+lenStr+"触发器----------------*//*");
        }*/
    }
}