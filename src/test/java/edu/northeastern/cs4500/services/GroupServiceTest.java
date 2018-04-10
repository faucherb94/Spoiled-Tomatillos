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

import edu.northeastern.cs4500.models.Group;
import edu.northeastern.cs4500.models.GroupMembership;
import edu.northeastern.cs4500.repositories.GroupMembershipRepository;
import edu.northeastern.cs4500.repositories.GroupRepository;
import edu.northeastern.cs4500.utils.ResourceNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
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

    @MockBean
    private GroupMembershipRepository groupMembershipRepository;

    private Group group;
    private List<Group> groups;
    private GroupMembership groupMembership;

    @Before
    public void setUp() {
        group = new Group(10, "Cool group", "This group is cool");
        group.setId(423);

        groups = new ArrayList<>();
        groups.add(group);

        groupMembership = new GroupMembership(1, 2);
    }

    @Test
    public void create_HappyPath() throws Exception {
        when(groupRepository.save(group)).thenReturn(group);

        Group newGroup = groupService.create(group);

        assertThat(newGroup).isEqualTo(group);
    }

    @Test
    public void getByID_HappyPath() throws Exception {
        when(groupRepository.findOne(group.getId())).thenReturn(group);

        Group retrievedGroup = groupService.getByID(group.getId());

        assertThat(retrievedGroup).isEqualTo(group);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getByID_NotFound() throws Exception {
        when(groupRepository.findOne(50)).thenReturn(null);

        groupService.getByID(50);
    }

    @Test
    public void getByCreatorID_HappyPath() throws Exception {
        when(groupRepository.findByCreatorID(group.getCreatorID())).thenReturn(groups);

        List<Group> retrievedGroups = groupService.getByCreatorID(group.getCreatorID());

        assertThat(retrievedGroups).isEqualTo(groups);
    }

    @Test
    public void getUserGroupMemberships_HappyPath() throws Exception {
        when(groupRepository.findByUserMembership(50)).thenReturn(groups);

        List<Group> retrievedGroups = groupService.getUserGroupMemberships(50);

        assertThat(retrievedGroups).isEqualTo(groups);
    }

    @Test
    public void joinGroup_HappyPath() throws Exception {
        List<Group> createdGroups = new ArrayList<>();
        createdGroups.add(group);
        when(groupMembershipRepository.save(any(GroupMembership.class)))
                .thenReturn(groupMembership);
        when(groupService.getByCreatorID(2)).thenReturn(createdGroups);

        GroupMembership createdGM = groupService.joinGroup(1, 2);

        assertThat(createdGM).isEqualTo(groupMembership);
    }

    @Test(expected = IllegalArgumentException.class)
    public void joinGroup_UserIsCreator() throws Exception {
        List<Group> createdGroups = new ArrayList<>();
        Group g = new Group(2, "", "");
        g.setId(10);
        createdGroups.add(g);
        when(groupService.getByCreatorID(2)).thenReturn(createdGroups);

        groupService.joinGroup(10, 2);
    }

}