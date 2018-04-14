package edu.northeastern.cs4500.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.cs4500.models.Movie;
import edu.northeastern.cs4500.utils.OMDBException;

@Service
public class TMDBClient {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String URI = "https://api.themoviedb.org/3";
    private String apiKey;
    private Map<String, Object> defaultParams;

    public TMDBClient() {
        this.apiKey = System.getenv("TMDB_API_KEY");
        initializeDefaultParams();
        Unirest.setTimeouts(0, 0);
    }

    /**
     * Initialize the default parameters for an TMDB request
     */
    private void initializeDefaultParams() {
        this.defaultParams = new HashMap<>();
        this.defaultParams.put("api_key", apiKey);
    }

    /**
     * Get movies now in theaters
     */
    public List<Movie> getNowPlaying() {
        Map<String, Object> params = new HashMap<>();
        params.put("page", "1");
        params.put("language", "en-US");
        params.put("region", "US");

        try {
            HttpResponse<JsonNode> response = Unirest.get(URI + "/movie/now_playing")
                    .queryString(this.defaultParams)
                    .queryString(params)
                    .asJson();
            if (response.getStatus() != 200) {
                throw new OMDBException(response.getBody().toString());
            } else {
                JSONArray results = response.getBody().getObject().getJSONArray("results");
                List<Movie> movies = new ArrayList<>();
                for (int i = 0; i < results.length(); i++) {
                    String imdbID = getImdbID(results.getJSONObject(i).getInt("id"));
                    OMDBClient omdbClient = new OMDBClient();
                    movies.add(omdbClient.getMovieByID(imdbID));
                }
                return movies;
            }
        } catch (UnirestException ex) {
            log.error(ex.toString());
            throw new OMDBException(ex.getMessage());
        }
    }

    /**
     * Get the IMDB ID of the movie
     */
    private String getImdbID(int tmdbID) {
        try {
            HttpResponse<JsonNode> response =
                    Unirest.get(URI + "/movie/" + tmdbID + "/external_ids")
                    .queryString(this.defaultParams)
                    .asJson();
            if (response.getStatus() != 200) {
                throw new OMDBException(response.getBody().toString());
            } else {
                return response.getBody().getObject().getString("imdb_id");
            }
        } catch (UnirestException ex) {
            log.error(ex.toString());
            throw new OMDBException(ex.getMessage());
        }
    }

}
