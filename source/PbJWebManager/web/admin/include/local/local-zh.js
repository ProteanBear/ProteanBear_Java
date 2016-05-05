var local=local||{};

(function(local){
    /*局部变量*/
    var pathIcon="../include/images/icons/";
    
    /*左侧菜单栏使用*/
    local.menu_index="首页";
    local.menugroup_sys_statics="运维统计";
    local.menugroup_sys_manage="平台管理";
    local.menugroup_sys_config="平台配置";
    local.menu_sys_region="行政区域管理";
    local.menu_sys_plugin="系统插件配置";
    local.menu_sys_type="企业类型配置";
    local.menu_sys_role="用户角色配置";
    local.menu_ori_limit="基础权限配置";
    local.menu_ori_plat="发布平台配置";
    
    /*显示内容使用*/
    local.property={
        custId:"自增主键",
        pluginId:"数据标识",
        pluginName:"显示名称",
        pluginEnable:"是否启用",
        pluginIcon:"显示图标",
        pluginParent:"权限类型",
        dataRemark:"说明内容",
        pluginLink:"关联链接",
        pluginDisplay:"插件显示",
        typeName:"类型名称",
        typeLevel:"类型级别",
        typePlugins:"包含插件",
        roleName:"角色名称",
        roleType:"角色级别",
        roleLimits:"角色权限",
        areaId:"区域标识",
        areaName:"显示名称",
        areaIconClose:"关闭图标",
        areaIconOpen:"打开图标",
        areaContact:"联系人员",
        areaTel:"联系电话",
        areaAddress:"企业地址",
        areaTypeMaps:"企业类型",
        areaIcon:"企业图标",
        appCode:"应用标识",
        appName:"应用名称",
        appAlias:"应用别名",
        appWeb:"应用网站",
        appOrder:"应用排序",
        userIcon:"用户头像",
        userName:"登录账户",
        userNick:"显示昵称",
        newPass:"设置密码",
        passAgain:"再次输入",
        userTel:"联系电话",
        userQq:"QQ",
        userRoles:"用户角色",
        limitId:"权限数值",
        limitName:"权限名称",
        limitInit:"初始创建",
        platName:"显示名称",
        platBelong:"所属平台",
        platSource:"源码类型",
        appThumbnail:"缩略图标",
        appIcon:"高清图标",
        appPlat:"应用平台"
    };
    local.action={"insert":"添加","edit":"编辑","look":"查看","submit":"保存","reset":"重置","remove":"删除","close":"关闭","cancel":"取消","confirm":"确定"};
    local.limitTypes={"0":"查询","1":"添加","2":"删除","3":"编辑"};
    local.typeLevel={"0":"平台级","1":"企业级"};
    local.areaClass={"0":"目录","1":"企业","2":"应用","3":"用户"};
    local.roleType={"0":"全局","1":"下级","2":"本级"};
    local.source={
        use:{"0":"禁用","1":"启用"},
        display:{"0":"隐藏","1":"显示"},
        yesOrNo:{"0":"否","1":"是"},
        platform:{"iOS":"苹果","Android":"安卓"},
        platSource:{"0":"原生","1":"HTML5"},
        glyphicon:["glyphicon-file","glyphicon-glass","glyphicon-cloud","glyphicon-user","glyphicon-signal",
            "glyphicon-cog","glyphicon-time","glyphicon-tag","glyphicon-tags",
            "glyphicon-book","glyphicon-bookmark","glyphicon-tint","glyphicon-gift","glyphicon-leaf",
            "glyphicon-fire","glyphicon-plane","glyphicon-globe","glyphicon-wrench","glyphicon-tasks",
            "glyphicon-briefcase","glyphicon-paperclip","glyphicon-pushpin","glyphicon-send",
            "glyphicon-floppy-disk","glyphicon-credit-card","glyphicon-transfer",
            "glyphicon-phone-alt","glyphicon glyphicon-tower","glyphicon-stats","glyphicon-cloud-download",
            "glyphicon glyphicon-cloud-upload","glyphicon-tree-conifer","glyphicon-tree-deciduous"],
        images:{
            "categoryClose":[pathIcon+"index_close.png",pathIcon+"blue-folder-horizontal.png",
                pathIcon+"blue-folder.png",pathIcon+"folder_blue.png",
                pathIcon+"folder_recycled.png",pathIcon+"folder_yellow.png",
                pathIcon+"folder-horizontal.png",pathIcon+"folder.png"
            ],
            "categoryOpen":[pathIcon+"index_open.png",pathIcon+"blue-folder-horizontal-open.png",
                pathIcon+"blue-folder-open.png",pathIcon+"folder_classic_blue.png",
                pathIcon+"folder_classic_recycled.png",pathIcon+"folder_classic_yellow.png",
                pathIcon+"folder-horizontal-open.png",pathIcon+"folder-open.png"
            ],
            "userIcon":[pathIcon+"user_angel_black.png",pathIcon+"user_angel_female.png",
                pathIcon+"user_astronaut.png",pathIcon+"user_ballplayer.png",
                pathIcon+"user_ballplayer_black.png",pathIcon+"user_banker.png",
                pathIcon+"user_bart.png",pathIcon+"user_batman.png",
                pathIcon+"user_chief.png",pathIcon+"user_chief_female.png",
                pathIcon+"user_cook.png",pathIcon+"user_cook_female.png",
                pathIcon+"user_cowboy.png",pathIcon+"user_cowboy_female.png",
                pathIcon+"user_death.png",pathIcon+"user_egyptian.png",
                pathIcon+"user_egyptian_female.png",pathIcon+"user_geisha.png",
                pathIcon+"user_gladiator.png",pathIcon+"user_halk.png",
                pathIcon+"user_ironman.png",pathIcon+"user_jason.png",
                pathIcon+"user_jawa.png",pathIcon+"user_jester.png",
                pathIcon+"user_jew.png",pathIcon+"user_judge.png",
                pathIcon+"user_king.png",pathIcon+"user_knight.png",
                pathIcon+"user_leprechaun.png",pathIcon+"user_lisa.png",
                pathIcon+"user_medical.png",pathIcon+"user_medical_female.png",
                pathIcon+"user_pilot.png",pathIcon+"user_pilot_civil.png",
                pathIcon+"user_pirate.png",pathIcon+"user_police_england.png",
                pathIcon+"user_police_female.png",pathIcon+"user_priest.png",
                pathIcon+"user_queen.png",pathIcon+"user_rambo.png",
                pathIcon+"user_robocop.png",pathIcon+"user_sailor.png",
                pathIcon+"user_scream.png",pathIcon+"user_spiderman.png",
                pathIcon+"user_striper.png",pathIcon+"user_student.png",
                pathIcon+"user_student_female.png",pathIcon+"user_superman.png",
                pathIcon+"user_vietnamese_female.png",pathIcon+"user_viking.png",
                pathIcon+"user_viking_female.png",pathIcon+"user_waiter.png",
                pathIcon+"user_waiter_female.png",pathIcon+"user_wicket.png",
                pathIcon+"user_yoda.png"
            ]
        }
    };
    local.name_module="模块";
    local.name_plugin="插件";
    local.name_limit="权限";
    local.name_comtype="类型";
    local.name_area="区域";
    local.name_role="角色";
    local.name_infor="信息";
    local.name_plat="平台";
    local.name_version="版本";
    local.name_lastnew="最新";
    
})(local);