package edu.northeastern.cs4500.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import edu.northeastern.cs4500.models.MovieReview;
import edu.northeastern.cs4500.repositories.ReviewRepository;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    private ReviewRepository repository;

    @Override
    public MovieReview reviewMovie(int userID, String movieID, MovieReview review) {
        review.setUserID(userID);
        review.setMovieID(movieID);
        return repository.save(review);
    }

    @Override
    public MovieReview getUserMovieReview(String movieID, int userID) {
        MovieReview review = repository.findByMovieIDAndUserID(movieID, userID);
        if (review == null) {
            throw new ResourceNotFoundException(MovieReview.class,
                    "movieID", movieID,
                    "userID", Integer.toString(userID));
        }
        return review;
    }

    @Override
    public List<MovieReview> getMovieReviews(String movieID) {
        return repository.findByMovieID(movieID);
    }

}
