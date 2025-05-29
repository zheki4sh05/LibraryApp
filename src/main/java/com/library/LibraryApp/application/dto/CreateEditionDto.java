package com.library.LibraryApp.application.dto;

import com.library.LibraryApp.core.util.RegexPatterns;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateEditionDto {

    @Pattern(regexp = RegexPatterns.ISBN)
    private String isbn;

    @Min(value = 1)
    @Max(value = 1500)
    @NotNull
    private  Integer pages;

    @Past
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private   LocalDate publication;

    @Min(value = 1)
    @Max(value = 20)
    private   Integer number;

    @NotNull
    private UUID book;
}
