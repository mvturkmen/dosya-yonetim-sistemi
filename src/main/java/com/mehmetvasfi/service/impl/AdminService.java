package com.mehmetvasfi.service.impl;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mehmetvasfi.model.Admin;
import com.mehmetvasfi.repository.AdminRepository;
import com.mehmetvasfi.service.IAdminService;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public List<Admin> getUsers() {
        return adminRepository.findAll();
    }

    @Override
    public boolean saveAdmin(Admin user) {
        boolean isNonExist = adminRepository.findByUsername(user.getUsername()).isEmpty();

        if (isNonExist) {

            String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(encryptedPassword);

            adminRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<Admin> findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public boolean adminLogin(Admin admin) {
        Optional<Admin> dbAdmin = adminRepository.findByUsername(admin.getUsername());
        if (dbAdmin.isPresent()) {
            Admin existAdmin = dbAdmin.get();
            if (existAdmin.getUsername().equals(admin.getUsername())) {
                if (BCrypt.checkpw(admin.getPassword(), existAdmin.getPassword())) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

}
