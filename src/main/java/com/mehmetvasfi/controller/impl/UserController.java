package com.mehmetvasfi.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mehmetvasfi.controller.IUserController;
import com.mehmetvasfi.model.User;
import com.mehmetvasfi.service.impl.UserService;

@RestController
@RequestMapping("/rest/api/user")
public class UserController implements IUserController {

    @Autowired
    private UserService userService;

    @Override
    @GetMapping(path = "/list")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @Override
    @PostMapping(path = "/save")
    public boolean saveUser(@RequestBody User user) {
        return userService.saveUser(user);

    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userService.findByUsername(username);
    }

    @Override
    @PostMapping(path = "/login")
    public Boolean login(@RequestBody User user) {
        return userService.login(user);

    }

}
