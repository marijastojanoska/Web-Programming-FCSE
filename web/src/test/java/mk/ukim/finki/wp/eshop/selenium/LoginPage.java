package mk.ukim.finki.wp.eshop.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends AbstractPage {

    private WebElement username;

    private WebElement password;

    private WebElement submit;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public static LoginPage openLogin(WebDriver driver) {
        get(driver, "/login");
        System.out.println(driver.getCurrentUrl());
        LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
        return loginPage;
    }

    public static ProductsPage doLogin(WebDriver driver, LoginPage loginPage, String username, String password) {
        loginPage.username.sendKeys(username);
        loginPage.password.sendKeys(password);
        loginPage.submit.click();
        System.out.println(driver.getCurrentUrl());
        ProductsPage productsPage = PageFactory.initElements(driver, ProductsPage.class);
        return productsPage;
    }
}
