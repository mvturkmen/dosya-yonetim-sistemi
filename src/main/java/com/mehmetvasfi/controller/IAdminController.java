package com.mehmetvasfi.controller;

import java.util.List;
import java.util.Optional;

import com.mehmetvasfi.model.Admin;
import com.mehmetvasfi.model.Request;

public interface IAdminController {
    public List<Admin> getUsers();

    public boolean saveAdmin(Admin admin);

    public Optional<Admin> findByUsername(String username);

    public boolean adminLogin(Admin admin);

    public List<Request> getPendingRequests();
}
