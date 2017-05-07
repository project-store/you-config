<#-- 自定义freemarker的宏 -->
<#macro page title="koolearn" assets=[] useheader="default" usefooter="default" description="" keywords="">
<!DOCTYPE HTML>
<html>
<head>
    <title>${title!''}</title>
    <meta name="description" content="${description!''}" />
    <meta name="keywords" content="${keywords!''}" />
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit"/>
    <meta name="viewport" content="width=device-width, initial-scale=1,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'/>
    <!-- Bootstrap 3.3.4 -->
    <link href="/static/css/bootstrap.min.css" rel="stylesheet" type="text/css" />

    <!-- FontAwesome 4.3.0 -->
    <link href="/static/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <!-- Theme style -->
    <link href="/static/css/AdminLTE.min.css" rel="stylesheet" type="text/css" />
    <!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
    <link href="/static/css/skins/_all-skins.min.css" rel="stylesheet" type="text/css" />
    <!-- jQuery 2.1.4 -->
    <#-- sweetalert2 -->
    <link href="/static/css/sweetalert2.min.css" rel="stylesheet" type="text/css"/>

    <link rel="stylesheet" type="text/css" href="/static/css/normalize.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/inputdemo.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/component.css" />
    <script src="/static/js/jQuery-2.1.4.min.js" type="text/javascript"></script>
    <script src="/static/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="/static/js/app.min.js" type="text/javascript"></script>
    <script src="/static/js/sweetalert2.min.js"></script>
    <script src="/static/js/validate.js"></script>
    <#--<script src="https://cdn.jsdelivr.net/promise.prototype.finally/1.0.1/finally.js"></script>-->
    <#-- sweetAlert 展示栏 -->
    <script src="/static/js/config.js"></script>
    <script src="/static/js/classie.js"></script>
</head>
<body class="skin-blue sidebar-mini">

<div class="wrapper">

    <header class="main-header">
        <!-- Logo -->
        <a href="" class="logo">
            <!-- mini logo for sidebar mini 50x50 pixels -->
            <span class="logo-mini"><b>配置</b></span>
            <!-- logo for regular state and mobile devices -->
            <span class="logo-lg"><b>配置</b>中心</span>
        </a>
        <!-- Header Navbar: style can be found in header.less -->
        <nav class="navbar navbar-static-top" role="navigation">
            <!-- Sidebar toggle button-->
            <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
                <span class="sr-only">Toggle navigation</span>
            </a>
        </nav>
    </header>

    <!-- Left side column. contains the logo and sidebar -->
    <aside xmlns:th="http://www.thymeleaf.org"
           th:fragment="sidebar" class="main-sidebar">
        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">
            <!-- sidebar menu: : style can be found in sidebar.less -->
            <ul class="sidebar-menu">
                <li class="header">功能导航</li>
                <li>
                    <a href="/app/info/list">
                        <i class="fa fa-cubes"></i> <span>App配置</span>
                    </a>
                </li>
                <li>
                    <a href="/datasource/config/list">
                        <i class="fa fa-cubes"></i> <span>数据源配置</span>
                    </a>
                </li>
            </ul>
        </section>
    </aside>

    <div class="content-wrapper">
        <#nested >
    </div>
    <footer class="main-footer">
        <div class="pull-right hidden-xs">
            <b>Version</b> 1.0.0
        </div>
        <strong>Copyright &copy; 2017-2037 miao.you.meng</strong> All Rights Reserved
    </footer>
</div>
</body>
</html>
</#macro>