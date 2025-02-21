package quantum.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import quantum.dto.auth.AuthResponse;
import quantum.dto.auth.LogInBody;
import quantum.dto.auth.SignUpBody;
import quantum.service.AuthService;
import quantum.web.rest.AuthController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quantum.utils.TestUtils.stringifyObject;

/**
 * Test for {@link AuthController} controller class.
 */

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {AuthController.class})
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected AuthService service;

    @Test
    @DisplayName("Test Auth controller POST (logIn)")
    void logIn() throws Exception {

        LogInBody input = LogInBody.builder()
                .username("username")
                .password("password")
                .build();

        when(service.logIn(any(LogInBody.class))).thenReturn(new AuthResponse());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/login")
                .content(stringifyObject(input))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).logIn(any(LogInBody.class));
    }

    @Test
    @DisplayName("Test Auth controller POST (signUp)")
    void signUp() throws Exception {

        SignUpBody input = SignUpBody.builder()
                .username("username")
                .email("email@email.com")
                .password("password")
                .build();

        when(service.signUp(any(SignUpBody.class))).thenReturn(new AuthResponse());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/signup")
                .content(stringifyObject(input))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).signUp(any(SignUpBody.class));
    }
}
