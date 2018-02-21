package edu.northeastern.cs4500.services;

import com.google.gson.Gson;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.cs4500.models.SearchResult;

public class OMDBClient {

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

    private boolean validResponse(HttpResponse<JsonNode> response) {
        JSONObject obj = response.getBody().getObject();
        return obj.getString("Response").equals("True");
    }

    public List<SearchResult> searchMovie(String query) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(URI)
                .queryString(this.defaultParams)
                .queryString("s", query)
                .asJson();
        if (validResponse(response)) {
            JSONArray arr = response.getBody().getObject().getJSONArray("Search");
            return Arrays.asList(this.gson.fromJson(arr.toString(), SearchResult[].class));
        } else {
            String error = response.getBody().getObject().getString("Error");
            throw new UnirestException(error); // TODO: make custom exception type
        }
    }
}
