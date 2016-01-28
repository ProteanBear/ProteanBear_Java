package pb.system.cms.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pb.data.Connector;
import pb.file.xml.XMLProductor;
import pb.json.JSONArray;
import pb.json.JSONObject;
import pb.pool.CachingServiceLocator;
import pb.system.cms.entity.BusiCmsArticle;
import pb.system.cms.entity.BusiCmsArticleGroup;
import pb.system.cms.manager.BusiCmsArticleFacade;
import pb.system.cms.manager.BusiCmsArticleFacadeLocal;
import pb.system.cms.manager.BusiCmsArticleGroupFacade;
import pb.system.cms.manager.BusiCmsArticleGroupFacadeLocal;
import pb.system.cms.manager.BusiCmsArticleGroupMapFacade;
import pb.system.cms.manager.BusiCmsArticleGroupMapFacadeLocal;
import pb.system.cms.manager.BusiCmsArticleResFacade;
import pb.system.cms.manager.BusiCmsArticleResFacadeLocal;
import pb.system.cms.module.BusiCmsArticleOutput;
import pb.system.cms.servlet.BusiCmsArticleServlet;
import pb.system.limit.action.AbstractAction;
import pb.system.limit.action.DataAction;
import pb.system.limit.entity.BusiResource;
import pb.system.limit.entity.BusiSection;
import pb.system.limit.entity.SystemApplication;
import pb.system.limit.manager.BusiResourceFacade;
import pb.system.limit.manager.BusiResourceFacadeLocal;
import pb.system.limit.manager.BusiSectionFacade;
import pb.system.limit.manager.BusiSectionFacadeLocal;
import pb.system.limit.manager.SystemApplicationFacade;
import pb.system.limit.manager.SystemApplicationFacadeLocal;
import pb.system.limit.module.LoginUser;
import pb.system.limit.servlet.AbstractServlet;
import pb.text.DateProcessor;

/**
 * 数据应用层类——记录文章内容相关信息
 *
 * @author ProteanBear
 * @version 1.00 2014-08-21
 */
public class BusiCmsArticleAction extends AbstractAction<BusiCmsArticle> implements DataAction
{
    /**
     * 域(受保护)<br/>
     * 名称: resMapManager<br/>
     * 描述: 文章对应资源的数据管理器对象<br/>
     */
    protected BusiCmsArticleResFacadeLocal resMapManager;

    /**
     * 域(受保护)<br/>
     * 名称: resManager<br/>
     * 描述: 资源的数据管理器对象<br/>
     */
    protected BusiResourceFacadeLocal resManager;

    /**
     * 域(受保护)<br/>
     * 名称: secManager<br/>
     * 描述: 业务栏目的数据管理器对象<br/>
     */
    protected BusiSectionFacadeLocal secManager;

    /**
     * 域(受保护)<br/>
     * 名称: appManager<br/>
     * 描述: 应用的数据管理器对象<br/>
     */
    protected SystemApplicationFacadeLocal appManager;

    /**
     * 域(受保护)<br/>
     * 名称: groupManager<br/>
     * 描述: 分组的数据管理器对象<br/>
     */
    protected BusiCmsArticleGroupFacadeLocal groupManager;

    /**
     * 域(受保护)<br/>
     * 名称: groupMapManager<br/>
     * 描述: 分组与文章对应的数据管理器对象<br/>
     */
    protected BusiCmsArticleGroupMapFacadeLocal groupMapManager;

    /**
     * 域(受保护)<br/>
     * 名称: dp<br/>
     * 描述: 时间日期管理器<br/>
     */
    protected DateProcessor dp;

    /**
     * 构造函数
     *
     * @param connector - 数据库连接器对象
     */
    public BusiCmsArticleAction(Connector connector)
    {
        super(connector,BusiCmsArticle.class);
    }

    /**
     * 方法（受保护）
     * 名称:initManager
     * 描述:初始化数据管理器
     */
    @Override
    protected void initManager()
    {
        this.manager=new BusiCmsArticleFacade(this.connector);
        this.resManager=new BusiResourceFacade(this.connector);
        this.resMapManager=new BusiCmsArticleResFacade(this.connector);
        this.secManager=new BusiSectionFacade(this.connector);
        this.appManager=new SystemApplicationFacade(this.connector);
        this.groupManager=new BusiCmsArticleGroupFacade(this.connector);
        this.groupMapManager=new BusiCmsArticleGroupMapFacade(this.connector);
        this.dp=new DateProcessor("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 方法（公共）<br/>
     * 名称: beforeCreate<br/>
     * 描述: 处理创建数据前的处理工作（多用于无主键生成器的数据表）<br/>
     *
     * @param request - HTTP请求对象
     * @param entity  - 构建的实体类
     * @return BusiCmsArticle - 处理后的实体类
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    @Override
    public BusiCmsArticle beforeCreate(HttpServletRequest request,BusiCmsArticle entity)
            throws ServletException
    {
        //创建时间
        entity.setArticleCreateTime(this.dp.getCurrent());

        //状态权限判断
        String status=request.getParameter("articleStatus");
        if(!this.paramNullCheck(status))
        {
            LoginUser user=this.getLoginUserInSession(request);
            if(!user.isHaveLimit("BUSI_CMS_ARTICLE",4))
            {
                entity.setArticleStatus(0);
            }
        }

        //自动更新内容
        entity=this.autoUpdateArticle(request,entity);

        return entity;
    }

    /**
     * 方法（受保护）<br/>
     * 名称: afterCreate<br/>
     * 描述: 处理创建数据成功后的处理工作（多用于多对多数据的创建）<br/>
     *
     * @param request   - HTTP请求对象
     * @param articleId - 创建后的文章标识
     * @throws javax.servlet.ServletException
     */
    @Override
    protected void afterCreate(HttpServletRequest request,String articleId)
            throws ServletException
    {
        //获取文章类型
        String typeStr=request.getParameter("articleType");
        if(this.paramNullCheck(typeStr))
        {
            int type=Integer.parseInt(typeStr);
            switch(type)
            {
                //文本类型
                case 0:
                    //图集类型
                case 1:
                    //视频类型
                case 2:
                {
                    //获取指定的文章资源标识数组
                    String[] artResources=request.getParameterValues(BusiCmsArticleServlet.PARAM_RESOURCE);

                    //类型判断
                    if(artResources!=null)
                    {
                        //遍历删除主键集
                        int successNum=0, errorNum=0;
                        String[] resArr;
                        boolean success;
                        for(String resource : artResources)
                        {
                            if(this.paramNullCheck(resource)) continue;
                            //生成JSON对象
                            JSONObject obj=new JSONObject(resource);
                            if(obj!=null)
                            {
                                JSONArray resources=obj.getJSONArray("resources");

                                //获取指定的资源
                                BusiResource newRes=null;
                                if(obj.get("resourceId")!=null)
                                {
                                    newRes=this.resManager.find(obj.getString("resourceId"));
                                    if(newRes==null)
                                    {
                                        throw new ServletException("未找到指定的资源数据信息！");
                                    }
                                    newRes.setResourceTitle(obj.getString("resourceTitle"));
                                    newRes.setResourceLink(obj.getString("resourceLink"));
                                    newRes.setDataRemark(obj.getString("dataRemark"));
                                    success=this.resManager.edit(newRes);
                                }
                                //创建分组信息
                                else
                                {
                                    throw new ServletException("指定的资源数据不存在！");
                                }
                                if(!success)
                                {
                                    throw new ServletException(this.groupManager.getError());
                                }

                                //创建分组对应文章数据
                                for(int i=0;i<resources.length();i++)
                                {
                                    success=this.resMapManager.create(
                                            obj.getString("articleId"),
                                            newRes.getResourceId()
                                    );
                                    if(success)
                                    {
                                        successNum++;
                                    }
                                    else
                                    {
                                        errorNum++;
                                    }
                                }
                            }
                        }

                        //处理结果
                        if(errorNum!=0)
                        {
                            throw new ServletException(
                                    "成功创建对应文章资源数据"+successNum+"个,"
                                            +"失败"+(errorNum)+"个"
                            );
                        }
                    }
                    break;
                }
                //专题类型
                case 3:
                {
                    //获取指定的专题文章内容标识数组
                    String[] artGroups=request.getParameterValues(BusiCmsArticleServlet.PARAM_GROUPS);
                    if(artGroups!=null)
                    {
                        //遍历删除主键集
                        int successNum=0, errorNum=0;
                        boolean success;
                        for(String group : artGroups)
                        {
                            if(this.paramNullCheck(group)) continue;
                            //生成JSON对象
                            JSONObject obj=new JSONObject(group);
                            if(obj!=null)
                            {
                                JSONArray arts=obj.getJSONArray("aritcles");

                                //获取指定的分组
                                BusiCmsArticleGroup newGroup=null;
                                if(obj.get("groupId")!=null)
                                {
                                    newGroup=this.groupManager.find(obj.getString("groupId"));
                                    if(newGroup==null)
                                    {
                                        throw new ServletException("未找到指定的专题分组信息！");
                                    }
                                    newGroup.setArticleId(obj.getString("articleId"));
                                    newGroup.setGroupName(obj.getString("groupName"));
                                    newGroup.setGroupType(obj.getInt("groupType"));
                                    success=this.groupManager.edit(newGroup);
                                }
                                //创建分组信息
                                else
                                {
                                    newGroup=new BusiCmsArticleGroup();
                                    newGroup.setArticleId(obj.getString("articleId"));
                                    newGroup.setGroupName(obj.getString("groupName"));
                                    newGroup.setGroupType(obj.getInt("groupType"));
                                    success=this.groupManager.create(newGroup);
                                }
                                if(!success)
                                {
                                    throw new ServletException(this.groupManager.getError());
                                }

                                //创建分组对应文章数据
                                for(int i=0;i<arts.length();i++)
                                {
                                    success=this.groupMapManager.create(
                                            obj.getString("articleId"),
                                            this.groupManager.getLastGenerator()
                                    );
                                    if(success)
                                    {
                                        successNum++;
                                    }
                                    else
                                    {
                                        errorNum++;
                                    }
                                }
                            }
                        }

                        //处理结果
                        if(errorNum!=0)
                        {
                            throw new ServletException(
                                    "成功创建对应分组文章数据"+successNum+"个,"
                                            +"失败"+(errorNum)+"个"
                            );
                        }
                    }
                    break;
                }
                default:
                {
                    break;
                }
            }
        }
    }

    /**
     * 方法（受保护）<br/>
     * 名称: beforeEdit<br/>
     * 描述: 处理编辑数据前的处理工作（多用于多对多数据的创建）<br/>
     *
     * @param request - HTTP请求对象
     * @param entity  - 构建的实体类
     * @return T - 处理后的实体类
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    @Override
    protected BusiCmsArticle beforeEdit(HttpServletRequest request,BusiCmsArticle entity)
            throws ServletException
    {
        //获取原始数据
        String primaryKey=request.getParameter(AbstractServlet.PARAM_PRIMARYKEY);
        BusiCmsArticle article=this.manager.find(primaryKey);

        //状态权限判断
        String status=request.getParameter("articleStatus");
        if(!this.paramNullCheck(status))
        {
            LoginUser user=this.getLoginUserInSession(request);
            if(!user.isHaveLimit("BUSI_CMS_ARTICLE",4))
            {
                entity.setArticleStatus(article.getArticleStatus());
            }
        }

        //自动更新内容
        entity=this.autoUpdateArticle(request,entity);

        //缩略图判断及自动生成
        String autoStr=request.getParameter(BusiCmsArticleServlet.PARAM_AUTO_IMAGE);
        boolean isAuto=true;
        try
        {
            isAuto=this.paramNullCheck(autoStr)
                    ?CachingServiceLocator.getInstance().getBoolean(AbstractServlet.ENV_CONFIG_AUTO_IMAGE)
                    :Boolean.parseBoolean(autoStr);
        }
        catch(NamingException ex)
        {
        }
        //获取默认图片对象
        if(this.paramNullCheck(entity.getArticleImageTitle()) && isAuto)
        {
            entity=this.generateImageTitle(entity);
        }

        return entity;
    }

    /**
     * 方法（受保护）<br/>
     * 名称: afterEdit<br/>
     * 描述: 处理编辑数据成功后的处理工作（多用于多对多数据的修改）<br/>
     *
     * @param request - HTTP请求对象
     * @throws javax.servlet.ServletException
     */
    @Override
    protected void afterEdit(HttpServletRequest request)
            throws ServletException
    {
        //获取文章标识
        String articleId=request.getParameter(BusiCmsArticleServlet.PARAM_PRIMARYKEY);

        //获取文章类型
        String typeStr=request.getParameter("articleType");
        if(this.paramNullCheck(typeStr))
        {
            int type=Integer.parseInt(typeStr);
            switch(type)
            {
                //文本类型
                case 0:
                {
                    break;
                }
                //图集类型
                case 1:
                    //视频类型
                case 2:
                {
                    this.resMapManager.removeByArticleId(articleId);
                    break;
                }
                //专题类型
                case 3:
                {
                    this.groupMapManager.removeByArticleId(articleId);
                    break;
                }
                default:
                {
                    break;
                }
            }
        }

        //重新创建企业类型对应的插件
        this.afterCreate(request,articleId);
    }

    /**
     * 方法（公共） 名称： findToXml 描述: 查询数据，返回Xml结果<br/>
     *
     * @param request - HTTP请求对象
     * @return String - 获取成功返回XML格式的列表结果
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    @Override
    public String findToXml(HttpServletRequest request)
            throws ServletException
    {
        //查询数据
        List list=this.findTo(request);
        list=this.generateOutput(request,list);

        int pageSize=this.manager.getPageSize();
        int page=this.currentPage;
        int total=this.manager.getTotalCount();
        int totalPage=this.manager.getTotalPage();

        return XMLProductor.toXmlString(list,pageSize,page,total,totalPage);
    }

    /**
     * 方法（公共） 名称： findToJson 描述: 查询数据，返回Json结果<br/>
     *
     * @param request - HTTP请求对象
     * @return String - 获取成功返回JSON格式的列表结果
     * @throws javax.servlet.ServletException - 抛出处理错误
     */
    @Override
    public String findToJson(HttpServletRequest request)
            throws ServletException
    {
        //查询数据
        List list=this.findTo(request);
        list=this.generateOutput(request,list);
        boolean isOut=this.isCurrentUseNoLoginMode(request);

        //生成结果
        JSONObject json=new JSONObject();
        json.put("success",true);
        json.put("pageSize",this.manager.getPageSize());
        json.put("page",this.currentPage);
        json.put("total",this.manager.getTotalCount());
        json.put("totalPage",this.manager.getTotalPage());

        //列表内容
        JSONArray array=new JSONArray();
        for(Object obj : list)
        {
            BusiCmsArticleOutput article=(BusiCmsArticleOutput)obj;
            JSONObject artJson=new JSONObject();

            //输出通用内容
            artJson.put("articleId",article.getArticleId());
            artJson.put("sectionCode",article.getSectionCode());
            artJson.put("articleTitle",article.getArticleTitle());
            artJson.put("articleType",article.getArticleType());
            artJson.put("articleImageTitle",this.getFullLink(request,article.getArticleImageTitle()));
            artJson.put("articleImageFocus",this.getFullLink(request,article.getArticleImageFocus()));
            artJson.put("articleSummary",article.getArticleSummary());
            artJson.put("articleKeywords",article.getArticleKeywords());
            artJson.put("articleSource",article.getArticleSource());
            artJson.put("articleAuthor",article.getArticleAuthor());
            if(!isOut) artJson.put("articleStatus",article.getArticleStatus());
            if(!isOut) artJson.put("articleUpdateTime",article.getArticleUpdateTime());
            if(!isOut) artJson.put("articleCanComment",article.getArticleCanComment());
            if(!isOut) artJson.put("articleIsFocus",article.getArticleIsFocus());
            if(!isOut) artJson.put("articleIsTop",article.getArticleIsTop());
            artJson.put("articleTitleSub",article.getArticleTitleSub());
            artJson.put("articleReleaseTime",article.getArticleReleaseTime());

            //查询列表和内容时输出内容不同（即list数据量来判断）
            //查询内容时输出
            if(list.size()<2)
            {
                artJson.put("articleContent",article.getArticleContent());
                if(!isOut) artJson.put("articleSourceType",article.getArticleSourceType());
                artJson.put("articleHtml",article.getArticleHtml());
                if(!isOut) artJson.put("articleCreateTime",article.getArticleCreateTime());
            }
            //查询列表时输出
            else
            {
                if(!isOut) artJson.put("articleSort",article.getArticleSort());
                if(!isOut) artJson.put("articleCountPush",article.getArticleCountPush());
                if(!isOut) artJson.put("articleCountShare",article.getArticleCountShare());
                if(!isOut) artJson.put("articleCountRead",article.getArticleCountRead());
                if(!isOut) artJson.put("articleCountFavorite",article.getArticleCountFavorite());
                if(!isOut) artJson.put("articleCountComment",article.getArticleCountComment());
            }

            //根据不同的文章类型输出相应的附件内容
            int type=article.getArticleType();
            int i=1, max=5;
            try
            {
                max=CachingServiceLocator.getInstance()
                        .getInteger(AbstractServlet.ENV_CONFIG_MAXRES);
            }
            catch(NamingException ex)
            {
                max=5;
            }
            switch(type)
            {
                //文本文章（不带任何附件）
                case 0:
                {
                    break;
                }
                //图集文章（数组类型：附带资源图片信息，列表输出显示有限数量，内容输出显示全部）
                case 1:
                {
                    JSONArray imgs=new JSONArray();
                    List<BusiResource> attachs=article.getArticleResource();
                    if(attachs!=null)
                    {
                        for(Object resObj : attachs)
                        {
                            BusiResource res=(BusiResource)resObj;
                            JSONObject img=new JSONObject();
                            img.put("resourceId",res.getResourceId());
                            img.put("resourceTitle",res.getResourceTitle());
                            img.put(
                                    "resourceThumb",this.getFullLink(
                                            request,
                                            res.resourceIsOut()?"":(res.getResourceLink()+res.getResourceThumb()+res
                                                    .getResourceSuffix())
                                    )
                            );
                            img.put(
                                    "resourceMiddle",this.getFullLink(
                                            request,
                                            res.resourceIsOut()?"":(res.getResourceLink()+res.getResourceMiddle()+res
                                                    .getResourceSuffix())
                                    )
                            );
                            img.put("resourceIsOut",res.getResourceIsOut());
                            img.put(
                                    "resourceLink",this.getFullLink(
                                            request,
                                            res.getResourceLink()+(res.resourceIsOut()?"":(res.getResourceFile()+res
                                                    .getResourceSuffix()))
                                    )
                            );
                            img.put("resourceType",res.getResourceType());
                            img.put("dataRemark",res.getDataRemark());
                            imgs.put(img);
                            if((++i>max) && list.size()>1)
                            {
                                break;
                            }
                        }
                        artJson.put("attachments",imgs);
                        artJson.put("attachCount",attachs.size());
                        break;
                    }
                    break;
                }
                //视频文章（加入的视频文件资源）
                case 2:
                {
                    JSONArray imgs=new JSONArray();
                    if(article.getArticleResource()!=null)
                    {
                        for(Object resObj : article.getArticleResource())
                        {
                            BusiResource res=(BusiResource)resObj;
                            JSONObject img=new JSONObject();
                            img.put("resourceId",res.getResourceId());
                            img.put("resourceTitle",res.getResourceTitle());
                            img.put("resourceThumb",this.getFullLink(request,res.getResourceThumb()));
                            img.put(
                                    "resourceMiddle",this.getFullLink(
                                            request,
                                            res.resourceIsOut()?"":(res.getResourceLink()+res.getResourceMiddle()+res
                                                    .getResourceSuffix())
                                    )
                            );
                            img.put("resourceIsOut",res.getResourceIsOut());
                            img.put(
                                    "resourceLink",this.getFullLink(
                                            request,
                                            res.getResourceLink()+(res.resourceIsOut()?"":(res.getResourceFile()+res
                                                    .getResourceSuffix()))
                                    )
                            );
                            img.put("resourceType",res.getResourceType());
                            img.put("dataRemark",res.getDataRemark());
                            imgs.put(img);
                            if((++i>max) && list.size()>1)
                            {
                                break;
                            }
                        }
                        artJson.put("attachments",imgs);
                        break;
                    }
                    break;
                }
                //专题文章（数组类型：增加专题文章关联文章的列表）
                case 3:
                {
                    JSONArray arts=new JSONArray();
                    if(article.getSubArticles()!=null)
                    {
                        for(Object artObj : article.getSubArticles())
                        {
                            BusiCmsArticle art=(BusiCmsArticle)artObj;
                            JSONObject sub=new JSONObject();
                            sub.put("articleId",art.getArticleId());
                            sub.put("sectionCode",art.getSectionCode());
                            sub.put("articleTitle",art.getArticleTitle());
                            sub.put("articleType",art.getArticleType());
                            sub.put("articleImageTitle",this.getFullLink(request,art.getArticleImageTitle()));
                            sub.put("articleImageFocus",this.getFullLink(request,art.getArticleImageFocus()));
                            sub.put("articleSummary",art.getArticleSummary());
                            sub.put("articleCanComment",art.getArticleCanComment());
                            arts.put(sub);
                            if((++i>max) && list.size()>1)
                            {
                                break;
                            }
                        }
                        artJson.put("attachments",arts);
                    }
                    break;
                }
                //其他不带任何附件）
                default:
                {
                    break;
                }
            }

            array.put(artJson);
        }

        json.put("list",array);
        return json.toString();
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    isUseTransaction<br/>
     * 描述:    是否使用事务处理机制<br/>
     *
     * @return boolean - 是否使用事务处理机制
     */
    @Override
    protected boolean isUseTransaction()
    {
        return true;
    }

    /**
     * 方法（受保护）<br/>
     * 名称: generateCondition<br/>
     * 描述: 通过请求参数生成相应查询条件。<br/>
     * 参数格式为：searchValue=con1,value1|con2,value2|.....
     *
     * @param request - HTTP请求对象
     * @return Map<String,Object> - 查询条件集合
     * @throws javax.servlet.ServletException
     */
    @Override
    protected Map<String,Object> generateCondition(HttpServletRequest request)
            throws ServletException
    {
        Map<String,Object> condition=super.generateCondition(request);

        //参数判断
        String priKey=request.getParameter(AbstractServlet.PARAM_PRIMARYKEY);
        String sectionCode=request.getParameter(BusiCmsArticleServlet.PARAM_SECTION);
        if(!this.paramNullCheck(priKey))
        {
            condition.put("articleId=?",priKey);
        }
        else
        {
            //必须指定栏目标识
            if(this.paramNullCheck(sectionCode))
            {
                throw new ServletException("未指定文章所属的栏目标识！");
            }
        }

        //后台访问
        if(!this.isCurrentUseNoLoginMode(request))
        {
            condition.put("sectionCode like ?",sectionCode+"%");
            //必须指定所属应用
            String sectionApp=request.getParameter(BusiCmsArticleServlet.PARAM_APP);
            if(this.paramNullCheck(sectionApp))
            {
                throw new ServletException("未指定数据所属的应用标识！");
            }
            //获取应用的相关信息
            BusiSection section=this.secManager.findBySectionCode(sectionApp,sectionCode);

            //获取应用的属性信息
            SystemApplication app=this.getAppByUserRound(request,sectionApp);
            if(app==null)
            {
                throw new ServletException("指定应用不存在，或者超出用户权限范围！");
            }

            //比较应用标识和栏目范围
            if(!this.paramNullCheck(app.getAppCode())
                    && !app.getAppCode().equals(section.getSectionApp()))
            {
                throw new ServletException("指定应用与栏目标识不匹配！");
            }
            condition.put("appCode like ?",sectionApp);
        }
        //外部接口
        else
        {
            //必须指定所属应用
            String app=request.getParameter(AbstractServlet.PARAM_FROMAPP);
            if(this.paramNullCheck(app))
            {
                throw new ServletException("未指定数据所属的应用标识！");
            }
            condition.put("appCode like ?",app);
            if(this.paramNullCheck(priKey)) condition.put("sectionCode=?",sectionCode);
            //增加已发布限制
            condition.put("articleStatus=?",2);
        }

        return condition;
    }

    /**
     * 方法（受保护）<br/>
     * 名称: generateOutput<br/>
     * 描述: 用于对查询的数据进行处理，增加新的字段内容（多用于多对多数据的输出）<br/>
     *
     * @param request - HTTP请求对象
     * @param list    - 数据对象列表
     * @return List - 处理后的数据对象列表
     */
    @Override
    protected List<Object> generateOutput(HttpServletRequest request,List<BusiCmsArticle> list)
    {
        List<Object> result=null;
        if(list!=null)
        {
            result=new ArrayList<>();
            for(BusiCmsArticle article : list)
            {
                //文章输出对象
                BusiCmsArticleOutput output=new BusiCmsArticleOutput(article);
                //文章类型
                int type=output.getArticleType();

                switch(type)
                {
                    //普通文章
                    case 0:
                    {
                        //单条数据输出富文本内容
                        output.setArticleContent(list.size()>1?"":article.getArticleContent());
                        break;
                    }
                    //图集文章
                    case 1:
                    {
                        //清空富文本
                        output.setArticleContent("");
                        //获取图集对应的资源地址
                        output.setArticleResource(this.resManager.findByArticleId(output.getArticleId()));
                        break;
                    }
                    //视频文章
                    case 2:
                    {
                        //单条数据输出富文本内容
                        output.setArticleContent(list.size()>1?"":article.getArticleContent());
                        //获取图集对应的资源地址
                        output.setArticleResource(this.resManager.findByArticleId(output.getArticleId()));
                        break;
                    }
                    //专题文章
                    case 3:
                    {
                        //清空富文本
                        output.setArticleContent("");
                        //获取分栏和专辑中文章列表数据
                        output.setSubArticles(
                                ((BusiCmsArticleFacadeLocal)this.manager)
                                        .findTopicByArticleId(output.getArticleId())
                        );
                        break;
                    }
                }

                result.add(output);
            }
        }
        return result;
    }

    /**
     * 方法（受保护）<br/>
     * 名称: autoUpdateArticle<br/>
     *
     * @param request - HTTP请求对象
     * @param entity  - 数据对象
     * @return BusiCmsArticle - 更新后数据对象
     * @throws javax.servlet.ServletException
     */
    protected BusiCmsArticle autoUpdateArticle(HttpServletRequest request,BusiCmsArticle entity)
            throws ServletException
    {
        //更新时间
        entity.setArticleUpdateTime(this.dp.getCurrent());

        //自动生成作者
        if(this.paramNullCheck(entity.getArticleAuthor()))
        {
            LoginUser user=this.getLoginUserInSession(request);
            entity.setArticleAuthor(user.getUserName());
        }

        //自动生成来源
        if(this.paramNullCheck(entity.getArticleSource()))
        {
            String appCode=request.getParameter(BusiCmsArticleServlet.PARAM_APP);
            if(this.paramNullCheck(appCode))
            {
                throw new ServletException("未指定数据所属的应用标识！");
            }
            //获取应用的属性信息
            SystemApplication app=this.getAppByUserRound(request,appCode);
            if(app==null)
            {
                throw new ServletException("指定应用不存在，或者超出用户权限范围！");
            }
            entity.setArticleSource(app.getAppName());
        }

        //自动生成简介
        if(this.paramNullCheck(entity.getArticleSummary()))
        {
            if(!this.paramNullCheck(entity.getArticleContent()))
            {
                entity.setArticleSummary(this.generateSummary(entity.getArticleContent()));
            }
        }

        //自动生成标签
        if(this.paramNullCheck(entity.getArticleKeywords()))
        {
            try
            {
                entity.setArticleKeywords(this.generateKeywords(entity.getArticleSource(),entity.getArticleTitle()));
            }
            catch(IOException ex)
            {
            }
        }

        return entity;
    }

    /**
     * 方法（受保护）<br/>
     * 名称: getAppByUserRound<br/>
     *
     * @param request    - HTTP请求对象
     * @param sectionApp - 应用标识
     * @return SystemApplication -
     * @throws javax.servlet.ServletException
     */
    protected SystemApplication getAppByUserRound
    (HttpServletRequest request,String sectionApp)
            throws ServletException
    {
        Map<String,Object> appCondition=new HashMap<>();
        LoginUser user=this.getLoginUserInSession(request);
        appCondition.putAll(user.generateAreaCondition("areaId",user.getAreaId()));
        appCondition.put("appCode=?",sectionApp);
        List<SystemApplication> appList=this.appManager.find(appCondition);
        SystemApplication app=(appList!=null && !appList.isEmpty())?appList.get(0):null;
        return app;
    }

    /**
     * 方法（受保护）<br/>
     * 名称: generateSummary<br/>
     *
     * @param content - 文章内容
     * @return String - 简介内容
     */
    protected String generateSummary(String content)
    {
        //解析HTML
        Document doc=Jsoup.parseBodyFragment(content);
        Elements pList=doc.select("p");

        //获取简介
        String result="";
        for(Element p : pList)
        {
            result=p.text();
            if(!this.paramNullCheck(result))
            {
                break;
            }
        }

        //截取长度
        int max=250;
        int length=Math.min(result.length(),max);
        result=result.substring(0,length);
        result+=(length==max?"...":"");

        return result;
    }

    /**
     * 方法（受保护）<br/>
     * 名称: generateImageTitle<br/>
     *
     * @param entity - 实体对象
     * @return BusiCmsArticle - 修改后的对象
     */
    protected BusiCmsArticle generateImageTitle(BusiCmsArticle entity)
    {
        String linkPath="";

        //有焦点图，则默认图片为焦点图
        if(!this.paramNullCheck(entity.getArticleImageFocus()))
        {
            linkPath=entity.getArticleImageFocus();
        }
        //没有焦点图，根据类型处理
        else
        {
            int type=entity.getArticleType();
            switch(type)
            {
                //文本类型：使用文本内容中的第一张图片为默认图片
                case 0:
                {
                    break;
                }
                //图集类型：使用图集中的主图为默认图片，未设置主图则为第一张
                case 1:
                {
                    break;
                }
                //视频类型：使用视频内容的首张截图为默认图片
                case 2:
                {
                    break;
                }
                //专题类型：使用第一篇文章的标题图为默认标题图
                case 3:
                {
                    break;
                }
                default:
                {
                    break;
                }
            }
        }
        return entity;
    }

    /**
     * 方法（受保护）<br/>
     * 名称: generateKeywords<br/>
     *
     * @param source - 来源
     * @param title  - 标题
     * @return String - 分词结果
     * @throws java.io.IOException
     */
    protected String generateKeywords(String source,String title) throws IOException
    {
        String result="";

        //创建分词对象  
        /*Analyzer anal = new IKAnalyzer(true);
        //分词
        try (StringReader reader = new StringReader(title))
        {
            //分词
            TokenStream ts = anal.tokenStream("", reader);
            CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
            //遍历分词数据
            while (ts.incrementToken())
            {
                result += term.toString() + " ";
            }
        }

        //添加来源
        result += " " + source;*/

        return result;
    }

    /**
     * 方法（受保护）<br/>
     * 名称:    supportNoLoginFind<br/>
     * 描述:    是否支持非登录下查询处理<br/>
     *
     * @return boolean - 如果参数值为null或""，返回true
     */
    @Override
    public boolean supportNoLoginFind()
    {
        return true;
    }
}
