<@page title="配置中心" description="配置中心"  keywords="配置中心" assets=[
]>
<section class="content-header">
    <h1>
        <small>应用&nbsp;&nbsp;&nbsp;</small>
        ${appName!}
        <small>&nbsp;&nbsp;&nbsp;配置</small>
    </h1>
    <ol class="breadcrumb">
        <li><a href="/home"><i class="fa fa-home"></i> Home</a></li>
        <li class="active"><a href="/app/info/list">app列表</a></li>
    </ol>
</section>

<!-- Main content -->
<section class="content">
<div class="row">
    <div class="col-xs-12">
        <div class="box box-primary">
            <!-- 修改栏 -->
            <div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="z-index:1100">
                <div class="modal-dialog" style="width:450px">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <br/>
                            <h4 class="modal-title" id="myModalLabel">
                                <div class="form-group form-inline">
                                    <label for="name">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;值:</label>
                                    <input type="text" style="width:300px" class="form-control" name="value">
                                </div>
                                <div class="form-group form-inline">
                                    <label for="name">描述:</label>
                                    <input type="text" style="width:300px" class="form-control" name="description">
                                </div>
                            </h4>
                        </div>
                        <input type="hidden" value="" id="configId"/>
                        <div class="modal-footer" style="marign:0,auto;">
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                            <button type="button" class="btn btn-primary" name="saveParam">保存</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->
            </div>

            <!-- 新增栏 -->
            <div class="modal fade" id="newConfigModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="z-index:1100">
                <div class="modal-dialog" style="width:450px">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <br/>
                            <h4 class="modal-title" id="myModalLabel">
                                <div class="form-group form-inline">
                                    <label for="name">&nbsp;&nbsp;&nbsp;&nbsp;Key&nbsp;:&nbsp;</label>
                                    <input type="text" name="name" class="form-control" style="width:300px">
                                </div>
                                <div class="form-group form-inline" style="margin-top:10px;">
                                    <label for="name">Value&nbsp;:&nbsp;</label>
                                    <textarea name="value" class="form-control" style="width:300px"></textarea>
                                </div>
                                <div class="form-group form-inline" style="margin-top:10px;">
                                    <label for="name">描&nbsp;&nbsp;述&nbsp;:&nbsp;</label>
                                    <input type="text" name="description" class="form-control" style="width:300px">
                                </div>
                            </h4>
                        </div>
                        <input type="hidden" value="" id="configId"/>
                        <div class="modal-footer" style="marign:0,auto;">
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                            <button type="button" class="btn btn-primary" name="addNewConfig" >新增</button>
                        </div>
                    </div>
                </div><!-- 新增栏 -->
            </div>


            <div class="box-header">
                <button class="btn btn-success btn-sm" data-toggle="modal" data-target="#newConfigModal">
                    &nbsp;&nbsp;&nbsp; 新增&nbsp;&nbsp;+ &nbsp;&nbsp;&nbsp;
                </button>&nbsp;&nbsp;&nbsp;&nbsp;
                <button class="btn btn-success btn-sm" data-toggle="modal" onclick="refreshAll()">
                    刷新zookeeper
                </button>&nbsp;&nbsp;&nbsp;&nbsp;
                <a href= "/app/config/compare/${appId}" class="btn btn-success btn-sm" role="button">&nbsp;&nbsp; 比对 &nbsp;&nbsp;</a>
            </div>

            <input type="hidden" value="${appId}" id="appId" name="appId"/>
            <!-- 数据展示栏 -->
            <div class="box-body table-responsive no-padding">
                <table class="table table-hover table-striped">
                    <thead>
                    <tr>
                        <th>id</th>
                        <th>name</th>
                        <th>值</th>
                        <th>描述</th>
                        <th>状态</th>
                        <th>更新时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                        <#list configList as config>
                        <tr>
                            <td name="configId">${config.id}</td>
                            <td name="name">${config.name}</td>
                            <td name="value">${config.value}</td>
                            <td name="description">${config.description}</td>
                            <td>
                                <#if config.compareCode == 1>
                                    <button class="btn btn-primary" disabled>同步</button>
                                <#else>
                                    <button class="btn btn-primary" disabled>不同步</button>
                                </#if>
                            </td>
                            <td>${config.ts?string("yyyy-MM-dd HH:mm")}</td>
                            <td>
                                <button class="btn btn-primary" data-toggle="modal" onclick="setDateToZookeeper(this)">
                                    更新zookeeper
                                </button>&nbsp&nbsp&nbsp&nbsp
                                <button class="btn btn-primary" data-toggle="modal" data-target="#updateModal" value="${config.id}"
                                        onclick="changeDescription(this)">
                                    修改
                                </button>
                            </td>
                        </tr>
                        </#list>
                    </tbody>
                </table>
            </div>
        </div>
        <!-- 数据展示栏 -->
    </div>
</section>
<script>
    /*保存一个值至zookeeper*/
    function setDateToZookeeper(ele) {
        var configId = $(ele).parent().parent().find("td[name='configId']").text()
        var appId = $('#appId').val();
        var timestamp = (new Date()).valueOf();
        var postUrl = "/app/config/synchronous/zookeeper/" + appId + "/" + configId + "?_=" + timestamp;
        $.ajax({
            url: postUrl ,
            type: "post",
            dataType: "json",
            async: false,
            success: function(data){
                if (data.code == 0){
                    showMsg(data.messageText);
                    window.location.reload();
                } else {
                    showMsg(data.messageText);
                }
            }
        })
    }

    /*保存所有值至zookeeper*/
    function refreshAll() {
        var appId = $('#appId').val();
        var timestamp = (new Date()).valueOf();
        var postUrl = "/app/config/refresh/zookeeper/all/" + appId + "?_=" + timestamp;
        var jsonData = {};
        $.ajax({
            url: postUrl,
            type: "post",
            dataType: "json",
            async: false,
            success: function(data){
                if (data.code == 0){
                    showMsg(data.messageText);
                    window.location.reload();
                } else {
                    showMsg(data.messageText);
                }
            }
        })
    }

    /*触发修改value , description 至数据库*/
    function changeDescription(ele){
        $('#configId').val($(ele).val());
        $("#updateModal").find("input[name=value]").val($(ele).parent().parent().find("td[name='value']").text());
        $("#updateModal").find("input[name=description]").val($(ele).parent().parent().find("td[name='description']").text());
    }

    /*保存value, description 至数据库*/
    $(function(){
       $('button[name=saveParam]').click(function(){
           var id = $('#configId').val();
           var description = $("#updateModal").find("input[name=description]").val();
           var value = $("#updateModal").find("input[name=value]").val();
           if (value == null || value == "" || value == undefined){
               showMsg("值不能为空");
               return false;
           }
           description = trim(description);
//           拼接参数
           var jsonData = {};
           jsonData.id = id;
           jsonData.description = description;
           jsonData.value = value;
           $.ajax({
               url: "/app/config/save",
               type: "post",
               data: jsonData,
               dataType: "json",
               async: "false",
               success: function(data){
                   if (data.code == 0){
                       showMsg(data.messageText);
                       window.location.reload();
                   } else {
                       showMsg(data.messageText);
                   }
               }
           })
           $(this).prev('button').trigger('click');
       })
    });

    /*新增config至数据库*/
    $('button[name=addNewConfig]').click(function(){
        var appId = $('#appId').val();

        var name = $(this).parent().parent().find('input[name=name]').val();
        var value = $(this).parent().parent().find('textarea[name=value]').val();
        var description =  ($(this).parent().parent().find('input[name=description]').val());

        var msg = "";
        var flag = true;
        if (isBlank(name)){
            showMsg("名字不能为空");
            return;
        } else if (isBlank(value)) {
            showMsg("值不能为空");
            return;
        }
        description = trim(description);
        var jsonData = {};
        jsonData.appId = appId;
        jsonData.name = name;
        jsonData.value = value;
        jsonData.description = description;
        $.ajax({
            url: "/app/config/add",
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
        $(this).prev('button').trigger('click');
    })
</script>
</div>
</@page>