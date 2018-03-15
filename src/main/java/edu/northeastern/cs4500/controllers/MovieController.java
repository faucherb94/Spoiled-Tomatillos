package edu.northeastern.cs4500.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import edu.northeastern.cs4500.models.MovieRating;
import edu.northeastern.cs4500.models.MovieReview;
import edu.northeastern.cs4500.services.IMovieRatingService;
import edu.northeastern.cs4500.services.IMovieReviewService;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

    @Autowired
    private IMovieRatingService ratingService;

    @Autowired
    private IMovieReviewService reviewService;

    @PostMapping("/rating")
    public MovieRating rateMovie(@Valid @RequestBody MovieRating rating) {
        return ratingService.rateMovie(rating);
    }

    @GetMapping("/rating")
    public ResponseEntity<MovieRating> getUserMovieRating(
            @RequestParam(value = "userID") int userID,
            @RequestParam(value = "movieID") String movieID) {
        MovieRating rating = ratingService.getUserMovieRating(movieID, userID);
        return ResponseEntity.ok(rating);
    }

    @PostMapping("/review")
    public MovieReview reviewMovie(@Valid @RequestBody MovieReview review) {
        return reviewService.reviewMovie(review);
    }

    @GetMapping("/review")
    public ResponseEntity<MovieReview> getUserMovieReview(
            @RequestParam(value = "userID") int userID,
            @RequestParam(value = "movieID") String movieID) {
        MovieReview review = reviewService.getUserMovieReview(movieID, userID);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<MovieReview>> getMovieReviews(
            @PathVariable(value = "id") String movieID) {
        List<MovieReview> reviews = reviewService.getMovieReviews(movieID);
        return ResponseEntity.ok(reviews);
    }
}
