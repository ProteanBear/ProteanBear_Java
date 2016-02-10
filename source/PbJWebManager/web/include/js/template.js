/**
 * Created by maqiang on 14-7-2.
 * 公用模板设置
 */
var mt=mt||{};

(function(mt){
    /*顶部导航栏*/
    mt.topbar=template.compile(
            '<div class="container-fluid">' +
                '<div class="navbar-header">' +
                    '<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">' +
                        '<span class="sr-only">Toggle navigation</span>' +
                        '<span class="icon-bar"></span>' +
                        '<span class="icon-bar"></span>' +
                        '<span class="icon-bar"></span>' +
                    '</button>' +
                    '<a class="navbar-brand" href="#"><!--[=system_title]--></a>' +
                '</div>' +
                '<div class="navbar-collapse collapse">' +
                    '<ul class="nav navbar-nav navbar-right">' +
                        '<li><a href="#" action-mode="user-config"><!--[=index_topnav_hello]--><!--[=userNick]--></a></li>' +
                        '<li><a href="#" action-mode="data-refresh"><!--[=index_topnav_refresh]--></a></li>' +
                        '<li><a href="#" action-mode="system-config"><!--[=index_topnav_setting]--></a></li>' +
                        '<li><a href="#" action-mode="user-logout"><!--[=index_topnav_logout]--></a></li>' +
                    '</ul>' +
                '</div>' +
            '</div>');

    /*弹出提示框*/
    mt.confirm=template.compile(
            '<div class="modal fade" id="confirmDialog" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">' +
                '<div class="modal-dialog">' +
                    '<div class="modal-content">' +
                        '<div class="modal-header">' +
                            '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>' +
                            '<h4 class="modal-title" id="moduleDialog-mode"><!--[=local.message["info"]]--></h4>' +
                        '</div>' +
                        '<div class="modal-body"><!--[=content]--></div>' +
                        '<div class="modal-footer">' +
                            '<button type="button" class="btn btn-default btn-sm" data-dismiss="modal"><!--[=local.action["cancel"]]--></button>' +
                            '<button type="button" action-mode="data-confirm" class="btn btn-primary btn-sm"><!--[=local.action["confirm"]]--></button>' +
                        '</div>' +
                    '</div>' +
                '</div>' +
            '</div>');

    /*弹出提示框*/
    mt.messageAlert=template.compile(
            '<div class="alert alert-<!--[=mode]--> fade">'
                +'<button type="button" class="close" data-dismiss="alert">&times;</button>'
                +'<strong><!--[=alert]--></strong> <!--[=message]-->'
            +'</div>'
            );
    
    /*属性内容*/
    var property='<!--[for(var key in property){]-->'
                    +'<!--[var prop=property[key];]-->'
                    //隐藏域
                    +'<!--[if(prop.type=="hidden"){]-->'
                        +'<input type="hidden" id="<!--[=key]-->" name="<!--[=key]-->" value="<!--[=data?data[prop.from||key]:""]-->" />'
                    +'<!--[continue;}]-->'
                    +'<!--[if(prop.inline!="end"){]--><div class="form-group"><!--[}]-->'
                    +'<!--[var col=(prop.col?prop.col:9);]-->'
                        //输入框
                        +'<!--[if(prop.type=="input"){]-->'
                            +'<label for="<!--[=key]-->" class="col-sm-2 control-label"><!--[=local.property[key]]--></label>'
                            +'<div class="col-sm-<!--[=col]-->">'
                                +'<!--[var canBe=(prop.only)?(prop.only==operate):true;]-->'
                                +'<!--[if(limit&&canBe){]-->'
                                    +'<input id="<!--[=key]-->" name="<!--[=key]-->" class="form-control" value="<!--[=data?data[key]:""]-->" />'
                                +'<!--[}else{]-->'
                                    +'<p class="display"><!--[=data?data[key]:""]--></p>'
                                +'<!--[}]-->'
                            +'</div>'
                        +'<!--[}]-->'
                        //密码等自定义框
                        +'<!--[if(prop.type=="custom"){]-->'
                            +'<!--[if(prop.customInput!="password"||(prop.customInput=="password"&&limit)){]-->'
                                +'<label for="<!--[=key]-->" class="col-sm-2 control-label"><!--[=local.property[key]]--></label>'
                                +'<div class="col-sm-<!--[=col]-->">'
                                +'<!--[var canBe=(prop.only)?(prop.only==operate):true;]-->'
                                +'<!--[if(limit&&canBe){]-->'
                                    +'<input id="<!--[=key]-->" name="<!--[=key]-->" type="<!--[=prop.customInput]-->" class="form-control" value="<!--[=data?data[key]:""]-->" />'
                                +'<!--[}else if(prop.customInput!="password"){]-->'
                                    +'<p class="display"><!--[=data?data[key]:""]--></p>'
                                +'<!--[}]-->'
                                +'</div>'
                            +'<!--[}]-->'
                        +'<!--[}]-->'
                        //单选框
                        +'<!--[if(prop.type=="select"){]-->'
                            +'<!--[prop.source=(typeof(prop.source)=="function")?prop.source():prop.source]-->'
                            +'<label for="<!--[=key]-->" class="col-sm-2 control-label"><!--[=local.property[key]]--></label>'
                            +'<div class="col-sm-<!--[=col]-->">'
                                +'<!--[if(limit){]-->'
                                    +'<select id="<!--[=key]-->" name="<!--[=key]-->" class="form-control" value="<!--[=data?data[key]:""]-->">'
                                    +'<!--[for(var selKey in prop.source){]-->'
                                        +'<option value="<!--[=selKey]-->" <!--[=data?(selKey==data[key]?"selected":""):(selKey==prop.default?"selected":"")]-->>'
                                            +'<!--[=prop.source[selKey]]-->'
                                        +'</option>'
                                    +'<!--[}]-->'
                                    +'</select>'
                                +'<!--[}else{]-->'
                                    +'<p class="display"><!--[=data?data[key]:""]--></p>'
                                +'<!--[}]-->'
                            +'</div>'
                        +'<!--[}]-->'
                        //多选框
                        +'<!--[if(prop.type=="multiple"){]-->'
                            +'<!--[prop.source=(typeof(prop.source)=="function")?prop.source():prop.source]-->'
                            +'<label for="<!--[=key]-->" class="col-sm-2 control-label"><!--[=local.property[key]]--></label>'
                            +'<div class="col-sm-<!--[=col]-->">'
                                +'<!--[if(limit){]-->'
                                    +'<select id="<!--[=key]-->" name="<!--[=key]-->" class="form-control" value="<!--[=data?data[key]:""]-->" multiple="multiple">'
                                    +'<!--[for(var selKey in prop.source){]-->'
                                        +'<option value="<!--[=selKey]-->" <!--[=data?(selKey==data[key]?"selected":""):(selKey==prop.default?"selected":"")]-->>'
                                            +'<!--[=prop.source[selKey]]-->'
                                        +'</option>'
                                    +'<!--[}]-->'
                                    +'</select>'
                                +'<!--[}else{]-->'
                                    +'<p class="display">'
                                        +'<!--[var i=0;for(var selKey in prop.source){]-->'
                                            +'<!--[=(i==0)?"":"|"]-->'
                                            +'<!--[=data?(selKey==data[key]?prop.source[selKey]:""):("")]-->'
                                        +'<!--[i++;}]-->'
                                    +'</p>'
                                +'<!--[}]-->'
                            +'</div>'
                        +'<!--[}]-->'
                        //radio
                        +'<!--[if(prop.type=="radio"){]-->'
                            +'<!--[prop.source=(typeof(prop.source)=="function")?prop.source():prop.source]-->'
                            +'<label for="<!--[=key]-->" class="col-sm-2 control-label"><!--[=local.property[key]]--></label>'
                            +'<div class="col-sm-<!--[=col]-->">'
                                +'<!--[if(limit){]-->'
                                    +'<!--[var i=0;for(var radioKey in prop.source){]-->'
                                        +'<label class="radio-inline">'
                                            +'<input type="radio" name="<!--[=key]-->" id="<!--[=key+"-"+i]-->" value="<!--[=radioKey]-->" <!--[=data?((radioKey==data[key])?"checked":""):(radioKey==prop.default?"checked":"")]-->> <!--[=prop.source[radioKey]]-->'
                                        +'</label>'
                                    +'<!--[i++;}]-->'
                                +'<!--[}else{]-->'
                                    +'<p class="display">'
                                        +'<!--[for(var radioKey in prop.source){]-->'
                                            +'<!--[=data?(radioKey==data[key]?prop.source[radioKey]:""):("")]-->'
                                        +'<!--[}]-->'
                                    +'</p>'
                                +'<!--[}]-->'
                            +'</div>'
                        +'<!--[}]-->'
                        //radio-images
                        +'<!--[if(prop.type=="radio-images"){]-->'
                            +'<!--[prop.source=(typeof(prop.source)=="function")?prop.source():prop.source]-->'
                            +'<label for="<!--[=key]-->" class="col-sm-2 control-label"><!--[=local.property[key]]--></label>'
                            +'<div class="col-sm-<!--[=col]-->">'
                                +'<!--[if(limit){]-->'
                                    +'<!--[var i=0;for(var radioKey in prop.source){]-->'
                                        +'<label class="radio-inline">'
                                            +'<input type="radio" name="<!--[=key]-->" id="<!--[=key+"-"+i]-->" value="<!--[=radioKey]-->" <!--[=data?(radioKey==data[key]?"checked":""):(radioKey==prop.default?"checked":"")]-->>'
                                            +'<img class="<!--[=(prop.isIcon==false)?"":"icon"]-->" src="<!--[=prop.source[radioKey]]-->" />'
                                        +'</label>'
                                    +'<!--[i++;}]-->'
                                +'<!--[}else{]-->'
                                    +'<p class="display">'
                                        +'<!--[for(var radioKey in prop.source){]-->'
                                            +'<img src="<!--[=prop.source[radioKey]]-->" />'
                                        +'<!--[}]-->'
                                    +'</p>'
                                +'<!--[}]-->'
                            +'</div>'
                        +'<!--[}]-->'
                        //checkbox
                        +'<!--[if(prop.type=="checkbox"){]-->'
                            +'<!--[prop.source=(typeof(prop.source)=="function")?prop.source():prop.source]-->'
                            +'<label for="<!--[=key]-->" class="col-sm-2 control-label"><!--[=local.property[key]]--></label>'
                            +'<div class="col-sm-<!--[=col]-->">'
                                +'<!--[if(limit){]-->'
                                    +'<!--[var i=0;for(var checkKey in prop.source){]-->'
                                        +'<label class="checkbox-inline">'
                                            +'<!--[var checked=false;if(data){for(var k=0;k<data[key].length;k++){if(checkKey==data[key][k]){checked=true;break;}}}]-->'
                                            +'<input type="checkbox" name="<!--[=key]-->" id="<!--[=key+"-"+i]-->" value="<!--[=checkKey]-->" <!--[=checked?"checked":""]-->> <!--[=prop.source[checkKey]]-->'
                                        +'</label>'
                                    +'<!--[i++;}]-->'
                                +'<!--[}else{]-->'
                                    +'<p class="display">'
                                        +'<!--[var i=0;for(var checkKey in prop.source){]-->'
                                            +'<!--[=(i==0)?"":"|"]-->'
                                            +'<!--[=data?(checkKey==data[key]?prop.source[checkKey]:""):("")]-->'
                                        +'<!--[i++;}]-->'
                                    +'</p>'
                                +'<!--[}]-->'
                            +'</div>'
                        +'<!--[}]-->'
                        //多行文本框
                        +'<!--[if(prop.type=="text"){]-->'
                            +'<label for="<!--[=key]-->" class="col-sm-2 control-label"><!--[=local.property[key]]--></label>'
                            +'<div class="col-sm-<!--[=col]-->">'
                                +'<!--[if(limit){]-->'
                                    +'<textarea id="<!--[=key]-->" name="<!--[=key]-->" class="form-control" rows="3"><!--[=data?$textExchange(data[key],1,prop.encode):""]--></textarea>'
                                +'<!--[}else{]-->'
                                    +'<p class="display"><!--[=data?$textExchange(data[key],0,prop.encode):""]--></p>'
                                +'<!--[}]-->'
                            +'</div>'
                        +'<!--[}]-->'
                        //glyphicon
                        +'<!--[if(prop.type=="glyphicon"){]-->'
                            +'<!--[prop.source=(typeof(prop.source)=="function")?prop.source():prop.source]-->'
                            +'<label for="<!--[=key]-->" class="col-sm-2 control-label"><!--[=local.property[key]]--></label>'
                            +'<div class="col-sm-<!--[=col]-->">'
                                +'<!--[if(limit){]-->'
                                    +'<!--[for(var i=0;i<prop.source.length;i++){]-->'
                                        +'<label class="radio-inline">'
                                            +'<input type="radio" name="<!--[=key]-->" id="<!--[=key+"-"+i]-->" value="<!--[=prop.source[i]]-->" <!--[=data?(prop.source[i]==data[key]?"checked":""):(prop.source[i]==prop.default?"checked":"")]-->>'
                                            +'<span class="glyphicon <!--[=prop.source[i]]-->"></span>'
                                        +'</label>'
                                    +'<!--[}]-->'
                                +'<!--[}else{]-->'
                                    +'<p class="display">'
                                        +'<!--[for(var i=0;i<prop.source.length;i++){]-->'
                                            +'<span class="glyphicon <!--[=data?(prop.source[i]==data[key]?prop.source[i]:""):("")]-->"></span>'
                                        +'<!--[}]-->'
                                    +'</p>'
                                +'<!--[}]-->'
                            +'</div>'
                        +'<!--[}]-->'
                        //images
                        +'<!--[if(prop.type=="images"){]-->'
                            +'<!--[prop.source=(typeof(prop.source)=="function")?prop.source():prop.source]-->'
                            +'<label for="<!--[=key]-->" class="col-sm-2 control-label"><!--[=local.property[key]]--></label>'
                            +'<div class="col-sm-<!--[=col]-->">'
                                +'<!--[if(limit){]-->'
                                    +'<!--[for(var i=0;i<prop.source.length;i++){]-->'
                                        +'<label class="radio-inline">'
                                            +'<input type="radio" name="<!--[=key]-->" id="<!--[=key+"-"+i]-->" value="<!--[=prop.source[i]]-->" <!--[=data?(prop.source[i]==data[key]?"checked":""):(prop.source[i]==prop.default?"checked":"")]-->>'
                                            +'<img class="<!--[=(prop.isIcon==false)?"":"icon"]-->" src="<!--[=prop.source[i]]-->" />'
                                        +'</label>'
                                    +'<!--[}]-->'
                                +'<!--[}else{]-->'
                                    +'<p class="display">'
                                        +'<!--[for(var i=0;i<prop.source.length;i++){]-->'
                                            +'<img src="<!--[=prop.source[i]]-->" />'
                                        +'<!--[}]-->'
                                    +'</p>'
                                +'<!--[}]-->'
                            +'</div>'
                        +'<!--[}]-->'
                        //图片上传
                        +'<!--[if(prop.type=="uploadIcon"){]-->'
                            +'<!--[prop.source=(typeof(prop.source)=="function")?prop.source():prop.source]-->'
                            +'<label for="<!--[=key]-->" class="col-sm-2 control-label"><!--[=local.property[key]]--></label>'
                            +'<div class="col-sm-<!--[=col]-->">'
                                +'<input type="hidden" id="<!--[=key]-->" name="<!--[=key]-->" value="<!--[=data?data[key]:""]-->" data-extra="<!--[=prop.extra||"{}"]-->" />'
                                +'<input type="file" id="<!--[=key]-->_file" name="<!--[=key]-->_file" to="<!--[=key]-->" style="display:none;" />'
                                +'<a href="#" class="thumbnail" action-mode="upload" to="<!--[=key]-->">'
                                    +'<!--[var holder="holder.js/"+prop.size+""]-->'
                                    +'<img id="<!--[=key]-->_img" name="<!--[=key]-->_img" data-src="<!--[=data?(data[key]?"":(holder)):(holder)]-->" style="width:<!--[=prop.width]-->px;height:<!--[=prop.height]-->px;" src="<!--[=data?data[key]:""]-->" alt="...">'
                                +'</a>'
                            +'</div>'
                        +'<!--[}]-->'
                        //template自定义模板
                        +'<!--[if(prop.type=="template"){]-->'
                            +'<!--[prop.source=(typeof(prop.source)=="function")?prop.source(data,limit,operate):prop.source]-->'
                            +'<label for="<!--[=key]-->" class="col-sm-2 control-label"><!--[=local.property[key]]--></label>'
                            +'<div class="col-sm-<!--[=col]-->">'
                                +'<!--[=prop.source]-->'
                            +'</div>'
                        +'<!--[}]-->'
                    +'<!--[if(prop.inline!="start"){]--></div><!--[}]-->'
                +'<!--[}]-->';
    
    /*弹出对话框*/
    mt.dialog=template.compile(''
            +'<div class="modal fade" id="<!--[=id]-->" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">'
                +'<div class="modal-dialog">'
                    +'<div class="modal-content">'
                        +'<div class="modal-header"><button type="button" class="close" data-dismiss="modal">'
                            +'<span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
                            +'<h4 class="modal-title" id="pluginDialog-mode"><!--[=title]--></h4>'
                        +'</div>'
                        +'<div class="modal-body form-horizontal">'
                        +property
                        +'</div>'
                        +'<div class="modal-footer">'
                            +'<button type="button" class="btn btn-default btn-sm" data-dismiss="modal"><!--[=local.action["close"]]--></button>'
                            +'<button id="<!--[=id]-->_submit" name="<!--[=name]-->" type="button" class="btn btn-primary btn-sm"><!--[=local.action["submit"]]--></button>'
                        +'</div>'
                    +'</div>'
                +'</div>'
            +'</div>'
            );
    
    /*属性表单内容*/
    mt.propertyForm=template.compile(''
            +'<div id="<!--[=id]-->" class="panel panel-primary">'
                +'<div class="panel-heading"><h3 class="panel-title"><!--[=title]--></h3></div>'
                +'<div class="panel-body form-horizontal">'
                    +property
                    +'<div class="form-group">'
                        +'<div class="<!--[="col-sm-offset-"+column.offset]--> <!--[="col-sm-"+column.value]-->">'
                            +'<!--[if((operate=="create"&&limit.insert)||(operate=="edit"&&limit.edit)){]--><button id="<!--[=id]-->_submit" action-mode="data-edit" name="<!--[=name]-->" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-floppy-save"><!--[=local.action["submit"]]--></button><!--[}]-->'
                            +'<!--[if(operate!="create"&&limit.remove){]-->'
                                +'&nbsp;&nbsp;<button id="<!--[=id]-->_remove" action-mode="data-remove" name="<!--[=name]-->" data-index="<!--[=index]-->" title="<!--[=local.alert_infor["confirm"]+config.title+"【"+data[config.keyTitle]+"】"+local.alert_infor["firmend"]]-->" type="button" class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-trash"><!--[=local.action["remove"]]--></button>'
                                +'<!--[if(config.buttons){]-->'
                                    +'<!--[for(var i=0;i<config.buttons.length;i++){]-->'
                                        +'&nbsp;&nbsp;<button id="<!--[=config.buttons[i].id]-->" action-mode="<!--[=config.buttons[i].action]-->" name="<!--[=config.buttons[i].name]-->" data-index="<!--[=config.buttons[i].index]-->" title="<!--[=config.buttons[i].title]-->" type="button" class="btn btn-<!--[=config.buttons[i].style]--> btn-sm"><span class="glyphicon <!--[=config.buttons[i].icon]-->"><!--[=config.buttons[i].display]--></button>'
                                    +'<!--[}]-->'
                                +'<!--[}]-->'
                            +'<!--[}]-->'
                        +'</div>'
                    +'</div>'
                +'</div>'
            +'</div>'
            );
    
    /*Input警告*/
    mt.inputWarning=template.compile('<span class="glyphicon glyphicon-warning-sign form-control-feedback"></span>');
    
    /*Input错误*/
    mt.inputError=template.compile('<span class="glyphicon glyphicon-remove form-control-feedback"></span>');
})(mt);