package com.library.LibraryApp.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchStorageDto
{
    @NotNull
    BookState status;


    @PastOrPresent
    LocalDate dateFrom;


    @PastOrPresent
    LocalDate dateTo;

    @Min(1) @Max(100)
    Integer rack;

}
