package edu.northeastern.cs4500.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import edu.northeastern.cs4500.models.Group;
import edu.northeastern.cs4500.models.GroupMembership;
import edu.northeastern.cs4500.services.IGroupService;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private IGroupService groupService;

    @PostMapping
    public ResponseEntity<Group> create(@Valid @RequestBody Group g) {
        if (g.getCreatorID() == 0) {
            throw new IllegalArgumentException("Creator ID cannot be 0");
        }

        if (g.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        Group createdGroup = groupService.create(g);
        return ResponseEntity.ok(createdGroup);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupByID(@PathVariable(value = "id") int id) {
        if (id == 0) {
            throw new IllegalArgumentException("Group ID cannot be 0");
        }

        Group group = groupService.getByID(id);
        return ResponseEntity.ok(group);
    }

    @GetMapping
    public ResponseEntity<List<Group>> getGroupsByCreatorID(
            @RequestParam(value = "creator-id") int creatorID) {

        List<Group> groups = groupService.getByCreatorID(creatorID);
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/{id}/users/{user-id}")
    public ResponseEntity<GroupMembership> joinGroup(@PathVariable(value = "id") int groupID,
                                                     @PathVariable(value = "user-id") int userID) {
        GroupMembership groupMembership = groupService.joinGroup(groupID, userID);
        return ResponseEntity.ok(groupMembership);
    }

}
