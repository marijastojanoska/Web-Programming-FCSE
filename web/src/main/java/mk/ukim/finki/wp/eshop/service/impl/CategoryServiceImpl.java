package mk.ukim.finki.wp.eshop.service.impl;

import mk.ukim.finki.wp.eshop.model.Category;
import mk.ukim.finki.wp.eshop.repository.inmemory.InMemoryCategoryRepository;
import mk.ukim.finki.wp.eshop.repository.jpa.CategoryRepository;
import mk.ukim.finki.wp.eshop.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private boolean categoryInvalid(String name) {
        return name == null || name.isEmpty();
    }

    @Override
    public List<Category> listCategories() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return this.categoryRepository.findById(id);
    }

    @Override
    public Category create(String name, String description) {
        if (categoryInvalid(name)) {
            throw new IllegalArgumentException();
        }

        Category category = new Category(name, description);
        return this.categoryRepository.save(category);
    }

    @Override
    public Category update(String name, String description) {
        return create(name, description);
    }

    @Override
    public void delete(String name) {
        categoryRepository.deleteByName(name);
    }

    @Override
    public List<Category> searchCategories(String text) {
        return categoryRepository.findAllByNameLike(text);
    }
}
