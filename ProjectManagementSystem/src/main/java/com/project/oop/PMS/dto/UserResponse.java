package com.project.oop.PMS.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Integer id;
    private String username;

    public UserResponse(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    // Getters and Setters
}
