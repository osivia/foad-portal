<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false" %>


<div class="panel panel-default">
    <div class="panel-body">
        <!-- Title -->
        <h3>
            <span><ttc:title document="${document}" /></span>
        </h3>
        
        <!-- Description -->
        <c:set var="description" value="${document.properties['dc:description']}" />
        <c:if test="${not empty description}">
            <p class="text-pre-wrap">${description}</p>
        </c:if>
        
        <!-- Type -->
        <c:set var="workspaceType" value="${document.properties['workspaceType']}" />
        <c:if test="${not empty workspaceType}">
            <p>
                <span class="label label-${workspaceType.color}">
                    <span><op:translate key="LIST_TEMPLATE_${workspaceType.key}" /></span>
                </span>
            </p>
        </c:if>
    </div>
</div>
