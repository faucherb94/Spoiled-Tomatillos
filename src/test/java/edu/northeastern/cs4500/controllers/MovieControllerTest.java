package edu.northeastern.cs4500.controllers;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;

import edu.northeastern.cs4500.models.MovieRating;
import edu.northeastern.cs4500.services.IMovieRatingService;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private IMovieRatingService service;

    private final String URI = "/api/movie";
    private MovieRating rating;

    @Before
    public void setUp() {
        rating = new MovieRating("tt0266543", 1, 5);
    }

    @Test
    public void rateMovie_HappyPath() throws Exception {
        when(service.rateMovie(rating)).thenReturn(rating);

        ResponseEntity<MovieRating> response = restTemplate.postForEntity(URI + "/rating",
                rating, MovieRating.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void rateMovie_DBConflict() throws Exception {
        when(service.rateMovie(rating))
                .thenThrow(new DataIntegrityViolationException("",
                        new ConstraintViolationException("", new SQLException(), "")));

        ResponseEntity<MovieRating> response = restTemplate.postForEntity(URI + "/rating",
                rating, MovieRating.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void getUserMovieRating_HappyPath() throws Exception {
        when(service.getUserMovieRating(rating.getMovieID(), rating.getUserID()))
                .thenReturn(rating);

        ResponseEntity<MovieRating> response = restTemplate.getForEntity(
                URI + "/rating?userID=" + rating.getUserID() + "&movieID=" + rating.getMovieID(),
                MovieRating.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getUserMovieRating_NotFound() throws Exception {
        when(service.getUserMovieRating(rating.getMovieID(), rating.getUserID()))
                .thenThrow(new ResourceNotFoundException(
                        MovieRating.class, "movieID", rating.getMovieID(),
                        "userID", Integer.toString(rating.getUserID())));

        ResponseEntity<MovieRating> response = restTemplate.getForEntity(
                URI + "/rating?userID=" + rating.getUserID()+ "&movieID=" + rating.getMovieID(),
                MovieRating.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}