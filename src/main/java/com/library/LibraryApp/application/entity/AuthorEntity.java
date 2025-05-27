package com.library.LibraryApp.application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "author")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorEntity {

    @Id
    private UUID id;

    @Column("name")
    private String name;

}
