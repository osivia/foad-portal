<!DOCTYPE html>
<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>

<head>
    <jsp:include page="../includes/head.jsp" />
</head>


<body>
    <!-- Condensed toolbar -->
    <p:region regionName="condensed-toolbar" />

    <!-- Tabs -->
    <div class="tabs-container hidden-xs">
        <div class="container-fluid">
            <p:region regionName="tabs" />
        </div>
    </div>

    <div class="wrapper-outer">
        <div class="wrapper-inner">
            <main id="page-content" class="container-fluid">
                <!-- Content navbar -->
                <jsp:include page="../includes/content-navbar.jsp" />
                
                <div class="row">
                    <!-- Drawer -->
                    <div id="drawer" class="taskbar-container taskbar-affix">
                        <p:region regionName="drawer-toolbar" />
                        
                        <div class="col-auto">
                            <div class="row">
                                <div class="col-auto">
                                    <p:region regionName="col-1" />
                                </div>
                                
                                <div
                                    <c:choose>
                                        <c:when test="${requestScope['osivia.panels.navigation-panel.closed']}">class="hidden"</c:when>
                                        <c:otherwise>class="col-auto"</c:otherwise>
                                    </c:choose>
                                >
                                    <p:region regionName="navigation-panel" />
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div>
                        <div class="col-sm-12">
                            <!-- Notifications -->
                            <p:region regionName="notifications" />
                           
                            <!-- Workspace header -->
                            <div class="workspace-header">
                                <p:region regionName="workspace-header-1" />
                                <p:region regionName="workspace-header-2" />
                            </div>
                            
                            <p:region regionName="cols-top" />
                            
                            <div class="row">
                                <div class="col-sm-6">
                                    <p:region regionName="col-2" />
                                </div>
                                
                                <div class="col-sm-6">
                                    <p:region regionName="col-3" />
                                </div>
                            </div>
                            
                            <p:region regionName="cols-bottom" />
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>
    
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
