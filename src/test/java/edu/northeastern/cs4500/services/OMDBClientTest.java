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
import edu.northeastern.cs4500.models.SearchResult;
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

    private String movieJsonString;

    private List<SearchResult> searchResults;

    private String searchJsonString;

    @Before
    public void setUp() throws ParseException {
        MockitoAnnotations.initMocks(this);
        mockStatic(Unirest.class);
        client = new OMDBClient();
        buildMovieAndJSON();
        buildSearchAndJSON();
    }

    private void buildSearchAndJSON() {
        searchJsonString = new JSONObject()
                .put("Search", new JSONArray()
                        .put(new JSONObject()
                                .put("Title", "Shrek")
                                .put("Year", "2001")
                                .put("imdbID", "tt0126029")
                                .put("Type", "movie")
                                .put("Poster", "http://poster.jpg"))
                        .put(new JSONObject()
                                .put("Title", "Shrek 2")
                                .put("Year", "2004")
                                .put("imdbID", "tt0298148")
                                .put("Type", "movie")
                                .put("Poster", "http://poster2.jpg")))
                .put("Response", "True").toString();

        searchResults = new ArrayList<>();
        SearchResult sr1 = new SearchResult(
                "Shrek", "2001", "tt0126029", "movie", "http://poster.jpg");
        searchResults.add(sr1);
        SearchResult sr2 = new SearchResult(
                "Shrek 2", "2004", "tt0298148", "movie", "http://poster2.jpg");
        searchResults.add(sr2);
    }

    private void buildMovieAndJSON() throws ParseException {
        movieJsonString = new JSONObject()
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
        movie.setPlot("Something happens");
        movie.setYear(2025);
        movie.setPoster("http://poster.jpg");
        movie.setRuntime("100 min");
        movie.setTitle("Ocean's 30");
        movie.setReleased(new SimpleDateFormat("dd MMM yyyy").parse("06 Apr 2025"));

        List<String> countries = new ArrayList<>();
        countries.add("USA");
        movie.setCountries(countries);

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
    public void searchMovie_HappyPath() throws Exception {
        com.mashape.unirest.http.JsonNode jsonNode =
                new com.mashape.unirest.http.JsonNode(searchJsonString);

        HttpResponse<com.mashape.unirest.http.JsonNode> mockResponse = mock(HttpResponse.class);
        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(jsonNode);

        GetRequest getRequest = mock(GetRequest.class);
        PowerMockito.doReturn(getRequest).when(Unirest.class, "get", anyString());
        PowerMockito.when(getRequest.queryString(anyMap())).thenReturn(getRequest);
        PowerMockito.when(getRequest.queryString(anyString(), anyString())).thenReturn(getRequest);
        PowerMockito.doReturn(mockResponse).when(getRequest).asJson();

        List<SearchResult> searchResults = client.searchMovie("tt47982");
        assertThat(searchResults).isEqualTo(this.searchResults);
    }

    @Test(expected = OMDBException.class)
    public void searchMovie_ErrorResponse() throws Exception {
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

        client.searchMovie("falskj");
    }

    @Test
    public void getMovieByID_HappyPath() throws Exception {
        com.mashape.unirest.http.JsonNode jsonNode =
                new com.mashape.unirest.http.JsonNode(movieJsonString);

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

    @Test
    public void getMovieByID_UnavailableData() throws Exception {
        com.mashape.unirest.http.JsonNode jsonNode =
                new com.mashape.unirest.http.JsonNode(
                        new JSONObject(movieJsonString)
                                .put("Year", "N/A")
                                .put("Production", "N/A")
                                .put("Released", "bad date")
                                .put("Actors", "N/A").toString());

        HttpResponse<com.mashape.unirest.http.JsonNode> mockResponse = mock(HttpResponse.class);
        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(jsonNode);
        GetRequest getRequest = mock(GetRequest.class);
        PowerMockito.doReturn(getRequest).when(Unirest.class, "get", anyString());
        PowerMockito.when(getRequest.queryString(anyMap())).thenReturn(getRequest);
        PowerMockito.when(getRequest.queryString(anyString(), anyString())).thenReturn(getRequest);
        PowerMockito.doReturn(mockResponse).when(getRequest).asJson();

        Movie movie = client.getMovieByID("alksdjf");
        assertThat(movie.getYear()).isEqualTo(0);
        assertThat(movie.getProduction()).isEqualTo("Unavailable");
        assertThat(movie.getReleased()).isNull();
        assertThat(movie.getActors().get(0)).isEqualTo("Unavailable");
    }

    @Test
    public void getMovieByID_FieldNotReturned() throws Exception {
        String jsonString = "{\"Response\":\"True\", \"Ratings\":[]}";
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

        Movie movie = client.getMovieByID("tt342987");
        assertThat(movie.getPoster()).isEqualTo("Unavailable");
    }

}