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
import java.util.Optional;
import java.util.UUID;


@Service
public class CardServiceImpl implements CardService {


    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }




    @Override
    public List<Card> getAllCard() {
        return cardRepository.findAll();
    }

    public Card getById(UUID cardId) {

        return cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException
                ("Card whit id %s does not exist".formatted(cardId)));
    }

    @Override
    public int getCardPrice(String name) {
        Optional<Card> optionalCard = cardRepository.findByName(name);
        Card card = optionalCard.get();
        Integer price = card.getPrice();
       return price /= 2;

    }


}
