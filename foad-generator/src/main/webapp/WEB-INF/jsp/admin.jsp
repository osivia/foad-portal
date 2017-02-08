<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="save" var="url" />


<form:form modelAttribute="configuration" action="${url}" method="post" cssClass="form-horizontal no-ajax-link" role="form">
    <fieldset>
        <legend>
            <i class="glyphicons glyphicons-building"></i>
            <span><op:translate key="INSTITUTIONS" /></span>
        </legend>
        
        <!-- Pools count -->
        <div class="form-group">
            <form:label path="poolsCount" cssClass="col-sm-3 control-label"><op:translate key="POOLS_COUNT" /></form:label>
            <div class="col-sm-9">
                <form:input path="poolsCount" type="number" cssClass="form-control" />
            </div>
        </div>
        
        <!-- Institutions count -->
        <div class="form-group">
            <form:label path="institutionsCount" cssClass="col-sm-3 control-label"><op:translate key="INSTITUTIONS_COUNT" /></form:label>
            <div class="col-sm-9">
                <form:input path="institutionsCount" type="number" cssClass="form-control" />
            </div>
        </div>
        
    </fieldset>

    <fieldset>
        <legend>
            <i class="glyphicons glyphicons-nameplate"></i>
            <span><op:translate key="LDAP" /></span>
        </legend>
        
        <!-- Users base DN -->
        <div class="form-group">
            <form:label path="profilesBaseDn" cssClass="col-sm-3 control-label"><op:translate key="PROFILES_BASE_DN" /></form:label>
            <div class="col-sm-9">
                <form:input path="profilesBaseDn" cssClass="form-control" />
            </div>
        </div>
        
        <!-- Users base DN -->
        <div class="form-group">
            <form:label path="usersBaseDn" cssClass="col-sm-3 control-label"><op:translate key="USERS_BASE_DN" /></form:label>
            <div class="col-sm-9">
                <form:input path="usersBaseDn" cssClass="form-control" />
            </div>
        </div>
        
        <!-- Users count -->
        <div class="form-group">
            <form:label path="usersCount" cssClass="col-sm-3 control-label"><op:translate key="USERS_COUNT" /></form:label>
            <div class="col-sm-9">
                <form:input path="usersCount" type="number" cssClass="form-control" />
            </div>
        </div>
        
    </fieldset>
    
    
    <fieldset>
        <legend>
            <i class="glyphicons glyphicons-briefcase"></i>
            <span><op:translate key="DOCUMENTS" /></span>
        </legend>

        <!-- Folders count -->
        <div class="form-group">
            <form:label path="foldersCount" cssClass="col-sm-3 control-label"><op:translate key="FOLDERS_COUNT" /></form:label>
            <div class="col-sm-9">
                <form:input path="foldersCount" type="number" cssClass="form-control" />
            </div>
        </div>
        
        <!-- Activities count -->
        <div class="form-group">
            <form:label path="activitiesCount" cssClass="col-sm-3 control-label"><op:translate key="ACTIVITIES_COUNT" /></form:label>
            <div class="col-sm-9">
                <form:input path="activitiesCount" type="number" cssClass="form-control" />
            </div>
        </div>
        
        <!-- Referrers count -->
        <div class="form-group">
            <form:label path="referrersCount" cssClass="col-sm-3 control-label"><op:translate key="REFERRERS_COUNT" /></form:label>
            <div class="col-sm-9">
                <form:input path="referrersCount" type="number" cssClass="form-control" />
            </div>
        </div>
    </fieldset>


    <!-- Buttons -->
    <div class="form-group">
        <div class="col-sm-offset-3 col-sm-9">
            <button type="submit" class="btn btn-primary">
                <i class="glyphicons glyphicons-floppy-disk"></i>
                <span><op:translate key="SAVE" /></span>
            </button>
            <button type="button" class="btn btn-default" onclick="closeFancybox()"><op:translate key="CANCEL" /></button>
        </div>
    </div>
</form:form>
