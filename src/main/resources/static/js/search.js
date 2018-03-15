/**
 * Get a movie by title
 * For a successful call, build a card for every query result
 * For a failed call, error to the console //TODO actually handle error :)
 */
function searchByTitle() {
    $("#feed").html("");
    var title = $("#moviesearch").val();
    $.getJSON(`/api/search?q=${title}`)
    .done(function(json) {
        for (var i=0; i < json.length; i++) {
            $("#feed").append(buildMovieCard(json[i], i));
            var movieid = json[i].imdbID;
            $('#rating' + i).rating({
                theme: 'krajee-svg',
                size: 'sm',
                step: '1',
                hoverOnClear: false,
                showClear: false
            }).on('rating:change', function(event, value, caption) {
                var ratingpkg = {
                    movieID: movieid,
                    userID: Cookies.get("uid"),
                    rating: value
                };
                $.ajax({
                    url: "/api/movie/rating",
                    type: "POST",
                    data: JSON.stringify(ratingpkg),
                    contentType: "application/json",
                    dataType: "json"
                }).done(function(json) {
                    console.log("Rating Successful");
                }).fail(function(jqxhr, status, err) {
                    $("#error").html("Failed rating");
                });
            });
            $.getJSON(`/api/movie/rating?userID=${Cookies.get("uid")}&movieID=${movieid}`)
                .done(function(resp) {
                    console.log(resp.rating);
                    $("#rating" + i).rating("update", resp.rating);
            });
        }

    })
    .fail(function(jqxhr, status, err) {
        console.error(status);
    });
}

/**
 * Build responsive containers for a single search result
 * Return html scaffolding for the card
 */
function buildMovieCard(movie, i) {
    var card = "<div class='card border-light'>" +
          "<h5 class='card-header'>" + movie.title + "</h5>" +
          "<div class='card-body'>" +
            "<span hidden id='mid-rating" + i + "'>" + movie.imdbID + "</span>" +
            "<div><a href='" + movie.poster + "'>" +
            "<img class='card-img-left card-float-left' src='" + movie.poster +
            "'/></a></div>" +
            "<div class='card-float-left'><h4 class='card-title'>" + movie.year + "</h4>" +
            "<p class='card-text'>Description - Coming Soon!!</p>" +
            "<input id='rating" + i + "' name='rating" + i + "' class='kv-ltr-theme-svg-star'><br>" +
            "<textarea id='review" + i +"' rows='4' columns='50' placeholder='Leave a review...'></textarea><br>" +
            "<button onclick='submitReview(" + movie.imdbID + ")' class='btn btn-secondary'>Review</button></div>" +
          "</div>" +
        "</div>";
    return card;
}

function submitReview(movieID) {
    console.log(movieID);

}

/**
 * Load Nav
 */
$(document).ready(function(){
    $('#navdiv').load('navbar.html');
    $('#nav-index').addClass('active');
});