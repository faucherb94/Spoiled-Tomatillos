package edu.northeastern.cs4500.services;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.northeastern.cs4500.models.MovieRating;
import edu.northeastern.cs4500.models.MovieRatingSnippet;
import edu.northeastern.cs4500.models.MovieReview;
import edu.northeastern.cs4500.models.MovieReviewSnippet;
import edu.northeastern.cs4500.models.Snippet;
import edu.northeastern.cs4500.models.User;
import edu.northeastern.cs4500.repositories.RatingRepository;
import edu.northeastern.cs4500.repositories.ReviewRepository;
import edu.northeastern.cs4500.repositories.UserRepository;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

@Service
public class UserService implements IUserService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private UserRepository repository;

    private static final String DEFAULT_PICTURE_PATH = "static/img/default-profile-picture.png";
    private static final String USER_NOT_FOUND = "user id {} not found";

    @Override
    public User findByID(int id) {
        User user = repository.findOne(id);
        if (user == null) {
			log.error(USER_NOT_FOUND, id);
            throw new ResourceNotFoundException(User.class, "id", Integer.toString(id));
        }
        log.info("user id {} retrieved", id);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = repository.findByUsername(username);
        if (user == null) {
            log.error("user with username {} not found", username);
            throw new ResourceNotFoundException(User.class, "username", username);
        }
        log.info("user with username {} retrieved", username);
        return user;
    }

    @Override
    public User create(User u) {
        byte[] bytes = getDefaultPictureBytes();
        if (bytes.length > 0) {
            u.setPicture(bytes);
        }
      log.info("created user " + u.getUsername());
      return repository.save(u);
    }

    @Override
    public User update(int id, User u) {
        User currentUser = repository.findOne(id);
        if (currentUser == null) {
            log.error(USER_NOT_FOUND, id);
            throw new ResourceNotFoundException(User.class, "id", Integer.toString(id));
        }
        
        currentUser.setUsername(u.getUsername());
        currentUser.setFirstName(u.getFirstName());
        currentUser.setLastName(u.getLastName());
        currentUser.setHometown(u.getHometown());

        log.info("user id {} updated", id);
        return repository.save(currentUser);
    }

    @Override
    public User delete(int id) {
        User userToDelete = repository.findOne(id);
        if (userToDelete == null) {
            log.error(USER_NOT_FOUND, id);
            throw new ResourceNotFoundException(User.class, "id", Integer.toString(id));
        }
        log.info("deleted user id {}", id);
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

    @Override
    public byte[] getProfilePicture(int id) {
        User currentUser = repository.findOne(id);
        if (currentUser == null) {
            throw new ResourceNotFoundException(User.class, "id", Integer.toString(id));
        }

        return currentUser.getPicture();
    }

    /**
     * Gets the bytes of the default profile picture. Used in the user creation method.
     */
    private byte[] getDefaultPictureBytes() {
        Resource resource = new ClassPathResource(DEFAULT_PICTURE_PATH);
        try {
            File defaultPicture = resource.getFile();
            Path filePath = defaultPicture.toPath();
            return Files.readAllBytes(filePath);
        } catch (Exception ex) {
            log.error("error opening default profile picture from classpath", ex);
        }

        return new byte[]{};
    }

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<Snippet> getUserActivity(int id) {
        List<MovieRating> ratings = ratingRepository.findByUserIDOrderByUpdatedAtDesc(id);

        List<Snippet> ratingSnippets = new ArrayList<>();
        for (MovieRating r : ratings) {
            Snippet mrs = new MovieRatingSnippet(r);
            ratingSnippets.add(mrs);
        }

        List<MovieReview> reviews = reviewRepository.findByUserIDOrderByUpdatedAtDesc(id);

        List<Snippet> reviewSnippets = new ArrayList<>();
        for (MovieReview r : reviews) {
            Snippet mrs = new MovieReviewSnippet(r);
            reviewSnippets.add(mrs);
        }

        List<Snippet> snippets = new ArrayList<>(ratingSnippets);
        snippets.addAll(reviewSnippets);

        Collections.sort(snippets, (o1, o2) -> {
            if(o1.getUpdatedAt().before(o2.getUpdatedAt()))
                return 1;
            return o1.getUpdatedAt().after(o2.getUpdatedAt()) ? -1 : 0;
        });

        return snippets;
    }

    @Override
    public List<User> searchUsers(String query) {
        return repository.findByUsernameContaining(query);
    }

}
