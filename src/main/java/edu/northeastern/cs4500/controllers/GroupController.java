package edu.northeastern.cs4500.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import edu.northeastern.cs4500.models.Group;
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

}
