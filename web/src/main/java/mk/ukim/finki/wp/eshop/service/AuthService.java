package mk.ukim.finki.wp.eshop.service;

import mk.ukim.finki.wp.eshop.model.User;

import java.util.List;

public interface AuthService {
    User login(String username, String password);

    List<User> findAll();
}
