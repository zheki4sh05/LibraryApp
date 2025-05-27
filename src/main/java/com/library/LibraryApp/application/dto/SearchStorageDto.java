package com.library.LibraryApp.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SearchStorageDto
        (

                @NotNull
                BookState mode,

                @NotNull
                LocalDate dateFrom,

                @NotNull
                LocalDate dateTo,

                @Min(1) @Max(100)
                Integer rack
        )


{
        public SearchStorageDto(){
                this(BookState.FREE, LocalDate.now().minusDays(1), LocalDate.now(), 1);
        }
}
