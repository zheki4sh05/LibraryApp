package ru.digitalchief.LibraryApp.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogDto {
    private Long id;

    private String udk;

    private String name;

    private String author;

    private Integer pages;

    private String year;
}
