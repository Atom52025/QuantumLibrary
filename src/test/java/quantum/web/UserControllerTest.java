package quantum.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import quantum.dto.user.*;
import quantum.service.UserService;
import quantum.web.rest.UserController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quantum.constant.TestConstants.SAMPLE_TOKEN;
import static quantum.utils.TestUtils.stringifyObject;

/**
 * Test for {@link UserController} controller class.
 */

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {UserController.class})
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected UserService service;

    /**
     * Test for {@link UserController#getUsers} method.
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test users controller GET")
    void getUsers() throws Exception {

        when(service.getUsers(any(Pageable.class))).thenReturn(new UserListResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users")
                .param("page", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).getUsers(any(Pageable.class));
    }

    /**
     * Test for {@link UserController#getUser(String, String)} method.
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test users controller GET")
    void getUser() throws Exception {

        when(service.getUsers(any(Pageable.class))).thenReturn(new UserListResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/{username}", "user")
                .param("page", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).getUser(any(String.class));
    }

    /**
     * Test for {@link UserController#postUser} method.
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test users controller POST")
    void postUsers() throws Exception {

        NewUserBody input = NewUserBody.builder()
                .username("username")
                .email("email@email.com")
                .password("password")
                .build();

        when(service.postUser(any(NewUserBody.class))).thenReturn(new UserResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users")
                .content(stringifyObject(input))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).postUser(any(NewUserBody.class));
    }

    /**
     * Test for {@link UserController#patchUser} method.
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test user controller PATCH")
    void patchUser() throws Exception {

        UpdateUserBody input = UpdateUserBody.builder()
                .email("email@email.com")
                .image("image")
                .build();

        when(service.updateUser(any(String.class), any(UpdateUserBody.class))).thenReturn(new UserResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/users/{username}", "user")
                .content(stringifyObject(input))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).updateUser(any(String.class), any(UpdateUserBody.class));
    }

    /**
     * Test for {@link UserController#patchUserPassword} method.
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test user password controller PATCH")
    void patchUserPassword() throws Exception {

        UpdatePasswordBody input = UpdatePasswordBody.builder()
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();

        when(service.updatePassword(any(String.class), any(UpdatePasswordBody.class))).thenReturn(new UserResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/users/{username}/password", "user")
                .content(stringifyObject(input))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).updatePassword(any(String.class), any(UpdatePasswordBody.class));
    }

    /**
     * Test for {@link UserController#deleteUser} method.
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test user controller DELETE")
    void deleteUser() throws Exception {

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/users/{username}", "user")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        verify(service, times(1)).deleteUser(any(String.class));
    }
}
