package mk.ukim.finki.wp.eshop.service;

import mk.ukim.finki.wp.eshop.model.Product;
import mk.ukim.finki.wp.eshop.model.ShoppingCart;
import mk.ukim.finki.wp.eshop.model.enumerations.ShoppingCartStatus;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShoppingCartService {

    List<Product> listAllProductsInShoppingCart(Long cartId);

    ShoppingCart getActiveShoppingCart(String username);

    ShoppingCart addProductToShoppingCart(String username, Long productId);

    List<ShoppingCart> filterByDateTimeBetween(LocalDateTime from, LocalDateTime to);

    List<ShoppingCart> findAll();

    Long countSuccessfulOrders(String username);

    ShoppingCart save(ShoppingCart shoppingCart);

    Optional<ShoppingCart> findById(Long id);

}
