package com.mehmetvasfi.service;

import java.util.List;
import java.util.Optional;

import com.mehmetvasfi.model.Admin;

public interface IAdminService {
    public List<Admin> getUsers();

    public boolean saveAdmin(Admin user);

    public Optional<Admin> findByUsername(String username);

    public boolean adminLogin(Admin admin);
}
