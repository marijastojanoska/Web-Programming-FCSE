package mk.ukim.finki.wp.eshop.service.impl;

import mk.ukim.finki.wp.eshop.model.Product;
import mk.ukim.finki.wp.eshop.model.ShoppingCart;
import mk.ukim.finki.wp.eshop.model.User;
import mk.ukim.finki.wp.eshop.model.enumerations.ShoppingCartStatus;
import mk.ukim.finki.wp.eshop.model.exceptions.ProductAlreadyInShoppingCartException;
import mk.ukim.finki.wp.eshop.model.exceptions.ProductNotFoundException;
import mk.ukim.finki.wp.eshop.model.exceptions.ShoppingCartNotFoundException;
import mk.ukim.finki.wp.eshop.model.exceptions.UserNotFoundException;
import mk.ukim.finki.wp.eshop.repository.jpa.ShoppingCartRepository;
import mk.ukim.finki.wp.eshop.repository.jpa.UserRepository;
import mk.ukim.finki.wp.eshop.service.ProductService;
import mk.ukim.finki.wp.eshop.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository,
                                   UserRepository userRepository,
                                   ProductService productService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.productService = productService;
    }

    @Override
    public List<Product> listAllProductsInShoppingCart(Long cartId) {
        Optional<ShoppingCart> shoppingCartOptional = this.shoppingCartRepository.findById(cartId);

        if (shoppingCartOptional.isEmpty()) {
            throw new ShoppingCartNotFoundException(cartId);
        }

        return shoppingCartOptional.get().getProducts();
    }

    @Override
    public ShoppingCart getActiveShoppingCart(String username) {
        return this.shoppingCartRepository
                .findByUserUsernameAndStatus(username, ShoppingCartStatus.CREATED)
                .orElseGet(() -> {
                    User user = this.userRepository.findByUsername(username)
                            .orElseThrow(() -> new UserNotFoundException(username));
                    ShoppingCart shoppingCart = new ShoppingCart(user);
                    return this.shoppingCartRepository.save(shoppingCart);
                });
    }

    @Override
    public ShoppingCart addProductToShoppingCart(String username, Long productId) {
        Product product = this.productService.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        ShoppingCart shoppingCart = this.getActiveShoppingCart(username);
        List<Product> productsInShoppingCart = shoppingCart.getProducts().stream()
                .filter(i -> i.getId().equals(productId))
                .collect(Collectors.toList());

        if (productsInShoppingCart.size() > 0) {
            throw new ProductAlreadyInShoppingCartException(productId, username);
        }

        shoppingCart.getProducts().add(product);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public List<ShoppingCart> filterByDateTimeBetween(LocalDateTime from, LocalDateTime to) {
        return this.shoppingCartRepository.findByDateCreatedBetween(from, to);
    }

    @Override
    public List<ShoppingCart> findAll() {
        return this.shoppingCartRepository.findAll();
    }

    @Override
    public Long countSuccessfulOrders(String username) {
        return this.shoppingCartRepository.countSuccessfulOrdersByUsername(username);
    }

    @Override
    public ShoppingCart save(ShoppingCart cart) {
        return this.shoppingCartRepository.save(cart);
    }

    @Override
    public Optional<ShoppingCart> findById(Long id) {
        return this.shoppingCartRepository.findById(id);
    }
}

