var loggedinuname = Cookies.get("username");
    if (loggedinuname != null) {
        $("#loggedin").html("Logged in as " + loggedinuname +
            "<button class='nav-item btn btn-small' style='margin-left:10px' onclick='logoutClick()'>Log Out</button>"
        );
    }

function logoutClick() { Cookies.remove("username"); window.location.href="login.html"; }