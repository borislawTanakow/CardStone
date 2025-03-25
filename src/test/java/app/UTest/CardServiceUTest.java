package app.UTest;

import app.cards.model.Card;
import app.cards.repository.CardRepository;
import app.cards.service.impl.CardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceUTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    public void testGetAllCard() {
        // Подготвяме тестови данни
        Card card1 = Card.builder()
                .name("card 1").build();
        Card card2 = Card.builder()
                .name("card 2").build();
        List<Card> expectedCards = Arrays.asList(card1, card2);

        // Когато извикаш findAll на репозитория, да връща подготвените карти
        when(cardRepository.findAll()).thenReturn(expectedCards);

        // Извикваме метода на услугата
        List<Card> actualCards = cardService.getAllCard();

        // Проверяваме резултата
        assertNotNull(actualCards);
        assertEquals(2, actualCards.size());
        assertEquals(expectedCards, actualCards);
    }

    @Test
    public void testGetByIdCardExists() {
        // Подготвяме тестови данни
        UUID cardId = UUID.randomUUID();
        Card expectedCard = Card.builder()
                .name("card 1")
                .build();
        expectedCard.setId(cardId);

        // Когато се извика findById на cardRepository, да върне подготвената карта
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(expectedCard));

        // Извикваме метода на услугата
        Card actualCard = cardService.getById(cardId);

        // Проверяваме резултата
        assertNotNull(actualCard);
        assertEquals(expectedCard.getId(), actualCard.getId());
        assertEquals(expectedCard.getName(), actualCard.getName());
    }
    @Test
    public void testGetByIdCardDoesNotExist() {
        // Подготвяме тестови данни
        UUID cardId = UUID.randomUUID();

        // Когато се извика findById на cardRepository, да върне празно (не съществуваща карта)
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        // Проверяваме дали методът хвърля изключение
        RuntimeException exception = assertThrows(RuntimeException.class, () -> cardService.getById(cardId));

        // Проверяваме съобщението на изключението
        assertEquals("Card whit id " + cardId + " does not exist", exception.getMessage());
    }
    @Test
    void getCardPrice_ExistingCard_ReturnsHalfPrice() {

        Card mockCard = new Card();
        mockCard.setPrice(100);
        when(cardRepository.findByName("Dragon")).thenReturn(Optional.of(mockCard));

        // Изпълнение & Проверка
        assertEquals(50, cardService.getCardPrice("Dragon"));
    }

    @Test
    void getCardPrice_OddPrice_ReturnsFloorValue() {


        Card mockCard = new Card();
        mockCard.setPrice(99);
        when(cardRepository.findByName("Griffin")).thenReturn(Optional.of(mockCard));

        assertEquals(49, cardService.getCardPrice("Griffin"));
    }

    @Test
    void getCardPrice_ZeroPrice_ReturnsZero() {


        Card mockCard = new Card();
        mockCard.setPrice(0);
        when(cardRepository.findByName("FreeCard")).thenReturn(Optional.of(mockCard));

        assertEquals(0, cardService.getCardPrice("FreeCard"));
    }

    @Test
    void getCardPrice_NonExistingCard_ThrowsException() {

        when(cardRepository.findByName("Ghost")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                cardService.getCardPrice("Ghost")
        );
    }
}





