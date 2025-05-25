package com.library.LibraryApp.core.repository;


import com.library.LibraryApp.core.entity.Book;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.*;

@Repository
public interface BookRepository extends R2dbcRepository<Book, UUID> {

    @Query("""
    select * from book where catalog_fk = :catalogId and is_taken=false limit 1
    """)
    Mono<Book> findFirstWithFreeBook(@Param("catalogId") Long isbn);

    @Query("""
    select * from book where id = :id
    """)
    Mono<Book> findByNumber(@Param("id") UUID id);

    @Query("""
    select * from book where catalog_fk = :catalogId and is_taken=true
    """)
    Flux<Book> findBorrowBook(Long catalogId);

    @Query("""
    select * from book where catalog_fk = :catalogId and is_taken=false
    """)
    Flux<Book> findFreeBooks(Long catalogId);
}

