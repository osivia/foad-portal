<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<div class="taskbar">
    <!-- Folders -->
    <ul class="folders">
        <c:forEach var="folder" items="${taskbar.folders}">
            <li ${folder.active ? 'class="active"' : ''}>
                <a href="${folder.url}" class="no-ajax-link">
                    <span>${folder.displayName}</span>
                </a>
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
