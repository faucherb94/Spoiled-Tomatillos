package edu.northeastern.cs4500.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import edu.northeastern.cs4500.models.Snippet;
import edu.northeastern.cs4500.models.User;

public interface IUserService {

    User findByID(int id);

    User findByUsername(String username);

    User create(User u);

    User update(int id, User u);

    User delete(int id);

    void uploadProfilePicture(int id, MultipartFile picture);

    byte[] getProfilePicture(int id);

    List<Snippet> getUserActivity(int id);
    
    List<User> getFriends(int id);

    void followUser(int uID, int fID);
}
