package pb.system.limit.servlet;

import org.apache.commons.fileupload.ProgressListener;
import pb.file.image.ImageProcessor;
import pb.json.JSONArray;
import pb.json.JSONObject;
import pb.system.file.FileUploader;
import pb.system.limit.action.BusiMemberAction;
import pb.system.limit.entity.BusiMember;
import pb.system.limit.manager.BusiMemberFacade;
import pb.system.limit.manager.BusiMemberFacadeLocal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

/**
 * 数据访问HTTP接口 - 会员上传头像接口
 *
 * @author proteanBear(马强)
 * @version 1.00 2012/02/27
 */
public class BusiMemberUpdateHeadServlet extends AbstractServlet
{
    /*-----------------------------开始：静态内容------------------------------*/

    // 上传路径
    private static final String   UPLOAD_TEMP         ="upload"+File.separator+"temp";
    // 上传路径
    private static final String   UPLOAD_PATH         ="upload"+File.separator+"head";
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
     * 域(受保护)<br>
     * 名称:    dataAction<br>
     * 描述:    使用DataAction接口对数据进行相关处理<br>
     */
    protected BusiMemberAction memberAction;

    /**
     * 域(受保护)<br>
     * 名称:    memberManager<br>
     * 描述:    会员数据管理器<br>
     */
    protected BusiMemberFacadeLocal memberManager;

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

            //处理文件上传
            //创建路径（根据用户所在的行政区域）
            String tempPath=request.getSession().getServletContext().getRealPath("/")+File.separator+UPLOAD_TEMP;
            this.createPath(tempPath,"");
            //根据当前的用户区域设置上传路径
            this.fileUploader.setSavePath(UPLOAD_TEMP);

            // 执行上传并获取操作结果
            FileUploader.Result result=this.fileUploader.upload(request,response);
            // 检查操作结果
            if(FileUploader.Result.SUCCESS!=result)
            {
                // 设置 request attribute
                throw new ServletException(this.fileUploader.getCause().getMessage());
            }

            BusiMember member=null;
            //检查用户是否已经登录
            boolean isLogin=this.memberAction.isLogin(request);
            //如果用户未登录，执行登录处理
            if(!isLogin)
            {
                //获取是否指定要创建资源数据
                Map<String,String[]> params=this.fileUploader.getParamFields();
                String paramMember=params.get(BusiMemberServlet.PARAM_MEMBER)[0];
                String paramMemberPass=params.get(BusiMemberServlet.PARAM_MEMBER_PASS)[0];
                String paramMemberType=params.get(BusiMemberServlet.PARAM_MEMBER_TYPE)[0];
                if(this.paramNullCheck(paramMember)
                        ||this.paramNullCheck(paramMemberPass)
                        ||this.paramNullCheck(paramMemberType))
                {
                    throw new ServletException("缺少必须的参数！");
                }
                int loginType=Integer.parseInt(paramMemberType);

                member=this.memberManager.login(paramMember,paramMemberPass,loginType);
            }
            else
            {
                member=this.memberAction.getMemberUserInSession(request);
            }
            //如果用户已经登录，获取登录用户属性
            if(member==null)
            {
                throw new ServletException("会员未登录无法使用此接口！");
            }

            // 文件保存路径的列表
            String filePath=request.getSession().getServletContext().getRealPath("/")+File.separator+UPLOAD_PATH;
            if(this.createPath(filePath,member.getCustId()+""))
            {
                filePath+=File.separator+member.getCustId()+"";
            }
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
                            "%s%s%s",
                            this.fileUploader.getSavePath(),
                            File.separator,name
                    );
                    String linkPath=String.format("%s%s",this.fileUploader.getSavePath(),File.separator);
                    filePaths+=link;

                    //生成相关缩略图
                    String preName=name.substring(0,name.lastIndexOf("."));
                    String suffix=name.substring(name.lastIndexOf("."));
                    String thumbName=preName+"@thumb";
                    this.processor.setFile(tempPath,name)
                            .setHandleMode(ImageProcessor.ImageHandleMode.COMPRESS_AND_SCALE)
                            .setCompressMode(ImageProcessor.ImageCompressMode.CUSTEM)
                            //.setOutputQuality(1)
                            .setOutputPath(filePath)
                            .setOutputName(thumbName)
                            .setOutputSize(90,90)
                            .setScaleMode(ImageProcessor.ImageScaleMode.PROPORTION)
                            .toImageFile().createNewFile();

                    //更新用户信息
                    String memberHead=linkPath+thumbName+"."+suffix;
                    member.setMemberHead(memberHead);
                    boolean success=memberManager.edit(member);
                    if(!success)    throw new ServletException("更新会员头像信息失败！");
                }
            }

            //储存用户数据信息到Session中
            member=this.memberManager.find(member.getCustId());
            request.getSession().setAttribute
                    (BusiMemberServlet.SESSION_MEMBER_USER,member);

            //返回信息
            out.print("{\"success\":true,\"path\":\""+filePaths+"\",\"memberHead\":"+this.memberAction.getFullLink(request,member.getMemberHead())+"}");
        }
        catch(Exception ex)
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
     * 名称:    processRequest<br>
     * 描述:    处理来自HTTP访问的相关请求，调用DataAction接口进行数据的增删改查处理<br>
     *
     * @param request  - HTTP请求对象
     * @param response - HTTP输出对象
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void processRequest(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException
    {
        PrintWriter out=response.getWriter();
        BusiMember member=null;



        //处理文件上传
        if(member!=null)
        {

        }
    }

    /**
     * 方法（受保护）<br>
     * 名称:    initDataAction<br>
     * 描述:    初始化数据应用层接口<br>
     */
    @Override
    protected void initDataAction()
    {
        this.dataAction=new BusiMemberAction(this.connector);
        this.memberAction=(BusiMemberAction)this.dataAction;
        this.memberManager=new BusiMemberFacade(this.connector);
    }

    /**
     * 方法（受保护）<br>
     * 名称:    getLimitPluginId<br>
     * 描述:    获取当前功能对应的功能插件标识<br>
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
