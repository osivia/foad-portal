<!DOCTYPE html>
<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<html>

<head>
    <jsp:include page="../includes/head.jsp" />
</head>


<body>
    <!-- Toolbar -->
    <p:region regionName="toolbar" />

    <jsp:include page="../includes/header.jsp" />
    
    <div class="wrapper-outer">
        <div class="wrapper-inner">
            <main id="page-content" class="container-fluid">
                <!-- Content navbar -->
                <jsp:include page="../includes/content-navbar.jsp" />
            
                <!-- Notifications -->
                <p:region regionName="notifications" />
                
                <div class="row">
                    <div class="col-sm-6 col-md-7 col-lg-8">
                        <p:region regionName="col-1" />
                    </div>
                    
                    <!-- Drawer -->
                    <div id="drawer">
                        <p:region regionName="drawer-toolbar" />
                        
                        <div class="col-sm-6 col-md-5 col-lg-4">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h2 class="panel-title">
                                        <span><op:translate key="PUBLIC_WORKSPACES_PANEL_TITLE" /></span>
                                    </h2>
                                </div>
                            
                                <div class="panel-body">
                                    <p:region regionName="col-2" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>
    
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
