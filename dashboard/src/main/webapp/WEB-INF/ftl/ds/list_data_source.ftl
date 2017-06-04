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
                        <button class="btn btn-success btn-sm add icon" data-toggle="modal" data-target="#insertModalLabel">
                            + &nbsp新增
                        </button>
                    </h3>
                </div>
                <!-- 新增框 -->
                <div class="modal fade" id="insertModalLabel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="z-index:1100;">
                    <div class="modal-dialog" style="width:450px">
                        <form action="/datasource/config/add" method="post" onsubmit="return ajaxSubmitCallback(this);">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                        &times;
                                    </button>
                                    <br/>
                                    <h4 class="modal-title" >
                                        <div class="form-group form-inline">
                                            <label for="name">&nbsp;名字&nbsp;:</label>
                                            <input type="text" name="appName" class="form-control" style="width:300px">
                                        </div>
                                        <div class="form-group form-inline">
                                            <label for="name">&nbsp;&nbsp;URL&nbsp;:</label>
                                            <input type="text" name="url" class="form-control" style="width:300px">
                                        </div>
                                        <div class="form-group form-inline">
                                            <label for="name">&nbsp;Host&nbsp;:</label>
                                            <input type="text" name="host" class="form-control" style="width:300px">
                                        </div>
                                        <div class="form-group form-inline">
                                            <label for="name">&nbsp;用户&nbsp;:</label>
                                            <input type="text" name="userName" class="form-control" style="width:300px">
                                        </div>
                                        <div class="form-group form-inline">
                                            <label for="name">&nbsp;密码&nbsp;:</label>
                                            <input type="text" name="password" class="form-control" style="width:300px">
                                        </div>
                                    </h4>
                                </div>
                                <input type="hidden" value="" id="configId"/>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                    <input type="submit" class="btn btn-primary" value="新增"/>
                                    <#--<button type="button" class="btn btn-primary" name="addNewDS" >新增</button>-->
                                </div>
                            </div>
                        </form>
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
                                    <a href= "/datasource/config/show/${item.id!!''}" class="btn btn-primary" role="button">详情</a>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>
<script>
    function ajaxSubmitCallback(form) {
        var $form = $(form);
        $.ajax({
            url: $form.attr("action"),
            data:$form.serializeArray(),
            type:$form.attr("method"),
            dataType: "json",
            success: function(data){
                if (data.code == 0){
                    showMsg(data.messageText);
                    window.location.reload();
                } else {
                    showMsg(data.messageText);
                }
            }
        });
        return false;
    }
</script>
</@page>