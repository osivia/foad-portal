<!DOCTYPE html>
<%@ taglib uri="portal-layout" prefix="p" %>


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
        
            <p:region regionName="maximized" />
        </div>
    </main>
    
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
