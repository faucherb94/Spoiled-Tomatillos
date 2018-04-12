package edu.northeastern.cs4500.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import edu.northeastern.cs4500.models.Relationship;
import edu.northeastern.cs4500.services.IRelationshipService;	

@RestController
@RequestMapping("/api/friends")
public class RelationshipController {

	@Autowired
	private IRelationshipService relationshipService;
    
    /**
     * Follow someone
     */
	@PostMapping
    public Relationship followUser(@Valid @RequestBody Relationship r) {
		return relationshipService.followUser(r);
    }	
    
}
