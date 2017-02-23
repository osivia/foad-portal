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
    
    <main>
        <div class="container-fluid">
            <!-- Content navbar -->
            <jsp:include page="../includes/content-navbar.jsp" />

            <div class="row">
                <!-- Drawer -->
                <div id="drawer">
                    <p:region regionName="drawer-toolbar" />
                </div>
            </div>
            
            <p:region regionName="maximized" />
        </div>
    </main>
    
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
