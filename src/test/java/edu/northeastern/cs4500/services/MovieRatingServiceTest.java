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
import edu.northeastern.cs4500.repositories.MovieRatingRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MovieRatingServiceTest {

    @TestConfiguration
    static class MovieRatingServiceContextConfiguration {
        @Bean
        public IMovieRatingService movieRatingService() {
            return new MovieRatingService();
        }
    }

    @Autowired
    private IMovieRatingService movieRatingService;

    @MockBean
    private MovieRatingRepository repository;

    private MovieRating rating;

    @Before
    public void setUp() {
        rating = new MovieRating("tt742389", 42, 4);
        rating.setId(3);
    }

    @Test
    public void rateMovie_HappyPath() throws Exception {
        when(repository.save(rating)).thenReturn(rating);

        MovieRating newRating = movieRatingService.rateMovie(rating);

        assertThat(newRating).isEqualTo(rating);
    }

}