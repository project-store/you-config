<@page title="配置中心" description="配置中心"  keywords="配置中心" assets=[
]>
<section class="content-header">
    <h1>
       [${type!''}${index!''}] 数据源:[${appName!''}]
        <small>详细配置</small>
    </h1>
    <ol class="breadcrumb">
        <li><a href=""><i class="fa fa-home"></i> Home</a></li>
        <li class="active">Hosts</li>
    </ol>
</section>
<!-- Main content -->
<section class="content">
    <div class="row">
        <!-- 修改栏 -->
        <div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
             aria-hidden="true" style="z-index:1100">
            <div class="modal-dialog" style="width:450px">
                <form action="/datasource/config/update" method="post" onsubmit="return ajaxSubmitCallback(this);">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <br/>
                            <h4 class="modal-title" >
                                <div class="form-group form-inline">
                                    <label for="name">&nbsp;Key&nbsp;:</label>
                                    <input type="text" name="value" class="form-control" style="width:300px">
                                </div>
                            </h4>
                        </div>
                        <input type="hidden" id="dataSourceId" name="dataSourceId" value=${id}>
                        <input type="hidden" id="slave_index" name="index" value=${index!''}>
                        <input type="hidden" name="type" value="${type!''}">
                        <input type="hidden" name="key" value="">
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                            <input type="submit" class="btn btn-primary" value="保存"/>
                        </div>
                    </div>
                </form>
            </div>
            <!-- /.modal -->
        </div>

        <div class="col-xs-12">
        <#if type = 'master'>
            <button  class="btn btn-success btn-sm" data-toggle="modal" onclick="refreshMasterAll()" style="margin-top:-40px;margin-left: 10px; ">
                同步zookeeper
            </button>
        <#elseif type = 'slave'>
            <button  class="btn btn-success btn-sm" data-toggle="modal" onclick="refreshSlaveAll()" style="margin-top:-40px;margin-left: 10px; ">
                同步zookeeper
            </button>
        <#else>

    </#if>

            <div class="box box-primary">

                <!-- 数据展示栏1 -->
                <div class="box-body table-responsive no-padding">
                    <table class="table table-hover table-striped">
                        <thead>
                        <tr>
                            <td><label>key</label></td>
                            <td><label>mysql值</label></td>
                            <td><label>zookeeper值</label></td>
                            <td><label>状态</label></labe.></td>
                            <td><label>操作</label></td>
                        </tr>
                        </thead>
                        <tbody>
                            <#list list as item>
                            <tr>
                                <td style="width:10%;">${item.key!''}</td>
                                <td style="width:20%;">${item.mysqlValue!''}</td>
                                <td style="width:20%;">${item.zkValue!''}</td>
                                <td style="width:80px;">
                                    <#if item.same>
                                        <button class="btn btn-primary" disabled>同步</button>
                                    <#else>
                                        <button class="btn btn-primary" disabled>不同步</button>
                                    </#if>
                                </td>
                                <td>
                                    <button class="btn btn-primary"  key="${item.key!''}" data-toggle="modal" data-target="#updateModal" onclick="changeAttr(this)">修改
                                    </button>
                                    <#if type = 'master'>
                                        <button class="btn btn-primary" data-toggle="modal"
                                                onclick="masterKey2Zookeeper('${item.key!''}')">
                                            同步
                                        </button>
                                    <#elseif type = 'slave'>
                                        <button class="btn btn-primary" data-toggle="modal"
                                                onclick="slaveKey2Zookeeper('${item.key!''}')">
                                            同步
                                        </button>
                                    <#else>
                                        错误
                                    </#if>

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
    /*更新具体值到zookeeper*/
    <#if type = 'master'>
    function masterKey2Zookeeper(key) {
        var dataSourceId = $('#dataSourceId').val();
        var jsonData = {};
        jsonData.key = key;
        $.ajax({
            url: "/datasource/config/zookeeper/master/key/" + dataSourceId,
            type: "post",
            data: jsonData,
            dataType: "json",
            async: "false",
            success: function(data){
                var msg = data.messageText;
                if (data.code == 0){
                    showMsg(msg);
                    setTimeout(window.location.reload(), 5000, true);
                } else {
                    showMsg(msg);
                }
            }
        })
    }

    function refreshMasterAll(){
        var dataSourceId = $('#dataSourceId').val();
        $.ajax({
            url: "/datasource/config/refresh/master/all",
            type: "post",
            dataType: "json",
            data:{"id" : dataSourceId},
            async: "false",
            success: function(data){
                var msg = data.messageText;
                if (data.code == 0){
                    showMsg(msg);
                    setTimeout(window.location.reload(), 5000, true);
                } else {
                    showMsg(msg);
                }
            }
        })
    }
    <#elseif type = 'slave'>
    function slaveKey2Zookeeper(key) {
        var dataSourceId = $('#dataSourceId').val();
        var slaveIndex = $('#slave_index').val();
        var jsonData = {};
        jsonData.key = key;
        jsonData.index = slaveIndex;
        $.ajax({
            url: "/datasource/config/zookeeper/slave/key/" + dataSourceId,
            type: "post",
            data: jsonData,
            dataType: "json",
            async: "false",
            success: function(data){
                var msg = data.messageText;
                if (data.code == 0){
                    showMsg(msg);
                    setTimeout(window.location.reload(), 5000, true);
                } else {
                    showMsg(msg);
                }
            }
        })
    }

    function refreshSlaveAll(){
        var dataSourceId = $('#dataSourceId').val();
        var slaveIndex = $('#slave_index').val();
        $.ajax({
            url: "/datasource/config/refresh/slave/all",
            type: "post",
            dataType: "json",
            data: {"id" : dataSourceId,"index":slaveIndex},
            async: "false",
            success: function(data){
                var msg = data.messageText;
                if (data.code == 0){
                    showMsg(msg);
                    setTimeout(window.location.reload(), 5000, true);
                } else {
                    showMsg(msg);
                }
            }
        })
    }
    <#else>
    </#if>

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

    function changeAttr(ele){
        $('#updateModal').find("input[name='key']").val($(ele).attr("key").trim());
    }
</script>
</@page>