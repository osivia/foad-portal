<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<c:set var="brand"><op:translate key="BRAND" /></c:set>


<html>

<head>
    <title><op:translate key="ERROR" /> - ${brand}</title>
    
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <!-- Socle -->
    <link rel='stylesheet' href='/osivia-portal-custom-web-assets/css/osivia.min.css'>
    
    <!-- Glyphicons -->
    <link rel='stylesheet' href='/osivia-portal-custom-web-assets/css/glyphicons.min.css'>
    
    <!-- Style FOAD -->
    <meta http-equiv="default-style" content="FOAD">
    <link rel="stylesheet" href="/foad-charte/css/foad.min.css" title="FOAD" />
    <link rel="icon" href="/foad-charte/img/favicon.ico" />
</head>


<body>
    <header>
        <div class="container-fluid">
            <div class="row">
                <div class="col">
                    <!-- Logo EN -->
                    <div class="logo-en hidden-xs">
                        <img src="/foad-charte/img/logo-en.png" alt="Minist&egrave;re de l'&Eacute;ducation nationale">
                    </div>
                </div>

                <div class="col"></div>
            </div>

            <div class="row hidden-xs">
                <div class="col"></div>

                <div class="col">
                    <div class="dots">
                        <span class="dot-blue"></span>
                        <span class="dot-green"></span>
                        <span class="dot-yellow"></span>
                        <span class="dot-orange"></span>
                        <span class="dot-pink"></span>
                        <span class="dot-violet"></span>
                    </div>
                </div>
            </div>

            <!-- Logo Tribu large -->
            <h1 class="logo-tribu-large">
                <img src="/foad-charte/img/logo-tribu-large.png" alt="${brand}">
            </h1>
        </div>
    </header>


    <main>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-offset-2 col-md-8 col-lg-offset-3 col-lg-6">
                    <div class="content-header-container">
                        <!-- Breadcrumb -->
                        <nav>
                            <h2 class="sr-only"><op:translate key="BREADCRUMB_TITLE" /></h2>
                            <ol class="breadcrumb">
                                <!-- Tribu home -->
                                <li>
                                    <a href="/portal">
                                        <span>${brand}</span>
                                    </a>
                                </li>
        
                                <li class="active">
                                    <span><op:translate key="ERROR" /></span>
                                </li>
                            </ol>
                        </nav>
                    </div>
                    
                    
                    <div class="panel panel-danger">
                        <div class="panel-body">
                            <div class="page-header">
                                <h1 class="text-center text-danger">
                                    <span><op:translate key="ERROR" /></span>
    
                                    <c:choose>
                                        <c:when
                                            test="${(param['httpCode'] eq 401) || (param['httpCode'] eq 403) || (param['httpCode'] eq 404) || (param['httpCode'] eq 500)}">
                                            <span>${param['httpCode']}</span>
                                            <small><op:translate key="ERROR_${param['httpCode']}" /></small>
                                        </c:when>
    
                                        <c:otherwise>
                                            <small><op:translate key="ERROR_GENERIC_MESSAGE" /></small>
                                        </c:otherwise>
                                    </c:choose>
                                </h1>
                            </div>
    
                            <c:if test="${(param['httpCode'] eq 401) || (param['httpCode'] eq 403) || (param['httpCode'] eq 404) || (param['httpCode'] eq 500)}">
                                <p class="lead text-center">
                                    <span><op:translate key="ERROR_${param['httpCode']}_MESSAGE" /></span>
                                </p>
                            </c:if>
    
                            <div class="text-center">
                                <a href="/" class="btn btn-link"> <span><op:translate key="BACK_TO_HOME" /></span>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>


    <footer>
        <div class="container-fluid">
            <ul>
                <li>
                    <i class="glyphicons glyphicons-copyright-mark"></i>
                    <span>${brand}</span>
                    <span>2018</span>
                </li>
            </ul>
        </div>
    </footer>
</body>

</html>
