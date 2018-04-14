function getMovie(imdbID) {
    var request = $.getJSON("/api/movies/" + imdbID);

    var success = function(json) {
        populateModal(json);
    };

    var failure = function(resp) {
        console.log(resp);
    };

    request.then(success, failure);
}

function populateModal(json) {
    setModalHeader(json.title, json.year);
    setMovieField("#rated", json.rated);
    setMovieField("#runtime", json.runtime);
    setMovieField("#genres", generateListString(json.genres));
    setMovieField("#released", json.released);

    if (json.poster === "Unavailable") {
        $("#posterImg").attr("src", "img/image_missing.png");
    } else {
        $("#posterImg").attr("src", json.poster);
    }

    setMovieField("#plot", json.plot);
    setRatings(json.ratings);
    setMovieField("#directors", generateListString(json.directors));
    setMovieField("#writers", generateListString(json.writers));
    setMovieField("#actors", generateListString(json.actors));
    setMovieField("#boxOffice", json.boxOffice);
    setMovieField("#production", json.production);
    setMovieField("#countries", generateListString(json.countries));
    setMovieField("#languages", generateListString(json.languages));
    setMovieField("#website", json.website);

    var cloned = $("#rating-" + json.imdbID).clone();
    cloned.attr('id', 'rating-' + json.imdbID + '-modal');
    cloned.appendTo("#rating-div-modal");
    $("#rating-" + json.imdbID + '-modal').rating({
        theme: 'krajee-svg',
        size: 'xs',
        step: '1',
        showClear: false,
        showCaption: false
    }).on('rating:change', updateRating);

    $("#movieModal").modal('show');
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
            case "#boxOffice":
            case "#production":
            case "#countries":
            case "#languages":
            case "#website":
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

function setRatings(ratings) {
    if (ratings.length === 0) {
        $(".ratings-row").hide();
    }

    var missingIMDB = true;
    var missingRT = true;
    var missingMC = true;
    for (var i = 0; i < ratings.length; i++) {
        var r = ratings[i];
        var src = "";
        switch (r.source) {
            case "Internet Movie Database":
                src = "#imdb";
                missingIMDB = false;
                break;
            case "Rotten Tomatoes":
                src = "#rotten-tomatoes";
                missingRT = false;
                break;
            case "Metacritic":
                src = "#metacritic";
                missingMC = false;
                break;
            default:
                console.log("Unexpected rating source " + r.source);
                continue;
        }
        $(src).html(r.value);
    }
    if (missingRT) {
        $("#rotten-tomatoes").parent().hide();
    }

    if (missingIMDB) {
        $("#imdb").parent().hide();
    }

    if (missingMC) {
        $("#metacritic").parent().hide();
    }
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
    $(this).find('#boxOffice').html("");
    $(this).find('#production').html("");
    $(this).find('#countries').html("");
    $(this).find('#languages').html("");
    $(this).find('#website').html("");
    $(this).find('#rotten-tomatoes').html("");
    $(this).find('#imdb').html("");
    $(this).find('#metacritic').html("");
    $(this).find('#rating-div-modal').empty();

    $(this).find(':hidden').show();
});