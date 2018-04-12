package edu.northeastern.cs4500.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import edu.northeastern.cs4500.models.Group;
import edu.northeastern.cs4500.models.MovieRating;
import edu.northeastern.cs4500.models.MovieReview;
import edu.northeastern.cs4500.models.Snippet;
import edu.northeastern.cs4500.models.User;
import edu.northeastern.cs4500.services.IGroupService;
import edu.northeastern.cs4500.services.IRatingService;
import edu.northeastern.cs4500.services.IRelationshipService;
import edu.northeastern.cs4500.services.IReviewService;
import edu.northeastern.cs4500.services.IUserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    /*********************************USER MANAGEMENT****************************************/

    @Autowired
    private IUserService userService;

    /**
     * Create a new user
     */
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    /**
     * Get user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserByID(@PathVariable(value = "id") int id) {
        User user = userService.findByID(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Get user by username
     */
    @GetMapping
    public ResponseEntity<User> getUserByUsername(
            @RequestParam(value = "name") String username) {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * Update user
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") int id,
                                           @Valid @RequestBody User u) {
        User updatedUser = userService.update(id, u);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Delete user
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable(value = "id") int id) {
        User deletedUser = userService.delete(id);
        return ResponseEntity.ok(deletedUser);
    }

    /*********************************USER REVIEWS****************************************/

    @Autowired
    private IReviewService reviewService;

    /**
     * Get user movie review
     */
    @GetMapping("/{id}/reviews/movies/{movie-id}")
    public ResponseEntity<MovieReview> getUserMovieReview(
            @PathVariable(value = "id") int userID,
            @PathVariable(value = "movie-id") String movieID) {
        MovieReview review = reviewService.getUserMovieReview(movieID, userID);
        return ResponseEntity.ok(review);
    }

    /**
     * Create user movie review
     */
    @PostMapping("/{id}/reviews/movies/{movie-id}")
    public MovieReview reviewMovie(@PathVariable(value = "id") int userID,
                                   @PathVariable(value = "movie-id") String movieID,
                                   @Valid @RequestBody MovieReview review) {
        return reviewService.reviewMovie(userID, movieID, review);
    }

    /*********************************USER RATINGS****************************************/

    @Autowired
    private IRatingService ratingService;

    /**
     * Get user movie rating
     */
    @GetMapping("/{id}/ratings/movies/{movie-id}")
    public ResponseEntity<MovieRating> getUserMovieRating(
            @PathVariable(value = "id") int userID,
            @PathVariable(value = "movie-id") String movieID) {
        MovieRating rating = ratingService.getUserMovieRating(movieID, userID);
        return ResponseEntity.ok(rating);
    }

    /**
     * Create user movie rating
     */
    @PostMapping("/{id}/ratings/movies/{movie-id}")
    public MovieRating rateMovie(@PathVariable(value = "id") int userID,
                                 @PathVariable(value = "movie-id") String movieID,
                                 @Valid @RequestBody MovieRating rating) {
        return ratingService.rateMovie(userID, movieID, rating);
    }

    /**
     * Update user movie rating
     */
    @PutMapping("/{id}/ratings/movies/{movie-id}")
    public ResponseEntity<MovieRating> updateUserMovieRating(
            @PathVariable(value = "id") int userID,
            @PathVariable(value = "movie-id") String movieID,
            @Valid @RequestBody MovieRating rating) {
        MovieRating updatedRating = ratingService.updateUserMovieRating(movieID, userID, rating);
        return ResponseEntity.ok(updatedRating);
    }

    /*********************************USER RATINGS AND REVIEWS*****************************/

    /**
     * Get all user ratings and reviews
     */
    @GetMapping("/{id}/activity")
    public ResponseEntity<List<Snippet>> getUserActivity(@PathVariable(value = "id") int userID) {
        List<Snippet> snippets = userService.getUserActivity(userID);
        return ResponseEntity.ok(snippets);
    }
    
    /*********************************GROUPS*************************************************/

    @Autowired
    private IGroupService groupService;

    /**
     * Get groups that a user is a member of
     */
    @GetMapping("/{id}/groups")
    public ResponseEntity<List<Group>> getGroupMemberships(@PathVariable(value = "id") int userID) {
        List<Group> groups = groupService.getUserGroupMemberships(userID);
        return ResponseEntity.ok(groups);
    }

    /*********************************FRIENDS*************************************************/

    @Autowired
    private IRelationshipService relationshipService;

    /**
     * Get a user's friends
     */
    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable(value = "id") int userID) {
        List<User> friends = relationshipService.getFriends(userID);
        return ResponseEntity.ok(friends);
    }

    /**
     * Search for other users
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam(value = "q") String query) {
        List<User> users = userService.searchUsers(query);
        return ResponseEntity.ok(users);
    }
}
