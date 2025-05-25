package com.library.LibraryApp.web.dto;

import java.time.LocalDate;

public record SearchEditionDto(
        String isbn, LocalDate publication, Integer number, String name
) {
}
