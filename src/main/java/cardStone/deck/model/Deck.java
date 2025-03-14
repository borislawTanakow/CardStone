package cardStone.deck.model;


import cardStone.cards.model.MyCard;
import cardStone.model.BaseModel;
import cardStone.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Deck extends BaseModel {

    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MyCard> cards = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private User owner;


}
