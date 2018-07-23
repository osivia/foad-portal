<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="checkIntegrity" var="checkIntegrityUrl">
	<portlet:param name="repare" value="false"/>
</portlet:actionURL>
<portlet:actionURL name="checkIntegrity" var="repareIntegrityUrl" >
	<portlet:param name="repare" value="true"/>
</portlet:actionURL>
<portlet:actionURL name="purgeUsers" var="testPurgeUsersUrl" >
	<portlet:param name="purge" value="false"/>
</portlet:actionURL>
<portlet:actionURL name="purgeUsers" var="purgeUsersUrl" >
	<portlet:param name="purge" value="true"/>
</portlet:actionURL>


<portlet:actionURL name="chgValidDate" var="chgValidDateUrl" >
</portlet:actionURL>

<h3><op:translate key="INTEG_TITLE" /></h3>

<p>
    <a href="${checkIntegrityUrl}" class="btn btn-default no-ajax-link">
        <span><op:translate key="CHECK_INTEG" /></span>
    </a>
    <a href="${repareIntegrityUrl}" class="btn btn-primary no-ajax-link">
        <i class="glyphicons glyphicons-electricity"></i>
        <span><op:translate key="REPARE_INTEG" /></span>
    </a>    

</p>

<hr >

<h3><op:translate key="PURGE_TITLE" /></h3>

<p>
    <a href="${testPurgeUsersUrl}" class="btn btn-default no-ajax-link">
        <span><op:translate key="TEST_BUTTON" /></span>
    </a>
    <a href="${purgeUsersUrl}" class="btn btn-danger no-ajax-link">
        <i class="glyphicons glyphicons-electricity"></i>
        <span><op:translate key="PURGE_BUTTON" /></span>
    </a>
</p>


<hr >

<h3><op:translate key="VALIDITY_TITLE" /></h3>


<form:form action="${chgValidDateUrl}" method="post" modelAttribute="chgValidDateForm" cssClass="form-horizontal" role="form">
	<p><op:translate key="VALIDITY_HELP_1" /></p>

   <!-- Change by date -->
   <div class="form-group">
       <c:set var="placeholderDate"><op:translate key="DATE_HELP" /></c:set>
       <form:label path="currentDate" cssClass="control-label col-sm-3 col-lg-2"><op:translate key="CURR_DATE" /></form:label>
       <div class="col-sm-9 col-lg-10">
           <form:input path="currentDate" cssClass="form-control" placeholder="${placeholderDate}" />
       </div>
   </div>	

	<p><op:translate key="VALIDITY_HELP_2" /></p>
	<!-- Logins -->
   <div class="form-group">
       <c:set var="placeholder"><op:translate key="VALIDITY_LOGINS_HELP" /></c:set>
       <form:label path="logins" cssClass="control-label col-sm-3 col-lg-2"><op:translate key="VALIDITY_LOGINS" /></form:label>
       <div class="col-sm-9 col-lg-10">
           <form:textarea path="logins" rows="3" cssClass="form-control" placeholder="${placeholder}" />
       </div>
   </div>
   
	<!-- Date -->
   <div class="form-group">
       <c:set var="placeholderDate"><op:translate key="DATE_HELP" /></c:set>
       <form:label path="validityDate" cssClass="control-label col-sm-3 col-lg-2"><op:translate key="VALIDITY_DATE" /></form:label>
       <div class="col-sm-9 col-lg-10">
           <form:input path="validityDate" cssClass="form-control" placeholder="${placeholderDate}" />
       </div>
   </div>
   
   
   <button type="submit" name="btnName" value="test" class="btn btn-default no-ajax-link"><op:translate key="TEST_BUTTON" /></button>
   <button type="submit" name="btnName" value="run"  class="btn btn-primary no-ajax-link">
   		<i class="glyphicons glyphicons-electricity"></i>
   		<op:translate key="VALIDITY_BUTTON" />
   	</button>

</form:form>

