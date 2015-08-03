/**
 * 说明：	处理WEB本地储存信息，如果浏览器支持HTML5 localStorage本地储存则使用WEB本地储存方式；如果不支持则使用Cookie方式。
 * 作者：	Maqiang(ProteanBear)
 * 创建时间：	2013-2-28
 * 修改日志：	
 * 计划改进：	
 * 当前版本：	1.01
 */

/*初始化命名空间*/
var pb=pb||{};
/*对象扩展方法*/
pb.expand||(pb.expand = function(target, source)
{
    for (var fn in source)
        target[fn] = source[fn];
    return target;
});

/**
 * storage:WEB储存封装类。
 * @param config - 相关属性配置
 */
pb.storage=function(config)
{
    var self=this;
    
    /*配置相关属性*/
    //过期时间（仅使用Cookie时有效）
    this.expireTime=null;
    //Cookie访问路径
    this.path=null;
    //Cookie访问主机
    this.domain=null;
    //Cookie安全性
    this.sesure=null;
    //当前页面使用储存模式
    this.currentMode=null;
    pb.expand(this,config);
    
    /**
     * init:初始化遮盖层相关设置
     */
    this.init=function(){};
    
    /**
     * getValue:获取指定键的相关值
     * @param name - 变量名
     * @return 键名对应的结果值 
     */
    this.getValue=function(name)
    {
        var value=null;
        if(localStorage)
        {
            this.currentMode="localStorage";
            value=localStorage.getItem(name);
        }
        else
        {
            this.currentMode="Cookie";
            value=getValueFromCookie(name);
        }
        return value;
    };
    
    /**
     * setValue:设置指定变量的相关值
     * @param name - 变量名
     * @param value - 变量值
     */
    this.setValue=function(name,value)
    {
        if(localStorage)
        {
            this.currentMode="localStorage";
            localStorage.setItem(name,value);
        }
        else
        {
            this.currentMode="Cookie";
            setValueIntoCookie(name,value);
        }
    };
    
    /**
     * remove:删除指定变量的值
     * @param name - 变量名
     */
    this.remove=function(name)
    {
        if(localStorage)
        {
            this.currentMode="localStorage";
            localStorage.removeItem(name);
        }
        else
        {
            this.currentMode="Cookie";
            removeFromCookie(name);
        }
    };
    
    /**
     * setValueIntoCookie:向Cookie中设置指定变量的相关值
     * @param name - 变量名
     * @param value - 变量值
     */
    function setValueIntoCookie(name,value)
    {
        var strCookie=name+"="+escape(value);
        //默认保留15天
        self.expireTime=(self.expireTime===null)?15*24*3600*1000:self.expireTime;
        
        var date=new Date();
        date.setTime(date.getTime()+self.selfexpireTime);
        strCookie+=";expire="+date.toGMTString();
        
        if(self.path!==null)   strCookie+=";path="+self.path;
        if(self.domain!==null) strCookie+=";domain="+self.domain;
        if(self.secure!==null) strCookie+=";"+self.secure;
        
        try{document.cookie=strCookie;}
        catch(e){throw e;}
    }
    
    /**
     * getValueFromCookie:从Cookie中获取指定键的相关值
     * @param name - 变量名
     * @return 键名对应的结果值 
     */
    function getValueFromCookie(name)
    {
        var strCookie=document.cookie;
        var arrCookie=strCookie.split(";");
        var arr; 
        var theValue="";
        
        for(var i=0;i<arrCookie.length;i++)
        {
            arr=arrCookie[i].split("=");
            if(arr[0]===name)
            {
                theValue=unescape(arr[1]);
                break;
            }
        }
        return theValue;
    }
    
    /**
     * removeFromCookie:从Cookie中删除指定变量的值
     * @param name - 变量名
     */
    function removeFromCookie(name)
    {
        var date=new Date();
        date.setTime(date.getTime()-10000);
        document.cookie=name+"=null;expire="+date.toGMTString();
    }
    
    this.init();
};