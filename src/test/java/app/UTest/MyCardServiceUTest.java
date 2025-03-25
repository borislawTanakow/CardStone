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
import app.user.repository.UserRepository;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyCardServiceImpl myCardService;

    @Test
    void buyCard_SuccessfulPurchase_UpdatesUserAndSavesCard() throws BuyCardException {
        // Подготовка
        User user = new User();
        user.setStoneCoin(150);

        Card card = new Card();
        card.setPrice(100);
        card.setName("Dragon");

        when(cardService.getById(any(UUID.class))).thenReturn(card);

        // Изпълнение
        myCardService.buyCard(UUID.randomUUID(), user);

        // Проверки
        assertEquals(50, user.getStoneCoin());
        verify(userRepository).save(user);
        verify(myCardRepository).save(argThat(myCard ->
                myCard.getName().equals("Dragon") &&
                        myCard.getOwner().equals(user)
        ));
    }

    @Test
    void buyCard_InsufficientFunds_DoesNothing() throws BuyCardException {
        // Подготовка
        User user = new User();
        user.setStoneCoin(50);

        Card card = new Card();
        card.setPrice(100);

        when(cardService.getById(any(UUID.class))).thenReturn(card);

        // Изпълнение
        myCardService.buyCard(UUID.randomUUID(), user);

        // Проверки
        assertEquals(50, user.getStoneCoin());
        verifyNoInteractions(myCardRepository);
        verifyNoInteractions(userRepository);
    }

    @Test
    void buyCard_ExactAmount_SavesCorrectly() throws BuyCardException {
        // Подготовка
        User user = new User();
        user.setStoneCoin(100);

        Card card = new Card();
        card.setPrice(100);

        when(cardService.getById(any(UUID.class))).thenReturn(card);

        // Изпълнение
        myCardService.buyCard(UUID.randomUUID(), user);

        // Проверки
        assertEquals(0, user.getStoneCoin());
        verify(userRepository).save(user);
        verify(myCardRepository).save(any());
    }

    @Test
    void deleteMyCardById_SuccessfulDeletion_UpdatesUserAndDeletesCard() {
        // Подготовка
        UUID cardId = UUID.randomUUID();
        User user = new User();
        user.setStoneCoin(100);

        MyCard myCard = new MyCard();
        myCard.setName("Dragon");

        when(myCardRepository.findById(cardId)).thenReturn(Optional.of(myCard));
        when(cardService.getCardPrice("Dragon")).thenReturn(50);

        // Изпълнение
        myCardService.deleteMyCardById(cardId, user);

        // Проверки
        assertEquals(150, user.getStoneCoin());
        verify(myCardRepository).deleteById(cardId);
        verify(userRepository).save(user);
    }

    @Test
    void deleteMyCardById_ZeroPrice_UpdatesUserCorrectly() {
        // Подготовка
        UUID cardId = UUID.randomUUID();
        User user = new User();
        user.setStoneCoin(200);

        MyCard myCard = new MyCard();
        myCard.setName("FreeCard");

        when(myCardRepository.findById(cardId)).thenReturn(Optional.of(myCard));
        when(cardService.getCardPrice("FreeCard")).thenReturn(0);

        // Изпълнение
        myCardService.deleteMyCardById(cardId, user);

        // Проверки
        assertEquals(200, user.getStoneCoin());
        verify(userRepository).save(user);
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
