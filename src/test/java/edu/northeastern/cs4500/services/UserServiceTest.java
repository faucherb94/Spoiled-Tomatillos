package edu.northeastern.cs4500.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.northeastern.cs4500.models.User;
import edu.northeastern.cs4500.repositories.UserRepository;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @TestConfiguration
    static class UserServiceContextConfiguration {
        @Bean
        public IUserService userService() {
            return new UserService();
        }
    }

    @Autowired
    private IUserService userService;

    @MockBean
    private UserRepository userRepository;

    private User defaultUser;
    private int BAD_USER_ID = 9999;

    @Before
    public void setUp() {
        defaultUser = new User("defaultUN", "john", "doe",
                "default@neu.edu", "defaultRole", "hometown");
        defaultUser.setId(4123);
    }

    @Test
    public void findByID_HappyPath() throws Exception {
        when(userRepository.findOne(defaultUser.getId())).thenReturn(defaultUser);

        User foundUser = userService.findByID(defaultUser.getId());

        assertThat(foundUser.getId()).isEqualTo(defaultUser.getId());
        assertThat(foundUser.getUsername()).isEqualTo(defaultUser.getUsername());
        assertThat(foundUser.getFirstName()).isEqualTo(defaultUser.getFirstName());
        assertThat(foundUser.getLastName()).isEqualTo(defaultUser.getLastName());
        assertThat(foundUser.getEmail()).isEqualTo(defaultUser.getEmail());
        assertThat(foundUser.getRole()).isEqualTo(defaultUser.getRole());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByID_NotFound() throws Exception {
        when(userRepository.findOne(BAD_USER_ID)).thenReturn(null);

        userService.findByID(BAD_USER_ID);
    }

    @Test
    public void findByUsername_HappyPath() throws Exception {
        when(userRepository.findByUsername(defaultUser.getUsername())).thenReturn(defaultUser);

        User foundUser = userService.findByUsername(defaultUser.getUsername());

        assertThat(foundUser.getId()).isEqualTo(defaultUser.getId());
        assertThat(foundUser.getUsername()).isEqualTo(defaultUser.getUsername());
        assertThat(foundUser.getFirstName()).isEqualTo(defaultUser.getFirstName());
        assertThat(foundUser.getLastName()).isEqualTo(defaultUser.getLastName());
        assertThat(foundUser.getEmail()).isEqualTo(defaultUser.getEmail());
        assertThat(foundUser.getRole()).isEqualTo(defaultUser.getRole());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByUsername_NotFound() throws Exception {
        String badUsername = "badUsername";
        when(userRepository.findByUsername(badUsername)).thenReturn(null);

        userService.findByUsername(badUsername);
    }

    @Test
    public void create_HappyPath() throws Exception {
        when(userRepository.save(defaultUser)).thenReturn(defaultUser);

        User newUser = userService.create(defaultUser);

        assertThat(newUser).isEqualTo(defaultUser);
    }

    @Test
    public void update_HappyPath() throws Exception {
        when(userRepository.findOne(defaultUser.getId())).thenReturn(defaultUser);
        when(userRepository.save(defaultUser)).thenReturn(defaultUser);

        User updatedUser = userService.update(defaultUser.getId(), defaultUser);

        assertThat(updatedUser).isEqualTo(defaultUser);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void update_NotFound() throws Exception {
        defaultUser.setId(BAD_USER_ID);
        when(userRepository.findOne(defaultUser.getId())).thenReturn(null);

        userService.update(defaultUser.getId(), defaultUser);
    }

    @Test
    public void delete_HappyPath() throws Exception {
        when(userRepository.findOne(defaultUser.getId())).thenReturn(defaultUser);

        User deletedUser = userService.delete(defaultUser.getId());

        verify(userRepository, times(1)).findOne(defaultUser.getId());
        verify(userRepository, times(1)).delete(defaultUser);
        verifyNoMoreInteractions(userRepository);

        assertThat(deletedUser).isEqualTo(defaultUser);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void delete_NotFound() throws Exception {
        when(userRepository.findOne(defaultUser.getId())).thenReturn(null);

        userService.delete(defaultUser.getId());

        verify(userRepository, times(1)).findOne(defaultUser.getId());
        verifyNoMoreInteractions(userRepository);
    }

}