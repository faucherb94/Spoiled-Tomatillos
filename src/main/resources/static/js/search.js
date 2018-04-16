/**
 * Get a movie by title
 * For a successful call, build a card for every query result
 * For a failed call, error to the console
 */
function searchByTitle() {
    $("#feed").html("");
    var title = $("#moviesearch").val();

    var searchRequest = $.getJSON(`/api/movies/search?q=${title}`);

    var searchSuccess = function(json) {
        for (var i = 0; i < json.length; i++) {
            $("#feed").append(buildMovieCard(json[i], i));
        }

        var fetchExistingData = true;
        if (!isLoggedIn()) {
            fetchExistingData = false;
        }
        $.each(json, function(i, movie) {
            if (fetchExistingData) {
                var existingRating = $.getJSON(
                    `/api/users/${Cookies.get("uid")}/ratings/movies/${movie.imdbID}`);

                var existingReview = $.getJSON(
                    `/api/users/${Cookies.get("uid")}/reviews/movies/${movie.imdbID}`);

                var success = function(resp) {
                    return [resp.movieID, resp.rating];
                };

                var failure = function(req, status, err) {
                    console.log(err);
                    return [movie.imdbID, 0];
                };

                var revsuccess = function(resp) {
                    return resp.review;
                };

                var revfailure = function(req, status, err) {
                    return 0;
                };

                var putReview = function(review) {
                    if (review == 0) {
                        $("#revarea" + i).html("<textarea id='review" + i +"' rows='4' columns='100' placeholder='Leave a review...'>" +
                            "</textarea><br/>" + "<button onclick='submitReview(this)' class='btn btn-secondary'" +
                            "id='reviewbtn" + i + "' movieid='" + movie.imdbID + "' iter='" + i + "'>Review</button>");
                    }
                    else {
                        $("#revarea" + i).html("<p><strong>Your Review:</strong> " + review + "</p>");
                    }
                }

                existingRating.then(success, failure).always(renderStars);
                existingReview.then(revsuccess, revfailure).always(putReview);
            } else {
                renderStars([movie.imdbID, 0]);
            }
        });
    };

    var searchFailure = function(req, status, err) {
        console.log(status);
    };

    searchRequest.then(searchSuccess, searchFailure);
}

function isLoggedIn() {
    var user = Cookies.get('uid');
    return (typeof user !== 'undefined');
}

function setModalHeader(title, year) {
    var titleAndYear = title + " <span id='year'>(" + year + ")</span>";
    $("#title-and-year").html(titleAndYear);
    $("#year").css({"font-weight": "normal", "font-size": "1.5rem"});
}

var renderStars = function(resp) {
    var disabled = false;
    if (!isLoggedIn()) {
        disabled = true;
    }
    var movieID = resp[0];
    var rating = resp[1];
    var elementId = 'rating-' + movieID;
    $('<input>').attr({
        id: elementId,
        name: elementId,
        value: rating
    }).appendTo('#rating-div-' + movieID);

    $('#'+elementId).rating({
        theme: 'krajee-svg',
        size: 'sm',
        step: '1',
        disabled: disabled,
        showClear: false
    }).on('rating:change', updateRating);
};

var updateRating = function(event, value, caption) {
    var ratingJSON = {
        rating: value
    };

    var existingRating = $($.parseHTML(event.target.outerHTML)).val();
    var elementId = this.id;
    var imdbID = elementId.split('-')[1];

    var httpMethod = "POST";
    if (existingRating > 0) {
        httpMethod = "PUT";
    }

    var newRatingRequest = $.ajax({
        url: "/api/users/" + Cookies.get("uid") + "/ratings/movies/" + imdbID,
        type: httpMethod,
        data: JSON.stringify(ratingJSON),
        contentType: "application/json",
        dataType: "json"
    });

    var updateSuccess = function(resp) {
        console.log("rating updated successfully for movie " + imdbID);
        $(elementId).rating('update', resp.rating);
        if (elementId === "rating-"+imdbID+"-modal") {
            $("#rating-"+imdbID).rating('update', resp.rating);
        }
    };

    var updateFailure = function(req, status, err) {
        console.log("rating could not be updated for movie " + imdbID);
        $("#error").html("Failed rating");
    };

    newRatingRequest.then(updateSuccess, updateFailure);
};

/**
 * Build responsive containers for a single search result
 * Return html scaffolding for the card
 */
function buildMovieCard(movie, i) {
    var posterImg = movie.poster;
    if (posterImg === "N/A") {
        posterImg = "img/image_missing.png";
    }

    var existingReview = $.getJSON(
        `/api/users/${Cookies.get("uid")}/reviews/movies/${movie.imdbID}`);

    var card = "<div id='" + movie.imdbID + "' class='card border-light cardhdr'>" +
                    "<h4 class='card-header'>" +
                        "<a style='color:white' class='movie-link' data-id='" + movie.imdbID + "' href=''>" +
                            movie.title +
                            " <span style='font-weight: normal; font-size: 1.5rem'>(" + movie.year + ")</span>" +
                        "</a>" +
                    "</h4>" +
                    "<div class='card-body cardbdy'>" +
                        "<img class='card-img-left card-float-left' src='" + posterImg +"'/>" +
                        "<div class='card-float-left'>" +
                            "<div id='rating-div-" + movie.imdbID + "'></div>" +
                            "<div id='revarea" + i + "'></div>" +
                        "</div>" +
                    "</div>" +
                "</div>";
    return card;
}

function submitReview(b) {
    var i = $(b).attr("iter");
    var reviewpkg = {
        review: $("#review" + i).val()
    };
    $.ajax({
        url: "/api/users/" + Cookies.get("uid") + "/reviews/movies/" + $(b).attr("movieid"),
        type: "POST",
        data: JSON.stringify(reviewpkg),
        contentType: "application/json",
        dataType: "json"
    }).done(function(json) {
            $("#review" + i).remove();
            $("#reviewbtn" + i).remove();
            $("#revarea" + i).append("<p>Your Review: " + json.review + "</p>");
    }).fail(function(jqxhr, status, err) {
        $("#error").html("Failed review");
    });

}

$(document).on('click', 'a.movie-link', function(e) {
    e.preventDefault();
    getMovie($(this).data('id'));
});

function imgError(image) {
    image.onerror = "";
    image.src = "img/image_missing.png";
    return true;
}

/**
 * Load Nav
 */
$(document).ready(function(){
    $('#navdiv').load('navbar.html');
    $('#nav-index').addClass('active');
});