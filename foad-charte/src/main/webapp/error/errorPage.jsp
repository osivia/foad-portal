<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<html>

<head>
    <title><op:translate key="ERROR" /> - <op:translate key="BRAND" /></title>
    
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    
    <!-- jQuery -->
    <script type='text/javascript' src='/osivia-portal-custom-web-assets/components/jquery/jquery-1.12.4.min.js'></script>
    <script type='text/javascript' src='/osivia-portal-custom-web-assets/js/jquery-integration.min.js'></script>
    <script type='text/javascript' src='/osivia-portal-custom-web-assets/components/jquery-ui/jquery-ui-1.11.3.min.js'></script>
    <link rel='stylesheet' href='/osivia-portal-custom-web-assets/components/jquery-ui/jquery-ui-1.11.3.min.css'>
    <script type='text/javascript' src='/osivia-portal-custom-web-assets/components/jquery-mobile/jquery.mobile.custom.min.js'></script>
    
    <!-- Bootstrap -->
    <link rel='stylesheet' href='/osivia-portal-custom-web-assets/css/bootstrap/bootstrap.min.css' title='Bootstrap'>
    <script src='/osivia-portal-custom-web-assets/components/bootstrap/js/bootstrap.min.js'></script>
    
    <!-- Socle -->
    <link rel='stylesheet' href='/osivia-portal-custom-web-assets/css/osivia.min.css'>
    <script src='/osivia-portal-custom-web-assets/js/socle.min.js'></script>
    
    <!-- Glyphicons -->
    <link rel='stylesheet' href='/osivia-portal-custom-web-assets/css/glyphicons.min.css'>
    
    <!-- Style FOAD -->
    <meta http-equiv="default-style" content="FOAD">
    <link rel="stylesheet" href="/foad-charte/css/foad.min.css" title="FOAD" />
    <link rel="icon" href="/foad-charte/img/favicon.ico" />
</head>


<body>
    <!-- Toolbar -->
    <div class="toolbar">
        <div class="navbar navbar-default navbar-fixed-top">
            <div class="container-fluid">
                <div class="navbar-header">
                    <div class="visible-xs">
                        <button type="button" onclick="toggleDrawer()" data-toggle="drawer" class="btn btn-link navbar-btn pull-left">
                            <span>
                                <i class="halflings halflings-menu-hamburger"></i>
                                <i class="halflings halflings-arrow-right"></i>
                            </span>
                        </button>
                    
                        <!-- Title -->
                        <div class="clearfix">
                            <p class="navbar-text text-overflow"><op:translate key="ERROR" /></p>
                        </div>
                    </div>
                
                    <!-- Brand -->
                    <a href="/" class="navbar-brand hidden-xs"><op:translate key="BRAND" /></a>
                </div>
            </div>
        </div>
    </div>


    <!-- Header -->
    <header class="hidden-xs">
        <div class="container-fluid">
            <!-- Logo -->
            <div class="logo">
                <!-- FOAD -->
                <a href="/portal"> <img src="/foad-charte/img/logo-foad.png" alt="FOAD, Formation Ouverte À Distance">
                </a>

                <!-- Ministère -->
                <img src="/foad-charte/img/logo-ministere.png" alt="Ministère de l'Éducation Nationale, de l'Enseignement supérieur et de la Recherche">
            </div>
        </div>

        <div class="background-fading"></div>
    </header>


    <!-- Tabs -->
    <div class="tabs-container hidden-xs">
        <div class="container-fluid">
            <nav class="tabs" role="navigation">
                <!-- Title -->
                <h2 class="sr-only"><op:translate key="TABS_TITLE" /></h2>

                <ul>
                    <li class="">
                        <a href="#">
                            <span><op:translate key="ERROR" /></span>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>


    <!-- Page content -->
    <div class="wrapper-outer">
        <div class="wrapper-inner">
            <main id="page-content" class="container-fluid">
                <div class="content-navbar">
                    <!-- Breadcrumb -->
                    <div class="content-navbar-breadcrumb">
                        <nav>
                            <h2 class="sr-only"><op:translate key="BREADCRUMB_TITLE" /></h2>
                            <ol class="breadcrumb hidden-xs">
                                <li class="active">
                                    <span><op:translate key="ERROR" /></span>
                                </li>
                            </ol>
                        </nav>
                    </div>
                </div>
        
        
                <div class="row">
                    <div class="col-lg-offset-3 col-lg-6">
                        <div class="panel panel-danger">
                            <div class="panel-body">
                                <div class="page-header">
                                    <h1 class="text-center text-danger">
                                        <span><op:translate key="ERROR" /></span>
                                        
                                        <c:choose>
                                            <c:when test="${(param['httpCode'] eq 401) || (param['httpCode'] eq 403) || (param['httpCode'] eq 404)}">
                                                <span>${param['httpCode']}</span>
                                                <small><op:translate key="ERROR_${param['httpCode']}" /></small>
                                            </c:when>
                                            
                                            <c:otherwise>
                                                <small><op:translate key="ERROR_GENERIC_MESSAGE" /></small>
                                            </c:otherwise>
                                        </c:choose>
                                    </h1>
                                </div>
                                
                                <c:if test="${(param['httpCode'] eq 401) || (param['httpCode'] eq 403) || (param['httpCode'] eq 404)}">
                                    <p class="lead text-center">
                                        <span><op:translate key="ERROR_${param['httpCode']}_MESSAGE" /></span>
                                    </p>
                                </c:if>
                                
                                <div class="text-center">
                                    <a href="/" class="btn btn-link">
                                        <span><op:translate key="BACK_TO_HOME" /></span>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>    
                </div>
            </main>
        </div>
    </div>


    <!-- Footer -->
    <footer class="hidden-xs">
        <div class="container-fluid">
            <div class="logo">
                <img src="/foad-charte/img/logo-foad-footer.png" alt="FOAD">
            </div>
        </div>
    </footer>
</body>

</html>
