<@page title="配置中心" description="配置中心"  keywords="配置中心" assets=[
]>
<section class="content-header">
    <h1>
    ${appName!''}
        <small>数据源配置列表</small>
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
                <input type="hidden" id="id" value=${id!''}>
                <input type="hidden" id="appName" value=${appName!''}>
                <!-- 数据展示栏 -->
                <div class="box-body table-responsive no-padding">
                    <table class="table table-hover table-striped">
                        <thead>
                        <tr>
                            <th>类型</th>
                            <th>HostName</th>
                            <th>Url</th>
                            <th>UserName</th>
                            <th>Password</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list configList as config>
                            <tr>
                                <td>
                                <label>
                                <#if config.type = 'master'>
                                ${config.displayName!''}
                                <#elseif config.type = 'slave'>
                                ${config.displayName!''}${config_index}
                                <#else>
                                    错误
                                </#if>

                                </label>
                                </td>
                                <td>${config.connectionPool.host!''}</td>
                                <td>${config.connectionPool.url!''}</td>
                                <td>${config.connectionPool.username!''}</td>
                                <td>${config.connectionPool.password!''}</td>
                                <td>
                                    <#if config.type = 'master'>
                                        <a href="/datasource/config/detail/master/${id}/" class="btn btn-primary" role="button">详情</a>
                                    <#elseif config.type = 'slave'>
                                        <a href="/datasource/config/detail/slave/${id}/${config_index}" class="btn btn-primary" role="button">详情</a>
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
</@page>