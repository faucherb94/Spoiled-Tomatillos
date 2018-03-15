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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.cs4500.models.MovieRating;
import edu.northeastern.cs4500.models.MovieReview;
import edu.northeastern.cs4500.services.IMovieRatingService;
import edu.northeastern.cs4500.services.IMovieReviewService;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private IMovieRatingService ratingService;

    @MockBean
    private IMovieReviewService reviewService;

    private final String URI = "/api/movie";
    private MovieRating rating;
    private MovieReview review;
    private List<MovieReview> reviewList = new ArrayList<>();

    @Before
    public void setUp() {
        String movieID = "tt0266543";
        rating = new MovieRating(movieID, 1, 5);
        review = new MovieReview(movieID, 1, "an amazing review");
        reviewList.add(review);
        reviewList.add(new MovieReview("tt0266543", 10, "a bad review"));
    }

    @Test
    public void rateMovie_HappyPath() throws Exception {
        when(ratingService.rateMovie(rating)).thenReturn(rating);

        ResponseEntity<MovieRating> response = restTemplate.postForEntity(URI + "/rating",
                rating, MovieRating.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void rateMovie_DBConflict() throws Exception {
        when(ratingService.rateMovie(rating))
                .thenThrow(new DataIntegrityViolationException("",
                        new ConstraintViolationException("", new SQLException(), "")));

        ResponseEntity<MovieRating> response = restTemplate.postForEntity(URI + "/rating",
                rating, MovieRating.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void getUserMovieRating_HappyPath() throws Exception {
        when(ratingService.getUserMovieRating(rating.getMovieID(), rating.getUserID()))
                .thenReturn(rating);

        ResponseEntity<MovieRating> response = restTemplate.getForEntity(
                URI + "/rating?userID=" + rating.getUserID() + "&movieID=" + rating.getMovieID(),
                MovieRating.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getUserMovieRating_NotFound() throws Exception {
        when(ratingService.getUserMovieRating(rating.getMovieID(), rating.getUserID()))
                .thenThrow(new ResourceNotFoundException(
                        MovieRating.class, "movieID", rating.getMovieID(),
                        "userID", Integer.toString(rating.getUserID())));

        ResponseEntity<MovieRating> response = restTemplate.getForEntity(
                URI + "/rating?userID=" + rating.getUserID()+ "&movieID=" + rating.getMovieID(),
                MovieRating.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void reviewMovie_HappyPath() throws Exception {
        when(reviewService.reviewMovie(review)).thenReturn(review);

        ResponseEntity<MovieReview> response = restTemplate.postForEntity(URI + "/review",
                review, MovieReview.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void reviewMovie_DBConflict() throws Exception {
        when(reviewService.reviewMovie(review))
                .thenThrow(new DataIntegrityViolationException("",
                        new ConstraintViolationException("", new SQLException(), "")));

        ResponseEntity<MovieReview> response = restTemplate.postForEntity(URI + "/review",
                review, MovieReview.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void getUserMovieReview_HappyPath() throws Exception {
        when(reviewService.getUserMovieReview(review.getMovieID(), rating.getUserID()))
                .thenReturn(review);

        ResponseEntity<MovieReview> response = restTemplate.getForEntity(
                URI + "/review?userID=" + review.getUserID() + "&movieID=" + review.getMovieID(),
                MovieReview.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getUserMovieReview_NotFound() throws Exception {
        when(reviewService.getUserMovieReview(review.getMovieID(), review.getUserID()))
                .thenThrow(new ResourceNotFoundException(
                        MovieReview.class, "movieID", review.getMovieID(),
                        "userID", Integer.toString(review.getUserID())));

        ResponseEntity<MovieReview> response = restTemplate.getForEntity(
                URI + "/review?userID=" + review.getUserID()+ "&movieID=" + review.getMovieID(),
                MovieReview.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
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

}