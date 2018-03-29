package edu.northeastern.cs4500.services;

import edu.northeastern.cs4500.models.MovieRating;

public interface IRatingService {

    MovieRating rateMovie(int userID, String movieID, MovieRating rating);

    MovieRating getUserMovieRating(String movieID, int userID);

    MovieRating updateUserMovieRating(String movieID, int userID, MovieRating rating);
}
