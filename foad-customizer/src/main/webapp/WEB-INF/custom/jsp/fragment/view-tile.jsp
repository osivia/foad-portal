<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false" %>


<div class="fragment-tile">
    <a href="${targetLink.url}" ${targetLink.external ? 'target="_blank"' : ''} class="panel panel-default">
        <span class="panel-body">
            <!-- Title -->
            <span class="title"><ttc:title document="${document}" linkable="false"/></span>
            
            <!-- Description -->
            <c:set var="description" value="${document.properties['dc:description']}" />
            <c:if test="${not empty description}">
                <span class="description">${description}</span>
            </c:if>
        </span>
    </a>
</div>
