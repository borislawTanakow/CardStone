package app.cards.repository;

import app.cards.model.MyCard;
import app.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface MyCardRepository extends JpaRepository<MyCard, UUID> {
    boolean existsByNameAndOwner(String name, User user);
}
