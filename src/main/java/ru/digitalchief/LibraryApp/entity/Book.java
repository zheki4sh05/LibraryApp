package ru.digitalchief.LibraryApp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "book")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Column(name = "number")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "rack")
    private Integer rack;

    @Column(name = "accounting")
    private LocalDate accounting;

    @Column(name = "is_taken")
    private Boolean isTaken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_fk")
    private Catalog catalogFk;

    public Book(Integer id, Integer rack, LocalDate accounting, Boolean isTaken) {
        this.id = id;
        this.rack = rack;
        this.accounting = accounting;
        this.isTaken = isTaken;
    }
}
