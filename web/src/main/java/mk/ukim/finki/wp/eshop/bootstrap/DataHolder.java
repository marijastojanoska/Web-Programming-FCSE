package mk.ukim.finki.wp.eshop.bootstrap;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.wp.eshop.model.*;
import mk.ukim.finki.wp.eshop.model.embeddables.UserAddress;
import mk.ukim.finki.wp.eshop.model.enumerations.ShoppingCartStatus;
import mk.ukim.finki.wp.eshop.repository.jpa.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataHolder {
    public static List<User> users = null;
    public static List<Category> categories = null;
    public static List<Manufacturer> manufacturers = null;
    public static List<Product> products = null;
    public static List<ShoppingCart> shoppingCarts = null;


    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ProductRepository productRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataHolder(CategoryRepository categoryRepository, ManufacturerRepository manufacturerRepository, ProductRepository productRepository, ShoppingCartRepository shoppingCartRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.categoryRepository = categoryRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        users = new ArrayList<>();
        categories = new ArrayList<>();
        manufacturers = new ArrayList<>();
        products = new ArrayList<>();
        shoppingCarts = new ArrayList<>();


        if (userRepository.count() == 0) {
            users.add(
                    new User(
                            "customer",
                            passwordEncoder.encode("customer"),
                            "customer",
                            "customer",
                            new UserAddress("Macedonia", "Skopje", "Ul. ulica123", "23/41-2"),
                            Role.ROLE_USER
            ));
            users.add(
                    new User(
                            "admin",
                            passwordEncoder.encode("admin"),
                            "admin",
                            "admin",
                            new UserAddress(),
                            Role.ROLE_ADMIN
                    )
            );
            userRepository.saveAll(users);
        }

        if (categoryRepository.count() == 0) {
            categories.add(new Category("Books", "Books category"));
            categories.add(new Category("Sports", "Sports category"));
            categories.add(new Category("Food", "Food category"));
            categoryRepository.saveAll(categories);
        }

        if (manufacturerRepository.count() == 0) {
            manufacturers.add(new Manufacturer("Nike", "USA"));
            manufacturers.add(new Manufacturer("Coca Cola", "USA"));
            manufacturers.add(new Manufacturer("Literatura", "MK"));
            manufacturerRepository.saveAll(manufacturers);
        }

        if (productRepository.count() == 0) {
            List<Category> categoryList = categoryRepository.findAll();
            List<Manufacturer> manufacturerList = manufacturerRepository.findAll();

            products.add(new Product("Shoes", 30d, 1000, categoryList.get(0), manufacturerList.get(0)));
            products.add(new Product("Hoodie", 50.99, 200, categoryList.get(0), manufacturerList.get(1)));
            products.add(new Product("Gloves", 10d, 56000, categoryList.get(1), manufacturerList.get(2)));
            products.add(new Product("Guitar", 590.99, 20, categoryList.get(2), manufacturerList.get(2)));
            productRepository.saveAll(products);
        }

        if (shoppingCartRepository.count() == 0) {
            List<Product> productList = productRepository.findAll();
            List<User> userList = userRepository.findAll();

            ShoppingCart shoppingCart1 = new ShoppingCart(userList.get(0));
            ShoppingCart shoppingCart2 = new ShoppingCart(userList.get(1));
            ShoppingCart shoppingCart3 = new ShoppingCart(userList.get(1));

            shoppingCart1.getProducts().add(productList.get(0));

            shoppingCart2.getProducts().add(productList.get(0));
            shoppingCart2.getProducts().add(productList.get(1));
            shoppingCart2.getProducts().add(productList.get(2));

            shoppingCart3.getProducts().add(productList.get(0));

            shoppingCart1.setStatus(ShoppingCartStatus.FINISHED);
            shoppingCart2.setStatus(ShoppingCartStatus.FINISHED);
            shoppingCart3.setStatus(ShoppingCartStatus.FINISHED);

            shoppingCartRepository.save(shoppingCart1);
            shoppingCartRepository.save(shoppingCart2);
            shoppingCartRepository.save(shoppingCart3);
        }
    }
}
