package app.UTest;

import app.cards.model.Card;
import app.cards.model.MyCard;
import app.cards.model.Type;
import app.cards.repository.MyCardRepository;
import app.cards.service.CardService;
import app.cards.service.impl.MyCardServiceImpl;
import app.deck.model.Deck;
import app.exception.BuyCardException;
import app.user.model.User;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyCardServiceUTest {

    @Mock
    private CardService cardService;

    @Mock
    private MyCardRepository myCardRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MyCardServiceImpl myCardService;

    @Test
    public void testCreateCardToBuy() throws BuyCardException {
        // Подготвяме тестови данни
        UUID cardId = UUID.randomUUID();
        User user = new User();
        user.setId(UUID.randomUUID()); // Примерно попълване на потребителя
        Card card = Card.builder()
                .name("Card Name")
                .description("Card Description")
                .imageUrl("image/url")
                .power(10)
                .type(Type.GOLD)
                .build();

        // Когато се извика getById, да върне подготвената карта
        when(cardService.getById(cardId)).thenReturn(card);

        // Създаваме MyCard обект, който се очаква да бъде създаден и запазен
        MyCard expectedCardToBuy = MyCard.builder()
                .name(card.getName())
                .description(card.getDescription())
                .owner(user)
                .imageUrl(card.getImageUrl())
                .power(card.getPower())
                .type(card.getType())
                .build();

        // Извикваме метода за създаване на карта за покупка
        myCardService.buyCard(cardId, user);

        // Проверяваме дали save е бил извикан с правилния обект
        verify(myCardRepository, times(1)).save(expectedCardToBuy);
    }

    @Test
    public void testDeleteCardToBuyById() {
        // Подготвяме тестови данни
        UUID cardId = UUID.randomUUID();
        User user = new User();

        // Извикваме метода, който трябва да изтрие картата
        myCardService.deleteMyCardById(cardId, user);

        // Проверяваме дали методът deleteById е извикан точно веднъж с правилния идентификатор
        verify(myCardRepository, times(1)).deleteById(cardId);
    }

    @Test
    public void testDeleteCardOnMyDeck() {
        // Подготвяме тестови данни
        UUID cardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MyCard card = MyCard.builder()
                .deck(new Deck())
                .build();
        card.setId(cardId);

        // Когато се извика findById с cardId, да върне Optional с картата
        when(myCardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Извикваме метода, който трябва да премахне картата от дек
        myCardService.deleteCardOnMyDeck(cardId, userId);

        // Проверяваме дали е извикано findById точно веднъж
        verify(myCardRepository, times(1)).findById(cardId);

        // Проверяваме дали deck е зададено на null
        assertNull(card.getDeck());

        // Проверяваме дали методът save е извикан с правилната карта
        verify(myCardRepository, times(1)).save(card);
    }

    @Test
    public void testAddCardToDeck() {
        // Подготвяме тестови данни
        UUID cardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        // Създаваме карта, която ще бъде добавена в дек
        MyCard card = MyCard.builder()
                .deck(null)  // Инициализираме без дек
                .build();
        card.setId(cardId);

        // Създаваме дек за потребителя
        Deck userDeck = new Deck();

        // Създаваме потребител
        User user = new User();
        user.setDeck(userDeck);

        // Подготвяме мока за findById
        when(myCardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Извикваме метода
        myCardService.addCardToDeck(cardId, user);

        // Проверяваме дали е извикано findById точно веднъж
        verify(myCardRepository, times(1)).findById(cardId);

        // Проверяваме дали картата е добавена в правилния дек
        assertEquals(userDeck, card.getDeck());

        // Проверяваме дали методът save е извикан с картата
        verify(myCardRepository, times(1)).save(card);
    }

}
