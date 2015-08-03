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
    var activeId=parent.console.storage.getValue("systemRoleId")||"";
    //记录当前的权限数据
    var limits;
    
    //自定义模板-插件选择
    var tempPlugin=template.compile(''
        +'<div class="form">'
            +'<!--[for(var i=0;i<data.length;i++){]-->'
            +'<!--[var module=data[i];if(i===data.length-1||data[i+1].pluginCode.substr(0,module.pluginCode.length)!==module.pluginCode){continue;}]-->'
            +'<!--[if(module.pluginType==0){]-->'
                    //所属模块不同时生成模块代码
                    +'<div class="form-group checkbox title">'
                        +'<label>'
                            +'<input type="checkbox" action-mode="check-all" check-index="<!--[=module.pluginCode]-->" > <!--[=module.pluginName]-->'
                        +'</label>'
                    +'</div>'
                    +'<div class="form-group checkbox">'
                        +'<!--[for(var j=0;j<data.length;j++){]-->'
                        +'<!--[var plugin=data[j];]-->'
                        +'<!--[if(plugin.pluginType==1&&plugin.pluginCode.indexOf(module.pluginCode)===0){]-->'
                            +'<label class="checkbox-inline">'
                                +'<input type="checkbox" action-mode="check-all" check-index="<!--[=plugin.pluginCode]-->" >'
                                    +' <!--[=plugin.pluginName]-->：'
                            +'</label>'
                            +'<!--[for(var k=0;k<data.length;k++){]-->'
                                +'<!--[var lim=data[k];]-->'
                                +'<!--[if(lim.pluginType==2&&lim.pluginCode.indexOf(plugin.pluginCode)===0){]-->'
                                    +'<label class="checkbox-inline">'
                                        +'<input type="checkbox" action-mode="check-only" name="roleLimits" id="<!--[="roleLimits-"+j]-->" '
                                            +' value="<!--[=lim.custId]-->" check-index="<!--[=lim.pluginCode]-->"'
                                            +' action-mode="check-only"'
                                            +' <!--[=checked?"checked":""]-->>'
                                            +' <!--[=lim.pluginName]-->'
                                    +'</label>'
                                +'<!--[}]-->'
                            +'<!--[}]--><br/>'
                        +'<!--[}]-->'
                        +'<!--[}]-->'
                    +'</div>'
            +'<!--[}]-->'
            +'<!--[}]-->'
        +'</div>'
    );
    
    //属性显示事件
    var displayData=function(title,i,extra){
        parent.console.storage.setValue("systemRoleId",list[i].roleId);
        var roleLimits=list[i].roleLimits;
        index.displayPropertyForm(
                urlconfig,
                "SYSTEM_CONFIG_ROLE",
                "systemRole",
                "#property",
                title,
                i,
                extra,
                {offset:"2",value:"9"},
                function(){
                    $("#systemRole").find("[name='roleLimits']").each(function(){
                        $(this).get(0).checked=false;
                        for(var i=0;i<roleLimits.length;i++)
                        {
                            if(roleLimits[i]==$(this).val())
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
    urlconfig.initKey = "SYSTEM_CONFIG_PLUGIN";
    urlconfig["SYSTEM_CONFIG_ROLE"] = {
        url: "../systemRole",
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
                list[i].roleId!==activeId||(activeIndex=i,click=true);
                if(click) break;
            }
            click||(parent.console.storage.setValue("systemRoleId",""));
            $("[action-mode='data-display']:eq("+activeIndex+")").click();
        },
        key:"roleId",
        keyTitle:"roleName",
        title:parent.local.name_role,
        property:{
            roleId:{type:"hidden"},
            roleType:{type:"radio",source:parent.local.roleType,default:1},
            roleName:{type:"input",must:true},
            dataRemark:{type:"text"},
            roleLimits:{
                type:"template",
                source:function(){return tempPlugin({data:limits});},
                bind:function(){
                    $("[action-mode='check-all']").click(function(){
                        var checked=$(this).get(0).checked;
                        var index=$(this).attr("check-index");
                        $("[check-index]").each(function(){
                            $(this).attr("check-index").substr(0,index.length)!==index
                                    ||($(this).get(0).checked=checked);
                        });
                    });
                    $("[action-mode='check-only']").click(function(){
                        var checked=$(this).get(0).checked;
                        var index=$(this).attr("check-index");
                        $("[check-index]").each(function(){
                            index.substr(0,$(this).attr("check-index").length)!==$(this).attr("check-index")
                                    ||($(this).get(0).checked=checked);
                        });
                    });
                },
                unbind:function(){
                    $("[action-mode='check-all']").unbind();
                    $("[action-mode='check-only']").unbind();
                },
                paramGenerate:function(params){
                    $("#systemRole").find("[name='roleLimits']").each(function(){
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
    
    urlconfig["SYSTEM_CONFIG_PLUGIN"] = {
        url: "../systemPlugin",
        to:".property",
        noSearch:true,
        extra:{page:0},
        success: function(local,data,limit){
            limits=data;
            index.requestData(urlconfig,"SYSTEM_CONFIG_ROLE");
        },
        key:"custId",
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
})(urlconfig);