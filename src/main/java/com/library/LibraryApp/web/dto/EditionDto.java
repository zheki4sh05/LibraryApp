package com.library.LibraryApp.web.dto;

import com.library.LibraryApp.core.util.RegexPatterns;
import com.library.LibraryApp.web.markers.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.service.annotation.*;

import java.time.LocalDate;


public record EditionDto(

        @Pattern(regexp = RegexPatterns.UUID, groups = {AdvancedInfo.class})
                @NotBlank(groups = {AdvancedInfo.class})
        String id,

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

        @NotBlank(groups = {AdvancedInfo.class, BasicInfo.class})
        @Pattern(regexp = RegexPatterns.UUID,groups = {AdvancedInfo.class, BasicInfo.class})
        String book

) {
}
