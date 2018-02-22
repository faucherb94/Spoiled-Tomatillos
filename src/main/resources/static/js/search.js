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

function buildMovieCard(movie) {
    var card = "<div class='card border-light'>" +
      "<h5 class='card-header'>" + movie.title + "</h5>" +
      "<div class='card-body'>" +
        "<div style='float:left;margin-right:20px;'><img src='" + movie.poster + "' style='width:150px;' " +
        "href='" + movie.poster "'/></div>" +
        "<div style='float:left'><h4 class='card-title'>"+ movie.year + "</h4>" +
        "<p class='card-text'>Description - Coming Soon!!</p>" +
        "<a href='#' class='btn btn-secondary'>Review (coming soon)</a></div>" +
      "</div>" +
    "</div>";
    return card;
}