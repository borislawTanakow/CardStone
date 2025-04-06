package app.user.model;


import app.cards.model.MyCard;
import app.deck.model.Deck;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<MyCard> myCards;

    @Column
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "stone_Coin")
    private Integer stoneCoin;

    @Column(name = "current_rank")
    private Integer currentRank;

    @OneToOne(cascade = CascadeType.ALL)
    private Deck deck;

    //TODO energy трябва да добавя енергия за битки която на всеки нов ден да се рестартира
}
