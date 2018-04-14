package edu.northeastern.cs4500.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
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
public class TMDBClientTest {

    private TMDBClient tmdbClient;

    private static final String URI = "https://api.themoviedb.org/3";

    private List<Movie> movies;

    private String nowPlayingJson;

    private String imdbIDJson;

    private String movieJson;

    private String errorJson;

    private Movie mockMovie;

    @Before
    public void setUp() throws ParseException {
        MockitoAnnotations.initMocks(this);
        mockStatic(Unirest.class);
        tmdbClient = new TMDBClient();

        nowPlayingJson = new JSONObject()
                .put("results", new JSONArray()
                        .put(new JSONObject()
                                .put("id", "347289"))).toString();

        imdbIDJson = new JSONObject()
                .put("imdb_id", "tt98472").toString();

        mockMovie = new Movie();
        mockMovie.setImdbID("tt98472");
        mockMovie.setWebsite("http://web.com");
        mockMovie.setProduction("Production company");
        mockMovie.setBoxOffice("$200");
        mockMovie.setRated("PG-13");

        List<String> actors = new ArrayList<>();
        actors.add("Brad Pitt");
        actors.add("George Clooney");
        mockMovie.setActors(actors);

        mockMovie.setAwards("14837 awards");
        mockMovie.setPlot("Something happens");
        mockMovie.setYear(2025);
        mockMovie.setPoster("http://poster.jpg");
        mockMovie.setRuntime("100 min");
        mockMovie.setTitle("Ocean's 30");
        mockMovie.setReleased(new SimpleDateFormat("dd MMM yyyy").parse("06 Apr 2025"));

        List<String> countries = new ArrayList<>();
        countries.add("USA");
        mockMovie.setCountries(countries);

        List<String> directors = new ArrayList<>();
        directors.add("Director1");
        directors.add("Director2");
        mockMovie.setDirectors(directors);

        List<String> genres = new ArrayList<>();
        genres.add("Adventure");
        genres.add("Comedy");
        mockMovie.setGenres(genres);

        List<CriticRating> ratings = new ArrayList<>();
        ratings.add(new CriticRating("Rotten Tomatoes", "100%"));
        ratings.add(new CriticRating("Metacritic", "100/100"));
        mockMovie.setRatings(ratings);

        List<String> languages = new ArrayList<>();
        languages.add("English");
        languages.add("Spanish");
        mockMovie.setLanguages(languages);

        List<String> writers = new ArrayList<>();
        writers.add("Writer1");
        writers.add("Writer2");
        mockMovie.setWriters(writers);

        movies = new ArrayList<>();
        movies.add(mockMovie);

        movieJson = new JSONObject()
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

        errorJson = new JSONObject()
                .put("Error", "something bad").toString();
    }

    @Test
    public void getNowPlaying_HappyPath() throws Exception {
        com.mashape.unirest.http.JsonNode jsonNode1 =
                new com.mashape.unirest.http.JsonNode(nowPlayingJson);

        HttpResponse<JsonNode> mockResponse1 = mock(HttpResponse.class);
        when(mockResponse1.getStatus()).thenReturn(200);
        when(mockResponse1.getBody()).thenReturn(jsonNode1);

        GetRequest getRequest1 = mock(GetRequest.class);
        PowerMockito.doReturn(getRequest1).when(Unirest.class, "get", URI + "/movie/now_playing");
        PowerMockito.when(getRequest1.queryString(anyMap())).thenReturn(getRequest1);
        PowerMockito.when(getRequest1.queryString(anyString(), anyString())).thenReturn(getRequest1);
        PowerMockito.doReturn(mockResponse1).when(getRequest1).asJson();

        com.mashape.unirest.http.JsonNode jsonNode2 =
                new com.mashape.unirest.http.JsonNode(imdbIDJson);

        HttpResponse<JsonNode> mockResponse2 = mock(HttpResponse.class);
        when(mockResponse2.getStatus()).thenReturn(200);
        when(mockResponse2.getBody()).thenReturn(jsonNode2);

        GetRequest getRequest2 = mock(GetRequest.class);
        PowerMockito.doReturn(getRequest2)
                .when(Unirest.class, "get", URI + "/movie/347289/external_ids");
        PowerMockito.when(getRequest2.queryString(anyMap())).thenReturn(getRequest2);
        PowerMockito.when(getRequest2.queryString(anyString(), anyString())).thenReturn(getRequest2);
        PowerMockito.doReturn(mockResponse2).when(getRequest2).asJson();

        com.mashape.unirest.http.JsonNode jsonNode3 =
                new com.mashape.unirest.http.JsonNode(movieJson);

        HttpResponse<JsonNode> mockResponse3 = mock(HttpResponse.class);
        when(mockResponse3.getStatus()).thenReturn(200);
        when(mockResponse3.getBody()).thenReturn(jsonNode3);

        GetRequest getRequest3 = mock(GetRequest.class);
        PowerMockito.doReturn(getRequest3)
                .when(Unirest.class, "get", "https://www.omdbapi.com/");
        PowerMockito.when(getRequest3.queryString(anyMap())).thenReturn(getRequest3);
        PowerMockito.when(getRequest3.queryString(anyString(), anyString())).thenReturn(getRequest3);
        PowerMockito.doReturn(mockResponse3).when(getRequest3).asJson();

        List<Movie> results = tmdbClient.getNowPlaying();
        assertThat(results).isEqualTo(movies);
    }

    @Test(expected = OMDBException.class)
    public void getNowPlaying_NowPlayingRequestError() throws Exception {
        com.mashape.unirest.http.JsonNode jsonNode =
                new com.mashape.unirest.http.JsonNode(errorJson);

        HttpResponse<JsonNode> mockResponse = mock(HttpResponse.class);
        when(mockResponse.getStatus()).thenReturn(500);
        when(mockResponse.getBody()).thenReturn(jsonNode);

        GetRequest getRequest = mock(GetRequest.class);
        PowerMockito.doReturn(getRequest).when(Unirest.class, "get", URI + "/movie/now_playing");
        PowerMockito.when(getRequest.queryString(anyMap())).thenReturn(getRequest);
        PowerMockito.when(getRequest.queryString(anyString(), anyString())).thenReturn(getRequest);
        PowerMockito.doReturn(mockResponse).when(getRequest).asJson();

        tmdbClient.getNowPlaying();
    }

    @Test(expected = OMDBException.class)
    public void getNowPlaying_GetImdbIDRequestError() throws Exception {
        com.mashape.unirest.http.JsonNode jsonNode1 =
                new com.mashape.unirest.http.JsonNode(nowPlayingJson);

        HttpResponse<JsonNode> mockResponse1 = mock(HttpResponse.class);
        when(mockResponse1.getStatus()).thenReturn(200);
        when(mockResponse1.getBody()).thenReturn(jsonNode1);

        GetRequest getRequest1 = mock(GetRequest.class);
        PowerMockito.doReturn(getRequest1).when(Unirest.class, "get", URI + "/movie/now_playing");
        PowerMockito.when(getRequest1.queryString(anyMap())).thenReturn(getRequest1);
        PowerMockito.when(getRequest1.queryString(anyString(), anyString())).thenReturn(getRequest1);
        PowerMockito.doReturn(mockResponse1).when(getRequest1).asJson();

        com.mashape.unirest.http.JsonNode jsonNode2 =
                new com.mashape.unirest.http.JsonNode(errorJson);

        HttpResponse<JsonNode> mockResponse2 = mock(HttpResponse.class);
        when(mockResponse2.getStatus()).thenReturn(500);
        when(mockResponse2.getBody()).thenReturn(jsonNode2);

        GetRequest getRequest2 = mock(GetRequest.class);
        PowerMockito.doReturn(getRequest2)
                .when(Unirest.class, "get", URI + "/movie/347289/external_ids");
        PowerMockito.when(getRequest2.queryString(anyMap())).thenReturn(getRequest2);
        PowerMockito.when(getRequest2.queryString(anyString(), anyString())).thenReturn(getRequest2);
        PowerMockito.doReturn(mockResponse2).when(getRequest2).asJson();

        tmdbClient.getNowPlaying();
    }

    @Test(expected = OMDBException.class)
    public void getNowPlaying_UnirestExceptionRequest1() throws Exception {
        GetRequest getRequest = mock(GetRequest.class);
        PowerMockito.doReturn(getRequest).when(Unirest.class, "get", anyString());
        PowerMockito.when(getRequest.queryString(anyMap())).thenReturn(getRequest);
        PowerMockito.when(getRequest.queryString(anyString(), anyString())).thenReturn(getRequest);
        PowerMockito.doThrow(new UnirestException("some error"))
                .when(getRequest).asJson();

        tmdbClient.getNowPlaying();
    }

    @Test(expected = OMDBException.class)
    public void getNowPlaying_UnirestExceptionRequest2() throws Exception {
        com.mashape.unirest.http.JsonNode jsonNode1 =
                new com.mashape.unirest.http.JsonNode(nowPlayingJson);

        HttpResponse<JsonNode> mockResponse1 = mock(HttpResponse.class);
        when(mockResponse1.getStatus()).thenReturn(200);
        when(mockResponse1.getBody()).thenReturn(jsonNode1);

        GetRequest getRequest1 = mock(GetRequest.class);
        PowerMockito.doReturn(getRequest1).when(Unirest.class, "get", URI + "/movie/now_playing");
        PowerMockito.when(getRequest1.queryString(anyMap())).thenReturn(getRequest1);
        PowerMockito.when(getRequest1.queryString(anyString(), anyString())).thenReturn(getRequest1);
        PowerMockito.doReturn(mockResponse1).when(getRequest1).asJson();

        GetRequest getRequest2 = mock(GetRequest.class);
        PowerMockito.doReturn(getRequest2).when(Unirest.class, "get", URI + "/movie/347289/external_ids");
        PowerMockito.when(getRequest2.queryString(anyMap())).thenReturn(getRequest2);
        PowerMockito.when(getRequest2.queryString(anyString(), anyString())).thenReturn(getRequest2);
        PowerMockito.doThrow(new UnirestException("some error"))
                .when(getRequest2).asJson();

        tmdbClient.getNowPlaying();
    }
}
