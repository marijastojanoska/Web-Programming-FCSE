package mk.ukim.finki.wp.eshop;

import mk.ukim.finki.wp.eshop.model.Category;
import mk.ukim.finki.wp.eshop.model.Manufacturer;
import mk.ukim.finki.wp.eshop.model.Product;
import mk.ukim.finki.wp.eshop.model.Role;
import mk.ukim.finki.wp.eshop.service.CategoryService;
import mk.ukim.finki.wp.eshop.service.ManufacturerService;
import mk.ukim.finki.wp.eshop.service.ProductService;
import mk.ukim.finki.wp.eshop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MockMvcResultHandlersDsl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class EshopApplicationTests {

    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ManufacturerService manufacturerService;

    @Autowired
    ProductService productService;

    private static boolean dataInitialized;

    private static Category c1;
    private static Manufacturer m1;

    @BeforeEach
    public void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        initData();
    }

    private void initData() {
        if (this.dataInitialized) return;

        c1 = categoryService.create("c1", "c1 desc");
        categoryService.create("c2", "c2 desc");

        m1 = manufacturerService.save("m1", "m1 address").get();
        manufacturerService.save("m2", "m2 address");

        userService.register("user1", "user","user", "user", "user", Role.ROLE_USER);
        userService.register("admin1", "admin","admin", "admin", "adminadmin", Role.ROLE_ADMIN);

        this.dataInitialized = true;
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void testGetProducts() throws Exception {
        MockHttpServletRequestBuilder productRequest = MockMvcRequestBuilders.get("/products");
        this.mockMvc.perform(productRequest).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("products"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "products"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Product product = productService.save("Jeans", 300d, 150, c1.getId(), m1.getId()).get();

        MockHttpServletRequestBuilder productRequest = MockMvcRequestBuilders.post("/products/delete/" + product.getId());
        this.mockMvc.perform(productRequest).andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/products"));
    }
}
