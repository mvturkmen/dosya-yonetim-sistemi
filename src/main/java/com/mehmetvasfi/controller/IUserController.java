package com.mehmetvasfi.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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

    public ResponseEntity<?> getSharedWithMe(@PathVariable Integer userId);

    public ResponseEntity<String> openFile(@PathVariable String filePath);

    public boolean deleteUserByUsername(Map<String, String> requestBody);
}
