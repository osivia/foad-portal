<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="migrateAccount" var="migrateAccountUrl" />


<form:form action="${migrateAccountUrl}" method="post" modelAttribute="form" cssClass="form-horizontal" role="form">

	<p><op:translate key="account.mig.help" /></p>

   <!-- Old account -->
   <div class="form-group">
       <form:label path="oldAccountUid" cssClass="control-label col-sm-3 col-lg-2"><op:translate key="account.mig.oldAccountUid" /></form:label>
       <div class="col-sm-9 col-lg-10">
           <form:input path="oldAccountUid" cssClass="form-control" />
       </div>
   </div>

	<!-- New account -->
	<div class="form-group">
		<form:label path="newAccountUids" cssClass="control-label">
			<op:translate key="account.mig.newAccountUids" />
		</form:label>
		<div class="col-xs-12">
			<form:textarea path="newAccountUids" cssClass="form-control" rows="5" />
		</div>
	</div>

	<button type="submit"  class="btn btn-primary no-ajax-link">
   		<i class="glyphicons glyphicons-electricity"></i>
   		<op:translate key="account.mig.btn" />
   	</button>
   
</form:form>