package edu.northeastern.cs4500.controllers;

import com.mashape.unirest.http.exceptions.UnirestException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import edu.northeastern.cs4500.models.SearchResult;
import edu.northeastern.cs4500.services.OMDBClient;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @GetMapping
    public ResponseEntity<List<SearchResult>> search(@RequestParam(value = "q") String query) {
        if (query.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            OMDBClient client = new OMDBClient();
            List<SearchResult> results = client.searchMovie(query);
            return ResponseEntity.ok(results);
        } catch (UnirestException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
