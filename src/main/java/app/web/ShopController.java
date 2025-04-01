package app.web;

import app.cards.model.Card;
import app.cards.model.Type;
import app.cards.service.CardService;
import app.cards.service.MyCardService;
import app.exception.BuyCardException;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public ModelAndView getShop(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, RedirectAttributes redirectAttributes) {

        User user = userService.getById(authenticationMetadata.getUserId());
        List<Card> allOtherCards = this.cardService.getAllCard();
        List<Card> goldCards = allOtherCards.stream().filter(card -> card.getType() == Type.GOLD).toList();
        List<Card> rearCards = allOtherCards.stream().filter(card -> card.getType() == Type.RARE).toList();
        List<Card> commonCards = allOtherCards.stream().filter(card -> card.getType() == Type.COMMON).toList();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("shop");
        modelAndView.addObject(redirectAttributes);
        modelAndView.addObject("goldCards", goldCards);
        modelAndView.addObject("rearCards", rearCards);
        modelAndView.addObject("commonCards", commonCards);
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @PostMapping("/cards/buy-card/{id}")
    public String buyCardToBuyList(@PathVariable UUID id,
                                    @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) throws BuyCardException {

        User user = userService.getById(authenticationMetadata.getUserId());

        myCardService.buyCard(id, user);

        return "redirect:/shop";
    }
}
