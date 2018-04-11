package edu.northeastern.cs4500.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import edu.northeastern.cs4500.models.MovieReview;
import edu.northeastern.cs4500.repositories.ReviewRepository;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

@Service
public class ReviewService implements IReviewService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
			
    @Autowired
    private ReviewRepository repository;

    @Override
    public MovieReview reviewMovie(int userID, String movieID, MovieReview review) {
        review.setUserID(userID);
        review.setMovieID(movieID);
        log.info("user id {} reviewed movie id {} with review {}",
                userID, movieID, review.getReview());
        return repository.save(review);
    }

    @Override
    public MovieReview getUserMovieReview(String movieID, int userID) {
        MovieReview review = repository.findByMovieIDAndUserID(movieID, userID);
        if (review == null) {
            log.error("user id {} movie id {} review not found", userID, movieID);
            throw new ResourceNotFoundException(MovieReview.class,
                    "movieID", movieID,
                    "userID", Integer.toString(userID));
        }
        log.info("retrieved movie review for user id {} and movie id {}", userID, movieID);
        return review;
    }

    @Override
    public List<MovieReview> getMovieReviews(String movieID) {
        log.info("reviews for movie id {} retrieved", movieID);
        return repository.findByMovieID(movieID);
    }

}
