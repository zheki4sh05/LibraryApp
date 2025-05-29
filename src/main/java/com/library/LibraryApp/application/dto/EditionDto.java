package com.library.LibraryApp.application.dto;

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
