package edu.northeastern.cs4500.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import edu.northeastern.cs4500.models.Group;
import edu.northeastern.cs4500.services.IGroupService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroupControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private IGroupService groupService;

    private static final String URI = "/api/groups";
    private Group group;

    @Before
    public void setUp() throws Exception {
        group = new Group(521, "A cool group", "This group is for cool people");
        group.setId(9874);
    }

    @Test
    public void create_HappyPath() throws Exception {
        when(groupService.create(group)).thenReturn(group);

        ResponseEntity<Group> response = restTemplate.postForEntity(URI,
                group, Group.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void create_BadCreatorID() throws Exception {
        group.setCreatorID(0);

        ResponseEntity<Group> response = restTemplate.postForEntity(URI, group, Group.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void create_EmptyName() throws Exception {
        group.setName("");

        ResponseEntity<Group> response = restTemplate.postForEntity(URI, group, Group.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}