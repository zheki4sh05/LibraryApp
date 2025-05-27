package com.library.LibraryApp.application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;


@Table(name = "book")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookEntity {

    @Id
    private UUID id;

    @Column("name")
    private String name;

    @Column("udk")
    private String udk;

    @Column("author_id")
    private UUID author;

    public BookEntity(String name, String udk, UUID author) {
        this.name = name;
        this.udk = udk;
        this.author = author;
    }
}
