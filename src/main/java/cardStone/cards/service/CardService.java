package cardStone.cards.service;

import cardStone.cards.model.Card;
import cardStone.user.model.User;
import cardStone.web.dto.EditProfileRequest;

import java.util.List;
import java.util.UUID;

public interface CardService {


    List<Card> getAllCard();
    Card getById(UUID cardId);



}
