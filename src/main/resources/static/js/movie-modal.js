function getMovie(imdbID) {
    var request = $.getJSON("/api/movies/" + imdbID);

    var success = function(json) {
        setModalHeader(json.title, json.year);
        setMovieField("#rated", json.rated);
        setMovieField("#runtime", json.runtime);
        setMovieField("#genres", generateListString(json.genres));
        setMovieField("#released", json.released);
        setMovieField("#plot", json.plot);
        setMovieField("#directors", generateListString(json.directors));
        setMovieField("#writers", generateListString(json.writers));
        setMovieField("#actors", generateListString(json.actors));
//        $("#boxOffice").html(json.boxOffice);
//        $("#production").html(json.production);
//        $("#website").html(json.website);

        if (json.poster === "Unavailable") {
            $("#posterImg").attr("src", "img/image_missing.png");
        } else {
            $("#posterImg").attr("src", json.poster);
        }

        $("#rating-" + imdbID).clone().appendTo("#rating-div-modal");

        $("#rating-" + imdbID).rating({
            theme: 'krajee-svg',
            size: 'xs',
            step: '1',
            showClear: false,
            showCaption: false
        }).on('rating:change', updateRating);

        $("#movieModal").modal('show');
    };

    var failure = function(resp) {
        console.log(resp);
    };

    request.then(success, failure);
}

function setMovieField(id, json) {
    var e = $(id);
    if (json === "Unavailable" || json === null) {
        e.hide();
        switch (id) {
            case "#rated":
            case "#runtime":
            case "#genres":
                e.next().hide();
                break;
            case "#released":
                e.prev().hide();
                break;
            case "#writers":
            case "#actors":
            case "#directors":
                e.parent().hide();
                break;
            default:

        }
    } else {
        e.html(json);
    }
}

function setModalHeader(title, year) {
    var titleAndYear = title + " <span id='year'>(" + year + ")</span>";
    $("#title-and-year").html(titleAndYear);
    $("#year").css({"font-weight": "normal", "font-size": "1.5rem"});
}

function generateListString(list) {
    var str = "";
    for (var i = 0; i < list.length; i++) {
        str += list[i];
        if (i !== (list.length-1)) {
            str += ", ";
        }
    }
    return str;
}

function imgError(image) {
    image.onerror = "";
    image.src = "img/image_missing.png";
    return true;
}

$('#movieModal').on('hidden.bs.modal', function(event) {
    $(this).find('#title-and-year').html("");
    $(this).find('#posterImg').attr('src', '');
    $(this).find('#rated').html("");
    $(this).find('#runtime').html("");
    $(this).find('#genres').html("");
    $(this).find('#released').html("");
    $(this).find('#plot').html("");
    $(this).find('#directors').html("");
    $(this).find('#writers').html("");
    $(this).find('#actors').html("");
    $(this).find('#rating-div-modal').empty();

    $(this).find(':hidden').show();
});