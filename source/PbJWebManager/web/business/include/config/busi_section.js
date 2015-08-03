/**
 * 脚本说明：	数据格式配置-busiSection
 * 脚本作者：     ProteanBear
 * 当前版本：	1.0 2014
 */

/*初始化命名空间*/
var urlconfig = urlconfig || {};

/**
 * 设置SystemPlugin相关的数据配置
 * @param urlconfig - 
 */
(function(urlconfig) {
    //图标路径
    var pathIcon="../include/images/icons/";
    //最大同时访问数
    var max=5;
    //记录应用
    var apps=parent.console.loginUser.apps;
    var idx=0;
    var local=parent.local;
    var curData={};
    
    //树显示配置
    var setting = {
        data: {
            simpleData: {
                enable: true
            }
        },
        view:{
            showLine:false,
            addHoverDom: addHoverDom,
            removeHoverDom: removeHoverDom
        },
        callback:{
            onClick:doWhenClickTreeNode
        }
    };
    
    /**
     * addHoverDom:树增加自定义控件
     * @param {String} treeId
     * @param {Object} treeNode
     */
    function addHoverDom(treeId, treeNode)
    {
        //显示按钮
        if (treeNode.type > 1) return;
        if ($("#treebutton-" + treeNode.id).length > 0) return;
        $("body").append(template("template_treebutton", {
            id: treeNode.id,
            type: treeNode.sectionTypeContent,
            name:treeNode.name,
            appCode:treeNode.sectionApp,
            sectionName:treeNode.sectionName,
            sectionCode:treeNode.sectionCode,
            local: parent.local
        }));
        $("#treebutton-" + treeNode.id).css({
            top:$("#" + treeNode.tId + "_a").offset().top+"px",
            left:$("#" + treeNode.tId + "_a").offset().left+$("#" + treeNode.tId + "_a").width()+10+"px"
        });
        
        //绑定事件
        $("[action-mode='section-edit']").each(function(){
            $(this).attr("onclick")||(
                $(this).attr("onclick","true"),
                $(this).click(function(){
                    var tempUp=eval("("+$(this).attr("data-extra")+")");
                    var customData=null;
                    for(var i=0;i<curData[tempUp.sectionApp].length;i++)
                    {
                        if(curData[tempUp.sectionApp][i].sectionCode===tempUp.upCode)
                        {
                            customData=curData[tempUp.sectionApp][i];
                            break;
                        }
                    }

                    index.displayPropertyForm(urlconfig,
                        $(this).attr("name"),
                        "busiSection",
                        "#sub-content",
                        $(this).attr("title"),
                        i,
                        $(this).attr("data-extra"),
                        $(this).attr("form-col"),
                        urlconfig["BUSI_MANAGE_SECTION"].afterHtml,
                        customData
                    );
                }));
        });
        index.bindCommonEvent();
    }

    /**
     * removeHoverDom:树删除自定义控件
     * @param {String} treeId
     * @param {Object} treeNode
     */
    function removeHoverDom(treeId, treeNode)
    {
        $(".treebutton").children("button").unbind();
        $(".treebutton").remove();
    }
    
    /**
     * doWhenClickTreeNode:点击树节点时显示
     * @param {type} event
     * @param {type} treeId
     * @param {type} treeNode
     * @param {type} clickFlag
     * @returns {undefined}
     */
    function doWhenClickTreeNode(event, treeId, treeNode,clickFlag)
    {
        if(!treeNode||treeNode.sectionTypeContent===undefined) return;
        //0：新闻类显示文章列表
        if(treeNode.sectionTypeContent===0)
        {
            index.loadSubContent("sub_articles.html?active=BUSI_CMS_ARTICLE&sectionName="+treeNode.sectionName
                    +"&sectionCode="+treeNode.sectionCode+"&appCode="+treeNode.sectionApp);
        }
    }
    
    //页面数据载入设置
    urlconfig.type = "multi";
    urlconfig.messageBefore = "#sub-content";
    urlconfig.initKey = function(){
        idx=0;
        apps.length===0||(index.requestData(urlconfig,"BUSI_MANAGE_SECTION",{sectionApp:apps[idx].appCode}));
    };
    urlconfig["BUSI_MANAGE_SECTION"] = {
        url: "../busiSection",
        //to:".main",
        key:"custId",
        keyTitle:"sectionName",
        title:parent.local.name_section,
        limitId:"BUSI_LIMIT",
        noSearch:true,
        save:false,
        noUpdate:true,
        customUpdate:function(extra){
            //获取索引
            var app;
            for(var i=0;i<apps.length;i++){if(extra.sectionApp===apps[i].appCode){idx=i;app=apps[i];break;}}
            //更新树
            index.requestData(urlconfig,"BUSI_MANAGE_SECTION",{sectionApp:app.appCode});
            //恢复输入
            $("[action-mode]").removeAttr("disabled");
            for (var key in urlconfig["BUSI_MANAGE_SECTION"].property)
            {
                $("#busiSection").find("[name='"+key+"']").each(function(){
                        $(this).removeAttr("disabled");
                    });
            }
        },
        success:function(local,data,limit,access){
            //显示树
            !data||!$.fn.zTree||($.fn.zTree.init($("#"+access.sectionApp+"_ul"),setting,data),curData[apps[idx].appCode]=data);
            //获取下一个应用
            idx++;
            idx>=apps.length||(index.requestData(urlconfig,"BUSI_MANAGE_SECTION",{sectionApp:apps[idx].appCode}));
        },
        afterHtml:function(){
            //显示预设图片
            !Holder||Holder.handle();
            //绑定上传点击事件
            index.bindFileEvent();
        },
        property:{
            custId:{type:"hidden"},
            sectionName:{type:"input",must:true,inline:"start",col:3},
            sectionAlias:{type:"input",inline:"end",col:3},
            sectionTypeContent:{type:"radio",inline:"start",col:3,source:parent.local.source.type["content"],default:"0"},
            sectionEnable:{type:"radio",inline:"end",col:3,source:parent.local.source["yesOrNo"],default:"1"},
            sectionTypeDisplay:{type:"radio-images",source:parent.local.source.type["display"],isIcon:false,default:"0"},
            sectionIconClose:{type:"images",source:parent.local.source.images["categoryClose"],default:pathIcon+"index_close.png"},
            sectionIconOpen:{type:"images",source:parent.local.source.images["categoryOpen"],default:pathIcon+"index_open.png"},
            sectionIcon:{type:"uploadIcon",inline:"start",col:3,size:"128X128",width:64,height:64},
            dataRemark:{type:"text",inline:"end",col:3}
        }
    };
})(urlconfig);