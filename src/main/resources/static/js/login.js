function loginClick() {
    let username = $("#uname").val();
    $.getJSON(`/api/user?name=${username}`)
    .done(function(json) {
        console.log(json);
        Cookies.set("username", json.username);
        window.location.href = "index.html";
    })
    .fail(function(jqxhr, status, err) {
        $("#error").html("Account not found. Please register!");
        showCreate();
    });

}

function showCreate() {
    $("#create").html(
    "<input type='text' placeholder='email' id='new_email'/><br>" +
    "<input type='text' placeholder='First name' id='fn'/><br>" +
    "<input type='text' placeholder='Last name' id='ln'/><br>" +
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
        url: "/api/user/create",
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