package com.library.LibraryApp.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("edition")
public class Edition {

    @Id
    private UUID id;
    private String isbn;
    private int pages;
    private LocalDate publication;
    private short number;
    private UUID bookId;

    public Edition(String isbn, int pages, LocalDate publication, short number, UUID bookId) {
        this.isbn = isbn;
        this.pages = pages;
        this.publication = publication;
        this.number = number;
        this.bookId = bookId;
    }
}
