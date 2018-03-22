package edu.northeastern.cs4500.services;

import java.util.List;

import edu.northeastern.cs4500.models.MovieReview;

public interface IReviewService {

    MovieReview reviewMovie(int userID, String movieID, MovieReview review);

    MovieReview getUserMovieReview(String movieID, int userID);

    List<MovieReview> getMovieReviews(String movieID);

}
