package ru.digitalchief.LibraryApp.dto;

import lombok.*;

import java.time.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDto {


    private Integer id;


    private Integer rack;


    private LocalDate accounting;


    private Boolean isTaken;


    private Long isbn;

}
