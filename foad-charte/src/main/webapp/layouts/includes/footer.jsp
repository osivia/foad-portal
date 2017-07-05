<%@ taglib uri="portal-layout" prefix="p" %>


<footer class="hidden-xs">
    <div class="container-fluid">
        <div class="logo">
            <img src="/foad-charte/img/logo-foad-footer.png" alt="FOAD">
        </div>
    </div>
</footer>


<!-- Notifications -->
<p:region regionName="notifications" />
<!-- Page settings -->
<p:region regionName="pageSettings" />
<!-- AJAX scripts -->
<p:region regionName="AJAXScripts" />
<!-- AJAX footer -->
<p:region regionName="AJAXFooter" />


<!-- Piwik -->
<script type="text/javascript">
	var u = "https://wa.phm.education.gouv.fr/foad/";
	var ui = 2;
	var _paq = _paq || [];
	_paq.push([ 'trackPageView' ]);
	_paq.push([ 'enableLinkTracking' ]);
	_paq.push([ 'setTrackerUrl', u + 'p.php' ]);
	_paq.push([ 'setSiteId', ui ]);
	function piwikIntegrated() {
		(function() {
			var d = document, g = d.createElement('script'), s = d
					.getElementsByTagName('script')[0];
			g.type = 'text/javascript';
			g.async = true;
			g.defer = true;
			g.src = u + 'p.js';
			s.parentNode.insertBefore(g, s);
		})();
	}
</script>
<noscript>
    <p>
        <img src="https://wa.phm.education.gouv.fr/foad/p.php?idsite=11" style="border: 0;" alt="" />
    </p>
</noscript>
