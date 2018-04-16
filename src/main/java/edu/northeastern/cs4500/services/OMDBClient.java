package edu.northeastern.cs4500.services;

import com.google.gson.Gson;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.cs4500.models.CriticRating;
import edu.northeastern.cs4500.models.Movie;
import edu.northeastern.cs4500.models.SearchResult;
import edu.northeastern.cs4500.utils.OMDBException;

@Service
public class OMDBClient {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String URI = "https://www.omdbapi.com/";
    private static final String UNAVAILABLE = "Unavailable";
    private String apiKey;
    private Map<String, Object> defaultParams;
    private Gson gson;

    public OMDBClient() {
        this.apiKey = System.getenv("OMDB_API_KEY");
        initializeDefaultParams();
        this.gson = new Gson();
        Unirest.setTimeouts(0, 0);
    }

    /**
     * Initialize the default parameters for an OMDB request
     */
    private void initializeDefaultParams() {
        this.defaultParams = new HashMap<>();
        this.defaultParams.put("apikey", apiKey);
        this.defaultParams.put("language", "en-US");
        this.defaultParams.put("type", "movie");
    }

    /**
     * Determine if the response from OMDB is valid or an error
     */
    private boolean validResponse(JSONObject obj) {
        return obj.getString("Response").equals("True");
    }

    /**
     * Generates a GET request using the specified params as well
     * as the default parameters
     */
    private JSONObject getRequest(Map<String, Object> params) {
        try {
            HttpResponse<JsonNode> response = Unirest.get(URI)
                    .queryString(this.defaultParams)
                    .queryString(params)
                    .asJson();
            return response.getBody().getObject();
        } catch (UnirestException ex) {
            log.error(ex.toString());
            throw new OMDBException(ex.getMessage());
        }
    }

    /**
     * Search for a list of movies on OMDB
     */
    public List<SearchResult> searchMovie(String query) {
        Map<String, Object> params = new HashMap<>();
        params.put("s", query);

        JSONObject obj = getRequest(params);

        if (validResponse(obj)) {
            JSONArray arr = obj.getJSONArray("Search");
            return Arrays.asList(this.gson.fromJson(arr.toString(), SearchResult[].class));
        } else {
            String error = obj.getString("Error");
            log.error("Error received from OMDB client: {}", error);
            throw new OMDBException(error);
        }
    }

    /**
     * Get a movie by its ID
     */
    public Movie getMovieByID(String movieID) {
        Map<String, Object> params = new HashMap<>();
        params.put("plot", "full");
        params.put("i", movieID);

        JSONObject obj = getRequest(params);
        if (validResponse(obj)) {
            return buildMovieObject(movieID, obj);
        } else {
            String error = obj.getString("Error");
            log.error("Error received from OMDB client: {}", error);
            throw new OMDBException(error);
        }
    }

    /**
     * Builds a Movie object to be returned to the client
     */
    private Movie buildMovieObject(String movieID, JSONObject obj) {
        Movie movie = new Movie();

        try {
            movie.setYear(Integer.parseInt(getString(obj, "Year")));
        } catch (NumberFormatException ex) {
            log.warn("Year for movie {} cannot be parsed into an int: {}",
                    movieID, ex.getLocalizedMessage());
        }

        String released = getString(obj, "Released");
        try {
            movie.setReleased(new SimpleDateFormat("dd MMM yyyy")
                    .parse(released));
        } catch (ParseException ex) {
            log.warn("Released date for movie {} cannot be parsed into the date format: {}",
                    movieID, released);
            movie.setReleased(null);
        }

        movie.setTitle(getString(obj, "Title"));
        movie.setRated(getString(obj, "Rated"));
        movie.setRuntime(getString(obj, "Runtime"));
        movie.setGenres(stringToList(obj, "Genre"));
        movie.setDirectors(stringToList(obj, "Director"));
        movie.setWriters(stringToList(obj, "Writer"));
        movie.setActors(stringToList(obj, "Actors"));
        movie.setPlot(getString(obj, "Plot"));
        movie.setLanguages(stringToList(obj, "Language"));
        movie.setCountries(stringToList(obj, "Country"));
        movie.setAwards(getString(obj, "Awards"));
        movie.setPoster(getString(obj, "Poster"));
        movie.setRatings(Arrays.asList(this.gson.fromJson(obj.getJSONArray("Ratings").toString(),
                        CriticRating[].class)));
        movie.setImdbID(getString(obj, "imdbID"));
        movie.setBoxOffice(getString(obj, "BoxOffice"));
        movie.setProduction(getString(obj, "Production"));
        movie.setWebsite(getString(obj, "Website"));

        return movie;
    }

    /**
     * Gets a specified string from the JSONObject
     */
    private String getString(JSONObject obj, String str) {
        String jsonStr;
        if (obj.has(str)) {
            jsonStr = obj.getString(str).replace("\\\\", "");
            if (isUnavailableData(jsonStr)) {
                jsonStr = UNAVAILABLE;
            }
        } else {
            jsonStr = UNAVAILABLE;
        }

        return jsonStr;
    }

    /**
     * Converts a specified string to a List using a "," as a delimiter
     */
    private List<String> stringToList(JSONObject obj, String str) {
        String s = getString(obj, str);
        if (!s.contains(UNAVAILABLE)) {
            return Arrays.asList(s.split(", "));
        }
        List<String> x = new ArrayList<>();
        x.add(s);
        return x;
    }


    /**
     * Checks if N/A is returned
     */
    private boolean isUnavailableData(String s) {
        return s.equals("N/A");
    }
}
