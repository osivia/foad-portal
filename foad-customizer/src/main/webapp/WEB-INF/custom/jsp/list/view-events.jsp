<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false" %>


<ul class="list-unstyled tiles">
    <c:forEach var="document" items="${documents}">
        <c:set var="date" value="${document.properties['vevent:dtstart']}" />
    
        <li>
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="media">
                        <div class="media-left">
                            <!-- Date -->
                            <div class="flat-color-event bg-primary">
                                <div><fmt:formatDate value="${date}" pattern="d" /></div>
                                <div><fmt:formatDate value="${date}" pattern="MMM" /></div>
                            </div>
                        </div>
                        
                        <div class="media-body">
                            <!-- Title -->
                            <h3 class="text-primary">
                                <span><ttc:title document="${document}" /></span>
                            </h3>
                            
                            <!-- Date -->
                            <p class="date">
                                <c:choose>
                                    <c:when test="${document.properties['vevent:allDay']}">
                                        <fmt:formatDate value="${date}" type="date" dateStyle="long" var="start" />
                                        <fmt:formatDate value="${document.properties['vevent:dtend']}" type="date" dateStyle="long" var="end" />
                                        
                                        <c:choose>
                                            <c:when test="${start eq end}">
                                                <span>${start}</span>    
                                            </c:when>
                                            
                                            <c:otherwise>
                                                <span><op:translate key="LIST_TEMPLATE_EVENTS_MANY_DAYS" args="${start}|${end}"/></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    
                                    <c:otherwise>
                                        <span><fmt:formatDate value="${date}" type="date" dateStyle="long" /></span>
                                        <span><op:translate key="LIST_TEMPLATE_EVENTS_DATE_TIME_SEPARATOR" /></span>
                                        <span><fmt:formatDate value="${date}" pattern="H'h'mm" /></span>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            
                            <!-- Location -->
                            <c:set var="location" value="${document.properties['vevent:location']}" />
                            <c:if test="${not empty location}">
                                <p>${location}</p>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </li>
    </c:forEach>
    
    
    <c:if test="${empty documents}">
        <li>
            <p>
                <span class="text-muted"><op:translate key="LIST_TEMPLATE_EVENTS_EMPTY" /></span>
            </p>
        </li>
    </c:if>
</ul>
