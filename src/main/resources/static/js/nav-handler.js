var loggedinuname = Cookies.get("username");
    if (loggedinuname != null) {
        $("#loggedin").html("Logged in as " + loggedinuname +
            "<button class='nav-item btn btn-small navbarbtn' onclick='logoutClick()'>Log Out</button>"
        );
    }

function logoutClick() {
    var GoogleUser = Cookies.get("googleuser");
    Cookies.remove("username");
    Cookies.remove("uid");
    GoogleUser.getAuthInstance().signOut().then(function () {
        console.log('User signed out.');
    });
    window.location.href="login.html";
}