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
    
    <!-- Home -->
    <c:if test="${not empty userPortal.defaultPage}">
        <div class="home">
            <ul>
                <li class="${userPortal.defaultPage.id eq currentId ? 'active' : ''}">
                    <a href="${userPortal.defaultPage.url}" title="${userPortal.defaultPage.name}" data-toggle="tooltip" data-placement="bottom">
                        <i class="halflings halflings-home"></i>
                        <span class="sr-only">${userPortal.defaultPage.name}</span>
                    </a>
                </li>
            </ul>
        </div>
    </c:if>
    
    <div>
        <ul>
            <c:forEach var="userPage" items="${userPages}">
                <c:if test="${not userPage.defaultPage}">
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
                </c:if>
            </c:forEach>
        </ul>
    </div>
</nav>
