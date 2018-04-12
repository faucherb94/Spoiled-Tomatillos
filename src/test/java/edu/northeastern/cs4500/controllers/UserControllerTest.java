package edu.northeastern.cs4500.controllers;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.cs4500.models.Group;
import edu.northeastern.cs4500.models.MovieRating;
import edu.northeastern.cs4500.models.MovieRatingSnippet;
import edu.northeastern.cs4500.models.MovieReview;
import edu.northeastern.cs4500.models.MovieReviewSnippet;
import edu.northeastern.cs4500.models.Snippet;
import edu.northeastern.cs4500.models.User;
import edu.northeastern.cs4500.services.IGroupService;
import edu.northeastern.cs4500.services.IRatingService;
import edu.northeastern.cs4500.services.IRelationshipService;
import edu.northeastern.cs4500.services.IReviewService;
import edu.northeastern.cs4500.services.UserService;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private UserService userService;

    @MockBean
    private IRatingService ratingService;

    @MockBean
    private IReviewService reviewService;

    @MockBean
    private IGroupService groupService;

    @MockBean
    private IRelationshipService relationshipService;

    private final String URI = "/api/users";
    private User mockUser;
    private MovieRating rating;
    private MovieReview review;
    private List<Group> groups;
    private List<User> userList;

    @Before
    public void setUp() {
        mockUser = new User("un", "fn", "ln", "random@neu.edu", "default", "boston",
                "linktopic.com");
        String movieID = "tt0266543";
        rating = new MovieRating(movieID, 1, 5);
        review = new MovieReview(movieID, 1, "an amazing review");

        groups = new ArrayList<>();
        groups.add(new Group(1, "test", "blah"));

        userList = new ArrayList<>();
        userList.add(mockUser);
    }

    /********************************USER MANAGEMENT*****************************************/

    @Test
    public void createUser_HappyPath() throws Exception {
        when(userService.create(any(User.class))).thenReturn(mockUser);

        ResponseEntity<User> user = restTemplate.postForEntity(URI,
                mockUser, User.class);

        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void createUser_DBConflict() throws Exception {
        when(userService.create(any(User.class)))
                .thenThrow(new DataIntegrityViolationException("",
                        new ConstraintViolationException("", new SQLException(), "")));

        ResponseEntity<User> user = restTemplate.postForEntity(URI,
                mockUser, User.class);

        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void getUserByID_HappyPath() throws Exception {
        when(userService.findByID(anyInt())).thenReturn(mockUser);

        ResponseEntity<User> user = restTemplate.getForEntity(URI + "/{id}", User.class, 1);

        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.OK);

        User body = user.getBody();
        assertThat(body.getUsername()).isEqualTo(mockUser.getUsername());
        assertThat(body.getFirstName()).isEqualTo(mockUser.getFirstName());
        assertThat(body.getLastName()).isEqualTo(mockUser.getLastName());
        assertThat(body.getEmail()).isEqualTo(mockUser.getEmail());
        assertThat(body.getRole()).isEqualTo(mockUser.getRole());
    }

    @Test
    public void getUserByID_NotFound() throws Exception {
        when(userService.findByID(anyInt()))
                .thenThrow(new ResourceNotFoundException(
                        User.class, "id", "1"));

        ResponseEntity<User> response = restTemplate.getForEntity(URI + "/{id}",
                User.class, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getUserByUsername_HappyPath() throws Exception {
        when(userService.findByUsername(anyString())).thenReturn(mockUser);

        ResponseEntity<User> user = restTemplate.getForEntity(URI + "?name=un", User.class);

        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.OK);

        User body = user.getBody();
        assertThat(body.getUsername()).isEqualTo(mockUser.getUsername());
        assertThat(body.getFirstName()).isEqualTo(mockUser.getFirstName());
        assertThat(body.getLastName()).isEqualTo(mockUser.getLastName());
        assertThat(body.getEmail()).isEqualTo(mockUser.getEmail());
        assertThat(body.getRole()).isEqualTo(mockUser.getRole());
    }

    @Test
    public void getUserByUsername_NotFound() throws Exception {
        when(userService.findByUsername(anyString()))
                .thenThrow(new ResourceNotFoundException(
                        User.class, "username", "un"));

        ResponseEntity<User> response = restTemplate.getForEntity(URI + "?name=un", User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateUser() throws Exception {
        when(userService.update(anyInt(), any(User.class)))
                .thenReturn(mockUser);

        HttpEntity<User> entity = new HttpEntity<>(mockUser, new HttpHeaders());
        ResponseEntity<User> user = restTemplate.exchange(URI + "/{id}", HttpMethod.PUT,
                entity, User.class, 1);

        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateUser_DBConflict() throws Exception {
        when(userService.update(anyInt(), any(User.class)))
                .thenThrow(new DataIntegrityViolationException("",
                        new ConstraintViolationException("", new SQLException(), "")));

        HttpEntity<User> entity = new HttpEntity<>(mockUser, new HttpHeaders());
        ResponseEntity<User> user = restTemplate.exchange(URI + "/{id}", HttpMethod.PUT,
                entity, User.class, 1);

        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void deleteUser_HappyPath() throws Exception {
        when(userService.delete(mockUser.getId())).thenReturn(mockUser);

        HttpEntity<User> entity = new HttpEntity<>(mockUser, new HttpHeaders());
        ResponseEntity<User> response = restTemplate.exchange(URI + "/{id}", HttpMethod.DELETE,
                entity, User.class, mockUser.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteUser_DBConflict() throws Exception {
        when(userService.delete(anyInt()))
                .thenThrow(new DataIntegrityViolationException("",
                        new ConstraintViolationException("", new SQLException(), "")));

        HttpEntity<User> entity = new HttpEntity<>(mockUser, new HttpHeaders());
        ResponseEntity<User> response = restTemplate.exchange(URI + "/{id}", HttpMethod.DELETE,
                entity, User.class, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    /*********************************USER RATINGS****************************************/

    @Test
    public void rateMovie_HappyPath() throws Exception {
        when(ratingService.rateMovie(rating.getUserID(), rating.getMovieID(), rating))
                .thenReturn(rating);

        ResponseEntity<MovieRating> response = restTemplate.postForEntity(
                URI + "/{id}/ratings/movies/{movie-id}",
                rating, MovieRating.class, 1, rating.getMovieID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void rateMovie_DBConflict() throws Exception {
        when(ratingService.rateMovie(rating.getUserID(), rating.getMovieID(), rating))
                .thenThrow(new DataIntegrityViolationException("",
                        new ConstraintViolationException("", new SQLException(), "")));

        ResponseEntity<MovieRating> response = restTemplate.postForEntity(
                URI + "/{id}/ratings/movies/{movie-id}",
                rating, MovieRating.class, 1, rating.getMovieID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void getUserMovieRating_HappyPath() throws Exception {
        when(ratingService.getUserMovieRating(rating.getMovieID(), rating.getUserID()))
                .thenReturn(rating);

        ResponseEntity<MovieRating> response = restTemplate.getForEntity(
                URI + "/{id}/ratings/movies/{movie-id}",
                MovieRating.class, rating.getUserID(), rating.getMovieID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getUserMovieRating_NotFound() throws Exception {
        when(ratingService.getUserMovieRating(rating.getMovieID(), rating.getUserID()))
                .thenThrow(new ResourceNotFoundException(
                        MovieRating.class, "movieID", rating.getMovieID(),
                        "userID", Integer.toString(rating.getUserID())));

        ResponseEntity<MovieRating> response = restTemplate.getForEntity(
                URI + "/{id}/ratings/movies/{movie-id}",
                MovieRating.class, rating.getUserID(), rating.getMovieID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateUserMovieRating_HappyPath() throws Exception {
        when(ratingService.updateUserMovieRating(rating.getMovieID(), rating.getUserID(),
                rating)).thenReturn(rating);

        HttpEntity<MovieRating> entity = new HttpEntity<>(rating, new HttpHeaders());
        ResponseEntity<MovieRating> response = restTemplate.exchange(
                URI + "/{id}/ratings/movies/{movie-id}", HttpMethod.PUT,
                entity, MovieRating.class, 1, rating.getMovieID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /*********************************USER REVIEWS****************************************/

    @Test
    public void reviewMovie_HappyPath() throws Exception {
        when(reviewService.reviewMovie(review.getUserID(), review.getMovieID(), review))
                .thenReturn(review);

        ResponseEntity<MovieReview> response = restTemplate.postForEntity(
                URI + "/{id}/reviews/movies/{movie-id}",
                review, MovieReview.class, review.getUserID(), review.getMovieID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void reviewMovie_DBConflict() throws Exception {
        when(reviewService.reviewMovie(review.getUserID(), review.getMovieID(), review))
                .thenThrow(new DataIntegrityViolationException("",
                        new ConstraintViolationException("", new SQLException(), "")));

        ResponseEntity<MovieReview> response = restTemplate.postForEntity(
                URI + "/{id}/reviews/movies/{movie-id}",
                review, MovieReview.class, review.getUserID(), review.getMovieID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void getUserMovieReview_HappyPath() throws Exception {
        when(reviewService.getUserMovieReview(review.getMovieID(), rating.getUserID()))
                .thenReturn(review);

        ResponseEntity<MovieReview> response = restTemplate.getForEntity(
                URI + "/{id}/reviews/movies/{movie-id}",
                MovieReview.class, review.getUserID(), review.getMovieID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getUserMovieReview_NotFound() throws Exception {
        when(reviewService.getUserMovieReview(review.getMovieID(), review.getUserID()))
                .thenThrow(new ResourceNotFoundException(
                        MovieReview.class, "movieID", review.getMovieID(),
                        "userID", Integer.toString(review.getUserID())));

        ResponseEntity<MovieReview> response = restTemplate.getForEntity(
                URI + "{id}/reviews/movies/{movie-id}",
                MovieReview.class, review.getUserID(), review.getUserID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /*********************************USER RATINGS AND REVIEWS*****************************/

    @Test
    public void getUserActivity_HappyPath() throws Exception {
        List<Snippet> snippets = new ArrayList<>();
        snippets.add(new MovieRatingSnippet(new MovieRating()));
        snippets.add(new MovieReviewSnippet(new MovieReview()));
        snippets.add(new MovieRatingSnippet(new MovieRating()));
        snippets.add(new MovieReviewSnippet(new MovieReview()));
        when(userService.getUserActivity(mockUser.getId()))
                .thenReturn(snippets);

        ResponseEntity<List<Snippet>> response = restTemplate.exchange(URI + "/{id}/activity",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Snippet>>() {},
                mockUser.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /*********************************GROUPS************************************************/

    @Test
    public void getGroupMemberships_HappyPath() throws Exception {
        when(groupService.getUserGroupMemberships(mockUser.getId()))
                .thenReturn(groups);

        ResponseEntity<List<Group>> response = restTemplate.exchange(URI + "/{id}/groups",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Group>>() {},
                mockUser.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /*******************************FRIENDS*************************************************/

    @Test
    public void getFriends_HappyPath() throws Exception {
        when(relationshipService.getFriends(mockUser.getId())).thenReturn(userList);

        ResponseEntity<List<User>> response = restTemplate.exchange(URI + "/{id}/friends",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {},
                mockUser.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /*******************************SEARCH*************************************************/

    @Test
    public void searchUsers_HappyPath() throws Exception {
        when(userService.searchUsers(anyString())).thenReturn(userList);

        ResponseEntity<List<User>> response = restTemplate.exchange(URI + "/search?q=test",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {},
                mockUser.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}