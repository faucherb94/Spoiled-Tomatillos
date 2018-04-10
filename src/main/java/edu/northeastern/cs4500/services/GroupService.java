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
import edu.northeastern.cs4500.models.User;
import edu.northeastern.cs4500.repositories.GroupMembershipRepository;
import edu.northeastern.cs4500.repositories.GroupRepository;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

@Service
public class GroupService implements IGroupService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String DEFAULT_GROUP_PICTURE_PATH = "static/img/image_missing.png";
    private static final String GROUP_NOT_FOUND = "group id {} not found";

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMembershipRepository groupMembershipRepository;

    @Override
    public Group create(Group g) {
        byte[] bytes = getDefaultGroupPicture();
        if (bytes.length > 0) {
            g.setPicture(bytes);
        }
        Group createdGroup = groupRepository.save(g);
        log.info("Group ID {} created by user ID {}",
                createdGroup.getId(), createdGroup.getCreatorID());
        return createdGroup;
    }

    @Override
    public Group getByID(int id) {
        Group group = groupRepository.findOne(id);
        if (group == null) {
            log.error(GROUP_NOT_FOUND, id);
            throw new ResourceNotFoundException(User.class, "id", Integer.toString(id));
        }
        log.info("group id {} retrieved", id);
        return group;
    }

    @Override
    public List<Group> getByCreatorID(int creatorID) {
        log.info("user ID {}'s groups fetched", creatorID);
        return groupRepository.findByCreatorID(creatorID);
    }

    @Override
    public List<Group> getUserGroupMemberships(int userID) {
        log.info("groups that user id {} is a member of fetched", userID);
        return groupRepository.findByUserMembership(userID);
    }

    @Override
    public GroupMembership joinGroup(int groupID, int userID) {
        List<Group> createdGroups = getByCreatorID(userID);
        for (Group g : createdGroups) {
            if (g.getId() == groupID) {
                throw new IllegalArgumentException(
                        "User " + userID + " is the creator of this group. Cannot join");
            }
        }
        GroupMembership groupMembership = new GroupMembership(groupID, userID);
        return groupMembershipRepository.save(groupMembership);
    }

    private byte[] getDefaultGroupPicture() {
        Resource resource = new ClassPathResource(DEFAULT_GROUP_PICTURE_PATH);
        try {
            File defaultPicture = resource.getFile();
            Path filePath = defaultPicture.toPath();
            return Files.readAllBytes(filePath);
        } catch (Exception ex) {
            log.error("There was an error opening the default group picture", ex);
        }

        return new byte[]{};
    }
}
