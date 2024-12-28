package com.mehmetvasfi.service;

import java.util.List;
import java.util.Optional;

import com.mehmetvasfi.model.User;

public interface IUserService {
    public List<User> getUsers();

    public boolean saveUser(User user);

    public Optional<User> findByUsername(String username);

    public boolean login(User user);

}
