/**
 * Created by maqiang on 14-7-29.
 * 子页面载入，载入后调用父页面方法，将内容复制到父页面，用于动态载入中心内容
 */

var subload = subload || {};
var urlconfig = urlconfig || {};

(function(){
    /*获取本地语言设置*/
    var local=parent.parent.local||{};
    //记录当前页面插件的标识
    var active=parent.parent.console.getUrlParam("active")?parent.parent.console.getUrlParam("active"):"welcome";
    //记录当前页面插件的权限
    var limit={
            insert:parent.parent.console.haveLimit(active,"01"),
            remove:parent.parent.console.haveLimit(active,"02"),
            edit:parent.parent.console.haveLimit(active,"03"),
            check:parent.parent.console.haveLimit(active,"04"),
            list:parent.parent.console.haveLimit(active,"00")
        };
    
    /**
     * loadComplete:调用父页面方法将当前页面内容传递给父页面
     * @param {type} data description
     */
    function loadComplete(data)
    {
        //获取要求父类动态载入的JS
        var files="";
        var config={};
        $("link[for='parentLoad']").each(function(){
            files===""||(files+="|");
            files+=$(this).attr("href");
        });
        $("script[for='parentLoad']").each(function(){
            files===""||(files+="|");
            files+=$(this).attr("src");
        });
        $("textarea[for='parentLoad']").each(function(){
            $(this).attr("class")!=="ckeditor"||(
                config["ckeditor-"+$(this).attr("id")]=config["ckeditor-"+$(this).attr("id")]||{},
                config["ckeditor-"+$(this).attr("id")].id=$(this).attr("id"),
                config["ckeditor-"+$(this).attr("id")].editor=eval($(this).attr("editor"))
            );
        });
        
        !parent.index.copeHtmlTo||(
            parent.index.copeHtmlTo(
                $(".main").html(),
                "#sub-content",
                files,config,data
            )
        );
    }
    
    /**
     * requestData:数据读取请求
     * @param config description
     * @param name description
     */
    function requestData(config,name)
    {
        //非空判断
        if (!config) return;

        //读取链接设置
        config = (config.type === "multi") ? config[name] : config;

        //显示载入指示器
        $(config.to).html(parent.parent.console.loading()||"");

        //获取提交数据
        var params = {},value="";
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
        //附属参数
        if(config.extra)
        {
            for (var key in config.extra)
            {
                params[key] = typeof (config.extra[key]) === "function" ?
                    config.extra[key]() : config.extra[key];
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
                        //(config.save===false)||(curData=data.list);
                        //空数据提示
                        !data.list||data.list.length>0||(parent.index.alertMessage("warning",local.warning_infor["001"]));
                        //显示数据内容
                        !config.success||((typeof(config.success)==="function")?
                                   (config.success(local,data.list,limit,config.access,null,data))
                                   :($(config.to).html(template(config.success,{local:local,data:data,limit:limit}))));
                        //结束载入
                        loadComplete(data);
                    }
                    else
                    {
                        parent.index.alertMessage("danger",data.infor);
                    }
                }
                else
                {
                    parent.index.alertMessage("danger",local.error_infor["004"]);
                }
            },
            error: function(XHR, infor)
            {
                //弹出错误信息
                parent.index.alertMessage("danger",local.error_infor["005"]);
            }
        });
    }
    
    /*元素全部载入后执行*/
    $(function () {
        /*处理模版显示*/
        //中心内容
        (parent.index.getSubUrlParam("mode")||"list")==="edit"||
            ($(".main").append(template("template_main",{
                local:local,
                mode:parent.index.getSubUrlParam("mode")||"",
                sectionName:parent.index.getSubUrlParam("sectionName")||"",
                sectionCode:parent.index.getSubUrlParam("sectionCode")||"",
                appCode:parent.index.getSubUrlParam("appCode")||""
            })),$("#template_main").remove());

        /*数据读取*/
        if(limit[parent.index.getSubUrlParam("mode")||"list"]
                &&urlconfig&&urlconfig.initKey)
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
        else
        {
            loadComplete();
        }

        /*测试*/
        //loadComplete();
    });
})(subload,urlconfig);