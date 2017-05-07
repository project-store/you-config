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
                <input type="hidden" id="id" value=${id}>
                <input type="hidden" id="appName" value=${appName}>
                <!-- 数据展示栏 -->
                <div class="box-body table-responsive no-padding">
                    <table class="table table-hover table-striped">
                        <thead>
                        <tr>
                            <th>machine</th>
                            <th>Host</th>
                            <th>Url</th>
                            <th>userName</th>
                            <th>password</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list list as machine>
                            <tr>
                                <td><label>${machine.machine!''}</label></td>
                                <td>${machine.host!''}</td>
                                <td>${machine.url!''}</td>
                                <td>${machine.userName!''}</td>
                                <td>${machine.password!''}</td>
                                <td>
                                    <button class="btn btn-primary" data-toggle="modal" onclick="jump('${machine.machine}')">
                                        详情
                                    </button>
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
/*跳转到数据源配置详情页面*/
function jump(machine){
    var id = $('#id').val();
    var appName = $('#appName').val();
    window.location.href="/ds/detail/machine?id=" + id + "&machine=" + machine + "&appName=" + appName;
}
</script>
</@page>