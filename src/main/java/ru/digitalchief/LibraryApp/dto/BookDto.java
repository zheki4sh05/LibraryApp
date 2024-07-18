package ru.digitalchief.LibraryApp.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDto {


    private Integer id;


    private Integer rack;


    private String accounting;


    private Boolean isTaken;


    private Long isbn;

}
