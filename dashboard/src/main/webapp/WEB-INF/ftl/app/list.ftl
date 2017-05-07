<@page title="配置中心" description="配置中心"  keywords="配置中心" assets=[
]>
<section class="content-header">
    <h1>
        app 列表
        <small>trunk环境</small>
    </h1>
    <ol class="breadcrumb">
        <li><a href="/home"><i class="fa fa-home"></i>Home</a></li>
    </ol>
</section>

<!-- Main content -->
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-primary">
                <!-- 修改框 -->
                <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="z-index:1100;"
                     aria-hidden="true">
                    <div class="modal-dialog" style="width:400px">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            </div>
                            <div class="modal-body">
                                <input name="id" value="" type="hidden">
                                <h4 class="modal-title" id="myModalLabel">
                                    <div class="form-group form-inline">
                                        <label for="name">所属组:</label>
                                        <select name="business" class="form-control" style="width:300px">
                                        <#list businessList as business>
                                            <option value="${business.left}">${business.right}</option>
                                        </#list>
                                        </select>
                                    </div>
                                    <div class="form-group form-inline">
                                        <label for="name">&nbsp;&nbsp;描&nbsp;&nbsp;述&nbsp;:</label>
                                        <input name="description" type="text" maxlength="50" class="form-control" style="width:300px">
                                    </div>
                                    <div class="form-group form-inline">
                                        <label for="name">&nbsp;&nbsp;邮&nbsp;&nbsp;箱&nbsp;:</label>
                                        <input name="email" type="text" maxlength="50" class="form-control" style="width:300px">
                                    </div>
                                </h4>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                <button type="button" class="btn btn-primary" name="saveParam">保存</div>
                        </div>
                    </div><!-- 修改框 -->
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
                                        <input type="text" name="name" maxlength="20" class="form-control" style="width:300px">
                                    </div>
                                    <div class="form-group form-inline">
                                        <label for="name">业务组&nbsp;:</label>
                                        <select name="business" class="form-control" style="width:300px" >
                                        <#list businessList as business>
                                            <option value="${business.left}">${business.right}</option>
                                        </#list>
                                        </select>
                                    </div>
                                    <div class="form-group form-inline">
                                        <label for="name">&nbsp;&nbsp;描&nbsp;&nbsp;&nbsp;述&nbsp;:</label>
                                        <input name="description" type="text" maxlength="50" class="form-control" style="width:300px">
                                    </div>
                                    <div class="form-group form-inline">
                                        <label for="name">&nbsp;&nbsp;邮&nbsp;&nbsp;&nbsp;件&nbsp;:</label>
                                        <input name="email" type="text" maxlength="50" class="form-control" style="width:300px">
                                    </div>
                                </h4>
                            </div>
                            <input type="hidden" value="" id="configId"/>
                            <div class="modal-footer" style="marign:0,auto;">
                                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                <button type="button" class="btn btn-primary" name="addNewApp" >新增</button>
                            </div>
                        </div>
                    </div><!-- 新增框 -->
                </div>
                <div class="box-header">
                    <h3 class="box-title">
                        <button class="btn btn-success btn-sm add icon" data-toggle="modal" data-target="#secondModal">
                            + &nbsp新增
                        </button>
                    </h3>
                </div>
                <!-- 数据展示栏 -->
                <div class="box-body table-responsive no-padding">
                    <table class="table table-hover table-striped">
                        <thead>
                        <tr>
                            <th>id</th>
                            <th>name</th>
                            <th>group</th>
                            <th>更新时间</th>
                            <th>description</th>
                            <th>emails</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#assign count=0>
                            <#list appList as app>
                            <tr id="appId_${app.id}">
                                <td name="appId">${app.id}</td>
                                <td>${app.name}</td>
                                <td name="business" businessId="${app.business}">
                                <#list businessList as business>
                                <#if business.left = app.business>
                                    ${business.right}
                                </#if>
                                </#list>
                                </td>
                                <td>${app.ts?string("yyyy-MM-dd HH:mm")}</td>
                                <td name="description">${app.description!}</td>
                                <td name="email">${app.email!}</td>
                                <td>
                                    <button class="btn btn-primary" data-toggle="modal" data-target="#myModal" value="${app.id}" onclick="transportParam(this)">
                                        修改
                                    </button>
                                    <button class="btn btn-primary" data-toggle="modal" onclick="window.location='/app/config/show/all/${app.id}'" >详情</button>
                                </td>
                            </tr>
                            <#assign count = count + 1>
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
     /*将数据放入修改栏*/
    function transportParam(ele){
        var saveParam = $('button[name=saveParam]').parent().parent();
        var appId = $(ele).val();
        $(saveParam).parent().parent().find('input[name=id]').val(appId);
        $(saveParam).parent().parent().find('select[name=business]').val($(ele).parent().parent().find("td[name='business']").attr("businessId"));
        $(saveParam).parent().parent().find('input[name=description]').val($(ele).parent().parent().find("td[name='description']").text());
        $(saveParam).parent().parent().find('input[name=email]').val($(ele).parent().parent().find("td[name='email']").text());
    }
    /*保存数据*/
    $(function(){
        $('button[name=saveParam]').click(function(){
            var id = $(this).parent().parent().find('input[name=id]').val();
            var business = $(this).parent().parent().find('select[name=business]').val();
            var description = $(this).parent().parent().find('input[name=description]').val();
            var email =  trim($(this).parent().parent().find('input[name=email]').val());
            if (isBlank(business)){
                showMsg("所属组不能为空");
                return;
            } else if (!isBlank(email) && !isEmail(email)){
                showMsg("邮件格式不正确");
                return;
            }
            var url = "/app/info/update/" + id;
            var jsonData = {};
            jsonData.business = business;
            jsonData.description = description;
            jsonData.email = email;
            $.ajax({
                url: url,
                type: "post",
                data: jsonData,
                dataType:"json",
                async: false,
                success: function(data){
                    if(data.code == 0) {
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

     /*新增数据*/
    $('button[name=addNewApp]').click(function(){
        var name = $(this).parent().parent().find('input[name=name]').val();
        var business = $(this).parent().parent().find('select[name=business]').val();
        var description =  ($(this).parent().parent().find('input[name=description]').val());
        var email =  trim($(this).parent().parent().find('input[name=email]').val());

        if (isBlank(name)){
            showMsg("名字不能为空");
            return;
        } else if (isBlank(business)){
            showMsg("所属组不能为空");
            return;
        } else if (!isBlank(email) && !isEmail(email)){
            showMsg("邮件格式不正确");
            return;
        }
        var jsonData = {};
        jsonData.name = name;
        jsonData.business = business;
        jsonData.description = description;
        jsonData.email = email;
        $.ajax({
            url: "/app/info/add",
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
    })
</script>
</@page>