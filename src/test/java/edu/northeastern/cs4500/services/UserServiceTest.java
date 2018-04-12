package edu.northeastern.cs4500.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @TestConfiguration
    static class UserServiceContextConfiguration {
        @Bean
        public IUserService userService() {
            return new UserService();
        }
    }

    @Autowired
    private IUserService userService;

    @MockBean
    private UserRepository userRepository;

    private User defaultUser0;
    private User defaultUser1;
    private int BAD_USER_ID = 9999;
    private List<MovieRating> defaultRatings;
    private List<MovieReview> defaultReviews;

    private MovieRating rating1;
    private MovieRating rating2;
    private MovieReview review1;
    private MovieReview review2;

    @Before
    public void setUp() {
        defaultUser0 = new User("defaultUN", "john", "doe",
                "default@neu.edu", "defaultRole", "hometown");
        defaultUser0.setId(4123);
        
        defaultUser1 = new User("DC", "Daniel", "Cormier",
                "suplex-city@aol.com", "defaultRole", "Salem");
        defaultUser1.setId(1);

        defaultRatings = new ArrayList<>();
        rating1 = new MovieRating("tt42387", 1, 5);
        rating1.setUpdatedAt(new Date(472389));
        defaultRatings.add(rating1);

        rating2 = new MovieRating("tt41234", 1, 1);
        rating2.setUpdatedAt(new Date(64923714));
        defaultRatings.add(rating2);

        defaultReviews = new ArrayList<>();
        review1 = new MovieReview("tt47293", 1, "good");
        review1.setUpdatedAt(new Date(4234));
        defaultReviews.add(review1);
        review2 = new MovieReview("tt74983", 1, "bad");
        review2.setUpdatedAt(new Date(974231));
        defaultReviews.add(review2);
    }

    @Test
    public void findByID_HappyPath() throws Exception {
        when(userRepository.findOne(defaultUser0.getId())).thenReturn(defaultUser0);

        User foundUser = userService.findByID(defaultUser0.getId());

        assertThat(foundUser.getId()).isEqualTo(defaultUser0.getId());
        assertThat(foundUser.getUsername()).isEqualTo(defaultUser0.getUsername());
        assertThat(foundUser.getFirstName()).isEqualTo(defaultUser0.getFirstName());
        assertThat(foundUser.getLastName()).isEqualTo(defaultUser0.getLastName());
        assertThat(foundUser.getEmail()).isEqualTo(defaultUser0.getEmail());
        assertThat(foundUser.getRole()).isEqualTo(defaultUser0.getRole());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByID_NotFound() throws Exception {
        when(userRepository.findOne(BAD_USER_ID)).thenReturn(null);

        userService.findByID(BAD_USER_ID);
    }

    @Test
    public void findByUsername_HappyPath() throws Exception {
        when(userRepository.findByUsername(defaultUser0.getUsername())).thenReturn(defaultUser0);

        User foundUser = userService.findByUsername(defaultUser0.getUsername());

        assertThat(foundUser.getId()).isEqualTo(defaultUser0.getId());
        assertThat(foundUser.getUsername()).isEqualTo(defaultUser0.getUsername());
        assertThat(foundUser.getFirstName()).isEqualTo(defaultUser0.getFirstName());
        assertThat(foundUser.getLastName()).isEqualTo(defaultUser0.getLastName());
        assertThat(foundUser.getEmail()).isEqualTo(defaultUser0.getEmail());
        assertThat(foundUser.getRole()).isEqualTo(defaultUser0.getRole());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByUsername_NotFound() throws Exception {
        String badUsername = "badUsername";
        when(userRepository.findByUsername(badUsername)).thenReturn(null);

        userService.findByUsername(badUsername);
    }

    @Test
    public void create_HappyPath() throws Exception {
        when(userRepository.save(defaultUser0)).thenReturn(defaultUser0);

        User newUser = userService.create(defaultUser0);

        assertThat(newUser).isEqualTo(defaultUser0);
    }

    @Test
    public void update_HappyPath() throws Exception {
        when(userRepository.findOne(defaultUser0.getId())).thenReturn(defaultUser0);
        when(userRepository.save(defaultUser0)).thenReturn(defaultUser0);

        User updatedUser = userService.update(defaultUser0.getId(), defaultUser0);

        assertThat(updatedUser).isEqualTo(defaultUser0);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void update_NotFound() throws Exception {
        defaultUser0.setId(BAD_USER_ID);
        when(userRepository.findOne(defaultUser0.getId())).thenReturn(null);

        userService.update(defaultUser0.getId(), defaultUser0);
    }

    @Test
    public void delete_HappyPath() throws Exception {
        when(userRepository.findOne(defaultUser0.getId())).thenReturn(defaultUser0);

        User deletedUser = userService.delete(defaultUser0.getId());

        verify(userRepository, times(1)).findOne(defaultUser0.getId());
        verify(userRepository, times(1)).delete(defaultUser0);
        verifyNoMoreInteractions(userRepository);

        assertThat(deletedUser).isEqualTo(defaultUser0);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void delete_NotFound() throws Exception {
        when(userRepository.findOne(defaultUser0.getId())).thenReturn(null);

        userService.delete(defaultUser0.getId());

        verify(userRepository, times(1)).findOne(defaultUser0.getId());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void uploadProfilePicture_HappyPath() throws Exception {
        when(userRepository.findOne(defaultUser0.getId())).thenReturn(defaultUser0);
        when(userRepository.save(defaultUser0)).thenReturn(defaultUser0);

        String testString = "xyz";
        MultipartFile file = new MockMultipartFile("mock", "mock.jpg", "", testString.getBytes());

        userService.uploadProfilePicture(defaultUser0.getId(), file);
    }

    @Test(expected = IllegalArgumentException.class)
    public void uploadProfilePicture_InvalidExtension() throws Exception {
        MultipartFile mockFile = new MockMultipartFile("example.txt", "test".getBytes());

        userService.uploadProfilePicture(defaultUser0.getId(), mockFile);
    }

    @Test(expected = IllegalArgumentException.class)
    public void uploadProfilePicture_BadPhoto() throws Exception {
        MultipartFile mockFile = new MockMultipartFile("example", "example.png", "", new byte[]{});

        when(userRepository.findOne(defaultUser0.getId())).thenReturn(defaultUser0);

        userService.uploadProfilePicture(defaultUser0.getId(), mockFile);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void uploadProfilePicture_UserNotFound() throws Exception {
        when(userRepository.findOne(defaultUser0.getId())).thenReturn(null);

        userService.uploadProfilePicture(defaultUser0.getId(),
                new MockMultipartFile("test", "test.jpg", "image/jpeg", new byte[]{}));

        verify(userRepository, times(1)).findOne(defaultUser0.getId());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getProfilePicture_HappyPath() throws Exception {
        defaultUser0.setPicture("bytes bytes bytes".getBytes());
        when(userRepository.findOne(defaultUser0.getId())).thenReturn(defaultUser0);

        byte[] picture = userService.getProfilePicture(defaultUser0.getId());

        assertThat(picture).isEqualTo(defaultUser0.getPicture());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getProfilePicture_UserNotFound() throws Exception {
        when(userRepository.findOne(defaultUser0.getId())).thenReturn(null);

        userService.getProfilePicture(defaultUser0.getId());
    }

    @MockBean
    private RatingRepository ratingRepository;

    @MockBean
    private ReviewRepository reviewRepository;

    @Test
    public void getUserActivity_HappyPath() throws Exception {
        when(ratingRepository.findByUserIDOrderByUpdatedAtDesc(1))
                .thenReturn(defaultRatings);

        when(reviewRepository.findByUserIDOrderByUpdatedAtDesc(1))
                .thenReturn(defaultReviews);

        List<Snippet> expected = new ArrayList<>();
        expected.add(new MovieRatingSnippet(rating2));
        expected.add(new MovieReviewSnippet(review2));
        expected.add(new MovieRatingSnippet(rating1));
        expected.add(new MovieReviewSnippet(review1));

        assertThat(userService.getUserActivity(1)).isEqualTo(expected);
    }
}