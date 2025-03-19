package cardStone.web;

import cardStone.cards.service.CardService;
import cardStone.cards.service.MyCardService;
import cardStone.security.AuthenticationMetadata;
import cardStone.user.model.User;
import cardStone.user.service.UserService;
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

        return "redirect:/home";
    }

    @DeleteMapping("/{id}")
    public String deleteCardToBuy(@PathVariable UUID id) {

        myCardService.deleteMyCardById(id);

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
