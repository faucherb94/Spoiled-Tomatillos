package edu.northeastern.cs4500.models;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class MovieReviewTest {

    private MovieReview review;

    private final String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
            "Phasellus imperdiet, tortor in malesuada scelerisque, elit odio blandit dui," +
            "vitae aliquam nunc libero et nunc. Pellentesque feugiat sapien nec lorem" +
            "ullamcorper ornare. Phasellus fermentum egestas diam, ac convallis libero" +
            "consequat vitae. Cras euismod augue non ex mollis sagittis." +
            "Donec nec fringilla massa, eu laoreet massa." +
            "Cras sodales tortor nec risus rutrum, non elementum lorem dapibus." +
            "Pellentesque bibendum felis vitae quam commodo, sit amet finibus ipsum fermentum.";

    @Before
    public void setUp() throws Exception {
        review = new MovieReview("tt0266543", 5, text);
        review.setId(894);
    }

    @Test
    public void equalsAndHashCode() throws Exception {
        EqualsVerifier.forClass(MovieReview.class).verify();
    }

    @Test
    public void getId() throws Exception {
        assertEquals(894, review.getId());
    }

    @Test
    public void getMovieID() throws Exception {
        assertEquals("tt0266543", review.getMovieID());
    }

    @Test
    public void getUserID() throws Exception {
        assertEquals(5, review.getUserID());
    }

    @Test
    public void getReview() throws Exception {
        assertEquals(text, review.getReview());
    }

    @Test
    public void getCreatedAt() throws Exception {
        assertEquals(null, review.getCreatedAt());
    }

    @Test
    public void getUpdatedAt() throws Exception {
        assertEquals(null, review.getUpdatedAt());
    }

    @Test
    public void setId() throws Exception {
        review.setId(98423);
        assertEquals(98423, review.getId());
    }

    @Test
    public void setMovieID() throws Exception {
        review.setMovieID("tt9847239");
        assertEquals("tt9847239", review.getMovieID());
    }

    @Test
    public void setUserID() throws Exception {
        review.setUserID(2);
        assertEquals(2, review.getUserID());
    }

    @Test
    public void setReview() throws Exception {
        review.setReview("this movie sucked");
        assertEquals("this movie sucked", review.getReview());
    }

}