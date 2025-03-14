//package cardStone.battle.service;
//
//import cardStone.cards.model.Card;
//import cardStone.cards.model.MyCard;
//import cardStone.deck.service.DeckService;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class BattleService {
//
//    private final DeckService deckService;
//
//    public BattleService(DeckService deckService) {
//        this.deckService = deckService;
//    }
//
//    public String battle(UUID deckId, UUID opponentDeckId) {
//        var deck = deckService.getByUUID(deckId).orElseThrow();
//        var opponentDeck = deckService.getByUUID(opponentDeckId).orElseThrow();
//
//        List<MyCard> cardList= deck.getCards();
//        List<MyCard> opponentCardList = opponentDeck.getCards();
//
//        double power = 0;
//        double opponentPower = 0;
//        for (MyCard card : cardList) {
////            if (card.effect) {
////                power += effect.apply(card.getPower())
////            }
//            power += card.getPower();
//        }
//
//        for (MyCard card : opponentCardList) {
//            opponentPower += card.getPower();
//        }
//
//        var koieposilen = Math.koiimapovechetochki(power, opponentPower);
//
//        //TOOD logika kakvo pravim s pobeditelq ot macha example: vseki user da si ima kolekciq ot to4ki
//        //i pri bitka da se iz4izslqva kolko to4ki vzima pobeditelq na baza primerno 10% ot to4kite na zagubiliq
//        // a na zagubiliq po maluk procent primerno 5% ot to4kite gi gubi
//
//
////        matchHistoryService.save(battleResult);
//
//        return "battle!";
//    }
//}
