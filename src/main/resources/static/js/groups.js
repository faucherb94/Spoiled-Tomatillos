function showGroups() {
    $.getJSON(`/api/groups?creator-id=${Cookies.get("uid")}`)
        .done(function(resp) {
            for (var g=0; g<resp.length; g++) {
                $("#owned-groups-list").append("<a href='?gid=" + resp[g].id +
                                        "' class='list-group-item list-group-item-action' id='" + resp[g].id +
                                        "'>" + resp[g].name + "</a>");
            }

        })
        .fail(function(jqxhr, err, msg){
            console.log(msg);
    });
    $.getJSON(`/api/users/${Cookies.get("uid")}/groups`)
        .done(function(resp2) {
            for (var h=0; h<resp2.length; h++) {
                $("#member-groups-list").append("<a href='?gid='" + resp2[h].id +
                    "' class='list-group-item list-group-item-action' id='" + resp2[h].id + "'>" +
                    resp2[h].name + "</a>");
            }

        })
        .fail(function(jqxhr, err, msg){
            console.log(msg);
    });
}

function loadGroup(gid) {
    $.getJSON(`/api/groups/${gid}`)
        .done(function(resp) {
            $("#gname").html(resp.name);
            $("#description").html(resp.description);
            $("#createdby").html(resp.creatorID);
            $("#show-group-div").removeAttr("hidden");
            $("#" + resp.id).addClass("active");

        })
        .fail(function(jqxhr, err, msg){
            console.log(msg);
        });
}

function createGroup() {
    $("#show-group-div").removeAttr("hidden");
    $("#show-group-div").append("<input type='text' placeholder='Group Name' id='addgname'><br>");
    $("#show-group-div").append("<input type='text' placeholder='Group Description' id='addgdesc'><br>");
    $("#show-group-div").append("<button class='btn btnpadding' onClick='submitCreate()'>Submit</button>");
}

function submitCreate() {
    var group = {
        creatorID: Cookies.get("uid"),
        name: $("#addgname").val(),
        description: $("#addgdesc").val()
    }
    $.ajax({
        url: "/api/groups",
        type: "POST",
        data: JSON.stringify(group),
        contentType: "application/json"
    }).done(function(json) {
        console.log(json);
        location.reload();
    }).fail(function(jqxhr, status, err) {
        console.log(err);
        $("#show-group-div").html("Error creating group. Refresh the page and try again");
    });
}