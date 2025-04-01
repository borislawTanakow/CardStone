package app.UTest;

import app.cards.model.MyCard;
import app.deck.model.Deck;
import app.deck.repository.DeckRepository;
import app.exception.EmailAlreadyExistException;
import app.exception.UsernameAlreadyExistException;
import app.user.model.RoleEnum;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.EditProfileRequest;
import app.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {


    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private DeckRepository deckRepository;

    @InjectMocks
    private UserService userService;

    @ParameterizedTest
    @MethodSource("userRolesArguments")
    void whenChangeUserRole_theCorrectRoleIsAssigned(RoleEnum currentUserRole, RoleEnum expectedUserRole) {

        // Given
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .role(currentUserRole)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        userService.switchRole(userId);

        // Then
        assertEquals(expectedUserRole, user.getRole());
    }

    private static Stream<Arguments> userRolesArguments() {

        return Stream.of(
                Arguments.of(RoleEnum.USER, RoleEnum.ADMIN),
                Arguments.of(RoleEnum.ADMIN, RoleEnum.USER)
        );
    }

    @Test
    void givenExistingUsersInDatabase_whenGetAllUsers_thenReturnThemAllSortedByCurrentRankDesc() {
        // Arrange
        User user1 = new User();
        user1.setCurrentRank(1);
        User user2 = new User();
        user2.setCurrentRank(2);
        List<User> userList = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getCurrentRank()).isEqualTo(2);
        assertThat(users.get(1).getCurrentRank()).isEqualTo(1);
    }

    // Switch status method
    @Test
    void givenUserWithStatusActive_whenSwitchStatus_thenUserStatusBecomeInactive() {

        // Given
        User user = User.builder()
                .id(UUID.randomUUID())
                .isActive(true)
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When
        userService.switchStatus(user.getId());

        // Then
        assertFalse(user.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUserWithStatusInactive_whenSwitchStatus_thenUserStatusBecomeActive() {

        // Given
        User user = User.builder()
                .id(UUID.randomUUID())
                .isActive(false)
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When
        userService.switchStatus(user.getId());

        // Then
        assertTrue(user.isActive());
        verify(userRepository, times(1)).save(user);
    }



    @Test
    void registerUser_ShouldThrowException_WhenUsernameExists() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testUser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");

        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.of(new User()));

        assertThrows(UsernameAlreadyExistException.class, () -> userService.registerUser(registerRequest));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailExists() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testUser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");

        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistException.class, () -> userService.registerUser(registerRequest));
    }
    @Test
    void registerUser_ShouldSaveUserAndDeck_WhenValidRequest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testUser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");

        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        userService.registerUser(registerRequest);

        verify(userRepository, times(1)).save(any(User.class));
        verify(deckRepository, times(1)).save(any(Deck.class));
    }
    @Test
    void getById_WhenUserExists_ShouldReturnUser() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setUsername("Test User");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.getById(userId);

        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
        assertEquals("Test User", foundUser.getUsername());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getById_WhenUserDoesNotExist_ShouldThrowException() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getById(userId));
        assertEquals("User whit id [%s] does not exist.".formatted(userId), exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getAllMyCards_UserWithoutCards_ReturnsEmptyList() {
        User user = mock(User.class);
        when(user.getMyCards()).thenReturn(Collections.emptyList());

        List<MyCard> result = userService.getAllMyCards(user);

        assertTrue(result.isEmpty(), "Трябва да връща празен списък за потребител без карти");
    }
    @Test
    void getAllMyCards_UserWithCards_ReturnsCorrectCards() {
        User user = mock(User.class);
        List<MyCard> expectedCards = List.of(new MyCard(), new MyCard());
        when(user.getMyCards()).thenReturn(expectedCards);

        List<MyCard> result = userService.getAllMyCards(user);


        assertEquals(2, result.size(), "Трябва да връща 2 карти");
        assertSame(expectedCards, result, "Трябва да връща същия списък");

    }
    @Test
    void loadUserByUsername_WhenUserExists_ReturnsCorrectUserDetails() {
        // Подготвяне
        String username = "testUser";
        User mockUser = User.builder()
                .username(username)
                .password("encodedPass")
                .role(RoleEnum.USER)
                .isActive(true)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Действие
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Проверка
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).isEqualTo("encodedPass");
        assertThat(userDetails.isEnabled()).isTrue();
        assertThat(userDetails.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");
    }

    @Test
    void loadUserByUsername_WhenUserNotFound_ThrowsException() {
        // Подготвяне
        String username = "nonExistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Действие & Проверка
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.loadUserByUsername(username)
        );

        assertThat(exception.getMessage())
                .isEqualTo("User with this username does not exist.");
    }

    @Test
    void loadUserByUsername_WhenUserIsInactive_ReturnsDisabledUser() {
        // Подготвяне
        String username = "inactiveUser";
        User inactiveUser = User.builder()
                .username(username)
                .isActive(false)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(inactiveUser));

        // Действие
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Проверка
        assertThat(userDetails.isEnabled()).isFalse();
    }

    @Test
    void loadUserByUsername_WhenAdminUser_ReturnsAdminAuthority() {
        // Подготвяне
        String username = "admin";
        User adminUser = User.builder()
                .username(username)
                .role(RoleEnum.ADMIN)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(adminUser));

        // Действие
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Проверка
        assertThat(userDetails.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_ADMIN");
    }
    @Test
    void editProfile_WhenUserNotFound_ThrowsException() {
        // Подготвяне
        UUID userId = UUID.randomUUID();
        User parameterUser = User.builder().id(userId).build();
        EditProfileRequest request = new EditProfileRequest();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Действие & Проверка
        assertThrows(RuntimeException.class, () -> userService.editProfile(request, parameterUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void editProfile_HappyPath_WhenUserExists_ReturnsCorrectUserDetails() {
        // Подготвяне
        UUID userId = UUID.randomUUID();
        User parameterUser = User.builder().id(userId).build();
        User dbUser = User.builder()
                .id(userId)
                .firstName("Old")
                .lastName("Name")
                .imageUrl("oldimg.jpg")
                .build();

        EditProfileRequest request = new EditProfileRequest();
        request.setFirstName("firstName");
        request.setLastName("lastName");
        request.setImageUrl("new.jpg");

        when(userRepository.findById(userId)).thenReturn(Optional.of(dbUser));

        // Действие
        userService.editProfile(request, parameterUser);

        // Проверка
        assertAll(
                () -> assertEquals(request.getFirstName(), dbUser.getFirstName()),
                () -> assertEquals(request.getLastName(),dbUser.getLastName()),
                () -> assertEquals(request.getImageUrl(), dbUser.getImageUrl())
        );
    }
}




