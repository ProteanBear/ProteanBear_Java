var local=local||{};

(function(local){
    /*系统*/
    local.system_title="ProteanBear";
    local.system_company="ProteanBear";
    
    /*登录页面使用*/
    local.login_header="登录到"+local.system_title;
    local.login_name="用户名称";
    local.login_pass="登录密码";
    local.login_verify="验证码";
    local.login_remember="记住我";
    local.login_action="登录";
    local.login_reset="重置";
    local.login_loading="登录中...";
    local.login_success="登录成功！载入用户系统主页。";
    
    /*主页导航栏使用*/
    local.index_topnav_hello="你好，";
    local.index_topnav_refresh="刷新";
    local.index_topnav_setting="设置";
    local.index_topnav_logout="退出";
    
    /*底部信息栏使用*/
    local.footer_gototop="返回顶部";
    local.footer_stop="官方网站";
    local.footer_about="关于我们";
    local.footer_contact="联系方式";
    local.footer_help="帮助手册";
    local.footer_feedback="意见反馈";
    local.footer_information="© 2013 - 2014 "+local.system_company+" Inc.";
    
    /*工具栏使用*/
    local.all="全部";
    local.search_input="输入搜索关键字";
    local.actions_title="批量操作";
    
    /*提示信息*/
    local.warning_checklog="检测用户登录状态";
    local.warning_logout="从系统中注销用户，清除用户服务对象";
    local.warning_loadlog="载入系统登录界面";
    local.warning_reload="访问服务器，更新用户缓存信息";
    local.warning_nullres="返回信息为空，网速较慢或服务器忙";
    local.warning_reloading="访问成功，刷新系统用户界面";
    
    /*错误信息*/
    local.message={"success":"操作成功！","danger":"出现错误！","info":"系统提示！","warning":"警告提醒！"};
    local.error_infor={
        "000":"用户名称或登录密码输入为空。",
        "001":"用户名称或登录密码输入不正确。",
        "002":"未输入验证码",
        "003":"对不起，您没有查询本数据的权限",
        "004":"对不起，接口返回的内容为空",
        "005":"访问接口失败，请检查网络或与管理员联系"
    };
    local.warning_infor={
        "000":"警告：输入内容不能为空！",
        "001":"当前的数据内容为空！"
    };
    local.alert_infor={
        "confirm":"确定要删除当前选择的",
        "firmend":"吗？"
    };
    
    /*其他提示*/
    local.loading="载入中...";
    local.operating="处理中...";
    
})(local);