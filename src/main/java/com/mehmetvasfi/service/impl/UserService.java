package com.mehmetvasfi.service.impl;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mehmetvasfi.model.User;
import com.mehmetvasfi.repository.UserRepository;
import com.mehmetvasfi.service.IUserService;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean saveUser(User user) {
        boolean isNonExist = userRepository.findByUsername(user.getUsername()).isEmpty();

        if (isNonExist) {
            // Şifreyi kriptola
            String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(encryptedPassword);

            // Kullanıcıyı kaydet
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean login(User user) {
        Optional<User> dbUser = userRepository.findByUsername(user.getUsername());
        if (dbUser.isPresent()) {
            User existUser = dbUser.get();

            if (BCrypt.checkpw(user.getPassword(), existUser.getPassword())) {
                return true;
            }
        }
        return false;
    }

}
