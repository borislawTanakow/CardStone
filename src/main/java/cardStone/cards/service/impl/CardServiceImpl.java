package cardStone.cards.service.impl;

import cardStone.cards.model.Card;
import cardStone.cards.repository.CardRepository;
import cardStone.cards.service.CardService;
import cardStone.deck.repository.DeckRepository;
import cardStone.user.model.User;
import cardStone.user.repository.UserRepository;
import cardStone.web.dto.EditProfileRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
public class CardServiceImpl implements CardService {


    private final CardRepository cardRepository;
    private final DeckRepository deckRepository;
    private final UserRepository userRepository;

    public CardServiceImpl(CardRepository cardRepository, DeckRepository deckRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.deckRepository = deckRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public void createNewCard(EditProfileRequest createCardRequest, User user) {

//        Card card = Card.builder()
//                .name(createCardRequest.getName())
//                .description(createCardRequest.getDescription())
//                .type(createCardRequest.getType())
//                .imageUrl(createCardRequest.getImageUrl())
//                .power(100)
//                .defence(0)
//                .health(100)
//                .build();
//
//        List<Card> cards = user.getDeck().getCards();
//        Deck userDeck = user.getDeck();
//
//
//
//        userDeck.setOwner(user);
//        user.setDeck(userDeck);
//        card.setDeck(userDeck);
//
//        cards.add(card);
//
//
//        // 4. Запазване на промените
//        userRepository.save(user); // Каскадно запазване на колодата и картите (ако е конфигурирано)
    }

    @Override
    public List<Card> getAllCard() {
        return cardRepository.findAll();
    }

    public Card getById(UUID cardId) {

        return cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException
                ("Card whit id %s does not exist".formatted(cardId)));
    }




}
