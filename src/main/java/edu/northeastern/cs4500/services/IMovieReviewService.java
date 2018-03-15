package edu.northeastern.cs4500.services;

import edu.northeastern.cs4500.models.MovieReview;

public interface IMovieReviewService {

    MovieReview reviewMovie(MovieReview review);

    MovieReview getUserMovieReview(String movieID, int userID);

}
