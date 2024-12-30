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

        User newUser = new User();
        newUser.setUsername(request.get().getUsername());

        String password = request.get().getNewPassword();
        if (!password.startsWith("$2a$")) {
            password = BCrypt.hashpw(password, BCrypt.gensalt());
        }

        newUser.setPassword(password);
        Optional<User> existingUser = userService.findByUsername(request.get().getUsername());
        if (existingUser.isPresent()) {
            userService.deleteUserById(existingUser.get().getId());
        }
        userService.saveUser(newUser);

        requestRepository.deleteById(id);
        return true;
    }

}
