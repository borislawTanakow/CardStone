package app.cards.repository;

import app.cards.model.MyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface MyCardRepository extends JpaRepository<MyCard, UUID> {
}
