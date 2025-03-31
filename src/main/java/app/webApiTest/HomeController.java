package app.webApiTest;

import app.cards.model.MyCard;
import app.security.AuthenticationMetadata;
import app.user.model.RoleEnum;
import app.user.model.User;
import app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());
        List<User> allUsers = this.userService.getAllUsers();
        RoleEnum role = user.getRole();
        boolean isAdmin = role == RoleEnum.ADMIN;


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        modelAndView.addObject("user", user);
        modelAndView.addObject("allUsers", allUsers);
        modelAndView.addObject("isAdmin", isAdmin);

        return modelAndView;
    }

    @GetMapping("/my-cards")
    public ModelAndView getMyCards(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());
        List<MyCard> allMyCards = userService.getAllMyCards(user);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("my-cards");
        modelAndView.addObject("allMyCards", allMyCards);




        return modelAndView;
    }
}
