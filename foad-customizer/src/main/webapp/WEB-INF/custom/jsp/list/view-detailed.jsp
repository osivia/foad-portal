<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
<%@ taglib uri="https://tribu.phm.education.gouv.fr/jsp/taglib/foad" prefix="foad" %>

<%@ page isELIgnored="false" %>


<ul class="list-unstyled tiles">
    <c:forEach var="document" items="${documents}">
        <li>
            <div class="panel panel-default">
                <div class="panel-body">
                    <!-- File Mime type -->
                    <c:if test="${document.type.file}">
                        <div class="mime-type-container">
                            <foad:mimeTypeIcon mimeType="${document.properties['file:content']['mime-type']}" />
                        </div>
                    </c:if>
                
                    <div>
                        <!-- Title -->
                        <h3 class="text-primary">
                            <span><ttc:title document="${document}" /></span>
                        </h3>
                        
                        <!-- Last edition informations -->
                        <c:if test="${not document.type.rootType and not (document.type.name eq 'Room')}">
                            <p>
                                <span class="date"><fmt:formatDate value="${document.properties['dc:modified']}" type="date" dateStyle="long" /></span>
                                <br>
                                <span><ttc:user name="${document.properties['dc:lastContributor']}" linkable="false" /></span>
                            </p>
                        </c:if>
                        
                        <!-- Description -->
                        <c:set var="description" value="${document.properties['dc:description']}" />
                        <c:if test="${not empty description}">
                            <p class="text-pre-wrap">${description}</p>
                        </c:if>
                    </div>
                </div>
            </div>
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
