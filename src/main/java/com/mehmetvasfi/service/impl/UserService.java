package com.mehmetvasfi.service.impl;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mehmetvasfi.model.Request;
import com.mehmetvasfi.model.User;
import com.mehmetvasfi.repository.RequestRepository;
import com.mehmetvasfi.repository.UserRepository;
import com.mehmetvasfi.service.IUserService;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean saveUser(User user) {
        boolean isNonExist = userRepository.findByUsername(user.getUsername()).isEmpty();

        if (isNonExist) {
            String password = user.getPassword();
            if (!password.startsWith("$2a$")) {
                String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                user.setPassword(encryptedPassword);
            }

            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean login(User user) {
        Optional<User> dbUser = userRepository.findByUsername(user.getUsername());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        if (dbUser.isPresent()) {
            User existUser = dbUser.get();
            if (BCrypt.checkpw(user.getPassword(), existUser.getPassword())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean changeUsername(User user, String newUsername) {
        Optional<User> dbUser = userRepository.findByUsername(user.getUsername());

        if (dbUser.isPresent()) {
            User existUser = dbUser.get();

            if (userRepository.findByUsername(newUsername).isPresent()) {
                System.out.println("Bu kullanıcı adı zaten mevcut");
                return false;
            }

            User newUser = new User();
            newUser.setUsername(newUsername);

            String password = existUser.getPassword();
            if (!password.startsWith("$2a$")) {
                password = BCrypt.hashpw(password, BCrypt.gensalt());
            }
            newUser.setPassword(password);

            deleteUserById(existUser.getId());
            saveUser(newUser);

            return true;
        }

        return false;
    }

    @Override
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean requestPasswordChange(String username, String newPassword) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            Request request = new Request();
            request.setUsername(username);
            request.setNewPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));

            requestRepository.save(request);
            return true;
        }
        return false;
    }

    @Override
    public Integer getIdByUsername(String username) {
        Optional<User> user = findByUsername(username);
        if (user.isPresent()) {
            return user.get().getId();
        }
        return 0;

    }

    @Override
    public String getUsernameById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get().getUsername();
        }
        return null;
    }

}
