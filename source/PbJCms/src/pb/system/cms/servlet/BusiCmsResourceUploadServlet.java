package pb.system.cms.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.ProgressListener;
import pb.file.image.ImageProcessor;
import pb.json.JSONArray;
import pb.json.JSONObject;
import pb.system.cms.manager.BusiCmsArticleResFacade;
import pb.system.cms.manager.BusiCmsArticleResFacadeLocal;
import pb.system.file.FileUploader;
import pb.system.file.FileUploader.FileInfo;
import pb.system.file.FileUploader.Result;
import pb.system.limit.entity.BusiResource;
import pb.system.limit.manager.BusiResourceFacade;
import pb.system.limit.manager.BusiResourceFacadeLocal;
import pb.system.limit.module.LoginUser;
import pb.system.limit.servlet.AbstractServlet;

import static pb.system.limit.servlet.AbstractServlet.PAGE_CODE;
import static pb.system.limit.servlet.AbstractServlet.PARAM_RETURN;
import static pb.system.limit.servlet.AbstractServlet.VALUE_RETURN_EXCEL;
import static pb.system.limit.servlet.AbstractServlet.VALUE_RETURN_JSON;
import static pb.system.limit.servlet.AbstractServlet.VALUE_RETURN_XML;

/**
 * 文件上传数据接口
 *
 * @author proteanBear(马强)
 * @version 1.00 2011/11/04
 */
public class BusiCmsResourceUploadServlet extends AbstractServlet
{
    /*-----------------------------开始：静态内容------------------------------*/

    // 上传路径
    private static final String   UPLOAD_PATH         ="upload";
    // 可接受的文件类型
    private static final String[] ACCEPT_TYPES        ={"png","jpg","jpeg","gif","bmp","mp4","mov","mp3"};
    // 类型判断-图片类型
    private static final String   TYPE_PICTURE        ="png|jpg|jpeg|gif|bmp";
    // 类型判断-音频类型
    private static final String   TYPE_AUDIO          ="mp3";
    // 类型判断-视频类型
    private static final String   TYPE_VIDEO          ="mp4|mov";
    // 总上传文件大小限制
    private static final long     MAX_SIZE            =1024*1024*100;
    // 单个传文件大小限制
    private static final long     MAX_FILE_SIZE       =1024*1024*10;
    // 参数-来源
    private static final String   PARAM_FROM          ="from";
    // 参数-是否创建资源
    private static final String   PARAM_RESOURCE      ="resource";
    // 参数值 - CKEditor上传
    private static final String   PARAM_VALUE_CKEDITOR="ckeditor";
    
    /**
     * 静态常量(公共)<br>
     * 名称:    PARAM_MAX<br>
     * 描述:    记录请求参数名称——一页数据数量<br>
     */
    public static final String PARAM_ARTICLE="articleId";

    /*-----------------------------结束：静态内容------------------------------*/
    /**
     * 域(受保护)<br/>
     * 名称: fileUploader<br/>
     * 描述: 文件上传组件<br/>
     */
    protected FileUploader fileUploader;

    /**
     * 域(受保护)<br/>
     * 名称: resManager<br/>
     * 描述: 文件上传组件<br/>
     */
    protected BusiResourceFacadeLocal resManager;
    
    /**
     * 域(受保护)<br/>
     * 名称: resManager<br/>
     * 描述: 文件上传组件<br/>
     */
    protected BusiCmsArticleResFacadeLocal artResManager;

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

            //关键参数判断
            //String appCode=request.getParameter(BusiCmsArticleServlet.PARAM_APP);

            //处理文件上传
            //创建路径（根据用户所在的行政区域）
            String filePath=request.getSession().getServletContext().getRealPath("/")+File.separator+UPLOAD_PATH;
            String path=UPLOAD_PATH;
            if(this.createPath(filePath,user.getAreaId()))
            {
                filePath+=File.separator+user.getAreaId();
                path+=File.separator+user.getAreaId();
                //创建路径（上传文件所属的应用）
                /*if(!this.paramNullCheck(appCode)&&this.createPath(filePath,appCode))
                {
                    filePath+=File.separator+appCode;
                    path+=File.separator+appCode;
                }*/
                //创建路径（当前的日期）
                String datePath=this.dp.setFormat("yyyy-MM-dd").getCurrent();
                if(this.createPath(filePath,datePath))
                {
                    filePath+=File.separator+datePath;
                    path+=File.separator+datePath;
                }
            }
            //根据当前的用户区域设置上传路径
            this.fileUploader.setSavePath(path);

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
            JSONArray resources=new JSONArray();

            //获取是否指定要创建资源数据
            Map<String,String[]> params=this.fileUploader.getParamFields();
            String[] isResParams=params.get(PARAM_RESOURCE);
            String isResStr=(isResParams!=null&&isResParams.length>0)?isResParams[0]:"1";
            boolean isRes=("1".equals(isResStr));
            
            /* 轮询文件表单域，填充 files */
            Set<String> keys=this.fileUploader.getFileFields().keySet();
            for(String key : keys)
            {
                FileInfo[] ffs=this.fileUploader.getFileFields().get(key);
                for(int i=0;i<ffs.length;i++)
                {
                    if(i!=0) filePaths+=";";
                    String name=ffs[i].getSaveFile().getName();
                    String link=String.format(
                            "%s%s%s",
                            this.fileUploader.getSavePath(),
                            File.separator,name
                    );
                    String linkPath=String.format("%s%s",this.fileUploader.getSavePath(),File.separator);
                    filePaths+=link;

                    //需创建资源时执行以下处理
                    if(isRes)
                    {
                        //生成相关缩略图
                        String preName=name.substring(0,name.lastIndexOf("."));
                        String suffix=name.substring(name.lastIndexOf("."));
                        String thumbName=preName+"@thumb";
                        String middleName=preName+"@middle";
                        this.processor.setFile(filePath,name)
                                .setHandleMode(ImageProcessor.ImageHandleMode.COMPRESS_AND_SCALE)
                                .setCompressMode(ImageProcessor.ImageCompressMode.CUSTEM)
                                //.setOutputQuality(1)
                                .setOutputName(thumbName)
                                .setOutputSize(320,320)
                                .setScaleMode(ImageProcessor.ImageScaleMode.PROPORTION)
                                .toImageFile().createNewFile();
                        this.processor.setFile(filePath,name)
                                .setHandleMode(ImageProcessor.ImageHandleMode.COMPRESS_AND_SCALE)
                                .setCompressMode(ImageProcessor.ImageCompressMode.CUSTEM)
                                //.setOutputQuality(0.6f)
                                .setOutputName(middleName)
                                .setOutputSize(640,640)
                                .setScaleMode(ImageProcessor.ImageScaleMode.PROPORTION)
                                .toImageFile().createNewFile();

                        //添加资源的数据信息到资源表中
                        this.resManager.transactionBegin();
                        BusiResource res=new BusiResource();
                        res.setResourceTitle(
                                ffs[i].getUploadFileName().substring(0,ffs[i].getUploadFileName().lastIndexOf("."))
                        );
                        res.setResourceThumb(thumbName);
                        res.setResourceLink(linkPath);
                        res.setResourceMiddle(middleName);
                        res.setResourceFile(preName);
                        res.setResourceSuffix(suffix);
                        res.setResourcePath(filePath);
                        String type=name.substring(name.lastIndexOf("."));
                        res.setResourceType((TYPE_VIDEO.contains(type))?2:(TYPE_AUDIO.contains(type)?1:0));
                        res.setResourceCreate(this.dp.setFormat("yyyy-MM-dd HH:mm:ss").getCurrent());
                        res.setResourceDate(this.dp.setFormat("yyyy-MM-dd").getCurrent());
                        res.setResourceSize(this.fileSize(ffs[i].getSaveFile()));
                        res.setResourceCount(1);
                        //创建资源数据失败时删除对应的文件
                        if(!this.resManager.create(res))
                        {
                            (new File(filePath,thumbName+"."+this.processor.getOutputFormat())).delete();
                            (new File(filePath,middleName+"."+this.processor.getOutputFormat())).delete();
                            ffs[i].getSaveFile().delete();
                            throw new ServletException("创建资源信息出现错误："+this.resManager.getError());
                        }

                        //指定文章标识，建立文章与图片的关联
                        String[] articleIds=params.get(PARAM_ARTICLE);
                        String articleId=(articleIds!=null&&articleIds.length>0)?params.get(PARAM_ARTICLE)[0]:null;
                        if(!this.paramNullCheck(articleId))
                        {
                            if(!this.artResManager.create(articleId,this.resManager.getLastGenerator()))
                            {
                                (new File(filePath,thumbName+"."+this.processor.getOutputFormat())).delete();
                                (new File(filePath,middleName+"."+this.processor.getOutputFormat())).delete();
                                ffs[i].getSaveFile().delete();
                                this.resManager.transactionRollBack();
                                throw new ServletException("关联文章时出现错误："+this.resManager.getError());
                            }
                        }
                        this.resManager.transactionCommit();

                        //创建成功则添加输出JSON信息
                        JSONObject resource=new JSONObject();
                        resource.put("resourceId",this.resManager.getLastGenerator());
                        resource.put("resourceTitle",res.getResourceTitle());
                        resource.put("resourceThumb",res.getResourceThumb());
                        resource.put("resourceLink",res.getResourceLink());
                        resource.put("resourceType",res.getResourceType());
                        resources.put(resource);
                    }
                }
            }

            out.print("{\"success\":true,\"path\":\""+filePaths+"\",\"resource\":"+resources.toString()+"}");
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
        //初始化资源数据管理器
        this.resManager=new BusiResourceFacade(this.connector);
        this.artResManager=new BusiCmsArticleResFacade(this.connector);
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

    /**
     * 方法（受保护）<br/>
     * 名称:    fileSize<br/>
     * 描述:    获取文件尺寸的描述信息<br/>
     *
     * @param file - 文件对象
     * @return String - 文件大小的描述信息
     */
    protected String fileSize(File file)
    {
        long length=file.length();
        String unit="Bytes";
        String after="";
        int divisor=1;

        if(length>1024*1024*1024)
        {
            unit="GB";
            divisor=1024*1024*1024;
        }
        else if(length>1024*1024)
        {
            unit="MB";
            divisor=1024*1024;
        }
        else if(length>1024)
        {
            unit="KB";
            divisor=1024;
        }

        String result="";
        if(divisor==1)
        {
            result=length+""+unit;
        }
        else
        {
            after=""+100*(length%divisor)/divisor;
            if("".equals(after)) after="0";
            result=length/divisor+"."+after+" "+unit;
        }

        return result;
    }
}
