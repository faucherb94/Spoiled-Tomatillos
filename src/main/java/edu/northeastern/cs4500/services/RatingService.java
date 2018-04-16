package edu.northeastern.cs4500.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.cs4500.models.MovieRating;
import edu.northeastern.cs4500.repositories.RatingRepository;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

@Service
public class RatingService implements IRatingService {
	
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RatingRepository repository;

    @Override
    public MovieRating rateMovie(int userID, String movieID, MovieRating rating) {
        rating.setUserID(userID);
        rating.setMovieID(movieID);
        log.info("user id {} rated movie id {} {} stars", userID, movieID, rating.getRating());
        return repository.save(rating);
    }

    @Override
    public MovieRating getUserMovieRating(String movieID, int userID) {
        MovieRating rating = repository.findByMovieIDAndUserID(movieID, userID);
        if (rating == null) {
            log.error("user id {} rating for movie id {} not found", userID, movieID);
            return new MovieRating(movieID, userID, 0);
        }
        log.info("retrieved movie rating for user id {} and movie id {}", userID, movieID);
        return rating;
    }

    @Override
    public MovieRating updateUserMovieRating(String movieID, int userID, MovieRating rating) {
        MovieRating currentRating = repository.findByMovieIDAndUserID(movieID, userID);
        if (currentRating == null) {
            log.error("user id {} rating for movie id {} not found", userID, movieID);
            throw new ResourceNotFoundException(MovieRating.class,
                    "movieID", movieID,
                    "userID", Integer.toString(userID));
        }

        currentRating.setMovieID(movieID);
        currentRating.setUserID(userID);
        currentRating.setRating(rating.getRating());
        log.info("user id {} rating for movie id {} updated to {} stars",
                userID, movieID, rating.getRating());
        return repository.save(currentRating);
    }

}
