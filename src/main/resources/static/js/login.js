function loginClick() {
    let username = $("#uname").val();
    $.getJSON(`/api/users?name=${username}`)
    .done(function(json) {
        console.log(json);
        Cookies.set("username", json.username);
        Cookies.set("uid", json.id);
        window.location.href = "index.html";
    })
    .fail(function(jqxhr, status, err) {
        $("#error").html("Account not found. Please register!");
        showCreate();
    });

}

function showCreate() {
    $("#create").html(
    "<input style='margin-bottom:15px;' type='text' placeholder='email' id='new_email'/><br>" +
    "<input style='margin-bottom:15px;' type='text' placeholder='First name' id='fn'/><br>" +
    "<input style='margin-bottom:15px;' type='text' placeholder='Last name' id='ln'/><br>" +
    "<button class='btn btn-secondary btn-sm' onclick='createUser()'>Register</button>"
    );
}

function createUser() {
    var user = {
        username: $("#uname").val(),
        firstName: $("#fn").val(),
        lastName: $("#ln").val(),
        email: $("#new_email").val(),
        role: "default"
    };
    $.ajax({
        url: "/api/users",
        type: "POST",
        data: JSON.stringify(user),
        contentType: "application/json",
        dataType: "json"
    }).done(function(json) {
        loginClick();
    }).fail(function(jqxhr, status, err) {
        $("#error").html("User already exists - try a different username or email");
    });
}