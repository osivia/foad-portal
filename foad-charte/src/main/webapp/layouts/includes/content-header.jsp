<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<div class="content-header-container">
    <!-- Breadcrumb -->
    <c:if test="${not home}">
        <p:region regionName="breadcrumb" />
    </c:if>
    
    <div class="content-header-navbar">
        <!-- Content title -->
        <p:region regionName="content-title" />
        
        <!-- Menubar -->
        <p:region regionName="menubar" />    
    </div>
    
    <!-- Content header -->
    <c:if test="${not maximized}">
        <p:region regionName="content-header" />
    </c:if>
</div>
