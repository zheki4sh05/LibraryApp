package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc;


import com.library.LibraryApp.application.entity.BookEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.*;

@Repository
public interface BookR2dbcRepo extends R2dbcRepository<BookEntity, UUID> {

    @Query("""
    select * from book where catalog_fk = :catalogId and is_taken=false limit 1
    """)
    Mono<BookEntity> findFirstWithFreeBook(@Param("catalogId") Long isbn);

    @Query("""
    select * from book where id = :id
    """)
    Mono<BookEntity> findByNumber(@Param("id") UUID id);

    @Query("""
    select * from book where catalog_fk = :catalogId and is_taken=true
    """)
    Flux<BookEntity> findBorrowBook(Long catalogId);

    @Query("""
    select * from book where catalog_fk = :catalogId and is_taken=false
    """)
    Flux<BookEntity> findFreeBooks(Long catalogId);
}

