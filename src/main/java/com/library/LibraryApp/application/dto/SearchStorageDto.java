package com.library.LibraryApp.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record SearchStorageDto
        (

                @NotNull
                BookState mode,

                @NotNull
                @PastOrPresent
                LocalDate dateFrom,

                @NotNull
                @PastOrPresent
                LocalDate dateTo,

                @Min(1) @Max(100)
                Integer rack
        )


{

}
