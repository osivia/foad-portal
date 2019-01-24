<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<c:set var="workspace" scope="request" value="true" />
<c:set var="drawer" scope="request" value="true" />


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
                        
                        <p:region regionName="top" />
                        
                        <div class="row">
                            <div class="col-sm-6 col-lg-4">
                                <p:region regionName="col-2" />
                            </div>
                            
                            <div class="col-sm-6 col-lg-8">
                                <p:region regionName="col-3-4" />
                            
                                <div class="row">
                                    <div class="col-lg-6">
                                        <p:region regionName="col-3" />
                                    </div>
                                    
                                    <div class="col-lg-6">
                                        <p:region regionName="col-4" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-sm-8">
                                <p:region regionName="col-5" />
                            </div>
                            
                            <div class="col-sm-4">
                                <p:region regionName="col-6" />
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
