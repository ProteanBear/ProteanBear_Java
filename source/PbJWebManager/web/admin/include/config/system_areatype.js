/**
 * 脚本说明：	数据格式配置-SystemAreaType
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
    //记录当前的数据索引
    var activeId=parent.console.storage.getValue("areaTypeId")||"";
    
    //自定义模板-插件选择
    var tempPlugin=template.compile(''
            +'<div class="form">'
                //根据给定的插件自动生成菜单
                +'<!--[for(var module in modules){]-->'
                    //所属模块不同时生成模块代码
                    +'<div class="form-group checkbox title">'
                        +'<label>'
                            +'<input type="checkbox" action-mode="check-all" check-index="<!--[=module]-->" > <!--[=module]-->'
                        +'</label>'
                    +'</div>'
                    +'<div class="form-group checkbox">'
                        //生成模块下的全部插件
                        +'<!--[for(var j=0;j<modules[module].plugins.length;j++){]-->'
                            +'<!--[var plugin=modules[module].plugins[j];]-->'
                            +'<!--[var checked=false;if(data){for(var k=0;k<data.typePlugins.length;k++){if(data.typePlugins[k]==plugin.custId){checked=true;break;}}}]-->'
                            +'<label class="checkbox-inline">'
                                +'<input type="checkbox" name="typePlugins" id="<!--[="typePlugins-"+j]-->" '
                                    +' value="<!--[=plugin.custId]-->" check-index="<!--[=module]-->"'
                                    +' action-mode="check-only"'
                                    +' <!--[=checked?"checked":""]-->>'
                                    +' <!--[=plugin.pluginName]-->'
                            +'</label>'
                        +'<!--[}]-->'
                    +'</div>'
                +'<!--[}]-->'
            +'</div>'
    );
    
    //属性显示事件
    var displayData=function(title,i,extra){
        parent.console.storage.setValue("areaTypeId",list[i].typeId);
        var typePlugins=list[i].typePlugins;
        index.displayPropertyForm(
                urlconfig,
                "SYSTEM_CONFIG_AREATYPE",
                "areaType",
                "#property",
                title,
                i,
                extra,
                {offset:"2",value:"9"},
                function(){
                    $("#areaType").find("[name='typePlugins']").each(function(){
                        $(this).get(0).checked=false;
                        for(var i=0;i<typePlugins.length;i++)
                        {
                            if(typePlugins[i]==$(this).val())
                            {
                                $(this).get(0).checked=true;
                                break;
                            }
                        }
                    });
                }
        );
        $("[action-mode='data-display']").each(function(){
            $(this).parent("li").removeClass("active");
            $(this).attr("data-index")!==i||$(this).parent("li").addClass("active");
        });
    };
    
    //页面数据载入设置
    urlconfig.type = "multi";
    urlconfig.errorBefore = null;
    urlconfig.initKey = "SYSTEM_CONFIG_AREATYPE";
    urlconfig["SYSTEM_CONFIG_AREATYPE"] = {
        url: "../systemAreaType",
        to:".property",
        success: function(local,data,limit){
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
                list[i].typeId!==activeId||(activeIndex=i,click=true);
                if(click) break;
            }
            click||(parent.console.storage.setValue("areaTypeId",""));
            $("[action-mode='data-display']:eq("+activeIndex+")").click();
        },
        key:"typeId",
        keyTitle:"typeName",
        title:parent.local.name_comtype,
        property:{
            typeId:{type:"hidden"},
            typeLevel:{type:"radio",source:parent.local.typeLevel,default:1},
            typeName:{type:"input",must:true},
            dataRemark:{type:"text"},
            typePlugins:{
                type:"template",
                source:function(data){
                    return tempPlugin({modules:parent.console.pluginsForModule("",false),data:data});
                },
                bind:function(){
                    $("[action-mode='check-all']").click(function(){
                        var checked=$(this).get(0).checked;
                        $("[check-index='"+$(this).attr("check-index")+"']").each(function(){
                            $(this).get(0).checked=checked;
                        });
                    });
                    $("[action-mode='check-only']").click(function(){
                        var checked=$(this).get(0).checked;
                        $("[check-index='"+$(this).attr("check-index")+"']").each(function(){
                            $(this).attr("action-mode")!=="check-all"
                                    ||($(this).get(0).checked=checked);
                        });
                    });
                },
                unbind:function(){
                    $("[action-mode='check-all']").unbind();
                    $("[action-mode='check-only']").unbind();
                },
                paramGenerate:function(params){
                    $("#areaType").find("[name='typePlugins']").each(function(){
                        if(this.checked)
                        {
                            params[this.name]=(params[this.name])?params[this.name]:[];
                            params[this.name].push(this.value);
                        }
                        $(this).attr("disabled","disabled");
                    });
                    return params;
                }
            }
        }
    };
})(urlconfig);