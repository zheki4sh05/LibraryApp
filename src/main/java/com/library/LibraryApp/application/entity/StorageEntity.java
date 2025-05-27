package com.library.LibraryApp.application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Table(name = "storage")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StorageEntity {
    @Id
    private UUID id;

    @Column("rack")
    private Integer rack;

    @Column("accounting")
    private LocalDate accounting;

    @Column("is_taken")
    private Boolean taken;

    @Column("book_edition_id")
    private UUID edition;

}
