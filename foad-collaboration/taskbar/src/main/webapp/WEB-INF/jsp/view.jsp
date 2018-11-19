<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:actionURL name="drop" var="dropUrl" />

<portlet:resourceURL id="lazy-loading" var="lazyLoadingUrl" />


<div class="taskbar" data-drop-url="${dropUrl}" data-lazy-loading-url="${lazyLoadingUrl}">
    <!-- Folders -->
    <ul class="folders">
        <c:forEach var="folder" items="${taskbar.folders}">
            <li ${folder.active ? 'class="active"' : ''}>
                <a href="${folder.url}" class="no-ajax-link">
                    <span>${folder.displayName}</span>
                </a>
                
                <!-- Children -->
                <c:if test="${folder.active}">
                    <div class="fancytree">
                        <c:set var="parent" value="${folder}" scope="request" />
                        <ttc:include page="folder-children.jsp" />
                    </div>
                </c:if>
            </li>
        </c:forEach>
    </ul>
    
    <!-- Services -->
    <ul class="services">
        <c:forEach var="service" items="${taskbar.services}">
            <li ${service.active ? 'class="active"' : ''}>
                <a href="${service.url}" class="no-ajax-link" data-type="${service.type}">
                    <i class="${service.icon}"></i>
                    <span>${service.displayName}</span>
                </a>
            </li>
        </c:forEach>
    </ul>
</div>
