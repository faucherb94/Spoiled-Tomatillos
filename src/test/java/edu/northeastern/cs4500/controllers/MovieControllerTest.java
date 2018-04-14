package edu.northeastern.cs4500.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.cs4500.models.Movie;
import edu.northeastern.cs4500.models.MovieReview;
import edu.northeastern.cs4500.models.SearchResult;
import edu.northeastern.cs4500.services.IReviewService;
import edu.northeastern.cs4500.services.OMDBClient;
import edu.northeastern.cs4500.services.TMDBClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private IReviewService reviewService;

    @MockBean
    private OMDBClient omdbClient;

    @MockBean
    private TMDBClient tmdbClient;

    private final String URI = "/api/movies";
    private MovieReview review;
    private List<MovieReview> reviewList = new ArrayList<>();
    private List<Movie> nowPlayingMovies;

    @Before
    public void setUp() {
        String movieID = "tt0266543";
        review = new MovieReview(movieID, 1, "an amazing review");
        reviewList.add(review);
        reviewList.add(new MovieReview("tt0266543", 10, "a bad review"));
        nowPlayingMovies = new ArrayList<>();
        nowPlayingMovies.add(new Movie());
    }

    @Test
    public void getMovieReviews_HappyPath() throws Exception {
        when(reviewService.getMovieReviews(review.getMovieID())).thenReturn(reviewList);

        ResponseEntity<List<MovieReview>> response = restTemplate.exchange(URI + "/{id}/reviews",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<MovieReview>>() {},
                "tt0266543");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(reviewList.size());
    }

    @Test
    public void searchMovie_HappyPath() throws Exception {
        List<SearchResult> results = new ArrayList<>();
        when(omdbClient.searchMovie(anyString()))
                .thenReturn(results);

        ResponseEntity<List<SearchResult>> response = restTemplate.exchange(URI + "/search?q=shrek",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<SearchResult>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void searchMovie_EmptyQuery() throws Exception {
        ResponseEntity<List<SearchResult>> response = restTemplate.exchange(URI + "/search?q=",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<SearchResult>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getMovieByID_HappyPath() throws Exception {
        when(omdbClient.getMovieByID(anyString()))
                .thenReturn(new Movie());

        ResponseEntity<Movie> response = restTemplate.getForEntity(URI + "/{id}",
                Movie.class, "tt472389");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getNowPlaying_HappyPath() throws Exception {
        when(tmdbClient.getNowPlaying()).thenReturn(nowPlayingMovies);

        ResponseEntity<List<Movie>> response = restTemplate.exchange(URI + "/now-playing",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Movie>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}