package mk.ukim.finki.wp.eshop.web.controller;

import mk.ukim.finki.wp.eshop.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getCategoriesPage(Model model) {
        model.addAttribute("bodyContent", "categories");
        model.addAttribute("categories", categoryService.listCategories());
        return "master-template";
    }
}
