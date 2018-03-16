package edu.northeastern.cs4500.models;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class MovieRatingTest {

    private MovieRating rating;

    @Before
    public void setUp() {
        rating = new MovieRating("tt0266543", 1, 5);
        rating.setId(42);
    }

    @Test
    public void equalsAndHashCode() throws Exception {
        EqualsVerifier.forClass(MovieRating.class).verify();
    }

    @Test
    public void getId() throws Exception {
        assertEquals(42, rating.getId());
    }

    @Test
    public void getMovieID() throws Exception {
        assertEquals("tt0266543", rating.getMovieID());
    }

    @Test
    public void getUserID() throws Exception {
        assertEquals(1, rating.getUserID());
    }

    @Test
    public void getRating() throws Exception {
        assertEquals(5, rating.getRating());
    }

    @Test
    public void getCreatedAt() throws Exception {
        assertEquals(null, rating.getCreatedAt());
    }

    @Test
    public void getUpdatedAt() throws Exception {
        assertEquals(null, rating.getUpdatedAt());
    }

    @Test
    public void setId() throws Exception {
        rating.setId(789);
        assertEquals(789, rating.getId());
    }

    @Test
    public void setMovieID() throws Exception {
        rating.setMovieID("tt423789");
        assertEquals("tt423789", rating.getMovieID());
    }

    @Test
    public void setUserID() throws Exception {
        rating.setUserID(4327);
        assertEquals(4327, rating.getUserID());
    }

    @Test
    public void setRating() throws Exception {
        rating.setRating(4);
        assertEquals(4, rating.getRating());
    }

}