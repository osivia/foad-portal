<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<c:set var="workspace" scope="request" value="true" />
<c:set var="drawer" scope="request" value="true" />
<c:set var="maximized" scope="request" value="true" />


<!DOCTYPE html>
<html lang="fr">

<head>
    <jsp:include page="../includes/head.jsp" />
</head>


<body class="workspace fixed-layout">
    <!-- Header -->
    <jsp:include page="../includes/header.jsp" />
    
    <!-- Main -->
    <main>
        <div class="container-fluid flexbox">
            <div class="row flexbox">
                <!-- Drawer -->
                <div id="drawer" class="col-sm-4 col-md-3 flexbox">
                   
                    <div class="flexbox scrollbox">
                        <p:region regionName="col-1" />
                    </div>
                    
                    <div class="row">
                        <p:region regionName="drawer-toolbar" />
                    </div>                    
                </div>
            
                <div class="col-sm-8 col-md-9 flexbox">
                    <div class="flexbox scrollbox">
                        <!-- Content header -->
                        <jsp:include page="../includes/content-header.jsp" />
                        
                        <p:region regionName="maximized" />
                    </div>
                </div>
            </div>
        </div>
    </main>
    
    <!-- Footer -->
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
