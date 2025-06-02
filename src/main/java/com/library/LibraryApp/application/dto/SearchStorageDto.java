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
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchStorageDto
{
    @NotNull
    private BookState status;

    @PastOrPresent
    private LocalDate dateFrom = LocalDate.now();

    @PastOrPresent
    private LocalDate dateTo= LocalDate.now();

    @Min(1) @Max(100)
    private Integer rackMin = 1;

    @Min(1) @Max(100)
    private Integer rackMax = 100;

    @NotNull
    private UUID edition;

}
