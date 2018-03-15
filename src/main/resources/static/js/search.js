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
        }
        $('.kv-ltr-theme-fa-star').rating({
            theme: 'krajee-fa',
            size: 'sm',
            step: '1',
            hoverOnClear: false,
            showClear: false
        }).on('rating:change', function(event, value, caption) {
            console.log(event.currentTarget.name);

        });
    })
    .fail(function(jqxhr, status, err) {
        console.err(status);
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
            "<div><a href='" + movie.poster + "'>" +
            "<img class='card-img-left card-float-left' src='" + movie.poster +
            "'/></a></div>" +
            "<div class='card-float-left'><h4 class='card-title'>" + movie.year + "</h4>" +
            "<p class='card-text'>Description - Coming Soon!!</p>" +
            "<input id='rating" + i + "' name='rating" + i + "' class='kv-ltr-theme-fa-star'><br>" +
            "<textarea id='review" + i +"' rows='4' columns='50'>Leave a review</textarea>" +
            "<input type='button' onclick='submitReview(i)' class='btn btn-secondary'>Review</input></div>" +
          "</div>" +
        "</div>";
    return card;
}

function submitReview(r) {


}

/**
 * Load Nav
 */
$(document).ready(function(){
    $('#navdiv').load('navbar.html');
    $('#nav-index').addClass('active');
});