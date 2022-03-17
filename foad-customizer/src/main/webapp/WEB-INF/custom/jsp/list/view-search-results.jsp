<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
<%@ taglib uri="https://tribu.phm.education.gouv.fr/jsp/taglib/foad" prefix="foad" %>

<%@ page isELIgnored="false" %>


<p class="text-muted">
    <c:choose>
        <c:when test="${totalSize eq 0}">
            <span><op:translate key="LIST_TEMPLATE_SEARCH_RESULTS_NO_RESULT" /></span>
        </c:when>
        
        <c:when test="${totalSize eq 1}">
            <span><op:translate key="LIST_TEMPLATE_SEARCH_RESULTS_ONE_RESULT" /></span>
        </c:when>
        
        <c:otherwise>
            <span><op:translate key="LIST_TEMPLATE_SEARCH_RESULTS_MULTIPLE_RESULTS" args="${totalSize}" /></span>
        </c:otherwise>
    </c:choose>
</p>


<ol class="list-unstyled tiles">
    <c:forEach var="document" items="${documents}" varStatus="status">
        <!-- Document properties -->
		<c:choose>
			<c:when test="${document.type.name eq 'UserProfile'}">
				<c:set var="vignetteUrl"><ttc:pictureLink document="${document}" property="userprofile:avatar" /></c:set>
			</c:when>
            
			<c:otherwise>
				<c:set var="vignetteUrl"><ttc:pictureLink document="${document}" property="ttc:vignette" /></c:set>
			</c:otherwise>
		</c:choose>
		<c:set var="description" value="${document.properties['dc:description']}" />
        <c:set var="author" value="${document.properties['dc:lastContributor']}" />
        <c:set var="date" value="${empty document.properties['dc:modified'] ? document.properties['dc:created'] : document.properties['dc:modified']}" />
    
        <li>
        	<div class="panel panel-default">
        		<div class="panel-body">
                    <div class="media">
                        <!-- File Mime type -->
                        <c:choose>
                            <c:when test="${document.type.name eq 'DocxfFile'}">
                                <div class="media-left">
                                    <foad:mimeTypeIcon mimeType="application/onlyoffice-docxf" />
                                </div>
                            </c:when>
                            <c:when test="${document.type.name eq 'OformFile'}">
                                <div class="media-left">
                                    <foad:mimeTypeIcon mimeType="application/onlyoffice-oform" />
                                </div>
                            </c:when>                        
                        
                            <c:when test="${document.type.file}">
                                <div class="media-left">
                                    <foad:mimeTypeIcon mimeType="${document.properties['file:content']['mime-type']}" />
                                </div>
                            </c:when>
                            
                            <c:when test="${not empty document.type.glyph}">
                                <div class="media-left">
                                    <span class="document-type" data-length="1" data-glyphicons data-type="${document.type.name}">
                                        <i class="${document.type.glyph}"></i>
                                    </span>
                                </div>
                            </c:when>
                        </c:choose>
                    
                        <div class="media-body">
                            <h3>
                                <!-- Title -->
                                <c:choose>
                                    <c:when test="${document.type.name eq 'UserProfile'}">
                                        <span><ttc:user name="${document.properties['ttc_userprofile:login']}" linkable="true" showAvatar="false" /></span>
                                    </c:when>
                                    
                                    <c:otherwise>
                                        <span><ttc:title document="${document}" openInSpaceTabs="true"/></span>
                                    </c:otherwise>
                                </c:choose>
                            </h3>
                            
                            <c:if test="${not (document.type.rootType or document.type.name eq 'UserProfile')}">
                                <!-- Space -->
                                <p class="small text-success">
                                    <i class="${document.spaceIcon}"></i>
                                    <span>${document.properties['ttc:spaceTitle']}</span>
                                </p>
                            </c:if>
                            
                            <!-- Description -->
                            <c:if test="${not empty description}">
                                <p class="text-pre-wrap">${description}</p>
                            </c:if>
                
                            <!-- Last edition informations -->
                            <c:if test="${not (document.type.rootType or document.type.name eq 'UserProfile')}">
                                <p class="small text-muted">
                                    <span><op:translate key="DOCUMENT_METADATA_MODIFIED_ON" /></span>
                                    <span><op:formatRelativeDate value="${date}" /></span>
                                    <c:if test="${not empty author}">
                                        <span><op:translate key="DOCUMENT_METADATA_BY" /></span>
                                        <span><ttc:user name="${author}" linkable="false" /></span>
                                    </c:if>
                                </p>
                            </c:if>
                        </div>
                        
                        <!-- Vignette -->
                        <c:if test="${not empty vignetteUrl}">
                            <div class="media-right">
                                <img src="${vignetteUrl}" alt="" class="media-object ${document.type.name eq 'UserProfile' ? 'avatar' : ''}">
                            </div>
                        </c:if>
                    </div>
	            </div>
            </div>
        </li>
    </c:forEach>
</ol>
