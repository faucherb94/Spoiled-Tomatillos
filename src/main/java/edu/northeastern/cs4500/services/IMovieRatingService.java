package edu.northeastern.cs4500.services;

import edu.northeastern.cs4500.models.MovieRating;

public interface IMovieRatingService {

    MovieRating rateMovie(MovieRating rating);

    MovieRating getUserMovieRating(String movieID, int userID);
}
