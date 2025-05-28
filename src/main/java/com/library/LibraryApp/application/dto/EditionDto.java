package com.library.LibraryApp.application.dto;

import com.library.LibraryApp.core.util.RegexPatterns;
import com.library.LibraryApp.web.markers.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.service.annotation.*;

import java.time.LocalDate;
import java.util.UUID;


public record EditionDto(


        @NotNull(groups = {AdvancedInfo.class})
        UUID id,

        @Pattern(regexp = RegexPatterns.ISBN, groups = {AdvancedInfo.class, BasicInfo.class})
        String isbn,

        @Min(value = 1, groups = {AdvancedInfo.class, BasicInfo.class})
        @Max(value = 1500, groups = {AdvancedInfo.class, BasicInfo.class})
        @NotNull
        Integer pages,

        @Past(groups = {AdvancedInfo.class, BasicInfo.class})
        @DateTimeFormat(pattern = "yyyy-mm-dd")
        LocalDate publication,

        @Min(value = 1, groups = {AdvancedInfo.class, BasicInfo.class})
        @Max(value = 20, groups = {AdvancedInfo.class, BasicInfo.class})
        Integer number,

        @NotNull(groups = {AdvancedInfo.class, BasicInfo.class})
        UUID book

) {
}
