function loadProfile(profile) {
    console.log(profile);
    $.getJSON("/api/users/?name=" + profile)
    .done(function(json) {
        $("#fullname").html(json.firstName + " " + json.lastName);
        $("#username").html(json.username);
        $("#hometown").html(json.hometown ? json.hometown : "No hometown set");
        $("#profpic").attr('src', `data:image/png;base64,${json.picture}`);
    }).fail(function (jqxhr, err, resp) {
        console.log(resp);
    });
}

/** If a user is on their page, let them edit their info **/
function allowEdit() {
    $("#buttons").append("<button id='editbutton' class='btn btn-primary'>Edit Your Info</button>");
    $("#editbutton").click(editUserForm);

    $("#buttons").append("<button id='deletebutton' class='btn btn-error'>Delete Your Profile</button>");
    $("#deletebutton").click(deleteUser);
}

function editUserForm() {
    var form = $("#buttons").append("<div id='formdiv'></div>");
    $("#formdiv").append("<input id='firstname-input' type='text'/>");
    $("#firstname-input").attr("placeholder", $("#fullname").html().split(" ")[0]);
    $("#formdiv").append("<br>");
    $("#formdiv").append("<input id='lastname-input' type='text'/>");
    $("#lastname-input").attr("placeholder", $("#fullname").html().split(" ")[1]);
    $("#formdiv").append("<br>");
    $("#formdiv").append("<input id='hometown-input' type='text'/>");
    $("#hometown-input").attr("placeholder", $("#hometown").html());
    $("#formdiv").append("<button id='submit-edit' type='text'>Submit</button>")
    $("#submit-edit").addClass("btn btn-secondary");
    $("#submit-edit").click(editUser);

}

function editUser() {
    var userpkg = {};
    if ($("#firstname-input").val()) { userpkg.firstName=$("#firstname-input").val() };
    if ($("#lastname-input").val()) { userpkg.lastName=$("#lastname-input").val() };
    if ($("#hometown-input").val()) { userpkg.hometown=$("#hometown-input").val() };
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
        console.log(resp);
    });
}

function deleteUser() {
    var del = confirm("Are you sure you want to delete your profile?");
    if (del) { //TODO delete
         }

}