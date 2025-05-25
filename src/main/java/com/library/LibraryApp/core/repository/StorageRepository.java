package com.library.LibraryApp.core.repository;

import com.library.LibraryApp.core.entity.Storage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface StorageRepository extends R2dbcRepository<Storage, UUID> {

    @Query("""
            
            select * from storage where book_id
            
            """)
    Mono<Storage> findByBookId(String id);


    @Query("""
            select * from storage s
            join edition e on s.book_edition_id = :id and e.id = :id
            """)
    Flux<Storage> findAllByEditionId(@Param("id") UUID uuid, Pageable pageable);
}
