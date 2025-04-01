package app.UTest;

import app.battle.service.BattleService;
import app.cards.model.MyCard;
import app.deck.model.Deck;
import app.matchHistory.client.MatchHistoryClient;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BattleServiceUTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MatchHistoryClient matchHistoryClient;

    @InjectMocks
    private BattleService battleService;


    @Test
    public void testBattleUserWins() {
        UUID opponentId = UUID.randomUUID();
        User user = new User();
        user.setUsername("user");
        user.setCurrentRank(0);
        User opponent = new User();
        opponent.setUsername("opponent");
        opponent.setCurrentRank(0);

        Deck deckUser = new Deck();
        deckUser.setCards(List.of(
                MyCard.builder().power(100).build(),
                MyCard.builder().power(100).build()
        ));
        Deck deckOpponent = new Deck();
        deckOpponent.setCards(List.of(
                MyCard.builder().power(10).build(),
                MyCard.builder().power(10).build()
        ));

        user.setDeck(deckUser);
        opponent.setDeck(deckOpponent);

        user.setStoneCoin(100);
        opponent.setStoneCoin(100);

        when(userService.getById(opponentId)).thenReturn(opponent);

        User winner = battleService.battle(user, opponentId);

        assertEquals(user, winner);
        assertEquals(150, user.getStoneCoin());
        assertEquals(100, opponent.getStoneCoin());

        verify(userRepository).save(user);
        verify(matchHistoryClient, times(2)).saveMatchHistory(any());
    }

    @Test
    public void testBattleOpponentWins() {
        UUID opponentId = UUID.randomUUID();
        User user = new User();
        user.setUsername("user");
        user.setCurrentRank(0);
        User opponent = new User();
        opponent.setUsername("opponent");
        opponent.setCurrentRank(0);

        Deck deckUser = new Deck();
        deckUser.setCards(List.of(
                MyCard.builder().power(10).build(),
                MyCard.builder().power(10).build()
        ));
        Deck deckOpponent = new Deck();
        deckOpponent.setCards(List.of(
                MyCard.builder().power(100).build(),
                MyCard.builder().power(100).build()
        ));

        user.setDeck(deckUser);
        opponent.setDeck(deckOpponent);

        user.setStoneCoin(100);
        opponent.setStoneCoin(100);

        when(userService.getById(opponentId)).thenReturn(opponent);

        User winner = battleService.battle(user, opponentId);

        assertEquals(opponent, winner);
        assertEquals(150, opponent.getStoneCoin());
        assertEquals(100, user.getStoneCoin());

        verify(userRepository).save(opponent);
        verify(matchHistoryClient, times(2)).saveMatchHistory(any());
    }


    @Test
    public void testBattleDraw() {
        UUID opponentId = UUID.randomUUID();
        User user = new User();
        user.setUsername("user");
        User opponent = new User();
        opponent.setUsername("opponent");

        Deck deckUser = new Deck();
        deckUser.setCards(List.of());
        Deck deckOpponent = new Deck();
        deckOpponent.setCards(List.of());

        user.setDeck(deckUser);
        opponent.setDeck(deckOpponent);

        user.setStoneCoin(100);
        opponent.setStoneCoin(100);

        when(userService.getById(opponentId)).thenReturn(opponent);

        User winner = battleService.battle(user, opponentId);

        assertNull(winner);
        assertEquals(120, user.getStoneCoin());
        assertEquals(120, opponent.getStoneCoin());

        verify(userRepository).save(user);
        verify(userRepository).save(opponent);
        verify(matchHistoryClient, times(2)).saveMatchHistory(any());
    }
}
