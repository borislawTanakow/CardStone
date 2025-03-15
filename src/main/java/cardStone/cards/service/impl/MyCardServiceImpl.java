package cardStone.cards.service.impl;

import cardStone.cards.model.Card;
import cardStone.cards.model.MyCard;
import cardStone.cards.repository.CardToBuyRepository;
import cardStone.cards.service.CardService;
import cardStone.cards.service.MyCardService;
import cardStone.deck.model.Deck;
import cardStone.user.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MyCardServiceImpl implements MyCardService {

    private final CardService cardService;
    private final CardToBuyRepository cardToBuyRepository;

    public MyCardServiceImpl(CardService cardService, CardToBuyRepository cardToBuyRepository) {
        this.cardService = cardService;
        this.cardToBuyRepository = cardToBuyRepository;
    }

    @Override
    public void createCardToBuy(UUID cardId, User user) {

        Card card = this.cardService.getById(cardId);

        MyCard cardToBuy = MyCard.builder()
                .name(card.getName())
                .description(card.getDescription())
                .owner(user)
                .imageUrl(card.getImageUrl())
                .power(card.getPower())
                .type(card.getType())
                .build();

        cardToBuyRepository.save(cardToBuy);

    }

    @Override
    public void deleteCardToBuyById(UUID id) {
        cardToBuyRepository.deleteById(id);

    }

    @Override
    public void deleteCardOnMyDeck(UUID cardId, UUID userId) {
        Optional<MyCard> optionalCard = cardToBuyRepository.findById(cardId);

        optionalCard.get().setDeck(null);
        cardToBuyRepository.save(optionalCard.get());
    }


    @Override
    public void addCardToDeck(UUID cardId, User user) {
        Optional<MyCard> optionalCard = cardToBuyRepository.findById(cardId);
        MyCard card = optionalCard.get();
        Deck userDeck = user.getDeck();
        card.setDeck(userDeck);


        cardToBuyRepository.save(card);

    }


}
