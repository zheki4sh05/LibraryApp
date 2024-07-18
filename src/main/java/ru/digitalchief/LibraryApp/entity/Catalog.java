package ru.digitalchief.LibraryApp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "catalog")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catalog {

    @Column(name = "isbn")
    @Id
    private Long id;

    @Column(name = "udk")
    private String udk;

    @Column(name = "name")
    private String name;

    @Column(name = "author")
    private String author;

    @Column(name = "pages")
    private Integer pages;

    @Column(name = "year")
    private LocalDate year;

}
