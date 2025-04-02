package app.Scheduling;

import app.user.model.User;
import app.user.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DailyRewardScheduler {

    private final UserRepository userRepository;

    public DailyRewardScheduler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 1 * * ?") // Изпълнение всеки ден в 1:00 часа
    public void addDailyStoneCoins() {
        List<User> activeUsers = userRepository.findAll();
        for (User user : activeUsers) {
            user.setStoneCoin(user.getStoneCoin() + 10);
            userRepository.save(user);
        }
    }
}
