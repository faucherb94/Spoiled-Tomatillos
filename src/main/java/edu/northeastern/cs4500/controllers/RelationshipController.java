package edu.northeastern.cs4500.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;


import edu.northeastern.cs4500.models.Relationship;
import edu.northeastern.cs4500.services.IRelationshipService;

@RestController
@RequestMapping("/api/friends")
public class RelationshipController {

	@Autowired
	private IRelationshipService rService;
	
    /**
     * Get all friends of a user
     */
    @GetMapping("/{id}/friends")
    public ResponseEntity<List<Relationship>> getFriends(@PathVariable(value = "id") int userID) {
    	List<Relationship> friendsList = rService.getFriends(userID);
    			return ResponseEntity.ok(friendsList);
    }
    
    /**
     * Follow someone
     */
	@PostMapping
    public Relationship followUser(@Valid @RequestBody Relationship r) {
		return this.rService.followUser(r);
    }	
    
}
