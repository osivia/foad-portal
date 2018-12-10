var fileBrowserToolbarXhr;
var fileBrowserPreviousIndex = -1;


$JQry(function() {
	var $browser = $JQry(".file-browser");
	
	if (!$browser.data("loaded")) {
		// Selectable
		$JQry(".file-browser-table-row-group").selectable({
			cancel: "a, .file-browser-draggable",
			filter: ".file-browser-table-row",
			
			selected: function(event, ui) {
				var $selected = $JQry(ui.selected);
				
				$selected.removeClass("bg-info");
				$selected.addClass("bg-primary");
			},
			
			selecting: function(event, ui) {
				var $selecting = $JQry(ui.selecting);
				var $selectable = $selecting.closest(".file-browser-table-row-group");
				var $selectee = $selectable.find(".file-browser-table-row");
				var currentIndex = $selectee.index(ui.selecting);
				
				$selectable.addClass("remove-hover");
				
				if (event.shiftKey && previousIndex > -1) {
					$selectee.slice(Math.min(previousIndex, currentIndex), Math.max(previousIndex, currentIndex) + 1).addClass("ui-selected bg-primary");
				} else {
					$selecting.addClass("bg-info");
					previousIndex = currentIndex;
				}
			},
			
			stop: function(event, ui) {
				var $target = $JQry(event.target);
				var $selectable = $target.closest(".file-browser-table-row-group");
				
				$selectable.removeClass("remove-hover");
				
				// Update toolbar
				updateFileBrowserToolbar($target);
			},
			
			unselected: function(event, ui) {
				var $unselected = $JQry(ui.unselected);
				
				if (!event.shiftKey) {
					$unselected.removeClass("bg-primary");
				}
			},
			
			unselecting: function(event, ui) {
				var $unselecting = $JQry(ui.unselecting);
				
				$unselecting.removeClass("bg-primary bg-info");
			}
		});
		
		
		// Checkbox
		$JQry(".file-browser-checkbox a").click(function(event) {
			var $target = $JQry(event.target);
			var $row = $target.closest(".file-browser-table-row");
			
			if ($row.hasClass("ui-selected")) {
				$row.removeClass("ui-selected bg-primary");
			} else {
				$row.addClass("ui-selected bg-primary");
			}
			
			// Update toolbar
			updateFileBrowserToolbar($target);
		});
		
		
		// Double click
		$JQry(".file-browser-table-row").dblclick(function(event) {
			var $target = $JQry(event.target);
			var $row = $target.closest(".file-browser-table-row");
			var $link = $row.find("a").first();
			
			if ($target.closest(".file-browser-checkbox").length) {
				// Do nothing
			} else if ($link.length) {
				$link.get(0).click();
			} else {
				console.log("Double click event failed: link not found.");
			}
		});
		
		
		// Click on draggable
		$JQry(".file-browser-draggable").click(function(event) {
			var $target = $JQry(event.target);
			var $row = $target.closest(".file-browser-table-row");
			var $selectable = $row.closest(".file-browser-table-row-group");
			
			if (event.ctrlKey) {
				$row.removeClass("ui-selected bg-primary");
			} else {
				$selectable.find(".ui-selected").each(function(index, element) {
					var $element = $JQry(element);
					
					if (!$element.is($row)) {
						$element.removeClass("ui-selected bg-primary");
					}
				});
			}
			
			// Update toolbar
			updateFileBrowserToolbar($target);
		});
		
		
		
		// File browser filler
		$JQry(".file-browser-filler").parentsUntil(".flexbox").addClass("flexbox");
		
		// Update scrollbar width
		updateFileBrowserScrollbarWidth();
		
		
		// Loaded indicator
		$browser.data("loaded", true);
	}
});


$JQry(window).resize(function() {
	// Update scrollbar width
	updateFileBrowserScrollbarWidth();
});


function updateFileBrowserToolbar($target) {
	var $browser = $target.closest(".file-browser");
	var $toolbar = $browser.find(".file-browser-toolbar");
	var $rows = $browser.find(".file-browser-table-row");
	var $selected = $browser.find(".ui-selected");
	var indexes = "";
	
	// Disable toolbar
	$toolbar.find("a").addClass("disabled");
	
	// Abort previous AJAX request
	if (fileBrowserToolbarXhr && fileBrowserToolbarXhr.readyState != 4) {
		fileBrowserToolbarXhr.abort();
    }
	
	// Build selected indexes 
	$selected.each(function(index, element) {
		var $element = $JQry(element);
		var index = $rows.index($element);
		
		if (indexes.length) {
			indexes += ",";
		}
		
		indexes += index;
	});
	
	
	// AJAX
	fileBrowserToolbarXhr = jQuery.ajax({
		url: $browser.data("toolbar-url"),
		async: true,
		cache: false,
		data: {
			indexes: indexes
		},
		dataType: "html",
		success : function(data, status, xhr) {
			$toolbar.html(data);
			
			// Call jQuery.ready() events
			$JQry(document).ready();
		}
	});
}


function updateFileBrowserScrollbarWidth() {
	var $filler = $JQry(".file-browser-filler");
	
	$filler.each(function(index, element) {
		var $element = $JQry(element);
		var width = Math.round($element.innerWidth() - $element.children().outerWidth(true));
		var $header = $element.find(".file-browser-table-header-group");
			
		// Update header
		$header.css({
			"padding-right": width
		});
	});
}
