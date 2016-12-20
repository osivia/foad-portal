<!DOCTYPE html>
<%@ taglib uri="portal-layout" prefix="p" %>


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
                    <div id="drawer">
                        <p:region regionName="drawer-toolbar" />
                        
                        <div class="col-sm-6">
                            <p:region regionName="col-1" />
                        </div>
                    </div>
                    
                    <div class="col-sm-6">
                        <p:region regionName="maximized" />
                    </div>
                </div>
            </main>
        </div>
    </div>
    
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
