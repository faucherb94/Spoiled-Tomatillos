package edu.northeastern.cs4500.services;

import com.google.gson.Gson;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private String apiKey;
    private Map<String, Object> defaultParams;
    private Gson gson;

    public OMDBClient() {
        this.apiKey = System.getenv("OMDB_API_KEY");
        initializeDefaultParams();
        this.gson = new Gson();
        Unirest.setTimeouts(3000, 10000);
        Unirest.setObjectMapper(new ObjectMapper() {

            @Override
            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return gson.fromJson(value, valueType);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String writeValue(Object o) {
                try {
                    return gson.toJson(o);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void initializeDefaultParams() {
        this.defaultParams = new HashMap<>();
        this.defaultParams.put("apikey", apiKey);
        this.defaultParams.put("language", "en-US");
        this.defaultParams.put("type", "movie");
    }

    private boolean validResponse(JSONObject obj) {
        return obj.getString("Response").equals("True");
    }

    public List<SearchResult> searchMovie(String query) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(URI)
                .queryString(this.defaultParams)
                .queryString("s", query)
                .asJson();
        JSONObject obj = response.getBody().getObject();
        if (validResponse(obj)) {
            JSONArray arr = obj.getJSONArray("Search");
            return Arrays.asList(this.gson.fromJson(arr.toString(), SearchResult[].class));
        } else {
            String error = obj.getString("Error");
            throw new UnirestException(error); // TODO: make custom exception type
        }
    }

    /**
     * Get a movie by its ID
     */
    public Movie getMovieByID(String movieID) {
        try {
            HttpResponse<JsonNode> response = Unirest.get(URI)
                    .queryString(this.defaultParams)
                    .queryString("plot", "full")
                    .queryString("i", movieID)
                    .asJson();
            JSONObject obj = response.getBody().getObject();
            if (validResponse(obj)) {
                return buildMovieObject(obj);
            } else {
                String error = obj.getString("Error");
                throw new OMDBException(error);
            }
        } catch (UnirestException ex) {
            log.error(ex.toString());
            throw new OMDBException(ex.getMessage());
        }
    }

    /**
     * Builds a Movie object to be returned to the client
     */
    private Movie buildMovieObject(JSONObject obj) {
        Movie movie = new Movie();

        try {
            movie.setYear(Integer.parseInt(getString(obj, "Year")));
        } catch (NumberFormatException ex) {
            log.error("Year returned by OMDB cannot be parsed into an int: " +
                    ex.getLocalizedMessage());
        }

        try {
            movie.setReleased(new SimpleDateFormat("dd MMM yyyy")
                    .parse(getString(obj, "Released")));
        } catch (ParseException ex) {
            throw new OMDBException(ex.getMessage());
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
        movie.setCountry(getString(obj, "Country"));
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

    private String getString(JSONObject obj, String str) {
        try {
            return obj.getString(str).replaceAll("\\\\", "");
        } catch (JSONException ex) {
            log.warn("The " + str + " movie field was not returned by OMDB." +
                    "Setting value to be empty.");
            return "";
        }
    }

    private List<String> stringToList(JSONObject obj, String str) {
        String s = obj.getString(str);
        return Arrays.asList(s.split(", "));
    }
}
