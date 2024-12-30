package com.mehmetvasfi.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mehmetvasfi.dto.UsernameDTO;
import com.mehmetvasfi.model.User;

public interface IUserController {

    public List<User> getUsers();

    public boolean saveUser(User user);

    public Optional<User> findByUsername(String username);

    public Boolean login(User user);

    public boolean changeUsername(Map<String, Object> request);

    public boolean changePassword(Map<String, Object> request);

    public Integer getIdByUsername(UsernameDTO usernameDTO);

    public String getUsernameById(UsernameDTO usernameDTO);
}
