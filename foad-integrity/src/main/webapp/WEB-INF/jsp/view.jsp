<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="checkIntegrity" var="checkIntegrityUrl">
	<portlet:param name="repare" value="false"/>
</portlet:actionURL>
<portlet:actionURL name="checkIntegrity" var="repareIntegrityUrl" >
	<portlet:param name="repare" value="true"/>
</portlet:actionURL>

<p>
    <a href="${checkIntegrityUrl}" class="btn btn-primary no-ajax-link">
        <i class="glyphicons glyphicons-electricity"></i>
        <span><op:translate key="CHECK_INTEG" /></span>
    </a>
    <a href="${repareIntegrityUrl}" class="btn btn-primary no-ajax-link">
        <i class="glyphicons glyphicons-electricity"></i>
        <span><op:translate key="REPARE_INTEG" /></span>
    </a>    

</p>
