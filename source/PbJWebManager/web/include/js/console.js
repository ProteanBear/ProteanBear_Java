/**
 * 脚本说明：	使用Iframe方式，根据登录情况载入登录页面和控制主页面。<br/>
 *              使用Ajax方式访问服务后台，检查登录情况，便于控制显示当前系统状态，不受页面切换的影响。<br/>
 * 脚本作者：     ProteanBear
 * 创建时间：	2013-2-27
 * 修改日志：	1.1 - 修改为jQuery基本版本
 * 计划改进：	
 * 当前版本：	1.1 2013-3-13
 */

/*初始化命名空间*/
var console=console||{};

/**
 * 控制台通用处理:
 * 添加控制台相关载入初始化处理
 * @param pb - 
 * @param console - 
 */
(function(pb,console){
    
    /*声明全局变量*/
    //设置是否使用本地测试数据
    console.isUseSimulation=false;
    //模拟数据的相对路径
    console.simulationData="../test/response/";
    //图片载入路径地址
    console.imagePath="images/";
    //记录使用的遮盖层对象
    console.overLay=null;
    //记录使用的本地数据存储对象
    console.storage=null;
    //记录当前的命名空间
    var href=document.location.href;
    href=href.substr(0,href.lastIndexOf("/"));
    href=href.substr(href.lastIndexOf("/")+1);
    console.namespace=href;

    //记录本地存储登录用户名的变量名（同时也为请求参数名）
    console.nameOfLoginUser="loginUser";
    //记录本地储存登录密码的变量名（同时也为请求参数名）
    console.nameOfLoginPass="loginPass";
    //记录请求参数名-验证码
    console.nameOfVerifyCode="verycode";
    //记录当前访问的页面URL
    console.nameOfCurrentUrl="currentUrl";
    //记录当前载入的子页面URL
    console.nameOfCurrentLoad="currentLoad";

    //记录载入图片地址
    console.imageOfLoading=console.imagePath+"overlay-load.gif";
    //记录错误图片地址
    console.imageOfError=console.imagePath+"overlay-close.jpg"; 

    //记录登录页面地址
    console.urlOfLogin="login.html";
    //记录平台主页面地址
    console.urlOfIndex="index.html";
    //记录平台欢迎页面地址
    console.urlOfWelcome="sub_welcome.html";
    //记录登录请求地址
    console.urlOfUserLogin=(console.isUseSimulation)?(console.simulationData+"login.json"):"../systemUserLogin";
    //记录缓存信息更新地址
    console.urlOfUserReload=(console.isUseSimulation)?(console.simulationData+"reload.json"):"../systemUserReload";
    //记录登录退出地址
    console.urlOfUserLogout=(console.isUseSimulation)?(console.simulationData+"logout.json"):"../systemUserLogout";

    /*记录当前的登录用户的数据信息*/
    //结构为{success:,userId:,userName:,areaId:,areaName:,plugins:[],limit:"",apps:[]}
    console.loginUser=null;
    console.userLimitArray=null;
    console.record={};
    
    /**
     * init:页面初始化方法
     */
    console.init=function()
    {
        //初始化全局变量
        console.storage=new pb.storage();
        
        //清除相关本地储存
        /*if(console.storage)
        {
            console.storage.remove(console.namespace+"."+console.nameOfLoginUser);
            console.storage.remove(console.namespace+"."+console.nameOfLoginPass);
            console.storage.remove(console.namespace+"."+console.nameOfCurrentUrl);
        }*/

        //修改相关尺寸
        console.resize();

        //读取Storage，检查登录状态
        console.loginCheck();
    };

    /**
     * resize:页面尺寸更改处理方法，修改遮盖层和iframe框架的大小
     */
    console.resize=function()
    {
        //调整iframe框架的长宽
        $("#sysFrame").height($(window).height()-4);
    };
    //绑定尺寸处理事件
    window.onresize=console.resize;

    /**
     * switching:子页面切换方法，更换iframe中的子页面
     * @param url - 指定子页面的地址
     */
    console.switching=function(url)
    {
        $("#sysFrame").attr("src",url);
        url===console.urlOfLogin||console.storage.setValue(console.namespace+"."+console.nameOfCurrentUrl,url);
    };
    
    /**
     * loading:返回载入中HTML模版
     * @param loadSize - 载入区域的大小
     */
    console.loading=function(loadSize)
    {
        loadSize=loadSize||48;
        loadSize=Math.max(16,loadSize);
        loadSize=Math.min(64,loadSize);
        
        return template("loading_template",{loading_size:loadSize});
    };

    /**
     * infor:使用遮盖层，显示处理信息
     * @param infor - 显示的文本信息,为null时隐藏信息层
     * @param isLoad - 是否显示载入
     * @param loadSize - 载入指示器的大小
     */
    console.infor=function(infor,isLoad,loadSize)
    {
        var show=(infor!==null&&infor!==undefined);
        isLoad=(isLoad===null||isLoad===undefined)?true:isLoad;
        loadSize=loadSize||64;
        loadSize=Math.max(16,loadSize);
        loadSize=Math.min(64,loadSize);
        
        //显示
        if(show)
        {
            //信息已存在相关处理
            if($(".modal-backdrop").length===0)
            {
                //添加并显示遮盖层
                $("body").append(template("alert_template",{
                    loading:(isLoad)?console.loading(loadSize):"",
                    infor:infor
                }));
                //绑定关闭事件
                $(".close").click(function(){console.infor();});
                $(".modal-backdrop").fadeIn("fast",function(){
                    $(this).addClass("in");
                });
            }
            else
            {
                $(".modal-backdrop").children(".content").children("p")
                        .html(infor);
            }
        }
        //隐藏
        else
        {
            //隐藏并删除遮盖层
            $(".modal-backdrop").fadeOut("fast",function(){
                $(this).removeClass("in");
            });
            $(".modal-backdrop").remove();
        }
    };

    /**
     * loginCheck:检查登录状态，如果保存了登录名和密码在Cookie或localStorage中,直接跳转到主页面；
     */
    console.loginCheck=function()
    {
        //显示检测登录状态
        console.infor(local.warning_checklog);
        
        var loginUser=null,loginPass=null;
        if(console.storage!==null)
        {
            //获取指定的数据存储
            loginUser=console.storage.getValue(console.namespace+"."+console.nameOfLoginUser);
            loginPass=console.storage.getValue(console.namespace+"."+console.nameOfLoginPass);
        }

        //检查是否有本地存储值
        if(loginUser&&loginUser!==""
            &&loginPass&&loginPass!=="")
        {
            console.login(loginUser,loginPass,null,false,{error:function(jqXHR,infor){
                console.switching(console.urlOfLogin);
            }});
        }
        else
        {
            //console.infor("载入登录界面");
            console.switching(console.urlOfLogin);
        }
    };

    /**
     * saveToStorage:保存登录名和密码到本地储存中
     * @param loginUser - 登录用户名
     * @param loginPass - 登录密码
     */
    console.saveToStorage=function(loginUser,loginPass)
    {
        if(console.storage!==null)
        {
            console.storage.setValue(console.namespace+"."+console.nameOfLoginUser,loginUser);
            console.storage.setValue(console.namespace+"."+console.nameOfLoginPass,loginPass);
        }
    };

    /**
     * login:登录到服务器
     * @param loginUser - 登录用户名
     * @param loginPass - 登录密码
     * @param verifyCode - 验证码
     * @param isSave - 是否保存登录状态
     * @param resFuncs - 回调函数指针
     */
    console.login=function(loginUser,loginPass,verifyCode,isSave,resFuncs)
    {
        //默认不保存
        isSave=(isSave===null||isSave===undefined)?false:isSave;
        //组合参数
        var param=console.nameOfLoginUser+"="+loginUser
                 +"&"+console.nameOfLoginPass+"="+encodeURIComponent(loginPass)
                 +((verifyCode===null||verifyCode===undefined)?"":("&"+console.nameOfVerifyCode+"="+verifyCode));
        //URL编码
        //param=encodeURI(param);

        //Ajax访问服务器，登录服务器
        $.ajax({
            type:'POST',
            url:console.urlOfUserLogin,
            data:param,
            dataType:"json",
            complete:function(XHR, TS)
            {
                !resFuncs||!resFuncs.complete||(resFuncs.complete(XHR, TS));
            },
            success:function(data,textStatus,jqXHR)
            {
                if(data!==null)
                {
                    if(data.success)
                    {
                        //保存登录状态
                        if (isSave) console.saveToStorage(loginUser, loginPass);

                        //记录当前登录用户信息
                        console.loginUser = data;
                        
                        //回调登录页面方法
                        !resFuncs||!resFuncs.success||(resFuncs.success(data,textStatus,jqXHR));
                        
                        //显示成功信息
                        console.infor(local.login_success);
                        //显示上次登录页面
                        console.switching(console.storage.getValue(console.namespace+"."+console.nameOfCurrentUrl)
                                ||console.urlOfIndex);
                    }
                    else
                    {
                        !data.infor||!resFuncs||!resFuncs.error||(resFuncs.error(jqXHR,data.infor));
                    }
                }
                else
                {
                    !resFuncs||!resFuncs.error||resFuncs.error(jqXHR,local.warning_nullres);
                }
            },
            error:function(XHR, infor)
            {
                !resFuncs||!resFuncs.error||(resFuncs.error(XHR, infor));
            }
        });
    };

    /**
     * logout:退出登录
     */
    console.logout=function()
    {
        //URL编码
        //param=encodeURI(param);
        //显示用户注销提示
        console.infor(local.warning_logout);

        //Ajax访问服务器，注销用户
        $.ajax({
            type:'POST',
            url:console.urlOfUserLogout,
            data:"",
            dataType:"json",
            complete:function()
            {
                //保存登录状态
                console.saveToStorage("","");

                //清除当前登录用户信息
                console.loginUser = null;
                console.infor(local.warning_loadlog);
                console.switching(console.urlOfLogin);
            }
        });
    };

    /**
     * haveLimit:是否包含指定编码值的权限
     * @param pluginId - 权限标识
     * @param limitType - 权限类型。00-查询、01-添加、02-删除、03-编辑
     * @return boolean - 如果包含此权限，返回true
     */
    console.haveLimit=function(pluginId,limitType)
    {
        if(console.loginUser===null)         return false;
        if(console.loginUser.isSuperAdmin)   return true;
        if(console.userLimitArray===null)    console.userLimitArray=console.loginUser.limits.split("|");
        if(console.userLimitArray===null)    return false;
        var result=false;
        for(var i=0;i<console.userLimitArray.length;i++)
        {
            if(console.userLimitArray[i]===
                    console.pluginPropertyById(pluginId,"pluginCode")+""+limitType)
            {
                result=true;
                break;
            }
        }
        return result;
    };
    
    /**
     * pluginNameById:通过指定ID获取插件名称
     * @param id - 插件ID
     */
    console.pluginNameById=function(id)
    {
        return console.pluginPropertyById(id,"pluginName");
    };
    
    /**
     * pluginPropertyById:通过指定ID获取插件属性
     * @param id - 插件ID
     * @param property - 属性名称
     */
    console.pluginPropertyById=function(id,property)
    {
        if(console.loginUser===null)    return "";
        if(!console.loginUser.plugins)  return "";
        
        var res="";
        for(var i=0;i<console.loginUser.plugins.length;i++)
        {
            var plu=console.loginUser.plugins[i];
            if(plu.pluginId===id)
            {
                res=plu[property];
                break;
            }
        }
        return res;
    };
    
    /**
     * pluginsForModule:按照模块划分当前全部插件
     * @param active 激活的插件
     * @param display 是否过滤显示
     */
    console.pluginsForModule=function(active,display)
    {
        if(console.loginUser.plugins.length<1) return {};
        
        var res={},module=console.loginUser.plugins[0].pluginParent,plugin,plugins=[];
        var isActive=false;
        display=(display===undefined)?true:display;
        for(var i=0;i<console.loginUser.plugins.length;i++)
        {
            plugin=console.loginUser.plugins[i];
            if(plugin.pluginDisplay===0&&display) continue;
            (module!==plugin.pluginParent)?(
                            res[module]={plugins:plugins,isActive:isActive},
                            plugins=[plugin],isActive=false,
                            module=plugin.pluginParent)
                        :(plugins.push(plugin));
            isActive=(active===plugin.pluginId)?true:isActive;
        }
        res[module]={plugins:plugins,isActive:isActive};
        return res;
    };

    /**
     * reload:重新载入用户信息
     */
    console.reload=function()
    {
        //显示正在刷新信息
        console.infor(local.warning_reload);

        //Ajax访问服务器，更新缓存信息
        $.ajax({
            type:'POST',
            url:console.urlOfUserReload,
            data:"",
            dataType:"json",
            success:function(data,textStatus,jqXHR)
            {
                if(data!==null)
                {
                    if(data.success)
                    {
                        //记录当前登录用户信息
                        console.loginUser=data;
                        console.infor(local.warning_reloading);
                        console.switching(console.storage.getValue(console.namespace+"."+console.nameOfCurrentUrl)
                                ||console.urlOfIndex);
                    }
                    else
                    {
                        window.location.reload();
                    }
                }
                else
                {
                    console.infor(local.warning_nullres);
                }
            },
            error:function(){window.location.reload();}
        });
    };
    
    /**
     * getUrlParam:获取链接内的参数
     * @param name - 参数名称
     */
    console.getUrlParam=function(name)
    {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var src=$("#sysFrame").attr("src");
        var r = (src.substr(src.indexOf("?")+1)).match(reg);  //匹配目标参数
        if (r !== null) return r[2];
        return null; //返回参数值
    };
    
    /**
     * getLastestLoad:获取最近访问的子页面链接
     */
    console.getLastestLoad=function()
    {
        return console.storage.getValue(console.namespace+"."+console.nameOfCurrentLoad)
                                ||console.urlOfWelcome;
    };
    
    /*载入完成后运行*/
    $(function(){
        //初始化页面
        console.init();
        
        /*测试*/
        //console.infor("载入中",true,48);
        //*/
    });
    
})(pb,console);