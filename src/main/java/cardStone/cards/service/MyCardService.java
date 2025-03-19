package cardStone.cards.service;

import cardStone.user.model.User;

import java.util.UUID;


public interface MyCardService {
    void createCardToBuy(UUID id, User user);

    void deleteMyCardById(UUID id);

    void deleteCardOnMyDeck(UUID cardId, UUID userId);


    void addCardToDeck(UUID id, User user);
}
