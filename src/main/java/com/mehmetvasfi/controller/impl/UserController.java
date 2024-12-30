package com.mehmetvasfi.controller.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mehmetvasfi.controller.IUserController;
import com.mehmetvasfi.dto.UsernameDTO;
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

    @Override
    @PostMapping(path = "/change/username")
    public boolean changeUsername(@RequestBody Map<String, Object> payload) {
        User user = new User();
        user.setUsername((String) payload.get("username"));
        user.setPassword((String) payload.get("password"));
        String newUsername = (String) payload.get("newUsername");

        return userService.changeUsername(user, newUsername);
    }

    @Override
    @PostMapping(path = "/change/password")
    public boolean changePassword(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String newPassword = (String) request.get("newPassword");

        return userService.requestPasswordChange(username, newPassword);
    }

    @Override
    @PostMapping(path = "/get-id")
    public Integer getIdByUsername(@RequestBody UsernameDTO usernameDTO) {
        System.out.println(usernameDTO.getUsername());
        return userService.getIdByUsername(usernameDTO.getUsername());
    }

    @Override
    @PostMapping(path = "/get-username")
    public String getUsernameById(@RequestBody UsernameDTO usernameDTO) {
        return userService.getUsernameById(usernameDTO.getId());
    }

}
