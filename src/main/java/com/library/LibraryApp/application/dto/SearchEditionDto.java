package com.library.LibraryApp.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record SearchEditionDto(
        String isbn,

        @Past @DateTimeFormat(pattern = "yyyy-mm-dd")
        LocalDate publication,
        @Min(1)
        Integer number,
        @Size(max = 100)
        String name
) {
    public SearchEditionDto(){
        this("", LocalDate.now().minusDays(1), 1, "");
    }

}
