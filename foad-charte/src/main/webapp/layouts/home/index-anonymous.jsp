<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<c:set var="home" scope="request" value="true" />


<!DOCTYPE html>
<html lang="fr">

<head>
    <jsp:include page="../includes/head.jsp" />
</head>


<body class="home">
    <!-- Header -->
    <jsp:include page="../includes/header.jsp" />
    
    <!-- Main -->
    <main>
        <div class="container-fluid">
            <!-- Content header -->
            <jsp:include page="../includes/content-header.jsp" />
        
            <div class="row">
                <div class="col-sm-5 col-md-4 col-lg-3">
                    <p:region regionName="col-1" />
                </div>
                
                <div class="col-sm-7 col-md-8 col-lg-9">
                    <p:region regionName="col-2" />
                </div>
            </div>
        </div>
    </main>
    
    <!-- Footer -->
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
