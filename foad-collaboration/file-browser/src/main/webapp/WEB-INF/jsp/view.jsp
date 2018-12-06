<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<div class="file-browser">
    <div class="panel panel-default">
        <div class="panel-body">
            <!-- Toolbar -->
            <div class="file-browser-toolbar">
                <span>Contenu de la toolbar...</span>
            </div>
            
        
            <div class="file-browser-table-container">
                <div class="portlet-filler">
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
                                    <span><op:translate key="FILE_BROWSER_HEADER_TITLE" /></span>
                                </div>
                            </div>
                            
                            <!-- Last modification -->
                            <div class="file-browser-table-cell" data-column="last-modification">
                                <div class="file-browser-cell" data-column="last-modification">
                                    <span><op:translate key="FILE_BROWSER_HEADER_LAST_MODIFICATION" /></span>
                                </div>
                            </div>
                            
                            <!-- File size -->
                            <div class="file-browser-table-cell" data-column="file-size">
                                <div class="file-browser-cell" data-column="file-size">
                                    <span><op:translate key="FILE_BROWSER_HEADER_FILE_SIZE" /></span>
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
                        
                        <!-- Row group -->
                        <div class="file-browser-table-row-group">
                            <c:forEach var="item" items="${form.items}">
                                <div class="file-browser-table-row">
                                    <!-- Title -->
                                    <div class="file-browser-table-cell">
                                        <div class="file-browser-cell">
                                            <span>${item.title}</span>
                                        </div>
                                    </div>
                                    
                                    <!-- Last modification -->
                                    <div class="file-browser-table-cell">
                                        <div class="file-browser-cell">
                                            <span>14 juin 2018 - Nicolas SCHWEYER</span>
                                        </div>
                                    </div>
                                    
                                    <!-- File size -->
                                    <div class="file-browser-table-cell">
                                        <div class="file-browser-cell">
                                            <span>000 ko</span>
                                        </div>
                                    </div>
                                    
                                    <!-- Document type -->
                                    <div class="file-browser-table-cell">
                                        <div class="file-browser-cell">
                                            <span>DOC</span>
                                        </div>
                                    </div>
                                    
                                    <!-- Checkbox -->
                                    <div class="file-browser-table-cell">
                                        <div class="file-browser-cell">
                                            <a href="javascript:;" class="no-ajax-link">
                                                <i class="glyphicons glyphicons-unchecked"></i>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
