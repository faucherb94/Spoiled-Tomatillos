package edu.northeastern.cs4500.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.cs4500.models.CriticRating;
import edu.northeastern.cs4500.models.Movie;
import edu.northeastern.cs4500.utils.OMDBException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"org.apache.http.conn.ssl.*", "javax.net.ssl.*" , "javax.crypto.*"})
@PrepareForTest(Unirest.class)
public class OMDBClientTest {

    private OMDBClient client;

    private Movie movie;

    private String jsonString;

    @Before
    public void setUp() throws ParseException {
        MockitoAnnotations.initMocks(this);
        mockStatic(Unirest.class);
        client = new OMDBClient();
        buildMovieAndJSON();
    }

    private void buildMovieAndJSON() throws ParseException {
        jsonString = new JSONObject()
            .put("Title", "Ocean's 30")
            .put("Year", "2025")
            .put("Rated", "PG-13")
            .put("Released", "06 Apr 2025")
            .put("Runtime", "100 min")
            .put("Genre", "Adventure, Comedy")
            .put("Director", "Director1, Director2")
            .put("Writer", "Writer1, Writer2")
            .put("Actors", "Brad Pitt, George Clooney")
            .put("Plot", "Something happens")
            .put("Language", "English, Spanish")
            .put("Country", "USA")
            .put("Awards", "14837 awards")
            .put("Poster", "http://poster.jpg")
            .put("Ratings", new JSONArray()
                    .put(new JSONObject()
                            .put("Source", "Rotten Tomatoes")
                            .put("Value", "100%"))
                    .put(new JSONObject()
                            .put("Source", "Metacritic")
                            .put("Value", "100/100")))
            .put("imdbID", "tt98472")
            .put("Website", "http://web.com")
            .put("Production", "Production company")
            .put("BoxOffice", "$200")
            .put("Response", "True").toString();

        movie = new Movie();
        movie.setImdbID("tt98472");
        movie.setWebsite("http://web.com");
        movie.setProduction("Production company");
        movie.setBoxOffice("$200");
        movie.setRated("PG-13");

        List<String> actors = new ArrayList<>();
        actors.add("Brad Pitt");
        actors.add("George Clooney");
        movie.setActors(actors);

        movie.setAwards("14837 awards");
        movie.setCountry("USA");
        movie.setPlot("Something happens");
        movie.setYear(2025);
        movie.setPoster("http://poster.jpg");
        movie.setRuntime("100 min");
        movie.setTitle("Ocean's 30");
        movie.setReleased(new SimpleDateFormat("dd MMM yyyy").parse("06 Apr 2025"));

        List<String> directors = new ArrayList<>();
        directors.add("Director1");
        directors.add("Director2");
        movie.setDirectors(directors);

        List<String> genres = new ArrayList<>();
        genres.add("Adventure");
        genres.add("Comedy");
        movie.setGenres(genres);

        List<CriticRating> ratings = new ArrayList<>();
        ratings.add(new CriticRating("Rotten Tomatoes", "100%"));
        ratings.add(new CriticRating("Metacritic", "100/100"));
        movie.setRatings(ratings);

        List<String> languages = new ArrayList<>();
        languages.add("English");
        languages.add("Spanish");
        movie.setLanguages(languages);

        List<String> writers = new ArrayList<>();
        writers.add("Writer1");
        writers.add("Writer2");
        movie.setWriters(writers);
    }

    @Test
    public void getMovieByID_HappyPath() throws Exception {
        com.mashape.unirest.http.JsonNode jsonNode =
                new com.mashape.unirest.http.JsonNode(jsonString);

        HttpResponse<com.mashape.unirest.http.JsonNode> mockResponse = mock(HttpResponse.class);
        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(jsonNode);

        GetRequest getRequest = mock(GetRequest.class);
        PowerMockito.doReturn(getRequest).when(Unirest.class, "get", anyString());
        PowerMockito.when(getRequest.queryString(anyMap())).thenReturn(getRequest);
        PowerMockito.when(getRequest.queryString(anyString(), anyString())).thenReturn(getRequest);
        PowerMockito.doReturn(mockResponse).when(getRequest).asJson();

        Movie movie = client.getMovieByID("tt428973");
        assertThat(movie).isEqualTo(this.movie);
    }

    @Test(expected = OMDBException.class)
    public void getMovieByID_ErrorResponse() throws Exception {
        String jsonString = "{\"Response\":\"False\",\"Error\":\"Not Found\"}";
        com.mashape.unirest.http.JsonNode jsonNode =
                new com.mashape.unirest.http.JsonNode(jsonString);

        HttpResponse<com.mashape.unirest.http.JsonNode> mockResponse = mock(HttpResponse.class);
        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(jsonNode);

        GetRequest getRequest = mock(GetRequest.class);
        PowerMockito.doReturn(getRequest).when(Unirest.class, "get", anyString());
        PowerMockito.when(getRequest.queryString(anyMap())).thenReturn(getRequest);
        PowerMockito.when(getRequest.queryString(anyString(), anyString())).thenReturn(getRequest);
        PowerMockito.doReturn(mockResponse).when(getRequest).asJson();

        client.getMovieByID("tt342987");
    }

    @Test(expected = OMDBException.class)
    public void getMovieByID_ConnectionError() throws Exception {
        GetRequest getRequest = mock(GetRequest.class);
        PowerMockito.doReturn(getRequest).when(Unirest.class, "get", anyString());
        PowerMockito.when(getRequest.queryString(anyMap())).thenReturn(getRequest);
        PowerMockito.when(getRequest.queryString(anyString(), anyString())).thenReturn(getRequest);
        PowerMockito.doThrow(new UnirestException("some error"))
                .when(getRequest).asJson();

        client.getMovieByID("fasldj");
    }

    @Test(expected = OMDBException.class)
    public void getMovieByID_ParseDateError() throws Exception {
        String jsonString = "{\"Response\":\"True\",\"Released\":\"bad date format\"}";
        com.mashape.unirest.http.JsonNode jsonNode =
                new com.mashape.unirest.http.JsonNode(jsonString);

        HttpResponse<com.mashape.unirest.http.JsonNode> mockResponse = mock(HttpResponse.class);
        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(jsonNode);
        GetRequest getRequest = mock(GetRequest.class);
        PowerMockito.doReturn(getRequest).when(Unirest.class, "get", anyString());
        PowerMockito.when(getRequest.queryString(anyMap())).thenReturn(getRequest);
        PowerMockito.when(getRequest.queryString(anyString(), anyString())).thenReturn(getRequest);
        PowerMockito.doReturn(mockResponse).when(getRequest).asJson();

        client.getMovieByID("alksdjf");
    }

}