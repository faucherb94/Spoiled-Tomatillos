package edu.northeastern.cs4500.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import edu.northeastern.cs4500.models.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group,Integer> {

    List<Group> findByCreatorID(int creatorID);

    @Query(value = "SELECT g.* FROM Groups g" +
            " INNER JOIN GroupMemberships gm ON gm.GroupID = g.GroupID WHERE gm.UserID = :UserID",
            nativeQuery = true)
    List<Group> findByUserMembership(@Param("UserID") int userID);

}
