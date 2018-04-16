package edu.northeastern.cs4500.controllers;

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
import edu.northeastern.cs4500.services.TMDBClient;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private OMDBClient omdbClient;

    @Autowired
    private TMDBClient tmdbClient;

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

        List<SearchResult> results = omdbClient.searchMovie(query);
        return ResponseEntity.ok(results);
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

    /**
     * Get movies now in theaters
     */
    @GetMapping("/now-playing")
    public ResponseEntity<List<Movie>> getNowPlaying() {
        List<Movie> movies = tmdbClient.getNowPlaying();
        return ResponseEntity.ok(movies);
    }
}
