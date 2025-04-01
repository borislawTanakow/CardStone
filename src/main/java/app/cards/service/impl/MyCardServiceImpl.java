package app.cards.service.impl;

import app.cards.model.Card;
import app.cards.model.MyCard;
import app.cards.repository.MyCardRepository;
import app.cards.service.CardService;
import app.cards.service.MyCardService;
import app.deck.model.Deck;
import app.exception.BuyCardException;
import app.user.model.User;
import app.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MyCardServiceImpl implements MyCardService {

    private final CardService cardService;
    private final MyCardRepository myCardRepository;
    private final UserRepository userRepository;

    public MyCardServiceImpl(CardService cardService, MyCardRepository myCardRepository, UserRepository userRepository) {
        this.cardService = cardService;
        this.myCardRepository = myCardRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void buyCard(UUID cardId, User user) throws BuyCardException {

        Card card = this.cardService.getById(cardId);
        Integer price = card.getPrice();
        Integer stoneCoin = user.getStoneCoin();

        if (stoneCoin < price) {
//            throw new BuyCardException("You don't have enough money for this card %s.".formatted(card.getName()));
            return;
        }
        user.setStoneCoin(stoneCoin - price);

        MyCard cardToBuy = MyCard.builder()
                .name(card.getName())
                .description(card.getDescription())
                .owner(user)
                .imageUrl(card.getImageUrl())
                .power(card.getPower())
                .type(card.getType())
                .build();

        myCardRepository.save(cardToBuy);
        userRepository.save(user);

    }


    @Override
    public void deleteMyCardByIdAndGiveHalfSC(UUID id, User user) {
        MyCard myCard = myCardRepository.findById(id).get();
        if (myCard.getDeck() != null) {
            return;
        }
        int price = cardService.getCardPrice(myCard.getName());
        Integer stoneCoin = user.getStoneCoin();
        stoneCoin += price;
        user.setStoneCoin(stoneCoin);

        userRepository.save(user);

        deleteCardOnMyCards(id);

    }

    private void deleteCardOnMyCards(UUID id) {
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
        if (userDeck.getCards().size() >= 3) {
            return;
        }
        card.setDeck(userDeck);


        myCardRepository.save(card);

    }


}
