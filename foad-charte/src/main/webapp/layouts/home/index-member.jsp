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
    
    <main>
        <div class="container-fluid">
            <!-- Content navbar -->
            <jsp:include page="../includes/content-navbar.jsp" />
  
            <div class="row">
                <div class="col-sm-6 col-md-4">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h2 class="panel-title">
                                <span><op:translate key="MY_WORKSPACES_PANEL_TITLE" /></span>
                            </h2>
                        </div>
                    
                        <div class="panel-body">
                            <p:region regionName="col-1" />
                        </div>
                    </div>
                </div>
                
                <div class="col-sm-6 col-md-8">
                	<div class="row">
                		<div class="col-md-12">
                			<p:region regionName="top" />
                		</div>
                	</div>
                	
                
                    <div class="row">
                        <div class="col-md-6">
                            <p:region regionName="col-2" />
                        </div>
                        
                        <!-- Drawer -->
                        <div id="drawer">
                            <p:region regionName="drawer-toolbar" />
                            
                            <div class="col-md-6">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h2 class="panel-title">
                                            <span><op:translate key="OTHER_WORKSPACES_PANEL_TITLE" /></span>
                                        </h2>
                                    </div>
                                
                                    <div class="panel-body">
                                        <p:region regionName="col-3" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
    
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
