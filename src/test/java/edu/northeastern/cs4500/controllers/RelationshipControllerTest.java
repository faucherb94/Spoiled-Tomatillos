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

import edu.northeastern.cs4500.models.Relationship;
import edu.northeastern.cs4500.services.IRelationshipService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RelationshipControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private IRelationshipService relationshipService;

    private static final String URI = "/api/friends";
    private Relationship relationship;

    @Before
    public void setUp() throws Exception {
        relationship = new Relationship(1, 2);
        relationship.setId(500);
    }

    @Test
    public void followUser() throws Exception {
        when(relationshipService.followUser(any(Relationship.class))).thenReturn(relationship);

        ResponseEntity<Relationship> response = restTemplate.postForEntity(
                URI, relationship, Relationship.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}