package mk.ukim.finki.wp.eshop.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import mk.ukim.finki.wp.eshop.model.ShoppingCart;
import mk.ukim.finki.wp.eshop.model.User;
import mk.ukim.finki.wp.eshop.model.enumerations.ShoppingCartStatus;
import mk.ukim.finki.wp.eshop.service.AuthService;
import mk.ukim.finki.wp.eshop.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;


    public ShoppingCartController(ShoppingCartService shoppingCartService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }

    @GetMapping
    public String getShoppingCartPage(@RequestParam(required = false) String error,
                                      HttpServletRequest req,
                                      Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        String username = req.getRemoteUser();
        ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(username);
        model.addAttribute("products", this.shoppingCartService.listAllProductsInShoppingCart(shoppingCart.getId()));
        model.addAttribute("bodyContent", "shopping-cart");
        return "master-template";
    }

    @PostMapping("/add-product/{id}")
    public String addProductToShoppingCart(@PathVariable Long id, HttpServletRequest req) {
        try {
            String username = req.getRemoteUser();
            ShoppingCart shoppingCart = this.shoppingCartService.addProductToShoppingCart(username, id);
            return "redirect:/shopping-cart";
        } catch (RuntimeException exception) {
            return "redirect:/shopping-cart?error=" + exception.getMessage();
        }
    }

    @GetMapping("/edit")
    public String showEditShoppingCart(HttpServletRequest req, Model model) {
        String username = req.getRemoteUser();
        ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(username);
        model.addAttribute("cart", shoppingCart);
        model.addAttribute("statuses", ShoppingCartStatus.values());
        model.addAttribute("bodyContent", "shopping-cart-edit");
        return "master-template";
    }

    @PostMapping("/update/{id}")
    public String updateShoppingCart(@RequestParam ShoppingCartStatus status,
                                     @PathVariable Long id,
                                     HttpServletRequest req) {

        Optional<ShoppingCart> shoppingCart = this.shoppingCartService.findById(id);
        shoppingCart.ifPresent((cart) -> {
            cart.setStatus(status);
            this.shoppingCartService.save(cart);
        });

        return "redirect:/shopping-cart";
    }

    @GetMapping("/filter")
    public String getFilterShoppingCartsPage(Model model) {
        List<ShoppingCart> carts = shoppingCartService.findAll();
        model.addAttribute("carts", carts);
        model.addAttribute("bodyContent", "shopping-carts-filtered");
        return "master-template";
    }


    @PostMapping("/filter")
    public String filterShoppingCarts(@RequestParam LocalDateTime from,
                                      @RequestParam LocalDateTime to,
                                      Model model) {
        List<ShoppingCart> filteredCarts = shoppingCartService.filterByDateTimeBetween(from, to);
        model.addAttribute("carts", filteredCarts);
        model.addAttribute("bodyContent", "shopping-carts-filtered");
        return "master-template";
    }

    @GetMapping("/count")
    public String getCountShoppingCartsPage(Model model) {
        List<ShoppingCart> shoppingCarts = shoppingCartService.findAll();
        model.addAttribute("carts", shoppingCarts);

        List<User> users = authService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("bodyContent", "shopping-carts-count");
        return "master-template";
    }

    @PostMapping("/count")
    public String countSuccessfulShoppingCartsByUser(@RequestParam String username,
                                     Model model) {
        List<ShoppingCart> shoppingCarts = shoppingCartService.findAll();
        model.addAttribute("carts", shoppingCarts);

        List<User> users = authService.findAll();
        model.addAttribute("users", users);

        Long count = shoppingCartService.countSuccessfulOrders(username);
        model.addAttribute("count", count);
        model.addAttribute("username", username);

        model.addAttribute("bodyContent", "shopping-carts-count");
        return "master-template";
    }
}
