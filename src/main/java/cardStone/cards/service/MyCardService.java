package cardStone.cards.service;

import cardStone.exception.BuyCardException;
import cardStone.user.model.User;

import java.util.UUID;


public interface MyCardService {
    void buyCard(UUID id, User user) throws BuyCardException;

    void deleteMyCardById(UUID id, User user);

    void deleteCardOnMyDeck(UUID cardId, UUID userId);


    void addCardToDeck(UUID id, User user);
}
