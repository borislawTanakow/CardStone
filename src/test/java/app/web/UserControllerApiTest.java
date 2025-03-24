package app.web;

import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.EditProfileRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void getEditProfilePage_WithAuthenticatedUser_ReturnsEditProfileView() throws Exception {
        UUID userId = UUID.randomUUID();

        AuthenticationMetadata authMetadata = mock(AuthenticationMetadata.class);
        when(authMetadata.getUserId()).thenReturn(userId);

        User mockUser = new User();
        when(userService.getById(userId)).thenReturn(mockUser);

        mockMvc.perform(get("/edit-profile")
                        .with(authentication(new TestingAuthenticationToken(authMetadata, null, "ROLE_USER")))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-profile"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("editProfileRequest"));

        verify(userService).getById(userId);
    }


    @Test
    @WithMockUser
    void postEditProfile_WithInvalidData_ReturnsEditProfileView() throws Exception {
        // Arrange
        EditProfileRequest invalidRequest = new EditProfileRequest(); // Празни полета

        // Act & Assert
        mockMvc.perform(post("/edit-profile")
                        .with(csrf())
                        .flashAttr("editProfileRequest", invalidRequest))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-profile"));

        verifyNoInteractions(userService);
    }

    @Test
    void viewProfile_WithAuthenticatedUser_ReturnsProfileView() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        AuthenticationMetadata authMetadata = mock(AuthenticationMetadata.class);
        when(authMetadata.getUserId()).thenReturn(userId);

        User mockUser = new User();
        mockUser.setId(userId);
        when(userService.getById(userId)).thenReturn(mockUser);

        // Act & Assert
        mockMvc.perform(get("/profile")
                        .with(authentication(new TestingAuthenticationToken(authMetadata, null, "ROLE_USER")))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", mockUser));

        verify(userService).getById(userId);
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    void switchUserStatus_AsAdmin_RedirectsToUsers() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(put("/users/{id}/status", userId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService).switchStatus(userId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void switchUserRole_AsAdmin_RedirectsToUsers() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(put("/users/{id}/role", userId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService).switchRole(userId);
    }
}