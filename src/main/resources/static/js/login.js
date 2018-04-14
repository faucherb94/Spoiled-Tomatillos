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
        picture: profile.getImageUrl(),
        email: email,
        username: username
    }
    Cookies.set("googleuser", googleUser);
    console.log(googleUser);
    loginClick(user);
}