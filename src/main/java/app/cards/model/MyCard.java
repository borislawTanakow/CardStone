package app.cards.model;

import app.deck.model.Deck;
import app.model.BaseModel;
import app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyCard extends BaseModel {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Deck deck;

    @Column
    private double power;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

}
