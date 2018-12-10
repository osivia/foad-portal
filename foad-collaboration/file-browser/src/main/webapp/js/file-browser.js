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
		
		
		// Draggable
		$JQry(".file-browser-draggable").draggable({
			addClasses: false,
			appendTo: "body",
			connectToFancytree: true,
			cursor: "move",
			distance: 10,
			revert: "invalid",
			revertDuration: 200,
			
			helper: function(event) {
				var $target = $JQry(event.target);
				var $draggable = $target.closest(".file-browser-draggable");
				var $row = $draggable.closest(".file-browser-table-row");
				var $selectable = $row.closest(".file-browser-table-row-group");
				var offset = $draggable.offset();
				var click = {
					top : event.pageY - offset.top,
					left : event.pageX - offset.left
				};

				// Selected items
				var $selected;
				if ($row.hasClass("ui-selected")) {
					$selected = $selectable.find(".ui-selected");
				} else {
					$selected = $row;
				}

				// Identifiers, types & text
				var identifiers = "";
				var types = "";
				var text = "";
				$selected.each(function(index, element) {
					var $element = $JQry(element);
					
					if (index > 0) {
						identifiers += ",";
						types += ",";
						text += ", ";
					}
					
					identifiers += $element.data("id");
					types += $element.data("type");
					text += $element.data("text");
				});
				
				// Helper
				var $helper = $JQry(document.createElement("div"));
				$helper.addClass("file-browser-helper");
				$helper.data({
					identifiers: identifiers,
					types: types
				});
				$helper.css({
					height: 0,
					width: 0
				});
				
				// Helper content
				var $content = $JQry(document.createElement("div"));
				$content.addClass("bg-primary");
				$content.appendTo($helper);
				
				// Helper content animation
				$content.css({
					width: $row.width()
				});
				$content.animate({
					top: click.top + 1,
					left: click.left + 1,
					width: 300
				}, 300);
				
				// Text
				var $text = $JQry(document.createElement("div"));
				$text.addClass("text-overflow");
				$text.text(text);
				$text.appendTo($content);
				
				return $helper;
			},
			
			start: function(event, ui) {
				var $target = $JQry(event.target);
				var $row = $target.closest(".file-browser-table-row");
				var $selectable = $row.closest(".file-browser-table-row-group");
				var $selected = $selectable.find(".ui-selected");
				var $browser = $target.closest(".file-browser");
				var $toolbar = $browser.find(".file-browser-toolbar");

				if ($row.hasClass("ui-selected")) {
					$selected.addClass("file-browser-dragged");
				} else {
					$row.addClass("ui-selected bg-primary file-browser-dragged");
					
					// Deselect
					$selected.not($row).removeClass("ui-selected bg-primary");
					
					// Disable toolbar
					$toolbar.find("a").addClass("disabled");
				}
			},
			
			stop: function(event, ui) {
				var $target = $JQry(event.target);
				var $row = $target.closest(".file-browser-table-row");
				var $selectable = $row.closest(".file-browser-table-row-group");
				var $dragged = $selectable.find(".file-browser-dragged");
			
				$dragged.removeClass("file-browser-dragged");
				
				// Update toolbar
				updateFileBrowserToolbar($target);
			}
		});
		
		
		// Click on draggable shadowbox
		$JQry(".file-browser-draggable-shadowbox").click(function(event) {
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
		
		
		// Droppable
		$JQry(".file-browser-droppable").droppable({
			addClasses: false,
			hoverClass: "bg-info border-info",
			tolerance: "pointer",
			
			accept: function($draggable) {
				var $droppable = $JQry(this);
				var $row = $droppable.closest(".file-browser-table-row");
				var $selectable = $row.closest(".file-browser-table-row-group");
				var $selected = $selectable.find(".ui-selected");
				var targetAcceptedTypes = $droppable.data("accepted-types").split(",");
				var accepted = true;
					
				if ($draggable.hasClass("ui-sortable-helper") || $row.hasClass("file-browser-dragged")) {
					// Prevent drop on sortable or selected element
					accepted = false;
				} else {
					$selected.each(function(index, element) {
						var $element = $JQry(element);
						var sourceType = $element.data("type");
						var match = false;
						
						jQuery.each(targetAcceptedTypes, function(index, targetType) {
							if (sourceType === targetType) {
								match = true;
								return false;
							}
						});
						
						if (!match) {
							accepted = false;
							return false;
						}
					});
				}
				
				return accepted;
			},
			
			drop: function(event, ui) {
				// Source
				var $source = $JQry(ui.helper.context);
				var sourceIdentifiers = $source.data("identifiers");
					
				// Target
				var $target = $JQry(event.target);
				var targetId = $target.closest(".file-browser-table-row").data("id");
					
				// AJAX parameters
				var container = null;
				var options = {
					requestHeaders : [ "ajax", "true", "bilto" ],
					method : "post",
					postBody : "sourceIds=" + sourceIdentifiers + "&targetId=" + targetId,
					onSuccess : function(t) {
						onAjaxSuccess(t, null);
					}
				};
				var url = $target.closest(".file-browser").data("drop-url");
				var eventToStop = null;
				var callerId = null;
				
				directAjaxCall(container, options, url, eventToStop, callerId);
			}
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
