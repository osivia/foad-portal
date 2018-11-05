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

            <div class="row">
                <div class="col-sm-offset-1 col-sm-10 col-md-offset-2 col-md-8 col-lg-offset-3 col-lg-6">
                    <p:region regionName="search-toolbar" />
                    
                    <div class="row">
                        <!-- Drawer -->
                        <div id="drawer">
                            <p:region regionName="drawer-toolbar" />
                            
                            <div class="col-xs-12">
                                <p:region regionName="search-filters" />
                            </div>
                        </div>
                    </div>
                    
                    <p:region regionName="search-results" />
                </div>
            </div>
        </div>
    </main>
    
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
