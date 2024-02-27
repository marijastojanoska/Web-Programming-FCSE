package mk.ukim.finki.wp.eshop.selenium;

import mk.ukim.finki.wp.eshop.model.Category;
import mk.ukim.finki.wp.eshop.model.Manufacturer;
import mk.ukim.finki.wp.eshop.model.Role;
import mk.ukim.finki.wp.eshop.model.User;
import mk.ukim.finki.wp.eshop.service.CategoryService;
import mk.ukim.finki.wp.eshop.service.ManufacturerService;
import mk.ukim.finki.wp.eshop.service.ProductService;
import mk.ukim.finki.wp.eshop.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SeleniumScenarioTest {
    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ManufacturerService manufacturerService;

    @Autowired
    ProductService productService;

    private HtmlUnitDriver driver;

    private static boolean dataInitialized;

    private static Category c1;
    private static Category c2;
    private static Manufacturer m1;
    private static Manufacturer m2;

    private static User user;
    private static User admin;

    @BeforeEach
    private void setup() {
        this.driver = new HtmlUnitDriver(true);
        initData();
    }

    @AfterEach
    public void destroy() {
        if (this.driver != null) {
            this.driver.close();
        }
    }

    private void initData() {
        if (this.dataInitialized) return;

        c1 = categoryService.create("c1", "c1 desc");
        c2 = categoryService.create("c2", "c2 desc");

        m1 = manufacturerService.save("m1", "m1 address").get();
        m2 = manufacturerService.save("m2", "m2 address").get();

        user = userService.register("user1", "user","user", "user", "user", Role.ROLE_USER);
        admin = userService.register("admin1", "admin","admin", "admin", "adminadmin", Role.ROLE_ADMIN);

        this.dataInitialized = true;
    }

    @Test
    public void testScenario() {
        this.driver = new HtmlUnitDriver(true);
        LoginPage loginPage = LoginPage.openLogin(this.driver);
        ProductsPage productsPage = LoginPage.doLogin(this.driver, loginPage, "admin1", "admin1");
        productsPage.assertElements(3, 3, 3, 3, 1);
    }
}
