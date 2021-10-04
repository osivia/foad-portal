<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="generate" var="generateUrl" />

<form:form action="${generateUrl}" method="post" modelAttribute="generateForm" cssClass="form-horizontal" role="form">

   <!-- Min index -->
   <div class="form-group">
       <form:label path="minIndex" cssClass="control-label col-sm-3 col-lg-2">Espace min :</form:label>
       <div class="col-sm-9 col-lg-10">
           <form:input path="minIndex" cssClass="form-control" />
       </div>
   </div>
   <!-- Max index -->
   <div class="form-group">
       <form:label path="maxIndex" cssClass="control-label col-sm-3 col-lg-2">Espace max :</form:label>
       <div class="col-sm-9 col-lg-10">
           <form:input path="maxIndex" cssClass="form-control" />
       </div>
   </div>
   
   <button type="submit" name="btnName" value="generate" class="btn btn-primary no-ajax-link">
		<i class="glyphicons glyphicons-electricity"></i>   
   		<span><op:translate key="GENERATE" /></span>
   	</button>

   <button type="submit" name="btnName" value="purge" class="btn btn-danger no-ajax-link">
		<i class="glyphicons glyphicons-spray"></i>   
   		<span><op:translate key="PURGE" /></span>
   	</button>
   	
   	
</form:form>
