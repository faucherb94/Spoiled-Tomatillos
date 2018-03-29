package edu.northeastern.cs4500.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import edu.northeastern.cs4500.models.MovieRating;

@Repository
public interface RatingRepository extends JpaRepository<MovieRating,Integer> {

    MovieRating findByMovieIDAndUserID(String movieID, int userID);

    List<MovieRating> findByUserIDOrderByUpdatedAtDesc(int userID);

}
