package edu.northeastern.cs4500.services;

import edu.northeastern.cs4500.models.MovieRating;

public interface IRatingService {

    MovieRating rateMovie(String movieID, MovieRating rating);

    MovieRating getUserMovieRating(String movieID, int userID);
}
