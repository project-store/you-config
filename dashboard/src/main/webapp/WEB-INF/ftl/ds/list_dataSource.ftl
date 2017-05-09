<@page title="配置中心" description="配置中心"  keywords="配置中心" assets=[
]>
<section class="content-header">
    <h1>
        dataSource 列表
        <small>数据源环境</small>
    </h1>
    <ol class="breadcrumb">
        <li><a href=""><i class="fa fa-home"></i> Home</a></li>
        <li class="active">Hosts</li>
    </ol>
</section>

<!-- Main content -->
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-primary">
                <div class="box-header">
                    <h3 class="box-title">
                        <button class="btn btn-success btn-sm add icon" data-toggle="modal" data-target="#secondModal">
                            + &nbsp新增
                        </button>
                    </h3>
                </div>
                <!-- 新增框 -->
                <div class="modal fade" id="secondModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="z-index:1100;">
                    <div class="modal-dialog" style="width:450px">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                    &times;
                                </button>
                                <br/>
                                <h4 class="modal-title" id="myModalLabel">
                                    <div class="form-group form-inline">
                                        <label for="name">&nbsp;&nbsp;名&nbsp;&nbsp;&nbsp;字&nbsp;:</label>
                                        <input type="text" class="form-control" style="width:300px" id="newAppName">
                                    </div>
                                    <div class="form-group form-inline">
                                        <label for="name">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;url:</label>
                                        <input type="text" class="form-control" style="width:300px" id="newUrl">
                                    </div>
                                    <div class="form-group form-inline">
                                        <label for="name">&nbsp;&nbsp;&nbsp;&nbsp;Host&nbsp;:</label>
                                        <input type="text" class="form-control" style="width:300px" id="newHost">
                                    </div>
                                    <div class="form-group form-inline">
                                        <label for="name">&nbsp;用&nbsp;&nbsp;户&nbsp;:</label>
                                        <input type="text" class="form-control" style="width:300px" id="newUserName">
                                    </div>
                                    <div class="form-group form-inline">
                                        <label for="name">&nbsp;&nbsp;&nbsp;密&nbsp;码&nbsp;:</label>
                                        <input type="text" class="form-control" style="width:300px" id="newPassword">
                                    </div>
                                </h4>
                            </div>
                            <input type="hidden" value="" id="configId"/>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                <button type="button" class="btn btn-primary" name="addNewDS" >新增</button>
                            </div>
                        </div>
                    </div><!-- 新增框 -->
                </div>

                <!-- 数据展示栏 -->
                <div class="box-body table-responsive no-padding">
                    <table class="table table-hover table-striped">
                        <thead>
                        <tr>
                            <th>id</th>
                            <th>appName</th>
                            <th>Host</th>
                            <th>Url</th>
                            <th>userName</th>
                            <th>password</th>
                            <th>更新时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list list as item>
                            <tr id="dsId_${item.id}">
                                <td name="appId">${item.id!''}</td>
                                <td name="appName">${item.appName!''}</td>
                                <td>${item.host!''}</td>
                                <td>${item.url!''}</td>
                                <td>${item.userName!''}</td>
                                <td>${item.password!''}</td>
                                <td>${item.updateTime?string("yyyy-MM-dd HH:mm")}</td>
                                <td>
                                    <a href= "/datasource/config/detail/${item.id!!''}" class="btn btn-primary" role="button">&nbsp;&nbsp; 查看 &nbsp;&nbsp;</a>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
            </div>
            <!-- 数据展示栏 -->
            <!-- json返回触发按钮 -->
        </div>
    </div>
</section>
<script>
    /*新增数据*/
    $('button[name=addNewDS]').click(function(){
        var appName = $('#newAppName').val();
        var url = $('#newUrl').val();
        var Host = $('#newHost').val();
        var password = $("#newPassword").val();
        var userName = $('#newUserName').val();
        if (appName == "" || appName == null || appName == undefined){
            showMsg("名字不能为空");
            return;
        } else if (url == null || url == "" || url == undefined){
            showMsg("url不能为空");
            return;
        } else if (Host == null || Host == "" || Host == undefined){
            showMsg("Host");
            return;
        } else if (userName == "" || userName == null || userName == undefined){
            showMsg("用户名不能为空");
            return;
        } else if (password == "" || password == null || password == undefined){
            showMsg("密码不能为空");
            return;
        }
        var jsonData = {};
        jsonData.appName = appName;
        jsonData.url = url;
        jsonData.host = Host;
        jsonData.password = password;
        jsonData.userName = userName;
        $.ajax({
            url: "/ds/add/trunk-env/",
            type: "post",
            data: jsonData,
            dataType: "json",
            async: "false",
            success: function(data){
                var msg = data.messageText;
                if (data.code == 0){
                    showMsg(msg);
                    window.location.reload();
                } else {
                    showMsg(msg);
                }
            }
        })
    })

</script>
</@page>