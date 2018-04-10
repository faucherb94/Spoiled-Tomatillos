package edu.northeastern.cs4500.services;

import java.util.List;

import edu.northeastern.cs4500.models.Group;
import edu.northeastern.cs4500.models.GroupMembership;

public interface IGroupService {

    Group create(Group g);

    Group getByID(int id);

    List<Group> getByCreatorID(int creatorID);

    List<Group> getUserGroupMemberships(int userID);

    GroupMembership joinGroup(int groupID, int userID);

}
