package mk.ukim.finki.wp.eshop.service.impl;

import mk.ukim.finki.wp.eshop.model.Category;
import mk.ukim.finki.wp.eshop.model.Manufacturer;
import mk.ukim.finki.wp.eshop.model.Product;
import mk.ukim.finki.wp.eshop.model.exceptions.CategoryNotFoundException;
import mk.ukim.finki.wp.eshop.model.exceptions.ProductNotFoundException;
import mk.ukim.finki.wp.eshop.model.exceptions.ManufacturerNotFoundException;
import mk.ukim.finki.wp.eshop.repository.jpa.CategoryRepository;
import mk.ukim.finki.wp.eshop.repository.jpa.ManufacturerRepository;
import mk.ukim.finki.wp.eshop.repository.jpa.ProductRepository;
import mk.ukim.finki.wp.eshop.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              ManufacturerRepository manufacturerRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return this.productRepository.findById(id);
    }

    @Override
    public Optional<Product> findByName(String name) {
        return this.productRepository.findByName(name);
    }

    @Override
    public Optional<Product> save(
            String name,
            Double price,
            Integer quantity,
            Long categoryId,
            Long manufacturerId
    ) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        Manufacturer manufacturer = this.manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new ManufacturerNotFoundException(manufacturerId));
        return Optional.of(this.productRepository.save(new Product(name, price, quantity, category, manufacturer)));
    }

    @Override
    public void deleteById(Long id) {
        this.productRepository.deleteById(id);
    }

    @Override
    public Optional<Product> edit(Long id, String name, Double price, Integer quantity, Long category, Long manufacturer) {
        Product p = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException(id));
        Category c = categoryRepository.findById(category).orElseThrow(()->new CategoryNotFoundException(category));
        Manufacturer m = manufacturerRepository.findById(manufacturer).orElseThrow(()->new ManufacturerNotFoundException(manufacturer));
        p.setName(name);
        p.setCategory(c);
        p.setManufacturer(m);
        p.setPrice(price);
        p.setQuantity(quantity);
        productRepository.save(p);
        return Optional.of(p);
    }
}