package cardStone.cards.service.impl;

import cardStone.cards.model.Card;
import cardStone.cards.model.MyCard;
import cardStone.cards.repository.MyCardRepository;
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
    private final MyCardRepository myCardRepository;

    public MyCardServiceImpl(CardService cardService, MyCardRepository myCardRepository) {
        this.cardService = cardService;
        this.myCardRepository = myCardRepository;
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

        myCardRepository.save(cardToBuy);

    }

    @Override
    public void deleteMyCardById(UUID id) {
        myCardRepository.deleteById(id);

    }

    @Override
    public void deleteCardOnMyDeck(UUID cardId, UUID userId) {
        Optional<MyCard> optionalCard = myCardRepository.findById(cardId);

        optionalCard.get().setDeck(null);
        myCardRepository.save(optionalCard.get());
    }


    @Override
    public void addCardToDeck(UUID cardId, User user) {
        Optional<MyCard> optionalCard = myCardRepository.findById(cardId);
        MyCard card = optionalCard.get();
        Deck userDeck = user.getDeck();
        card.setDeck(userDeck);


        myCardRepository.save(card);

    }


}
