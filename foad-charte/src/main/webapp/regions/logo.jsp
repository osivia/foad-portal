<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<c:set var="brand"><op:translate key="BRAND" /></c:set>


<div class="row">
    <div class="col-sm-6">
        <div class="logo">
            <!-- FOAD -->
            <img src="/foad-charte/img/logo-foad.png" alt="FOAD, Formation Ouverte � Distance">
            
            <!-- Minist�re -->
            <img src="/foad-charte/img/logo-ministere.png" alt="Minist�re de l'�ducation Nationale, de l'Enseignement sup�rieur et de la Recherche">
        </div>
    </div>
    
    <div class="col-sm-6">
        <h1 class="logo">
            <a href="${requestScope['osivia.home.url']}">
                <img src="/foad-charte/img/logo-tribu.png" alt="${brand}">
            </a>
        </h1>
    </div>
</div>

