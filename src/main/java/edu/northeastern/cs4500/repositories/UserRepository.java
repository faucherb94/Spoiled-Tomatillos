package edu.northeastern.cs4500.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.northeastern.cs4500.models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUsername(String username);
    
    @Query(value = "SELECT Username, FirstName, LastName, Email, Role, Hometown, DisplayPicture, CreatedAt, UpdatedAt FROM Relationships JOIN UserAccount ON Relationships.MemberID = :UserID", nativeQuery = true)
    List<User> getFriends(@Param("UserID") int id);
    
    @Query(value = "INSERT INTO Relationships (UserID, MemberID) VALUES (:uID, :fID)", nativeQuery = true)
    void followUser(@Param("uID") int uID, @Param("fID") int fID);
}
