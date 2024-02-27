package mk.ukim.finki.wp.eshop.selenium;

import lombok.Getter;
import mk.ukim.finki.wp.eshop.model.Product;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Getter
public class ProductsPage extends AbstractPage {

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = "tr[class=product")
    private List<WebElement> productRows;

    @FindBy(css = ".add-product-btn")
    private List<WebElement> addProductButton;

    @FindBy(className = "edit-product")
    private List<WebElement> editButtons;

    @FindBy(css = ".delete-product")
    private List<WebElement> deleteButtons;

    @FindBy(className = "add-to-cart")
    private List<WebElement> addToCartButton;

    public static ProductsPage to(WebDriver driver) {
        get(driver, "/products");
        return PageFactory.initElements(driver, ProductsPage.class);
    }

    public void assertElements(int productsNumber, int deleteButtons, int editButtons, int cartButtons, int addButtons) {
        Assert.assertEquals("rows do not match", productsNumber, this.productRows.size());
        Assert.assertEquals("delete do not match", deleteButtons, this.deleteButtons.size());
        Assert.assertEquals("edit do not match", editButtons, this.editButtons.size());
        Assert.assertEquals("cart do not match", cartButtons, this.addToCartButton.size());
        Assert.assertEquals("add is visible", addButtons, this.addProductButton.size());
    }
}
