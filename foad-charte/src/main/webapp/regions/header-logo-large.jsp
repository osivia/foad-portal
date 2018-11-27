<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />


<h1 class="logo-tribu-large">
    <a href="${requestScope['osivia.home.url']}">
        <img src="${contextPath}/img/logo-tribu-large.png" alt="${requestScope['osivia.header.application.name']}">
    </a>
</h1>
