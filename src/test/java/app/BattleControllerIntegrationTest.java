package app;

import app.battle.service.BattleService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BattleControllerIntegrationTest{

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private BattleService battleService;

    private User createUser(UUID id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);

        return user;
    }

    @Test
    @WithMockUser(username = "bok", roles = {"USER"})
    public void testShowWinnerPage() throws Exception {
        User winner = createUser(UUID.randomUUID(), "winnerUser");

        mockMvc.perform(get("/win-page")
                        .flashAttr("winner", winner))
                .andExpect(status().isOk())
                .andExpect(model().attribute("winner", winner))
                .andExpect(view().name("win-page"));
    }

    @Test
    @WithMockUser(username = "bok", roles = {"USER"})
    public void testShowDrawPage() throws Exception {
        mockMvc.perform(get("/draw-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("draw-page"));
    }


    @Test
    public void testGetBattlePage() throws Exception {
        UUID opponentId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        // Създаване на AuthenticationMetadata с нужния userId
        AuthenticationMetadata authMetadata = new AuthenticationMetadata();
        authMetadata.setUserId(userId);

        // Създаване на dummy потребители
        User user = createUser(userId, "bok");
        User opponent = createUser(opponentId, "opponent");

        // Stub-ване на методите в userService
        when(userService.getById(userId)).thenReturn(user);
        when(userService.getById(opponentId)).thenReturn(opponent);

        // Създаваме аутентикационен токен, който използва authMetadata като principal
        TestingAuthenticationToken authToken = new TestingAuthenticationToken(authMetadata, null);
        authToken.setAuthenticated(true);

        mockMvc.perform(get("/battle/" + opponentId.toString())
                        .with(authentication(authToken)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("opponent", opponent))
                .andExpect(view().name("battle"));
    }



    @Test
    public void testPostBattleWinner() throws Exception {
        UUID opponentId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        // Създаване на AuthenticationMetadata с нужния userId
        AuthenticationMetadata authMetadata = new AuthenticationMetadata();
        authMetadata.setUserId(userId);

        // Създаваме потребител и победител (тук победителят е същият като потребителя)
        User user = createUser(userId, "bok");
        User winner = createUser(userId, "bok");

        // Stub-ване на методите в userService и battleService
        when(userService.getById(userId)).thenReturn(user);
        when(battleService.battle(eq(user), eq(opponentId))).thenReturn(winner);

        // Създаваме TestingAuthenticationToken с authMetadata като principal
        TestingAuthenticationToken authToken = new TestingAuthenticationToken(authMetadata, null);
        authToken.setAuthenticated(true);

        mockMvc.perform(post("/battle/" + opponentId.toString())
                        .with(authentication(authToken))
                        .with(csrf()))  // добавяне на CSRF токен
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/win-page"));
    }




    @Test
    public void testPostBattleDraw() throws Exception {
        UUID opponentId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        // Създаване на AuthenticationMetadata с нужния userId
        AuthenticationMetadata authMetadata = new AuthenticationMetadata();
        authMetadata.setUserId(userId);

        // Създаване на потребител
        User user = createUser(userId, "bok");

        when(userService.getById(userId)).thenReturn(user);
        // Симулираме draw чрез връщане на null
        when(battleService.battle(eq(user), eq(opponentId))).thenReturn(null);

        // Създаваме TestingAuthenticationToken, който съдържа authMetadata като principal
        TestingAuthenticationToken authToken = new TestingAuthenticationToken(authMetadata, null);
        authToken.setAuthenticated(true);

        mockMvc.perform(post("/battle/" + opponentId.toString())
                        .with(authentication(authToken))
                        .with(csrf())) // добавяне на валиден CSRF токен
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/draw-page"));
    }


}
