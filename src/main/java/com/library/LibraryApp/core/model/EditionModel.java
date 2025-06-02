package com.library.LibraryApp.core.model;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EditionModel {


    private UUID id;
    private String isbn;
    private int pages;
    private LocalDate publication;
    private short number;
    private UUID book;

}
