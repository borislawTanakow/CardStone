package app.cards.service;

import app.exception.BuyCardException;
import app.user.model.User;

import java.util.UUID;


public interface MyCardService {
    void buyCard(UUID id, User user) throws BuyCardException;

    void deleteMyCardByIdAndGiveHalfSC(UUID id, User user);

    void deleteCardOnMyDeck(UUID cardId, UUID userId);


    void addCardToDeck(UUID id, User user);
}
