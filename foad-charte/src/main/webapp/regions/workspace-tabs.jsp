<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<ul class="workspace-tabs">
    <c:forEach var="tab" items="${requestScope['workspace.tabs']}">
        <li class="${tab.active ? 'active' : ''} ${tab.home ? 'home' : ''}">
            <a href="${tab.url}" title="${tab.title}">
                <c:choose>
                    <c:when test="${tab.home}">
                        <i class="glyphicons glyphicons-home"></i>
                        <span class="sr-only"><op:translate key="HOME" /></span>
                    </c:when>
                    
                    <c:otherwise>
                        <span>${tab.title}</span>
                    </c:otherwise>
                </c:choose>
            </a>                
        </li>
    </c:forEach>
</ul>
