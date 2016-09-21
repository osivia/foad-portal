<%@ taglib uri="portal-layout" prefix="p" %>


<footer class="hidden-xs">
    <div class="container-fluid">
        <div class="logo">
            <a href="${requestScope['osivia.home.url']}">
                <img src="/foad-charte/img/logo-foad-footer.png" alt="FOAD">
            </a>
        </div>
    </div>
</footer>


<!-- Page settings -->
<p:region regionName="pageSettings" />
<!-- AJAX scripts -->
<p:region regionName="AJAXScripts" />
<!-- AJAX footer -->
<p:region regionName="AJAXFooter" />    
