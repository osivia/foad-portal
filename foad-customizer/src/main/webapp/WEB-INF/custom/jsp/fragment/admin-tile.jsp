<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<c:set var="namespace"><portlet:namespace /></c:set>


<!-- Path -->
<c:set var="placeholder"><op:translate key="FRAGMENT_TILE_PATH_PLACEHOLDER" /></c:set>
<div class="form-group">
    <label for="${namespace}-path" class="col-sm-3 control-label"><op:translate key="FRAGMENT_TILE_PATH_LABEL" /></label>
    <div class="col-sm-9">
        <input id="${namespace}-path" type="text" name="path" value="${path}" placeholder="${placeholder}" class="form-control">
        <p class="help-block">
            <span><op:translate key="FRAGMENT_TILE_PATH_HELP" /></span>
        </p>
    </div>
</div>


<!-- Target -->
<c:set var="placeholder"><op:translate key="FRAGMENT_TILE_TARGET_PLACEHOLDER" /></c:set>
<div class="form-group">
    <label for="${namespace}-target" class="col-sm-3 control-label"><op:translate key="FRAGMENT_TILE_TARGET_LABEL" /></label>
    <div class="col-sm-9">
        <input id="${namespace}-target" type="text" name="target" value="${target}" placeholder="${placeholder}" class="form-control">
        <p class="help-block">
            <span><op:translate key="FRAGMENT_TILE_TARGET_HELP" /></span>
        </p>
    </div>
</div>
