<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc"%>

<%@ page isELIgnored="false"%>


<div class="extra">
    <!-- Size -->
    <p>
        <strong><ttc:fileSize size="${document.properties['file:content']['length']}" /></strong>
    </p>
</div>


<ttc:include page="view-default-extra.jsp" />
