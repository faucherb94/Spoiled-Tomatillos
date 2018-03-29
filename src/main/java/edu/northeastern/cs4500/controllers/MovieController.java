package edu.northeastern.cs4500.controllers;

import com.mashape.unirest.http.exceptions.UnirestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import edu.northeastern.cs4500.models.Movie;
import edu.northeastern.cs4500.models.MovieReview;
import edu.northeastern.cs4500.models.SearchResult;
import edu.northeastern.cs4500.services.IReviewService;
import edu.northeastern.cs4500.services.OMDBClient;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private OMDBClient omdbClient;

    /************************************MOVIE REVIEWS*********************************/

    @Autowired
    private IReviewService reviewService;

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<MovieReview>> getMovieReviews(
            @PathVariable(value = "id") String movieID) {
        List<MovieReview> reviews = reviewService.getMovieReviews(movieID);
        return ResponseEntity.ok(reviews);
    }

    /***********************************SEARCH MOVIES*********************************/

    @GetMapping("/search")
    public ResponseEntity<List<SearchResult>> search(@RequestParam(value = "q") String query) {
        if (query.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            List<SearchResult> results = omdbClient.searchMovie(query);
            return ResponseEntity.ok(results);
        } catch (UnirestException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    /*********************************GET MOVIES*************************************/

    /**
     * Get a movie by its ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieByID(@PathVariable(value = "id") String movieID) {
        Movie movie = omdbClient.getMovieByID(movieID);
        return ResponseEntity.ok(movie);
    }
}
