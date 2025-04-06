package app.web;

import app.cards.service.CardService;
import app.cards.service.MyCardService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;
    private final UserService userService;
    private final MyCardService myCardService;

    @Autowired
    public CardController(CardService cardService, UserService userService, MyCardService cardToBuyService) {
        this.cardService = cardService;
        this.userService = userService;
        this.myCardService = cardToBuyService;
    }


    @PostMapping("/add-to-deck/{id}")
    public String addCardToBuy(@PathVariable UUID id,
                                    @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        myCardService.addCardToDeck(id, user);

        return "redirect:/my-cards";
    }

    @DeleteMapping("/{id}")
    public String deleteCardToBuy(@PathVariable UUID id,  @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        myCardService.deleteMyCardByIdAndGiveHalfSC(id ,user);

        return "redirect:/home";
    }

    @DeleteMapping("deck/{id}")
    public String deleteCardFromDeck(@PathVariable UUID id,
     @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        myCardService.deleteCardOnMyDeck(id, user.getId());

        return "redirect:/home";
    }







}
