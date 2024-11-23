package com.daniilSergachev.CloudFileStorage.dto;

import lombok.Data;

@Data
public class  RegistrationUserDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
}