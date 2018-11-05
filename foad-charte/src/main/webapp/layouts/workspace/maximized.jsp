<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<html lang="fr">

<head>
    <jsp:include page="../includes/head.jsp" />
</head>


<body>
    <!-- Header -->
    <jsp:include page="../includes/header.jsp" />
    
    <!-- Main -->
    <main>
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-4 col-lg-3">
                    <p:region regionName="col-1" />
                </div>
                
                <div class="col-sm-8 col-lg-9">
                    <!-- Content header -->
                    <jsp:include page="../includes/content-header.jsp" />
                    
                    <p:region regionName="maximized" />
                </div>
            </div>
        </div>
    </main>
    
    <!-- Footer -->
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
