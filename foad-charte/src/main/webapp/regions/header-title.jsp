<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />


<div class="header-title">
    <a href="${requestScope['osivia.home.url']}">
        <img src="${contextPath}/img/logo-tribu-small.png" alt="${requestScope['osivia.header.application.name']}">
    </a>

    <span>${requestScope['stats.space.title']}</span>
</div>
