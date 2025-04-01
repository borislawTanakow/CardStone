package app.init;

import app.cards.model.Card;
import app.cards.model.Type;
import app.cards.repository.CardRepository;
import app.deck.model.Deck;
import app.user.model.RoleEnum;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitCardAndUser implements CommandLineRunner {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitCardAndUser(CardRepository cardRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private List<User> createUsers() {
        List<User> users = new ArrayList<>();

        User user1 = User.builder()
                .username("damgass")
                .email("moko@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .isActive(true)
                .deck(new Deck())
                .firstName("Boko")
                .lastName("Tabakov")
                .imageUrl("https://gratisography.com/wp-content/uploads/2024/11/gratisography-augmented-reality-800x525.jpg")
                .role(RoleEnum.ADMIN)
                .stoneCoin(100)
                .currentRank(0)
                .build();

        User user2 = User.builder()
                .username("alek")
                .email("moko123@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .isActive(true)
                .deck(new Deck())
                .firstName("Alek")
                .lastName("Goshov")
                .imageUrl("https://img-cdn.pixlr.com/image-generator/history/65bb506dcb310754719cf81f/ede935de-1138-4f66-8ed7-44bd16efc709/medium.webp")
                .role(RoleEnum.USER)
                .stoneCoin(100)
                .currentRank(0)
                .build();

        User user3 = User.builder()
                .username("Bea")
                .email("bea@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .isActive(true)
                .deck(new Deck())
                .firstName("Bea")
                .lastName("Tabakova")
                .imageUrl("https://babyboxfamily.com/cdn/shop/articles/ausstattung-schlafen-co-746426.jpg?crop=center&height=700&v=1692918758&width=800")
                .role(RoleEnum.USER)
                .stoneCoin(100)
                .currentRank(0)
                .build();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        return users;
    }


  List<Card> cards = List.of(
          createCard("BlackMagic", "BlackMagic", "https://i.pinimg.com/736x/fd/08/56/fd085657cb5dc97241754f324837a21e.jpg", Type.GOLD, 165, 200)
          ,createCard("LichKing", "Lich king", "https://i.ytimg.com/vi/BCr7y4SLhck/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLAF5MnRDfztzIe6wAe_dT9jI29gPQ", Type.GOLD, 220, 250)
          ,createCard("BlueEyesDragon", "blue eyes dragon", "https://static1.cbrimages.com/wordpress/wp-content/uploads/2020/10/Blue-Eyes-White-Dragon-Art-2.png", Type.GOLD, 160, 180)
          ,createCard("LeeroyJenkins", "leeroy jenkins", "https://warcraft.wiki.gg/images/7/71/Leeroy_Jenkins_TCG.jpg", Type.GOLD, 200,240)
          ,createCard("Exodia", "Exodia", "https://cdn.ygorganization.com/2023/03/BandaiExodiaTitleCard.png", Type.GOLD, 300, 300)
          ,createCard("RenoJackson", "return heath to Max", "https://i.pinimg.com/736x/51/34/a4/5134a4f9f624ed453481a5bbb8443cce.jpg", Type.RARE, 100,100)
          ,createCard("DrBoom", "Have a bombs", "https://hearthstone.wiki.gg/images/thumb/f/fd/Dr._Boom_full.jpg/300px-Dr._Boom_full.jpg", Type.RARE, 150, 150)
          ,createCard("GrommashHellscream", "Charge", "https://hearthstone.wiki.gg/images/thumb/5/53/Grommash_Hellscream_full.jpg/248px-Grommash_Hellscream_full.jpg", Type.RARE, 120, 130)
          ,createCard("BrannBronzebeard", "brann bronzebeard", "https://pbs.twimg.com/profile_images/844556361551753217/grrn9CtX_400x400.jpg", Type.RARE, 40,10)
          ,createCard("Sylvanas", "sylvanas", "https://hearthstone.wiki.gg/images/thumb/f/f9/Sylvanas_Windrunner_full.jpg/334px-Sylvanas_Windrunner_full.jpg", Type.RARE, 140, 145)
          ,createCard("Imp", "imp", "https://hearthstone.wiki.gg/images/thumb/2/2c/Imp_Token_full.jpg/400px-Imp_Token_full.jpg", Type.COMMON, 10,10)
          ,createCard("Murloc", "Murloc", "https://i.pinimg.com/736x/25/a9/f8/25a9f8529eae8c7cc43e41f278921679.jpg", Type.COMMON, 20,30)
          ,createCard("HarrisonJones", "Harrison jones", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe3kXHwn2VfpfH-Ja1QkCQpauStXyCRkkf9Q&s", Type.COMMON,50,60)
          ,createCard("LordJaraxxus", "Lord jaraxxus", "https://external-preview.redd.it/HjB-cgPr5-joVIsszmsfwVxVpNqMfn-2hDpJLBt5nAE.jpg?auto=webp&s=3e683a4ebc42c8b004b60bd4c87d8d42a7e3bc7f", Type.COMMON, 66, 66)
          ,createCard("KingBagurgle", "Murloc", "https://external-preview.redd.it/rnd5_jsWo4_xPovQ5n3jgyQfNVoO0yP4PvFqPfAkn5Q.jpg?auto=webp&s=5cdbd3513451c8e61d8fa1538b37a54e2f8e6084", Type.COMMON, 40, 50)
  );






    @Override
    public void run(String... args) throws Exception {

        long count = cardRepository.count();
        List<User> users = createUsers();

        if (count > 0) {
            return;
        }

        userRepository.saveAll(users);
        cardRepository.saveAll(cards);


    }



    public Card createCard(String name, String description, String imageUrl, Type type, Integer power , Integer price) {

        return Card.builder()
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .type(type)
                .power(power)
                .price(price)
                .build();

    }
}
