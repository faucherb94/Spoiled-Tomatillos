/**
 * Load Nav
 */
$(document).ready(function(){
    $('#navdiv').load('navbar.html');
    $('#nav-index').addClass('active');

    var success = function(json) {
        for (var i = 0; i < json.length; i++) {
            buildMovieCard(json[i], i);
            $("#" + json[i].imdbID + " .movie-link").data('movie', json[i]);
        }

        var shouldGetRatings = true;
        if (!isLoggedIn()) {
            shouldGetRatings = false;
        }
        $.each(json, function(i, movie) {
            if (shouldGetRatings) {
                var existingRating = $.getJSON(
                    `/api/users/${Cookies.get("uid")}/ratings/movies/${movie.imdbID}`);

                var success = function(resp) {
                    return [resp.movieID, resp.rating];
                };

                var failure = function(req, status, err) {
                    console.log(err);
                    return [movie.imdbID, 0];
                };

                existingRating.then(success, failure).always(renderStars);
            } else {
                renderStars([movie.imdbID, 0]);
            }
        });
    };

    var failure = function(req, status, err) {
        console.log(status);
    };

    $.ajax({
        url: "/api/movies/now-playing",
        type: "get",
        beforeSend: function() {
            $(".loader").show();
        },
        cache: true,
        success: success,
        complete: function(data) {
            $(".loader").hide();
        }
    });
});

function isLoggedIn() {
    var user = Cookies.get('uid');
    return (typeof user !== 'undefined');
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

function buildMovieCard(movie, i) {
    var posterImg = movie.poster;
    if (posterImg === "N/A") {
        posterImg = "img/image_missing.png";
    }

    var card =
        "<div id='" + movie.imdbID + "' class='card border-light cardhdr'>" +
            "<h4 class='card-header'>" +
                "<a style='color:white' class='movie-link' href=''>" +
                    movie.title +
                    " <span style='font-weight: normal; font-size: 1.5rem'>(" + movie.year + ")</span>" +
                "</a>" +
            "</h4>" +
            "<div class='card-body cardbdy'>" +
                "<div class='row'>" +
                    "<div class='col-sm-4'>" +
                        "<img class='card-img-left card-float-left' src='" + posterImg +"'/>" +
                    "</div>" +
                    "<div class='col-sm-8'>" +
                        "<p class='card-text' style='color: black; max-height:150px; overflow-y:scroll'>" + movie.plot + "</p>" +
                        "<div id='rating-div-" + movie.imdbID + "'></div>" +
                        "<textarea id='review" + i +"' rows='4' columns='100' placeholder='Leave a review...'></textarea>" +
                        "<br/>" +
                        "<button onclick='submitReview(this)' class='btn btn-secondary' id='reviewbtn" + i + "' movieid='" + movie.imdbID + "' iter='" + i + "'>" +
                            'Review' +
                        "</button>" +
                    "</div>" +
                "</div>" +
            "</div>" +
        "</div>";

    $("#results").append(card);
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

$(document).on('click', 'a.movie-link', function(e) {
    e.preventDefault();
    populateModal($(this).data('movie'));
});
