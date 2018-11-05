<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false" %>


<ul class="list-unstyled tiles">
    <c:forEach var="document" items="${documents}">
        <li>
            <c:set var="document" value="${document}" scope="request"></c:set>
            <ttc:include page="workspace.jsp" />
        </li>
    </c:forEach>
    
    
    <c:if test="${empty documents}">
        <li>
            <p>
                <span class="text-muted"><op:translate key="LIST_NO_ITEMS" /></span>
            </p>
        </li>
    </c:if>
</ul>
