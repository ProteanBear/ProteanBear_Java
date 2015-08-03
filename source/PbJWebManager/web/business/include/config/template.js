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
                +'<li class="active">' 
                    +'<a href="#" load="sub_welcome.html")>' 
                    +'<span class="glyphicon glyphicon-home"></span><!--[=local.menu_index]-->' 
                    +'</a>' 
                +'</li>' 
            +'</ul>'
            //根据给定的应用生成应用管理模块
            +'<!--[if(apps){]-->'
                +'<!--[for(var i=0;i<apps.length;i++){]-->'
                    +'<div class="panel panel-info">'
                        +'<div class="panel-heading">' 
                            +'<h3 class="panel-title">'
                                +'<a href="#" action-mode="app-display" id="SYSTEM_APPLICATION" name="SYSTEM_APPLICATION" form-name="systemApplication" form-to="#sub-content" data-index="<!--[=i]-->" title="<!--[=local.action["look"]+local.name_app]-->">'
                                    +'<!--[=apps[i].appName]-->'
                                +'</a>'
                                +'<div class="btn-group btn-group-right">'
                                    +'<button type="button" action-mode="data-display" '
                                        +'id="BUSI_MANAGE_SECTION" name="BUSI_MANAGE_SECTION" form-name="busiSection" form-to="#sub-content" '
                                        +' title="<!--[=local.action["insert"]+"【"+apps[i].appName+"】"+local.name_section_sub]-->" data-extra="{sectionApp:\'<!--[=apps[i].appCode]-->\'}" class="btn btn-link btn-xs">'
                                        +'<span class="glyphicon glyphicon-plus"></span><!--[=local.name_section]-->'
                                    +'</button>'
                                +'</div>'
                            +'</h3>' 
                        +'</div>' 
                        +'<div class="panel-body">' 
                            +'<ul id="<!--[=apps[i].appCode]-->_ul" class="ztree"">'
                            +'</ul>'
                        +'</div>' 
                    +'</div>'
                +'<!--[}]-->'
            +'<!--[}]-->'
        );

    /*分页栏*/
    mt.pagination=template.compile(''
                +'<ul class="pagination pagination-sm">'
                    +'<li <!--[=(data.page==1)?"class=\\"disabled\\"":""]-->><a name="<!--[=name]-->" action-mode="data_first" href="#"><i class="glyphicon glyphicon-step-backward"></i></a></li>'
                    +'<li <!--[=(data.page==1)?"class=\\"disabled\\"":""]-->><a name="<!--[=name]-->" action-mode="data_prev" href="#"><i class="glyphicon glyphicon-chevron-left"></i></a></li>'
                    +'<!--[var start=(data.totalPage-data.page<dis+1)?((data.totalPage-max>0)?data.totalPage-max:1):((data.page>dis)?(data.page-dis):1);]-->'
                    +'<!--[for(var i=start;i<data.totalPage+1&&i<start+max+1;i++){]-->'
                    +'<li <!--[=(data.page==i)?"class=\\"active\\"":""]-->><a name="<!--[=name]-->" action-mode="data_page" href="#"><!--[=i]--></a></li>'
                    +'<!--[}]-->'
                    +'<li <!--[=(data.page==data.totalPage)?"class=\\"disabled\\"":""]-->><a name="<!--[=name]-->" action-mode="data_next" href="#"><i class="glyphicon glyphicon-chevron-right"></i></a></li>'
                    +'<li <!--[=(data.page==data.totalPage)?"class=\\"disabled\\"":""]-->><a name="<!--[=name]-->" action-mode="data_last" href="#"><i class="glyphicon glyphicon-step-forward"></i></a></li>'
                +'</ul>'
            );
})(mt);