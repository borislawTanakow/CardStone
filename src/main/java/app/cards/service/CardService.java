package app.cards.service;

import app.cards.model.Card;

import java.util.List;
import java.util.UUID;

public interface CardService {


    List<Card> getAllCard();
    Card getById(UUID cardId);


    int getCardPrice(String name);
}
