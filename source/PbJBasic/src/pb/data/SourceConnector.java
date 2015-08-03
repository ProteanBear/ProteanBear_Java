package pb.data;

import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 本Java类是用于描述通过引用web服务器连接池资源连接数据库的方法，实现了Connecter接口
 *
 * @author proteanBear(马强)
 * @version 2.00 2010/09/17
 * @since java ee 5
 */
public class SourceConnector extends AbstractConnector implements Connector
{
    /**
     * 域(私有)<br>
     * 名称:    resource<br>
     * 描述:    引用的资源名称<br>
     */
    private String resource;

    /**
     * 构造函数（无参数）<br>
     */
    public SourceConnector()
    {
        super();
    }

    /**
     * 构造函数（一个参数）<br>
     * 描述：   指定引用的资源名称
     *
     * @param source - 资源名称
     */
    public SourceConnector(String source)
    {
        this();
        this.setResource(source);
    }

    /**
     * 构造函数（一个参数）<br>
     * 描述：   指定引用的资源名称
     *
     * @param source    - 资源名称
     * @param loginname - 登录名
     * @param pass      - 密码
     */
    public SourceConnector(String source,String loginname,String pass)
    {
        this();
        this.setResource(source);
        this.setLoginname(loginname);
        this.setPassword(pass);
    }

    /**
     * 方法（公共）<br>
     * 名称:    connect<br>
     * 描述:    根据指定的Url连接到数据库并获取连接<br>
     */
    @Override
    public void connect()
    {
        if(this.resource==null || "".equals(this.resource.trim()))
        {
            this.setError("未指定引用的资源");
            return;
        }

        try
        {
            InitialContext initContext=new InitialContext();
            DataSource source=(DataSource)initContext.lookup(this.resource);
            if(this.getLoginname()!=null
                    && !"".equals(this.getLoginname().trim()))
            {
                this.setConnection
                        (
                                source.getConnection
                                        (this.getLoginname(),this.getPassword())
                        );
            }
            else
            {
                this.setConnection(source.getConnection());
            }
            this.setIsConnect((this.getConnection()!=null));
        }
        catch(SQLException|NamingException ex)
        {
            this.setError(ex.getMessage());
            this.setIsConnect(false);
        }
    }

    /**
     * 访问器<br>
     * 目标:    resource<br>
     *
     * @return String - 当前资源名称
     */
    public String getResource()
    {
        return this.resource;
    }

    /**
     * 更改器
     * 目标：   resource
     *
     * @param source - 资源名称
     */
    public final void setResource(String source)
    {
        this.resource=source;
    }
}
