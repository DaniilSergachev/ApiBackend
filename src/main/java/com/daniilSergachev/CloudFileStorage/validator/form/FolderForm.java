package com.daniilSergachev.CloudFileStorage.validator.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

@Getter
@Setter
@Component
public class FolderForm {
    @NotBlank(message = "Имя папки не должно быть пустым")
    @Pattern(regexp = "^[\\w\\s\\-\\.а-яА-ЯёЁ]+$", message = "Имя папки содержит недопустимые символы")
    private String name;

    @Nullable
    private String path;

}