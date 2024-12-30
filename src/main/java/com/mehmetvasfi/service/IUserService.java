package com.mehmetvasfi.service;

import java.util.List;
import java.util.Optional;

import com.mehmetvasfi.dto.UsernameDTO;
import com.mehmetvasfi.model.User;

public interface IUserService {
    public List<User> getUsers();

    public boolean saveUser(User user);

    public Optional<User> findByUsername(String username);

    public boolean login(User user);

    public boolean changeUsername(User user, String newUsername);

    public boolean requestPasswordChange(String username, String newPassword);

    public void deleteUserById(Integer id);

    public Integer getIdByUsername(String username);

    public String getUsernameById(Integer id);

}
