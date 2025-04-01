package app.web;

import app.battle.service.BattleService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class BattleController {

    private final UserService userService;
    private final BattleService battleService;

    @Autowired
    public BattleController(UserService userService, BattleService battleService) {
        this.userService = userService;
        this.battleService = battleService;
    }


    @GetMapping("/battle/{id}")
    public ModelAndView getBattlePage(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());
        User opponent = userService.getById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("battle");
        modelAndView.addObject("user", user);
        modelAndView.addObject("opponent", opponent);


        return modelAndView;
    }



    @PostMapping("/battle/{id}")
    public String battle(@PathVariable UUID id,
                         @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                         RedirectAttributes redirectAttributes) {

        User user = userService.getById(authenticationMetadata.getUserId());

        User winner = battleService.battle(user, id);


        if (winner == null) {
            return "redirect:/draw-page";
        }

        redirectAttributes.addFlashAttribute("winner", winner);

        return "redirect:/win-page";
    }

    @GetMapping("/win-page")
    public ModelAndView showWinner(@ModelAttribute("winner") User winner) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("win-page");

        modelAndView.addObject("winner", winner);


        return modelAndView;
    }

    @GetMapping("/draw-page")
    public String showDraw() {


        return "draw-page";
    }


}
