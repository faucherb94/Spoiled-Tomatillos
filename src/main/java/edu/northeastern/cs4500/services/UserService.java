package edu.northeastern.cs4500.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import edu.northeastern.cs4500.models.User;
import edu.northeastern.cs4500.repositories.UserRepository;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository repository;

    @Override
    public User findByID(int id) {
        User user = repository.findOne(id);
        if (user == null) {
            throw new ResourceNotFoundException(User.class, "id", Integer.toString(id));
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = repository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException(User.class, "username", username);
        }
        return user;
    }

    @Override
    public User create(User u) {
        return repository.save(u);
    }

    @Override
    public User update(int id, User u) {
        User currentUser = repository.findOne(id);
        if (currentUser == null) {
            throw new ResourceNotFoundException(User.class, "id", Integer.toString(id));
        }

        currentUser.setUsername(u.getUsername());
        currentUser.setEmail(u.getEmail());
        currentUser.setFirstName(u.getFirstName());
        currentUser.setLastName(u.getLastName());
        currentUser.setRole(u.getRole());
        currentUser.setHometown(u.getHometown());

        return repository.save(currentUser);
    }

    @Override
    public User delete(int id) {
        User userToDelete = repository.findOne(id);
        if (userToDelete == null) {
            throw new ResourceNotFoundException(User.class, "id", Integer.toString(id));
        }

        repository.delete(userToDelete);
        return userToDelete;
    }

    @Override
    public void uploadProfilePicture(int id, MultipartFile picture) {
        String extension = FilenameUtils.getExtension(picture.getOriginalFilename());
        if (!extension.equals("png") && !extension.equals("jpg") && !extension.equals("jpeg")) {
            throw new IllegalArgumentException("Only PNG and JPG file types accepted");
        }

        User currentUser = repository.findOne(id);
        if (currentUser == null) {
            throw new ResourceNotFoundException(User.class, "id", Integer.toString(id));
        }

        if (picture.isEmpty()) {
            throw new IllegalArgumentException("No file uploaded");
        }

        try {
            byte[] pictureBytes = picture.getBytes();
            currentUser.setPicture(pictureBytes);
            repository.save(currentUser);
        } catch (IOException ex) {
            throw new IllegalArgumentException();
        }
    }

}
