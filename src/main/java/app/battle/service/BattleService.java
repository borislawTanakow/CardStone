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
import java.util.Random;
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
            double bonus = powerBonus(power);
            power += bonus;
        }

        for (MyCard card : opponentCards) {
            opponentPower += card.getPower();
            double bonus = powerBonus(opponentPower);
            opponentPower += bonus;
        }

     if(power > opponentPower) {
         user.setStoneCoin(user.getStoneCoin() + 50);
         user.setCurrentRank(user.getCurrentRank() + 10);
         userRepository.save(user);
         saveMatchResult(user.getId(), "WIN" , opponent.getUsername(), power, opponentPower, 50);
         saveMatchResult(opponent.getId(), "LOSE" , user.getUsername(), opponentPower, power, 0);
          return user;
     } else if(power < opponentPower) {
         opponent.setStoneCoin(opponent.getStoneCoin() + 50);
         opponent.setCurrentRank(user.getCurrentRank() + 10);
         userRepository.save(opponent);
         saveMatchResult(user.getId(), "LOSE" , opponent.getUsername(), power, opponentPower, 0);
         saveMatchResult(opponent.getId(), "WIN" , user.getUsername(), opponentPower, power, 50);
         return opponent;
     }

        user.setStoneCoin(user.getStoneCoin() + 20);
        opponent.setStoneCoin(opponent.getStoneCoin() + 20);
        userRepository.save(user);
        userRepository.save(opponent);

        saveMatchResult(user.getId(), "DRAW" , opponent.getUsername(), power, opponentPower, 20);
        saveMatchResult(opponent.getId(), "DRAW" , user.getUsername(), opponentPower, power, 20);



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

    private double powerBonus(double power) {
        Random random = new Random();
        int bonus = 0;

        if (power >= 0 && power <= 100) {
            bonus = random.nextInt(51);  //от 0 до 50
        } else if (power > 100 && power <= 200) {
            bonus = 50 + random.nextInt(51);  // от 50 до 100
        } else if (power > 200 && power <= 400) {
            bonus = 100 + random.nextInt(101);  // 100 до 200
        } else if (power > 400 && power <= 600) {
            bonus = 200 + random.nextInt(101);  // 200 до 300
        } else if (power > 600) {
            bonus = 300 + random.nextInt(301);  // 300 до 600
        }

       return bonus;
    }
}
