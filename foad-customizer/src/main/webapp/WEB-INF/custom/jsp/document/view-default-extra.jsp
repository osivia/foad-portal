<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false"%>


<c:if test="${metadata}">
    <div class="extra">
        <!-- Vignette -->
        <c:set var="vignetteUrl"><ttc:pictureLink document="${document}" property="ttc:vignette" /></c:set>
        <c:if test="${not empty vignetteUrl}">
            <p><img src="${vignetteUrl}" alt="" class="img-responsive"></p>
        </c:if>
        
        <!-- Description -->
        <c:if test="${not empty document.properties['dc:description']}">
            <p>${document.properties['dc:description']}</p>
        </c:if>
        
        <!-- Validation state -->
        <c:set var="validationState" value="${document.properties['validationState']}" />
        <c:if test="${not empty validationState}">
            <p>
                <span class="label label-${validationState.color}">
                    <c:if test="${not empty validationState.icon}">
                        <i class="${validationState.icon}"></i>
                    </c:if>
                    
                    <span><op:translate key="${validationState.key}" /></span>
                </span>
            </p>
        </c:if>
    </div>
</c:if>
