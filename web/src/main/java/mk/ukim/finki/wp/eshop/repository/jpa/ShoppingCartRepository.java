package mk.ukim.finki.wp.eshop.repository.jpa;

import mk.ukim.finki.wp.eshop.model.ShoppingCart;
import mk.ukim.finki.wp.eshop.model.User;
import mk.ukim.finki.wp.eshop.model.enumerations.ShoppingCartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    Optional<ShoppingCart> findByUserAndStatus(User user, ShoppingCartStatus created);

    Optional<ShoppingCart> findByUserUsernameAndStatus(String username, ShoppingCartStatus created);

    List<ShoppingCart> findByDateCreatedBetween(LocalDateTime from, LocalDateTime to);

    @Query("SELECT COUNT(s) FROM ShoppingCart s INNER JOIN s.user u WHERE u.username = :username AND s.status = 'FINISHED'")
    Long countSuccessfulOrdersByUsername(@Param("username") String username);
}
