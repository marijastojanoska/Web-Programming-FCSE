package mk.ukim.finki.wp.eshop.web.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/servlet/messy/category")
public class MessyCategoryServlet extends HttpServlet {

    private final SpringTemplateEngine springTemplateEngine;

    public MessyCategoryServlet(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }

    static class Category {
        public String name;
        public String description;

        public Category(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    List<Category> categories = null;

    @Override
    public void init() {
        categories = new ArrayList<>();
        categories.add(new Category("Movies",
                "Movies category"));
        categories.add(new Category("Books",
                "Books category"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req, resp);

        WebContext context = new WebContext(webExchange);
        context.setVariable("categories", categories);
        context.setVariable("ipAddress", req.getRemoteAddr());

        springTemplateEngine.process(
                "categories.html",
                context,
                resp.getWriter()
        );
    }

    void addCategory(Category category) {
        categories.add(category);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String name = req.getParameter("cname");
        String description = req.getParameter("cdesc");

        Category category = new Category(name, description);
        addCategory(category);
        resp.sendRedirect("/servlet/category");
    }
}
