package edu.northeastern.cs4500.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import edu.northeastern.cs4500.models.User;
import edu.northeastern.cs4500.services.IUserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService service;

    /**
     * Create a new user
     */
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return service.create(user);
    }

    /**
     * Get user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserByID(@PathVariable(value = "id") int id) {
        User user = service.findByID(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Get user by username
     */
    @GetMapping
    public ResponseEntity<User> getUserByUsername(
            @RequestParam(value = "name") String username) {
        User user = service.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * Update user
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") int id,
                                           @Valid @RequestBody User u) {
        User updatedUser = service.update(id, u);
        return ResponseEntity.ok(updatedUser);
    }
}
