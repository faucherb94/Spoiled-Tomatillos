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
        $.each(json, function(i, movie) {
            var existingRating = $.getJSON(
                `/api/users/${Cookies.get("uid")}/ratings/movies/${movie.imdbID}`);

            var success = function(resp) {
                return resp.rating;
            };

            var failure = function(req, status, err) {
                return 0;
            };

            var renderStars = function(rating) {
                var elementId = 'rating-' + movie.imdbID;
                $('<input>').attr({
                    id: elementId,
                    name: elementId,
                    value: rating
                }).appendTo('#rating-div-' + movie.imdbID);

                $('#'+elementId).rating({
                    theme: 'krajee-svg',
                    size: 'sm',
                    step: '1',
                    showClear: false
                }).on('rating:change', updateRating);
            };

            existingRating.then(success, failure).always(renderStars);
        });
    };

    var searchFailure = function(req, status, err) {
        console.log(status);
    };

    searchRequest.then(searchSuccess, searchFailure);
}

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

    var card = "<div id='" + movie.imdbID + "' class='card border-light'>" +
          "<h4 class='card-header'><a class='movie-link' data-id='" +
          movie.imdbID + "' href=''>" + movie.title + "</a></h4>" +
          "<div class='card-body'>" +
            "<div><img class='card-img-left card-float-left' src='" + posterImg +
            "' alt='Poster Unavailable'/></div>" +
            "<div class='card-float-left'><h4 class='card-title'>" + movie.year + "</h4>" +
            "<p class='card-text'>Description - Coming Soon!!</p>" +
            "<div id='rating-div-" + movie.imdbID + "'></div>" +
            "<textarea id='review" + i +"' rows='4' columns='50' placeholder='Leave a review...'></textarea><br/>" +
            "<button onclick='submitReview(this)' class='btn btn-secondary' id='reviewbtn" + i + "' movieid='" + movie.imdbID + "' iter='" + i + "'>Review</button></div>" +
          "</div>" +
        "</div>";
    return card;
}

function submitReview(b) {
    var reviewpkg = {
        review: $("#review" + $(b).attr("iter")).val()
    };
    $.ajax({
        url: "/api/users/" + Cookies.get("uid") + "/reviews/movies/" + $(b).attr("movieid"),
        type: "POST",
        data: JSON.stringify(reviewpkg),
        contentType: "application/json",
        dataType: "json"
    }).done(function(json) {
        $.each(json, function(movie, i) {
            $("#review" + i).text($("#review" + $(b).attr("iter")).val());
            $("#reviewbtn" + i).remove();
        });
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