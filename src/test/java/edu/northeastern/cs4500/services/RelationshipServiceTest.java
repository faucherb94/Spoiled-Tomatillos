package edu.northeastern.cs4500.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.cs4500.models.Relationship;
import edu.northeastern.cs4500.models.User;
import edu.northeastern.cs4500.repositories.RelationshipRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RelationshipServiceTest {

    @TestConfiguration
    static class RelationshipServiceContextConfiguration {
        @Bean
        public IRelationshipService RelationshipService() {
            return new RelationshipService();
        }
    }

    @Autowired
    private IRelationshipService relationshipService;

    @MockBean
    private RelationshipRepository relationshipRepository;

    private User defaultUser0;
    private User defaultUser1;
    private Relationship r;
    private List<User> userList;
    
    @Before
    public void setUp() {
    	defaultUser0 = new User("Notorious", "Connor", "McGregor",
                "throwing-dollies@gmail.com", "defaultRole", "Dublin", "linktopic.com");
        defaultUser0.setId(1);
        
        defaultUser1 = new User("DC", "Daniel", "Cormier",
                "suplex-city@aol.com", "defaultRole", "Atlanta", "linktopic.com");
        defaultUser1.setId(2);   
        r = new Relationship(1, 2);
        r.setId(1);

        userList = new ArrayList<>();
        userList.add(defaultUser0);
        userList.add(defaultUser1);
    }

    @Test
    public void getFriends_HappyPath() throws Exception {
        when(relationshipRepository.getFriends(anyInt())).thenReturn(userList);

        List<User> users = relationshipService.getFriends(10);

        assertThat(users).isEqualTo(userList);
    }

    @Test
    public void followUser_HappyPath() throws Exception {
        when(relationshipRepository.save(any(Relationship.class))).thenReturn(r);

        Relationship newRelationship = relationshipService.followUser(r);

        assertThat(newRelationship).isEqualTo(r);
    }
}