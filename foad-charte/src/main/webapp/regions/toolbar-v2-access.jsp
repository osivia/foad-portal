<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal"
	prefix="op"%>

<%@ page contentType="text/html" isELIgnored="false"%>

<c:if test="${not empty requestScope['foad.portalv2.url']}">

	<p class="text-right">
		<a href="${requestScope['foad.portalv2.url']}"
			class="btn btn-xs btn-default"> <span class="label label-info">V2</span>
			<span>Tribu Nouvelle version</span>
		</a>
	</p>
</c:if>