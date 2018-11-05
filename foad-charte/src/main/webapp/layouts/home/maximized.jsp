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
            <p:region regionName="maximized" />
        </div>
    </main>
    
    <!-- Footer -->
    <jsp:include page="../includes/footer.jsp" />
</body>

</html>
