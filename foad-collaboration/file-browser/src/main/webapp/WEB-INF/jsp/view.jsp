<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
<%@ taglib uri="https://tribu.phm.education.gouv.fr/jsp/taglib/foad" prefix="foad" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:actionURL name="drop" var="dropUrl" />

<portlet:resourceURL id="toolbar" var="toolbarUrl" />


<div class="file-browser" data-drop-url="${dropUrl}" data-toolbar-url="${toolbarUrl}">
    <div class="panel panel-default">
        <div class="panel-body">
            <div class="clearfix">
                <!-- View -->
                <div class="pull-left">
                    <a href="#" class="btn btn-default btn-sm">
                        <i class="glyphicons glyphicons-show-big-thumbnails"></i>
                    </a>
                </div>
            
                <!-- Toolbar -->
                <div class="file-browser-toolbar pull-right">
                    <div></div>
                </div>
            </div>
        
            <div class="file-browser-table-container">
                <div class="file-browser-filler">
                    <!-- Table -->
                    <div class="file-browser-table">
                        <!-- Column group -->
                        <div class="file-browser-table-column-group">
                            <div class="file-browser-table-column" data-column="title"></div>
                            <div class="file-browser-table-column" data-column="last-modification"></div>
                            <div class="file-browser-table-column" data-column="file-size"></div>
                            <div class="file-browser-table-column" data-column="document-type"></div>
                            <div class="file-browser-table-column" data-column="checkbox"></div>
                        </div>
                        
                        <!-- Header -->
                        <div class="file-browser-table-header-group">
                            <!-- Title -->
                            <div class="file-browser-table-cell" data-column="title">
                                <div class="file-browser-cell" data-column="title">
                                    <div class="file-browser-text">
                                        <portlet:actionURL name="sort" var="url">
                                            <portlet:param name="sort" value="title" />
                                            <portlet:param name="alt" value="${form.criteria.sort.id eq 'title' and not form.criteria.alt}" />
                                        </portlet:actionURL>
                                    
                                        <a href="${url}">
                                            <span><op:translate key="FILE_BROWSER_HEADER_TITLE" /></span>
                                            <c:if test="${form.criteria.sort.id eq 'title'}">
                                                <i class="glyphicons glyphicons-arrow-${form.criteria.alt ? 'up' : 'down'}"></i>
                                            </c:if>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Last modification -->
                            <div class="file-browser-table-cell" data-column="last-modification">
                                <div class="file-browser-cell" data-column="last-modification">
                                    <div class="file-browser-text">
                                        <portlet:actionURL name="sort" var="url">
                                            <portlet:param name="sort" value="last-modification" />
                                            <portlet:param name="alt" value="${form.criteria.sort.id eq 'last-modification' and not form.criteria.alt}" />
                                        </portlet:actionURL>
                                    
                                        <a href="${url}">
                                            <span><op:translate key="FILE_BROWSER_HEADER_LAST_MODIFICATION" /></span>
                                            <c:if test="${form.criteria.sort.id eq 'last-modification'}">
                                                <i class="glyphicons glyphicons-arrow-${form.criteria.alt ? 'up' : 'down'}"></i>
                                            </c:if>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- File size -->
                            <div class="file-browser-table-cell" data-column="file-size">
                                <div class="file-browser-cell" data-column="file-size">
                                    <div class="file-browser-text">
                                        <span><op:translate key="FILE_BROWSER_HEADER_FILE_SIZE" /></span>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Document type -->
                            <div class="file-browser-table-cell" data-column="document-type">
                                <div class="file-browser-cell" data-column="document-type">
                                    <span><op:translate key="FILE_BROWSER_HEADER_DOCUMENT_TYPE" /></span>
                                </div>
                            </div>
                            
                            <!-- Checkbox -->
                            <div class="file-browser-table-cell" data-column="checkbox">
                                <div class="file-browser-cell" data-column="checkbox"></div>
                            </div>
                        </div>
                        
                        <c:choose>
                            <c:when test="${empty form.items}">
                                <div class="file-browser-table-row file-browser-empty">
                                    <div class="file-browser-table-cell">
                                        <div class="file-browser-cell">
                                            <span class="text-muted"><op:translate key="FILE_BROWSER_EMPTY" /></span>
                                        </div>
                                    </div>
                                </div>
                            </c:when>
                            
                            <c:otherwise>
                                <!-- Row group -->
                                <div class="file-browser-table-row-group">
                                    <c:forEach var="item" items="${form.items}">
                                        <div class="file-browser-table-row ${item.folderish ? 'file-browser-droppable' : ''}" data-id="${item.document.id}" data-type="${item.document.type.name}" data-text="${item.title}" data-accepted-types="${item.acceptedTypes}">
                                            <!-- Title -->
                                            <div class="file-browser-table-cell" data-column="title">
                                                <div class="file-browser-cell">
                                                    <div class="file-browser-text file-browser-draggable">
                                                        <span><ttc:title document="${item.document}" /></span>
                                                    </div>
                                                </div>
                                            </div>
                                            
                                            <!-- Last modification -->
                                            <div class="file-browser-table-cell" data-column="last-modification">
                                                <div class="file-browser-cell">
                                                    <div class="file-browser-text">
                                                        <span><fmt:formatDate value="${item.lastModification}" type="date" dateStyle="long" /></span>
                                                        <span class="visible-lg-inline">
                                                            <span>-</span>
                                                            <span><ttc:user name="${item.lastContributor}" linkable="false" /></span>
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                            
                                            <!-- File size -->
                                            <div class="file-browser-table-cell" data-column="file-size">
                                                <div class="file-browser-cell">
                                                    <div class="file-browser-text">
                                                        <c:choose>
                                                            <c:when test="${item.size gt 0}">
                                                                <span><ttc:fileSize size="${item.size}" /></span>
                                                            </c:when>
                                                            
                                                            <c:otherwise>
                                                                <span>&ndash;</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </div>
                                            </div>
                                            
                                            <!-- Document type -->
                                            <div class="file-browser-table-cell" data-column="document-type">
                                                <div class="file-browser-cell">
                                                    <c:choose>
                                                        <c:when test="${item.document.type.file}">
                                                            <foad:mimeTypeIcon mimeType="${item.document.properties['file:content']['mime-type']}" />
                                                        </c:when>
                                                        
                                                        <c:when test="${item.document.type.name eq 'Note'}">
                                                            <span class="document-type document-type-note" data-display="note" data-length="4"></span>
                                                        </c:when>
                                                        
                                                        <c:when test="${not empty item.document.type.glyph}">
                                                            <span class="document-type" data-length="1" data-folderish="${item.folderish}">
                                                                <i class="${item.document.type.glyph}"></i>
                                                            </span>
                                                        </c:when>
                                                    </c:choose>
                                                </div>
                                            </div>
                                            
                                            <!-- Checkbox -->
                                            <div class="file-browser-table-cell" data-column="checkbox">
                                                <div class="file-browser-cell">
                                                    <div class="file-browser-checkbox">
                                                        <a href="javascript:;" class="no-ajax-link">
                                                            <i class="glyphicons glyphicons-unchecked"></i>
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                            
                                            <!-- Draggable -->
                                            <c:if test="${item.document.type.supportsPortalForms}">
                                                <div class="file-browser-draggable file-browser-draggable-shadowbox border-primary"></div>
                                            </c:if>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
