package cardStone.user.model;


import cardStone.cards.model.MyCard;
import cardStone.deck.model.Deck;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Data
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

    private String firstName;

    private String lastName;

    private String imageUrl;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<MyCard> cardToBuys;

    private RoleEnum role;

    private boolean isActive;

    private Integer stoneCoin;

    @OneToOne(cascade = CascadeType.ALL)
    private Deck deck;
}
