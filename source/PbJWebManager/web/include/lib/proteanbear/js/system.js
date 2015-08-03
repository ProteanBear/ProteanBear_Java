/** 
 * 描述：JS库-载入器方法
 * 更新：20130314 - 组件版（用于与jQuery配合使用，去除载入包等方法）
 * 版本：3.01(components)-20130314
 */

/*初始化命名空间*/
var pb=pb||{};

/*添加基本方法库-系统及载入器*/
(function(pb)
{
    /*定义系统类*/
    var System=function()
    {
        /*--------------------私有属性*/
        var place=0;								//搜索位置
        var searchStr=null;							//搜索字符串
        var detect=null;							//浏览器信息
        var os=null;								//操作系统
        var browser=null;							//浏览器类型
        var version=null;							//浏览器版本
        var url=null;								//当前页面地址信息
        var main=null;								//当前页面目录信息
        var root=null;								//页面根目录信息
        var param=null;								//当前get参数信息
        var params=null;                                                        //当前get参数信息数组
        var pbhome=null;							//pb框架库所在根目录
        var isSupport3d=null;                                                   //是否支持Css 3d 定位

        /*--------------------公共属性*/
        this.isIE=false;                                                        //是否IE浏览器
        this.isFF=false;                                                        //是否Firefox浏览器
        this.isOP=false;                                                        //是否Opera浏览器
        this.isSA=false;                                                        //是否Safari浏览器
        this.isCH=false;                                                        //是否Chrome浏览器
        this.isMobile=false;							//是否为手机
        this.isAndroid=false;							//是否为Android手机
        this.isiPhone=false;							//是否为iPhone手机
        this.isiPad=false;							//是否为iPad手机
        
        /*--------------------私有方法*/
        /*check：从信息字符串里检索指定字符串*/
        function check(string)
        {
            if(detect===null){this.detect();}
            place=detect.indexOf(string)+1;
            searchStr=string;
            return place;
        }

        /*initParam：初始化公共属性*/
        function initParam(tar)
        {
            tar.isIE=false;
            tar.isFF=false;
            tar.isOP=false;
            tar.isSA=false;
            tar.isCH=false;
        }
        
        /*generateParam:生成参数字符串*/
        function generateParam(array)
        {
            var i=0;
            var result="";
            array=(array)?array:params;
            for(var name in array)
            {
                if(name==="clone"||name==="extend") continue;
                if(!params[name]) continue;
                if(i!==0) result+="&";
                result+=((name==="")?"":(name+"="))+params[name];
                i++;
            }
            return result;
        }
        /*--------------------公共方法*/
        /*init:初始化系统参数类*/
        this.init=function()
        {
            this.detect();
            this.os();
            this.browser();
            this.url();
            this.main();
            this.root();
            this.param();
            //this.home();
        };

        /*reload:重新载入系统参数*/
        this.reload=function(){this.init();};

        /*detect:获取浏览器信息字符串*/
        this.detect=function()
        {
            if(detect===null) detect=navigator.userAgent.toLowerCase();
            return detect;
        };

        /*os:获取操作系统信息*/
        this.os=function()
        {
            if(os===null)
            {
                if(check("linux")) 		os="Linux";
                else if(check("x11")) 		os="Unix";
                else if(check("mac")) 		os="Mac";
                else if(check("win")) 		os="Windows";
                else 				os="other system";

                if(check("mobile"))             {os="Mobile";this.isMobile=true;}
                if(check("Android"))            {os="Android";this.isAndroid=true;}
                if(check("iPhone"))             {os="iPhone";this.isiPhone=true;}
                if(check("iPad"))               {os="iPad";this.isiPad=true;}
            }
            return os;
        };

        /*browser:获取浏览器类型*/
        this.browser=function()
        {
            if(browser===null)
            {
                if(check("msie"))				{browser="Internet Explorer";initParam(this);this.isIE=true;this.version();}
                else if(check("firefox"))			{browser="Firefox";initParam(this);this.isFF=true;this.version();}
                else if(check("opera"))                         {browser="Opera";initParam(this);this.isOP=true;this.version();}
                else if(check("safari")&&!check("chrome")) 	{browser="Safari";initParam(this);this.isSA=true;this.version();}
                else if(check("chrome"))			{browser="chrome";initParam(this);this.isCH=true;this.version();}
                else 						{browser="other browser";initParam(this);}
            }
            return browser;
        };

        /*version:获取浏览器版本*/
        this.version=function()
        {
            if(version===null)
            {
                var index=place+searchStr.length;
                var temp=detect.charAt(index);
                version="";
                do
                {
                    version+=temp;
                    index++;
                    temp=detect.charAt(index);
                }while(temp!==";"&&temp!==" "&&index<detect.length); 
            }
            return version;
        };
        
        /*isIE6:是否为IE6*/
	this.isIE6=function(){return (this.isIE&&(version==="6.0"||version==="7.0"));};
	 
	/*url:获取当前页面地址信息*/
	this.url=function()
        {
            if(url===null) url=location.href;
            return url;
        };
	 
	/*main:当前页面目录信息*/
	this.main=function()
	{
            if(url===null) 	this.url();
            if(main===null)	main=(url.indexOf("/")!==-1)?url.substring(0,url.lastIndexOf("/")+1):null;
            return main;
	};
	 
	/*root:页面根目录信息*/
        this.root=function()
        {
            if(main===null) this.main();
            if(root===null)
            {
                if(main.indexOf("file")===0){}
                else
                {
                    root=/http:\/\/([\w-]+\.)*(\w+)(:(\d)+)?\//.exec(main)[0];
                }
            }
            return root;
        };
	 
	/*param:当前get参数信息*/
	this.param=function(key,value,string)
	{
            //获取当前的get参数
            param||(param=(location.search.length===0)?"":location.search.substring(1));
            
            //如果指定了参数字符串，则替换为指定的参数字符串
            var temp=string||param;
            
            //生成参数数组
            var paramArr=temp.split("&");
            params={};
            for(var i=0,j,t;i<paramArr.length; i++)
            {
                j=paramArr[i];
                t=j.substring(0,j.indexOf("="));
                params[t] = j.substring(j.indexOf("=")+1,j.length);
            }
            
            //组合参数对象
            var result={
                string:param,
                map:params,
                length:paramArr.length
            };
            
            //判断是否指定键名,未指定键值返回当前对象
            if(!key)    return result;
            
            //判断是否指定键值，指定则设置键值
            if(value)
            {
                params[key]=value;
                //更新字符串
                result.string=generateParam(result.map);
            }
            
            //增加键名对属性
            result.key=key;
            result.value=params[key]||null;
            
            //增加单个参数处理方法
            result.remove=function()
            {
                //删除参数
                !this.key||(delete this.map[this.key]);
                //更新字符串
                this.string=generateParam(this.map);
                return this;
            };
            
            return result;
	};
         
        /*isSupport3d:是否支持3d定位（transform:translate3d(0,0,0)）*/
        this.isSupport3d=function()
        {
            try
            {
                if(isSupport3d===null)
                {
                    var div;
                    if("styleMedia" in window)
                    {
                        isSupport3d=window.styleMedia.matchMedium("(-webkit-transform-3d)");
                    }
                    else
                    {
                        if("media" in window)
                        {
                            isSupport3d=window.media.matchMedium("(-webkit-transform-3d)");
                        }
                    }
                    if(!isSupport3d)
                    {
                        if(!document.getElementById("supportsThreeDStyle"))
                        {
                            var d=document.createElement("style");
                            d.id="supportsThreeDStyle";
                            d.textContent="@media (transform-3d),(-o-transform-3d),(-moz-transform-3d),(-ms-transform-3d),(-webkit-transform-3d) { #supportsThreeD { height:3px } }";
                            document.querySelector("head").appendChild(d);
                        }
                        if(!(div=document.querySelector("#supportsThreeD")))
                        {
                            div=document.createElement("div");
                            div.id="supportsThreeD";
                            document.body.appendChild(div);
                        }
                        isSupport3d=(div.offsetHeight===3);
                    }
                }
                return isSupport3d;
            }
            catch(c)
            {
                return false;
            }
        };
	 
        /*初始化系统信息类*/
        this.init();
    };
    /*创建系统属性对象*/
    pb.sys=new System();
    
    /*对象扩展方法*/
    pb.expand=function(target,source)
    {
        for(var fn in source) target[fn]=source[fn];
        return target;
    };
})(pb);