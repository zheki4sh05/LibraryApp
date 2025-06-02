package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc;

import com.library.LibraryApp.application.dto.*;
import com.library.LibraryApp.application.entity.StorageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.*;
import java.util.*;

@Repository
public interface StorageR2dbcRepository extends R2dbcRepository<StorageEntity, UUID> {

    @Query("""
            select * from storage s
            join edition e on s.book_edition_id = :id and e.id = :id
            """)
    Flux<StorageEntity> findAllByEditionId(@Param("id") UUID uuid, Pageable pageable);

    Flux<StorageEntity> findByStatusAndAccountingBetweenAndRackBetweenAndEdition(String status, LocalDate dateFrom, LocalDate dateTo, Integer rackMin, Integer rackMax, UUID edition, Pageable pageable);
}
