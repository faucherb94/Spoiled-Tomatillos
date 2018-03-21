package edu.northeastern.cs4500.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import edu.northeastern.cs4500.models.MovieReview;
import edu.northeastern.cs4500.services.IReviewService;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private IReviewService reviewService;

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<MovieReview>> getMovieReviews(
            @PathVariable(value = "id") String movieID) {
        List<MovieReview> reviews = reviewService.getMovieReviews(movieID);
        return ResponseEntity.ok(reviews);
    }
}
