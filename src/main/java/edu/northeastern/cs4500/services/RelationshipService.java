package edu.northeastern.cs4500.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import edu.northeastern.cs4500.models.Relationship;
import edu.northeastern.cs4500.models.User;
import edu.northeastern.cs4500.repositories.RelationshipRepository;

@Service
public class RelationshipService implements IRelationshipService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RelationshipRepository rRepository;

    @Override
    public List<User> getFriends(int userID) {
        return rRepository.getFriends(userID);
    }

	@Override
	public Relationship followUser(Relationship r) {
	      log.info("user with id " + r.getUserid1() + " followed user with id " + r.getUserid2());
	      return rRepository.save(r);
	}
}
