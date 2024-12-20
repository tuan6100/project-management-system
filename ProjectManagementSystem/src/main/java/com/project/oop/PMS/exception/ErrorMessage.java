package com.project.oop.PMS.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorMessage {

    private String message;

    public ErrorMessage(String message) {
        this.message = message;
    }
}
