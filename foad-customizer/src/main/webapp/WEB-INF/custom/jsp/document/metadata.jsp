<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false"%>


<div class="metadata">
    <dl>
        <!-- Creation -->
        <c:if test="${not empty document.properties['dc:created']}">
            <dt><op:translate key="DOCUMENT_METADATA_CREATION" /></dt>
            <dd>
                <p>
                    <span><fmt:formatDate value="${document.properties['dc:created']}" type="date" dateStyle="long" /></span>
                    
                    <c:if test="${not empty document.properties['dc:creator']}">
                        <br>
                        <span><op:translate key="DOCUMENT_METADATA_BY" /></span>
                        <span><ttc:user name="${document.properties['dc:creator']}"/></span>
                    </c:if>
                </p>
            </dd>
        </c:if>
        
        <!-- Modification -->
        <c:if test="${not empty document.properties['dc:modified']}">
            <dt><op:translate key="DOCUMENT_METADATA_MODIFICATION" /></dt>
            <dd>
                <p>
                    <span><fmt:formatDate value="${document.properties['dc:modified']}" type="date" dateStyle="long" /></span>
                    
                    <c:if test="${not empty document.properties['dc:lastContributor']}">
                        <br>
                        <span><op:translate key="DOCUMENT_METADATA_BY" /></span>
                        <span><ttc:user name="${document.properties['dc:lastContributor']}"/></span>
                    </c:if>
                </p>
            </dd>
        </c:if>

        <!-- Publication -->
        <c:if test="${not empty document.properties['ttc:publicationDate']}">
            <dt><op:translate key="DOCUMENT_METADATA_PUBLICATION" /></dt>
            <dd>
                <p>
                    <span><fmt:formatDate value="${document.properties['ttc:publicationDate']}" type="date" dateStyle="long" /></span>
                </p>
            </dd>
        </c:if>
    
    
        <!-- Custom metadata -->
        <ttc:include page="metadata-custom.jsp" />
                    
        <!-- Remote publication spaces -->
        <ttc:include page="metadata-remote-sections.jsp" />
    </dl>
</div>
