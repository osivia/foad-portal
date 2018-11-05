<%@ taglib uri="portal-layout" prefix="p" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<header class="hidden-xs">
    <div class="container-fluid">
        <div class="row">
            <div class="col">
                <c:choose>
                    <c:when test="${home}">
                        <!-- Logo EN -->
                        <div class="logo-en">
                            <img src="/foad-charte/img/logo-en.png" alt="Minist&egrave;re de l'&Eacute;ducation Nationale">
                        </div>
                    </c:when>
                    
                    <c:otherwise>
                        <!-- Header title -->
                        <p:region regionName="header-title" />
                    </c:otherwise>
                </c:choose>
            </div>
            
            <div class="col">
                <!-- Toolbar -->
                <p:region regionName="toolbar" />
            </div>
        </div>
    
        <c:choose>
            <c:when test="${home}">
                <!-- Logo Tribu large -->
                <h1 class="logo-tribu-large">
                    <img src="/foad-charte/img/logo-tribu-large.png" alt="Tribu">
                </h1>
            </c:when>
        
            <c:otherwise>
                <!-- Workspace tabs -->
                <p:region regionName="workspace-tabs" />
            </c:otherwise>
        </c:choose>
    </div> 
</header>
