package cardStone.web;

import cardStone.cards.service.CardService;
import cardStone.user.service.UserService;
import cardStone.web.dto.LoginRequest;
import cardStone.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class IndexController {

    private final UserService userService;
    private final CardService cardService;

    @Autowired
    public IndexController(UserService userService, CardService cardService) {
        this.userService = userService;
        this.cardService = cardService;

    }

    @GetMapping("/")
    public String getIndexPage() {

        return "index";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());

        return modelAndView;
    }

    @PostMapping("/register")
    public String processRegisterRequest(@Valid RegisterRequest registerRequest,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {

            return "/register";
        }

        userService.registerUser(registerRequest);


        return "redirect:/login";

    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(@RequestParam(value = "error", required = false) String errorParam) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        if (errorParam != null) {
            modelAndView.addObject("errorMessage", "Incorrect username or password!");
        }

        return modelAndView;
    }









}

