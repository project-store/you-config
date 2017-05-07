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

        <!-- 修改栏 -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="z-index:1100">
            <div class="modal-dialog" style="width:450px">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <br/>
                        <h4 class="modal-title" id="myModalLabel">
                            <div class="form-group form-inline">
                                <section class="content bgcolor-6">
                                <span class="input input--juro">
                                    <input class="input__field input__field--juro" type="text" id="newValue" style="margin-bottom:50px;"/>
                                    <label class="input__label input__label--juro" for="input-28" style="margin-top:-10px;">
                                        <span class="input__label-content input__label-content--juro" id="newKey"></span>
                                    </label>
                                </span>
                                </section>
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
        <input type="hidden" id="id" value=${id}>
        <input type="hidden" id="machine" value=${machine}>
        <input type="hidden" id="appName" value=${appName}>

        <div class="col-xs-12" style="margin-left:60px;">
            <button class="btn btn-success btn-sm" data-toggle="modal" onclick="reflushAll()" style="margin-top:-40px;">
                刷新zookeeper
            </button>&nbsp;&nbsp;&nbsp;&nbsp;
            <div class="box box-primary">

                <div class="box box-primary">
                    <!-- 数据展示栏1 -->
                    <div class="box-body table-responsive no-padding">
                        <table class="table table-hover table-striped">
                            <thead>
                            <tr><h3 style="text-align:center;margin-left:-200px;margin-bottom:40px;">数据源详细配置</h3></tr>
                            <tr>
                                <td><label>key</label></td>
                                <td><label>mysqlValue</label></td>
                                <td><label>zookeeperValue</label></td>
                                <td><label>状态</label></labe.></td>
                                <td><label>操作</label></td>
                            </tr>
                            </thead>
                            <tbody>
                                <#list other as ot>
                                <tr >
                                    <td name="key" style="width:25%;" value="${ot.key}">${ot.key!''}</td>
                                    <td name="value" style="width:20%;">${ot.mysqlValue!''}</td>
                                    <td style="width:20%;">${ot.zooValue!''}</td>
                                    <td>
                                        <#if ot.compare == 1>
                                            <button class="btn btn-primary" disabled>同步</button>
                                        <#else>
                                            <button class="btn btn-primary" disabled>不同步</button>
                                        </#if>
                                    </td>
                                    <td>
                                        <button class="btn btn-primary" data-toggle="modal" data-target="#myModal" onclick="changeAttr(this)" >
                                        修改
                                        </button>&nbsp&nbsp&nbsp&nbsp
                                        <button class="btn btn-primary" data-toggle="modal" onclick="setDateToZookeeper(this, '')">
                                            更新zookeeper
                                        </button>
                                    </td>
                                </tr>
                                </#list>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- 数据展示栏2 -->
                <div class="box-body table-responsive no-padding">
                    <table class="table table-hover table-striped">
                        <tbody>
                            <#list config as co>
                                <tr>
                                    <td name="key" style="width:25%;" value="${co.key}">&nbsp;&nbsp;|--->&nbsp;&nbsp;${co.key!''}</td>
                                    <td name="value" style="width:20%;">${co.mysqlValue!''}</td>
                                    <td style="width:20%;">${co.zooValue!''}</td>
                                    <td>
                                        <#if co.compare == 1>
                                            <button class="btn btn-primary" disabled>同步</button>
                                        <#else>
                                            <button class="btn btn-primary" disabled>不同步</button>
                                        </#if>
                                    </td>
                                    <td>
                                        <button class="btn btn-primary" data-toggle="modal" data-target="#myModal" onclick="changeAttr(this)">
                                          修改
                                        </button>&nbsp&nbsp&nbsp&nbsp
                                        <button class="btn btn-primary" data-toggle="modal" onclick="setDateToZookeeper(this, 'config/')">
                                            更新zookeeper
                                        </button>
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
    <#-- 修改数据源的值 -->
    function changeAttr(ele){
        $('#newKey').text($(ele).parent().parent().find("td[name=key]").text());
        $('#newValue').val($(ele).parent().parent().find("td[name=value]").text());
    }

    /*将修改后的值保持到mysql*/
    $('button[name=saveParam]').click(function(){
        var id = $('#id').val().trim();
        var machine = $('#machine').val();
        var key = $('#newKey').text().trim();
        var value = $('#newValue').val().trim();
        var appName = $('#appName').val().trim();
        var jsonData = {};
        jsonData.machine = machine;
        jsonData.id = id;
        jsonData.key = key;
        jsonData.value = value;
        jsonData.appName = appName;
        $.ajax({
            url: "/ds/alter/detail",
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
        $(this).prev('button').trigger('click');
    })
</script>
<script>
    /*更新具体值到zookeeper*/
    function setDateToZookeeper(ele, path){
        var appName = $('#appName').val();
        var machine = $('#machine').val();
        var key = path + $(ele).parent().parent().find("td[name=key]").attr("value").trim();
        var value = $(ele).parent().parent().find("td[name=value]").text().trim();
        var jsonData = {};
        jsonData.appName = appName;
        jsonData.machine = machine;
        jsonData.key = key;
        jsonData.value = value;
        $.ajax({
            url: "/ds/update/zookeeper",
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
    }
</script>
<script>
    /*更新所有值到zookeeper*/
    function reflushAll(){
        var id = $('#id').val();
        var appName = $('#appName').val();
        var dataJson = {};
        dataJson.id = id;
        dataJson.appName = appName;
        $.ajax({
            url: "/ds/reflush/all",
            type: "post",
            data: dataJson,
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
    }
</script>
</@page>