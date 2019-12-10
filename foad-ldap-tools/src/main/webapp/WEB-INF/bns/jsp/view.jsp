<%@page import="javax.portlet.WindowState"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page contentType="text/plain; charset=UTF-8"%>


<portlet:defineObjects />

<portlet:actionURL name="executeImport" var="executeImportUrl">
</portlet:actionURL>

<form:form  method="post" action="${executeImportUrl}" enctype="multipart/form-data" cssClass="form-horizontal" modelAttribute="form" role="form">


	<!-- File -->
    <div class="form-group">
        <form:label path="file.upload" cssClass="col-sm-3 col-lg-6 control-label">Fichier</form:label>
        <div class="col-sm-9 col-lg-6">
            <div>
                <!-- Upload -->
                <label class="btn btn-sm btn-default btn-file">
                    <i class="halflings halflings-folder-open"></i>
                    <span>Importer</span>
                    <form:input type="file" path="file.upload" />
                </label>
                <input type="submit" name="upload-avatar" class="hidden">
                
            </div>
        </div>
    </div>
    
	<!-- Group -->
    <div class="form-group">
        <form:label path="profile" cssClass="col-sm-3 col-lg-6 control-label">Groupe</form:label>
        <div class="col-sm-9 col-lg-6">
            <div>
	               <form:select path="profile">
	               	<form:options items="${form.profiles}" />
	               </form:select>

                
            </div>
        </div>
    </div>    
   
	<!-- Buttons -->
    <div class="row">
        <div class="col-sm-offset-3 col-sm-9 col-lg-offset-2 col-lg-10">
            <!-- Save -->
            <button type="submit" name="save" class="btn btn-primary">
                <i class="glyphicons glyphicons-floppy-disk"></i>
                <span><op:translate key="SAVE" /></span>
            </button>

           
        </div>
	</div>
	

</form:form>