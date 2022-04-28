<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>

<c:choose>
	<c:when test="${form.error}">
		<op:translate key="SERVICE_UNAVALIABLE" />
	</c:when>
	<c:otherwise>
	
		<p class="text-center">
			<a class="btn btn-primary" href="${form.url}" role="button" target="_blank">
				<i class="halflings halflings-chevron-right"></i>
				<op:translate key="JOIN" /></a>
		</p>
	
	</c:otherwise>
</c:choose>
