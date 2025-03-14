package cardStone.deck.service;

import cardStone.deck.repository.DeckRepository;
import cardStone.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeckService {

    @Autowired
    public DeckService(DeckRepository deckRepository) {
    }


}
