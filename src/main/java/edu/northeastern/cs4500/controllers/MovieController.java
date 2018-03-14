package edu.northeastern.cs4500.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import edu.northeastern.cs4500.models.MovieRating;
import edu.northeastern.cs4500.services.IMovieRatingService;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

    @Autowired
    private IMovieRatingService service;

    @PostMapping("/rating")
    public MovieRating rateMovie(@Valid @RequestBody MovieRating rating) {
        return service.rateMovie(rating);
    }
}
