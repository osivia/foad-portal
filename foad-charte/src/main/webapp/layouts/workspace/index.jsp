<!DOCTYPE html>
<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>

<head>
    <jsp:include page="../includes/head.jsp" />
</head>


<body class="fixed-layout condensed-layout">
    <!-- Condensed toolbar -->
    <p:region regionName="condensed-toolbar" />

    <!-- Tabs -->
    <div class="tabs-container hidden-xs">
        <div class="container-fluid">
            <p:region regionName="tabs" />
        </div>
    </div>

    <main>
        <div class="container-fluid flexbox">
            <!-- Content navbar -->
            <jsp:include page="../includes/content-navbar.jsp" />
                
            <div class="row flexbox">
                <!-- Drawer -->
                <div id="drawer" class="col-auto flexbox">
                    <div class="row">
                        <p:region regionName="drawer-toolbar" />
                    </div>
                        
                    <div class="scrollbox">
                        <p:region regionName="col-1" />
                    </div>
                        
                    <div
                        <c:choose>
                            <c:when test="${requestScope['osivia.panels.navigation-panel.closed']}">class="hidden"</c:when>
                            <c:otherwise>class="col-auto flexbox hidden-sm"</c:otherwise>
                        </c:choose>
                    >
                        <div class="scrollbox">
                            <p:region regionName="navigation-panel" />
                        </div>
                    </div>
                </div>
                
                <div class="col-offset-auto col-auto flexbox">
                    <div class="scrollbox">
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
        </div>
    </main>
    
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>