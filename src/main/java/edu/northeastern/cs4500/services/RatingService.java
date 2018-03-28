package edu.northeastern.cs4500.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.cs4500.models.MovieRating;
import edu.northeastern.cs4500.repositories.RatingRepository;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RatingService implements IRatingService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RatingRepository repository;

    @Override
    public MovieRating rateMovie(int userID, String movieID, MovieRating rating) {
        rating.setUserID(userID);
        rating.setMovieID(movieID);
        log.info("user id " + userID + "rated movie " + movieID + " with a " + rating + " rating");
        return repository.save(rating);
    }

    @Override
    public MovieRating getUserMovieRating(String movieID, int userID) {
        MovieRating rating = repository.findByMovieIDAndUserID(movieID, userID);
        if (rating == null) {
			log.error("user with id " + userID + " rating of movie with id " + movieID + " not found");
            throw new ResourceNotFoundException(MovieRating.class,
                    "movieID", movieID,
                    "userID", Integer.toString(userID));
        }
        log.info("ratings from user with id " + userID + " from movie with id " + movieID + " requested");
        return rating;
    }

    @Override
    public MovieRating updateUserMovieRating(String movieID, int userID, MovieRating rating) {
        MovieRating currentRating = repository.findByMovieIDAndUserID(movieID, userID);
        if (currentRating == null) {
			log.error("user with id " + userID + " rating of movie with id " + movieID + " not found");
            throw new ResourceNotFoundException(MovieRating.class,
                    "movieID", movieID,
                    "userID", Integer.toString(userID));
        }

        currentRating.setMovieID(movieID);
        currentRating.setUserID(userID);
        currentRating.setRating(rating.getRating());
        log.info("user id " + userID + " rating for movie with id " + movieID + " updated with a " + rating);
        return repository.save(currentRating);
    }

}
