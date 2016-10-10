<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<div class="row">
    <div class="col-sm-6">
        <div class="logo">
            <!-- FOAD -->
            <img src="/foad-charte/img/logo-foad.png" alt="FOAD, Formation Ouverte À Distance">
            
            <!-- Ministère -->
            <img src="/foad-charte/img/logo-ministere.png" alt="Ministère de l'Éducation Nationale, de l'Enseignement supérieur et de la Recherche">
        </div>
    </div>
    
    <div class="col-sm-6">
        <h1>
            <a href="${requestScope['osivia.home.url']}">
                <span class="text-middle">
                    <span><op:translate key="BRAND" /></span>
                    <br>
                    <small>Espaces collaboratifs</small>
                </span>
            </a>
        </h1>
    </div>
</div>

