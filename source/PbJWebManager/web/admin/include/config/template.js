/**
 * Created by maqiang on 14-7-2.
 * 公用模板设置
 */
var mt=mt||{};

(function(mt){
    
    /*左侧菜单栏*/
    mt.leftMenu=template.compile(''
            //首页
            +'<ul class="nav nav-sidebar">' 
                +'<li class=\'<!--[=(!active||active==="welcome"?"active":"")]-->\'>' 
                    +'<a href="#" link=\'<!--[=(active!=="welcome"?"index.html?active=welcome":"")]-->\'>' 
                    +'<span class="glyphicon glyphicon-home"></span><!--[=local.menu_index]-->' 
                    +'</a>' 
                +'</li>' 
            +'</ul>'
            //根据给定的插件自动生成菜单
            +'<!--[for(var module in modules){]-->'
                //所属模块不同时生成模块代码
                +'<div class="panel <!--[=(modules[module].isActive)?"panel-success":"panel-info"]-->">'
                    +'<div class="panel-heading">' 
                        +'<h3 class="panel-title"><!--[=module]--></h3>' 
                    +'</div>' 
                    +'<div class="panel-body">' 
                        +'<ul class="nav nav-sidebar">'
                            //生成模块下的全部插件
                            +'<!--[for(var j=0;j<modules[module].plugins.length;j++){]-->'
                                +'<!--[var plugin=modules[module].plugins[j];]-->'
                                +'<li class="<!--[=(active===plugin.pluginId?"active":"")]-->">'
                                    +'<a href="#" link="<!--[=(active===plugin.pluginId)?"":(plugin.pluginLink+"?active="+plugin.pluginId)]-->">'
                                        +'<span class="glyphicon <!--[=plugin.pluginIcon]-->"></span><!--[=plugin.pluginName]-->'
                                    +'</a>' 
                                +'</li>' 
                            +'<!--[}]-->'
                        +'</ul>'
                    +'</div>' 
                +'</div>'
            +'<!--[}]-->'
        );
})(mt);