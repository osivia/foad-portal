<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />


<img src="${contextPath}/img/logo-foad-footer.png" alt="FOAD">

<ul>
    <li>
        <i class="glyphicons glyphicons-copyright-mark"></i>
        <span>${requestScope['osivia.header.application.name']}</span>
        <span>2018</span>
    </li>
    
    <!-- CGU -->
    <c:set var="cguUrl" value="${requestScope['cgu.url']}" />
    <c:if test="${not empty cguUrl}">
        <li>
            <a href="${cguUrl}">
                <span><op:translate key="FOOTER_CGU" /></span>
            </a>
        </li>
    </c:if>

    <!-- Help -->
    <c:set var="helpUrl" value="${requestScope['osivia.toolbar.helpURL']}" />
    <c:if test="${not empty helpUrl}">
        <li>
            <a href="${helpUrl}">
                <span><op:translate key="FOOTER_HELP" /></span>
            </a>
        </li>
    </c:if>
</ul>

