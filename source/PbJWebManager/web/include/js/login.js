var login=login||{};

(function(login){
    /*获取本地语言设置*/
    var local=parent.local||{};
    
    /*元素全部载入后执行*/
    $(function()
    {
        /*处理模版显示*/
        $("body").append(template("login_template",local));
        $("#login_template").remove();
        
        /*绑定按钮事件*/
        //验证码切换
        $("#verifyImage").mouseover(function(){
            $('#verifyImage').tooltip();
        });
        $("#verifyImage").click(function(){
            changeVerify(this);
        });
        //登录
        $("#login_action").click(userLogin);
        $("#verycode").keyup(function(e){
            if(e.keyCode===13) userLogin();
        });
        //重置
        $("#login_reset").click(loginReset);
        
        /*关闭上级提示框*/
        !parent.console.infor||(parent.console.infor());
        
        /*切换图片处理*/
        if($("[action-mode='switch']").length>0) loadImages();
        
        /*测试*/
        //alertMessage("danger","000");
    });
    
    /**
     * alertMessage:弹出显示提示信息
     * @param mode danger|success|info|warning
     * @param message - 错误信息
     */
    function alertMessage(mode,message)
    {
        //元素存在，清除元素
        if ($(".alert").length !== 0) $(".alert").remove();

        //添加元素
        $(".form-signin").before(mt.messageAlert({
            mode:mode,
            alert: local.message[mode],
            message: message
        }));

        //弹出元素
        $(".alert").fadeIn("fast", function() {$(".alert").addClass("in");});
    }
    
    /*setInput:设置输入状态*/
    function setInput(log)
    {
        log=(log===null||log===undefined||log==="")?true:log;
        if(log)
        {
            $(".btn").addClass("disabled");
            $(".btn").attr("disabled","disabled");
            $("input").attr("disabled","disabled");
            
            $("#login_action").html(
                (parent.console.loading?
                    parent.console.loading(24)
                    :$("#login_action").attr("data-loading-text"))
            );
        }
        else
        {
            $(".btn").removeClass("disabled");
            $(".btn").removeAttr("disabled");
            $("input").removeAttr("disabled");
            
            $("#login_action").html(local.login_action);
        }
    }
    /*loginFail:登录失败显示错误信息*/
    function loginFail(infor)
    {
        //清空输入
        $("#userName").val("");
        $("#password").val("");
        $("#verycode").val("");
        changeVerify($("#verifyImage"));
        //抖动输入框
        shake();
        //显示错误信息
        alertMessage("danger",infor);
    }
    
    /*userLogin:用户登录*/
    function userLogin()
    {
        //读取信息
        var username=$("#userName").val();
        var password=$("#password").val();
        var verycode=$("#verycode").val();
        var remember=$("#remember").get(0).checked;
        
        //判断空输入
        if(username===""||password==="")
        {
            alertMessage("warning",local.warning_infor["000"]);
            return;
        }
        if(verycode==="")
        {
            alertMessage("warning",local.warning_infor["002"]);
            return;
        }
        
        //清除提示框
        $("body").removeClass("error");
        $(".alert").remove();
        //显示登录状态
        setInput(true);
        
        //用户登录
        if(parent.console.login)
        {
            parent.console.login(username,password,verycode,remember,{
                complete: function(XHR, TS){setInput(false);},
                successError:function(infor){loginFail(infor);},
                error: function(XHR, infor){loginFail(infor);}
            });
        }
    }
    /*loginReset:登录输入重置*/
    function loginReset()
    {
        //清空输入
        $("#userName").val("");
        $("#password").val("");
        $("#verycode").val("");
        changeVerify($("#verifyImage"));
        
        //关闭提示
        $(".alert").alert("close");
        
        //抖动输入框
        shake();
    }
    
    /*changeVerify:更换验证码图片*/
    function changeVerify(verify)
    {
        verify.src=addDateToUrl(verify.src);
    }
    /*addDateToUrl:为链接增加时间戳参数*/
    function addDateToUrl(url,params)
    {
        params=(params===undefined||params===null)?"?":params;
	var timestamp = (new Date()).valueOf();
	url = url +params+ "timestamp=" + timestamp;
	return url;
    }
    
    /*adImage:广告图片*/
    //图片集合
    var adImages=[];
    //当前索引
    var imageIndex=-1;
    //设置切换间隔
    var interval=6000;
    //加载方法
    function loadImages()
    {
        //Ajax访问服务器，登录服务器
        $.ajax({
            type:'POST',
            url:"response/login_images.json",
            data:{},
            dataType:"json",
            complete:function(XHR, TS){},
            error:function(XHR, infor){},
            success:function(data,textStatus,jqXHR)
            {
                if(data!==null)
                {
                    if(data.success)
                    {
                        adImages=data.list&&data.list.length>0?data.list:adImages;
                        setInterval(switchImage,interval);
                    }
                }
            }
        });
    }
    //切换方法
    function switchImage()
    {
        if(adImages&&adImages.length>0)
        {
            //计算索引
            imageIndex=(++imageIndex>(adImages.length-1))?0:imageIndex;
            //获取数据
            var imageSrc=adImages[imageIndex].src;
            //显示效果
            $("[action-mode='switch']").fadeOut("fast",function(){
                $("[action-mode='switch']").attr("src",imageSrc);
                $("[action-mode='switch']").fadeIn("slow");
            });
        }
    }
    
    /*shake:产生抖动*/
    //抖动次数
    var shakeTimes=6;
    //抖动偏移
    var shakeOffset=30;
    //抖动频率
    var shakeInterval=15;
    //抖动波动
    var shakeWave=0;
    //抖动方法
    function shake()
    {
        //改变样式
        if(shakeWave===0)
        {
            $(".container").css("padding-left",shakeOffset+"px");
            shakeWave=1;
        }
        else
        {
            $(".container").css("padding-left","0px");
            shakeWave=0;
            shakeTimes--;
        }
        
        if(shakeTimes>0)
        {
            setTimeout(shake,shakeInterval);
        }
        else
        {
            shakeTimes=5;
        }
    }
    
})(login);