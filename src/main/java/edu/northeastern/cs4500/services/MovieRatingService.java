package edu.northeastern.cs4500.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.cs4500.models.MovieRating;
import edu.northeastern.cs4500.repositories.MovieRatingRepository;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

@Service
public class MovieRatingService implements IMovieRatingService {

    @Autowired
    private MovieRatingRepository repository;

    @Override
    public MovieRating rateMovie(MovieRating rating) {
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

}
