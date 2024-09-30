package quantum.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import quantum.dto.group.*;
import quantum.dto.userGroups.UserGroupsListResponse;
import quantum.model.UserGroup;
import quantum.service.GroupService;
import quantum.web.rest.GroupController;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quantum.constant.TestConstants.SAMPLE_TOKEN;
import static quantum.utils.TestUtils.stringifyObject;

/**
 * Test for {@link GroupController} controller class.
 */

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {GroupController.class})
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class GroupControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected GroupService service;

    @Test
    @DisplayName("Test group controller PATCH")
    void patchGroup() throws Exception {

        UpdateGroupBody input = UpdateGroupBody.builder()
                .name("New Group Name")
                .build();

        // Mock the service update method
        when(service.updateGroup(any(Long.class), any(UpdateGroupBody.class))).thenReturn(new GroupResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/groups/{groupId}", 1L)
                .content(stringifyObject(input))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request and expect no content (204)
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        // Verify that the service was called with the expected arguments
        verify(service, times(1)).updateGroup(any(Long.class), any(UpdateGroupBody.class));
    }

    @Test
    @DisplayName("Test group controller GET group games")
    void getGroupGames() throws Exception {

        when(service.getGroupGames(any(Long.class))).thenReturn(new GroupGamesResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/groups/{group_id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request and expect OK status
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        // Verify the service call
        verify(service, times(1)).getGroupGames(any(Long.class));
    }

    @Test
    @DisplayName("Test group controller GET user groups")
    void getUserGroups() throws Exception {

        when(service.getUserGroups(any(String.class))).thenReturn(new UserGroupsListResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/{username}/groups", "user")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request and expect OK status
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).getUserGroups(any(String.class));
    }

    @Test
    @DisplayName("Test group controller POST")
    void postGroup() throws Exception {

        NewGroupBody input = NewGroupBody.builder()
                .name("New group name")
                .invited_users(new ArrayList<>())
                .build();

        when(service.postGroup(any(String.class), any(NewGroupBody.class))).thenReturn(new GroupResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user/{username}/groups", "user")
                .content(stringifyObject(input))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request and expect OK status
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).postGroup(any(String.class), any(NewGroupBody.class));
    }

    @Test
    @DisplayName("Test group controller SEND INVITE")
    void sendInvite() throws Exception {

        // Mock the service to do nothing
        when(service.sendInvite(any(String.class), any(Long.class))).thenReturn(new UserGroup());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/groups/{group_id}/invite/{username}", 1L, "user")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request and expect no content (204)
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        verify(service, times(1)).sendInvite(any(String.class), any(Long.class));
    }

    @Test
    @DisplayName("Test group controller JOIN group")
    void joinGroup() throws Exception {

        // Mock the service to do nothing
        doNothing().when(service).joinGroup(any(String.class), any(Long.class));

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/user/{username}/groups/{group_id}", "user", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request and expect no content (204)
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        verify(service, times(1)).joinGroup(any(String.class), any(Long.class));
    }

    @Test
    @DisplayName("Test group controller DELETE user group")
    void deleteUserGroup() throws Exception {

        // Mock the service to do nothing
        doNothing().when(service).declineOrExitGroup(any(String.class), any(Long.class));

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/user/{username}/groups/{groupId}", "user", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request and expect no content (204)
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        verify(service, times(1)).declineOrExitGroup(any(String.class), any(Long.class));
    }
}