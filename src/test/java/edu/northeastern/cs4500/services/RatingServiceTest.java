package edu.northeastern.cs4500.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.northeastern.cs4500.models.MovieRating;
import edu.northeastern.cs4500.repositories.RatingRepository;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RatingServiceTest {

    @TestConfiguration
    static class MovieRatingServiceContextConfiguration {
        @Bean
        public IRatingService movieRatingService() {
            return new RatingService();
        }
    }

    @Autowired
    private IRatingService movieRatingService;

    @MockBean
    private RatingRepository repository;

    private MovieRating rating;

    @Before
    public void setUp() {
        rating = new MovieRating("tt742389", 42, 4);
        rating.setId(3);
    }

    @Test
    public void rateMovie_HappyPath() throws Exception {
        when(repository.save(rating)).thenReturn(rating);

        MovieRating newRating = movieRatingService.rateMovie(
                rating.getUserID(), rating.getMovieID(), rating);

        assertThat(newRating).isEqualTo(rating);
    }

    @Test
    public void getUserMovieRating_HappyPath() throws Exception {
        when(repository.findByMovieIDAndUserID(rating.getMovieID(), rating.getUserID()))
                .thenReturn(rating);

        MovieRating newRating = movieRatingService.getUserMovieRating(
                rating.getMovieID(), rating.getUserID());

        assertThat(newRating).isEqualTo(rating);
    }

    @Test
    public void getUserMovieRating_RatingNotCreatedYet() throws Exception {
        when(repository.findByMovieIDAndUserID(anyString(), anyInt()))
                .thenReturn(null);

        MovieRating rating = movieRatingService.getUserMovieRating("", 1);
        MovieRating expected = new MovieRating("", 1, 0);

        assertThat(rating).isEqualTo(expected);
    }

    @Test
    public void updateUserMovieRating_HappyPath() throws Exception {
        when(repository.findByMovieIDAndUserID(anyString(), anyInt())).thenReturn(rating);
        when(repository.save(rating)).thenReturn(rating);

        MovieRating updatedRating = movieRatingService.updateUserMovieRating(
                rating.getMovieID(), rating.getUserID(), rating);
        assertThat(updatedRating).isEqualTo(rating);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateUserMovieRating_NotFound() throws Exception {
        when(repository.findByMovieIDAndUserID(anyString(), anyInt()))
                .thenReturn(null);

        movieRatingService.updateUserMovieRating("", 423423, rating);
    }

}