package edu.northeastern.cs4500.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.cs4500.models.Group;
import edu.northeastern.cs4500.models.GroupMembership;
import edu.northeastern.cs4500.services.IGroupService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
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
    private List<Group> groups;
    private GroupMembership groupMembership;

    @Before
    public void setUp() throws Exception {
        group = new Group(521, "A cool group", "This group is for cool people");
        group.setId(9874);

        groups = new ArrayList<>();
        groups.add(group);

        groupMembership = new GroupMembership(5, 10);
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

    @Test
    public void getGroupByID_HappyPath() throws Exception {
        when(groupService.getByID(group.getId())).thenReturn(group);

        ResponseEntity<Group> response = restTemplate.getForEntity(URI + "/{id}", Group.class,
                group.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getGroupByID_BadGroupID() throws Exception {
        group.setId(0);

        ResponseEntity<Group> response = restTemplate.getForEntity(URI + "/{id}", Group.class,
                group.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getGroupsByCreatorID_HappyPath() throws Exception {
        when(groupService.getByCreatorID(group.getCreatorID())).thenReturn(groups);

        ResponseEntity<List<Group>> response = restTemplate.exchange(URI + "?creator-id=10",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Group>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void joinGroup_HappyPath() throws Exception {
        when(groupService.joinGroup(anyInt(), anyInt())).thenReturn(groupMembership);

        ResponseEntity<GroupMembership> response = restTemplate.postForEntity(
                URI + "/{id}/users/{user-id}", null, GroupMembership.class, 5, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}