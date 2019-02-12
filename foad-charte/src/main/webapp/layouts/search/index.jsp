<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<c:set var="search" scope="request" value="true" />
<c:set var="drawer" scope="request" value="true" />


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
        <div class="container">
            <div class="row">
                <div class="col-sm-offset-1 col-sm-10 col-md-offset-2 col-md-8">
                    <!-- Content header -->
                    <jsp:include page="../includes/content-header.jsp" />
                
                    <p:region regionName="search-toolbar" />
                    
                    <div class="row">
                        <div class="col-sm-7 col-lg-8">
                            <p:region regionName="search-results" />
                        </div>
                        
                        <!-- Drawer -->
                        <div id="drawer" class="col-sm-5 col-lg-4">
                            
                            <div class="flexbox scrollbox">
                                <p:region regionName="search-filters" />
                            </div>                
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
    
    <!-- Footer -->
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
