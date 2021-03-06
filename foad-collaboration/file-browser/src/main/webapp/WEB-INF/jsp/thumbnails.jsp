<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
<%@ taglib uri="https://tribu.phm.education.gouv.fr/jsp/taglib/foad" prefix="foad" %>


<div class="file-browser-thumbnails-container file-browser-selectable">
    <div class="file-browser-filler">
        <div class="file-browser-folders">
            <div class="clearfix">
                <div class="btn-group btn-group-sm pull-right">
                    <div class="btn-group btn-group-sm">
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                            <span><op:translate key="${form.criteria.sort.key}" /></span>
                            <span class="caret"></span>
                        </button>
                        
                        <ul class="dropdown-menu dropdown-menu-right">
                            <c:forEach var="sort" items="${sorts}">
                                <portlet:actionURL name="sort" copyCurrentRenderParameters="true" var="url">
                                    <portlet:param name="sort" value="${sort.id}" />
                                    <portlet:param name="alt" value="${form.criteria.sort.id eq sort.id and not form.criteria.alt}" />
                                </portlet:actionURL>
                            
                                <li ${form.criteria.sort eq sort ? 'class="active"' : ''}>
                                    <a href="${url}">
                                        <span><op:translate key="${sort.key}" /></span>
                                    </a>
                                </li>                            
                            </c:forEach>
                        </ul>
                    </div>

                    <portlet:actionURL name="sort" copyCurrentRenderParameters="true" var="url">
                        <portlet:param name="sort" value="${form.criteria.sort.id}" />
                        <portlet:param name="alt" value="${not form.criteria.alt}" />
                    </portlet:actionURL>
                                
                    <a href="${url}" class="btn btn-default">
                        <i class="glyphicons glyphicons-arrow-${form.criteria.alt ? 'up' : 'down'}"></i>
                    </a>
                </div>
            
                <h3 class="h4"><op:translate key="FILE_BROWSER_FOLDERS" /></h3>
            </div>
            
            <c:set var="count" value="0" />
            
            <div class="row">
                <c:forEach var="item" items="${form.items}">
                    <c:if test="${item.folderish}">
                        <c:set var="count" value="${count + 1}" />
                        
                        <div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
                            <div class="file-browser-thumbnail file-browser-selectee file-browser-droppable" data-id="${item.document.id}" data-type="${item.document.type.name}" data-text="${item.title}" data-accepted-types="${item.acceptedTypes}">
                                <!-- Title -->
                                <div class="file-browser-thumbnail-title">
                                    <div class="file-browser-text file-browser-draggable">
                                        <i class="${item.document.type.glyph}"></i>
                                        <span><ttc:title document="${item.document}" /></span>
                                    </div>
                                </div>
                                
                                <!-- Draggable -->
                                <c:if test="${item.document.type.supportsPortalForms}">
                                    <div class="file-browser-draggable file-browser-draggable-shadowbox border-primary"></div>
                                </c:if>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
            
            <c:if test="${count eq 0}">
                <div class="file-browser-cell">
                    <span class="text-muted"><op:translate key="FILE_BROWSER_EMPTY" /></span>
                </div>
            </c:if>
        </div>
        
        <div class="file-browser-files">
            <h3 class="h4"><op:translate key="FILE_BROWSER_FILES" /></h3>
    
            <c:set var="count" value="0" />
            
            <div class="row">
                <c:forEach var="item" items="${form.items}">
                    <c:if test="${not item.folderish}">
                        <c:set var="count" value="${count + 1}" />
                        
                        <div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
                            <div class="file-browser-thumbnail file-browser-selectee" data-id="${item.document.id}" data-type="${item.document.type.name}" data-text="${item.title}">
                                <!-- Preview -->
                                <div class="file-browser-thumbnail-preview-container">
                                    <div class="file-browser-thumbnail-preview">
                                        <c:set var="vignetteUrl"><ttc:pictureLink document="${item.document}" property="ttc:vignette" /></c:set>
                                        <c:choose>
                                            <c:when test="${not empty vignetteUrl}">
                                                <img src="${vignetteUrl}" alt="" class="vignette">
                                            </c:when>
                                        
                                            <c:when test="${item.document.type.name eq 'Picture'}">
                                                <c:set var="url"><ttc:documentLink document="${item.document}" picture="true" displayContext="Small" /></c:set>
                                                <img src="${url}" alt="" class="picture">
                                            </c:when>
                                        
                                            <c:otherwise>
                                                <span class="file-browser-thumbnail-empty-preview">
                                                    <i class="glyphicons glyphicons-file"></i>
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                
                                <!-- Title -->
                                <div class="file-browser-thumbnail-title">
                                    <div class="file-browser-text file-browser-draggable">
                                        <c:choose>
                                            <c:when test="${item.document.type.file}">
                                                <foad:mimeTypeIcon mimeType="${item.mimeType}" />
                                            </c:when>
                                            
                                            <c:when test="${not empty item.document.type.glyph}">
                                                <span class="document-type" data-length="1" data-glyphicons>
                                                    <i class="${item.document.type.glyph}"></i>
                                                </span>
                                            </c:when>
                                        </c:choose>
                                    
                                        <span><ttc:title document="${item.document}" /></span>
                                    </div>
                                </div>
                                
                                <!-- Draggable -->
                                <c:if test="${item.document.type.supportsPortalForms}">
                                    <div class="file-browser-draggable file-browser-draggable-shadowbox border-primary"></div>
                                </c:if>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
            
            <c:if test="${count eq 0}">
                <div class="file-browser-cell">
                    <span class="text-muted"><op:translate key="FILE_BROWSER_EMPTY" /></span>
                </div>
            </c:if>
        </div>
    </div>
</div>
