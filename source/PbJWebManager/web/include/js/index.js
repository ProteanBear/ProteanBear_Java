/**
 * Created by maqiang on 14-7-2.
 * 用户主页
 */

var index = index || {};
var urlconfig = urlconfig || {};

(function(index,urlconfig)
{
    /*获取本地语言设置*/
    var local=parent.local||{};
    //记录当前编辑的数据主键
    var curDataKey=null;
    //记录操作模式参数名称
    var operateMode="mode";
    //记录参数主键
    var primaryKey="primaryKey";
    //记录参数搜索
    var searchValue="searchValue";
    //记录操作模式参数内容
    var operate=["list","create","remove","edit"];
    //记录当前操作模式值
    var curOperate=-1;
    //记录当前编辑的数据对应的页码
    var curPage=1;
    var totalPage = 0;
    //记录当前获取到的数据信息
    var curData=null;
    //记录当前页面插件的标识
    var active=parent.console.getUrlParam("active")?parent.console.getUrlParam("active"):"BUSI_MANAGE_SECTION";
    //记录当前页面插件的权限
    var limit={
            insert:parent.console.haveLimit(active,"01"),
            remove:parent.console.haveLimit(active,"02"),
            edit:parent.console.haveLimit(active,"03"),
            check:parent.console.haveLimit(active,"04"),
            list:parent.console.haveLimit(active,"00")
        };

    /**
     * alertMessage:弹出显示提示信息
     * @param mode danger|success|info|warning
     * @param message - 错误信息
     * @param second - 自动消失的秒数
     */
    function alertMessage(mode,message,second)
    {
        //元素存在，清除元素
        if ($(".alert").length !== 0) $(".alert").remove();
        second=second||4;

        //添加元素
        $(urlconfig.messageBefore||urlconfig.to||".property").before(mt.messageAlert({
            mode:mode,
            alert: local.message[mode],
            message: message
        }));

        //弹出元素
        $(".alert").fadeIn("fast", function() {$(".alert").addClass("in");});

        //自动消失
        !second||(setTimeout(function(){$(".alert").fadeOut("fast");},second*1000));
    }
    index.alertMessage=alertMessage;

    /**
     * getUrlParam:获取链接内的参数
     * @param name - 参数名称
     */
    index.getSubUrlParam=function(name)
    {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var src=$("#contentFrame").attr("src");
        if(!src) return null;
        var r = (src.substring(src.indexOf("?")+1)).match(reg);  //匹配目标参数
        if (r !== null) return r[2];
        return null; //返回参数值
    };

    /**
     * textExchange:文本格式转换
     * @param str - 参数名称
     * @param mode - 参数名称
     * @param code - 是否进行编码处理
     */
    function textExchange(str,mode,code)
    {
        if (!str) return "";
        var reg;
        //转换到<br>
        if (mode === 0)
        {
            reg = parent.pb.sys.isIE ? (new RegExp("\r\n", "g"))
                    : (new RegExp("\n", "g"));
            str = str.replace(reg, "<br>");
            str = code?encodeURIComponent(str):str;
        }
        //转换到\r\n
        else if (mode === 1)
        {
            str = code?decodeURIComponent(str):str;
            reg = new RegExp("<br>", "g");
            str = parent.pb.sys.isIE ? (str.replace(reg, "\r\n"))
                    : (str.replace(reg, "\n"));
        }
        return str;
    }
    template.helper('$textExchange', textExchange);
    index.textExchange = textExchange;

    /**
     * htmlExchange:html格式转换
     * @param str - 参数名称
     * @param mode - 参数名称
     * @param code - 是否进行编码处理
     */
    function htmlExchange(str,mode,code)
    {
        if (!str) return "";
        var reg;
        //转换到<br>
        if (mode === 0)
        {
            str = code?encodeURIComponent(str):str;
        }
        //转换到\r\n
        else if (mode === 1)
        {
            str = code?decodeURIComponent(str):str;
        }
        return str;
    };

    /**
     * copeHtmlTo:复制HTML到指定元素下，用于子页面动态载入
     * @param {String} html - HTML内容
     * @param {String} to - 指定元素选择器
     * @param {String} exJs - 额外动态载入JS链接
     * @param {String} config description
     * @param {Object} data - 数据内容
     */
    function copeHtmlTo(html,to,exJs,config,data)
    {
        //复制内容
        $(to).html(html);
        //记录数据信息
        recordDataInfor(data,true);
        //绑定事件
        bindCommonEvent();
        bindFileEvent();
        //额外载入JS
        loadResource(exJs);
        //处理特殊配置
        for(var key in config)
        {
            if(key.indexOf("ckeditor")!==-1)
            {
                $('textarea#'+config[key].id).ckeditor(config[key].editor);
                window.location.hash="mainContent";
                CKEDITOR.instances[config[key].id].on("focus",function(){
                    $("#cke_"+config[key].id).addClass("active");
                });
                CKEDITOR.instances[config[key].id].on("blur",function(){
                    $("#cke_"+config[key].id).removeClass("active");
                });
            }
        }
    }
    index.copeHtmlTo=copeHtmlTo;

    /**
     * loadResource - 动态载入脚本或资源文件
     * @param {type} path - 文件相对路径
     * @returns {undefined}
     */
    function loadResource(path)
    {
        if(!path) return;
        var keys=path.split("|");
        for(var i=0;i<keys.length;i++)
        {
            if(keys[i].indexOf(".css")!==-1)
            {
                $("<link>")
                    .attr({ rel: "stylesheet",
                        type: "text/css",
                        href: keys[i]
                    })
                    .appendTo("head");
            }
            if(keys[i].indexOf(".js")!==-1)
            {
                //async为false的时候是同步的  
                //dataType为script的时候已经帮你把返回结果用script类型的dom元素添加到文档中了，如果跨域，POST会转换为GET 
//                $.ajax({  
//                    type: 'GET',  
//                    url:keys[i],  
//                    async:false,
//                    dataType:'script'
//                });
                $("<script>")
                    .attr({
                        src: keys[i]
                    })
                    .appendTo("head");
            }
        }
    }

    /**
     * valNullCheck:输入校验处理
     * @param input - 输入框元素
     */
    function valNullCheck(input)
    {
        var result=false;
        $(input).val()!==""?(
            $(input).parents(".form-group").removeClass("has-warning has-feedback"),
            $(input).attr("placeholder",""),
            $(input).parent().children("span").remove(),
            result=true
        ):(
            $(input).parents(".form-group").addClass("has-warning has-feedback"),
            $(input).attr("placeholder",local.warning_infor["000"]),
            $(input).after(mt.inputWarning({})),
            result=false
        );
        return result;
    }

    /**
     * valMatchCheck:输入校验处理
     * @param input - 输入框元素
     * @param reg - 正则表达式
     * @param alert - 错误时提示信息
     */
    function valMatchCheck(input,reg,alert)
    {
        var result=false;
        $(input).val().match(reg)?(
            $(input).parents(".form-group").removeClass("has-error has-feedback"),
            $(input).attr("placeholder",""),
            $(input).parent().children("span").remove(),
            result=true
        ):(
            $(input).parents(".form-group").addClass("has-error has-feedback"),
            $(input).val(""),
            $(input).attr("placeholder",alert),
            $(input).after(mt.inputError({})),
            result=false
        );
        return result;
    }

    /**
     * bindFileEvent:绑定文件相关处理事件
     */
    function bindFileEvent()
    {
        $("[action-mode='upload']").unbind();
        $("[action-mode='upload']").click(function(){
            $("#"+$(this).attr("to")+"_file").click();
            $("#"+$(this).attr("to")+"_file").unbind();
            $("#"+$(this).attr("to")+"_file").change(function(){
                ajaxFileUpload($(this).attr("to"),$(this).attr("action-plugin"));
            });
        });
    }
    index.bindFileEvent=bindFileEvent;

    /**
     * recordDataInfor:记录数据相关的分页信息
     * @param {type} data
     * @param {type} isSave
     * @returns {undefined}
     */
    function recordDataInfor(data,isSave)
    {
        isSave=(isSave===null||isSave===undefined)?true:isSave;
        curPage=data?(data.page||1):1;
        totalPage=data?(data.totalPage||0):0;
        if(isSave)
        {
            curData=data?(data.list||[]):curData;
            active=index.getSubUrlParam("active");
        }
    }

    /**
     * ajaxFileUpload:Ajax文件上传
     * @param id - 标识
     */
    function ajaxFileUpload(id,plugin)
    {
        function complete()
        {
            //$("#"+id+"_file").get(0).disabled=false;
            $("[action-mode]").removeAttr("disabled");
            $("#"+id+"_file").val("");
        }

        //$("#"+id+"_file").get(0).disabled=true;
        $("[action-mode]").attr("disabled","disabled");

        var params={};
        var extra=$("#"+id).attr("data-extra");
        extra=extra?eval("("+extra+")"):null;
        if(extra)
        {
            for (var key in extra)
            {
                params[key] = typeof (extra[key]) === "function" ?
                    extra[key]() : extra[key];
            }
        }

        $.ajaxFileUpload?$.ajaxFileUpload({
            url: '../fileUpload',
            secureuri: false,
            fileElementId: id+'_file',
            dataType: 'json',
            data:extra,
            complete:function(){complete();},
            success: function(data, status) {
                if (data !== null)
                {
                    if (data.success)
                    {
                        (plugin&&plugins&&plugins[plugin]&&plugins[plugin].success)?
                            (plugins[plugin].success(data))
                            :($("#"+id).val(data.path),$("#"+id+"_img").attr("src","../"+data.path));
                    }
                    else
                    {
                        alertMessage("danger",data.infor);
                    }
                }
                else
                {
                    alertMessage("danger",local.error_infor["004"]);
                }
            },
            error: function(XHR, infor)
            {
                //弹出错误信息
                alertMessage("danger",local.error_infor["005"]);
            }
        }):(complete());
    }

    /**
     * requestData:数据读取请求
     * @param config description
     * @param name description
     * @param access description
     */
    function requestData(config,name,access)
    {
        //非空判断
        if (!config) return;

        //读取链接设置
        config = (config.type === "multi") ? config[name] : config;
        //非空判断
        if (!config) return;

        //显示载入指示器
        !config.to||$(config.to).length===0||$(config.to).html(parent.console.loading()||"");

        //获取提交数据
        var params = {page:curPage},value="";
        if (config && config.searchs)
        {
            var i=0;
            for (var attr in config.searchs)
            {
                value+=(i===0)?"":"|";
                value+=attr+";"+(typeof (config.searchs[attr]) === "function" ?
                    config.searchs[attr]() : config.searchs[attr]);
                i++;
            }
        }
        if($("#"+searchValue).length>0&&!config.noSearch)
        {
            value+=(value==="")?"":"|";
            value+=$("#"+searchValue).attr("name")+";"+"%"+$("#"+searchValue).val()+"%";
            params[searchValue]=value;
        }
        //附属参数
        if(config.extra)
        {
            for (var key in config.extra)
            {
                params[key] = typeof (config.extra[key]) === "function" ?
                    config.extra[key]() : config.extra[key];
            }
        }
        //access=access||config.access;
        if(access)
        {
            for (var key in access)
            {
                params[key] = typeof (access[key]) === "function" ?
                    access[key]() : access[key];
            }
        }

        //Ajax访问服务器
        $.ajax({
            type: 'POST',
            url: config.url,
            data: params,
            dataType: "json",
            complete: function(XHR, TS){},
            success: function(data, textStatus, jqXHR)
            {
                if (data !== null)
                {
                    if (data.success)
                    {
                        //记录当前数据
                        (config.save===false)||(curData=data.list);
                        recordDataInfor(data,config.save);
                        //空数据提示
                        !curData||curData.length>0||(alertMessage("warning",local.warning_infor["001"]));
                        //获取缓存的激活标识
                        var activeId=null;
                        !config.keyStorage||(activeId=(parent.console.storage.getValue(config.keyStorage)||""));
                        //显示数据内容
                        !config.success||((typeof(config.success)==="function")?
                                   (config.success(local,data.list,limit,access,activeId,data))
                                   :($(config.to).html(template(config.success,{local:local,data:data.list,limit:limit}))));
                        //绑定事件
                        bindCommonEvent();
                    }
                    else
                    {
                        alertMessage("danger",data.infor);
                    }
                }
                else
                {
                    alertMessage("danger",local.error_infor["004"]);
                }
            },
            error: function(XHR, infor)
            {
                //弹出错误信息
                alertMessage("danger",local.error_infor["005"]);
            }
        });
    }
    index.requestData=requestData;

    /**
     * submitData:数据读取请求
     * @param config 链接配置
     * @param name 配置索引
     * @param button 提交点击按钮
     * @param source 提交点击来源，dialog|normal
     * @param extra 付增数据
     * @param index 主键索引
     */
    function submitData(config,name,button,source,extra,index)
    {
        //非空判断
        if (!config) return;

        //读取链接设置
        config = (config.type === "multi") ? config[name] : config;
        //非空判断
        if (!config) return;

        //字段填写校验
        var check=true;
        for (var key in config.property)
        {
            var prop=config.property[key];
            $("#"+source).find("#"+key).each(function(){
                if((prop.type==="input"||prop.type==="custom")
                        &&(prop.must||(curOperate===1&&prop.insertMust)))
                {
                    check=valNullCheck(this);
                    !check||!prop.match||valMatchCheck(this,prop.match,prop.alert);
                }
            });
        }
        if(!check) return;

        //锁定按钮
        $("[action-mode]").attr("disabled","disabled");
        //显示载入指示器
        var btnHtml=button.html();
        button.html(parent.console.loading(16)||parent.local.operating||"");

        //获取提交数据
        var params = {};
        //附属参数
        if(config.extra)
        {
            for (var key in config.extra)
            {
                params[key] = typeof (config.extra[key]) === "function" ?
                    config.extra[key]() : config.extra[key];
            }
        }
        if(extra)
        {
            extra=eval("("+extra+")");
            for (var key in extra)
            {
                params[key] = typeof (extra[key]) === "function" ?
                    extra[key]() : extra[key];
            }
        }
        //数据参数
        if(config.property)
        {
            for (var key in config.property)
            {
                var prop=config.property[key];
                if(prop.type==="radio"||prop.type==="glyphicon"||prop.type==="images")
                {
                    $("#"+source).find("[name='"+key+"']").each(function(){
                        if(this.checked) params[this.name]=this.value;
                        $(this).attr("disabled","disabled");
                    });
                }
                else if(prop.type==="checkbox")
                {
                    $("#"+source).find("[name='"+key+"']").each(function(){
                        if(this.checked)
                        {
                            params[this.name]=(params[this.name])?params[this.name]:[];
                            params[this.name].push(this.value);
                        }
                        $(this).attr("disabled","disabled");
                    });
                }
                else if(prop.type==="hidden"&&key===config.key)
                {
                    if(curOperate===3)
                    {
                        index?(params[primaryKey]=index):
                            ($("#"+source).find("#"+key).each(function(){
                                params[primaryKey]=this.value;
                            }));
                    }
                    else
                    {
                        if(prop.auto===false)
                        {
                            index?(params[this.name]=index):
                                ($("#"+source).find("#"+key).each(function(){
                                    params[this.name]=this.value;
                                }));
                        }
                    }
                }
                else if(prop.type==="template")
                {
                    params=prop.paramGenerate(params,curOperate);
                }
                else
                {
                    $("#"+source).find("#"+key).each(function(){
                        var value=(prop.type==="ckeditor")?CKEDITOR.instances[key].getData():this.value;
                        params[this.name]=(prop.type==="text")?textExchange(value,0,prop.encode):value;
                        params[this.name]=(prop.type==="ckeditor")?htmlExchange(value,0,prop.encode):value;
                        $(this).attr("disabled","disabled");
                    });
                }
            }
        }
        //模式参数
        curOperate===-1||(params[operateMode]=operate[curOperate]);

        //Ajax访问服务器
        $.ajax({
            type: 'POST',
            url: config.url,
            data: params,
            dataType: "json",
            complete: function(XHR, TS)
            {
                //关闭指示器
                button.html(btnHtml);
                //解除锁定
                $("[action-mode]").removeAttr("disabled");
                $(".modal").modal('hide');
                for (var attr in config.property)
                {
                    $("[name='"+attr+"']").each(function(){
                        $(this).removeAttr("disabled");
                    });
                }
            },
            success: function(data, textStatus, jqXHR)
            {
                if (data !== null)
                {
                    //显示信息
                    data.success?(
                            alertMessage("success",""),
                            //记录新增的主键
                            !config.keyStorage||!data.newId||(parent.console.storage.setValue(config.keyStorage,data.newId)),
                            //更新列表
                            config.noUpdate||requestData(urlconfig,name),
                            //自定义更新
                            !config.customUpdate||config.customUpdate(params,data)
                        )
                        :alertMessage("danger",data.infor);
                }
                else
                {
                    alertMessage("danger",local.error_infor["004"]);
                }
            },
            error: function(XHR, infor)
            {
                //弹出错误信息
                alertMessage("danger",local.error_infor["005"]);
            }
        });
    }
    index.submitData=submitData;

    /**
     * removeData:数据删除请求
     * @param config 链接配置
     * @param name 配置索引
     * @param button 提交点击按钮
     * @param batch 是否批量
     * @param customData 自定义数据内容
     */
    function removeData(config,name,button,batch,customData)
    {
        //非空判断
        if (!config) return;

        //读取链接设置
        config = (config.type === "multi") ? config[name] : config;
        //非空判断
        if (!config) return;
        if (!button.attr("data-index")&&!customData) return;
        var data=curData[button.attr("data-index")]||customData;
        if (!data) return;

        //弹出询问对话框
        displayConfirm(button.attr("title"),function(conBut){
            //锁定按钮
            button.attr("disabled","disabled");
            conBut.attr("disabled","disabled");
            conBut.html(parent.console.loading(16)||parent.local.operating||"");

            //获取提交数据
            var params = {};
            //操作参数
            curOperate=2;
            curOperate===-1||(params[operateMode]=operate[curOperate]);
            params[primaryKey]=(config.property[config.key]&&config.property[config.key].from)?data[config.property[config.key].from]:data[config.key];
            !config.keyStorage||(parent.console.storage.setValue(config.keyStorage,""));
            customData=data;

            //Ajax访问服务器
            $.ajax({
                type: 'POST',
                url: config.url,
                data: params,
                dataType: "json",
                complete: function(XHR, TS)
                {
                    //解除锁定
                    button.removeAttr("disabled");
                    //关闭指示器
                    conBut.html(parent.local.action["submit"]||"保存");
                    //解除锁定
                    conBut.removeAttr("disabled");
                    $(".modal").modal('hide');
                },
                success: function(data, textStatus, jqXHR)
                {
                    if (data !== null)
                    {
                        //显示信息
                        data.success?alertMessage("success","")
                            :alertMessage("danger",data.infor);
                        //更新列表
                        !data.success||config.noUpdate||requestData(urlconfig,name);
                        //自定义更新
                        !data.success||!config.customUpdate||config.customUpdate(params,data,customData);
                    }
                    else
                    {
                        alertMessage("danger",local.error_infor["004"]);
                    }
                },
                error: function(XHR, infor)
                {
                    //弹出错误信息
                    alertMessage("danger",local.error_infor["005"]);
                }
            });
        });
    }
    index.removeData=removeData;

    /**
     * displayPropertyDialog:显示属性对话框
     * @param config 数据配置
     * @param name 配置索引
     * @param dialog 对话框标识
     * @param title 对话框显示标题
     * @param idx 数据索引
     * @param extra 付增数据
     * @param customData 自定义数据内容
     */
    function displayPropertyDialog(config,name,dialog,title,idx,extra,customData)
    {
        //非空判断
        if (!config) return;
        //读取链接设置
        config = (config.type === "multi") ? config[name] : config;
        //空判断
        if(!config) return;

        //记录操作模式
        curOperate=(idx!==null&&idx!==undefined)?3:1;
        //确定当前权限值
        var curLimit=config.limitId?({
                insert:parent.console.haveLimit(config.limitId,"01"),
                remove:parent.console.haveLimit(config.limitId,"02"),
                edit:parent.console.haveLimit(config.limitId,"03"),
                list:parent.console.haveLimit(config.limitId,"00")
            }):limit;

        //添加模板到当前页面
        $(".modal").length===0||($(".modal").remove());
        $("body").append(mt.dialog({
            id:dialog,
            name:name,
            title:title,
            local:local,
            property:config.property,
            data:customData||curData?curData[idx]:null,
            limit:idx?curLimit.edit:curLimit.insert
        }));
        //显示对话框
        $("#"+dialog).modal({});

        //绑定数据提交事件
        $("#"+dialog+"_submit").attr("onclick")||(
            $("#"+dialog+"_submit").attr("onclick","true"),
                $("#"+dialog+"_submit").click(function(){
                    submitData(urlconfig,$(this).attr("name"),$(this),dialog,extra);
                }));
        //绑定输入框校验事件
        for (var key in config.property)
        {
            var prop=config.property[key];
            if((prop.type==="input"||prop.type==="custom")&&(prop.must||(curOperate===1&&prop.insertMust)))
            {
                $("#"+dialog).find("#"+key).data({match:prop.match,alert:prop.alert});
                $("#"+dialog).find("#"+key).blur(function(){
                    !valNullCheck(this)||!$(this).data().match
                            ||valMatchCheck(this,$(this).data().match,$(this).data().alert);
                });
            }
            prop.type!=="template"||(prop.unbind(),prop.bind());
        }
    }

    /**
     * displayConfirm:显示确认对话框
     * @param msg description
     * @param callback description
     */
    function displayConfirm(msg,callback)
    {
        $("[action-mode='data-confirm']").unbind();
        $("#confirmDialog").remove();

        $("body").append(mt.confirm({content:msg,local:local}));
        $("#confirmDialog").modal({});
        $("[action-mode='data-confirm']").click(function(){
            callback($(this));
        });
    }

    /**
     * displayPropertyForm:显示属性表单
     * @param config 数据配置
     * @param name 配置索引
     * @param form 表单标识
     * @param to 添加的标识
     * @param title 对话框显示标题
     * @param idx 数据索引
     * @param extra 付增数据
     * @param col 布局设置
     * @param afterHtml 添加模板后执行
     * @param customData 自定义数据内容
     */
    function displayPropertyForm(config,name,form,to,title,idx,extra,col,afterHtml,customData)
    {
        //非空判断
        if (!config) return;
        //读取链接设置
        config = (config.type === "multi") ? config[name] : config;
        //空判断
        if(!config) return;

        //记录操作模式
        curOperate=(idx!==null&&idx!==undefined)?3:1;
        //确定当前权限值
        var curLimit=config.limitId?({
                insert:parent.console.haveLimit(config.limitId,"01"),
                remove:parent.console.haveLimit(config.limitId,"02"),
                edit:parent.console.haveLimit(config.limitId,"03"),
                list:parent.console.haveLimit(config.limitId,"00")
            }):limit;

        //添加模板到当前页面
        $("#"+form).length===0||($("#"+form).remove());
        $(to).html(mt.propertyForm({
            id:form,
            name:name,
            title:title,
            local:local,
            operate:operate[curOperate],
            property:config.property,
            config:config,
            data:customData||(curData?curData[idx]:null),
            index:idx,
            limit:curLimit,
            column:col||{offset:2,value:8}
        }));
        !afterHtml||(typeof(afterHtml)!=="function")||afterHtml();
        !config.afterHtml||(typeof(config.afterHtml)!=="function")||config.afterHtml();

        //绑定数据提交事件
        $("#"+form+"_submit").click(function(){
            submitData(urlconfig,$(this).attr("name"),$(this),form,extra);
        });
        //绑定数据删除事件
        $("#"+form+"_remove").click(function(){
            removeData(urlconfig,$(this).attr("name"),$(this),false,customData);
        });
        //绑定输入框校验事件
        for (var key in config.property)
        {
            var prop=config.property[key];
            if((prop.type==="input"||prop.type==="custom")&&(prop.must||(curOperate===1&&prop.insertMust)))
            {
                $("#"+form).find("#"+key).data({match:prop.match,alert:prop.alert});
                $("#"+form).find("#"+key).blur(function(){
                    !valNullCheck(this)||!$(this).data().match
                            ||valMatchCheck(this,$(this).data().match,$(this).data().alert);
                });
            }
            prop.type!=="template"||(prop.unbind(),prop.bind());
        }
        //定制绑定项目
        !config.bind||!config.bind["form"]||(config.bind["form"]());
    }
    index.displayPropertyForm=displayPropertyForm;

    /**
     * loadSubContent - 载入子内容网页
     * @param {type} url - 子内容链接地址
     */
    function loadSubContent(url)
    {
        $("#sub-content").html(parent.console.loading()||"");
        $("#contentFrame").attr("src",url);
    }
    index.loadSubContent=loadSubContent;

    /**
     * bindCommonEvent:绑定通用事件
     */
    function bindCommonEvent()
    {
        //链接跳转事件
        $("a[link]").each(function(){
            $(this).attr("onclick")||(
                $(this).attr("onclick","true"),
                $(this).click(function(){
                    if($(this).attr("link")==="") return;
                    //显示载入指示器
                    !parent.console.infor || (parent.console.infor(local.loading));
                    parent.console.switching($(this).attr("link"));
                }));
        });
        //链接内容切换事件
        $("a[load]").each(function(){
            $(this).attr("onclick")||(
                $(this).attr("onclick","true"),
                $(this).click(function(){
                    if($(this).attr("load")==="") return;
                    loadSubContent($(this).attr("load"));
                }));
        });
        //弹出对话框
        $("[action-mode='data-dialog']").each(function(){
            $(this).attr("onclick")||(
                $(this).attr("onclick","true"),
                $(this).click(function(){
                    displayPropertyDialog(urlconfig,
                        $(this).attr("name"),
                        $(this).attr("name-dialog"),
                        $(this).attr("title"),
                        $(this).attr("data-index"),
                        $(this).attr("data-extra"),
                        $(this).attr("data-content")?eval("("+$(this).attr("data-content")+")"):null
                    );
                }));
        });
        //显示属性框
        $("[action-mode='data-display']").each(function(){
            $(this).attr("onclick")||(
                $(this).attr("onclick","true"),
                $(this).click(function(){
                    displayPropertyForm(urlconfig,
                        $(this).attr("name"),
                        $(this).attr("form-name"),
                        $(this).attr("form-to"),
                        $(this).attr("title"),
                        $(this).attr("data-index"),
                        $(this).attr("data-extra"),
                        $(this).attr("form-col")
                    );
                }));
        });
        //数据操作-删除
        $("[action-mode='data-remove']").each(function(){
            $(this).attr("onclick")||(
                $(this).attr("onclick","true"),
                $(this).click(function(){
                    removeData(urlconfig,$(this).attr("name"),$(this),false);
                }));
        });
        //绑定数据搜索事件
        $("button[action-mode='data-search']").each(function(){
            $(this).attr("onclick")||(
                $(this).attr("onclick","true"),
                $(this).click(function(){
                    urlconfig.customSearch?
                        (urlconfig.customSearch($("#"+$(this).attr("name")).val(),curData))
                        :(requestData(urlconfig,$(this).attr("name")));
                }));
        });
        $("input[action-mode='data-search']").each(function(){
            $(this).attr("onkeyup")||(
                $(this).attr("onkeyup","true"),
                $(this).keyup(function(e){
                    if(e.keyCode===13)
                    {
                        urlconfig.customSearch?
                            (urlconfig.customSearch($(this).val(),curData))
                            :(requestData(urlconfig,$(this).attr("action-name")));
                    }
                }));
        });
        //弹出提示框
        $("[action-mode='popover']").each(function(){
            $(this).attr("onclick")||(
                $(this).popover(),
                $(this).attr("onclick","true"),
                $(this).click(function(){
                    $(this).toggleClass("active");
                    $(this).children("b").html($(this).hasClass("active")?local.action["close"]:$(this).attr("init-title"));
                    $(this).children("span").toggleClass("glyphicon-plus");
                    $(this).children("span").toggleClass("glyphicon-remove");
                    !$(this).hasClass("active")||$(".popover").find("a[load]").click(function(){
                        if($(this).attr("load")==="") return;
                        loadSubContent($(this).attr("load"));
                    });
                }));
        });
        //数据提交事件
        $("[action-mode='submit']").each(function(){
            $(this).attr("onclick")||(
                    $(this).attr("onclick","true"),
                    $(this).click(function(){
                        //读取链接设置
                        // var config = (urlconfig.type === "multi") ? urlconfig[$(this).attr("name")] : urlconfig;
                        var idx=$(this).attr("data-index");
                        //记录操作模式
                        curOperate=(idx!==null&&idx!==undefined)?3:1;
                        //确定当前权限值
                        // var curLimit=config.limitId?({
                        //         insert:parent.console.haveLimit(config.limitId,"01"),
                        //         remove:parent.console.haveLimit(config.limitId,"02"),
                        //         edit:parent.console.haveLimit(config.limitId,"03"),
                        //         list:parent.console.haveLimit(config.limitId,"00")
                        //     }):limit;
                        submitData(urlconfig,$(this).attr("name"),$(this),$(this).attr("data-to"),$(this).attr("data-extra"),idx);
                    }));
        });
        //分页处理事件
        $("[action-mode='data_first']").each(function(){
            $(this).attr("onclick")||(
                    $(this).attr("onclick","true"),
                    $(this).click(function(){
                        curPage === 1 || (curPage = 1, requestData(urlconfig,$(this).attr("name")));
                    }));
        });
        $("[action-mode='data_prev']").each(function(){
            $(this).attr("onclick")||(
                    $(this).attr("onclick","true"),
                    $(this).click(function(){
                        curPage === 1 || (curPage = Math.max(--curPage, 1),requestData(urlconfig,$(this).attr("name")));
                    }));
        });
        $("[action-mode='data_page']").each(function(){
            $(this).attr("onclick")||(
                    $(this).attr("onclick","true"),
                    $(this).click(function(){
                        curPage === parseInt($(this).html()) || (curPage = parseInt($(this).html()),requestData(urlconfig,$(this).attr("name")));
                    }));
        });
        $("[action-mode='data_next']").each(function(){
            $(this).attr("onclick")||(
                    $(this).attr("onclick","true"),
                    $(this).click(function(){
                        curPage === totalPage || (curPage = Math.min(++curPage, totalPage),requestData(urlconfig,$(this).attr("name")));
                    }));
        });
        $("[action-mode='data_last']").each(function(){
            $(this).attr("onclick")||(
                    $(this).attr("onclick","true"),
                    $(this).click(function(){
                        curPage === totalPage || (curPage = totalPage,requestData(urlconfig,$(this).attr("name")));
                    }));
        });
    }
    index.bindCommonEvent=bindCommonEvent;

    /*元素全部载入后执行*/
    $(function () {
        /*处理模版显示*/
        //顶部导航栏
        local.userNick=parent.console.loginUser.userNick?
                        parent.console.loginUser.userNick
                        :parent.console.loginUser.userName;
        $(".navbar").append(mt.topbar(local));
        $("#template_navbar").remove();
        //左侧菜单
        $(".sidebar").append(mt.leftMenu({
            local:local,
            active:active,
            modules:parent.console.pluginsForModule(active),
            apps:parent.console.loginUser.apps,
            limit:limit
        }));
        $("#template_navbar").remove();
        //中心内容
        $(".main").append(template("template_main",{
            local:local,
            active:active,
            icon:parent.console.pluginPropertyById(active,"pluginIcon"),
            title:parent.console.pluginNameById(active),
            limit:limit,
            link:parent.console.getLastestLoad()
        }));
        $("#template_main").remove();
        $("#sub-content").html(parent.console.loading()||"");

        /*绑定按钮事件*/
        //用户信息修改
        $("[action-mode='user-config']").click(function() {

        });
        //缓存数据更新
        $("[action-mode='data-refresh']").click(function() {
            !parent.console.reload || (parent.console.reload());
        });
        //前端系统设置
        $("[action-mode='system-config']").click(function() {

        });
        //用户注销系统
        $("[action-mode='user-logout']").click(function() {
            !parent.console.logout || (parent.console.logout());
        });
        //通用事件绑定
        bindCommonEvent();

        /*关闭上级提示框*/
        !parent.console.infor||(parent.console.infor());

        /*数据读取*/
        if(limit.list&&urlconfig&&urlconfig.initKey)
        {
            if(typeof(urlconfig.initKey)==="function")
            {
                urlconfig.initKey();
            }
            else
            {
                var keys=urlconfig.initKey.split("|");
                for(var i=0;i<keys.length;i++){requestData(urlconfig,keys[i]);}
            }
        }
        //无查询权限
        if(!limit.list){alertMessage("warning",local.error_infor["003"]);}

        /*测试*/
    });
})(index,urlconfig);