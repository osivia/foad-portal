<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<c:if test="${not empty requestScope['osivia.toolbar.tasks.url']}">
    <c:set var="title"><op:translate key="NOTIFICATION_TASKS" /></c:set>
    <li>
        <button type="button" name="open-tasks" class="btn btn-link" data-tasks-count="${requestScope['osivia.toolbar.tasks.count']}" data-target="#osivia-modal" data-load-url="${requestScope['osivia.toolbar.tasks.url']}" data-load-callback-function="tasksModalCallback" data-title="${title}" data-footer="true">
            <i class="glyphicons glyphicons-bell"></i>
            <span class="sr-only">${title}</span>
        </button>
    </li>
</c:if>
