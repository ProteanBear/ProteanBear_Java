package pb.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pb.data.Connector;
import pb.data.JdbcConnector;
import pb.data.QueryManager;
import pb.generator.meta.MetaData;
import pb.generator.meta.TableMetaData;
import pb.generator.template.ActionTemplate;
import pb.generator.template.EntityTemplate;
import pb.generator.template.FacadeLocalTemplate;
import pb.generator.template.FacadeTemplate;
import pb.generator.template.ServletTemplate;
import pb.generator.template.Template;

/**
 * 数据相关类生成器类，根据指定数据库连接生成数据表的实体类、管理器类、应用类及服务接口类
 *
 * @author ProteanBear
 */
public class DataClassGenerator
{
    /*静态属性（受保护）
     *  名称：DIRECTORY_ENTITY
     *  描述：实体类目录
     */
    protected static final String DIRECTORY_ENTITY="entity";

    /*静态属性（受保护）
     *  名称：DIRECTORY_MANAGE
     *  描述：管理类目录
     */
    protected static final String DIRECTORY_MANAGE="manager";

    /*静态属性（受保护）
     *  名称：DIRECTORY_ACTION
     *  描述：应用类目录
     */
    protected static final String DIRECTORY_ACTION="action";

    /*静态属性（受保护）
     *  名称：DIRECTORY_SERVLET
     *  描述：服务接口类目录
     */
    protected static final String DIRECTORY_SERVLET="servlet";

    /*属性（受保护）
     *  名称：connector
     *  描述：数据连接器对象
     */
    protected Connector connector;

    /*属性（受保护）
     *  名称：query
     *  描述：语句执行对象
     */
    protected QueryManager query;

    /*属性（受保护）
     *  名称：filePath
     *  描述：记录文件生成的路径
     */
    protected String filePath;

    /*构造函数
     *@param connector - 连接器
     *@param filePath - 文件生成的路径
     */
    public DataClassGenerator(Connector connector,String filePath)
    {
        this.connector=connector;
        this.query=(this.connector!=null)?new QueryManager(this.connector):null;
        this.filePath=filePath;
    }

    /*方法（公共）
     *  名称：generateDataClass
     *  描述：生成数据相关类，包括实体类、管理接口、管理类以及应用类和服务接口
     */
    public void generateDataClass() throws SQLException, IOException
    {
        //过滤空连接
        if(this.connector==null) return;

        //开启数据库连接
        this.connector.connect();

        //生成相关类
        if(this.connector.isConnected())
        {
            //获取数据库的全部表
            List<MetaData> tables=this.allTablesInDatabase();

            //遍历全部表生成相关类
            for(MetaData metaData : tables)
            {
                //生成数据表实体类
                Template entityTemp=new EntityTemplate();
                if(this.createDirectory(DIRECTORY_ENTITY))
                {
                    this.writeToFile(
                            this.filePath+DIRECTORY_ENTITY,
                            entityTemp.generateFileName(metaData),
                            entityTemp.contentForMetaData(metaData)
                    );
                }

                //生成数据表管理类
                Template facadeLocalTemp=new FacadeLocalTemplate();
                Template facadeTemp=new FacadeTemplate();
                if(this.createDirectory(DIRECTORY_MANAGE))
                {
                    this.writeToFile(
                            this.filePath+DIRECTORY_MANAGE,
                            facadeLocalTemp.generateFileName(metaData),
                            facadeLocalTemp.contentForMetaData(metaData)
                    );
                    this.writeToFile(
                            this.filePath+DIRECTORY_MANAGE,
                            facadeTemp.generateFileName(metaData),
                            facadeTemp.contentForMetaData(metaData)
                    );
                }

                //生成数据表应用层
                Template actionTemp=new ActionTemplate();
                if(this.createDirectory(DIRECTORY_ACTION))
                {
                    this.writeToFile(
                            this.filePath+DIRECTORY_ACTION,
                            actionTemp.generateFileName(metaData),
                            actionTemp.contentForMetaData(metaData)
                    );
                }

                //生成数据表服务接口
                Template servletTemp=new ServletTemplate();
                if(this.createDirectory(DIRECTORY_SERVLET))
                {
                    this.writeToFile(
                            this.filePath+DIRECTORY_SERVLET,
                            servletTemp.generateFileName(metaData),
                            servletTemp.contentForMetaData(metaData)
                    );
                }
            }
        }

        //关闭数据库连接
        this.connector.close();
    }

    /*方法（受保护）
     *  名称：allTablesInDatabase
     *  描述：获取指定连接数据库中的全部表名称、注释和结构
     *@return List - 全部表名列表
     */
    protected List<MetaData> allTablesInDatabase() throws SQLException
    {
        List<MetaData> result=new ArrayList<>();

        //连接数据库获取
        if(this.connector.isConnected())
        {
            //数据库信息
            DatabaseMetaData metaData=connector.getConnection().getMetaData();

            //数据表列表
            ResultSet sqlResult=this.query.executeQuery("show table status");
            while(sqlResult.next())
            {
                //获取数据表名称
                String tableName=sqlResult.getString("name");
                //获取数据表注释
                String tableComment=sqlResult.getString("comment");
                //获取数据表主键
                ResultSet primaryResultSet=metaData.getPrimaryKeys(null,null,tableName);
                String tablePrimary="";
                while(primaryResultSet.next())
                {
                    tablePrimary=primaryResultSet.getString(4);
                }
                //获取数据表结构
                ResultSet metaResult=metaData.getColumns(null,null,tableName,null);
                List<TableMetaData> tableMetaData=new ArrayList<>();
                while(metaResult.next())
                {
                    //字段名称
                    String columnName=metaResult.getString(4);
                    //字段类型
                    String columnType=metaResult.getString(6);
                    //字段注释
                    String columnComment=metaResult.getString(12);
                    //字段结构
                    tableMetaData.add(new TableMetaData(columnName,columnType,columnComment));
                }
                result.add(new MetaData(tableName,tableComment,tablePrimary,tableMetaData));
            }
        }

        return result;
    }

    /*方法（受保护）
     *  名称：createDirectory
     *  描述：创建目录
     */
    protected boolean createDirectory(String name)
    {
        File directory=new File(this.filePath,name);
        if(!directory.exists()) directory.mkdir();
        return directory.exists();
    }

    /*方法（受保护）
     *  名称：writeToFile
     *  描述：将给定的字符内容写入文件中
     *@param fileName - 文件名称
     *@param content - 字符内容
     *@return boolean - 创建是否成功
     */
    protected boolean writeToFile(String filePath,String fileName,String content) throws IOException
    {
        boolean result=false;

        //创建文件
        File file=new File(filePath,fileName);
        if(!file.createNewFile()) return result;

        //写入文件
        FileOutputStream output=null;
        try
        {
            output=new FileOutputStream(file);
            output.write(content.getBytes());
            result=true;
        }
        catch(FileNotFoundException ex)
        {
        }
        finally
        {
            try
            {
                output.close();
            }
            catch(IOException e)
            {
            }
        }

        return result;
    }

    public static void main(String[] args) throws SQLException
    {

        Connector connector=new JdbcConnector(
                JdbcConnector.DATABASE_MYSQL,
                "127.0.0.1",3306,"ProteanBear","pbadmin","admin147258"
        );
        connector.connect();
        if(connector.isConnected())
        {
            /**/
            DataClassGenerator test=new DataClassGenerator(connector,"/Users/maqiang/workspace/backup/");
            try
            {
                test.generateDataClass();
            }
            catch(IOException ex)
            {
                System.out.println(ex.getMessage());
            }

            /**
             ResultSet result=connector.getConnection().getMetaData().getPrimaryKeys(null,null,"ORIGINAL_LIMIT");
             while(result.next())
             {
             System.out.println(result.getString(4));
             }
             /**/
        }
        else
        {
            System.out.println("连接数据库失败："+connector.getError());
        }
        connector.close();
    }
}