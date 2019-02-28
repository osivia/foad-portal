<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />


<div class="footer-row">
    <div>
        <img src="${contextPath}/img/logo-foad-footer.png" alt="FOAD" class="media-object">        
    </div>
    
    <div>
        <ul class="list-inline text-center">
            <li>
                <i class="glyphicons glyphicons-copyright-mark"></i>
                <span>${requestScope['osivia.header.application.name']}</span>
                <span>2019</span>
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
        </ul>    
    </div>
    
    <div>
        <c:set var="faqUrl" value="${requestScope['help.faq.url']}" />
        <c:set var="tutorialsUrl" value="${requestScope['help.tutorials.url']}" />
        <c:set var="contactUrl" value="${requestScope['help.contact.url']}" />
    
        <c:if test="${not empty faqUrl or not empty tutorialsUrl or not empty contactUrl}">
            <!-- Help menu -->
            <div class="dropdown dropup text-right">
                <a href="javascript:;" data-toggle="dropdown" data-target="#" role="button">
                    <span><op:translate key="FOOTER_HELP_MENU" /></span>
                    <span class="caret"></span>
                </a>
                
                <ul class="dropdown-menu dropdown-menu-right">
                    <!-- FAQ -->
                    <c:if test="${not empty faqUrl}">
                        <li>
                            <a href="${faqUrl}">
                                <span><op:translate key="FOOTER_HELP_FAQ" /></span>
                            </a>
                        </li>
                    </c:if>
                    
                    <!-- Tutorials -->
                    <c:if test="${not empty tutorialsUrl}">
                        <li>
                            <a href="${tutorialsUrl}">
                                <span><op:translate key="FOOTER_HELP_TUTORIALS" /></span>
                            </a>
                        </li>
                    </c:if>
                    
                    <!-- Contact -->
                    <c:if test="${not empty contactUrl}">
                        <li>
                            <a href="${contactUrl}">
                                <span><op:translate key="FOOTER_HELP_CONTACT" /></span>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </div>
        </c:if>
    </div>
</div>
