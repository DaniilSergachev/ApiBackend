package com.daniilSergachev.CloudFileStorage.dto;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}