var loggedinuname = Cookies.get("username");
    if (loggedinuname != null) {
        $("#loggedin").html("Logged in as " + loggedinuname +
            "<button class='nav-item btn btn-small btn-dark' onclick='logoutClick()'>Log Out</button>"
        );
    }

function logoutClick() {
    var GoogleUser = Cookies.get("googleuser");
    Cookies.remove("username");
    Cookies.remove("uid");
    Cookies.remove("googleuser");
    window.location.href='login.html';

}

function searchClick() {
    // window.location.href=`profile.html?id=${$("#searchbox").val()}`;

     $.getJSON(`/api/users/search?q=${$("#searchbox").val()}`)
     .done(function(json) {
        $("#feed").html("");
        for (var r=0; r<json.length; r++) {
            $("#feed").append("<a href='profile.html?id=" + json[r].username + "'>" + json[r].username + "</a><br>");
        }
     });

}