package com.library.LibraryApp.application.dto;


import jakarta.validation.constraints.Size;

public record SearchAuthorDto(
        @Size(max = 100)
        String name
) {
}
