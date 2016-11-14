/**
 * 脚本说明：	数据格式配置-busiSection
 * 脚本作者：     ProteanBear
 * 当前版本：	1.0 2014
 */

/*初始化命名空间*/
var urlconfig = urlconfig || {};
var plugins=plugins||{};

/**
 * 设置SystemPlugin相关的数据配置
 * @param urlconfig - 
 */
(function(urlconfig,plugins) {
    /*获取本地语言设置*/
    var local=parent.parent.local||parent.local;
    var editUrl=["text","album","video","topic"];
    var curIndex=parent.index||index;
    var console=parent.parent.console||parent.console;
    
    //自定义文章列表项显示模板
    var artItems=template.compile(''
            //循环显示列表项
            +'<!--[for(var i=0;i<data.length;i++)]-->'
            +'<!--[{]-->'
                    +'<tr>'
                        +'<td><input type="checkbox"></td>'
                        +'<td class="td-header">'
                            //仅浏览模式下显示
                            +'<!--[if(mode===1&&!data[i].ariticleIsFocus){]-->'
                                //标题图
                                +'<!--[if(data[i].articleImageTitle!==""){]--><img class="img-title" src="<!--[=data[i].articleImageTitleFull]-->" />'
                                    //播放按钮（视频类型）
                                    +'<!--[if(data[i].articleType===2){]-->'
                                        +'<p class="video-play">'
                                            +'<span class="background"></span>'
                                            +'<span class="play glyphicon glyphicon-play-circle"></span>'
                                        +'</p>'
                                    +'<!--[}]-->'
                                +'<!--[}]-->'
                            +'<!--[}]-->'
                            //标题内容（全部模式下都显示）
                            +'<p class="title">'
                                //是否焦点
                                +'<!--[if(data[i].ariticleIsFocus){]--><span class="glyphicon glyphicon-star"></span><!--[}]-->'
                                //是否置顶
                                +'<!--[if(data[i].ariticleIsTop){]--><span class="glyphicon glyphicon-pushpin"></span><!--[}]-->'
                                //文章类型
                                +'<span class="glyphicon glyphicon-<!--[=local.source.typeIcons[data[i].articleType]]-->"></span>'
                                //图片数量（图集类型）
                                +'<!--[if(data[i].articleType===1){]--><span class="small">(<!--[=data[i].attachCount]-->)</span><!--[}]-->'
                                //文章标题
                                +'<span><a href="#"><!--[=data[i].articleTitle]--></a></span>'
                                //文章状态
                                +'<!--[if(data[i].articleStatus<2){]-->-<span class="glyphicon glyphicon-<!--[=local.source.statusIcons[data[i].articleStatus]]-->"></span><!--[=local.articleStatus[data[i].articleStatus]]--><!--[}]-->'
                            +'</p>'
                            //简介（仅浏览模式下显示）
                            +'<!--[if(mode===1){]-->'
                                +'<!--[if(data[i].articleSummary!==""){]--><p><!--[=data[i].articleSummary]--></p><!--[}]-->'
                            +'<!--[}]-->'
                            //焦点图（浏览模式下的焦点文章或图片模式下）
                            +'<!--[if((mode===2)||(mode===1&&data[i].ariticleIsFocus)){]-->'
                                +'<p class="image-focus">'
                                    +'<img class="img-title" src="<!--[=data[i].ariticleIsFocus?data[i].articleImageFocusFull:data[i].articleImageTitleFull]-->" />'
                                +'</p>'
                                +'<!--[if(data[i].articleType===2){]-->'
                                    +'<p class="video-play-focus">'
                                        +'<span class="background"></span>'
                                        +'<span class="play glyphicon glyphicon-play-circle"></span>'
                                    +'</p>'
                                +'<!--[}]-->'
                            +'<!--[}]-->'
                            //图集多图（仅在浏览模式下）
                            +'<!--[if(mode===1&&data[i].articleType==1){]-->'
                                +'<p class="images">'
                                    +'<!--[for(var j=0;j<data[i].attachments.length;j++){]-->'
                                        +'<img class="img-title" src="<!--[=data[i].attachments[j].resourceThumb]-->" />'
                                    +'<!--[}]-->'
                                +'</p>'
                            +'<!--[}]-->'
                            //操作按钮（全部模式下显示）
                            +'<p class="btn-group btn-group-xs">'
                                //编辑权限
                                +'<!--[if(limit.edit){]-->'
                                    //编辑
                                    +'<a class="btn btn-default" href="#" '
                                            +'load="sub_article_edit_<!--[=editUrl[data[i].articleType]]-->.html?active=BUSI_CMS_ARTICLE'
                                            +'&mode=edit&sectionName=<!--[=sectionName]-->&sectionCode=<!--[=sectionCode]-->'
                                            +'&appCode=<!--[=appCode]-->&id=<!--[=data[i].articleId]-->">'
                                        +'<span class="glyphicon glyphicon-edit"></span><!--[=local.action["edit"]]-->'
                                    +'</a>'
                                    //快速编辑
                                    +'<button type="button" class="btn btn-default">'
                                        +'<span class="glyphicon glyphicon-pencil"></span><!--[=local.action["quickEdit"]]-->'
                                    +'</button>'
                                +'<!--[}]-->'
                                //删除权限
                                +'<!--[if(limit.remove){]-->'
                                    //删除
                                    +'<button action-mode="data-remove" name="BUSI_CMS_ARTICLE" data-index="<!--[=i]-->" title="<!--[=local.alert_infor["confirm"]+local.name_article+"【"+data[i].articleTitle+"】"+local.alert_infor["firmend"]]-->" type="button" class="btn btn-default">'
                                        +'<span class="glyphicon glyphicon-trash"></span><!--[=local.action["remove"]]-->'
                                    +'</button>'
                                +'<!--[}]-->'
                                //编辑权限
                                +'<!--[if(limit.edit){]-->'
                                    //移动
                                    +'<button type="button" class="btn btn-default">'
                                        +'<span class="glyphicon glyphicon-move"></span><!--[=local.action["move"]]-->'
                                    +'</button>'
                                +'<!--[}]-->'
                                //创建权限
                                +'<!--[if(limit.insert){]-->'
                                    //移动
                                    +'<button type="button" class="btn btn-default">'
                                        +'<span class="glyphicon glyphicon-file"></span><!--[=local.action["copy"]]-->'
                                    +'</button>'
                                +'<!--[}]-->'
                                //查询权限
                                +'<!--[if(limit.list){]-->'
                                    //预览
                                    +'<button type="button" class="btn btn-default">'
                                        +'<span class="glyphicon glyphicon-eye-open"></span><!--[=local.action["preview"]]-->'
                                    +'</button>'
                                    //推送
                                    +'<button type="button" class="btn btn-default">'
                                        +'<span class="glyphicon glyphicon-export"></span><!--[=local.action["push"]]-->'
                                    +'</button>'
                                +'<!--[}]-->'
                            +'</p>'
                        +'</td>'
                        +'<td><!--[=data[i].articleAuthor]--></td>'
                        +'<td><!--[=data[i].articleKeywords]--></td>'
                        +'<td></td>'
                        +'<td><!--[=data[i].articleUpdateTime+" "+local.tips["lastUpdate"]]--></td>'
                    +'</tr>'
            +'<!--[}]-->' 
        );
    //图集文章图片显示模板-缩略方式
    var imgsSmall=template.compile(
        '<!--[if(data){for(var i=0;i<data.attachments.length;i++){]-->' +
        '<li>' +
            '<div>' +
                '<img id="resourceThumb" name="resourceThumb" alt="" src="<!--[=data.attachments[i].resourceThumb]-->">' +
                '<div class="text">' +
                    '<div class="inner">' +
                        '<p><input id="resourceTitle" name="resourceTitle" value="<!--[=data.attachments[i].resourceTitle]-->" placeholder="<!--[=local.placeholder["albumTitle"]]-->"></p>' +
                        '<p><textarea id="dataRemark" name="dataRemark" placeholder="<!--[=local.placeholder["albumSummary"]]-->"><!--[=data.attachments[i].dataRemark]--></textarea>' +
                    '</div>' +
                '</div>' +
            '</div>' +
            '<div class="tools tools-bottom">' +
                '<a href="#"><i class="glyphicon glyphicon-pushpin"></i></a>' +
                '<a href="#"><i class="glyphicon glyphicon-link"></i></a>' +
                '<a href="#"><i class="glyphicon glyphicon-trash"></i></a>' +
            '</div>' +
        '</li>' +
        '<!--[}}]-->'
    );
    
    //页面数据载入设置
    urlconfig.type = "multi";
    urlconfig.initKey = "BUSI_CMS_ARTICLE";
    //文章接口
    urlconfig["BUSI_CMS_ARTICLE"] = {
        url: "../busiCmsArticle",
        to:"dataList",
        key:"articleId",
        keyTitle:"articleTitle",
        title:local.name_article,
        limitId:"BUSI_CMS_ARTICLE",
        noUpdate:true,
        extra:{
            "sectionCode":function(){return curIndex.getSubUrlParam("sectionCode");},
            "appCode":function(){return curIndex.getSubUrlParam("appCode");},
            "searchValue":function()
            {
                var result="";
                //增加id标识
                var articleId=curIndex.getSubUrlParam("id");
                !articleId||(result+="articleId=?;"+articleId);
                //增加筛选获取
                var articleStatus=curIndex.getSubUrlParam("articleStatus");
                !articleStatus||articleStatus==-1||(result+=((result==""?"":"|")+"articleStatus=?;"+articleStatus));
                //增加搜索关键字
                var articleSearch=$("#searchValue").val();
                !articleSearch||articleSearch==""||(result+=((result==""?"":"|")+"article_title like ?;"+"%"+articleSearch+"%"));
                return result;
            }
        },
        access:{"mode":function(){return curIndex.getSubUrlParam("mode");}},
        success:function(local,data,limit,access,activeId,response){
            var mode=urlconfig["BUSI_CMS_ARTICLE"].access["mode"]()||"list";
            var sectionName=curIndex.getSubUrlParam("sectionName");
            var sectionCode=curIndex.getSubUrlParam("sectionCode");
            var appCode=curIndex.getSubUrlParam("appCode");
            limit.check=console.haveLimit("BUSI_CMS_ARTICLE","04");
            (mode==="edit")?
                //显示内容
                $(".main").append(template("template_main",{
                    local:local,
                    data:data.length>0?data[0]:null,
                    limit:limit,
                    sectionName:sectionName,
                    sectionCode:sectionCode,
                    appCode:appCode,
                    id:curIndex.getSubUrlParam("id")
                }))
                //显示列表
                :($("#dataList").html(artItems({
                    local:local,
                    data:data,
                    limit:limit,
                    sectionName:sectionName,
                    sectionCode:sectionCode,
                    appCode:appCode,
                    mode:1,
                    editUrl:editUrl}))
                ,
                //显示页码
                $(".pagination-right").html(mt.pagination({data: response, max: 5, dis: 2,name:"BUSI_CMS_ARTICLE"}))
            );
            
            //显示统计
            if(mode!="edit"&&response&&response.statistics)
            {
                //当前状态
                var articleStatus=curIndex.getSubUrlParam("articleStatus")||-1;
                for(var status in local.checkType)
                {
                    $("#statusNumber-"+status).html(response.statistics[status]);
                    !articleStatus||articleStatus!=status||(
                        $("#statusNumber-"+status).parent().removeAttr("load"),
                        $("#statusNumber-"+status).parent().addClass("disabled")
                    );
                }
            }
        },
        customUpdate:function(params,data){
            var sectionName=curIndex.getSubUrlParam("sectionName");
            var sectionCode=curIndex.getSubUrlParam("sectionCode");
            var appCode=curIndex.getSubUrlParam("appCode");
            var url=(params["handleAfter"]==="0")?
                    //0：创建同类型文章
                    ($("#contentFrame").attr("src"))
                    //1：编辑新创建的文章
                    :(params["handleAfter"]==="1"?("sub_article_edit_"+editUrl[params.articleType]+".html?active=BUSI_CMS_ARTICLE&mode=edit&sectionName="+sectionName+"&sectionCode="+sectionCode+"&appCode="+appCode+"&id="+data.infor)
                    //2：返回文章列表
                    :"sub_articles.html?active=BUSI_CMS_ARTICLE&sectionName="+sectionName+"&sectionCode="+sectionCode+"&appCode="+appCode);
        },
        property:{
            articleId:{type:"hidden"},
            sectionCode:{type:"hidden"},
            articleTitle:{type:"input",must:true},
            articleTitleSub:{type:"input"},
            articleContent:{type:"ckeditor"},
            articleStatus:{type:"radio"},
            articleReleaseTime:{type:"input"},
            articleSort:{type:"checkbox"},
            articleIsFocus:{type:"checkbox"},
            articleCanCommit:{type:"checkbox"},
            articleSummary:{type:"text"},
            articleKeywords:{type:"input"},
            articleImageTitle:{type:"hidden"},
            articleImageFocus:{type:"hidden"},
            handleAfter:{type:"radio"}
        }
    };
    //文章分组接口
    urlconfig["BUSI_CMS_ARTICLE_GROUP"] = {
        url: "../busiCmsArticleGroup",
        //to:"dataList",
        key:"groupId",
        keyTitle:"groupName",
        title:local.name_group,
        limitId:"BUSI_CMS_ARTICLE_GROUP",
        noUpdate:true,
        customUpdate:function(extra,response){
            //更新分组显示
            $("#topicGroupContent").add(template("template_group",{data:extra,index:response.infor}));
        },
        extra:{articleId:function(){return curIndex.getSubUrlParam("id");}},
        property:{
            groupId:{type:"hidden"},
            articleId:{type:"hidden"},
            groupName:{type:"input",must:true},
            groupType:{type:"radio",source:local.source.groupType,default:0}
        }
    };

    //图集文章内容编辑特殊处理
    //内容获取连接配置
    urlconfig["BUSI_CMS_ARTICLE_ALBUM"] = {
        url: "../busiCmsArticle",
        to:".pb-thumbnails",
        limitId:"BUSI_CMS_ARTICLE",
        extra:{
            "sectionCode":function(){return curIndex.getSubUrlParam("sectionCode");},
            "appCode":function(){return curIndex.getSubUrlParam("appCode");},
            "searchValue":function()
            {
                var articleId=curIndex.getSubUrlParam("id");
                return (articleId?("articleId=?;"+articleId):null);
            }
        },
        success:function(local,list,limit,access)
        {
            $(".pb-thumbnails").html(imgsSmall({
                    local:local,
                    data:list.length>0?list[0]:null
                }));
        }
    };
    //链接资源添加处理
    urlconfig["BUSI_RESOURCE"]={
        url: "../busiResource",
        limitId:"BUSI_RESOURCE",
        property:{
            resourceId:{type:"hidden"},
            resourceTitle:{type:"input",must:true},
            resourceLink:{type:"input",must:true}
        }
    };
    //增加自定义处理插件
    //plugins[ARTICLE_ALBUM_UPLOAD]:处理图集图片上传成功后的相关处理
    plugins["ARTICLE_ALBUM_UPLOAD"]={
        success:function(local,data,limit,access)
        {
            index.requestData(urlconfig,"BUSI_CMS_ARTICLE_ALBUM");
        }
    };
})(urlconfig,plugins);