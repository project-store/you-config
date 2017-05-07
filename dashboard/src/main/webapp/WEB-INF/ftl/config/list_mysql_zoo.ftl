<@page title="配置中心" description="配置中心"  keywords="配置中心" assets=[
]>
<section class="content-header">
    <h1>
        <small>应用&nbsp;&nbsp;&nbsp;</small>
    ${appName!}
        <small>&nbsp;&nbsp;&nbsp;配置</small>
    </h1>
    <ol class="breadcrumb">
        <li><a href=""><i class="fa fa-home"></i> Home</a></li>
        <li class="active"><a href="/app/info/list">app 列表</a></li>
        <li class="active"><a href="/app/config/show/all/67">配置详情</a></li>
    </ol>
</section>

<!-- Main content -->
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-primary">
                <input type="hidden" value="${appId}" id="appId"/>
                <!-- 数据展示栏 -->
                <div class="box-body table-responsive no-padding">
                    <table class="table table-hover table-striped">
                        <thead>
                            <tr>
                                <th>id</th>
                                <th>key</th>
                                <th>mysql值</th>
                                <th>zookeeper值</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <#list list as zooMysql>
                            <tr >
                                <td name="configId">${zooMysql.id}</td>
                                <td name="name">${zooMysql.name}</td>
                                <td name="mysqlValue">${zooMysql.mysqlValue}</td>
                                <td name="zooValue">${zooMysql.zooValue!''}</td>
                                <td>
                                    <button class="btn btn-primary" data-toggle="modal" onclick="updateZookeeper(this)">
                                        更新zookeeper
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
/*更新具体值到zookeeper*/
function updateZookeeper(ele) {
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
</script>
</div>
</@page>