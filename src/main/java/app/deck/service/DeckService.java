package app.deck.service;

import app.deck.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeckService {

    @Autowired
    public DeckService(DeckRepository deckRepository) {
    }


}
