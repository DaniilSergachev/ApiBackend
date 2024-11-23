package com.daniilSergachev.CloudFileStorage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@Setter
public class FolderUpdateRequest {
    @NotBlank(message = "New folder name is required")
    private String newNameFolder;

    @NotBlank(message = "Old folder name is required")
    private String oldFolderName;

    @Nullable
    private String currentPath;

}