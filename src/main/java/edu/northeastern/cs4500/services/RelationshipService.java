package edu.northeastern.cs4500.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import edu.northeastern.cs4500.models.Group;
import edu.northeastern.cs4500.models.GroupMembership;
import edu.northeastern.cs4500.models.Relationship;
import edu.northeastern.cs4500.models.User;
import edu.northeastern.cs4500.repositories.GroupMembershipRepository;
import edu.northeastern.cs4500.repositories.GroupRepository;
import edu.northeastern.cs4500.repositories.RelationshipRepository;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

@Service
public class RelationshipService implements IRelationshipService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RelationshipRepository rRepository;

	@Override
	public List<Relationship> getFriends(int userID) {
		log.info("user with id " + userID + "accessing friends list");
		return this.rRepository.getFriends(userID);
	}

	@Override
	public Relationship followUser(Relationship r) {
	      log.info("user with id " + r.getUserid1() + " followed user with id " + r.getUserid2());
	      return rRepository.save(r);
	}
}
