<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
<%@ taglib uri="https://tribu.phm.education.gouv.fr/jsp/taglib/foad" prefix="foad" %>


<c:set var="checkboxTitle"><op:translate key="TABLE_CHECKBOX_TITLE" /></c:set>


<div class="file-browser-table-container">
    <div class="file-browser-filler">
        <!-- Table -->
        <div class="file-browser-table">
            <!-- Column group -->
            <div class="file-browser-table-column-group">
                <div class="file-browser-table-column" data-column="title"></div>
                <div class="file-browser-table-column" data-column="last-modification"></div>
                <div class="file-browser-table-column" data-column="file-size"></div>
                <div class="file-browser-table-column" data-column="checkbox"></div>
            </div>
            
            <!-- Header -->
            <div class="file-browser-table-header-group">
                <!-- Title -->
                <div class="file-browser-table-cell" data-column="title">
                    <div class="file-browser-cell" data-column="title">
                        <div class="file-browser-text">
                            <portlet:actionURL name="sort" copyCurrentRenderParameters="true" var="url">
                                <portlet:param name="sort" value="title" />
                                <portlet:param name="alt" value="${form.criteria.sort.id eq 'title' and not form.criteria.alt}" />
                            </portlet:actionURL>
                        
                            <a href="${url}">
                                <span><op:translate key="FILE_BROWSER_SORT_TITLE" /></span>
                                <c:if test="${form.criteria.sort.id eq 'title'}">
                                    <small>
                                        <i class="glyphicons glyphicons-arrow-${form.criteria.alt ? 'up' : 'down'}"></i>
                                    </small>
                                </c:if>
                            </a>
                        </div>
                    </div>
                </div>
                
                <!-- Last modification -->
                <div class="file-browser-table-cell" data-column="last-modification">
                    <div class="file-browser-cell" data-column="last-modification">
                        <div class="file-browser-text">
                            <portlet:actionURL name="sort" copyCurrentRenderParameters="true" var="url">
                                <portlet:param name="sort" value="last-modification" />
                                <portlet:param name="alt" value="${form.criteria.sort.id eq 'last-modification' and not form.criteria.alt}" />
                            </portlet:actionURL>
                        
                            <a href="${url}">
                                <span><op:translate key="FILE_BROWSER_SORT_LAST_MODIFICATION" /></span>
                                <c:if test="${form.criteria.sort.id eq 'last-modification'}">
                                    <small>
                                        <i class="glyphicons glyphicons-arrow-${form.criteria.alt ? 'up' : 'down'}"></i>
                                    </small>
                                </c:if>
                            </a>
                        </div>
                    </div>
                </div>
                
                <!-- File size -->
                <div class="file-browser-table-cell" data-column="file-size">
                    <div class="file-browser-cell" data-column="file-size">
                        <div class="file-browser-text">
                            <portlet:actionURL name="sort" copyCurrentRenderParameters="true" var="url">
                                <portlet:param name="sort" value="file-size" />
                                <portlet:param name="alt" value="${form.criteria.sort.id eq 'file-size' and not form.criteria.alt}" />
                            </portlet:actionURL>
                        
                            <a href="${url}">
                                <span><op:translate key="FILE_BROWSER_SORT_FILE_SIZE" /></span>
                                <c:if test="${form.criteria.sort.id eq 'file-size'}">
                                    <small>
                                        <i class="glyphicons glyphicons-arrow-${form.criteria.alt ? 'up' : 'down'}"></i>
                                    </small>
                                </c:if>
                            </a>
                        </div>
                    </div>
                </div>
                
                <!-- Checkbox -->
                <div class="file-browser-table-cell" data-column="checkbox">
                    <div class="file-browser-cell" data-column="checkbox">
                        <div class="file-browser-checkbox">
                            <c:set var="title"><op:translate key="TABLE_SELECT_ALL_CHECKBOX_TITLE" /></c:set>
                            <a href="javascript:;" class="no-ajax-link" title="${title}">
                                <i class="glyphicons glyphicons-unchecked"></i>
                            </a>
                        </div>
                    </div>
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
                    <div class="file-browser-table-row-group file-browser-selectable">
                        <c:forEach var="item" items="${form.items}">
                            <div class="file-browser-table-row file-browser-selectee ${item.folderish ? 'file-browser-droppable' : ''}" data-id="${item.document.id}" data-type="${item.document.type.name}" data-text="${item.title}" data-accepted-types="${item.acceptedTypes}">
                                <!-- Title -->
                                <div class="file-browser-table-cell" data-column="title">
                                    <div class="file-browser-cell">
                                        <c:choose>
                                            <c:when test="${item.document.type.file}">
                                                <foad:mimeTypeIcon mimeType="${item.mimeType}" />
                                            </c:when>
                                            
                                            <c:when test="${not empty item.document.type.glyph}">
                                                <span class="document-type" data-length="1" data-folderish="${item.folderish}">
                                                    <i class="${item.document.type.glyph}"></i>
                                                </span>
                                            </c:when>
                                        </c:choose>
                                    
                                        <div class="file-browser-text file-browser-draggable">
                                            <span><ttc:title document="${item.document}" /></span>
                                        </div>
                                        
                                        <c:if test="${not empty item.lock || item.subscription}">
                                            <div>
                                                <c:if test="${not empty item.lock}">
                                                    <div>
                                                        <i class="${item.lock}"></i>
                                                    </div>
                                                </c:if>
                                            
                                                <c:if test="${item.subscription}">
                                                    <div>
                                                        <i class="glyphicons glyphicons-flag"></i>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:if>
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
                                
                                <!-- Checkbox -->
                                <div class="file-browser-table-cell" data-column="checkbox">
                                    <div class="file-browser-cell">
                                        <div class="file-browser-checkbox">
                                            <a href="javascript:;" class="no-ajax-link" title="${checkboxTitle}">
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
