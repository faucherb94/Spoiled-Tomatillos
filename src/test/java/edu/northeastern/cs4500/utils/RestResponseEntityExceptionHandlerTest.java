package edu.northeastern.cs4500.utils;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;

import edu.northeastern.cs4500.models.Movie;
import edu.northeastern.cs4500.models.User;
import edu.northeastern.cs4500.services.OMDBClient;
import edu.northeastern.cs4500.services.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestResponseEntityExceptionHandlerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private UserService userService;

    @MockBean
    private OMDBClient omdbClient;

    @Test
    public void handleOMDBException_NotFound() throws Exception {
        when(userService.findByID(anyInt()))
                .thenThrow(new OMDBException("not found"));

        ResponseEntity<Movie> response = restTemplate.getForEntity("/api/users/{id}",
                Movie.class, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void handleOMDBException_Incorrect() throws Exception {
        when(userService.findByID(anyInt()))
                .thenThrow(new OMDBException("incorrect"));

        ResponseEntity<Movie> response = restTemplate.getForEntity("/api/users/{id}",
                Movie.class, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void handleOMDBException_Other() throws Exception {
        when(userService.findByID(anyInt()))
                .thenThrow(new OMDBException("other"));

        ResponseEntity<Movie> response = restTemplate.getForEntity("/api/users/{id}",
                Movie.class, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void handleResourceNotFound() throws Exception {
        when(userService.findByID(anyInt()))
                .thenThrow(new ResourceNotFoundException(
                        User.class, "id", "1"));

        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/{id}",
                User.class, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void handleDataIntegrityViolation_WithConstraintViolation() throws Exception {
        when(userService.create(any(User.class)))
                .thenThrow(new DataIntegrityViolationException("",
                        new ConstraintViolationException("", new SQLException(), "")));

        ResponseEntity<User> user = restTemplate.postForEntity("/api/users",
                new User(), User.class);

        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void handleDataIntegrityViolation_NoConstraintViolation() throws Exception {
        when(userService.create(any(User.class)))
                .thenThrow(new DataIntegrityViolationException("", new Exception()));

        ResponseEntity<User> user = restTemplate.postForEntity("/api/users",
                new User(), User.class);

        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void handleIllegalArgument() throws Exception {
        when(userService.findByID(anyInt()))
                .thenThrow(new IllegalArgumentException("illegal"));

        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/{id}",
                User.class, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void handleUnknownException() throws Exception {
        when(userService.findByID(anyInt()))
                .thenThrow(new MockitoException("random message"));

        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/{id}",
                User.class, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
