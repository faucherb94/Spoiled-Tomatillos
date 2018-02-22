package edu.northeastern.cs4500.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import edu.northeastern.cs4500.models.User;
import edu.northeastern.cs4500.repositories.UserRepository;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository repository;

    /**
     * Create a new user
     */
    @PostMapping("/create")
    public User createUser(@Valid @RequestBody User user) {

        System.out.println(user);
        return repository.save(user);
    }

    /**
     * Get user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserByID(@PathVariable(value = "id") int id) {
        User user = repository.findOne(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Get user by username
     */
    @GetMapping
    public ResponseEntity<User> getUserByUsername(
            @RequestParam(value = "name") String username) {
        User user = repository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}
