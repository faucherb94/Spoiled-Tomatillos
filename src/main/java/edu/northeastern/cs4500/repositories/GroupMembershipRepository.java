package edu.northeastern.cs4500.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.northeastern.cs4500.models.GroupMembership;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership,Integer> {

    GroupMembership findByGroupIDAndUserID(int groupID, int userID);

}
