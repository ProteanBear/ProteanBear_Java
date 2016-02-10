/**
 * 脚本说明：	数据格式配置-SystemArea
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
    //记录企业类型列表
    var areaTypeList={};
    //记录用户角色列表
    var userRoleList={};
    //记录应用平台列表
    var platList={};
    //获取本地化语言支持
    var local=parent.local;
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
        async: {
            enable: true,
            url:"../systemAreaTree",
            autoParam:["id=upId"],
            otherParam:{treeMode:"sub"},
            dataFilter: filter
        },
        callback:{
            onClick:displayEdit
        }
    };
    
    //自定义模板-平台选择
    var tempPlat=template.compile(''
        //编辑显示
        +'<!--[if(limit.insert||limit.edit){]-->'
            +'<!--[var i=0;for(var checkKey in platList){]-->'
                //获取对应的选中数据
                +'<!--[var checked=false;var platData=null;]-->'
                +'<!--[if(data){]-->'
                    +'<!--[for(var k=0;k<data["appPlat"].length/3;k++){]-->'
                        +'<!--[if(checkKey==data["appPlat"][k*3+0]){]-->'
                            +'<!--[platData={appPlatId:data["appPlat"][k*3+1],version:data["appPlat"][k*3+2]};checked=true;break;]-->'
                        +'<!--[}]-->'
                    +'<!--[}]-->'
                +'<!--[}]-->'
                //添加时显示格式
                +'<!--[if(operate=="create"){]-->'
                    +'<label class="checkbox-inline">'
                        +'<input type="checkbox" name="appPlat" id="<!--[="appPlat-"+i]-->" value="<!--[=checkKey]-->" <!--[=checked?"checked":""]-->> <!--[=platList[checkKey]]-->'
                    +'</label>'
                //编辑时显示，增加版本显示
                +'<!--[}else{]-->'
                    +'<div class="checkbox">'
                        +'<label>'
                            +'<input type="checkbox" name="appPlat" id="<!--[="appPlat-"+i]-->" value="<!--[=checkKey]-->" <!--[=checked?"checked":""]-->> <!--[=platList[checkKey]]-->'
                        +'</label>'
                        +'<!--[if(platData){]-->'
                            +'<label>'
                                +'(<!--[=local.name_lastnew+local.name_version+":"+(platData.version?version:"无")]-->)'
                            +'</label>'
                            +'<label>'
                                +'<!--[if(platData.version){]-->'
                                    +'<button id="systemVersion" name="systemVersion" type="button" class="btn btn-info btn-xs"><span class="glyphicon glyphicon-plus"></span><!--[=local.name_version+local.action["insert"]]--></button>'
                                +'<!--[}else{]-->'
                                    +'<button id="systemVersion" name="systemVersion" action-mode="data-dialog" title="" type="button" class="btn btn-info btn-xs"><span class="glyphicon glyphicon-plus"></span><!--[=local.action["insert"]+local.name_version]--></button>'
                                +'<!--[}]-->'
                            +'</label>'
                        +'<!--[}else{]-->'
                        +'<!--[}]-->'
                    +'</div>'
                +'<!--[}]-->'
            +'<!--[i++;}]-->'
        //只读显示
        +'<!--[}else{]-->'
            +'<p class="display">'
                +'<!--[var i=0;for(var checkKey in platList){]-->'
                    +'<!--[=(i==0)?"":"|"]-->'
                    +'<!--[=data?(checkKey==data["appPlat"]?platList[checkKey]:""):("")]-->'
                +'<!--[i++;}]-->'
            +'</p>'
        +'<!--[}]-->'
    );
    
    /**
     * filter:树Ajax更新处理
     * @param {String} treeId -
     * @param {Object} parentNode -
     * @param {Array} childNodes -
     * @returns {Array}
     */
    function filter(treeId, parentNode, childNodes) 
    {
        if (!childNodes||!childNodes.list) return null;
        //转换数据结构
        childNodes=childNodes.list;
        for (var i=0, l=childNodes.length; i<l; i++) {
            var data=childNodes[i];
            data.name = data.name.replace(/\.n/g, '.');
            //应用信息
            if(data.type===2)
            {
                data.appId=data.id;
                data.appName=data.name;
                data.areaId=parentNode.areaId;
                data.appThumbnail=data.icon;
                data.appCode=data.areaName;
                data.appIcon=data.areaAddress;
                data.appPlat=data.areaTypeMaps;
            }
            //用户信息
            if(data.type===3)
            {
                data.custId=data.id;
                data.areaId=parentNode.areaId;
                data.userIcon=data.icon;
                data.userName=data.areaName;
                data.userNick=data.name;
                data.userTel=data.areaTel;
                data.userQq=data.areaContact;
                data.userRoles=data.areaTypeMaps;
            }
        }
        //记录当前数据
        for(var i=0;i<list.length;i++)
        {
            if(list[i].areaId===parentNode.areaId)
            {
                list[i].childNodes=childNodes;
                break;
            }
        }
        return childNodes;
    }

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
        $("body").append(template("template_treebutton", {id: treeNode.id, type: treeNode.type,local: parent.local}));
        $("#treebutton-" + treeNode.id).css({
            top:$("#" + treeNode.tId + "_a").offset().top+"px",
            left:$("#" + treeNode.tId + "_a").offset().left+$("#" + treeNode.tId + "_a").width()+10+"px"
        });
        
        //绑定事件
        $("[action-mode='data-display']").unbind();
        $("[action-mode='data-display']").click(function(){
            displayForm($(this).attr("name"),$(this).attr("title"),$(this).attr("data-index"),$(this).attr("data-extra"));
        });
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
     * displayEdit:显示编辑属性
     * @param {Object} event
     * @param {String} treeId
     * @param {Object} treeNode
     * @param {boolean} clickFlag
     */
    function displayEdit(event, treeId, treeNode,clickFlag)
    {
        if(!treeNode||treeNode.type===undefined) return;
        var configNames=["category","company","application","user"];
        displayForm(
                configNames[treeNode.type],
                local.action["edit"]+local.areaClass[treeNode.type]+"（"+treeNode.areaName+"）",
                treeNode.areaIndex,null,treeNode);
    }
    
    //属性显示事件
    var displayForm=function(name,title,i,extra,customData){
        index.displayPropertyForm(
                urlconfig,
                name,
                name,
                "#property",
                title,
                i,
                extra,
                {offset:"2",value:"9"},
                function(){
                    //显示预设图片
                    !Holder||Holder.handle();
                    //绑定上传点击事件
                    index.bindFileEvent();
                },
                customData
        );
    };
    
    //页面数据载入设置
    urlconfig.type = "multi";
    urlconfig.errorBefore = null;
    urlconfig.initKey = "SYSTEM_MANAGE_AREA|SYSTEM_CONFIG_AREATYPE|SYSTEM_CONFIG_ROLE|ORIGINAL_PLATFORM";
    //行政区域树
    urlconfig["SYSTEM_MANAGE_AREA"] = {
        url: "../systemAreaTree",
        to:".property",
        success: function(local,data,limit){
            //记录数据
            list=data;
            for(var i=0;i<list.length;i++) list[i].areaIndex=i;
            //关闭指示器
            $(".property").html("");
            //显示树
            !data||!$.fn.zTree||$.fn.zTree.init($("#region-tree"),setting,data);
        },
        key:"areaId",
        keyTitle:"areaName",
        title:parent.local.name_area,
        property:{
            areaId:{},
            areaName:{}
        }
    };
    //企业类型
    urlconfig["SYSTEM_CONFIG_AREATYPE"] = {
        url: "../systemAreaType",
        noSearch:true,
        save:false,
        success: function(local,data,limit){
            for(var i=0;i<data.length;i++){areaTypeList[data[i].typeId]=data[i].typeName;}
        }
    };
    //用户角色
    urlconfig["SYSTEM_CONFIG_ROLE"] = {
        url: "../systemRole",
        noSearch:true,
        save:false,
        success: function(local,data,limit){
            for(var i=0;i<data.length;i++){userRoleList[data[i].roleId]=data[i].roleName;}
        }
    };
    //平台信息
    urlconfig["ORIGINAL_PLATFORM"] = {
        url: "../originalPlatform",
        noSearch:true,
        save:false,
        success: function(local,data,limit){
            for(var i=0;i<data.length;i++){platList[data[i].custId]=data[i].platName;}
        }
    };
    //目录
    urlconfig["category"] = {
        url: "../systemArea",
        key:"areaId",
        extra:{areaClass:"0"},
        title:"目录",
        keyTitle:"areaName",
        buttons:[
            {id:"",action:"data-display",name:"category",title:local.action["insert"]+local.areaClass["0"],style:"info",icon:"glyphicon-plus",display:local.areaClass["0"]},
            {id:"",action:"data-display",name:"company",title:local.action["insert"]+local.areaClass["1"],style:"info",icon:"glyphicon-plus",display:local.areaClass["1"]}
        ],
        bind:{
            "form":function(){
                $("[action-mode='data-display']").unbind();
                $("[action-mode='data-display']").click(function(){
                    displayForm($(this).attr("name"),$(this).attr("title"),$(this).attr("data-index"),$(this).attr("data-extra"));
                });
            }
        },
        property:{
            areaId:{type:"hidden"},
            areaName:{type:"input",must:true},
            areaIconClose:{type:"images",source:local.source.images["categoryClose"],default:""},
            areaIconOpen:{type:"images",source:local.source.images["categoryOpen"],default:""},
            dataRemark:{type:"text"}
        }
    };
    //企业
    urlconfig["company"] = {
        url: "../systemArea",
        key:"areaId",
        extra:{areaClass:"1"},
        buttons:[
            {id:"",action:"data-display",name:"application",title:local.action["insert"]+local.areaClass["2"],style:"info",icon:"glyphicon-plus",display:local.areaClass["2"]},
            {id:"",action:"data-display",name:"user",title:local.action["insert"]+local.areaClass["3"],style:"info",icon:"glyphicon-plus",display:local.areaClass["3"]}
        ],
        bind:{
            "form":function(){
                $("[action-mode='data-display']").unbind();
                $("[action-mode='data-display']").click(function(){
                    displayForm($(this).attr("name"),$(this).attr("title"),$(this).attr("data-index"),$(this).attr("data-extra"));
                });
            }
        },
        property:{
            areaId:{type:"hidden"},
            areaTypeMaps:{type:"checkbox",source:function(){return areaTypeList;}},
            areaName:{type:"input",must:true},
            areaContact:{type:"input",inline:"start",col:3},
            areaTel:{type:"input",inline:"end",col:4},
            areaIcon:{type:"uploadIcon",inline:"start",col:3,size:"64x64",extra:"{resource:0}"},
            areaAddress:{type:"text",inline:"end",col:4},
            dataRemark:{type:"text"}
        }
    };
    //应用
    urlconfig["application"] = {
        url: "../systemApplication",
        key:"appId",
        property:{
            appId:{type:"hidden"},
            appCode:{type:"input",must:true,inline:"start",col:3},
            appName:{type:"input",must:true,inline:"end",col:4},
            appIcon:{type:"uploadIcon",inline:"start",col:3,size:"1024x1024",width:128,height:128,extra:"{resource:0}"},
            appThumbnail:{type:"uploadIcon",inline:"end",col:2,size:"64x64",width:64,height:64,extra:"{resource:0}"},
            appPlat:{
                type:"template",
                source:function(data,limit,operate){return tempPlat({data:data,limit:limit,operate:operate,platList:platList,local:local});},
                bind:function(){},
                unbind:function(){},
                paramGenerate:function(params){
                     $("#application").find("[name='appPlat']").each(function(){
                        if(this.checked)
                        {
                            params[this.name]=(params[this.name])?params[this.name]:[];
                            params[this.name].push(this.value);
                        }
                        $(this).attr("disabled","disabled");
                    });
                    return params;
                }
            },
            dataRemark:{type:"text"}
        }
    };
    //用户
    urlconfig["user"] = {
        url: "../systemUser",
        key:"custId",
        property:{
            custId:{type:"hidden",auto:true},
            userIcon:{type:"images",source:local.source.images["userIcon"]},
            userRoles:{type:"checkbox",source:function(){return userRoleList;}},
            userName:{type:"input",must:true,inline:"start",col:4},
            userNick:{type:"input",must:true,inline:"end",col:3},
            newPass:{type:"custom",insertMust:true,customInput:"password",inline:"start",col:4},
            passAgain:{type:"custom",insertMust:true,customInput:"password",inline:"end",col:3},
            userTel:{type:"input",inline:"start",col:4},
            userQq:{type:"input",inline:"end",col:3}
        }
    };
})(urlconfig);