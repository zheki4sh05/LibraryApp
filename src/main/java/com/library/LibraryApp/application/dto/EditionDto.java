package com.library.LibraryApp.application.dto;

import com.library.LibraryApp.core.util.RegexPatterns;
import com.library.LibraryApp.web.markers.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.service.annotation.*;

import java.time.LocalDate;
import java.util.UUID;


public record EditionDto(



        UUID id,


        String isbn,


        Integer pages,


        LocalDate publication,


        Integer number,


        UUID book

) {
}
