package cardStone.cards.repository;

import cardStone.cards.model.MyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CardToBuyRepository extends JpaRepository<MyCard, UUID> {
}
