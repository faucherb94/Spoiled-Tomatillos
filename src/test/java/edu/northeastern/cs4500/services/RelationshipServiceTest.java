package edu.northeastern.cs4500.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;


import edu.northeastern.cs4500.models.Relationship;
import edu.northeastern.cs4500.models.User;
import edu.northeastern.cs4500.repositories.RelationshipRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
    private IRelationshipService rService;

    @MockBean
    private RelationshipRepository rRepository;

    private User defaultUser0;
    private User defaultUser1;
    private Relationship r;
    
    @Before
    public void setUp() {
    	defaultUser0 = new User("Notorious", "Connor", "McGregor",
                "throwing-dollies@gmail.com", "defaultRole", "Dublin");
        defaultUser0.setId(1);
        
        defaultUser1 = new User("DC", "Daniel", "Cormier",
                "suplex-city@aol.com", "defaultRole", "Atlanta");
        defaultUser1.setId(2);   
        r = new Relationship(1, 2);
        r.setRid(1);
    }

    @Test
    public void TestGetFriends() {
    	this.rService.followUser(r);
    	List<Relationship> friends = this.rRepository.getFriends(1);
    	assertThat(friends.size() == 1);
    }
}