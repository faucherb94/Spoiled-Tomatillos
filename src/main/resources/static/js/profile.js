function loadProfile(profile) {
    $.getJSON("/api/users/?name=" + profile)
    .done(function(json) {
        $("#fullname").html(json.firstName + " " + json.lastName);
        $("#username").html(json.username);
        $("#hometown").html(json.hometown ? json.hometown : "No hometown set");
        showActivity(json.id);
        $.ajax({
            url: `/api/users/${json.id}/picture`,
            type: "GET",
            contentType: "text/plain"
        }).done(function(json2) {
            $("#profpic").attr("src", `data:image/png;base64,${json2}`);
        });
    }).fail(function (jqxhr, status, err) {
        console.log(err);
    });
}

/** If a user is on their page, let them edit their info **/
function allowEdit() {
    $("#buttons").append("<button id='editbutton' class='btn profilebtn1'>Edit Your Info</button>");
    $("#editbutton").click(editUserForm);

    $("#buttons").append("<button id='deletebutton' class='btn profilebtn2'>Delete Your Profile</button>");
    $("#deletebutton").click(deleteUser);
}

function editUserForm() {
    $("#editbutton").attr("disabled", "disabled");
    var form = $("#buttons").append("<div id='formdiv'></div>");
    $("#formdiv").append("<input id='firstname-input' type='text'/>");
    $("#firstname-input").val($("#fullname").html().split(" ")[0]);
    $("#formdiv").append("<br>");
    $("#formdiv").append("<input id='lastname-input' type='text'/>");
    $("#lastname-input").val($("#fullname").html().split(" ")[1]);
    $("#formdiv").append("<br>");
    $("#formdiv").append("<input id='hometown-input' type='text'/>");
    $("#hometown-input").val($("#hometown").html());
    $("#formdiv").append("<button id='submit-edit' type='text'>Submit</button>")
    $("#submit-edit").addClass("btn btn-secondary");
    $("#submit-edit").click(editUser);

}

function editUser() {
    var userpkg = {};
    if ($("#firstname-input").val()) { userpkg.firstName=$("#firstname-input").val() };
    if ($("#lastname-input").val()) { userpkg.lastName=$("#lastname-input").val() };
    if ($("#hometown-input").val()) { userpkg.hometown=$("#hometown-input").val() };
    userpkg.username = Cookies.get("username");
    $.ajax({
        url: "/api/users/" + Cookies.get("uid"),
        type: "PUT",
        data: JSON.stringify(userpkg),
        contentType: "application/json"
    }).done(function(json) {
        console.log(json);
        $("#formdiv").remove();
        location.reload();
    }).fail(function(jqxhr, status, err) {
        console.log(err);
        $("#formdiv").append("<div id='formerr' style='color:red'></div>")
        $("#formerr").html("Make sure you have entries for all fields.")
    });
}

function deleteUser() {
    var del = confirm("Are you sure you want to delete your profile?");
    if (del) {
        $.ajax({
            url: "/api/users/" + Cookies.get("uid"),
            type: "DELETE"
        }).done(function(json) {
            Cookies.remove("uid");
            Cookies.remove("username");
            window.location.assign("index.html");
        }).fail(function(jqxhr, status, err) {
            console.log(err);
        });
    }
}

function showActivity(uid) {
    $.getJSON(`/api/users/${uid}/activity`)
        .done(function(json) {
            $.each(json, function(i, activity) {
                var renderStars = function() {
                    $('#rating-' + i).rating({
                        theme: 'krajee-svg',
                        size: 'sm',
                        step: '1',
                        showClear: false,
                        readonly: true
                    })
                };
                // TODO $.getJSON() for movie info
                var rating = (activity.type == "rating") ? activity.rating : "Not Rated";
                var review = (activity.type == "review") ? activity.review : "Not reviewed";
                $("#feed").append(buildActivityCard(activity.movieID, i, rating, review));
                renderStars();
            });

        }).fail(function(jqxhr, err, status) {
            console.log(err);
    });

}

function buildActivityCard(movieid, i, rating, review) {
    if (rating == "Not Rated") {rating = 0};
    var card = "<div id='" + movieid + "' class='card border-light'>" +
        "<h5 class='card-header'>" + movieid + "</h5>" +
        "<div class='card-body'>" +
        "<div><a href='" + "Poster" + "'>" +
        "<img class='card-img-left card-float-left' src='" + "Poster" +
        "'/></a></div>" +
        "<div class='card-float-left'><h4 class='card-title'>" + "Year" + "</h4>" +
        "<p class='card-text'>Description - Coming Soon!!</p>" +
        "<input id='rating-" + i + "' name='rating-" + i + "' value='" + rating + "'><br>" +
        "<p><strong>Review: </strong> " + review + "</p>" +
        "</div>" +
        "</div>";
    return card;
}