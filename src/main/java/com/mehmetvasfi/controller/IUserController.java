package com.mehmetvasfi.controller;

import java.util.List;
import java.util.Optional;

import com.mehmetvasfi.model.User;

public interface IUserController {

    public List<User> getUsers();

    public boolean saveUser(User user);

    public Optional<User> findByUsername(String username);

    public Boolean login(User user);

}
