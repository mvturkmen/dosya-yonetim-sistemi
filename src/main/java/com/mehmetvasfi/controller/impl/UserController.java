package com.mehmetvasfi.controller.impl;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.awt.Desktop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mehmetvasfi.controller.IUserController;
import com.mehmetvasfi.dto.UsernameDTO;
import com.mehmetvasfi.model.FileEntity;
import com.mehmetvasfi.model.User;
import com.mehmetvasfi.service.impl.UserService;

@RestController
@RequestMapping("/rest/api/user")
public class UserController implements IUserController {

    @Autowired
    private UserService userService;

    @Override
    @GetMapping(path = "/list")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @Override
    @PostMapping(path = "/save")
    public boolean saveUser(@RequestBody User user) {
        return userService.saveUser(user);

    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userService.findByUsername(username);
    }

    @Override
    @PostMapping(path = "/login")
    public Boolean login(@RequestBody User user) {
        return userService.login(user);

    }

    @Override
    @PostMapping(path = "/change/username")
    public boolean changeUsername(@RequestBody Map<String, Object> payload) {
        User user = new User();
        user.setUsername((String) payload.get("username"));
        user.setPassword((String) payload.get("password"));
        String newUsername = (String) payload.get("newUsername");

        return userService.changeUsername(user, newUsername);
    }

    @Override
    @PostMapping(path = "/change/password")
    public boolean changePassword(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String newPassword = (String) request.get("newPassword");

        return userService.requestPasswordChange(username, newPassword);
    }

    @Override
    @PostMapping(path = "/get-id")
    public Integer getIdByUsername(@RequestBody UsernameDTO usernameDTO) {
        System.out.println(usernameDTO.getUsername());
        return userService.getIdByUsername(usernameDTO.getUsername());
    }

    @Override
    @PostMapping(path = "/get-username")
    public String getUsernameById(@RequestBody UsernameDTO usernameDTO) {
        return userService.getUsernameById(usernameDTO.getId());
    }

    @Override
    @GetMapping(path = "/shared-with/{userId}")
    public ResponseEntity<?> getSharedWithMe(@PathVariable Integer userId) {

        try {
            List<FileEntity> accessibleFiles = userService.getAccessibleFiles(userId);
            if (accessibleFiles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No files found for user with ID: " + userId);
            }
            return ResponseEntity.ok(accessibleFiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving files: " + e.getMessage());
        }

    }

    @Override
    @GetMapping("/open")
    public ResponseEntity openFile(@RequestParam(value = "path") String filePath) {
        try {
            System.out.println("1. Gelen dosya yolu: " + filePath);

            // Windows path ayracını düzelt
            filePath = filePath.replace("/", "\\");
            System.out.println("2. Düzeltilmiş dosya yolu: " + filePath);

            File file = new File(filePath);
            System.out.println("3. Mutlak dosya yolu: " + file.getAbsolutePath());
            System.out.println("4. Dosya var mı?: " + file.exists());
            System.out.println("5. Dosya mı?: " + file.isFile());
            System.out.println("6. Okunabilir mi?: " + file.canRead());
            System.out.println("7. Desktop destekleniyor mu?: " + Desktop.isDesktopSupported());

            if (file.exists() && file.isFile()) {
                Runtime.getRuntime().exec(new String[] { "cmd", "/c", "start", filePath });

                return ResponseEntity.ok("File opened successfully.");
            } else {
                String errorDetails = String.format(
                        "Dosya durumu: exists=%b, isFile=%b, canRead=%b, Desktop.supported=%b",
                        file.exists(), file.isFile(), file.canRead(), Desktop.isDesktopSupported());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("File not found or unsupported. Details: " + errorDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

}
