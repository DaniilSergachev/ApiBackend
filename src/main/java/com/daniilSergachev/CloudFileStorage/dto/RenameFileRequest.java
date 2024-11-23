package com.daniilSergachev.CloudFileStorage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@Setter
public class RenameFileRequest {
    @NotBlank(message = "New file name is required")
    private String oldNameFile;
    @NotBlank(message = "New file name is required")
    private String newNameFile;
    @Nullable
    private String currentPath;


}
