<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="serverUrl" value="${requestScope['stats.server.url']}" />
<c:set var="serverSiteId" value="${requestScope['stats.server.siteid']}" />

<c:set var="dimSpaceId" value="${requestScope['stats.dim.spaceid']}" />
<c:set var="dimSpaceTitle" value="${requestScope['stats.dim.spacetitle']}" />

<c:set var="spaceid" value="${requestScope['stats.space.id']}" />
<c:set var="spacetitle" value="${requestScope['stats.space.title']}" />


<c:if test="${not empty serverUrl}">

	<!-- Piwik -->
	<script type="text/javascript">
		var u = "${serverUrl}";
		var ui = ${serverSiteId};
		var _paq = _paq || [];
		
		<c:if test="${not empty dimSpaceId}">
			_paq.push(['setCustomDimension', ${dimSpaceId}, "${spaceid}"
				       ]);
		</c:if>
		
		<c:if test="${not empty dimSpaceTitle}">
			_paq.push(['setCustomDimension', ${dimSpaceTitle}, "${spacetitle}"
			       ]);	
		</c:if>
		
		_paq.push([ 'trackPageView' ]);
		_paq.push([ 'enableLinkTracking' ]);
		_paq.push([ 'setTrackerUrl', u + '/p.php' ]);
		_paq.push([ 'setSiteId', ui ]);
		function piwikIntegrated() {
			(function() {
				var d = document, g = d.createElement('script'), s = d
						.getElementsByTagName('script')[0];
				g.type = 'text/javascript';
				g.async = true;
				g.defer = true;
				g.src = u + '/p.js';
				s.parentNode.insertBefore(g, s);
			})();
		}
	</script>
	<noscript>
	    <p>
	        <img src="${serverUrl}/p.php?idsite=${serverSiteId}" style="border: 0;" alt="" />
	    </p>
	</noscript>
	


</c:if>

