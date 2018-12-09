var wall_url = "http://localhost:8080/account/rest/tiles/getWallTiles";
var like_url = "http://localhost:8080/account/rest/tiles/voteUp";
var likecancel_url = "http://localhost:8080/account/rest/tiles/voteUp";
var favorite_url = "http://localhost:8080/account/rest/tiles/markFavourite";
var favoritecancel_url = "http://localhost:8080/account/rest/tiles/markFavourite";
var idea_url = "http://localhost:8080/account/rest/tiles/getDetails";
var report_url = "report.json";
var add_idea_images_limit = 10;

var wall_colors_1x1 = [ '#01243B', '#336699', '#5288DB' ]; // , '#9DA7B2',
															// '#C5C5C5'
var wall_ideas = [];
var wall_squares = [];
wall_squares[0] = [];
var wall_cols_count;
var wall_rows_count = 1;
var wall_padding = 10;
var wall_width_old;
var wall_page = 1;

// CSRF 403 forbidden
$(function() {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
});

$(function() {

	// ПОСЛЕ ВНЕДРЕНИЯ УДАЛИТЬ ЭТИ СТРОКИ ===>
	$(".header").load("./header.html");
	$(".footer").load("./footer.html");
	$(".idea").load("./details.html", function() {
		ideaFire(); // ... А ЭТОТ ВЫЗОВ ФУНКЦИИ ВЫНЕСТИ НАРУЖУ!
	});
	// <===

	/*
	 * Set minimal width for content area (because footer must be in the very
	 * bottom of screen):
	 */
	stickFooterOnLoad();
	$(window).on('resize', function() {
		stickFooter();
	});

	/*
	 * Slider at home page:
	 */
	$('.carousel').click({
		dots : true,
		prevArrow : $('.carousel-arrow.prev'),
		nextArrow : $('.carousel-arrow.next'),
		mobileFirst : true,
		autoplay : true,
		autoplaySpeed : 5000,
		lazyLoad : 'progressive'

	});

	// At Home and Favorotes pages...
	if ($('.page-home').length || $('.page-favorites').length) {
		// ... add animations (square and magnifying glass)...
		$(".home-idea").magnify({
			size : '2x'
		}).on('click', function() {
			// ... show idea info on click
			ideaDetailsDisplay($(this).data('ideaId'));
		});
	}

	// At the wall page only:
	if ($('#wall').length) {
		wallInit(); // Init wall
		wallGetData(); // Get first page of ideas

		// Reorder blocks on every Bootstrap breakpoint:
		$(window).on('resize', function() {
			var wall_width = $("#wall").width();
			if (wall_width_old != wall_width) {
				wallInit();
				wallFill(wall_ideas);
			}
		});

		// Show/hide red "Add idea" button near the page scrollbar:
		$(window).on('scroll', function() {
			if ($(window).scrollTop() > 300) {
				$('#add_idea_fixed').show();
			} else {
				$('#add_idea_fixed').hide();
			}
		});

		// Get the next page of ideas:
		$("#wall-more").on('click', function() {
			wallGetData();
		});
	}

	// At the "Add idea" page only:
	if ($('.page-add-idea').length) {
		$(".add-idea-image").on('change', function() {
			addIdeaImageBindEvents($(this));
		})
	}
});

/*
 * Get the next page of ideas and display it:
 */
var wallGetData = function() {
	$("#wall-more").hide();
	$("#wall-loader").show();
	$.get({
		url : wall_url,
		data : {
			page : wall_page
		},
		dataType : "json",
	}).done(function(json) {
		var new_ideas = JSON.parse(JSON.stringify(json));
		wallFill(new_ideas);
		$("#wall-loader").hide();
		$("#wall-more").css('display', 'block');
		wall_ideas = wall_ideas.concat(new_ideas);
		wall_page++;
	}).fail(function(xhr, status, errorThrown) {
		alert("Sorry, there was a problem!");
		console.log("Error: " + errorThrown);
		console.log("Status: " + status);
		console.dir(xhr);
	}).always(function(xhr, status) {
		console.log("Request completed!");
	});
}

/*
 * Adds single idea on wall:
 */
var addIdea = function(idea, row, col) {
	var left = wall_block_width * col + wall_padding * col;
	var top = wall_block_width * row + wall_padding * row;
	var random_int = Math.floor(Math.random() * 3);
	var color = '';
	if (idea.type == '1x1') {
		color = ' background-color: ' + wall_colors_1x1[random_int] + '; ';
	}
	var e = '<a class="wall-idea type' + idea.type + '" style="left: ' + left
			+ 'px; top: ' + top + 'px; ' + color
			+ ' " ><div class="wall-idea-wrapper"><div class="wall-idea-img" ';
	if (idea.images[0] != undefined) {
		e += 'style="background: url(' + idea.images[0].url + ');"';
	}
	e += '></div><div class="wall-idea-details" ><div class="wall-idea-details-wrapper"><h3>'
			+ idea.title
			+ '</h3><div class="date">'
			+ idea.date
			+ ' by '
			+ idea.author + '</div>' + idea.intro + '</div></div></div></a>';
	var $el = $(e);
	$el.hide().appendTo('#wall').fadeIn(1000).on('click', function() {
		ideaDetailsDisplay();
	});
	// Adding animations (square and magnifying glass):
	$el.find('.wall-idea-wrapper').magnify({
		size : '3x'
	});
}

/*
 * Initialize wall:
 */
var wallInit = function() {
	// Reset all important values and global variables:
	$("#wall").html('');
	$("#wall").css('height', false);
	wall_squares = [];
	wall_squares[0] = [];
	wall_rows_count = 1;

	// Set number of columns and wall width for each screen dimension:
	var wall_width = $("#wall").width();
	wall_width_old = wall_width;
	// Formula of counting: wall_block_width = (wall_width -
	// wall_padding*(wall_cols_count-1)) / wall_cols_count
	if (wall_width >= 1140) {
		wall_cols_count = 6;
		wall_block_width = 181.66;
	} else if (wall_width >= 940) {
		wall_cols_count = 5;
		wall_block_width = 180;
	} else if (wall_width >= 720) {
		wall_cols_count = 4;
		wall_block_width = 172.5;
	} else if (wall_width >= 540) {
		wall_cols_count = 3;
		wall_block_width = 173.33;
	} else if (wall_width >= 360) {
		wall_cols_count = 2;
		wall_block_width = 175;
	} else {
		wall_cols_count = 1;
		wall_block_width = 180;
	}
}

/*
 * Fill the wall by ideas
 */
var wallFill = function(ideas) {
	$
			.each(
					ideas,
					function(i, idea) {
						var row = 0;
						$finished = false;
						while (!$finished) {
							if (typeof wall_squares[row + 1] == 'undefined') {
								wall_squares[row + 1] = [];
							}
							for (col = 0; col < wall_cols_count; col++) {
								if (typeof wall_squares[row][col] == 'undefined') {
									switch (idea.type) {
									case "2x2":
										if (wall_cols_count == 1
												|| (col < wall_cols_count - 1
														&& typeof wall_squares[row + 1][col] == 'undefined'
														&& typeof wall_squares[row][col + 1] == 'undefined' && typeof wall_squares[row + 1][col + 1] == 'undefined')) {
											addIdea(idea, row, col);
											wall_squares[row][col] = true;
											wall_squares[row + 1][col] = true;
											wall_squares[row][col + 1] = true;
											wall_squares[row + 1][col + 1] = true;
											$finished = true;
										}
										break;
									case "2x1":
										if (wall_cols_count == 1
												|| (col < wall_cols_count - 1 && typeof wall_squares[row][col + 1] == 'undefined')) {
											addIdea(idea, row, col);
											wall_squares[row][col] = true;
											wall_squares[row][col + 1] = true;
											$finished = true;
										}
										break;
									case "1x2":
										if (typeof wall_squares[row + 1][col] == 'undefined') {
											addIdea(idea, row, col);
											wall_squares[row][col] = true;
											wall_squares[row + 1][col] = true;
											$finished = true;
										}
										break;
									case "1x1":
										addIdea(idea, row, col);
										wall_squares[row][col] = true;
										$finished = true;
										break;
									}

								}
								if ($finished)
									break;
							}
							row++;

							if (row > 2 * (wall_ideas.length + ideas.length))
								break; // Делаем проверку на всякий случай, в
										// качестве защиты от зацикливания
						}
						if (row >= wall_rows_count)
							wall_rows_count = row;
					});
	$("#wall").height(
			wall_block_width * wall_rows_count + wall_padding
					* (wall_rows_count - 1));
}

/*
 * "Like" button click handler
 */
var like = function(idea_id) {
	$(".idea-like[data-idea-id=" + idea_id + "]")
			.removeClass('idea-like-ready').addClass('idea-like-progress')
	$.post({
		url : like_url,
		data : {
			"idea_id" : idea_id
		}
	}).done(
			function(count) {
				var $like_btn = $(".idea-like[data-idea-id=" + idea_id + "]");
				$like_btn.removeClass('idea-like-progress').addClass(
						'idea-like-done').find("span").text(count);
				$like_btn.off('click').on('click', function() {
					likeCancel($(this).attr('data-idea-id'));
				});
			});
}

/*
 * "Like" cancelling handler
 */
var likeCancel = function(idea_id) {
	$(".idea-like[data-idea-id=" + idea_id + "]").removeClass('idea-like-done')
			.addClass('idea-like-progress')
	$.post({
		url : likecancel_url,
		data : {
			idea_id : idea_id
		}
	}).done(
			function() {
				var $like_btn = $(".idea-like[data-idea-id=" + idea_id + "]");
				$like_btn.removeClass('idea-like-progress').addClass(
						'idea-like-ready').find("span").text('Like');
				$like_btn.off('click').on('click', function() {
					like($(this).attr('data-idea-id'));
				});
			});
}

/*
 * "Favorite" button click handler
 */
var favorite = function(idea_id) {
	$(".idea-favorite[data-idea-id=" + idea_id + "]").removeClass(
			'idea-favorite-ready').addClass('idea-favorite-progress')
	$.post({
		url : favorite_url,
		data : {
			idea_id : idea_id
		}
	}).done(
			function(count) {
				var $favorite_btn = $(".idea-favorite[data-idea-id=" + idea_id
						+ "]");
				$favorite_btn.removeClass('idea-favorite-progress').addClass(
						'idea-favorite-done').find("span").text(count);
				$favorite_btn.off('click').on('click', function() {
					favoriteCancel($(this).attr('data-idea-id'));
				});
			});
}

/*
 * "Favorite" cancelling handler
 */
var favoriteCancel = function(idea_id) {
	$(".idea-favorite[data-idea-id=" + idea_id + "]").removeClass(
			'idea-favorite-done').addClass('idea-favorite-progress')
	$.post({
		url : favoritecancel_url,
		data : {
			idea_id : idea_id
		}
	}).done(
			function() {
				var $favorite_btn = $(".idea-favorite[data-idea-id=" + idea_id
						+ "]");
				$favorite_btn.removeClass('idea-favorite-progress').addClass(
						'idea-favorite-ready').find("span").text('Favourite');
				$favorite_btn.off('click').on('click', function() {
					favorite($(this).attr('data-idea-id'));
				});
			});
}

/*
 * Plygin for adding of mugnifying amination (square and magnifying glass):
 * options.size - size of icon (lg, 2x, 3x, 4x)
 */
$.fn.magnify = function(options) {
	this
			.addClass('magnify')
			.append(
					'<div class="magnify-square"></div><div class="magnify-icon"><i class="fa fa-search fa-'
							+ options.size + '"></i></div>');
	return this;
};

/*
 * Start all events, handlers, animations, etc. for idea container after its
 * adding
 */
var ideaFire = function() {
	/*
	 * Slider at idea page:
	 */
	$('.idea-slider').slick({
		slidesToShow : 4,
		slidesToScroll : 4,
		prevArrow : $('.idea-slider-arrow.prev'),
		nextArrow : $('.idea-slider-arrow.next'),
		dots : false,
		mobileFirst : true,
		focusOnSelect : true
	});
	// Change large image in idea slider:
	$('.idea-slider').on('afterChange', function(event, slick, currentSlide) {
		$(".idea-full-image div").hide();
		var image = $(".idea-full-image div").get(currentSlide);
		$(image).fadeIn();
	});

	// Keep previews to be square:
	previewsBeSquare();
	$(window).on('resize', function() {
		previewsBeSquare();
	});

	// Adding animations (square and magnifying glass):
	$('.idea-slider-item').magnify({
		size : '2x'
	});

	// Like button handler:
	$('.idea-like-ready').on('click', function() {
		like($(this).attr('data-idea-id'));
	});
	$('.idea-like-done').on('click', function() {
		likeCancel($(this).attr('data-idea-id'));
	});

	// Favorite button handler:
	$('.idea-favorite-ready').on('click', function() {
		favorite($(this).attr('data-idea-id'));
	});
	$('.idea-favorite-done').on('click', function() {
		favoriteCancel($(this).attr('data-idea-id'));
	});

	// Comments events binding:
	commentEventsBind();
}

/*
 * Keep previews to be square:
 */
var previewsBeSquare = function() {
	$('.idea-slider-item').each(function() {
		$(this).height($(this).width());
	});
	$('.idea-slider-arrow').css('bottom',
			$('.idea-slider-item').width() / 2 - 12);
}

/*
 * Getting idea details from server and display in popup
 */
var ideaDetailsDisplay = function(idea_id) {
	$.get({
		url : idea_url,
		data : {
			"idea_id" : idea_id
		}
	}).done(function(idea_html) {
		$.fancybox.open('<div class="container idea">' + idea_html + '</div>');
		ideaFire();
	});
}

/*
 * Binding all events, handlers, animations, etc. for comment container
 */
var commentEventsBind = function() {
	$('.comment-reply').on(
			'click',
			function() {
				var $comment_links = $(this).parent();
				if ($comment_links.siblings('.comment-form').length == 0) {
					$('.comment-form.example').clone().removeClass('example')
							.hide().insertAfter($comment_links).slideDown();
				} else {
					var $comment_form = $comment_links
							.siblings('.comment-form');
					if ($comment_form.is(':visible')) {
						$comment_form.slideUp();
						$comment_links.find('a').fadeOut(200);
					} else {
						$comment_form.slideDown();
					}
				}
			});
	$('.comment-wrapper').hover(function() {
		if ($(window).width() >= 992) { // min-width: $md-min
			$(this).find('> .comment-links > a').fadeIn(200);
		}
	}, function() {
		if ($(window).width() >= 992) { // min-width: $md-min
			var $comment_form = $(this).find('> .comment-form');
			if ($comment_form.length == 0 || $comment_form.is(':hidden')) {
				$(this).find('> .comment-links > a').fadeOut(200);
			}
		}
	});
	$('.comment-report')
			.on(
					'click',
					function() {
						if (confirm('Do you want to report this comment to the administration?')) {
							var comment_id = $(this).attr('data-comment-id');
							$.post({
								url : report_url,
								data : {
									comment_id : comment_id
								}
							}).done(function() {
								alert('Report sent. Thank you!');
							})
						}
					});
}

/*
 * Set minimal width for content area (because footer must be in the very bottom
 * of screen)
 */
var stickFooter = function() {
	if ($(".content .container").length == 1) {
		var content_min_height = $(window).height() - $(".header").height()
				- $(".footer").height();
		$(".content .container").css('min-height', content_min_height);
	}
}

var stickFooterOnLoad = function() {
	$(window).on("load", function() {
		if ($(".header").height() == 0 || $(".footer").height() == 0) {
			setTimeout(function() {
				stickFooter();
			}, 1000);
		} else {
			stickFooter();
		}
	})
}

var addIdeaImageBindEvents = function($el) {
	if ($($el).val() != ""
			&& $($el).siblings("input.add-idea-image").length < add_idea_images_limit - 1) {

		$('.add-idea-images-wrapper')
				.append(
						'<input type="file" class="form-control add-idea-image" name="image[]" /><br />');
		$(".add-idea-image").on('change', function() {
			addIdeaImageBindEvents($(this));
		})
	}
}