package com.project.oop.PMS.controller;

import com.project.oop.PMS.dto.ApiResponse;
import com.project.oop.PMS.dto.LoginRequest;
import com.project.oop.PMS.dto.RegisterRequest;
import com.project.oop.PMS.dto.UserResponse;
import com.project.oop.PMS.entity.User;
import com.project.oop.PMS.service.implement.UserServiceImplement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "https://pms.daokiencuong.id.vn")
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UserServiceImplement userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User newUser = userService.register(request.getUsername(), request.getPassword());
            UserResponse userResponse = new UserResponse(newUser.getUserId(), newUser.getUsername());
            return ResponseEntity.ok(new ApiResponse("User registered successfully", userResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error: " + e.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.login(request.getUsername(), request.getPassword());
            UserResponse userResponse = new UserResponse(user.getUserId(), user.getUsername());
            return ResponseEntity.ok(new ApiResponse("User login successfully", userResponse));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new ApiResponse("Invalid username or password"));
        }
    }

}
