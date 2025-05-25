package com.library.LibraryApp.web.dto;

import java.time.LocalDate;

public record SearchStorageDto
        (

                BookState mode,
                LocalDate dateFrom,
                LocalDate dateTo,
                Integer rack
        )
{
}
