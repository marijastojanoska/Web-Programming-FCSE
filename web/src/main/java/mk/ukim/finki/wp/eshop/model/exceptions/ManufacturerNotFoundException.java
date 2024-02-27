package mk.ukim.finki.wp.eshop.model.exceptions;

public class ManufacturerNotFoundException extends RuntimeException {

    public ManufacturerNotFoundException(Long manufacturerId) {
        super(String.format("Manufacturer with id %d does not exist.", manufacturerId));
    }
}
