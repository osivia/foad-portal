<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<c:set var="userPortal" value="${requestScope['osivia.userPortal']}" />
<c:set var="userPages" value="${userPortal.userPages}" />
<c:set var="currentId" value="${requestScope['osivia.currentPageId']}" />
<c:set var="closeLabel"><op:translate key="CLOSE" /></c:set>


<nav class="tabs" role="navigation">
    <!-- Title -->
    <h2 class="sr-only"><op:translate key="TABS_TITLE" /></h2>
    
    <ul>
        <c:forEach var="userPage" items="${userPages}">
            <li class="${userPage.id eq currentId ? 'active' : ''}">
                <a href="${userPage.url}">${userPage.name}</a>
                
                <!-- Close -->
                <c:if test="${not empty userPage.closePageUrl}">
                    <a href="${userPage.closePageUrl}" title="${closeLabel}" data-toggle="tooltip" data-placement="bottom">
                        <i class="glyphicons glyphicons-remove"></i>
                        <span class="sr-only">${closeLabel}</span>
                    </a>
                </c:if>
            </li>
        </c:forEach>
    </ul>
</nav>
