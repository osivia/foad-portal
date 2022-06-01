<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />


<header>
    <div class="container-fluid">
        <div class="row">
            <div class="col">
                <c:if test="${drawer}">
                    <button type="button" onclick="toggleDrawer()" class="btn btn-link" data-toggle="drawer">
                        <i class="glyphicons glyphicons-menu-hamburger"></i>
                    </button>
                </c:if>
            
                <c:choose>
                    <c:when test="${workspace}">
                        <!-- Header title -->
                        <p:region regionName="header-title" />
                    </c:when>
                    
                    <c:otherwise>
                        <!-- Logo EN -->
                        <div class="logo-en hidden-xs">
                            <img src="${contextPath}/img/logo-en.jpg" alt="Minist&egrave;re de l'&Eacute;ducation nationale et de la Jeunesse">
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <div class="col">
                <!-- Toolbar -->
                <div class="toolbar">
                    <ul class="list-inline">                   
                        <!-- Administration -->
                        <p:region regionName="toolbar-administration" />
                        
                        <!-- Search -->
                        <li>
                            <p:region regionName="toolbar-search" />
                        </li>
                        
                        <!-- Tasks -->
                        <p:region regionName="toolbar-tasks" />
                        
                        <!-- User menu -->
                        <p:region regionName="toolbar-user-menu" />
                    </ul>
                    
                    
                    <p:region regionName="toolbar-v2-access" />
                    	
				    
                </div>
            </div>
        </div>
    
        <div class="row hidden-xs">
            <div class="col">
                <c:if test="${workspace}">
                    <!-- Workspace tabs -->
                    <p:region regionName="workspace-tabs" />
                </c:if>
            </div>
        
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
    
        <c:if test="${not workspace}">
            <!-- Logo Tribu large -->
            <p:region regionName="header-logo-large" />
        </c:if>
    </div> 
</header>
