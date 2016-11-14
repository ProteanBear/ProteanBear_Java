/**
 * 脚本说明：	数据格式配置-SystemPlugin
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
    //记录基本权限数据信息
    var originalLimit;
    
    //自定义搜索
    function search(keyword,data)
    {
        for(var i=0;i<data.length;i++)
        {
            (keyword==""||data[i].pluginName.indexOf(keyword)===-1)
                ?(
                    $("#module_"+data[i].pluginCode).removeClass("panel-danger"),
                    $("#plugin_"+data[i].pluginCode).removeClass("panel-danger"),
                    $("#module_"+data[i].pluginCode).addClass("panel-primary"),
                    $("#plugin_"+data[i].pluginCode).addClass("panel-info")
                )
                :(
                    $("#module_"+data[i].pluginCode).removeClass("panel-primary"),
                    $("#plugin_"+data[i].pluginCode).removeClass("panel-info"),
                    $("#module_"+data[i].pluginCode).addClass("panel-danger"),
                    $("#plugin_"+data[i].pluginCode).addClass("panel-danger")
                );
        }
    }
    
    //页面数据载入设置
    urlconfig.type = "multi";
    urlconfig.errorBefore = null;
    urlconfig.initKey = "SYSTEM_CONFIG_PLUGIN|ORIGINAL_LIMIT";
    urlconfig.customSearch=search;
    urlconfig["SYSTEM_CONFIG_PLUGIN"] = {
        url: "../systemPlugin",
        to:".property",
        success: "template_plugins",
        key:"custId",
        extra:{page:0},
        property:{
            custId:{type:"hidden"},
            pluginId:{type:"input"},
            pluginName:{type:"input"},
            pluginEnable:{type:"radio"},
            pluginType:{type:"radio"},
            pluginIcon:{type:"radio"},
            pluginLink:{type:"input"},
            pluginDisplay:{type:"radio"},
            dataRemark:{type:"text"}
        }
    };
    urlconfig["ORIGINAL_LIMIT"]={
        url: "../originalLimit",
        save:false,
        success: function(local,data,limit){
            //{local:local,data:data.list,limit:limit}
            if(!data) return;
            originalLimit={};
            for(var i=0;i<data.length;i++)
            {
                originalLimit[data[i].limitId+""]=data[i].limitName;
            }
        }
    };
    urlconfig["module"] = {
        url: "../systemPlugin",
        key:"custId",
        extra:{pluginType:"0"},
        noUpdate:true,
        customUpdate:function(params,data){index.requestData(urlconfig,"SYSTEM_CONFIG_PLUGIN");},
        property:{
            custId:{type:"hidden"},
            pluginId:{type:"input",must:true,match:/[A-Z|_]{6,60}/,alert:"插件标识必须由大写英文字母或下划线组成，最少6个!"},
            pluginName:{type:"input",must:true},
            dataRemark:{type:"text"},
            pluginEnable:{type:"radio",source:parent.local.source.use,default:1}
        }
    };
    urlconfig["plugin"] = {
        url: "../systemPlugin",
        key:"custId",
        extra:{pluginType:"1"},
        noUpdate:true,
        customUpdate:function(params,data){index.requestData(urlconfig,"SYSTEM_CONFIG_PLUGIN");},
        property:{
            custId:{type:"hidden"},
            pluginIcon:{type:"glyphicon",source:parent.local.source.glyphicon,default:"glyphicon-file"},
            pluginId:{type:"input",must:true,match:/[A-Z|_]{6,60}/,alert:"插件标识必须由大写英文字母或下划线组成，最少6个!"},
            pluginName:{type:"input",must:true},
            pluginLink:{type:"input",must:true},
            dataRemark:{type:"text"},
            pluginDisplay:{type:"radio",source:parent.local.source.display,default:1,inline:"start",col:4},
            pluginEnable:{type:"radio",source:parent.local.source.use,default:1,inline:"end",col:3}
        }
    };
    urlconfig["limit"] = {
        url: "../systemPlugin",
        key:"custId",
        extra:{pluginType:"2"},
        noUpdate:true,
        customUpdate:function(params,data){index.requestData(urlconfig,"SYSTEM_CONFIG_PLUGIN");},
        property:{
            custId:{type:"hidden"},
            pluginName:{type:"input",must:true},
            dataRemark:{type:"text"},
            pluginParent:{type:"radio",source:function(){return originalLimit;},default:0}
        }
    };
})(urlconfig);