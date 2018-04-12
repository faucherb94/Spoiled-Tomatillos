package edu.northeastern.cs4500.services;

import java.util.List;

import edu.northeastern.cs4500.models.Relationship;

public interface IRelationshipService {

    List<Relationship> getFriends(int userID);
    
    Relationship followUser(Relationship relationship);
}
