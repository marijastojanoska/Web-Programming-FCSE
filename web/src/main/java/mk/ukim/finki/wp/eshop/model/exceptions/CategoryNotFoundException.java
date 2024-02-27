package mk.ukim.finki.wp.eshop.model.exceptions;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long categoryId) {
        super(String.format("Category with id %d does not exist.", categoryId));
    }
}
