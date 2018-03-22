package edu.northeastern.cs4500.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.northeastern.cs4500.models.MovieReview;
import edu.northeastern.cs4500.repositories.ReviewRepository;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ReviewServiceTest {

    @TestConfiguration
    static class MovieReviewServiceContextConfiguration {
        @Bean
        public IReviewService movieReviewService() {
            return new ReviewService();
        }
    }

    @Autowired
    private IReviewService movieReviewService;

    @MockBean
    private ReviewRepository repository;

    private MovieReview review;

    @Before
    public void setUp() {
        review = new MovieReview("tt742389", 42, "a really good movie review");
        review.setId(3);
    }

    @Test
    public void reviewMovie_HappyPath() throws Exception {
        when(repository.save(review)).thenReturn(review);

        MovieReview newReview = movieReviewService.reviewMovie(
                review.getUserID(), review.getMovieID(), review);

        assertThat(newReview).isEqualTo(review);
    }

    @Test
    public void getUserMovieReview_HappyPath() throws Exception {
        when(repository.findByMovieIDAndUserID(review.getMovieID(), review.getUserID()))
                .thenReturn(review);

        MovieReview newReview = movieReviewService.getUserMovieReview(
                review.getMovieID(), review.getUserID());

        assertThat(newReview).isEqualTo(review);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getUserMovieReview_MovieNotFound() throws Exception {
        String badMovieID = "bad";
        when(repository.findByMovieIDAndUserID(badMovieID, 1))
                .thenReturn(null);

        movieReviewService.getUserMovieReview(badMovieID, 1);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getUserMovieReview_UserNotFound() throws Exception {
        int badUserID = 874923;
        when(repository.findByMovieIDAndUserID("", badUserID))
                .thenReturn(null);

        movieReviewService.getUserMovieReview("", badUserID);
    }

}