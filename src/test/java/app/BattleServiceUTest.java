package app;

import cardStone.battle.service.BattleService;
import cardStone.cards.model.MyCard;
import cardStone.deck.model.Deck;
import cardStone.user.model.User;
import cardStone.user.repository.UserRepository;
import cardStone.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BattleServiceUTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BattleService battleService;


    @Test
    public void testBattleUserWins() {
        // Подготвяме данни за потребителите
        UUID opponentId = UUID.randomUUID();
        User user = new User();
        User opponent = new User();

        // Създаваме колоди с карти за двамата
        MyCard userCard1 = MyCard.builder().power(10).build();
        MyCard userCard2 = MyCard.builder().power(20).build();
        MyCard opponentCard1 = MyCard.builder().power(15).build();
        MyCard opponentCard2 = MyCard.builder().power(10).build();
        Deck deckUser = new Deck();
        Deck deckOpponent = new Deck();
        deckUser.setCards(List.of(userCard1, userCard2));
        deckOpponent.setCards(List.of(opponentCard1, opponentCard2));

        // Добавяме картите към дековете
        user.setDeck(deckUser);
        opponent.setDeck(deckOpponent);

        // Определяме камъчета на потребителите
        user.setStoneCoin(100);
        opponent.setStoneCoin(100);

        // Мокваме метода getById да връща опонента
        when(userService.getById(opponentId)).thenReturn(opponent);

        // Извикваме метода за битка
        User winner = battleService.battle(user, opponentId);

        // Проверяваме дали победителят е потребителя
        assertEquals(user, winner);
        assertEquals(150, user.getStoneCoin());
        assertEquals(100, opponent.getStoneCoin()); // Опонентът не е получил камъчета
    }

    @Test
    public void testBattleOpponentWins() {
        // Подготвяме данни за потребителите
        UUID opponentId = UUID.randomUUID();
        User user = new User();
        User opponent = new User();

        // Създаваме колоди с карти за двамата
        MyCard userCard1 = MyCard.builder().power(10).build();
        MyCard userCard2 = MyCard.builder().power(10).build();
        MyCard opponentCard1 = MyCard.builder().power(20).build();
        MyCard opponentCard2 = MyCard.builder().power(20).build();

        Deck deckUser = new Deck();
        Deck deckOpponent = new Deck();
        deckUser.setCards(List.of(userCard1, userCard2));
        deckOpponent.setCards(List.of(opponentCard1, opponentCard2));

        // Добавяме картите към дековете
        user.setDeck(deckUser);
        opponent.setDeck(deckOpponent);

        // Определяме камъчета на потребителите
        user.setStoneCoin(100);
        opponent.setStoneCoin(100);

        // Мокваме метода getById да връща опонента
        when(userService.getById(opponentId)).thenReturn(opponent);

        // Извикваме метода за битка
        User winner = battleService.battle(user, opponentId);

        // Проверяваме дали победителят е опонента
        assertEquals(opponent, winner);
        assertEquals(150, opponent.getStoneCoin());
        assertEquals(100, user.getStoneCoin()); // Потребителят не е получил камъчета
    }

    @Test
    public void testBattleDraw() {
        // Подготвяме данни за потребителите
        UUID opponentId = UUID.randomUUID();
        User user = new User();
        User opponent = new User();

        // Създаваме колоди с карти за двамата с еднаква сила
        MyCard userCard1 = MyCard.builder().power(20).build();
        MyCard userCard2 = MyCard.builder().power(20).build();
        MyCard opponentCard1 = MyCard.builder().power(20).build();
        MyCard opponentCard2 = MyCard.builder().power(20).build();

        Deck deckUser = new Deck();
        Deck deckOpponent = new Deck();
        deckUser.setCards(List.of(userCard1, userCard2));
        deckOpponent.setCards(List.of(opponentCard1, opponentCard2));

        // Добавяме картите към дековете
        user.setDeck(deckUser);
        opponent.setDeck(deckOpponent);

        // Определяме камъчета на потребителите
        user.setStoneCoin(100);
        opponent.setStoneCoin(100);

        // Мокваме метода getById да връща опонента
        when(userService.getById(opponentId)).thenReturn(opponent);

        // Извикваме метода за битка
        User winner = battleService.battle(user, opponentId);

        // Проверяваме дали няма победител
        assertNull(winner);
        assertEquals(100, user.getStoneCoin());
        assertEquals(100, opponent.getStoneCoin());
    }
}
