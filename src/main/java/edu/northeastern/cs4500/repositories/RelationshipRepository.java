package edu.northeastern.cs4500.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.northeastern.cs4500.models.Relationship;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Integer> {
	
	@Query("SELECT * from Relationships WHERE UserID1 = ?1")
    List<Relationship> getFriends(int uid1);
    
}

