package pb.system.cms.servlet;

import org.apache.commons.fileupload.ProgressListener;
import pb.file.image.ImageProcessor;
import pb.system.file.FileUploader;
import pb.system.limit.module.LoginUser;
import pb.system.limit.servlet.AbstractServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * CKEditor图片上传数据接口
 *
 * @author proteanBear(马强)
 * @version 1.00 2016/11/05
 */
public class CKEditorImageUploadsServlet extends AbstractServlet
{
    /*-----------------------------开始：静态内容------------------------------*/

    // 上传路径
    private static final String   UPLOAD_PATH         ="upload";
    // 可接受的文件类型
    private static final String[] ACCEPT_TYPES        ={"png","jpg","jpeg","gif","bmp","mp4","mov","mp3"};
    // 总上传文件大小限制
    private static final long     MAX_SIZE            =1024*1024*100;
    // 单个传文件大小限制
    private static final long     MAX_FILE_SIZE       =1024*1024*10;

    /*-----------------------------结束：静态内容------------------------------*/

    /**
     * 域(受保护)<br/>
     * 名称: fileUploader<br/>
     * 描述: 文件上传组件<br/>
     */
    protected FileUploader fileUploader;

    /**
     * 域(受保护)<br/>
     * 名称: processor<br/>
     * 描述: 图片处理器<br/>
     */
    protected ImageProcessor processor;

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
            //处理请求参数，设置输出格式
            response.setContentType("text/html;charset="+PAGE_CODE);

            LoginUser user=null;
            //如果用户已经登录，获取登录用户属性
            user=this.userAction.getLoginUserInSession(request);

            //处理文件上传
            //创建路径（根据用户所在的行政区域）
            String filePath=request.getSession().getServletContext().getRealPath("/")+File.separator+UPLOAD_PATH;
            String path=UPLOAD_PATH;
            if(this.createPath(filePath,user.getAreaId()))
            {
                filePath+=File.separator+user.getAreaId();
                path+=File.separator+user.getAreaId();
                //创建路径（当前的日期）
                String datePath=this.dp.setFormat("yyyy-MM-dd").getCurrent();
                if(this.createPath(filePath,datePath))
                {
                    path+=File.separator+datePath;
                }
            }
            //根据当前的用户区域设置上传路径
            this.fileUploader.setSavePath(path);

            // 执行上传并获取操作结果
            FileUploader.Result result=this.fileUploader.upload(request,response);
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
                FileUploader.FileInfo[] ffs=this.fileUploader.getFileFields().get(key);
                for(int i=0;i<ffs.length;i++)
                {
                    if(i!=0) filePaths+=";";
                    String name=ffs[i].getSaveFile().getName();
                    String link=String.format(
                            "../%s%s%s",
                            this.fileUploader.getSavePath(),
                            File.separator,name
                    );
                    filePaths+=link;
                }
            }

            //CKEditor回调处理
            String callback = request.getParameter("CKEditorFuncNum");
            out.println("<script type=\"text/javascript\">");
            out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + filePaths + "',''" + ")");
            out.println("</script>");
        }
        catch(ServletException ex)
        {
            this.errorHandle(request,out,ex.getMessage().replace("javax.servlet.ServletException:",""));
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
     * 名称:    errorHandle<br>
     * 描述:    封装产生错误时的处理方法<br>
     *
     * @param out
     * @param error - 错误信息
     * @throws java.io.IOException
     */
    protected void errorHandle(HttpServletRequest request,PrintWriter out,String error) throws IOException
    {
        String callback = request.getParameter("CKEditorFuncNum");
        out.println("<script type=\"text/javascript\">");
        out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",''," + "'"+error+"');");
        out.println("</script>");
    }

    /**
     * 方法（受保护）<br/>
     * 名称: init<br/>
     * 描述: Servlet初始化时调用此方法，初始化连接器以及数据层接口的实现方法<br/>
     *
     * @throws javax.servlet.ServletException
     */
    @Override
    public void init() throws ServletException
    {
        super.init();

        //初始化文件上传器
        this.fileUploader=new FileUploader(UPLOAD_PATH,ACCEPT_TYPES,MAX_SIZE,MAX_FILE_SIZE);
        this.processor=new ImageProcessor();

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

    /**
     * 方法（受保护、抽象）<br/>
     * 名称:    getLimitPluginId<br/>
     * 描述:    获取当前功能对应的功能插件标识<br/>
     *
     * @return int - 创建模式的权限编码
     */
    @Override
    protected String getLimitPluginId()
    {
        return "";
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    initDataAction<br/>
     * 描述:    初始化数据应用层接口<br/>
     */
    @Override
    protected void initDataAction()
    {
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    createPath<br/>
     * 描述:    创建指定的文件目录<br/>
     *
     * @param upPath - 上级目录
     * @param path   - 路径名称
     * @return boolean - 是否创建成功
     * @throws javax.servlet.ServletException
     */
    protected boolean createPath(String upPath,String path) throws ServletException
    {
        File file=new File(upPath+File.separator+path);
        boolean fileExist=file.exists();
        if(!fileExist) fileExist=file.mkdirs();
        if(!fileExist)
        {
            throw new ServletException("创建文件夹失败");
        }
        return fileExist;
    }
}