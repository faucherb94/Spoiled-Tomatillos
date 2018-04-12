package edu.northeastern.cs4500.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import edu.northeastern.cs4500.models.Relationship;
import edu.northeastern.cs4500.models.User;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Integer> {

    @Query(value = "SELECT ua.* FROM UserAccount ua" +
            " INNER JOIN Relationships r ON ua.UserID = r.UserID2 WHERE r.UserID1 = :UserID",
            nativeQuery = true)
    List<User> getFriends(@Param("UserID") int userID);
}

