package cardStone.web;

import cardStone.security.AuthenticationMetadata;
import cardStone.user.model.User;
import cardStone.user.service.UserService;
import cardStone.web.dto.EditProfileRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/edit-profile")
    public ModelAndView getUser(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit-profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("editProfileRequest", new EditProfileRequest());


        return modelAndView;
    }

    @PostMapping("/edit-profile")
    public String editUser(@Valid EditProfileRequest editProfileRequest,
                                    BindingResult bindingResult,
                                    @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        if (bindingResult.hasErrors()) {
            return "edit-profile";
        }
        User user = userService.getById(authenticationMetadata.getUserId());

        userService.editProfile(editProfileRequest, user);

        return "redirect:/home";
    }


    @GetMapping("/profile")
    public ModelAndView viewProfile(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");
        modelAndView.addObject("user", user);

        return modelAndView;
    }

}
