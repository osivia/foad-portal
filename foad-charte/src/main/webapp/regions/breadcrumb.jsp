<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<c:set var="breadcrumb" value="${requestScope['osivia.breadcrumb']}" />


<nav>
    <h2 class="sr-only"><op:translate key="BREADCRUMB_TITLE" /></h2>
    <ol class="breadcrumb">
        <!-- Tribu home -->
        <li>
            <a href="${requestScope['osivia.home.url']}">
                <span>${requestScope['osivia.header.application.name']}</span>
            </a>
        </li>
    
        <c:forEach var="child" items="${breadcrumb.children}" varStatus="breadcrumbStatus">
            <c:choose>
                <c:when test="${breadcrumbStatus.last}">
                    <li class="active">
                        <span>${child.name}</span>
                    </li>
                </c:when>
                
                <c:otherwise>
                    <li>
                        <a href="${child.url}">${child.name}</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </ol>
</nav>
