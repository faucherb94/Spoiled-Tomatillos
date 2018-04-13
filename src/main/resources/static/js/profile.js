function loadProfile(profile, isUsersPage) {
    $.getJSON("/api/users/?name=" + profile)
    .done(function(json) {
        $("#profpic").attr("src", json.picture);
        $("#fullname").html(json.firstName + " " + json.lastName);
        $("#username").html(json.username);
        $("#hometown").html(json.hometown ? json.hometown : "No hometown set");
        $("#friends").html("Loading friends...");
        if (!isUsersPage) {
            // check if friends
            var friends = true;
            if (friends) {
                showActivity(json.id);
                // showRemoveFriendButton();
            } else {
                showFriendButton();
            }
        } else {
            showActivity(json.id);
        }

        /** $.ajax({
            url: `/api/users/${json.id}/friends`,
            type: "GET"
        }).done(function(friendslist) {
            $("#friends").html(friendslist);
            Show friends as url to their page?
        }); **/
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
                $.getJSON(`/api/movies/${activity.movieID}`)
                    .done(function(moviedata){
                        var rating = (activity.type == "rating") ? activity.rating : "Not Rated";
                        var review = (activity.type == "review") ? activity.review : "Not reviewed";
                        $("#feed").append(buildActivityCard(moviedata, i, rating, review));
                        renderStars();
                    });

            });

        }).fail(function(jqxhr, err, status) {
            console.log(err);
    });

}

function buildActivityCard(moviedata, i, rating, review) {
    if (rating == "Not Rated") {rating = 0};
    var card = "<div id='" + moviedata.imdbID + "' class='card border-light cardhdr'>" +
        "<h5 class='card-header' style='color:white'>" + moviedata.title + "</h5>" +
        "<div class='card-body cardbdy'>" +
        "<div><a href='" + moviedata.poster + "'>" +
        "<img class='card-img-left card-float-left' src='" + moviedata.poster +
        "'/></a></div>" +
        "<div class='card-float-left'><h4 class='card-title'>" + moviedata.year + "</h4>" +
        "<p class='card-text'>" + moviedata.plot + "</p>" +
        "<input id='rating-" + i + "' name='rating-" + i + "' value='" + rating + "'><br>" +
        "<p><strong>Review: </strong> " + review + "</p>" +
        "</div>" +
        "</div>";
    return card;
}

function showFriendButton() {
    $("#feed").append("<button class='btn profilebtn1' onclick='addFriend()'>Add Friend</button>");
}

function addFriend() {
    // need to add the post mapping here
}