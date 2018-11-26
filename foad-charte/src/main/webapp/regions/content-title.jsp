<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<c:choose>
    <c:when test="${empty requestScope['page.content.title']}"><c:set var="title">${requestScope['osivia.header.title']}</c:set></c:when>
    <c:otherwise><c:set var="title">${requestScope['page.content.title']}</c:set></c:otherwise>
</c:choose>


<h1 class="content-title">${title}</h1>
