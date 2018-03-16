package edu.northeastern.cs4500.controllers;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;

import edu.northeastern.cs4500.models.User;
import edu.northeastern.cs4500.services.UserService;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private UserService userService;

    private final String URI = "/api/user";
    private User mockUser;

    @Before
    public void setUp() {
        mockUser = new User("un", "fn", "ln", "random@neu.edu", "default", "boston");
    }

    @Test
    public void createUser_HappyPath() throws Exception {
        when(userService.create(any(User.class))).thenReturn(mockUser);

        ResponseEntity<User> user = restTemplate.postForEntity(URI + "/create",
                mockUser, User.class);

        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void createUser_DBConflict() throws Exception {
        when(userService.create(any(User.class)))
                .thenThrow(new DataIntegrityViolationException("",
                        new ConstraintViolationException("", new SQLException(), "")));

        ResponseEntity<User> user = restTemplate.postForEntity(URI + "/create",
                mockUser, User.class);

        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void getUserByID_HappyPath() throws Exception {
        when(userService.findByID(anyInt())).thenReturn(mockUser);

        ResponseEntity<User> user = restTemplate.getForEntity(URI + "/{id}", User.class, 1);

        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.OK);

        User body = user.getBody();
        assertThat(body.getUsername()).isEqualTo(mockUser.getUsername());
        assertThat(body.getFirstName()).isEqualTo(mockUser.getFirstName());
        assertThat(body.getLastName()).isEqualTo(mockUser.getLastName());
        assertThat(body.getEmail()).isEqualTo(mockUser.getEmail());
        assertThat(body.getRole()).isEqualTo(mockUser.getRole());
    }

    @Test
    public void getUserByID_NotFound() throws Exception {
        when(userService.findByID(anyInt()))
                .thenThrow(new ResourceNotFoundException(
                        User.class, "id", "1"));

        ResponseEntity<User> response = restTemplate.getForEntity(URI + "/{id}",
                User.class, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getUserByUsername_HappyPath() throws Exception {
        when(userService.findByUsername(anyString())).thenReturn(mockUser);

        ResponseEntity<User> user = restTemplate.getForEntity(URI + "?name=un", User.class);

        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.OK);

        User body = user.getBody();
        assertThat(body.getUsername()).isEqualTo(mockUser.getUsername());
        assertThat(body.getFirstName()).isEqualTo(mockUser.getFirstName());
        assertThat(body.getLastName()).isEqualTo(mockUser.getLastName());
        assertThat(body.getEmail()).isEqualTo(mockUser.getEmail());
        assertThat(body.getRole()).isEqualTo(mockUser.getRole());
    }

    @Test
    public void getUserByUsername_NotFound() throws Exception {
        when(userService.findByUsername(anyString()))
                .thenThrow(new ResourceNotFoundException(
                        User.class, "username", "un"));

        ResponseEntity<User> response = restTemplate.getForEntity(URI + "?name=un", User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateUser() throws Exception {
        when(userService.update(anyInt(), any(User.class)))
                .thenReturn(mockUser);

        HttpEntity<User> entity = new HttpEntity<>(mockUser, new HttpHeaders());
        ResponseEntity<User> user = restTemplate.exchange(URI + "/{id}", HttpMethod.PUT,
                entity, User.class, 1);

        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateUser_DBConflict() throws Exception {
        when(userService.update(anyInt(), any(User.class)))
                .thenThrow(new DataIntegrityViolationException("",
                        new ConstraintViolationException("", new SQLException(), "")));

        HttpEntity<User> entity = new HttpEntity<>(mockUser, new HttpHeaders());
        ResponseEntity<User> user = restTemplate.exchange(URI + "/{id}", HttpMethod.PUT,
                entity, User.class, 1);

        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

}