<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<p>
    <button type="button" class="btn btn-default" data-target="#search-filters-panel" data-toggle="collapse">
        <span><op:translate key="DISPLAY_SEARCH_FILTERS" /></span> 
    </button>
</p>


<div id="search-filters-panel" class="collapse ${requestScope['osivia.advancedSearch.filters'] ? 'in' : ''}">
    <div class="panel panel-default">
        <div class="panel-body">
            <div class="row">
