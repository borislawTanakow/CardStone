package cardStone.battle.service;

import cardStone.cards.model.Card;
import cardStone.cards.model.MyCard;
import cardStone.deck.service.DeckService;
import cardStone.user.model.User;
import cardStone.user.repository.UserRepository;
import cardStone.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BattleService {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public BattleService(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public User battle(User user, UUID opponentId) {


        User opponent = userService.getById(opponentId);

        List<MyCard> userCards= user.getDeck().getCards();
        List<MyCard> opponentCards = opponent.getDeck().getCards();

        double power = 0;
        double opponentPower = 0;

        for (MyCard card : userCards) {
            power += card.getPower();
        }

        for (MyCard card : opponentCards) {
            opponentPower += card.getPower();
        }

     if(power > opponentPower) {
         user.setStoneCoin(user.getStoneCoin() + 50);
         userRepository.save(user);
          return user;
     } else if(power < opponentPower) {
         opponent.setStoneCoin(opponent.getStoneCoin() + 50);
         userRepository.save(opponent);
         return opponent;
     }

        //TOOD logika kakvo pravim s pobeditelq ot macha example: vseki user da si ima kolekciq ot to4ki
        //i pri bitka da se iz4izslqva kolko to4ki vzima pobeditelq na baza primerno 10% ot to4kite na zagubiliq
        // a na zagubiliq po maluk procent primerno 5% ot to4kite gi gubi


//        matchHistoryService.save(battleResult);

        return null;
    }
}
