package com.library.LibraryApp.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("edition")
public class EditionEntity {

    @Id
    private UUID id;
    private String isbn;
    private int pages;
    private LocalDate publication;
    private Integer number;
    @Column("book_id")
    private UUID book;

    public EditionEntity(String isbn, int pages, LocalDate publication, Integer number, UUID bookId) {
        this.isbn = isbn;
        this.pages = pages;
        this.publication = publication;
        this.number = number;
        this.book = bookId;
    }
}
