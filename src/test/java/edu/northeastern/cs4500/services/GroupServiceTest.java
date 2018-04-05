package edu.northeastern.cs4500.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.northeastern.cs4500.models.Group;
import edu.northeastern.cs4500.repositories.GroupRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class GroupServiceTest {

    @TestConfiguration
    static class GroupServiceContextConfiguration {
        @Bean
        public IGroupService groupService() {
            return new GroupService();
        }
    }

    @Autowired
    private IGroupService groupService;

    @MockBean
    private GroupRepository groupRepository;

    private Group group;

    @Before
    public void setUp() {
        group = new Group(10, "Cool group", "This group is cool");
        group.setId(423);
    }

    @Test
    public void create_HappyPath() throws Exception {
        when(groupRepository.save(group)).thenReturn(group);

        Group newGroup = groupService.create(group);

        assertThat(newGroup).isEqualTo(group);
    }

}