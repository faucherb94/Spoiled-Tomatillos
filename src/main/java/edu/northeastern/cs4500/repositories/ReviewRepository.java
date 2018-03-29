package edu.northeastern.cs4500.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import edu.northeastern.cs4500.models.MovieReview;

@Repository
public interface ReviewRepository extends JpaRepository<MovieReview, Integer> {

    MovieReview findByMovieIDAndUserID(String movieID, int userID);

    List<MovieReview> findByMovieID(String movieID);

    List<MovieReview> findByUserIDOrderByUpdatedAtDesc(int userID);

}
