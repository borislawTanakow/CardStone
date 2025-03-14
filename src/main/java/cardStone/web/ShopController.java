package cardStone.web;

import cardStone.cards.model.Card;
import cardStone.cards.model.Type;
import cardStone.cards.service.CardService;
import cardStone.cards.service.MyCardService;
import cardStone.security.AuthenticationMetadata;
import cardStone.user.model.User;
import cardStone.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
public class ShopController {

    private final UserService userService;
    private final CardService cardService;
    private final MyCardService myCardService;

    public ShopController(UserService userService, CardService cardService, MyCardService myCardService) {
        this.userService = userService;
        this.cardService = cardService;
        this.myCardService = myCardService;
    }

    @GetMapping("/shop")
    public ModelAndView getShop(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());
        List<Card> allOtherCards = this.cardService.getAllCard();
        List<Card> goldCards = allOtherCards.stream().filter(card -> card.getType() == Type.GOLD).toList();
        List<Card> rearCards = allOtherCards.stream().filter(card -> card.getType() == Type.RARE).toList();
        List<Card> commonCards = allOtherCards.stream().filter(card -> card.getType() == Type.COMMON).toList();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("shop");
        modelAndView.addObject("goldCards", goldCards);
        modelAndView.addObject("rearCards", rearCards);
        modelAndView.addObject("commonCards", commonCards);
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @PostMapping("/cards/buy-card/{id}")
    public String makeCardToBuyList(@PathVariable UUID id,
                                    @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        myCardService.createCardToBuy(id, user);

        return "redirect:/home";
    }
}
