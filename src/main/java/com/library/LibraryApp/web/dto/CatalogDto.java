package com.library.LibraryApp.web.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CatalogDto {

    private Long id;

    private String udk;

    private String name;

    private String author;

    private Integer pages;

    private String publication;
}
