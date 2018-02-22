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
            console.log(json[i]);
            $("#feed").append(buildMovieCard(json[i]));
        }
    })
    .fail(function(jqxhr, status, err) {
        console.err(status);
    });
}

/**
 * Build responsive containers for a single search result
 * Return html scaffolding for the card
 */
function buildMovieCard(movie) {
    var card = "<div class='card border-light'>" +
          "<h5 class='card-header'>" + movie.title + "</h5>" +
          "<div class='card-body'>" +
            "<div class='card-img-left'><img class='card-float-left' src='" + movie.poster +
            "' href='" + movie.poster + "'/></div>" +
            "<div class='card-float-left'><h4 class='card-title'>" + movie.year + "</h4>" +
            "<p class='card-text'>Description - Coming Soon!!</p>" +
            "<a href='#' class='btn btn-secondary'>Review (coming soon)</a></div>" +
          "</div>" +
        "</div>";
    return card;
}

/**
 * Load Nav
 */
$(document).ready(function(){
    $('#navdiv').load('navbar.html');
    $('#nav-index').addClass('active');
});