package com.library.LibraryApp.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditionModel {


    private UUID id;
    private String isbn;
    private int pages;
    private LocalDate publication;
    private short number;
    private UUID bookId;

}
