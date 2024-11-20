package com.project.oop.PMS.dto;

import com.project.oop.PMS.entity.User;
import lombok.Data;

@Data
public class UserResponse {
    private Integer id;
    private String username;

    public UserResponse(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    public static UserResponse fromEntity(User user) {
        return new UserResponse(
            user.getUserId(),
            user.getUsername()
        );
    }
}
