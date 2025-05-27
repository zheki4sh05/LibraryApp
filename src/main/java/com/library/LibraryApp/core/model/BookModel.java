package com.library.LibraryApp.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookModel {


    private UUID id;
    private String name;
    private String udk;
    private UUID author;
}
