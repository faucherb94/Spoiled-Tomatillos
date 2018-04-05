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

import edu.northeastern.cs4500.models.Group;
import edu.northeastern.cs4500.repositories.GroupRepository;

@Service
public class GroupService implements IGroupService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String DEFAULT_GROUP_PICTURE_PATH = "static/img/image_missing.png";

    @Autowired
    private GroupRepository groupRepository;

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
