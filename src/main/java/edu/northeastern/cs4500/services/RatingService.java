package edu.northeastern.cs4500.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.cs4500.models.MovieRating;
import edu.northeastern.cs4500.repositories.RatingRepository;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

@Service
public class RatingService implements IRatingService {

    @Autowired
    private RatingRepository repository;

    @Override
    public MovieRating rateMovie(int userID, String movieID, MovieRating rating) {
        rating.setUserID(userID);
        rating.setMovieID(movieID);
        return repository.save(rating);
    }

    @Override
    public MovieRating getUserMovieRating(String movieID, int userID) {
        MovieRating rating = repository.findByMovieIDAndUserID(movieID, userID);
        if (rating == null) {
            throw new ResourceNotFoundException(MovieRating.class,
                    "movieID", movieID,
                    "userID", Integer.toString(userID));
        }
        return rating;
    }

    @Override
    public MovieRating updateUserMovieRating(String movieID, int userID, MovieRating rating) {
        MovieRating currentRating = repository.findByMovieIDAndUserID(movieID, userID);
        if (currentRating == null) {
            throw new ResourceNotFoundException(MovieRating.class,
                    "movieID", movieID,
                    "userID", Integer.toString(userID));
        }

        currentRating.setMovieID(movieID);
        currentRating.setUserID(userID);
        currentRating.setRating(rating.getRating());

        return repository.save(currentRating);
    }

}
