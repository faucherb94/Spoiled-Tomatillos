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
        $.each(json, function(i, movie) {
            var existingRating = $.getJSON(
                `/api/users/${Cookies.get("uid")}/ratings/movies/${movie.imdbID}`);

            var success = function(resp) {
                $("#feed").append(buildMovieCard(movie, i, resp.rating));
            };

            var failure = function(req, status, err) {
                $("#feed").append(buildMovieCard(movie, i, 0));
            };

            var updateRating = function(event, value, caption) {
                var ratingJSON = {
                    rating: value
                };

                var httpMethod = "POST";
                if (existingRating.responseJSON.rating > 0) {
                    httpMethod = "PUT";
                }

                var newRatingRequest = $.ajax({
                    url: "/api/users/" + Cookies.get("uid") + "/ratings/movies/" + movie.imdbID,
                    type: httpMethod,
                    data: JSON.stringify(ratingJSON),
                    contentType: "application/json",
                    dataType: "json"
                });

                var updateSuccess = function(resp) {
                    console.log("rating updated successfully for movie " + movie.imdbID);
                    $("#rating-" + i).rating('update', resp.rating);
                };

                var updateFailure = function(req, status, err) {
                    console.log("rating could not be updated for movie " + movie.imdbID);
                    $("#error").html("Failed rating");
                };

                newRatingRequest.then(updateSuccess, updateFailure);
            };

            var renderStars = function() {
                $('#rating-' + i).rating({
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

/**
 * Build responsive containers for a single search result
 * Return html scaffolding for the card
 */
function buildMovieCard(movie, i, initialRating) {
    var card = "<div id='" + movie.imdbID + "' class='card border-light'>" +
          "<h5 class='card-header'>" + movie.title + "</h5>" +
          "<div class='card-body'>" +
            "<div><a href='" + movie.poster + "'>" +
            "<img class='card-img-left card-float-left' src='" + movie.poster +
            "'/></a></div>" +
            "<div class='card-float-left'><h4 class='card-title'>" + movie.year + "</h4>" +
            "<p class='card-text'>Description - Coming Soon!!</p>" +
            "<input id='rating-" + i + "' name='rating-" + i + "' value='" + initialRating + "'><br>" +
            "<textarea id='review" + i +"' rows='4' columns='50' placeholder='Leave a review...'></textarea><br>" +
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

/**
 * Load Nav
 */
$(document).ready(function(){
    $('#navdiv').load('navbar.html');
    $('#nav-index').addClass('active');
});