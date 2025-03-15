package cardStone.cards.model;



import cardStone.deck.model.Deck;
import cardStone.model.BaseModel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card extends BaseModel {


    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String description;

    @Column
    private double power;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column
    private Integer price;





}
