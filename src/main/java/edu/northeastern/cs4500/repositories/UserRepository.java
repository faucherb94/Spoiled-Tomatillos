package edu.northeastern.cs4500.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import edu.northeastern.cs4500.models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUsername(String username);

    List<User> findByUsernameContaining(String query);

}
