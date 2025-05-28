package com.library.LibraryApp.application.dto;

import com.library.LibraryApp.core.util.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateBookDto(

        @NotBlank
        @Size(min = 1, max = 100)
        String name,

        @Pattern(regexp = RegexPatterns.UDK, message = "Некорректный формат УДК")
        @Size(min = 3, max = 20, message = "УДК должен содержать от 3 до 20 символов")
        String udk,

        @NotNull
        UUID author
) {

}
