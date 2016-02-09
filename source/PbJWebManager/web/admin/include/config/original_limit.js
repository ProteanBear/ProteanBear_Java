/**
 * 脚本说明：	数据格式配置-OriginalLimit
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
    //记录当前的数据
    var list;
    var keyStorage="limitId";
    
    //属性显示事件
    var displayData=function(title,i,extra){
        parent.console.storage.setValue(keyStorage,list[i].limitId);
        index.displayPropertyForm(
                urlconfig,
                "ORIGINAL_LIMIT",
                "originalLimit",
                "#property",
                title,
                i,
                extra,
                {offset:"2",value:"9"}
        );
        $("[action-mode='data-display']").each(function(){
            $(this).parent("li").removeClass("active");
            $(this).attr("data-index")!==i||$(this).parent("li").addClass("active");
        });
    };
    
    //页面数据载入设置
    urlconfig.type = "multi";
    urlconfig.errorBefore = null;
    urlconfig.initKey = "ORIGINAL_LIMIT";
    urlconfig["ORIGINAL_LIMIT"] = {
        url: "../originalLimit",
        to:".property",
        success: function(local,data,limit,access,activeId){
            //记录数据
            list=data;
            
            //显示数据
            $(".property").html("");
            $("#tableList").html(template("template_list",{data:data,local:local,limit:limit}));
            
            //绑定事件
            $("[action-mode='data-display']").unbind();
            $("[action-mode='data-display']").click(function(){
                displayData($(this).attr("title"),$(this).attr("data-index"),$(this).attr("data-extra"));
            });
            
            //选择默认数据
            var click=false,activeIndex=0;
            for(var i=0;i<list.length;i++)
            {
                list[i].limitId!=activeId||(activeIndex=i,click=true);
                if(click) break;
            }
            click||(parent.console.storage.setValue(keyStorage,""));
            $("[action-mode='data-display']:eq("+activeIndex+")").click();
        },
        key:"priKey",
        keyTitle:"limitName",
        keyStorage:keyStorage,
        title:parent.local.name_limit,
        property:{
            priKey:{type:"hidden",from:"limitId"},
            limitId:{type:"custom",custemInput:"number",must:true},
            limitName:{type:"input",must:true},
            limitInit:{type:"radio",source:parent.local.source["yesOrNo"],default:"1"}
        }
    };
})(urlconfig);