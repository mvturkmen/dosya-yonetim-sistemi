package com.mehmetvasfi.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mehmetvasfi.controller.IAdminController;
import com.mehmetvasfi.model.Admin;
import com.mehmetvasfi.service.impl.AdminService;

@RestController
@RequestMapping("/rest/api/admin")
public class AdminController implements IAdminController {

    @Autowired
    private AdminService adminService;

    @Override
    @GetMapping("/list")
    public List<Admin> getUsers() {
        return adminService.getUsers();
    }

    @Override
    @PostMapping("/save")
    public boolean saveAdmin(@RequestBody Admin admin) {
        return adminService.saveAdmin(admin);
    }

    @Override
    public Optional<Admin> findByUsername(String username) {
        return adminService.findByUsername(username);
    }

    @Override
    @PostMapping("/login")
    public boolean adminLogin(@RequestBody Admin admin) {
        return adminService.adminLogin(admin);
    }

}
