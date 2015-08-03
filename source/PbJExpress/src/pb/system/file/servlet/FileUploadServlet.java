package pb.system.file.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.ProgressListener;
import pb.system.file.FileUploader;
import pb.system.file.FileUploader.FileInfo;
import pb.system.file.FileUploader.Result;
import pb.system.limit.module.LoginUser;
import pb.system.limit.servlet.AbstractServlet;

/**
 * 文件上传数据接口
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/11/04
 */
public class FileUploadServlet extends AbstractServlet
{
    /*-----------------------------开始：静态内容------------------------------*/

    // 上传路径
    private static final String   UPLOAD_PATH  ="upload";
    // 可接受的文件类型
    private static final String[] ACCEPT_TYPES ={"txt","pdf","png","doc","docx","jpg","zip","rar"};
    // 总上传文件大小限制
    private static final long     MAX_SIZE     =1024*1024*100;
    // 单个传文件大小限制
    private static final long     MAX_FILE_SIZE=1024*1024*10;

    /*-----------------------------结束：静态内容------------------------------*/
    /**
     * 域(受保护)<br>
     * 名称: fileUploader<br>
     * 描述: 文件上传组件<br>
     */
    protected FileUploader fileUploader;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void process(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException
    {
        //设置页面输入输出编码
        request.setCharacterEncoding(PAGE_CODE);
        response.setCharacterEncoding(PAGE_CODE);

        PrintWriter out=response.getWriter();
        try
        {
            //处理请求参数，设置输出格式（默认为Json格式）
            String re=request.getParameter(PARAM_RETURN);
            re=(re==null)?VALUE_RETURN_JSON:re;
            this.isXml=(VALUE_RETURN_XML.equals(re.trim().toLowerCase()));
            this.isExcel=(VALUE_RETURN_EXCEL.equals(re.trim().toLowerCase()));
            if(this.isExcel)
            {
                response.setContentType("application/vnd.ms-excel;charset="+PAGE_CODE);
                response.setHeader("Content-disposition","attachment; filename="+this.getExcelFileName()+".xls");
            }
            else
            {
                response.setContentType("text/html;charset="+PAGE_CODE);
            }

            LoginUser user=null;
            //检查用户是否已经登录
            //boolean isLogin=this.userAction.isLogin(request);
            //如果用户未登录，执行登录处理
            //if(!isLogin)       isLogin=this.userAction.login(request);
            //如果用户已经登录，获取登录用户属性
            user=this.userAction.getLoginUserInSession(request);

            //处理文件上传
            //根据当前的用户区域设置上传路径
            this.fileUploader.setSavePath(UPLOAD_PATH+File.separator+user.getAreaId());
            //创建路径
            File file=new File(
                    request.getSession().getServletContext()
                            .getRealPath("/")+File.separator+UPLOAD_PATH+File.separator+user.getAreaId()
            );
            boolean fileExist=file.exists();
            if(!fileExist) fileExist=file.mkdirs();
            if(!fileExist)
            {
                throw new ServletException("创建文件夹失败");
            }

            // 执行上传并获取操作结果
            Result result=this.fileUploader.upload(request,response);

            // 检查操作结果
            if(FileUploader.Result.SUCCESS!=result)
            {
                // 设置 request attribute
                throw new ServletException(this.fileUploader.getCause().getMessage());
            }

            // 文件保存路径的列表
            String filePaths="";

            /* 轮询文件表单域，填充 files */
            Set<String> keys=this.fileUploader.getFileFields().keySet();
            for(String key : keys)
            {
                FileInfo[] ffs=this.fileUploader.getFileFields().get(key);
                for(int i=0;i<ffs.length;i++)
                {
                    if(i!=0)
                    {
                        filePaths+=";";
                    }
                    filePaths+=String.format(
                            "%s%s%s",
                            this.fileUploader.getSavePath(),
                            File.separator,ffs[i].getSaveFile().getName()
                    );
                }
            }

            out.print("{\"success\":true,\"path\":\""+filePaths+"\"}");
        }
        catch(ServletException ex)
        {
            this.errorHandle(out,ex.getMessage().replace("javax.servlet.ServletException:",""));
        }
        finally
        {
            if(this.connector!=null)
            {
                this.connector.close();
            }
            out.close();
        }
    }

    /**
     * 方法（受保护）<br>
     * 名称: init<br>
     * 描述: Servlet初始化时调用此方法，初始化连接器以及数据层接口的实现方法<br>
     *
     * @throws javax.servlet.ServletException
     */
    @Override
    public void init() throws ServletException
    {
        super.init();

        //初始化文件上传器
        this.fileUploader=new FileUploader(UPLOAD_PATH,ACCEPT_TYPES,MAX_SIZE,MAX_FILE_SIZE);

        // 根据实际情况设置对象属性（可选）
        //设置名字生成器
        /*this.fileUploader.setFileNameGenerator(new FileNameGenerator()
        {
            @Override
            public String generate(FileItem item,
                    String suffix)
            {
                return String.format("%d_%d", item.hashCode(), item.get().hashCode());
            }
        });*/
        //设置进度监听器
        this.fileUploader.setServletProgressListener(
                new ProgressListener()
                {
                    @Override
                    public void update(long pBytesRead,
                                       long pContentLength,
                                       int pItems)
                    {
                        System.out.printf("%d: length -> %d, read -> %d.\n",pItems,pContentLength,pBytesRead);
                    }
                }
        );
    }

    @Override
    protected String getLimitPluginId()
    {
        return "";
    }

    @Override
    protected void initDataAction()
    {
    }
}
