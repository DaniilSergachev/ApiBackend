package com.daniilSergachev.CloudFileStorage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@Setter
public class RequestDeleteFile {
    @NotBlank
    private String name;
    @Nullable
    private String path;

    @NotBlank
    private boolean isFolder;
}
