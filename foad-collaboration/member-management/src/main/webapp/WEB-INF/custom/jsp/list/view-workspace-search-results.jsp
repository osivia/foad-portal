<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
<%@ taglib uri="https://tribu.phm.education.gouv.fr/jsp/taglib/foad" prefix="foad" %>

<%@ page isELIgnored="false" %>


<c:set var="namespace"><portlet:namespace /></c:set>


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
        <c:set var="workspaceType" value="${document.properties['workspaceType']}" />
        <c:set var="memberStatus" value="${document.properties['memberStatus']}" />
    
        <li>
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="media">
                        <!-- File Mime type -->
                        <c:choose>
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
                                        <span><ttc:title document="${document}" linkable="${workspaceType ne 'PRIVATE' or memberStatus.id eq 'member'}" openInSpaceTabs="true" /></span>
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
                            
                            <!-- Type & action -->
                            <c:if test="${not empty workspaceType or workspaceType.allowedInvitationRequests}">
                                <div class="actions">
                                    <c:if test="${not empty workspaceType}">
                                        <span class="label label-${workspaceType.color} workspace-type">
                                            <span><op:translate key="LIST_TEMPLATE_${workspaceType.key}" /></span>
                                        </span>
                                    </c:if>
                                    
                                    <c:choose>
                                        <c:when test="${not empty memberStatus}">
                                            <span class="label label-${memberStatus.color}">
                                                <span><op:translate key="${memberStatus.key}" /></span>
                                            </span>
                                        </c:when>
                                        
                                        <c:when test="${workspaceType.allowedInvitationRequests}">
                                            <portlet:actionURL name="createRequest" var="createRequestUrl">
                                                <portlet:param name="id" value="${document.properties['webc:url']}" />
                                            </portlet:actionURL>
                                        
                                            <button type="button" onclick="$JQry('#${namespace}-confirmation-form').attr('action', '${createRequestUrl}');" class="btn btn-tile" data-toggle="modal" data-target="#${namespace}-confirmation">
                                                <span><op:translate key="LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_CREATION" /></span>
                                            </button>
                                        </c:when>
                                    </c:choose>
                                </div>
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


<div id="${namespace}-confirmation" class="modal fade" tabindex="-1" role="dialog">
    <form id="${namespace}-confirmation-form" action="#" method="post">
    
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <i class="glyphicons glyphicons-remove"></i>
                        <span class="sr-only"><op:translate key="CLOSE" /></span>
                    </button>
    
                    <h4 class="modal-title"><op:translate key="LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_CONFIRMATION_TITLE" /></h4>
                </div>
            
                <div class="modal-body">
                    <p><op:translate key="LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_CONFIRMATION_MESSAGE_1" /></p>
    
                    <p class="form-group required">
                        <label class="control-label" for="userMessage"><op:translate key="LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_INPUT_MESSAGE" /></label>
                        
                        <textarea id="${namespace}-message" class="form-control" name="userMessage" rows="4" onkeyup="if($JQry('#${namespace}-message').val() != '') {$JQry('#${namespace}-confirmation-submit').removeAttr('disabled')} else {$JQry('#${namespace}-confirmation-submit').attr('disabled','disabled')}"></textarea>
                    </p>
                </div>
                
                <div class="modal-footer">
                    <button id="${namespace}-confirmation-submit"  type="submit" class="btn btn-primary" disabled="disabled">
                        <i class="glyphicons glyphicons-inbox-out"></i>
                        <span><op:translate key="LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_CONFIRM" /></span>
                    </button>
                    
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <span><op:translate key="CANCEL" /></span>
                    </button>
                </div>
            </div>
        </div>
    </form>
</div>
