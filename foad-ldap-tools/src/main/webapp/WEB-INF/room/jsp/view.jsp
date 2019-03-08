<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="migration" var="checkMigUrl">
</portlet:actionURL>

<h3><op:translate key="ROOM_MIG_TITLE" /></h3>
<form:form action="${checkMigUrl}" method="post" modelAttribute="roomMigForm" cssClass="form-horizontal" role="form">

   <button type="submit" name="exec" value="false" class="btn btn-default no-ajax-link"><op:translate key="TEST_BUTTON" /></button>
   
   <c:if test="${not form.analyseDone}">
   		<c:set var="buttonDisabled">disabled="disabled"</c:set>
   </c:if>
   
   
   <button type="submit" name="exec" value="true" class="btn btn-default no-ajax-link" ${buttonDisabled}>
   		<i class="glyphicons glyphicons-electricity"></i>
   		<op:translate key="MIG_ROOM_BUTTON" />
	</button>

   
   <p><op:translate key="NB_ROOMS" /> : ${form.nbRooms}</p>
   <p><op:translate key="NB_ROOMS_ERR" /> : ${form.nbRoomsInError}</p>


</form:form>