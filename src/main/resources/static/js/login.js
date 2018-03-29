function loginClick(user) {
    $.getJSON(`/api/users?name=${user.username}`)
    .done(function(json) {
        console.log(json);
        Cookies.set("username", json.username);
        Cookies.set("uid", json.id);
        window.location.href = "index.html";
    })
    .fail(function(jqxhr, status, err) {
        createUser(user);
    });

}

/** function showCreate() {
    $("#create").html(
    "<input style='margin-bottom:15px;' type='text' placeholder='email' id='new_email'/><br>" +
    "<input style='margin-bottom:15px;' type='text' placeholder='First name' id='fn'/><br>" +
    "<input style='margin-bottom:15px;' type='text' placeholder='Last name' id='ln'/><br>" +
    "<button class='btn btn-secondary btn-sm' onclick='createUser()'>Register</button>"
    );
} **/

function createUser(newuser) {
    $.ajax({
        url: "/api/users",
        type: "POST",
        data: JSON.stringify(newuser),
        contentType: "application/json",
        dataType: "json"
    }).done(function(json) {
        loginClick(newuser);
    }).fail(function(jqxhr, status, err) {
        if (jqxhr.responseJSON.apierror.status == "INTERNAL_SERVER_ERROR") {
            $("#error").html("Server error - contact administrator");
        }
        else {
            $("#error").html("User already exists - try a different username or email");
        }
    });
}

function onSignIn(googleUser) {
    var profile = googleUser.getBasicProfile();

    var email = profile.getEmail();
    var username = email.split("@")[0];
    var user = {
        firstName: profile.getGivenName(),
        lastName: profile.getFamilyName(),
        propic: profile.getImageUrl(),
        email: email,
        username: username
    }
    Cookies.set("googleuser", googleUser)
    console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
    console.log('Name: ' + profile.getName());
    console.log('Image URL: ' + profile.getImageUrl());
    console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
    loginClick(user);
}