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
import com.mehmetvasfi.service.IRequestService;

@Service
public class RequestService implements IRequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public boolean RequestPasswordCahnge(String username, String newPassword) {
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
    public List<Request> getPendingRequests(boolean approved) {
        return requestRepository.findByApproved(approved);
    }

    @Override
    public boolean refuseRequest(Integer id) {
        try {
            requestRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean acceptRequest(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Request ID cannot be null");
        }

        Optional<Request> request = requestRepository.findById(id);
        if (!request.isPresent()) {
            throw new IllegalArgumentException("Request not found for the given ID");
        }

        String username = request.get().getUsername();
        String newPassword = request.get().getNewPassword();

        Optional<User> existingUser = userService.findByUsername(username);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (!newPassword.startsWith("$2a$")) {
                newPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            }
            user.setPassword(newPassword);
            userService.updateUser(user); // Kullanıcıyı güncelle
        } else {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            userService.saveUser(newUser); // Yeni kullanıcı oluştur
        }

        requestRepository.deleteById(id);
        return true;
    }

}
