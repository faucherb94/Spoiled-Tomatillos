package edu.northeastern.cs4500.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        user = new User("un", "fn", "ln", "email@neu.edu", "default", "boston");
        user.setId(42738);
    }

    @Test
    public void getId() throws Exception {
        assertEquals(42738, user.getId());
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals("un", user.getUsername());
    }

    @Test
    public void getFirstName() throws Exception {
        assertEquals("fn", user.getFirstName());
    }

    @Test
    public void getLastName() throws Exception {
        assertEquals("ln", user.getLastName());
    }

    @Test
    public void getEmail() throws Exception {
        assertEquals("email@neu.edu", user.getEmail());
    }

    @Test
    public void getRole() throws Exception {
        assertEquals("default", user.getRole());
    }

    @Test
    public void getHometown() throws Exception {
        assertEquals("boston", user.getHometown());
    }

    @Test
    public void getPicture() throws Exception {
        assertEquals(null, user.getPicture());
    }

    @Test
    public void getCreatedAt() throws Exception {
        assertEquals(null, user.getCreatedAt());
    }

    @Test
    public void getUpdatedAt() throws Exception {
        assertEquals(null, user.getUpdatedAt());
    }

    @Test
    public void setId() throws Exception {
        user.setId(23218);
        assertEquals(23218, user.getId());
    }

    @Test
    public void setUsername() throws Exception {
        user.setUsername("newUsername");
        assertEquals("newUsername", user.getUsername());
    }

    @Test
    public void setFirstName() throws Exception {
        user.setFirstName("newFirstName");
        assertEquals("newFirstName", user.getFirstName());
    }

    @Test
    public void setLastName() throws Exception {
        user.setLastName("newLastName");
        assertEquals("newLastName", user.getLastName());
    }

    @Test
    public void setEmail() throws Exception {
        user.setEmail("newEmail@neu.edu");
        assertEquals("newEmail@neu.edu", user.getEmail());
    }

    @Test
    public void setRole() throws Exception {
        user.setRole("admin");
        assertEquals("admin", user.getRole());
    }

    @Test
    public void setHometown() throws Exception {
        user.setHometown("new york");
        assertEquals("new york", user.getHometown());
    }

    @Test
    public void setPicture() throws Exception {
        byte[] newPic = new byte[]{};
        user.setPicture(newPic);
        assertEquals(newPic, user.getPicture());
    }

    @Test
    public void equals() throws Exception {
        User user2 = new User(user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getHometown());
        user2.setId(user.getId());

        assertTrue(user.equals(user2) && user2.equals(user));

        user2.setId(48792);
        assertFalse(user.equals(user2));
    }

    @Test
    public void testHashCode() throws Exception {
        User user2 = new User(user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getHometown());
        user2.setId(user.getId());

        assertTrue(user.hashCode() == user2.hashCode());
    }

    @Test
    public void canEqual() throws Exception {
        assertFalse(user.canEqual(new Object()));
    }

    @Test
    public void testToString() throws Exception {
        String expected = "User(id=" + user.getId() +
                ", username=" + user.getUsername() +
                ", firstName=" + user.getFirstName() +
                ", lastName=" + user.getLastName() +
                ", email=" + user.getEmail() +
                ", role=" + user.getRole() +
                ", hometown=" + user.getHometown() +
                ", picture=null, createdAt=null, updatedAt=null)";

        assertEquals(expected, user.toString());
    }

}