package com.project.oop.PMS.controller;

import com.project.oop.PMS.dto.ApiResponse;
import com.project.oop.PMS.dto.LoginRequest;
import com.project.oop.PMS.dto.RegisterRequest;
import com.project.oop.PMS.dto.UserResponse;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User newUser = userService.registerUser(request.getUsername(), request.getPassword());
            UserResponse userResponse = new UserResponse(newUser.getUserId(), newUser.getUsername());
            return ResponseEntity.ok(new ApiResponse("User registered successfully", userResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error: " + e.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> user = userService.loginUser(request.getUsername(), request.getPassword());
        
        return user.map(u -> {
            UserResponse userResponse = new UserResponse(u.getUserId(), u.getUsername());
            return ResponseEntity.ok(new ApiResponse("User login successfully", userResponse));
        }).orElseGet(() -> {
            return ResponseEntity.status(401).body(new ApiResponse("Invalid username or password"));
        });
    }


}
