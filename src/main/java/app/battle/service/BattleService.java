package app.battle.service;

import app.cards.model.MyCard;
import app.matchHistory.client.MatchHistoryClient;
import app.matchHistory.model.MatchHistory;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BattleService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final MatchHistoryClient matchHistoryClient;

    @Autowired
    public BattleService(UserService userService, UserRepository userRepository, MatchHistoryClient matchHistoryClient) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.matchHistoryClient = matchHistoryClient;
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
         saveMatchResult(user.getId(), "WIN" , opponent.getUsername(), power, opponentPower, 50);
          return user;
     } else if(power < opponentPower) {
         opponent.setStoneCoin(opponent.getStoneCoin() + 50);
         userRepository.save(opponent);
         saveMatchResult(user.getId(), "LOSE" , opponent.getUsername(), power, opponentPower, 0);
         return opponent;
     }

        //TOOD logika kakvo pravim s pobeditelq ot macha example: vseki user da si ima kolekciq ot to4ki
        //i pri bitka da se iz4izslqva kolko to4ki vzima pobeditelq na baza primerno 10% ot to4kite na zagubiliq
        // a na zagubiliq po maluk procent primerno 5% ot to4kite gi gubi


//        matchHistoryService.save(battleResult);

        return null;
    }

    public void saveMatchResult(UUID userId, String result, String opponentName, double myPower, double opponentPower, int sc) {
        MatchHistory matchHistory = MatchHistory.builder()
                .userId(userId)
                .status(result)
                .opponent(opponentName)
                .myPower(myPower)
                .opponentPower(opponentPower)
                .stoneCoins(sc)
                .build();
        matchHistoryClient.saveMatchHistory(matchHistory);
    }
}
