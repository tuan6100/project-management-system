package com.project.oop.PMS.dto;

import lombok.Data;

@Data
public class UserRequest {
    private Integer userId;

    // Constructor
    public UserRequest(Integer userId) {
        this.userId = userId;
    }
}
