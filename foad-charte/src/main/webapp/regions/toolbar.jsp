<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<div class="toolbar">
    <ul class="list-inline">
        <!-- Administration -->
        <li>
            <c:out value="${requestScope['osivia.toolbar.administrationContent']}" escapeXml="false" />
        </li>
        
        <!-- Search -->
        <li>
            <form action="${requestScope['osivia.search.url']}" class="form-inline" role="search">
                <input type="hidden" name="action" value="advancedSearch">
            
                <div class="form-group">
                    <label class="sr-only" for="search-input"><op:translate key="SEARCH" /></label>
                    <div class="input-group">
                        <input id="search-input" type="text" name="search" class="form-control">
                        <div class="input-group-btn">
                            <button type="submit" class="btn btn-link">
                                <i class="glyphicons glyphicons-search"></i>
                                <span class="sr-only"><op:translate key="SEARCH" /></span>
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </li>
        
        <!-- Tasks -->
        <c:if test="${not empty requestScope['osivia.toolbar.tasks.url']}">
            <c:set var="title"><op:translate key="NOTIFICATION_TASKS" /></c:set>
            <li>
                <button type="button" name="open-tasks" class="btn btn-link" data-target="#osivia-modal" data-load-url="${requestScope['osivia.toolbar.tasks.url']}" data-load-callback-function="tasksModalCallback" data-title="${title}" data-footer="true">
                    <i class="glyphicons glyphicons-bell"></i>
                    <span class="sr-only">${title}</span>
                    
                    <span class="counter small">
                        <c:choose>
                            <c:when test="${requestScope['osivia.toolbar.tasks.count'] gt 0}">
                                <span class="label label-danger">${requestScope['osivia.toolbar.tasks.count']}</span>
                            </c:when>
                            
                            <c:otherwise>
                                <span class="label label-default">0</span>
                            </c:otherwise>
                        </c:choose>
                    </span>
                </button>
            </li>
        </c:if>
        
        <c:set var="principal" value="${requestScope['osivia.toolbar.principal']}" />
        <c:set var="person" value="${requestScope['osivia.toolbar.person']}" />
        <c:choose>
            <c:when test="${empty principal}">
                <!-- Login -->
                <li>
                    <a href="${requestScope['osivia.toolbar.loginURL']}" class="btn btn-link">
                        <i class="halflings halflings-log-in"></i>
                        <span><op:translate key="LOGIN" /></span>
                    </a>
                </li>
            </c:when>

            <c:otherwise>
                <!-- User menu -->
                <li>
                    <div class="dropdown">
                        <button type="button" class="btn btn-link dropdown-toggle" data-toggle="dropdown">
                            <c:choose>
                                <c:when test="${empty person}">
                                    <i class="halflings halflings-user"></i>
                                    <span class="visible-lg-inline">${principal}</span>
                                </c:when>
                                
                                <c:otherwise>
                                    <img class="avatar" src="${person.avatar.url}" alt="">
                                    <span class="visible-lg-inline">${person.displayName}</span>
                                </c:otherwise>
                            </c:choose>
                            <span class="caret"></span>
                        </button>
                        
                        <ul class="dropdown-menu dropdown-menu-right" role="menu">
                            <li class="dropdown-header hidden-lg" role="presentation">
                                <c:choose>
                                    <c:when test="${empty person}">${principal}</c:when>
                                    <c:otherwise>${person.displayName}</c:otherwise>
                                </c:choose>
                            </li>
                        
                            <!-- User profile -->
                            <c:set var="userProfileUrl" value="${requestScope['osivia.toolbar.myprofile']}" />
                            <c:if test="${not empty userProfileUrl}">
                                <li role="presentation">
                                    <a href="${userProfileUrl}" role="menuitem">
                                        <i class="glyphicons glyphicons-nameplate"></i>
                                        <span><op:translate key="MY_PROFILE" /></span>
                                    </a>
                                </li>
                            </c:if>
                            
                            <!-- User settings -->
                            <c:set var="userSettingsUrl" value="${requestScope['osivia.toolbar.userSettings.url']}" />
                            <c:if test="${not empty userSettingsUrl}">
                                <li role="presentation">
                                    <a href="${userSettingsUrl}" role="menuitem">
                                        <i class="glyphicons glyphicons-cogwheel"></i>
                                        <span><op:translate key="USER_SETTINGS"/></span>
                                    </a>
                                </li>
                            </c:if>
    
                            <c:if test="${not empty userProfileUrl or not empty userSettingsUrl}">
                                <li class="divider" role="presentation"></li>
                            </c:if>
                            
                            <!-- Logout -->
                            <li role="presentation">
                                <a href="javascript:logout()" role="menuitem">
                                    <i class="halflings halflings-log-out"></i>
                                    <span><op:translate key="LOGOUT" /></span>
                                </a>
                            </li>
                        </ul>
                    </div>
                </li>
            </c:otherwise>
        </c:choose>
    </ul>
</div>


<!-- Disconnection modal -->
<div id="disconnection" class="modal fade" data-apps="${op:join(requestScope['osivia.sso.applications'], '|')}" data-redirection="${requestScope['osivia.toolbar.signOutURL']}">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <i class="glyphicons glyphicons-exit"></i>
                <span><op:translate key="LOGOUT_MESSAGE" /></span>
            </div>
        </div>
    </div>

    <div class="apps-container hidden"></div>
</div>
