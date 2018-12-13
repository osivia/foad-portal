<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<c:set var="search" scope="request" value="true" />
<c:set var="drawer" scope="request" value="true" />
<c:set var="maximized" scope="request" value="true" />


<!DOCTYPE html>
<html lang="fr">

<head>
    <jsp:include page="../includes/head.jsp" />
</head>


<body class="search">
    <!-- Header -->
    <jsp:include page="../includes/header.jsp" />
    
    <!-- Main -->
    <main>
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-offset-1 col-sm-10 col-md-offset-2 col-md-8 col-lg-offset-3 col-lg-6">
                    <!-- Content header -->
                    <jsp:include page="../includes/content-header.jsp" />
                
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
                    
                    <p:region regionName="maximized" />
                </div>
            </div>
        </div>
    </main>
    
    <!-- Footer -->
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
