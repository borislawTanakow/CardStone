package cardStone.user.service;

import cardStone.cards.model.MyCard;
import cardStone.deck.model.Deck;
import cardStone.deck.repository.DeckRepository;
import cardStone.exception.EmailAlreadyExistException;
import cardStone.exception.UsernameAlreadyExistException;
import cardStone.security.AuthenticationMetadata;
import cardStone.user.model.RoleEnum;
import cardStone.user.model.User;
import cardStone.user.repository.UserRepository;
import cardStone.web.dto.EditProfileRequest;
import cardStone.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DeckRepository deckRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, DeckRepository deckRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.deckRepository = deckRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User with this username does not exist."));

        return new AuthenticationMetadata(user.getId(), username, user.getPassword());
    }

    public void registerUser(RegisterRequest registerRequest) {

        Optional<User> optionUser = userRepository.findByUsername(registerRequest.getUsername());
        if (optionUser.isPresent()) {
            throw new UsernameAlreadyExistException("Username %s already exist.".formatted(registerRequest.getUsername()));
        }

        Optional<User> OptionalEmailUser = userRepository.findByEmail(registerRequest.getEmail());
        if (OptionalEmailUser.isPresent()) {
            throw new EmailAlreadyExistException("Email %s already exist.".formatted(registerRequest.getEmail()));
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .isActive(true)
                .role(RoleEnum.USER)
                .stoneCoin(50)
                .build();

        Deck deck = Deck.builder().build();
        deck.setOwner(user);

        user.setDeck(deck);

        userRepository.save(user);

        deckRepository.save(deck);
    }




    public User getById(UUID userId) {

        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException(
                "User whit id [%s] does not exist.".formatted(userId)));
    }

    public List<User> getAllUsers() {
            return userRepository.findAll();
    }

    public List<MyCard> getAllMyCards(User user) {
        return user.getCardToBuys();
    }

    public void editProfile(EditProfileRequest editProfileRequest, User user) {

        User editUser = getById(user.getId());
        editUser.setFirstName(editProfileRequest.getFirstName());
        editUser.setLastName(editProfileRequest.getLastName());
        editUser.setImageUrl(editProfileRequest.getImageUrl());

        userRepository.save(editUser);

    }
}
