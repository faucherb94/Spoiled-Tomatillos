package edu.northeastern.cs4500.services;

import java.util.List;

import edu.northeastern.cs4500.models.Relationship;
import edu.northeastern.cs4500.models.User;

public interface IRelationshipService {

    List<User> getFriends(int userID);

    Relationship followUser(Relationship relationship);
}
