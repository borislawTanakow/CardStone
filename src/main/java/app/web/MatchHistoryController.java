package app.web;

import app.matchHistory.client.MatchHistoryClient;
import app.matchHistory.model.MatchHistory;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MatchHistoryController {

    private final MatchHistoryClient matchHistoryClient;
    private final UserService userService;

    // Инжектиране на Feign клиента чрез конструктор
    public MatchHistoryController(MatchHistoryClient matchHistoryClient, UserService userService) {
        this.matchHistoryClient = matchHistoryClient;
        this.userService = userService;
    }

    @GetMapping("/match")
    public ModelAndView getHistoryByPlayerId(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("match-history");
        // Извикване на Feign клиента за получаване на данни
        List<MatchHistory> history = matchHistoryClient.getRecentMatches(user.getId());
        modelAndView.addObject("history", history);
        return modelAndView;
    }

    @PostMapping
    public String saveMatch(
            @ModelAttribute MatchHistory matchHistory) {
        // Извикване на Feign клиента за запис на мач
        matchHistoryClient.saveMatchHistory(matchHistory);
        return "redirect:/match-history";
    }
}